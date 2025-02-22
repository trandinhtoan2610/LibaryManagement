package GUI;

import com.mysql.cj.log.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private JPanel mainPanel = new JPanel();
    //Bar
    private JMenuBar menuBar = new JMenuBar();
    private JMenu fileMenu = new JMenu("File");
    private JMenu optionMenu = new JMenu("Option");
    private JMenu helpMenu = new JMenu("Help");
    private JMenu aboutMenu = new JMenu("About");
    private JMenuItem exitItem = new JMenuItem("Exit");
    // Left Panel
    private JPanel leftPanel = new JPanel();
    private JButton homeButton = new JButton("Trang chủ");
    private JButton bookButton = new JButton("Sách");
    private JButton authorButton = new JButton("Tác giả");
    private JButton publisherButton = new JButton("Nhà Xuất Bản");
    private JButton customerButton = new JButton("Khách hàng");
    private JButton employeeButton = new JButton("Nhân viên");
    private JButton supplierButton = new JButton("Nhà Cung Cấp");
    private JButton borrowButton = new JButton("Mượn");
    private JButton buyingButton = new JButton("Mua");
    private JButton statisticButton = new JButton("Thống kê");
    private JButton logoutButton = new JButton("Đăng xuất");
//    private JButton
    //center
    private JPanel centerPanel = new JPanel();

    public MainFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        mainBar();
        leftMenu();
        centerContent();
        setVisible(true);
    }
    public void mainBar() {
        setJMenuBar(menuBar);
        menuBar.add(fileMenu);
        menuBar.add(optionMenu);
        menuBar.add(helpMenu);
        menuBar.add(aboutMenu);
        fileMenu.add(exitItem);

        // Add action listener for exit item
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
    public void leftMenu() {
        leftPanel.setLayout(new GridLayout(11, 1)); // 5 rows, 1 column
        leftPanel.setPreferredSize(new Dimension(150, 600)); // Set preferred size

        // Add buttons to the left panel
        leftPanel.add(homeButton);
        leftPanel.add(bookButton);
        leftPanel.add(authorButton);
        leftPanel.add(publisherButton);
        leftPanel.add(customerButton);
        leftPanel.add(employeeButton);
        leftPanel.add(borrowButton);
        leftPanel.add(supplierButton);
        leftPanel.add(buyingButton);
        leftPanel.add(statisticButton);
        leftPanel.add(logoutButton);

        // Add action listeners for buttons
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        authorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        customerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                new LoginForm();
            }
        });
        add(leftPanel, BorderLayout.WEST);
    }
    public void centerContent(){
        centerPanel.setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Chào mừng đến với hệ thống quản lý thư viện!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        centerPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Thêm hình ảnh
        ImageIcon homeImage = new ImageIcon("path/to/image.jpg");
        JLabel imageLabel = new JLabel(homeImage);
        centerPanel.add(imageLabel, BorderLayout.CENTER);

        // Thêm thông báo chào mừng
        JTextArea welcomeText = new JTextArea("Xin chào! Đây là hệ thống quản lý thư viện. \n" +
                "Sử dụng menu bên trái để điều hướng đến các tính năng.");
        welcomeText.setEditable(false);
        welcomeText.setFont(new Font("Arial", Font.PLAIN, 14));
        welcomeText.setBackground(new Color(240, 240, 240));
        centerPanel.add(welcomeText, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);
    }
    public static void main(String[] args) {
        new MainFrame();
    }
}
