
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.virtualpetgame;

/**
 *
 * @author Mitchell
 */
public class ManaPot extends Item {
    public ManaPot(int amount) { super("ManaPot", amount); }
    @Override
    public void use(Pet pet) { pet.setSpecialAmount(pet.getSpecialAmountMax()); }
    @Override
    public int getPrice() { return 20; }
}
