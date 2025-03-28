package GUI.Component.Dialog;

import GUI.Component.Button.ButtonBack;
import GUI.Component.Combobox.CustomComboBox;
import GUI.Component.TextField.CustomTextField;

import javax.swing.*;
import java.awt.*;

public class addEmployeeDialog extends JPanel {
    public addEmployeeDialog() {
        initComponents();
    }
    public void initComponents() {
        setLayout(new BorderLayout(50, 50));
        JPanel title = title();
        add(title, BorderLayout.NORTH);
        JPanel buttonPanel = buttonPanel();
        add(buttonPanel, BorderLayout.WEST);
        JPanel inputPanel = textFieldPanel();
        add(inputPanel, BorderLayout.CENTER);
    }
    public JPanel title(){
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JLabel txt = new JLabel("Thêm Nhân Viên");
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 36));
        txt.setHorizontalAlignment(SwingConstants.CENTER);
        txt.setVerticalAlignment(SwingConstants.CENTER);
        txt.setForeground(Color.WHITE);
        panel.add(txt, BorderLayout.CENTER);
        panel.setForeground(Color.WHITE);
        panel.setBackground(new Color(0, 120, 215));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(2, 7, 2, 7),
                BorderFactory.createLineBorder(new Color(200, 200, 200), 2)
        ));
        return panel;
    }
    public JPanel buttonPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        add(Box.createVerticalGlue());
        ButtonBack buttonBack = new ButtonBack();
        panel.add(buttonBack);
        return panel;
    }
    public JPanel textFieldPanel(){
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(1980, 1080));
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        JLabel nametxt = setJlabel("Tên Nhân Viên");
        JLabel gendertxt = setJlabel("Giới Tính");
        JLabel usernametxt = setJlabel("Tên Đăng Nhập");
        JLabel passwordtxt = setJlabel("Mật Khẩu");
        JLabel roleIDtxt = setJlabel("Vai Trò");
        JLabel phonetxt = setJlabel("Số Điện Thoại");
        JLabel addresstxt = setJlabel("Địa Chỉ");
        JLabel salarytxt = setJlabel("Lương");

        CustomTextField name = new CustomTextField();
        CustomTextField username = new CustomTextField();
        CustomTextField password = new CustomTextField();
        CustomTextField phone = new CustomTextField();
        CustomTextField address = new CustomTextField();
        CustomTextField salary = new CustomTextField();

        CustomComboBox gendercbb = new CustomComboBox();
        gendercbb.setModel(new DefaultComboBoxModel(new String[]{"Male", "Female"}));
        gendercbb.setPreferredSize(new Dimension(200, 100));
        CustomComboBox rolecbb = new CustomComboBox();
        rolecbb.setModel(new DefaultComboBoxModel(new String[]{"admin", "employee", "staff"}));
        rolecbb.setPreferredSize(new Dimension(200, 100));
        add(nametxt);
        add(name);
        add(gendertxt);
        add(gendercbb);
        add(usernametxt);
        add(username);
        add(passwordtxt);
        add(password);
        add(roleIDtxt);
        add((rolecbb));
        add(phonetxt);
        add(phone);
        add(addresstxt);
        add((address));
        add(salarytxt);
        add(salary);
        return panel;
    }
    public JLabel setJlabel(String text){
        JLabel label = new JLabel(text);
        label.setFont(new Font("Verdana", Font.PLAIN, 24));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setForeground(Color.BLACK);
        label.setBackground(Color.WHITE);
        return label;
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setSize(1920, 1080);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new addEmployeeDialog());
        frame.setVisible(true);
    }
}
