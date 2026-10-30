/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.virtualpetgame;

/**
 *
 * @author Mitchell
 */
public abstract class Item {
    private String name;
    private int amount;

    public Item(String name, int amount) {
        this.name = name;
        this.amount = amount;
    }
    public String getName() { return this.name; }
    public int getAmount() { return this.amount; }
    public void setName(String name) { this.name = name; }
    public void setAmount(int amount) { this.amount = amount; }
    public void addAmount(int amount) { this.amount += amount; }

    public abstract void use(Pet pet);
    public abstract int getPrice();
}