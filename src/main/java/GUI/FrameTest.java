package GUI;

import GUI.Component.Panel.TaskBarPanel;
import GUI.page.HomePage;

import javax.swing.*;
import java.awt.*;

public class FrameTest extends JFrame {
    TaskBarPanel taskbar;
    HomePage home;

    public FrameTest() {
        this.setTitle("FrameTest");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setSize(1500, 800);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        taskbar = new TaskBarPanel();
        this.add(taskbar, BorderLayout.WEST);

        this.add(home = new HomePage());
    }

    public static void main(String[] args) {
        FrameTest frameTest = new FrameTest();
        frameTest.setVisible(true);
    }
}
