/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.virtualpetgame;

/**
 *
 * @author Mitchell
 */


public class Battle {
    private final InputReader in;

    public Battle(InputReader in) {
        this.in = in;
    }

    public BattleResult fight(User user, Pet pet, Pet enemy) {
        int round = 1;
        int buffAmount = 0;      // attack added by a strength potion (0 = no buff)
        int buffEndRound = 0;
        System.out.println(pet.getName() + " vs " + enemy.getName());
        try {
            while (pet.isAlive() && enemy.isAlive()) {
                if (buffAmount > 0 && round >= buffEndRound) {
                    pet.addAttack(-buffAmount);
                    buffAmount = 0;
                    System.out.println("Your strength potion wore off.");
                }
                System.out.println("\nRound: " + round);
                System.out.println("Pet:   " + pet.getStatsDisplay());
                System.out.println("Enemy: " + enemy.getStatsDisplay());

                boolean turnTaken = false;
                while (!turnTaken) {
                    System.out.println("Press a to attack, i for items, s for special ("
                            + pet.specialMoveDesciption() + ") or r to run");
                    String input = in.readLine();
                    if (input.equalsIgnoreCase("a")) {
                        pet.scrap(enemy);
                        turnTaken = true;
                    } else if (input.equalsIgnoreCase("s")) {
                        if (pet.getSpecialAmount() > 0) {
                            pet.specialMove(enemy);
                            pet.addSpecialAmount(-1);
                            turnTaken = true;
                        } else {
                            System.out.println("You don't have enough special mana.");
                        }
                    } else if (input.equalsIgnoreCase("i")) {
                        int bonus = itemMenu(user, pet, buffAmount > 0);
                        if (bonus > 0) {
                            buffAmount = bonus;
                            buffEndRound = round + 2;
                        }
                    } else if (input.equalsIgnoreCase("r") || input.equalsIgnoreCase("x")) {
                        return BattleResult.RAN;
                    } else {
                        System.out.println("Incorrect input. Please press a, i, s or r.");
                    }
                }
                if (enemy.isAlive()) {
                    enemy.scrap(pet);
                }
                round++;
            }
            return pet.isAlive() ? BattleResult.WIN : BattleResult.LOSE;
        } finally {
            if (buffAmount > 0) {
                pet.addAttack(-buffAmount);
            }
        }
    }

    private int itemMenu(User user, Pet pet, boolean buffActive) {
        int bonus = 0;
        while (true) {
            System.out.println("Press h to heal (+50 hp), s for strength (2x attack), "
                    + "m for mana (restore special) or x to go back");
            System.out.println("Current items:");
            if (user.getItems().isEmpty()) {
                System.out.println("  (none)");
            }
            for (Item item : user.getItems()) {
                System.out.println("  " + item.getName() + ": " + item.getAmount());
            }
            String input = in.readLine();
            if (input.equalsIgnoreCase("h")) {
                useItem(user, "HealPot", pet, "health");
            } else if (input.equalsIgnoreCase("m")) {
                useItem(user, "ManaPot", pet, "mana");
            } else if (input.equalsIgnoreCase("s")) {
                if (buffActive || bonus > 0) {
                    System.out.println("A strength potion is already active.");
                } else {
                    int before = pet.getAttack();
                    if (useItem(user, "StrengthPot", pet, "strength")) {
                        bonus = pet.getAttack() - before;
                    }
                }
            } else if (input.equalsIgnoreCase("x")) {
                return bonus;
            } else {
                System.out.println("Incorrect input. Please press h, s, m or x.");
            }
        }
    }

    private boolean useItem(User user, String itemName, Pet pet, String label) {
        Item item = user.getItem(itemName);
        if (item == null || item.getAmount() <= 0) {
            System.out.println("You don't have any " + label + " potions left.");
            return false;
        }
        item.use(pet);
        item.addAmount(-1);
        return true;
    }
}