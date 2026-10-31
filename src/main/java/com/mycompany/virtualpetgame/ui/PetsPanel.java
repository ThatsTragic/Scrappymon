package com.mycompany.virtualpetgame.ui;

import com.mycompany.virtualpetgame.Pet;
import com.mycompany.virtualpetgame.PetType;
import com.mycompany.virtualpetgame.User;
import com.mycompany.virtualpetgame.service.ShopService;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

/**
 *
 * @author Mitchell
 */
public class PetsPanel extends JPanel implements Refreshable {
    private final MainFrame frame;
    private final JLabel coinsLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel statusLabel = new JLabel(" ", SwingConstants.CENTER);
    private final DefaultListModel<String> model = new DefaultListModel<>();
    private final JList<String> petList = new JList<>(model);

    public PetsPanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel top = new JPanel(new GridLayout(0, 1, 5, 5));
        top.add(Ui.title("My Pets"));
        top.add(coinsLabel);
        add(top, BorderLayout.NORTH);

        petList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(petList), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new GridLayout(0, 1, 8, 8));
        buttons.add(Ui.button("Make selected pet active", e -> setActive()));
        buttons.add(Ui.button("Buy a new pet - " + ShopService.NEW_PET_COST + " coins", e -> buyPet()));
        buttons.add(Ui.button("Upgrade active pet (+1 special move) - " + ShopService.UPGRADE_COST + " coins", e -> upgrade()));
        buttons.add(statusLabel);
        buttons.add(Ui.button("Back to menu", e -> frame.show(MainFrame.HOME)));
        add(buttons, BorderLayout.SOUTH);
    }

    private void setActive() {
        int index = petList.getSelectedIndex();
        if (index < 0) {
            frame.showError("Select a pet from the list first.");
            return;
        }
        User user = frame.getUser();
        user.setActivePet(user.getPets().get(index));
        frame.saveGame();
        updateDisplay();
        statusLabel.setText(user.getActivePet().getName() + " is now your active pet.");
    }

    private void buyPet() {
        JComboBox<PetType> typeBox = new JComboBox<>(PetType.values());
        JTextField nameField = new JTextField(15);
        JPanel form = new JPanel(new GridLayout(0, 1, 5, 5));
        form.add(new JLabel("Pet type:"));
        form.add(typeBox);
        form.add(new JLabel("Pet name:"));
        form.add(nameField);
        int choice = JOptionPane.showConfirmDialog(this, form, "Buy a new pet",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (choice != JOptionPane.OK_OPTION) {
            return;
        }
        try {
            Pet pet = frame.getShopService().buyPet(frame.getUser(),
                    (PetType) typeBox.getSelectedItem(), nameField.getText());
            frame.saveGame();
            updateDisplay();
            statusLabel.setText("You bought " + pet.getName() + "!");
        } catch (RuntimeException e) {
            frame.report(e);
        }
    }

    private void upgrade() {
        try {
            String message = frame.getShopService().upgradePet(frame.getUser(), frame.getUser().getActivePet());
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
        model.clear();
        for (Pet pet : user.getPets()) {
            String marker = pet == user.getActivePet() ? "  [ACTIVE]" : "";
            model.addElement(pet.getStatsDisplay() + marker);
        }
    }

    @Override
    public void refresh() {
        if (frame.getUser() != null) {
            updateDisplay();
        }
        statusLabel.setText(" ");
    }
}
