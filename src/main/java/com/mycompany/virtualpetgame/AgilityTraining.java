/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.virtualpetgame;

/**
 *
 * @author Mitchell
 */
public class AgilityTraining implements TrainingMethod{
    
    @Override
    public void train(Pet pet){
        pet.addMaxHealth(5);
        pet.addAttack(5);
        
    }
}
