package com.mycompany.virtualpetgame.ui;

import com.mycompany.virtualpetgame.Pet;
import com.mycompany.virtualpetgame.User;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *
 * @author Mitchell
 */
public class HomePanel extends JPanel implements Refreshable {
    private final MainFrame frame;
    private final JLabel playerLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel petLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel statusLabel = new JLabel(" ", SwingConstants.CENTER);

    public HomePanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel top = new JPanel(new GridLayout(0, 1, 5, 5));
        top.add(Ui.title("Main Menu"));
        top.add(playerLabel);
        top.add(petLabel);
        add(top, BorderLayout.NORTH);

        JPanel training = new JPanel(new GridLayout(1, 3, 8, 8));
        training.setBorder(BorderFactory.createTitledBorder("Train your pet"));
        training.add(Ui.button("Strength (+10 attack)", e -> train("strength")));
        training.add(Ui.button("Agility (+5 max health, +5 attack)", e -> train("agility")));
        training.add(Ui.button("Endurance (+10 max health)", e -> train("endurance")));

        JPanel menu = new JPanel(new GridLayout(0, 1, 8, 8));
        menu.add(training);
        menu.add(Ui.button("Battle", e -> frame.show(MainFrame.BATTLE)));
        menu.add(Ui.button("Shop", e -> frame.show(MainFrame.SHOP)));
        menu.add(Ui.button("My pets", e -> frame.show(MainFrame.PETS)));
        add(menu, BorderLayout.CENTER);

        JPanel bottom = new JPanel(new GridLayout(0, 1, 5, 5));
        bottom.add(statusLabel);
        JPanel buttons = new JPanel(new GridLayout(1, 2, 8, 8));
        buttons.add(Ui.button("Log out", e -> frame.logOut()));
        buttons.add(Ui.button("Save and quit", e -> frame.quit()));
        bottom.add(buttons);
        add(bottom, BorderLayout.SOUTH);
    }

    private void train(String kind) {
        try {
            String message = frame.getTrainingService().train(kind, frame.getUser().getActivePet());
            frame.saveGame();
            updateLabels();
            statusLabel.setText(message);
        } catch (RuntimeException e) {
            frame.report(e);
        }
    }

    private void updateLabels() {
        User user = frame.getUser();
        Pet pet = user.getActivePet();
        playerLabel.setText("Player: " + user.getUsername() + "    Coins: " + user.getCoins());
        petLabel.setText(pet.getStatsDisplay());
    }

    @Override
    public void refresh() {
        if (frame.getUser() != null) {
            updateLabels();
        }
        statusLabel.setText(" ");
    }
}
