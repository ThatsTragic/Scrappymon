package com.mycompany.virtualpetgame.service;

import com.mycompany.virtualpetgame.BattleResult;
import com.mycompany.virtualpetgame.Dog;
import com.mycompany.virtualpetgame.Item;
import com.mycompany.virtualpetgame.Pet;
import com.mycompany.virtualpetgame.StrengthPot;
import com.mycompany.virtualpetgame.User;
import java.util.Random;
/**
 *
 * @author Mitchell
 */
public class BattleEngine {
    private final User user;
    private final Pet pet;
    private final Pet enemy;
    private int round = 1;
    private int buffAmount = 0;
    private int buffEndRound = 0;
    private BattleResult result = null;   // null while the battle is still going
    private static final Random RANDOM = new Random();

    public BattleEngine(User user, Pet pet, Pet enemy) {
        this.user = user;
        this.pet = pet;
        this.enemy = enemy;
    }

   

    // Builds a random enemy based on the pet's stats. 
    public static Pet createEnemy(Pet pet) {
        return createEnemy(pet, RANDOM);
    }

    //Enemy health is 0.5x to 4x the pet's max health and enemy attack is 0.5x to 2x the pet's attack. Taking the Random as a parameter lets the unit tests use a fixed seed.
    public static Pet createEnemy(Pet pet, Random random) {
        double healthFactor = 0.5 + random.nextDouble() * 1.5;   // 0.5 to 2.0
        double attackFactor = 0.5 + random.nextDouble() * 1.0;   // 0.5 to 1.5
        int health = Math.max(1, (int) Math.round(pet.getMaxHealth() * healthFactor));
        int attack = Math.max(1, (int) Math.round(pet.getAttack() * attackFactor));
        return new Dog("Bal", health, attack, 3);
    }

    public Pet getPet() { return pet; }
    public Pet getEnemy() { return enemy; }
    public int getRound() { return round; }
    public boolean isFinished() { return result != null; }
    public BattleResult getResult() { return result; }

    // Basic attack. Uses up the turn. 
    public String playerAttack() {
        if (isFinished()) {
            return "The battle is over.";
        }
        int damage = pet.getAttack();
        pet.scrap(enemy);
        return endTurn(pet.getName() + " attacks " + enemy.getName() + " for " + damage + " damage.");
    }

    // Special move. If the pet has none left, nothing happens and the turn is kept. 
    public String playerSpecial() {
        if (isFinished()) {
            return "The battle is over.";
        }
        if (pet.getSpecialAmount() <= 0) {
            return pet.getName() + " has no special moves left.";
        }
        pet.specialMove(enemy);
        pet.addSpecialAmount(-1);
        return endTurn(pet.getName() + " uses its special move: " + pet.specialMoveDesciption() + ".");
    }

    // Uses one potion. This does not use up the turn. 
    public String useItem(String itemName) {
        if (isFinished()) {
            return "The battle is over.";
        }
        Item item = user.getItem(itemName);
        if (item == null || item.getAmount() <= 0) {
            return "You have no " + itemName + " left.";
        }
        if (item instanceof StrengthPot && buffAmount > 0) {
            return "A strength potion is already active.";
        }
        int attackBefore = pet.getAttack();
        item.use(pet);
        item.addAmount(-1);
        if (item instanceof StrengthPot) {
            buffAmount = pet.getAttack() - attackBefore;
            buffEndRound = round + 2;
        }
        return "You used a " + itemName + ".";
    }

    // Runs away. No coins are won or lost.
    public String run() {
        if (isFinished()) {
            return "The battle is over.";
        }
        return finish(BattleResult.RAN, "You ran away.");
    }

    private String endTurn(String actionText) {
        StringBuilder text = new StringBuilder(actionText);
        if (!enemy.isAlive()) {
            return finish(BattleResult.WIN, text.append("\n").append("You WON! +10 coins").toString());
        }
        int damage = enemy.getAttack();
        enemy.scrap(pet);
        text.append("\n").append(enemy.getName()).append(" strikes back for ")
                .append(damage).append(" damage.");
        if (!pet.isAlive()) {
            return finish(BattleResult.LOSE, text.append("\n").append("You LOST. -10 coins").toString());
        }
        round++;
        if (buffAmount > 0 && round >= buffEndRound) {
            removeBuff();
            text.append("\nYour strength potion wore off.");
        }
        return text.toString();
    }

    private String finish(BattleResult outcome, String text) {
        result = outcome;
        removeBuff();
        if (outcome == BattleResult.WIN) {
            user.addCoins(10);
        } else if (outcome == BattleResult.LOSE) {
            user.setCoins(Math.max(0, user.getCoins() - 10));
        }
        pet.setHealth(pet.getMaxHealth());
        pet.setSpecialAmount(pet.getSpecialAmountMax());
        return text;
    }

    private void removeBuff() {
        if (buffAmount > 0) {
            pet.addAttack(-buffAmount);
            buffAmount = 0;
        }
    }
}
