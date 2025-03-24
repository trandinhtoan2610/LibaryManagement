package GUI.Panel;

import GUI.Component.AccountNameLabel;
import GUI.Component.IconNavBarLabel;
import GUI.Component.SearchNavBarLabel;

import javax.swing.*;
import java.awt.*;

public class NavBarPanel extends JPanel {
    IconNavBarLabel leftNavBar;
    SearchNavBarLabel rightNavBar;
    AccountNameLabel accountNameLabel;
    public NavBarPanel() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setPreferredSize(new Dimension(1200, 100));
        setBackground(Color.WHITE);
        setForeground(new Color(240, 240, 240));
        leftNavBar = new IconNavBarLabel();
        rightNavBar = new SearchNavBarLabel();
        accountNameLabel = new AccountNameLabel();
        rightNavBar.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        add(Box.createVerticalStrut(20));
        add(Box.createHorizontalStrut(5));
        add(accountNameLabel);
        add(Box.createHorizontalStrut(10));
        add(leftNavBar);
        add(Box.createHorizontalStrut(280));
        add(rightNavBar);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("NavBarPanel");
        frame.getContentPane().add(new NavBarPanel());
        frame.pack();
        frame.setVisible(true);
    }
}