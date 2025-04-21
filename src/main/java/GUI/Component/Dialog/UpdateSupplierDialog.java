package GUI.Component.Dialog;

import DTO.SupplierDTO;
import BUS.SupplierBUS;
import GUI.Component.Panel.SupplierPanel;
import GUI.Component.TextField.CustomTextField;
import GUI.Controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UpdateSupplierDialog extends JDialog {
    private String currentID;
    private CustomTextField id;
    private CustomTextField name;
    private CustomTextField phone;
    private CustomTextField address;

    private SupplierDTO supplierToUpdate;
    private SupplierBUS supplierBUS;
    private SupplierPanel supplierPanel;
    private List<SupplierDTO> supplierList; // Thêm danh sách nhà cung cấp

    public UpdateSupplierDialog(JFrame parent, SupplierPanel supplierPanel, SupplierDTO supplierToUpdate, List<SupplierDTO> supplierList) {
        super(parent, "Cập Nhật Nhà Cung Cấp", true);
        this.supplierPanel = supplierPanel;
        this.supplierBUS = new SupplierBUS();
        this.supplierToUpdate = supplierToUpdate;
        this.supplierList = supplierList; // Khởi tạo danh sách nhà cung cấp
        initComponents();
        if (supplierToUpdate != null) {
            fillSupplierData();
        }
        pack();
        setLocationRelativeTo(parent);
    }

    private void fillSupplierData() {
        if (supplierToUpdate != null) {
            currentID = supplierToUpdate.getId();
            id.setText(currentID);
            name.setText(supplierToUpdate.getName());
            phone.setText(supplierToUpdate.getPhone());
            address.setText(supplierToUpdate.getAddress());
        }
    }

    public void initComponents() {
        getContentPane().setLayout(new BorderLayout(10, 10));
        JPanel title = title();
        getContentPane().add(title, BorderLayout.NORTH);
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        JPanel inputPanel = textFieldPanel();
        contentPanel.add(inputPanel, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton updateButton = new JButton("Cập nhật");
        updateButton.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        updateButton.setPreferredSize(new Dimension(130, 40));
        updateButton.setBackground(new Color(0, 120, 215));
        updateButton.setForeground(Color.WHITE);
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateSupplier();
            }
        });

        JButton cancelButton = new JButton("Hủy bỏ");
        cancelButton.setBackground(new Color(218, 42, 0));
        cancelButton.setForeground(Color.white);
        cancelButton.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        cancelButton.setPreferredSize(new Dimension(100, 40));
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        bottomPanel.add(updateButton);
        bottomPanel.add(cancelButton);

        getContentPane().add(contentPanel, BorderLayout.CENTER);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 550);
        setResizable(false);
    }

    private boolean fieldController() {
        if (id.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã nhà cung cấp ! ");
            id.requestFocus();
            return false;
        }

        if (name.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên nhà cung cấp ! ");
            name.requestFocus();
            return false;
        }

        if (phone.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số điện thoại ! ");
            phone.requestFocus();
            return false;
        }

        if (!Controller.checkValidPhone(phone.getText())) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ ! ");
            phone.requestFocus();
            return false;
        }

        if (address.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập địa chỉ ! ");
            address.requestFocus();
            return false;
        }
        return true;
    }

    public void updateSupplier() {
        String oldName = this.name.getText();
        String oldPhone = this.phone.getText();
        String oldAddress = this.address.getText();
        String oldID = currentID; // Lưu lại ID cũ để so sánh
        String newID = id.getText().trim(); // Lấy ID đã chỉnh sửa

        if (fieldController()) {
            if (!newID.equals(oldID) && isSupplierIdDuplicate(newID)) {
                JOptionPane.showMessageDialog(this, "Mã nhà cung cấp đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                id.requestFocus();
                return; // Không cho phép cập nhật nếu ID mới bị trùng
            }

            String supplierName = this.name.getText();
            String supplierPhone = this.phone.getText();
            String supplierAddress = this.address.getText();

            SupplierDTO updatedSupplier = new SupplierDTO(
                    newID, // Sử dụng ID đã chỉnh sửa (newID)
                    supplierName,
                    supplierPhone,
                    supplierAddress
            );

            boolean success = supplierBUS.updateSupplier(updatedSupplier, oldID);
            if (success) {
                supplierPanel.reloadSupplierTable(); 
                JOptionPane.showMessageDialog(this, "Cập nhật thành công");
                this.dispose();
            } else {
                this.id.setText(oldID); // Khôi phục ID cũ
                this.name.setText(oldName);
                this.phone.setText(oldPhone);
                this.address.setText(oldAddress);
                JOptionPane.showMessageDialog(this, "Cập nhật nhà cung cấp thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);
                currentID = oldID; 
                fillSupplierData(); // Hiển thị lại ID cũ trên form
            }
        }
    }

    private boolean isSupplierIdDuplicate(String idToCheck) {
        // Kiểm tra xem IDToCheck có tồn tại trong danh sách nhà cung cấp (trừ nhà cung cấp đang được chỉnh sửa)
        return supplierList.stream()
                .anyMatch(s -> s.getId().equals(idToCheck) && !s.getId().equals(currentID));
    }
    public void setCurrentID(String id) {
        this.currentID = id;
    }

    public JPanel title() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JLabel txt = new JLabel("Cập Nhật Nhà Cung Cấp");
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

    public JPanel textFieldPanel() {
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JLabel idtxt = setJlabel("Mã Nhà Cung Cấp");
        JLabel nametxt = setJlabel("Tên Nhà Cung Cấp");
        JLabel phonetxt = setJlabel("Số Điện Thoại");
        JLabel addresstxt = setJlabel("Địa Chỉ");

        id = new CustomTextField();
        name = new CustomTextField();
        phone = new CustomTextField();
        address = new CustomTextField();

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(idtxt)
                                .addComponent(nametxt)
                                .addComponent(phonetxt)
                                .addComponent(addresstxt))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(id)
                                .addComponent(name)
                                .addComponent(phone)
                                .addComponent(address))
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(idtxt)
                                .addComponent(id))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(nametxt)
                                .addComponent(name))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(phonetxt)
                                .addComponent(phone))
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(addresstxt)
                                .addComponent(address))
        );

        return panel;
    }

    public JLabel setJlabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Verdana", Font.PLAIN, 16));
        label.setHorizontalAlignment(SwingConstants.LEFT);
        label.setForeground(Color.BLACK);
        label.setBackground(Color.WHITE);
        return label;
    }
}