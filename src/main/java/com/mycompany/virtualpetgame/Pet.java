/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.virtualpetgame;

/**
 *
 * @author Mitchell
 */


public abstract class Pet {
    private String name;
    private int health;
    private int attack;
    private int maxHealth;
    private int specialAmount;
    private int specialAmountMax;

    public Pet(String name, int health, int attack, int specialAmount) {
        this.attack = attack;
        this.maxHealth = health;
        this.health = health;
        this.name = name;
        this.specialAmount = specialAmount;
        this.specialAmountMax = specialAmount;
    }

    public String getName() { return this.name; }
    public int getHealth() { return this.health; }
    public int getMaxHealth() { return this.maxHealth; }
    public int getAttack() { return this.attack; }
    public String getType() { return this.getClass().getSimpleName(); }
    public int getSpecialAmount() { return this.specialAmount; }
    public int getSpecialAmountMax() { return this.specialAmountMax; }
    public boolean isAlive() { return this.health > 0; }

    // One-line summary of the pet for menus and battles.
    public String getStatsDisplay() {
        return name + " | Type: " + getType()
                + " | Health: " + health + "/" + maxHealth
                + " | Attack: " + attack
                + " | Special: " + specialAmount + "/" + specialAmountMax;
    }

    public void setName(String name) { this.name = name; }
    public void setHealth(int health) { this.health = Math.max(0, Math.min(health, maxHealth)); }
    public void setHealthMax(int health) { this.maxHealth = health; }
    public void setAttack(int attack) { this.attack = attack; }
    public void setSpecialAmount(int amount) { this.specialAmount = amount; }
    public void setSpecialAmountMax(int amount) { this.specialAmountMax = amount; }

    // Raises max health and gives the pet the same amount of current health.
    public void addMaxHealth(int amount) {
        this.maxHealth += amount;
        this.health += amount;
    }
    public void addAttack(int amount) { this.attack += amount; }
    public void addSpecialAmount(int amount) { this.specialAmount += amount; }
    public void addSpecialAmountMax(int amount) { this.specialAmountMax += amount; }

    // Removes health, never dropping below 0. 
    public void takeDamage(int amount) {
        this.health = Math.max(0, this.health - amount);
    }

    // Restores health, never going above max health. 
    public void heal(int amount) {
        this.health = Math.min(this.maxHealth, this.health + amount);
    }

    // Basic attack. 
    public void scrap(Pet enemy) {
        enemy.takeDamage(this.getAttack());
    }

    public abstract void specialMove(Pet enemy);
    public abstract String specialMoveDesciption();
}