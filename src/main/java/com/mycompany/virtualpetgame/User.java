/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.virtualpetgame;

/**
 *
 * @author Mitchell
 */


import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class User {
    private String username;
    private int coins;
    private final ArrayList<Pet> pets;
    private final Map<String, Item> items;
    private Pet activePet;
    private String savedActivePetName;

    public User(String username, int coins) {
        this.username = username;
        this.coins = coins;
        this.pets = new ArrayList<>();
        this.items = new LinkedHashMap<>();
    }

    public String getUsername() { return username; }
    public int getCoins() { return coins; }
    public Pet getActivePet() { return activePet; }
    public ArrayList<Pet> getPets() { return pets; }
    public Collection<Item> getItems() { return items.values(); }
    public void setUsername(String username) { this.username = username; }
    public void setCoins(int coins) { this.coins = coins; }
    public void setActivePet(Pet pet) { this.activePet = pet; }
    public void setSavedActivePetName(String name) { this.savedActivePetName = name; }
    public void addPet(Pet pet) { pets.add(pet); }
    public void addCoins(int amount) { this.coins += amount; }

    // Returns the item with this name, or null if the user does not own it. 
    public Item getItem(String name) { return items.get(name); }

    // Adds an item, merging the amount into an existing stack of the same kind. 
    public void addItem(Item item) {
        Item existing = items.get(item.getName());
        if (existing != null) {
            existing.addAmount(item.getAmount());
        } else {
            items.put(item.getName(), item);
        }
    }

    // Spends coins only if the user can afford it. Returns whether it worked. 
    public boolean spendCoins(int cost) {
        if (coins < cost) {
            return false;
        }
        coins -= cost;
        return true;
    }

    // Picks the pet saved as active last time, otherwise the first pet. 
    public void resolveActivePet() {
        activePet = null;
        for (Pet p : pets) {
            if (p.getName().equalsIgnoreCase(savedActivePetName)) {
                activePet = p;
                break;
            }
        }
        if (activePet == null && !pets.isEmpty()) {
            activePet = pets.get(0);
        }
    }
}
