package GUI;


import GUI.Component.Panel.EmployeeRightPanel;
import GUI.Component.Panel.TaskBarPanel;

import javax.swing.*;
import java.awt.*;

public class mainFrame extends JFrame {
    private TaskBarPanel taskBarPanel;
    private EmployeeRightPanel employeeRightPanel;
    public mainFrame() {
        setupUI();
    }
    private void setupUI(){
        setTitle("Quản lý thư viện");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1600, 800);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        taskBarPanel = new TaskBarPanel(this);
        taskBarPanel.setPreferredSize(new Dimension(252, getHeight()));
        add(taskBarPanel, BorderLayout.WEST);

        employeeRightPanel = new EmployeeRightPanel(this);
        add(employeeRightPanel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                mainFrame frame = new mainFrame();
                frame.setVisible(true);
            }
        });
    }
}
