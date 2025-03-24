package GUI;

import GUI.Panel.NavBarPanel;
import GUI.Panel.TaskBarPanel;

import javax.swing.*;
import java.awt.*;

public class FrameTest extends JFrame {
    TaskBarPanel taskbar;
    NavBarPanel navbar;
    public FrameTest() {
        this.setTitle("FrameTest");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setSize(1500, 800);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        taskbar = new TaskBarPanel();
        navbar = new NavBarPanel();
        this.add(taskbar, BorderLayout.WEST);
        this.add(navbar, BorderLayout.NORTH);
    }

    public static void main(String[] args) {
        FrameTest frameTest = new FrameTest();
        frameTest.setVisible(true);
    }
}
