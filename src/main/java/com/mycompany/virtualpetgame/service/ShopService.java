package com.mycompany.virtualpetgame.service;

import com.mycompany.virtualpetgame.InputReader;
import com.mycompany.virtualpetgame.Item;
import com.mycompany.virtualpetgame.ItemFactory;
import com.mycompany.virtualpetgame.Pet;
import com.mycompany.virtualpetgame.PetFactory;
import com.mycompany.virtualpetgame.PetType;
import com.mycompany.virtualpetgame.User;

/**
 *
 * @author Mitchell
 */
public class ShopService {
    public static final int NEW_PET_COST = 50;
    public static final int UPGRADE_COST = 20;

    // Buys one potion. Returns a message describing the purchase. 
    public String buyItem(User user, String itemName) {
        Item item = ItemFactory.create(itemName, 1);
        if (item == null) {
            throw new IllegalArgumentException("Unknown item: " + itemName);
        }
        if (!user.spendCoins(item.getPrice())) {
            throw new IllegalArgumentException("Not enough coins: " + itemName
                    + " costs " + item.getPrice() + " and you have " + user.getCoins() + ".");
        }
        user.addItem(item);
        return "Bought 1 " + itemName + ".";
    }

    // Buys a new pet. The name must be valid and not already used by this player. 
    public Pet buyPet(User user, PetType type, String petName) {
        String name = petName == null ? "" : petName.trim();
        if (!InputReader.isValidName(name)) {
            throw new IllegalArgumentException(
                    "Pet name must be 1-20 letters, numbers, spaces, '-' or '_'.");
        }
        for (Pet existing : user.getPets()) {
            if (existing.getName().equalsIgnoreCase(name)) {
                throw new IllegalArgumentException("You already have a pet called " + name + ".");
            }
        }
        if (!user.spendCoins(NEW_PET_COST)) {
            throw new IllegalArgumentException("Not enough coins: a new pet costs "
                    + NEW_PET_COST + " and you have " + user.getCoins() + ".");
        }
        Pet pet = PetFactory.createPet(type, name, 100, 10, 3);
        user.addPet(pet);
        return pet;
    }

    // Adds one special move to the pet. 
    public String upgradePet(User user, Pet pet) {
        if (!user.spendCoins(UPGRADE_COST)) {
            throw new IllegalArgumentException("Not enough coins: an upgrade costs "
                    + UPGRADE_COST + " and you have " + user.getCoins() + ".");
        }
        pet.addSpecialAmountMax(1);
        pet.setSpecialAmount(pet.getSpecialAmountMax());
        return pet.getName() + " can now use " + pet.getSpecialAmountMax() + " special moves.";
    }
}
