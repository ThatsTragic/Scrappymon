/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.virtualpetgame;

/**
 *
 * @author Mitchell
 */
public class StrengthPot extends Item {
    public StrengthPot(int amount) { super("StrengthPot", amount); }
    @Override
    public void use(Pet pet) { pet.addAttack(pet.getAttack()); }   // doubles attack
    @Override
    public int getPrice() { return 10; }
}