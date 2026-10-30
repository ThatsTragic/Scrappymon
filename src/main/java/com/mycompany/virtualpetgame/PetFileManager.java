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
import java.util.List;
import java.util.Map;

public class PetFileManager extends FileManagerBase {

    public PetFileManager() {
        super("pets.txt");
    }

    public void savePets(Collection<User> users) {
        List<String> lines = new ArrayList<>();
        for (User user : users) {
            for (Pet pet : user.getPets()) {
                lines.add(user.getUsername() + "," + pet.getType() + "," + pet.getName()
                        + "," + pet.getHealth() + "," + pet.getMaxHealth()
                        + "," + pet.getAttack()
                        + "," + pet.getSpecialAmount() + "," + pet.getSpecialAmountMax());
            }
        }
        writeLines(lines);
    }

    public void loadPets(Map<String, User> users) {
        for (String line : readLines()) {
            try {
                String[] p = line.split(",");
                if (p.length != 6 && p.length != 8) {
                    throw new IllegalArgumentException("wrong number of fields");
                }
                User owner = users.get(p[0].trim().toLowerCase());
                if (owner == null) {
                    continue;
                }
                PetType type = PetType.valueOf(p[1].trim().toUpperCase());
                String name = p[2].trim();
                int health;
                int maxHealth;
                int attack;
                int special;
                int specialMax;
                if (p.length == 8) {
                    health = Integer.parseInt(p[3].trim());
                    maxHealth = Integer.parseInt(p[4].trim());
                    attack = Integer.parseInt(p[5].trim());
                    special = Integer.parseInt(p[6].trim());
                    specialMax = Integer.parseInt(p[7].trim());
                } else {
                    health = Integer.parseInt(p[3].trim());
                    maxHealth = health;
                    attack = Integer.parseInt(p[4].trim());
                    special = Integer.parseInt(p[5].trim());
                    specialMax = special;
                }
                if (name.isEmpty() || maxHealth <= 0 || attack < 0 || specialMax < 0) {
                    throw new IllegalArgumentException("invalid values");
                }
                Pet pet = PetFactory.createPet(type, name, maxHealth, attack, specialMax);
                pet.setHealth(maxHealth);
                pet.setSpecialAmount(specialMax);
                owner.addPet(pet);
            } catch (IllegalArgumentException e) {
                System.out.println("Skipping bad pet line: " + line);
            }
        }
    }
}