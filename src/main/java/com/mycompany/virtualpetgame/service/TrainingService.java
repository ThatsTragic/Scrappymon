/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.virtualpetgame.service;


import com.mycompany.virtualpetgame.AgilityTraining;
import com.mycompany.virtualpetgame.EnduranceTraining;
import com.mycompany.virtualpetgame.Pet;
import com.mycompany.virtualpetgame.StrengthTraining;
import com.mycompany.virtualpetgame.TrainingMethod;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Mitchell
 */
public class TrainingService {
    private final Map<String, TrainingMethod> methods = new LinkedHashMap<>();

    public TrainingService() {
        methods.put("strength", new StrengthTraining());
        methods.put("agility", new AgilityTraining());
        methods.put("endurance", new EnduranceTraining());
    }

    /** Trains the pet. Returns a message describing the result. */
    public String train(String kind, Pet pet) {
        TrainingMethod method = methods.get(kind.toLowerCase());
        if (method == null) {
            throw new IllegalArgumentException("Unknown training: " + kind);
        }
        method.train(pet);
        return kind.substring(0, 1).toUpperCase() + kind.substring(1).toLowerCase()
                + " training complete! " + pet.getStatsDisplay();
    }
}
