package GUI.Component.Dialog;

import GUI.Component.Button.ButtonBack;
import GUI.Component.Combobox.CustomComboBox;
import GUI.Component.TextField.CustomTextField;

import javax.swing.*;
import java.awt.*;

public class EditEmployeeDialog extends JDialog {
    public EditEmployeeDialog(JFrame parent) {
        super(parent, "Cập Nhật Nhân Viên", true);
        initComponents();
        pack();
        setLocationRelativeTo(parent);
    }

    public void initComponents() {
        getContentPane().setLayout(new BorderLayout(10, 10));
        JPanel title = title();
        getContentPane().add(title, BorderLayout.NORTH);
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        JPanel buttonPanel = buttonPanel();
        contentPanel.add(buttonPanel, BorderLayout.WEST);
        JPanel inputPanel = textFieldPanel();
        contentPanel.add(inputPanel, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton cancelButton = new JButton("Hủy bỏ");
        cancelButton.setBackground(new Color(218, 42, 0));
        cancelButton.setForeground(Color.white);
        cancelButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        cancelButton.setPreferredSize(new Dimension(120, 40));
        cancelButton.addActionListener(e -> dispose());

        JButton addButton = new JButton("Thêm nhân viên");
        addButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        addButton.setPreferredSize(new Dimension(180, 40));
        addButton.setBackground(new Color(0, 120, 215));
        addButton.setForeground(Color.WHITE);
        addButton.addActionListener(e -> editEmployee());

        bottomPanel.add(cancelButton);
        bottomPanel.add(addButton);

        getContentPane().add(contentPanel, BorderLayout.CENTER);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1000, 700);
        setResizable(false);
    }
    public void editEmployee(){
        // xu li
    }
    public JPanel title(){
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JLabel txt = new JLabel("Cập Nhật Nhân Viên");
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
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        ButtonBack buttonBack = new ButtonBack();
        buttonBack.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(buttonBack);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    public JPanel textFieldPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 2, -2, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 5, 20));

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
        CustomComboBox rolecbb = new CustomComboBox();
        rolecbb.setModel(new DefaultComboBoxModel(new String[]{"admin", "employee", "staff"}));

        panel.add(nametxt);
        panel.add(name);
        panel.add(gendertxt);
        panel.add(gendercbb);
        panel.add(usernametxt);
        panel.add(username);
        panel.add(passwordtxt);
        panel.add(password);
        panel.add(roleIDtxt);
        panel.add(rolecbb);
        panel.add(phonetxt);
        panel.add(phone);
        panel.add(addresstxt);
        panel.add(address);
        panel.add(salarytxt);
        panel.add(salary);

        return panel;
    }

    public JLabel setJlabel(String text){
        JLabel label = new JLabel(text);
        label.setFont(new Font("Verdana", Font.PLAIN, 16));
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setForeground(Color.BLACK);
        label.setBackground(Color.WHITE);
        return label;
    }

//    public static void main(String[] args) {
//        JFrame frame = new JFrame();
//        frame.setSize(1920, 1080);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        EditEmployeeDialog dialog = new EditEmployeeDialog(frame);
//        dialog.setVisible(true);
//    }
}