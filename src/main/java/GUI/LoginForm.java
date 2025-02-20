package GUI;


import BUS.EmployeeBUS;
import DTO.Employee;
import GUI.Component.RoundedPasswordField;
import GUI.Component.RoundedTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class LoginForm extends JFrame {
    public static String username = "";
    public EmployeeBUS employeeBUS = new EmployeeBUS();

    /**
     * Create the frame.
     */
    public LoginForm() {
        // Tạo cửa sổ JFrame
        setTitle("Đăng nhập");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(960, 650);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1,2));


        JPanel image = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon bg = new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/treebook.png")));
                g.drawImage(bg.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        add(image, BorderLayout.WEST);

        RoundedTextField usernameField = new RoundedTextField(20, 15, 15);
        usernameField.setPlaceholder("Nhập tài khoản");
        usernameField.setBackground(new Color(245, 245, 245));
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setBorderColor(new Color(200, 200, 200));
        usernameField.setFocusBorderColor(new Color(0, 120, 215));
        RoundedPasswordField passwordField = new RoundedPasswordField(20, 15, 15);
        passwordField.setPlaceholder("Nhập mật khẩu");
        passwordField.setBackground(new Color(245, 245, 245));
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));


        JButton loginButton = new JButton("ĐĂNG NHẬP");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(new Color(0, 120, 215));
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        loginButton.setFocusPainted(false);
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(0, 90, 180));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(0, 120, 215));
            }
        });


        JPanel form = new JPanel();
        form.setLayout(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 10, 10); // Padding
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 0;
        form.add(createLabel("Tài khoản:"), gbc);
        gbc.gridy = 1;
        form.add(usernameField, gbc);
        gbc.gridy = 2;
        form.add(createLabel("Mật khẩu:"), gbc);
        gbc.gridy = 3;
        form.add(passwordField, gbc);
        gbc.gridy = 4;
        gbc.insets = new Insets(15, 10, 5, 10); // Tăng padding trên dưới cho nút
        form.add(loginButton, gbc);
        add(form);
        setVisible(true);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String password = new String(passwordField.getPassword());
                Employee user = employeeBUS.login(usernameField.getText(),password);

//                loading load = new loading();
//                load.setVisible(true);
//                Sleep(2000);
//                load.setVisible(false);
//                load.dispose();

                if (user != null) {
                    username = user.getUsername();
                    setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(LoginForm.this, "Sai tên đăng nhập hoặc mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(new Color(80, 80, 80));
        return label;
    }
}
