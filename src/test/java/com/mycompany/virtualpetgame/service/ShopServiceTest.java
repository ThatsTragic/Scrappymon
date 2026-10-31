package com.mycompany.virtualpetgame.service;

import com.mycompany.virtualpetgame.Pet;
import com.mycompany.virtualpetgame.PetFactory;
import com.mycompany.virtualpetgame.PetType;
import com.mycompany.virtualpetgame.User;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 *
 * @author Mitchell
 */
public class ShopServiceTest {
    private final ShopService shop = new ShopService();

    @Test
    public void buyItemTakesCoinsAndAddsItem() {
        User user = new User("Bob", 20);
        shop.buyItem(user, "HealPot");
        assertEquals(15, user.getCoins());
        assertEquals(1, user.getItem("HealPot").getAmount());
    }

    @Test
    public void buyItemRefusesWhenTooPoorAndKeepsCoins() {
        User user = new User("Bob", 4);
        try {
            shop.buyItem(user, "HealPot");
            fail("expected a refusal");
        } catch (IllegalArgumentException e) {
            assertEquals(4, user.getCoins());
            assertNull(user.getItem("HealPot"));
        }
    }

    @Test
    public void upgradeRefusesWithoutCoinsAndNeverGoesNegative() {
        User user = new User("Bob", 0);
        Pet pet = PetFactory.createPet(PetType.DOG, "Rex", 100, 10, 3);
        try {
            shop.upgradePet(user, pet);
            fail("expected a refusal");
        } catch (IllegalArgumentException e) {
            assertEquals(0, user.getCoins());
            assertEquals(3, pet.getSpecialAmountMax());
        }
    }

    @Test
    public void upgradeRaisesSpecialAmountAndCostsCoins() {
        User user = new User("Bob", 25);
        Pet pet = PetFactory.createPet(PetType.DOG, "Rex", 100, 10, 3);
        shop.upgradePet(user, pet);
        assertEquals(4, pet.getSpecialAmountMax());
        assertEquals(5, user.getCoins());
    }

    @Test
    public void buyPetRejectsDuplicateNameAndBadName() {
        User user = new User("Bob", 200);
        user.addPet(PetFactory.createPet(PetType.DOG, "Rex", 100, 10, 3));
        try {
            shop.buyPet(user, PetType.CAT, "rex");
            fail("duplicate name should be refused");
        } catch (IllegalArgumentException e) {
            assertEquals(200, user.getCoins());
        }
        try {
            shop.buyPet(user, PetType.CAT, "bad,name");
            fail("comma name should be refused");
        } catch (IllegalArgumentException e) {
            assertEquals(1, user.getPets().size());
        }
    }

    @Test
    public void buyPetAddsPetAndCostsFiftyCoins() {
        User user = new User("Bob", 60);
        shop.buyPet(user, PetType.SNAKE, "Sly");
        assertEquals(10, user.getCoins());
        assertEquals(1, user.getPets().size());
    }
}
