package GUI.Component.Panel;

import DTO.Employee;
import DTO.Enum.Gender;
import DTO.ReaderDTO;
import GUI.Component.Button.*;
import GUI.Component.Panel.Components.SearchNavBarLabel;
import GUI.Component.Table.BorrowDetailTable;
import GUI.Component.Table.BorrowinSheetTable;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BorrowPanel extends JPanel {

    private ButtonAdd buttonAdd;
    private ButtonUpdate buttonUpdate;
    private ButtonDelete buttonDelete;
    private ButtonExportExcel buttonExportExcel;
    private ButtonImportExcel buttonImportExcel;
    private SearchNavBarLabel searchNavBarLabel;

    private JPanel readerPanel;
    private JPanel employeePanel;

    private BorrowDetailTable borrowDetailTable;
    private BorrowinSheetTable borrowinSheetTable;


    private JFrame parentFrame;

    public BorrowPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout(0, 5)); // Giảm khoảng cách giữa các thành phần

        // Panel nút chức năng
        this.add(buttonPanel(parentFrame), BorderLayout.NORTH);

        // Bảng chính
        borrowinSheetTable = new BorrowinSheetTable();
        JScrollPane scrollPane = new JScrollPane(borrowinSheetTable);
        add(scrollPane, BorderLayout.CENTER);

        // Panel dưới cùng
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 0)); // Thêm khoảng cách ngang 10px

        // Panel thông tin (nhân viên + độc giả)
        JPanel infoPanel = employeeNreaderPanel(new Employee(1L, "Hoàng", "Quý"),
                new ReaderDTO(1L, "Nguyễn", "Thành", Gender.Nam, "0987654321", "Hà Nội"));
        infoPanel.setPreferredSize(new Dimension(400, 150)); // Tăng chiều rộng

        // Bảng chi tiết
        borrowDetailTable = new BorrowDetailTable();
        customizeDetailTable(); // Tùy chỉnh bảng chi tiết
        bottomPanel.setBackground(new Color(240, 240, 240));
        JScrollPane detailScrollPane = new JScrollPane(borrowDetailTable);
        detailScrollPane.setPreferredSize(new Dimension(300, 180));
        borrowDetailTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        detailScrollPane.setBorder(BorderFactory.createTitledBorder("Chi tiết phiếu mượn"));
        bottomPanel.setPreferredSize(new Dimension(0, 220));
        bottomPanel.add(infoPanel, BorderLayout.WEST);
        bottomPanel.add(detailScrollPane, BorderLayout.CENTER);

        // Thêm padding xung quanh
        JPanel paddedPanel = new JPanel(new BorderLayout());
        paddedPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        paddedPanel.add(bottomPanel, BorderLayout.CENTER);
        paddedPanel.setBackground(new Color(240, 240, 240));


        this.add(paddedPanel, BorderLayout.SOUTH);
    }
    private void customizeDetailTable() {
        // Đặt kích thước cột
        TableColumnModel columnModel = borrowDetailTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100);  // Cột 1
        columnModel.getColumn(1).setPreferredWidth(100); // Cột 2
        columnModel.getColumn(2).setPreferredWidth(100);// Cột 3
        columnModel.getColumn(3).setPreferredWidth(100);

        // Đặt font và chiều cao
        borrowDetailTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        borrowDetailTable.setRowHeight(25);
        borrowDetailTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Tắt tự động resize
        borrowDetailTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }


    public JPanel buttonPanel(JFrame parentFrame) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        buttonPanel.setBackground(Color.WHITE);
        buttonAdd = new ButtonAdd();
        buttonAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
        });
        buttonUpdate = new ButtonUpdate();
        buttonUpdate.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
            }
        });
        buttonDelete = new ButtonDelete();
        buttonDelete.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
            }
        });
        buttonExportExcel = new ButtonExportExcel();
        buttonImportExcel = new ButtonImportExcel();
        searchNavBarLabel = new SearchNavBarLabel();
        buttonPanel.add(buttonAdd);
        buttonPanel.add(buttonUpdate);
        buttonPanel.add(buttonDelete);
        buttonPanel.add(buttonExportExcel);
        buttonPanel.add(buttonImportExcel);
        buttonPanel.add(Box.createRigidArea(new Dimension(60, 0)));
        buttonPanel.add(searchNavBarLabel);
        return buttonPanel;
    }

    private JPanel employeeNreaderPanel(Employee employee, ReaderDTO readerDTO) {
        JPanel container = new JPanel(new GridBagLayout()); // Dùng GridBagLayout để tùy chỉnh kích thước tốt hơn
        container.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        container.setBackground(new Color(245, 245, 245)); // Màu nền sáng

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Panel nhân viên (40% chiều cao)
        employeePanel = new JPanel(new GridLayout(2, 1));
        employeePanel.setBorder(BorderFactory.createTitledBorder("Thông tin Nhân viên"));
        employeePanel.setBackground(Color.WHITE);
        employeePanel.setPreferredSize(new Dimension(350, 80)); // Đảm bảo đủ không gian
        employeePanel.add(createStyledLabel("Mã NV: " + employee.getId()));
        employeePanel.add(createStyledLabel("Họ tên: " + employee.getFirstName() + " " + employee.getLastName()));

        gbc.gridy = 0;
        gbc.weighty = 0.40; // Chiếm 40% chiều cao
        container.add(employeePanel, gbc);

        // Panel độc giả (60% chiều cao)
        readerPanel = new JPanel(new GridLayout(5, 1));
        readerPanel.setBorder(BorderFactory.createTitledBorder("Thông tin Độc giả"));
        readerPanel.setBackground(Color.WHITE);
        readerPanel.setPreferredSize(new Dimension(350, 120)); // Đủ không gian cho 5 dòng thông tin
        readerPanel.add(createStyledLabel("Mã DG: " + readerDTO.getId()));
        readerPanel.add(createStyledLabel("Họ tên: " + readerDTO.getFirstName() + " " + readerDTO.getLastName()));
        readerPanel.add(createStyledLabel("Giới tính: " + readerDTO.getGender()));
        readerPanel.add(createStyledLabel("Điện thoại: " + readerDTO.getPhone()));
        readerPanel.add(createStyledLabel("Địa chỉ: " + readerDTO.getAddress()));

        gbc.gridy = 1;
        gbc.weighty = 0.60; // Chiếm 60% chiều cao
        container.add(readerPanel, gbc);

        return container;
    }


    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        return label;
    }
}
