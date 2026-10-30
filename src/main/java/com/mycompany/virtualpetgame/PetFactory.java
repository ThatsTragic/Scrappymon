/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.virtualpetgame;



/**
 *
 * @author Mitchell
 */
public class PetFactory {
    public static Pet createPet(PetType type, String name, int health, int attack, int specialAmount) {
        return switch (type) {
            case DOG -> new Dog(name, health, attack, specialAmount);
            case CAT -> new Cat(name, health, attack, specialAmount);
            case SNAKE -> new Snake(name, health, attack, specialAmount);
        };
    }
}

