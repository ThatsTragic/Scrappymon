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

public class ItemFileManager extends FileManagerBase {

    public ItemFileManager() {
        super("items.txt");
    }

    public void saveItems(Collection<User> users) {
        List<String> lines = new ArrayList<>();
        for (User user : users) {
            for (Item item : user.getItems()) {
                lines.add(user.getUsername() + "," + item.getName() + "," + item.getAmount());
            }
        }
        writeLines(lines);
    }

    public void loadItems(Map<String, User> users) {
        for (String line : readLines()) {
            try {
                String[] p = line.split(",");
                if (p.length != 3) {
                    throw new IllegalArgumentException("wrong number of fields");
                }
                User owner = users.get(p[0].trim().toLowerCase());
                if (owner == null) {
                    continue;
                }
                int amount = Integer.parseInt(p[2].trim());
                Item item = ItemFactory.create(p[1].trim(), amount);
                if (item == null || amount < 0) {
                    throw new IllegalArgumentException("unknown item");
                }
                owner.addItem(item);
            } catch (IllegalArgumentException e) {
                System.out.println("Skipping bad item line: " + line);
            }
        }
    }
}