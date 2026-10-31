package com.mycompany.virtualpetgame;

import org.junit.Test;
import static org.junit.Assert.*;

/** Tests for the damage and healing rules in Pet. */
public class PetTest {

    @Test
    public void healNeverGoesAboveMaxHealth() {
        Pet pet = PetFactory.createPet(PetType.DOG, "Rex", 100, 10, 3);
        pet.takeDamage(10);
        pet.heal(50);
        assertEquals(100, pet.getHealth());
    }

    @Test
    public void takeDamageNeverGoesBelowZero() {
        Pet pet = PetFactory.createPet(PetType.CAT, "Kit", 100, 10, 3);
        pet.takeDamage(500);
        assertEquals(0, pet.getHealth());
        assertFalse(pet.isAlive());
    }

    @Test
    public void petFactoryBuildsTheRightSubclass() {
        assertTrue(PetFactory.createPet(PetType.DOG, "a", 100, 10, 3) instanceof Dog);
        assertTrue(PetFactory.createPet(PetType.CAT, "b", 100, 10, 3) instanceof Cat);
        assertTrue(PetFactory.createPet(PetType.SNAKE, "c", 100, 10, 3) instanceof Snake);
    }

    @Test
    public void spendCoinsRefusesWhenBalanceTooLow() {
        User user = new User("Bob", 5);
        assertFalse(user.spendCoins(20));
        assertEquals(5, user.getCoins());
    }
}
