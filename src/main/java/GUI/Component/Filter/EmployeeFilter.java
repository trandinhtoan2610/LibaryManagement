package GUI.Component.Filter;

import GUI.Component.Panel.EmployeePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EmployeeFilter extends JDialog {
    private JLabel chucvuLabel = new JLabel("Chức Vụ");
    private JRadioButton none = new JRadioButton("Tất Cả");
    private JRadioButton admin = new JRadioButton("Admin");
    private JRadioButton staff = new JRadioButton("Quản Lý");
    private JRadioButton employee = new JRadioButton("Nhân Viên");
    private ButtonGroup group = new ButtonGroup();

    private JLabel salaryLabel = new JLabel("Khoảng lương:");
    private JTextField minSalaryField = new JTextField(10);
    private JTextField maxSalaryField = new JTextField(10);
    private JLabel toLabel = new JLabel("đến");

    private JButton filterButton = new JButton("Lọc");
    private JButton cancelButton = new JButton("Hủy");

    private EmployeeFilterListener filterListener;

    public EmployeeFilter(JFrame parent) {
        super(parent, "Lọc nhân viên", true);
        setSize(400, 400);

        if (parent != null) {
            // Lấy vị trí và kích thước của parent
            Rectangle parentBounds = parent.getBounds();

            // Tính toán vị trí mới - căn phải
            int x = parentBounds.x + parentBounds.width - getWidth() - 20; // 20px padding
            int y = parentBounds.y + 140; // 50px từ top

            setLocation(x, y);
        } else {
            setLocationRelativeTo(null); // Fallback nếu không có parent
        }

        setupUI();
        setupEvents();
    }


    private void setupUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel positionPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        positionPanel.setBorder(BorderFactory.createTitledBorder("Lọc theo chức vụ"));

        group.add(none);
        group.add(admin);
        group.add(staff);
        group.add(employee);
        none.setSelected(true);

        positionPanel.add(chucvuLabel);
        positionPanel.add(none);
        positionPanel.add(admin);
        positionPanel.add(staff);
        positionPanel.add(employee);

        // Panel lương
        JPanel salaryPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        salaryPanel.setBorder(BorderFactory.createTitledBorder("Lọc theo khoảng lương"));

        salaryPanel.add(salaryLabel);
        salaryPanel.add(new JLabel()); // placeholder
        salaryPanel.add(new JLabel("Từ:"));
        salaryPanel.add(minSalaryField);
        salaryPanel.add(new JLabel("Đến:"));
        salaryPanel.add(maxSalaryField);

        // Panel button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(filterButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(positionPanel, BorderLayout.NORTH);
        mainPanel.add(salaryPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private boolean checkSalary(double minSalary, double maxSalary) {
        if(minSalary < 0 || maxSalary < 0) {
            JOptionPane.showMessageDialog(this, "Lương không được âm", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if(minSalary > maxSalary) {
            JOptionPane.showMessageDialog(this, "Lương tối thiểu không được lớn hơn lương tối đa", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    private void setupEvents() {
        filterButton.addActionListener(e -> {
            if (filterListener != null) {
                try {
                    String position = "";
                    if (admin.isSelected()) position = "Admin";
                    else if (staff.isSelected()) position = "Quản Lý";
                    else if (employee.isSelected()) position = "Nhân Viên";
                    double minSalary = minSalaryField.getText().isEmpty() ? 0 :
                            Double.parseDouble(minSalaryField.getText());
                    double maxSalary = maxSalaryField.getText().isEmpty() ? Double.MAX_VALUE :
                            Double.parseDouble(maxSalaryField.getText());
                    if (checkSalary(minSalary, maxSalary)) {
                        filterListener.onFilterApplied(position, minSalary, maxSalary);
                    }else{
                        return;
                    }
                    dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(EmployeeFilter.this,
                            "Vui lòng nhập số hợp lệ cho khoảng lương",
                            "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    public void setEmployeeFilterListener(EmployeeFilterListener listener) {
        this.filterListener = listener;
    }

    public interface EmployeeFilterListener {
        void onFilterApplied(String position, double minSalary, double maxSalary);
    }


}