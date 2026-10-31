/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.virtualpetgame.data;

import java.sql.Statement;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.mycompany.virtualpetgame.Item;
import com.mycompany.virtualpetgame.ItemFactory;
import com.mycompany.virtualpetgame.User;
import java.util.List;

/**
 *
 * @author Mitchell
 */
public class ItemDAOTest {

    private ItemDAO dao;

    @Before
    public void setUp() throws Exception {
        try (Statement st = DatabaseManager.getInstance().getConnection().createStatement()) {
            st.executeUpdate("DELETE FROM ITEMS");
            st.executeUpdate("DELETE FROM PETS");
            st.executeUpdate("DELETE FROM USERS");
        }
        dao = new ItemDAO();
    }

    @Test
    public void upsertCreatesItemWhenMissing() {
        new UserDAO().insert(new User("Jeff", 0));
        Item item = ItemFactory.create("HealPot", 3);
        dao.upsert("Jeff", item);

        List<Item> loaded = dao.findByOwner("Jeff");
        assertEquals(1, loaded.size());
        assertEquals("HealPot", loaded.get(0).getName());
        assertEquals(3, loaded.get(0).getAmount());

    }

    @Test
    public void upsertUpdatesExistingAmount() {
        new UserDAO().insert(new User("Jeff", 0));
        Item item = ItemFactory.create("HealPot", 3);
        dao.upsert("Jeff", item);

        Item item2 = ItemFactory.create("HealPot", 5);
        dao.upsert("Jeff", item2);

        List<Item> loaded = dao.findByOwner("Jeff");
        assertEquals(1, loaded.size());
        assertEquals("HealPot", loaded.get(0).getName());
        assertEquals(5, loaded.get(0).getAmount());

    }

    @Test
    public void findByOwnerOnlyReturnsThatOwnersItems() {
        new UserDAO().insert(new User("Jeff", 0));
        Item item = ItemFactory.create("HealPot", 3);
        dao.upsert("Jeff", item);

        new UserDAO().insert(new User("Bob", 0));
        Item item2 = ItemFactory.create("HealPot", 5);
        dao.upsert("Bob", item2);

        List<Item> loaded = dao.findByOwner("Jeff");
        assertEquals(1, loaded.size());
        assertEquals("HealPot", loaded.get(0).getName());
        assertEquals(3, loaded.get(0).getAmount());

        List<Item> loaded2 = dao.findByOwner("Bob");
        assertEquals(1, loaded2.size());
        assertEquals("HealPot", loaded2.get(0).getName());
        assertEquals(5, loaded2.get(0).getAmount());
    }
}
