package com.mycompany.virtualpetgame.service;

import com.mycompany.virtualpetgame.Pet;
import com.mycompany.virtualpetgame.PetType;
import com.mycompany.virtualpetgame.User;
import com.mycompany.virtualpetgame.data.DatabaseManager;
import java.sql.Statement;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
/**
 *
 * @author Mitchell
 */
public class GameServiceTest {
    private GameService service;

    @Before
    public void setUp() throws Exception {
        try (Statement st = DatabaseManager.getInstance().getConnection().createStatement()) {
            st.executeUpdate("DELETE FROM ITEMS");
            st.executeUpdate("DELETE FROM PETS");
            st.executeUpdate("DELETE FROM USERS");
        }
        service = new GameService();
    }

    @Test
    public void registerCreatesUserWithStarterPet() {
        service.register("Jeff", PetType.CAT, "Kitty");
        User loaded = service.login("jeff");
        assertNotNull(loaded);
        assertEquals(1, loaded.getPets().size());
        assertEquals("Kitty", loaded.getActivePet().getName());
        assertEquals(GameService.STARTING_COINS, loaded.getCoins());
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerRejectsExistingUsername() {
        service.register("Jeff", PetType.DOG, "Rex");
        service.register("JEFF", PetType.CAT, "Kit");
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerRejectsNamesWithCommas() {
        service.register("Je,ff", PetType.DOG, "Rex");
    }

    @Test
    public void loginReturnsNullForUnknownUser() {
        assertNull(service.login("nobody"));
    }

    @Test
    public void saveThenLoginKeepsMaxHealthCoinsAndItems() {
        User user = service.register("Jeff", PetType.DOG, "Rex");
        Pet pet = user.getActivePet();
        pet.addMaxHealth(10);                 // like Endurance training
        user.addCoins(15);
        user.addItem(com.mycompany.virtualpetgame.ItemFactory.create("ManaPot", 2));
        service.save(user);

        User loaded = service.login("Jeff");
        assertEquals(110, loaded.getActivePet().getMaxHealth());
        assertEquals(GameService.STARTING_COINS + 15, loaded.getCoins());
        assertEquals(2, loaded.getItem("ManaPot").getAmount());
    }
}
