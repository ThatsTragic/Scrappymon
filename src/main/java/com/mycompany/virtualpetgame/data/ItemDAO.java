/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.virtualpetgame.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.virtualpetgame.ItemFactory;
import com.mycompany.virtualpetgame.Item;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Mitchell
 */
public class ItemDAO {

    private Connection connection() throws SQLException {
        return DatabaseManager.getInstance().getConnection();
    }

    public List<Item> findByOwner(String owner) {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT ITEM_NAME, AMOUNT FROM ITEMS WHERE UPPER(OWNER) = UPPER(?) AND AMOUNT > 0";
        try (PreparedStatement ps = connection().prepareStatement(sql)) {
            ps.setString(1, owner);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Item item = ItemFactory.create(rs.getString("ITEM_NAME"), rs.getInt("AMOUNT"));

                    items.add(item);
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException("Could not load items for " + owner, e);
        }
        return items;
    }
       // Saves the item's amount, inserting a new row if the owner has none yet.
    public void upsert(String owner, Item item) {
        String updateSql = "UPDATE ITEMS SET AMOUNT = ? WHERE UPPER(OWNER) = UPPER(?) AND UPPER(ITEM_NAME) = UPPER(?)";
        String insertSql = "INSERT INTO ITEMS (OWNER, ITEM_NAME, AMOUNT) VALUES (?, ?, ?)";
        try {
            boolean updated;
            try (PreparedStatement ps = connection().prepareStatement(updateSql)) {
                ps.setInt(1, item.getAmount());
                ps.setString(2, owner);
                ps.setString(3, item.getName());
                updated = ps.executeUpdate() > 0;
            }
            if (!updated) {
                try (PreparedStatement ps = connection().prepareStatement(insertSql)) {
                    ps.setString(1, owner);
                    ps.setString(2, item.getName());
                    ps.setInt(3, item.getAmount());
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Could not save item " + item.getName() + " for " + owner, e);
        }
    }
}
