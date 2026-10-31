package com.mycompany.virtualpetgame.service;

import com.mycompany.virtualpetgame.InputReader;
import com.mycompany.virtualpetgame.Item;
import com.mycompany.virtualpetgame.Pet;
import com.mycompany.virtualpetgame.PetFactory;
import com.mycompany.virtualpetgame.PetType;
import com.mycompany.virtualpetgame.User;
import com.mycompany.virtualpetgame.data.ItemDAO;
import com.mycompany.virtualpetgame.data.PetDAO;
import com.mycompany.virtualpetgame.data.UserDAO;

/**
 *
 * @author Mitchell
 */
public class GameService {
    public static final int STARTING_COINS = 30;

    private final UserDAO userDao;
    private final PetDAO petDao;
    private final ItemDAO itemDao;

    public GameService() {
        this(new UserDAO(), new PetDAO(), new ItemDAO());
    }

    public GameService(UserDAO userDao, PetDAO petDao, ItemDAO itemDao) {
        this.userDao = userDao;
        this.petDao = petDao;
        this.itemDao = itemDao;
    }

    // Loads a player with their pets and items. Returns null if the username is unknown. 
    public User login(String username) {
        String name = username == null ? "" : username.trim();
        User user = userDao.findByUsername(name);
        if (user == null) {
            return null;
        }
        for (Pet pet : petDao.findByOwner(user.getUsername())) {
            user.addPet(pet);
        }
        for (Item item : itemDao.findByOwner(user.getUsername())) {
            user.addItem(item);
        }
        user.resolveActivePet();
        if (user.getActivePet() == null) {
            throw new IllegalStateException("This account has no pets.");
        }
        return user;
    }

    // Creates a new player with a starter pet. Throws IllegalArgumentException with a friendly message.
    public User register(String username, PetType type, String petName) {
        String name = username == null ? "" : username.trim();
        String pet = petName == null ? "" : petName.trim();
        if (!InputReader.isValidName(name)) {
            throw new IllegalArgumentException(
                    "Username must be 1-20 letters, numbers, spaces, '-' or '_'.");
        }
        if (!InputReader.isValidName(pet)) {
            throw new IllegalArgumentException(
                    "Pet name must be 1-20 letters, numbers, spaces, '-' or '_'.");
        }
        if (userDao.exists(name)) {
            throw new IllegalArgumentException("That username is already taken.");
        }
        User user = new User(name, STARTING_COINS);
        Pet starter = PetFactory.createPet(type, pet, 100, 10, 3);
        user.addPet(starter);
        user.setActivePet(starter);
        userDao.insert(user);
        petDao.insert(name, starter);
        return user;
    }

    // Writes coins, active pet, every pet and every item back to the database. 
    public void save(User user) {
        String owner = user.getUsername();
        userDao.updateCoins(owner, user.getCoins());
        Pet active = user.getActivePet();
        userDao.updateActivePet(owner, active == null ? null : active.getName());
        for (Pet pet : user.getPets()) {
            if (petDao.exists(owner, pet.getName())) {
                petDao.update(owner, pet);
            } else {
                petDao.insert(owner, pet);
            }
        }
        for (Item item : user.getItems()) {
            itemDao.upsert(owner, item);
        }
    }
}
