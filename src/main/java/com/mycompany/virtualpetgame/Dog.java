/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.virtualpetgame;

/**
 *
 * @author Mitchell
 */
public class Dog extends Pet {
    public Dog(String name, int health, int attack, int specialAmount){
        super(name, health, attack,specialAmount);
    }
    @Override
    public void specialMove(Pet enemy){
        
        this.heal(this.getMaxHealth());
    }
    @Override
    public String specialMoveDesciption(){
        return "set current health to max health";
    }
}
