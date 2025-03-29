package GUI.Component.Panel;

import GUI.Component.Panel.Components.AccountNameLabel;
import GUI.Component.Panel.Components.ItemSideBarLabel;

import javax.swing.*;
import java.awt.*;

public class TaskBarPanel extends JPanel {
    ItemSideBarLabel itemSideBarLabel;
    AccountNameLabel accountNameLabel;
    JFrame parentFrame;
    public TaskBarPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setPreferredSize(new Dimension(252, 800));
        accountNameLabel = new AccountNameLabel("Hoàng Đình Phú Quý", "Admin");
        add(accountNameLabel);
        itemSideBarLabel = new ItemSideBarLabel(parentFrame);
        this.add(itemSideBarLabel);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        TaskBarPanel panel = new TaskBarPanel(frame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }
}
