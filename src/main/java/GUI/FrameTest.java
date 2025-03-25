package GUI;

import GUI.Component.Panel.NavBarPanel;
import GUI.Component.Panel.TaskBarPanel;

import javax.swing.*;
import java.awt.*;

public class FrameTest extends JFrame {
    TaskBarPanel taskbar;
    public FrameTest() {
        this.setTitle("FrameTest");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setSize(1500, 800);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        taskbar = new TaskBarPanel();
        this.add(taskbar, BorderLayout.WEST);
    }

    public static void main(String[] args) {
        FrameTest frameTest = new FrameTest();
        frameTest.setVisible(true);
    }
}
