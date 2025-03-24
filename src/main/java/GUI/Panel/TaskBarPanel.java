package GUI.Panel;

import GUI.Component.AccountNameLabel;
import GUI.Component.ItemSideBarLabel;

import javax.swing.*;
import java.awt.*;

public class TaskBarPanel extends JPanel {
    ItemSideBarLabel itemSideBarLabel;
    AccountNameLabel accountNameLabel;
    public TaskBarPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setPreferredSize(new Dimension(252, 800));
        accountNameLabel = new AccountNameLabel();
        add(accountNameLabel);
        itemSideBarLabel = new ItemSideBarLabel();
        this.add(itemSideBarLabel);
    }

    public static void main(String[] args) {
        Frame frame = new Frame();
        TaskBarPanel panel = new TaskBarPanel();
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }
}
