package GUI.Component.Panel;

import GUI.Component.Panel.Components.AccountNameLabel;
import GUI.Component.Panel.Components.ItemSideBarLabel;

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
}
