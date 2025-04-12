package GUI.Component.Dialog;

import BUS.EmployeeBUS;
import DTO.Employee;
import DTO.Enum.Gender;
import GUI.Component.Button.ButtonBack;
import GUI.Component.Combobox.CustomComboBox;
import GUI.Component.Panel.EmployeePanel;
import GUI.Component.TextField.CustomTextField;
import GUI.Controller.Controller;

import javax.swing.*;
import java.awt.*;

public class AddEmployeeDialog extends JDialog {
    private Long currentID;
    private CustomTextField name;
    private CustomTextField username;
    private CustomTextField password;
    private CustomTextField phone;
    private CustomTextField address;
    private CustomTextField salary;
    private CustomComboBox rolecbb;
    private JRadioButton male;
    private JRadioButton female;

    private EmployeeBUS employeeBUS;
    private EmployeePanel employeePanel;

    public AddEmployeeDialog(JFrame parent, EmployeePanel employeePanel) {
        super(parent, "Thêm Nhân Viên", true);
        this.employeeBUS = new EmployeeBUS();
        this.employeePanel = employeePanel;
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
        addButton.addActionListener(e -> addEmployee());

        bottomPanel.add(cancelButton);
        bottomPanel.add(addButton);

        getContentPane().add(contentPanel, BorderLayout.CENTER);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(1000, 700);
        setResizable(false);
    }

    private boolean fieldController() {
        if (!male.isSelected() && !female.isSelected()) {
            AlertDialog GenderAlert = new AlertDialog(this, "Vui lòng chọn giới tính ! ");
            GenderAlert.setVisible(true);
            male.requestFocus();
            return false;
        }
        if (username.getText().isEmpty()) {
            AlertDialog usernameAlert = new AlertDialog(this, "Vui lòng nhập tài khoản ! ");
            usernameAlert.setVisible(true);
            username.requestFocus();
            return false;
        }
        if (password.getText().isEmpty()) {
            AlertDialog passwordAlert = new AlertDialog(this, "Vui lòng nhập mật khẩu ! ");
            passwordAlert.setVisible(true);
            password.requestFocus();
            return false;
        }
        if (rolecbb.getSelectedItem() == null) {
            AlertDialog roleAlert = new AlertDialog(this, "Vui lòng chọn vai trò ! ");
            roleAlert.setVisible(true);
            rolecbb.requestFocus();
            return false;
        }
        if (address.getText().isEmpty()) {
            AlertDialog addressAlert = new AlertDialog(this, "Vui lòng nhập địa chỉ ! ");
            addressAlert.setVisible(true);
            phone.requestFocus();
            return false;
        }
        if (salary.getText().isEmpty()) {
            AlertDialog salaryAlert = new AlertDialog(this, "Vui lòng nhập lương ! ");
            salaryAlert.setVisible(true);
            salary.requestFocus();
            return false;
        }
        if (!Controller.checkSalary(Float.parseFloat(salary.getText()))) {
            AlertDialog invalidNameAlert = new AlertDialog(this, "Vui lòng nhập lương hợp lệ ! ");
            invalidNameAlert.setVisible(true);
            salary.requestFocus();
            return false;
        }
        if (name.equals("")) {
            AlertDialog blankNameAlert = new AlertDialog(this, "Vui lòng nhập họ và tên ! ");
            blankNameAlert.setVisible(true);
            name.requestFocus();
            return false;
        }

        if (!Controller.checkValidName(name.getText())) {
            AlertDialog invalidNameAlert = new AlertDialog(this, "Vui lòng nhập họ và tên hợp lệ ! ");
            invalidNameAlert.setVisible(true);
            name.requestFocus();
            return false;
        }

        if (phone.equals("")) {
            AlertDialog blankPhoneAlert = new AlertDialog(this, "Vui lòng nhập số điện thoại ! ");
            blankPhoneAlert.setVisible(true);
            phone.requestFocus();
            return false;
        }

        if (!Controller.checkValidPhone(phone.getText())) {
            AlertDialog invalidPhoneAlert = new AlertDialog(this, "Vui lòng nhập số điện thoại hợp lệ ! ");
            invalidPhoneAlert.setVisible(true);
            phone.requestFocus();
            return false;
        }

        if (address.getText().equals("")) {
            AlertDialog blankAddressAlert = new AlertDialog(this, "Vui lòng nhập địa chỉ ! ");
            blankAddressAlert.setVisible(true);
            address.requestFocus();
            return false;
        }
        return true;
    }

