package com.mycompany.virtualpetgame.ui;

import com.mycompany.virtualpetgame.Item;
import com.mycompany.virtualpetgame.User;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

/**
 *
 * @author Mitchell
 */
public class ShopPanel extends JPanel implements Refreshable {
    private final MainFrame frame;
    private final JLabel coinsLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel statusLabel = new JLabel(" ", SwingConstants.CENTER);
    private final JTextArea inventory = new JTextArea(5, 20);

    public ShopPanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel top = new JPanel(new GridLayout(0, 1, 5, 5));
        top.add(Ui.title("Shop"));
        top.add(coinsLabel);
        add(top, BorderLayout.NORTH);

        inventory.setEditable(false);
        inventory.setBorder(BorderFactory.createTitledBorder("Your potions"));
        add(inventory, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new GridLayout(0, 1, 8, 8));
        buttons.add(Ui.button("Buy Heal potion - 5 coins (+50 health)", e -> buy("HealPot")));
        buttons.add(Ui.button("Buy Strength potion - 10 coins (double attack for 2 rounds)", e -> buy("StrengthPot")));
        buttons.add(Ui.button("Buy Mana potion - 20 coins (refill special moves)", e -> buy("ManaPot")));
        buttons.add(statusLabel);
        buttons.add(Ui.button("Back to menu", e -> frame.show(MainFrame.HOME)));
        add(buttons, BorderLayout.SOUTH);
    }

    private void buy(String itemName) {
        try {
            String message = frame.getShopService().buyItem(frame.getUser(), itemName);
            frame.saveGame();
            updateDisplay();
            statusLabel.setText(message);
        } catch (RuntimeException e) {
            frame.report(e);
        }
    }

    private void updateDisplay() {
        User user = frame.getUser();
        coinsLabel.setText("Coins: " + user.getCoins());
        StringBuilder text = new StringBuilder();
        for (Item item : user.getItems()) {
            if (item.getAmount() > 0) {
                text.append(item.getName()).append(": ").append(item.getAmount()).append("\n");
            }
        }
        inventory.setText(text.length() == 0 ? "(none yet)" : text.toString());
    }

    @Override
    public void refresh() {
        if (frame.getUser() != null) {
            updateDisplay();
        }
        statusLabel.setText(" ");
    }
}
