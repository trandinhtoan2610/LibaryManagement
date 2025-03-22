package GUI.Panel;

import GUI.Component.IconNavBarLabel;
import GUI.Component.SearchNavBarLabel;

import javax.swing.*;
import java.awt.*;

public class NavBarPanel extends JPanel {
    IconNavBarLabel leftNavBar;
    SearchNavBarLabel rightNavBar;

    public NavBarPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1200, 100));
        setBackground(new Color(240, 240, 240));
        setForeground(new Color(240, 240, 240));
        leftNavBar = new IconNavBarLabel();
        rightNavBar = new SearchNavBarLabel();

        rightNavBar.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0)); // 20px margin-top

        add(leftNavBar, BorderLayout.WEST);

        add(rightNavBar, BorderLayout.EAST);
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