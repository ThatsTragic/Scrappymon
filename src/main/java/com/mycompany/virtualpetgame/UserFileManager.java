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
import java.util.List;
import java.util.Map;

public class UserFileManager extends FileManagerBase {

    public UserFileManager() {
        super("users.txt");
    }

    public Map<String, User> loadUsers() {
        Map<String, User> users = new LinkedHashMap<>();
        for (String line : readLines()) {
            String[] parts = line.split(",");
            if (parts.length < 2 || parts.length > 3) {
                System.out.println("Skipping bad user line: " + line);
                continue;
            }
            try {
                String username = parts[0].trim();
                int coins = Math.max(0, Integer.parseInt(parts[1].trim()));
                if (username.isEmpty()) {
                    throw new IllegalArgumentException("empty username");
                }
                User user = new User(username, coins);
                if (parts.length == 3) {
                    user.setSavedActivePetName(parts[2].trim());
                }
                users.put(username.toLowerCase(), user);
            } catch (IllegalArgumentException e) {
                System.out.println("Skipping bad user line: " + line);
            }
        }
        return users;
    }

    public void saveUsers(Collection<User> users) {
        List<String> lines = new ArrayList<>();
        for (User user : users) {
            String active = user.getActivePet() == null ? "" : user.getActivePet().getName();
            lines.add(user.getUsername() + "," + user.getCoins() + "," + active);
        }
        writeLines(lines);
    }
}