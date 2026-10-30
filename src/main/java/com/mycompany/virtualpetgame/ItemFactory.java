/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.virtualpetgame;

/**
 *
 * @author Mitchell
 */


public class ItemFactory {
    public static Item create(String name, int amount) {
        return switch (name) {
            case "HealPot" -> new HealPot(amount);
            case "StrengthPot" -> new StrengthPot(amount);
            case "ManaPot" -> new ManaPot(amount);
            default -> null;
        };
    }
}
