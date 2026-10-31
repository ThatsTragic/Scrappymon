package com.mycompany.virtualpetgame.ui;

import com.mycompany.virtualpetgame.Item;
import com.mycompany.virtualpetgame.Pet;
import com.mycompany.virtualpetgame.User;
import com.mycompany.virtualpetgame.service.BattleEngine;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.function.Supplier;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

/**
 *
 * @author Mitchell
 */
public class BattlePanel extends JPanel implements Refreshable {
    private final MainFrame frame;
    private BattleEngine engine;
    private final JLabel roundLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel petLabel = new JLabel("", SwingConstants.CENTER);
    private final JLabel enemyLabel = new JLabel("", SwingConstants.CENTER);
    private final JTextArea log = new JTextArea();
    private final JButton attackButton;
    private final JButton specialButton;
    private final JButton healButton;
    private final JButton strengthButton;
    private final JButton manaButton;
    private final JButton runButton;

    public BattlePanel(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel top = new JPanel(new GridLayout(0, 1, 3, 3));
        top.add(Ui.title("Battle"));
        top.add(roundLabel);
        top.add(petLabel);
        top.add(enemyLabel);
        add(top, BorderLayout.NORTH);

        log.setEditable(false);
        log.setLineWrap(true);
        log.setWrapStyleWord(true);
        log.setFont(log.getFont().deriveFont(18f));
        add(new JScrollPane(log), BorderLayout.CENTER);

        attackButton = Ui.button("Attack", e -> perform(() -> engine.playerAttack()));
        specialButton = Ui.button("Special", e -> perform(() -> engine.playerSpecial()));
        healButton = Ui.button("Heal potion", e -> perform(() -> engine.useItem("HealPot")));
        strengthButton = Ui.button("Strength potion", e -> perform(() -> engine.useItem("StrengthPot")));
        manaButton = Ui.button("Mana potion", e -> perform(() -> engine.useItem("ManaPot")));
        runButton = Ui.button("Run away", e -> perform(() -> engine.run()));

        JPanel actions = new JPanel(new GridLayout(2, 3, 8, 8));
        actions.add(attackButton);
        actions.add(specialButton);
        actions.add(runButton);
        actions.add(healButton);
        actions.add(strengthButton);
        actions.add(manaButton);

        JPanel bottom = new JPanel(new BorderLayout(5, 8));
        bottom.add(actions, BorderLayout.CENTER);
        bottom.add(Ui.button("Back to menu", e -> backToMenu()), BorderLayout.SOUTH);
        add(bottom, BorderLayout.SOUTH);
    }

    /** Runs one action on the engine, shows its text, and saves when the battle is over. */
    private void perform(Supplier<String> action) {
        if (engine == null || engine.isFinished()) {
            return;
        }
        try {
            log.append(action.get() + "\n\n");
            log.setCaretPosition(log.getDocument().getLength());
            if (engine.isFinished()) {
                frame.saveGame();
            }
            updateDisplay();
        } catch (RuntimeException e) {
            frame.report(e);
        }
    }

    /** Leaving a battle that is still going counts as running away. */
    private void backToMenu() {
        if (engine != null && !engine.isFinished()) {
            engine.run();
            frame.saveGame();
        }
        frame.show(MainFrame.HOME);
    }

    private void updateDisplay() {
        Pet pet = engine.getPet();
        roundLabel.setText("Round " + engine.getRound());
        petLabel.setText("You:   " + pet.getStatsDisplay());
        enemyLabel.setText("Enemy: " + engine.getEnemy().getStatsDisplay());
        boolean live = !engine.isFinished();
        attackButton.setEnabled(live);
        specialButton.setEnabled(live);
        runButton.setEnabled(live);
        healButton.setEnabled(live);
        strengthButton.setEnabled(live);
        manaButton.setEnabled(live);
        User user = frame.getUser();
        healButton.setText("Heal potion (" + count(user, "HealPot") + ")");
        strengthButton.setText("Strength potion (" + count(user, "StrengthPot") + ")");
        manaButton.setText("Mana potion (" + count(user, "ManaPot") + ")");
    }

    private int count(User user, String itemName) {
        Item item = user.getItem(itemName);
        return item == null ? 0 : item.getAmount();
    }

    /** Starts a fresh battle every time the screen is opened. */
    @Override
    public void refresh() {
        User user = frame.getUser();
        if (user == null) {
            return;
        }
        Pet pet = user.getActivePet();
        engine = new BattleEngine(user, pet, BattleEngine.createEnemy(pet));
        log.setText("A wild " + engine.getEnemy().getName() + " appears!\n\n");
        updateDisplay();
    }
}
