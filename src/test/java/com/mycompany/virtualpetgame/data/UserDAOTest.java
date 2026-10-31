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
import java.sql.Statement;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserDAOTest {
    private UserDAO dao;

    @Before
    public void setUp() throws Exception {
        try (Statement st = DatabaseManager.getInstance().getConnection().createStatement()) {
            st.executeUpdate("DELETE FROM ITEMS");
            st.executeUpdate("DELETE FROM PETS");
            st.executeUpdate("DELETE FROM USERS");
        }
        dao = new UserDAO();
    }

    @Test
    public void insertedUserCanBeFoundWithSameCoins() {
        dao.insert(new User("Bob", 25));
        User found = dao.findByUsername("bob");
        assertNotNull(found);
        assertEquals(25, found.getCoins());
    }

    @Test
    public void findByUsernameReturnsNullForUnknownUser() {
        assertNull(dao.findByUsername("nobody"));
    }
}
