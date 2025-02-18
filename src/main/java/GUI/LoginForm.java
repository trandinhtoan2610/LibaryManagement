package GUI;


import GUI.Styles.RoundedPasswordField;
import GUI.Styles.RoundedTextField;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Objects;
import java.util.prefs.Preferences;

public class LoginForm extends JFrame {
    public Preferences pref = Preferences.userRoot().node("remember");
    public static long role;
    public static boolean isLogin = false;
    public static String username = "";








    /**
     * Create the frame.
     */
    public LoginForm() {
        // Tạo cửa sổ JFrame
        setTitle("Login Form");
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





        JTextField usernameField = new RoundedTextField(20, 100, 100);
        usernameField.setForeground(Color.GRAY);
        usernameField.setText("Nhập tài khoản");
        usernameField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (usernameField.getText().equals("Nhập tài khoản")) {
                    usernameField.setText("");
                    usernameField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (usernameField.getText().isEmpty()) {
                    usernameField.setText("Nhập tài khoản");
                    usernameField.setForeground(Color.GRAY);
                }
            }
        });

        JPasswordField passwordField = new RoundedPasswordField(20, 100, 100);
        passwordField.setText("<PASSWORD>");
        passwordField.setForeground(Color.GRAY);
        passwordField.setBorder(new LineBorder(Color.BLACK, 1, true));
        passwordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (passwordField.getText().equals("<PASSWORD>")) {
                    passwordField.setText("");
                    passwordField.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (passwordField.getText().isEmpty()) {
                    passwordField.setText("<PASSWORD>");
                    passwordField.setForeground(Color.GRAY);
                }
            }
        });

        JButton loginButton = new JButton("Login");
        JPanel form = new JPanel();
        form.setLayout(new GridLayout(6, 1));
        // Thêm các thành phần vào form

        form.add(usernameField);
        form.add(passwordField);
        form.add(loginButton);

        // Thêm form vào cửa sổ JFrame
        add(form);
        setVisible(true);

        // Thêm sự kiện khi nhấn nút login
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());


                if (username.equals("admin") && password.equals("1234")) {
                    JOptionPane.showMessageDialog(LoginForm.this, "Đăng nhập thành công!");
                } else {
                    JOptionPane.showMessageDialog(LoginForm.this, "Sai tên đăng nhập hoặc mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
