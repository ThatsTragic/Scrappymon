package com.mycompany.virtualpetgame.ui;

import com.mycompany.virtualpetgame.User;
import com.mycompany.virtualpetgame.data.DataAccessException;
import com.mycompany.virtualpetgame.data.DatabaseManager;
import com.mycompany.virtualpetgame.service.GameService;
import com.mycompany.virtualpetgame.service.ShopService;
import com.mycompany.virtualpetgame.service.TrainingService;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Mitchell
 */
public class MainFrame extends JFrame {
    public static final String LOGIN = "login";
    public static final String HOME = "home";
    public static final String BATTLE = "battle";
    public static final String SHOP = "shop";
    public static final String PETS = "pets";

    private final CardLayout cards = new CardLayout();
    private final JPanel root = new JPanel(cards);
    private final Map<String, Refreshable> screens = new HashMap<>();
    private final GameService gameService;
    private final ShopService shopService = new ShopService();
    private final TrainingService trainingService = new TrainingService();
    private User currentUser;

    public MainFrame() {
        super("Scrappymon");
        DatabaseManager.getInstance();            // fail early with a clear message if the DB is broken
        gameService = new GameService();

        addScreen(LOGIN, new LoginPanel(this));
        addScreen(HOME, new HomePanel(this));
        addScreen(BATTLE, new BattlePanel(this));
        addScreen(SHOP, new ShopPanel(this));
        addScreen(PETS, new PetsPanel(this));

        setContentPane(root);
        setPreferredSize(new Dimension(760, 560));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                quit();
            }
        });
        pack();
        setLocationRelativeTo(null);
        show(LOGIN);
    }

    private void addScreen(String name, JPanel panel) {
        screens.put(name, (Refreshable) panel);
        root.add(panel, name);
    }

    /** Shows a screen, refreshing it first so it always displays current data. */
    public void show(String name) {
        screens.get(name).refresh();
        cards.show(root, name);
    }

    public GameService getGameService() { return gameService; }
    public ShopService getShopService() { return shopService; }
    public TrainingService getTrainingService() { return trainingService; }
    public User getUser() { return currentUser; }

    public void logIn(User user) {
        currentUser = user;
        show(HOME);
    }

    public void logOut() {
        saveGame();
        currentUser = null;
        show(LOGIN);
    }

    /** Saves the current player. Shows a message instead of crashing if the database fails. */
    public void saveGame() {
        if (currentUser == null) {
            return;
        }
        try {
            gameService.save(currentUser);
        } catch (DataAccessException e) {
            showError("Your progress could not be saved: " + e.getMessage());
        }
    }

    /** Saves and closes the game. */
    public void quit() {
        saveGame();
        dispose();
        System.exit(0);
    }

    public void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Scrappymon", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Scrappymon", JOptionPane.WARNING_MESSAGE);
    }

    /** Turns any problem into a friendly message for the player. */
    public void report(RuntimeException e) {
        if (e instanceof IllegalArgumentException || e instanceof IllegalStateException) {
            showError(e.getMessage());
        } else {
            showError("Something went wrong: " + e.getMessage());
        }
    }
}
