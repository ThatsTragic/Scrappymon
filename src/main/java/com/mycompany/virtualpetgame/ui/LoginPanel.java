package com.mycompany.virtualpetgame.ui;

import com.mycompany.virtualpetgame.PetType;
import com.mycompany.virtualpetgame.User;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Mitchell
 */
public class LoginPanel extends JPanel implements Refreshable {
    private final MainFrame frame;
    private final JTextField loginField = new JTextField(15);
    private final JTextField newNameField = new JTextField(15);
    private final JTextField petNameField = new JTextField(15);
    private final JComboBox<PetType> typeBox = new JComboBox<>(PetType.values());

    public LoginPanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(Ui.title("Welcome to Scrappymon"), BorderLayout.NORTH);

        JPanel existing = new JPanel(new GridLayout(0, 1, 5, 5));
        existing.setBorder(BorderFactory.createTitledBorder("Returning player"));
        existing.add(new JLabel("Username:"));
        existing.add(loginField);
        existing.add(Ui.button("Log in", e -> login()));

        JPanel create = new JPanel(new GridLayout(0, 1, 5, 5));
        create.setBorder(BorderFactory.createTitledBorder("New player"));
        create.add(new JLabel("Choose a username:"));
        create.add(newNameField);
        create.add(new JLabel("Starter pet type:"));
        create.add(typeBox);
        create.add(new JLabel("Name your pet:"));
        create.add(petNameField);
        create.add(Ui.button("Create player", e -> register()));

        JPanel centre = new JPanel(new GridLayout(1, 2, 15, 0));
        centre.add(existing);
        centre.add(create);
        add(centre, BorderLayout.CENTER);

        add(Ui.button("Quit", e -> frame.quit()), BorderLayout.SOUTH);
        loginField.addActionListener(e -> login());
    }

    private void login() {
        String name = loginField.getText().trim();
        if (name.isEmpty()) {
            frame.showError("Please type your username.");
            return;
        }
        try {
            User user = frame.getGameService().login(name);
            if (user == null) {
                frame.showError("There is no player called \"" + name
                        + "\". Create a new player on the right.");
                return;
            }
            frame.logIn(user);
        } catch (RuntimeException e) {
            frame.report(e);
        }
    }

    private void register() {
        try {
            User user = frame.getGameService().register(newNameField.getText(),
                    (PetType) typeBox.getSelectedItem(), petNameField.getText());
            frame.logIn(user);
        } catch (RuntimeException e) {
            frame.report(e);
        }
    }

    @Override
    public void refresh() {
        loginField.setText("");
        newNameField.setText("");
        petNameField.setText("");
    }
}
