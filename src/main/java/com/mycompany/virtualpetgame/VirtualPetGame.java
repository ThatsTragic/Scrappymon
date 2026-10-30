/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.virtualpetgame;

/**
 *
 * @author Mitchell
 */


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class VirtualPetGame {
    private static final int NEW_PET_COST = 50;
    private static final int UPGRADE_COST = 20;
    private static final String INVALID = "Incorrect input. Please choose one of the listed keys.";

    private final InputReader in = new InputReader();
    private final UserFileManager userManager = new UserFileManager();
    private final PetFileManager petManager = new PetFileManager();
    private final ItemFileManager itemManager = new ItemFileManager();
    private Map<String, User> users = new LinkedHashMap<>();
    private User currentUser;

    public static void main(String[] args) {
        new VirtualPetGame().run();
    }

    private void run() {
        users = userManager.loadUsers();
        petManager.loadPets(users);
        itemManager.loadItems(users);
        System.out.println("Welcome to Scrappymon");
        login();
        mainMenu();
    }

    private void saveAll() {
        userManager.saveUsers(users.values());
        petManager.savePets(users.values());
        itemManager.saveItems(users.values());
    }

    // ---------------------------------------------------------------- login

    private void login() {
        String name = in.readName("Please enter a username: ");
        currentUser = users.get(name.toLowerCase());
        if (currentUser == null) {
            System.out.println("New user made");
            currentUser = new User(name, 0);
            users.put(name.toLowerCase(), currentUser);
            giveStarterPet();
            saveAll();
        } else {
            System.out.println("Welcome back " + currentUser.getUsername());
            currentUser.resolveActivePet();
            if (currentUser.getActivePet() == null) {
                System.out.println("You don't have a pet yet, so let's pick one.");
                giveStarterPet();
                saveAll();
            }
        }
    }

    private void giveStarterPet() {
        Pet starter = null;
        while (starter == null) {
            starter = createPetInteractive(false);
        }
        currentUser.addPet(starter);
        currentUser.setActivePet(starter);
    }

    private Pet createPetInteractive(boolean allowCancel) {
        while (true) {
            System.out.println("Pet type:\nPress d for dog, s for snake or c for cat"
                    + (allowCancel ? " or x to cancel" : ""));
            String input = in.readLine();
            PetType type = null;
            if (input.equalsIgnoreCase("d")) {
                type = PetType.DOG;
            } else if (input.equalsIgnoreCase("s")) {
                type = PetType.SNAKE;
            } else if (input.equalsIgnoreCase("c")) {
                type = PetType.CAT;
            } else if (allowCancel && input.equalsIgnoreCase("x")) {
                return null;
            } else {
                System.out.println(INVALID);
            }
            if (type != null) {
                String name = in.readName("Pet name: ");
                return PetFactory.createPet(type, name, 100, 10, 3);
            }
        }
    }

    // ----------------------------------------------------------- main menu

    private void mainMenu() {
        boolean running = true;
        while (running) {
            Pet pet = currentUser.getActivePet();
            System.out.println("\nCoins: " + currentUser.getCoins());
            System.out.println(pet.getStatsDisplay());
            System.out.println("Press t for training, b for a battle, s for shop, p for pets or x to exit");
            String input = in.readLine().toLowerCase();
            switch (input) {
                case "t" -> trainingMenu(pet);
                case "b" -> battleMenu(pet);
                case "s" -> shopMenu();
                case "p" -> petsMenu();
                case "x" -> {
                    saveAll();
                    System.out.println("Progress saved. Goodbye!");
                    running = false;
                }
                default -> System.out.println(INVALID);
            }
        }
    }

    // ------------------------------------------------------------ training

    private void trainingMenu(Pet pet) {
        while (true) {
            System.out.println("Press s for strength, a for agility, e for endurance or x to go back");
            String input = in.readLine();
            TrainingMethod training = null;
            if (input.equalsIgnoreCase("s")) {
                training = new StrengthTraining();
            } else if (input.equalsIgnoreCase("a")) {
                training = new AgilityTraining();
            } else if (input.equalsIgnoreCase("e")) {
                training = new EnduranceTraining();
            } else if (input.equalsIgnoreCase("x")) {
                return;
            } else {
                System.out.println("Incorrect input.");
            }
            if (training != null) {
                training.train(pet);
                System.out.println("Training complete! " + pet.getStatsDisplay());
                saveAll();
                return;
            }
        }
    }

    // -------------------------------------------------------------- battle

    private void battleMenu(Pet pet) {
        Pet enemy = new Dog("bal", 200, 20, 3);
        BattleResult result = new Battle(in).fight(currentUser, pet, enemy);
        switch (result) {
            case WIN -> {
                System.out.println("You WON! +10 coins");
                currentUser.addCoins(10);
            }
            case LOSE -> {
                System.out.println("You Lost. -10 coins");
                currentUser.setCoins(Math.max(0, currentUser.getCoins() - 10));
            }
            case RAN -> System.out.println("You Ran");
        }
        pet.setHealth(pet.getMaxHealth());
        pet.setSpecialAmount(pet.getSpecialAmountMax());
        saveAll();
    }

    // ---------------------------------------------------------------- shop

    private void shopMenu() {
        while (true) {
            System.out.println("\nWelcome to the shop, you have " + currentUser.getCoins() + " coins:");
            if (currentUser.getItems().isEmpty()) {
                System.out.println("  (you own no items)");
            }
            for (Item item : currentUser.getItems()) {
                System.out.println("  " + item.getName() + ": " + item.getAmount());
            }
            System.out.println("Press h for health potion (5 coins), s for strength potion (10 coins), "
                    + "m for mana potion (20 coins) or x to leave");
            String input = in.readLine();
            if (input.equalsIgnoreCase("h")) {
                buyItem("HealPot");
            } else if (input.equalsIgnoreCase("s")) {
                buyItem("StrengthPot");
            } else if (input.equalsIgnoreCase("m")) {
                buyItem("ManaPot");
            } else if (input.equalsIgnoreCase("x")) {
                saveAll();
                return;
            } else {
                System.out.println(INVALID);
            }
        }
    }

    private void buyItem(String itemName) {
        Item item = ItemFactory.create(itemName, 1);
        if (currentUser.spendCoins(item.getPrice())) {
            currentUser.addItem(item);
            System.out.println("Bought 1 " + itemName + ".");
        } else {
            System.out.println("You don't have enough coins (" + itemName + " costs "
                    + item.getPrice() + ").");
        }
    }

    // ---------------------------------------------------------------- pets

    private void petsMenu() {
        Pet pet = currentUser.getActivePet();
        System.out.println("Coins: " + currentUser.getCoins());
        System.out.println("Current Pet: " + pet.getName());
        while (true) {
            System.out.println("Press b to buy new pets, c to change pets, u to upgrade or x to leave");
            String input = in.readLine();
            if (input.equalsIgnoreCase("b")) {
                buyPet();
                break;
            } else if (input.equalsIgnoreCase("c")) {
                changePet();
                break;
            } else if (input.equalsIgnoreCase("u")) {
                upgradePet(pet);
                break;
            } else if (input.equalsIgnoreCase("x")) {
                break;
            } else {
                System.out.println(INVALID);
            }
        }
        saveAll();
    }

    private void buyPet() {
        if (currentUser.getCoins() < NEW_PET_COST) {
            System.out.println("A new pet costs " + NEW_PET_COST + " coins and you only have "
                    + currentUser.getCoins() + ".");
            return;
        }
        while (true) {
            System.out.println("Coins: " + currentUser.getCoins()
                    + "\nA new pet is " + NEW_PET_COST + " coins, press b to buy or x to leave");
            String input = in.readLine();
            if (input.equalsIgnoreCase("x")) {
                return;
            } else if (input.equalsIgnoreCase("b")) {
                Pet newPet = createPetInteractive(true);
                if (newPet != null) {
                    currentUser.spendCoins(NEW_PET_COST);
                    currentUser.addPet(newPet);
                    System.out.println("You bought " + newPet.getName() + "!");
                }
                return;
            } else {
                System.out.println(INVALID);
            }
        }
    }

    private void changePet() {
        ArrayList<Pet> pets = currentUser.getPets();
        for (int i = 0; i < pets.size(); i++) {
            System.out.println("Pet " + (i + 1) + ": " + pets.get(i).getStatsDisplay());
        }
        while (true) {
            System.out.println("Please select which pet using numbers, or x to cancel");
            String input = in.readLine();
            if (input.equalsIgnoreCase("x")) {
                return;
            }
            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= pets.size()) {
                    currentUser.setActivePet(pets.get(choice - 1));
                    System.out.println("Active pet is now " + pets.get(choice - 1).getName());
                    return;
                }
                System.out.println("Incorrect input: number out of range.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: please type a number.");
            }
        }
    }

    private void upgradePet(Pet pet) {
        while (true) {
            System.out.println("Coins: " + currentUser.getCoins() + "\nIt costs " + UPGRADE_COST
                    + " coins to increase special amount by 1, press b to buy or x to exit");
            String input = in.readLine();
            if (input.equalsIgnoreCase("b")) {
                if (currentUser.spendCoins(UPGRADE_COST)) {
                    pet.addSpecialAmountMax(1);
                    pet.setSpecialAmount(pet.getSpecialAmountMax());
                    System.out.println("Upgraded! Special amount is now " + pet.getSpecialAmountMax());
                } else {
                    System.out.println("You don't have enough coins.");
                }
                return;
            } else if (input.equalsIgnoreCase("x")) {
                return;
            } else {
                System.out.println(INVALID);
            }
        }
    }
}
