/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.virtualpetgame.data;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.mycompany.virtualpetgame.Pet;
import com.mycompany.virtualpetgame.PetFactory;
import com.mycompany.virtualpetgame.PetType;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Mitchell
 */
public class PetDAO {
    private Connection connection() throws SQLException {
        return DatabaseManager.getInstance().getConnection();
    }
    public List<Pet> findByOwner(String owner){
        List<Pet> pets = new ArrayList<>(); 
        String sql = "SELECT TYPE, NAME, HEALTH, MAX_HEALTH, ATTACK, SPECIAL, SPECIAL_MAX FROM PETS WHERE UPPER(OWNER) = UPPER(?)";
        try (PreparedStatement ps = connection().prepareStatement(sql)){
            ps.setString(1, owner);
            try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {                            
                PetType type = PetType.valueOf(rs.getString("TYPE").toUpperCase());
                Pet pet = PetFactory.createPet(type, rs.getString("NAME"),
                        rs.getInt("MAX_HEALTH"), rs.getInt("ATTACK"), rs.getInt("SPECIAL_MAX"));
                pet.setHealth(rs.getInt("HEALTH"));
                pet.setSpecialAmount(rs.getInt("SPECIAL"));
                pets.add(pet);                             
            }
            }
        
        }catch (SQLException e){
            throw new DataAccessException("Could not load pets for " + owner, e);
        }
        return pets;
    }
    // Returns true if this owner already has a pet with that name (ignoring case). 
    public boolean exists(String owner, String petName){
        String sql = "SELECT NAME FROM PETS WHERE UPPER(OWNER) = UPPER(?) AND UPPER(NAME) = UPPER(?)";
        try (PreparedStatement ps = connection().prepareStatement(sql)) {
            ps.setString(1, owner);
            ps.setString(2, petName);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
               throw new DataAccessException("Could not check pet " + petName + " for " + owner, e);
        }
    }
    public void insert(String owner, Pet pet){
        if (exists(owner, pet.getName())){
        throw new IllegalArgumentException("Pet name already used: " + pet.getName());
    }
        String sql = "INSERT INTO PETS (OWNER, TYPE, NAME, HEALTH, MAX_HEALTH, ATTACK, SPECIAL, SPECIAL_MAX) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection().prepareStatement(sql)) {
            ps.setString(1, owner);
            ps.setString(2, pet.getType());
            ps.setString(3, pet.getName());
            ps.setInt(4, pet.getHealth());
            ps.setInt(5, pet.getMaxHealth());
            ps.setInt(6, pet.getAttack());
            ps.setInt(7, pet.getSpecialAmount());
            ps.setInt(8, pet.getSpecialAmountMax());
            ps.executeUpdate();
    } catch (SQLException e) {
        throw new DataAccessException("Could not insert pet " + pet.getName(), e);
    }
    }
    public void update(String owner, Pet pet){
        String sql = "UPDATE PETS SET HEALTH = ?, MAX_HEALTH = ?, ATTACK = ?, SPECIAL = ?, SPECIAL_MAX = ? WHERE UPPER(OWNER) = UPPER(?) AND UPPER(NAME) = UPPER(?)";
        try (PreparedStatement ps = connection().prepareStatement(sql)) {
        ps.setInt(1, pet.getHealth());
        ps.setInt(2, pet.getMaxHealth());
        ps.setInt(3, pet.getAttack());
        ps.setInt(4, pet.getSpecialAmount());
        ps.setInt(5, pet.getSpecialAmountMax());
        ps.setString(6, owner);
        ps.setString(7, pet.getName());
        ps.executeUpdate();
    } catch (SQLException e) {
        throw new DataAccessException("Could not update pet " + pet.getName() + " for " + owner, e);
    }
    }
}
