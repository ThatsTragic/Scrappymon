/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.virtualpetgame;

/**
 *
 * @author Mitchell
 */
public class Cat extends Pet {
    public Cat(String name, int health, int attack, int specialAmount){
        super(name, health, attack,specialAmount);
    }
    @Override
    public void specialMove(Pet enemy){
       this.scrap(enemy);
       this.scrap(enemy);
       this.scrap(enemy);
    }
    @Override
    public String specialMoveDesciption(){
        return "attack 3 times in one turn";
    }
}
