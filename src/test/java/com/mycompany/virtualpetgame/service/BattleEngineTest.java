package com.mycompany.virtualpetgame.service;

import com.mycompany.virtualpetgame.BattleResult;
import com.mycompany.virtualpetgame.Dog;
import com.mycompany.virtualpetgame.ItemFactory;
import com.mycompany.virtualpetgame.Pet;
import com.mycompany.virtualpetgame.PetFactory;
import com.mycompany.virtualpetgame.PetType;
import com.mycompany.virtualpetgame.User;
import org.junit.Test;
import static org.junit.Assert.*;

/** Tests for the battle rules (no database needed). */
public class BattleEngineTest {

    private Pet strongPet() {
        return PetFactory.createPet(PetType.CAT, "Kit", 100, 50, 3);
    }

    @Test
    public void killingTheEnemyWinsAndPaysTenCoins() {
        User user = new User("Bob", 0);
        BattleEngine engine = new BattleEngine(user, strongPet(), new Dog("Weak", 40, 5, 3));
        engine.playerAttack();
        assertTrue(engine.isFinished());
        assertEquals(BattleResult.WIN, engine.getResult());
        assertEquals(10, user.getCoins());
    }

    @Test
    public void deadEnemyDoesNotStrikeBack() {
        BattleEngine engine = new BattleEngine(new User("Bob", 0), strongPet(), new Dog("Weak", 40, 5, 3));
        String log = engine.playerAttack();
        assertFalse(log.contains("strikes back"));
    }

    @Test
    public void losingTakesCoinsButNeverBelowZero() {
        User user = new User("Bob", 5);
        Pet weak = PetFactory.createPet(PetType.DOG, "Rex", 10, 1, 3);
        BattleEngine engine = new BattleEngine(user, weak, new Dog("Big", 500, 100, 3));
        engine.playerAttack();
        assertEquals(BattleResult.LOSE, engine.getResult());
        assertEquals(0, user.getCoins());
        assertEquals(10, weak.getHealth());   // pet is restored after the battle
    }

    @Test
    public void runningEndsBattleWithNoCoinChange() {
        User user = new User("Bob", 7);
        BattleEngine engine = new BattleEngine(user, strongPet(), new Dog("Big", 500, 20, 3));
        engine.run();
        assertEquals(BattleResult.RAN, engine.getResult());
        assertEquals(7, user.getCoins());
    }

    @Test
    public void strengthPotionBuffIsRemovedWhenBattleEnds() {
        User user = new User("Bob", 0);
        user.addItem(ItemFactory.create("StrengthPot", 1));
        Pet pet = PetFactory.createPet(PetType.DOG, "Rex", 100, 15, 3);
        BattleEngine engine = new BattleEngine(user, pet, new Dog("Big", 500, 5, 3));
        engine.useItem("StrengthPot");
        assertEquals(30, pet.getAttack());
        engine.run();
        assertEquals(15, pet.getAttack());
        assertEquals(0, user.getItem("StrengthPot").getAmount());
    }

    @Test
    public void strengthPotionLastsTwoRounds() {
        User user = new User("Bob", 0);
        user.addItem(ItemFactory.create("StrengthPot", 1));
        Pet pet = PetFactory.createPet(PetType.DOG, "Rex", 1000, 10, 3);
        BattleEngine engine = new BattleEngine(user, pet, new Dog("Big", 5000, 1, 3));
        engine.useItem("StrengthPot");     // drunk in round 1
        engine.playerAttack();             // round 1: 20 damage
        assertEquals(20, pet.getAttack()); // still active in round 2
        engine.playerAttack();             // round 2: 20 damage
        assertEquals(10, pet.getAttack()); // worn off at the start of round 3
    }

    @Test
    public void specialWithNoManaKeepsTheTurn() {
        Pet pet = PetFactory.createPet(PetType.DOG, "Rex", 100, 10, 0);
        BattleEngine engine = new BattleEngine(new User("Bob", 0), pet, new Dog("Big", 500, 20, 3));
        engine.playerSpecial();
        assertEquals(1, engine.getRound());
        assertEquals(100, pet.getHealth());
    }

    @Test
    public void createdEnemyStaysWithinTheAllowedRanges() {
        Pet pet = PetFactory.createPet(PetType.DOG, "Rex", 100, 10, 3);
        java.util.Random random = new java.util.Random(42);
        java.util.Set<Integer> healths = new java.util.HashSet<>();
        for (int i = 0; i < 1000; i++) {
            Pet enemy = BattleEngine.createEnemy(pet, random);
            assertTrue(enemy.getMaxHealth() >= 50 && enemy.getMaxHealth() <= 200);
            assertTrue(enemy.getAttack() >= 5 && enemy.getAttack() <= 15);
            healths.add(enemy.getMaxHealth());
        }
        assertTrue(healths.size() > 50);   // really random, not one fixed value
    }
}
