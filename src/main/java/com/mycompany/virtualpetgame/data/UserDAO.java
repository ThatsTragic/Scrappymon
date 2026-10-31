/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.virtualpetgame.data;

/**
 *
 * @author Mitchell
 */

import com.mycompany.virtualpetgame.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private Connection connection() throws SQLException {
        return DatabaseManager.getInstance().getConnection();
    }
    public boolean exists(String name) {
    String sql = "SELECT USERNAME FROM USERS WHERE UPPER(USERNAME) = UPPER(?)";
    try (PreparedStatement ps = connection().prepareStatement(sql)) {
        ps.setString(1, name);
        try (ResultSet rs = ps.executeQuery()) {
            return rs.next();
        }
    } catch (SQLException e) {
        throw new DataAccessException("Could not check user " + name, e);
    }
    }
    public void insert(User user) {
    if (exists(user.getUsername())){
        throw new IllegalArgumentException("User already exists");
    }
    String sql = "INSERT INTO USERS (USERNAME, COINS, ACTIVE_PET) VALUES (?, ?, ?)";
    try (PreparedStatement ps = connection().prepareStatement(sql)) {
        ps.setString(1, user.getUsername());
        ps.setInt(2, user.getCoins());
        ps.setString(3, user.getActivePet() == null ? null : user.getActivePet().getName());
        ps.executeUpdate();
    } catch (SQLException e) {
        throw new DataAccessException("Could not insert user " + user.getUsername(), e);
    }
    }
    public User findByUsername(String name){
        String sql = "SELECT USERNAME, COINS, ACTIVE_PET FROM USERS WHERE UPPER(USERNAME) = UPPER(?)";
        try (PreparedStatement ps = connection().prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;                       // no such user
                }
                User user = new User(rs.getString("USERNAME"), rs.getInt("COINS"));
                user.setSavedActivePetName(rs.getString("ACTIVE_PET"));
                return user;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not find user " + name, e);
        }
    }
    public void updateCoins(String name, int coins) {
    String sql = "UPDATE USERS SET COINS = ? WHERE UPPER(USERNAME) = UPPER(?)";
    try (PreparedStatement ps = connection().prepareStatement(sql)) {
        ps.setInt(1, coins);
        ps.setString(2, name);
        ps.executeUpdate();
    } catch (SQLException e) {
        throw new DataAccessException("Could not update coins for " + name, e);
    }
    }
    public void updateActivePet(String name, String petName) {
    String sql = "UPDATE USERS SET ACTIVE_PET = ? WHERE UPPER(USERNAME) = UPPER(?)";
    try (PreparedStatement ps = connection().prepareStatement(sql)) {
        ps.setString(1, petName);
        ps.setString(2, name);
        ps.executeUpdate();
    } catch (SQLException e) {
        throw new DataAccessException("Could not update pet for " + name, e);
    }
    }
}
