/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.virtualpetgame.data;

import com.mycompany.virtualpetgame.Pet;
import com.mycompany.virtualpetgame.PetFactory;
import com.mycompany.virtualpetgame.PetType;
import com.mycompany.virtualpetgame.User;
import java.sql.Statement;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Mitchell
 */
public class PetDAOTest {
    private PetDAO dao;

    @Before
    public void setUp() throws Exception {
        try (Statement st = DatabaseManager.getInstance().getConnection().createStatement()) {
            st.executeUpdate("DELETE FROM ITEMS");
            st.executeUpdate("DELETE FROM PETS");
            st.executeUpdate("DELETE FROM USERS");
        }
        dao = new PetDAO();
    }
    
    @Test
    public void savedPetKeepsMaxHealthAfterReload() {
        new UserDAO().insert(new User("Bob", 0));
        Pet pet = PetFactory.createPet(PetType.DOG, "Rex", 100, 10, 3);
        pet.addMaxHealth(10);                       // like Endurance training
        dao.insert("Bob", pet);
        Pet loaded = dao.findByOwner("Bob").get(0);
        assertEquals(110, loaded.getMaxHealth());
    }               // then the pet
    @Test
    public void updateChangesAllStoredStats() {
        new UserDAO().insert(new User("Bob", 0));
        Pet pet = PetFactory.createPet(PetType.CAT, "Kitty", 100, 10, 3);
        dao.insert("Bob", pet);

        pet.addAttack(5);
        pet.addMaxHealth(10);
        pet.addSpecialAmountMax(1);
        dao.update("Bob", pet);

        Pet loaded = dao.findByOwner("Bob").get(0);
        assertEquals(15, loaded.getAttack());
        assertEquals(110, loaded.getMaxHealth());
        assertEquals(4, loaded.getSpecialAmountMax());
    }
    
    
}
