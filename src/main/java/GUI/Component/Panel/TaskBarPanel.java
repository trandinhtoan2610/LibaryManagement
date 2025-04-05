package GUI.Component.Panel;

import GUI.Component.Panel.Components.AccountNameLabel;
import GUI.Component.Panel.Components.ItemSideBarLabel;
import GUI.Component.Panel.Components.SidebarListener;

import javax.swing.*;
import java.awt.*;

public class TaskBarPanel extends JPanel {
    ItemSideBarLabel itemSideBarLabel;
    AccountNameLabel accountNameLabel;
    JFrame parentFrame;
    public TaskBarPanel(JFrame parentFrame, SidebarListener listener) {
        this.parentFrame = parentFrame;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setPreferredSize(new Dimension(252, parentFrame.getHeight()));
        accountNameLabel = new AccountNameLabel("Hoàng Đình Phú Quý", "Admin");
        add(accountNameLabel);
        itemSideBarLabel = new ItemSideBarLabel(parentFrame, listener);
        this.add(itemSideBarLabel);
    }
}
