/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.virtualpetgame;

/**
 *
 * @author Mitchell
 */
public class HealPot extends Item {
    public HealPot(int amount) { super("HealPot", amount); }
    @Override
    public void use(Pet pet) { pet.heal(50); }
    @Override
    public int getPrice() { return 5; }
}