    private void setCurrentID(){
        try {
            currentID = employeeBUS.getCurrentID() + 1;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi lấy ID nhân viên: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void addEmployee(){
        setCurrentID();
        if (fieldController()) {
            String fullName = Controller.formatFullName(this.name.getText());
            int lastSpaceOf = fullName.lastIndexOf(" ");
            String fName = "";
            String lName = "";
            if (lastSpaceOf != -1) {
                fName = fullName.substring(0, lastSpaceOf);
                lName = fullName.substring(lastSpaceOf + 1);
            }else{
                lName = fullName;
            }
            String username = this.username.getText();
            String password = this.password.getText();
            String phone = this.phone.getText();
            String address = this.address.getText();
            Long salary = Long.parseLong(this.salary.getText());

            Long roleIDDD;
            if(rolecbb.getSelectedItem() == "admin"){
                roleIDDD = Long.parseLong("1");
            }else if(rolecbb.getSelectedItem() == "staff"){
                roleIDDD = Long.parseLong("2");
            }else roleIDDD = Long.parseLong("3");

            Employee nv = new Employee(
                    fName,
                    lName,
                    male.isSelected() ? Gender.Nam : Gender.Nữ,
                    username,
                    password,
                    roleIDDD,
                    phone,
                    address,
                    salary
            );
            Long uuid = employeeBUS.addEmployee(nv);
            if (uuid == null) {
                AlertDialog alert = new AlertDialog(this, "Thêm nhân viên thất bại !");
                alert.setVisible(true);
                return;
            }
            nv.setId(uuid);
            employeePanel.addEmployee(nv);
            JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công !");
            dispose();
        }
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
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        ButtonBack buttonBack = new ButtonBack();
        buttonBack.addActionListener(e -> dispose());
        buttonBack.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(buttonBack);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    public JPanel textFieldPanel() {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Thiết lập GroupLayout
        layout.setAutoCreateGaps(true); // Tự động tạo khoảng cách giữa các thành phần
        layout.setAutoCreateContainerGaps(true); // Tự động tạo khoảng cách với container

        // Các thành phần
        JLabel nametxt = setJlabel("Tên Nhân Viên");
        JLabel gendertxt = setJlabel("Giới Tính");
        JLabel usernametxt = setJlabel("Tên Đăng Nhập");
        JLabel passwordtxt = setJlabel("Mật Khẩu");
        JLabel roleIDtxt = setJlabel("Vai Trò");
        JLabel phonetxt = setJlabel("Số Điện Thoại");
        JLabel addresstxt = setJlabel("Địa Chỉ");
        JLabel salarytxt = setJlabel("Lương");

        name = new CustomTextField();
        username = new CustomTextField();
        password = new CustomTextField();
        phone = new CustomTextField();
        address = new CustomTextField();
        salary = new CustomTextField();

        ButtonGroup bg = new ButtonGroup();
        male = new JRadioButton("Nam");
        female = new JRadioButton("Nữ");

        // Thiết lập font đẹp hơn
        Font radioFont = new Font("Segoe UI", Font.PLAIN, 14);
        male.setFont(radioFont);
        female.setFont(radioFont);

        bg.add(male);
        bg.add(female);

        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0)); // Sắp xếp ngang
        radioPanel.add(male);
        radioPanel.add(female);

        rolecbb = new CustomComboBox();
        rolecbb.setModel(new DefaultComboBoxModel<>(new String[]{"admin", "staff", "employee"}));

        // Căn chỉnh các thành phần theo hàng ngang và cột dọc
        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(nametxt)
                                .addComponent(gendertxt)
                                .addComponent(usernametxt)
                                .addComponent(passwordtxt)
                                .addComponent(roleIDtxt)
                                .addComponent(phonetxt)
                                .addComponent(addresstxt)
                                .addComponent(salarytxt))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(name)
                                .addComponent(radioPanel)
                                .addComponent(username)
                                .addComponent(password)
                                .addComponent(rolecbb)
                                .addComponent(phone)
                                .addComponent(address)
                                .addComponent(salary))
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(nametxt)
                                .addComponent(name))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED) // Khoảng cách giữa hàng
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(gendertxt)
                                .addComponent(radioPanel))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED) // Khoảng cách giữa hàng
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(usernametxt)
                                .addComponent(username))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED) // Khoảng cách giữa hàng
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(passwordtxt)
                                .addComponent(password))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED) // Khoảng cách giữa hàng
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(roleIDtxt)
                                .addComponent(rolecbb))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED) // Khoảng cách giữa hàng
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(phonetxt)
                                .addComponent(phone))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED) // Khoảng cách giữa hàng
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(addresstxt)
                                .addComponent(address))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED) // Khoảng cách giữa hàng
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(salarytxt)
                                .addComponent(salary))
        );

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
}