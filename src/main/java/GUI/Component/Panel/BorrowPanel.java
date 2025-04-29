package GUI.Component.Panel;

import BUS.*;
import DTO.*;
import DTO.Enum.PayStatus;
import DTO.Enum.Status;
import DTO.Enum.SubStatus;
import GUI.Component.Button.*;
import GUI.Component.Dialog.AddBorrowDialog;
import GUI.Component.Dialog.AlertDialog;
import GUI.Component.Dialog.DeleteBorrowDialog;
import GUI.Component.Dialog.UpdateBorrowDialog;
import GUI.Component.Table.BookTable;
import GUI.Component.Table.BorrowDetailTable;
import GUI.Component.Table.BorrowinSheetTable;
import GUI.Component.TextField.RoundedTextField;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BorrowPanel extends JPanel {
    private final BorrowSheetBUS borrowSheetBUS = new BorrowSheetBUS();
    private final BorrowDetailBUS borrowDetailBUS = new BorrowDetailBUS();
    private final EmployeeBUS employeeBUS = new EmployeeBUS();
    private final ReaderBUS readerBUS = new ReaderBUS();
    private final BookBUS bookBUS = new BookBUS();
    private ButtonAdd buttonAdd;
    private ButtonUpdate buttonUpdate;
    private ButtonDelete buttonDelete;
    private ButtonPaidBook buttonPaidBook;
    private ButtonExportPDF buttonExportPDF;
    private JPanel searchNavBarLabel;
    private RoundedTextField searchfield;
    private JComboBox<String> searchOptionsComboBox;
    private JRadioButton allRadioButton;
    private JRadioButton borrowedRadioButton;
    private JRadioButton returnedRadioButton;
    private JRadioButton overdueRadioButton;
    private ButtonGroup buttonGroup;
    private ButtonRefresh buttonRefresh;
    private ButtonFilter filterButton;

    private JPanel readerPanel;
    private JPanel employeePanel;

    private BorrowDetailTable borrowDetailTable;
    private BorrowinSheetTable borrowinSheetTable;

    private Employee currentEmployee;
    private ReaderDTO currentReader;

    private BookTable bookTable;
    private TableRowSorter<TableModel> sorter;

    private JFrame parentFrame;

    public BorrowPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        setLayout(new BorderLayout(0, 5));
        this.add(buttonPanel(parentFrame), BorderLayout.NORTH);
        borrowinSheetTable = new BorrowinSheetTable();
        borrowinSheetTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                updateEmployeeAndReaderInfo();
                updateBorrowDetailInfo();
            }
        });
        sorter = new TableRowSorter<>(borrowinSheetTable.getModel());
        borrowinSheetTable.setRowSorter(sorter);
        JScrollPane scrollPane = new JScrollPane(borrowinSheetTable);
        add(scrollPane, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 0)); // Thêm khoảng cách ngang 10px

        JPanel infoPanel = employeeNreaderPanel();
        infoPanel.setPreferredSize(new Dimension(400, 150));

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

        JPanel paddedPanel = new JPanel(new BorderLayout());
        paddedPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        paddedPanel.add(bottomPanel, BorderLayout.CENTER);
        paddedPanel.setBackground(new Color(240, 240, 240));
        this.add(paddedPanel, BorderLayout.SOUTH);
        addRightClickMenutoDetailTable();
        loadData();
    }
    private void addRightClickMenutoDetailTable() {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItemBorrowing = new JMenuItem("Đang mượn");
        menuItemBorrowing.addActionListener(e -> updateSelectedDetailStatus(SubStatus.Đang_Mượn));
        JMenuItem menuItemReturned = new JMenuItem("Đã Trả");
        menuItemReturned.addActionListener(e -> updateSelectedDetailStatus(SubStatus.Đã_Trả));
        JMenuItem menuItemLostBook = new JMenuItem("Mất Sách");
        menuItemLostBook.addActionListener(e -> updateSelectedDetailStatus(SubStatus.Mất_Sách));
        JMenuItem menuItemBrokenBook = new JMenuItem("Hư Sách");
        menuItemBrokenBook.addActionListener(e -> updateSelectedDetailStatus(SubStatus.Hư_Sách));
        popupMenu.add(menuItemBorrowing);
        popupMenu.add(menuItemReturned);
        popupMenu.add(menuItemLostBook);
        popupMenu.add(menuItemBrokenBook);
        borrowDetailTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = borrowDetailTable.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        BorrowDetailDTO selectedDetails = borrowDetailTable.getBorrowDetailAt(row);
                        if (selectedDetails.getStatus() == SubStatus.Đang_Mượn){
                            menuItemBorrowing.setVisible(false);
                            menuItemReturned.setVisible(true);
                            menuItemLostBook.setVisible(true);
                            menuItemBrokenBook.setVisible(true);
                        }else {
                            menuItemBorrowing.setVisible(true);
                            menuItemReturned.setVisible(false);
                            menuItemLostBook.setVisible(false);
                            menuItemBrokenBook.setVisible(false);
                        }
                        borrowDetailTable.setRowSelectionInterval(row, row);
                        popupMenu.show(borrowDetailTable, e.getX(), e.getY());
                    }
                }
            }
        });
    }
    private void updateBorrowSheetStatus(BorrowDTO borrowDTO){
        List<BorrowDetailDTO> borrowDetailList = borrowDetailBUS.getBorrowDetailsBySheetId(Long.parseLong(borrowDTO.getId().substring(2)));
        boolean hasBorrowing = false;
        boolean hasOverdue = false;
        boolean hasLostBook = false;
        boolean hasBrokenBook = false;
        for (BorrowDetailDTO borrowDetail : borrowDetailList) {
            if (borrowDetail.getStatus() == SubStatus.Đang_Mượn){
                hasBorrowing = true;
            }
            if (borrowDetail.getStatus() == SubStatus.Quá_Hạn){
                hasOverdue = true;
            }
            if (borrowDetail.getStatus() == SubStatus.Mất_Sách){
                hasLostBook = true;
            }
            if (borrowDetail.getStatus() == SubStatus.Hư_Sách){
                hasBrokenBook = true;
            }
        }
        if (hasBorrowing) {
            borrowDTO.setStatus(Status.Đang_Mượn);
            borrowDTO.setActualReturnDate(null);
        } else {
            if (hasOverdue || hasLostBook || hasBrokenBook) {
                borrowDTO.setStatus(Status.Phạt);
            } else {
                borrowDTO.setStatus(Status.Đã_Trả);
            }
            borrowDTO.setActualReturnDate(new Date());
        }
        borrowSheetBUS.updateBorrowSheet(borrowDTO);
        if(borrowDTO.getStatus().equals(Status.Phạt)) {
            PenaltyBUS penaltyBUS = new PenaltyBUS();
            PenaltyDetailsBUS penaltyDetailsBUS = new PenaltyDetailsBUS();
            try {
                PenaltyDTO p = new PenaltyDTO(
                        penaltyBUS.getCurrentID(),
                        borrowDTO.getActualReturnDate(),
                        PayStatus.Chưa_Thanh_Toán,
                        0L, null, null
                );
                penaltyBUS.addPenaltySheet(p);
                penaltyDetailsBUS.addPenaltyDetails(p.getId(), borrowDTO);
                Long totalAmount = penaltyDetailsBUS.getTotalFee(p.getId());
                p.setTotalAmount(totalAmount);
                penaltyBUS.updatePenaltySheet(p);
                PenaltyPanel.reloadTable();
            }catch (Exception e) {
                throw new RuntimeException("Tạo phiếu phạt thất bại");
            }
        }
    }
    private void updateSelectedDetailStatus(SubStatus newStatus){
        int selectedRow = borrowDetailTable.getSelectedRow();
        if (selectedRow < 0){
            new AlertDialog(parentFrame, "Vui lòng chọn một chi tiết mượn sách").setVisible(true);
            return;
        }
        try {
            BorrowDetailDTO selectedDetail = borrowDetailTable.getBorrowDetailAt(selectedRow);
            if (selectedDetail == null){
                new AlertDialog(parentFrame, "Không tìm thấy chi tiết mượn sách");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(parentFrame,
                    "Xác nhận chuyển trạng thái sang " + newStatus + " ?",
                    "Xác nhận", JOptionPane.YES_OPTION
                    );
            if (confirm != JOptionPane.YES_OPTION){
                return;
            }
            SubStatus oldStatus = selectedDetail.getStatus();
            BorrowDTO borrowSelected = borrowinSheetTable.getSelectedBorrow();
            Date currentDate = new Date();

            SubStatus finalStatus = newStatus;
            if (newStatus == SubStatus.Đã_Trả && currentDate.after(borrowSelected.getDuedate())) {
                finalStatus = SubStatus.Quá_Hạn;
            }
            selectedDetail.setStatus(finalStatus);
            if (finalStatus == SubStatus.Đã_Trả || finalStatus == SubStatus.Quá_Hạn || finalStatus == SubStatus.Mất_Sách || finalStatus == SubStatus.Hư_Sách) {
                selectedDetail.setActualReturnDate(new Date());
            } else {
                selectedDetail.setActualReturnDate(null);
            }

            Book selectedBook = bookBUS.getBookById(selectedDetail.getBookId());
            if (selectedBook != null){
                if (oldStatus == SubStatus.Đang_Mượn && finalStatus != SubStatus.Đang_Mượn){
                    if (finalStatus == SubStatus.Đã_Trả || finalStatus == SubStatus.Quá_Hạn){
                        selectedBook.setBorrowedQuantity(selectedBook.getBorrowedQuantity() - selectedDetail.getQuantity());
                    }
                    if (finalStatus == SubStatus.Hư_Sách || finalStatus == SubStatus.Mất_Sách){
                        selectedBook.setQuantity(selectedBook.getQuantity() - selectedDetail.getQuantity());
                        selectedBook.setBorrowedQuantity(selectedBook.getBorrowedQuantity() - selectedDetail.getQuantity());
                    }
                }else if (oldStatus != SubStatus.Đang_Mượn && finalStatus == SubStatus.Đang_Mượn){
                    if (oldStatus == SubStatus.Đã_Trả || oldStatus == SubStatus.Quá_Hạn){
                        selectedBook.setBorrowedQuantity(selectedBook.getBorrowedQuantity() + selectedDetail.getQuantity());
                    }
                    if (oldStatus == SubStatus.Hư_Sách || oldStatus == SubStatus.Mất_Sách){
                        selectedBook.setQuantity(selectedBook.getQuantity() + selectedDetail.getQuantity());
                        selectedBook.setBorrowedQuantity(selectedBook.getBorrowedQuantity() + selectedDetail.getQuantity());
                    }
                }
                bookBUS.updateBook(selectedBook);
                BookPanel.loadData();
            }
            borrowDetailBUS.updateBorrowDetail(selectedDetail);
            if (borrowSelected != null){
               updateBorrowSheetStatus(borrowSelected);
            }
            refreshData();
            new AlertDialog(parentFrame, "Cập nhật trạng thái thành công").setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void customizeDetailTable() {
        TableColumnModel columnModel = borrowDetailTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100);  // Cột 1
        columnModel.getColumn(1).setPreferredWidth(100); // Cột 2
        columnModel.getColumn(2).setPreferredWidth(100);// Cột 3
        borrowDetailTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        borrowDetailTable.setRowHeight(25);
        borrowDetailTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        borrowDetailTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    }


    public JPanel buttonPanel(JFrame parentFrame) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        buttonPanel.setBackground(Color.WHITE);
        buttonAdd = new ButtonAdd();
        buttonAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AddBorrowDialog addBorrowDialog = new AddBorrowDialog(parentFrame, BorrowPanel.this);
                addBorrowDialog.setVisible(true);
                refreshTable();
            }
        });
        buttonUpdate = new ButtonUpdate();
        buttonUpdate.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                BorrowDTO selectedBorrow = borrowinSheetTable.getSelectedBorrow();
                if (selectedBorrow == null) {
                    AlertDialog updateAlert = new AlertDialog(parentFrame, "Vui lòng chọn phiếu mượn cần sửa");
                    updateAlert.setVisible(true);
                } else {
                    UpdateBorrowDialog updateBorrowDialog = new UpdateBorrowDialog(parentFrame, BorrowPanel.this, selectedBorrow);
                    updateBorrowDialog.setVisible(true);
                    updateBorrow(selectedBorrow);
                    refreshTable();
                }
            }
        });
        buttonDelete = new ButtonDelete();
        buttonDelete.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                BorrowDTO selectedBorrow = borrowinSheetTable.getSelectedBorrow();
                if (selectedBorrow == null) {
                    AlertDialog deleteAlert = new AlertDialog(parentFrame, "Vui lòng chọn phiếu mượn cần xóa");
                    deleteAlert.setVisible(true);
                } else {
                    DeleteBorrowDialog deleteBorrowDialog = new DeleteBorrowDialog(parentFrame, BorrowPanel.this, selectedBorrow);
                    deleteBorrowDialog.setVisible(true);
                    refreshTable();
                }
            }
        });
        buttonPaidBook = new ButtonPaidBook();
        buttonPaidBook.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                BorrowDTO selectedBorrow = borrowinSheetTable.getSelectedBorrow();
                actionPaidBook(selectedBorrow);
            }
        });
        buttonExportPDF = new ButtonExportPDF();
        buttonExportPDF.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                BorrowDTO selectedBorrow = borrowinSheetTable.getSelectedBorrow();
                exportToPDF(selectedBorrow);
            }
        });
        searchNavBarLabel = getSearchNavBarLabel();
        buttonPanel.add(buttonAdd);
        buttonPanel.add(buttonUpdate);
        buttonPanel.add(buttonDelete);
        buttonPanel.add(buttonPaidBook);
        buttonPanel.add(buttonExportPDF);
        buttonPanel.add(Box.createRigidArea(new Dimension(175, 0)));
        buttonPanel.add(searchNavBarLabel);
        return buttonPanel;
    }

    private JPanel employeeNreaderPanel() {
        JPanel container = new JPanel(new GridBagLayout()); // Dùng GridBagLayout để tùy chỉnh kích thước tốt hơn
        container.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        container.setBackground(new Color(245, 245, 245)); // Màu nền sáng
        getEmployeeNReaderbyId();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        employeePanel = new JPanel(new GridLayout(2, 1));
        employeePanel.setBorder(BorderFactory.createTitledBorder("Thông tin Nhân viên"));
        employeePanel.setBackground(Color.WHITE);
        employeePanel.setPreferredSize(new Dimension(350, 80));
        employeePanel.add(createStyledLabel("Mã NV: "));
        employeePanel.add(createStyledLabel("Họ tên: "));
        gbc.gridy = 0;
        gbc.weighty = 0.40;
        container.add(employeePanel, gbc);
        readerPanel = new JPanel(new GridLayout(5, 1));
        readerPanel.setBorder(BorderFactory.createTitledBorder("Thông tin Độc giả"));
        readerPanel.setBackground(Color.WHITE);
        readerPanel.setPreferredSize(new Dimension(350, 120));
        readerPanel.add(createStyledLabel("Mã DG: "));
        readerPanel.add(createStyledLabel("Họ tên: "));
        readerPanel.add(createStyledLabel("Giới tính: "));
        readerPanel.add(createStyledLabel("Điện thoại: "));
        readerPanel.add(createStyledLabel("Địa chỉ: "));
        gbc.gridy = 1;
        gbc.weighty = 0.60;
        container.add(readerPanel, gbc);
        return container;
    }
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        return label;
    }
    private void loadData() {
        List<BorrowDTO> borrows = BorrowSheetBUS.borrowSheetList;
        if (borrows != null) {
            borrowinSheetTable.setBorrows(borrows);
        }else{
            System.out.println("Không có dữ liệu nhân viên");
        }
    }

    public void refreshTable() {
        borrowinSheetTable.refreshTable();
    }

    public void addBorrow(BorrowDTO borrowDTO) {
        borrowinSheetTable.addBorrow(borrowDTO);
    }

    public void updateBorrow(BorrowDTO borrowDTO) {
        borrowinSheetTable.updateBorrow(borrowDTO);
    }

    public void deleteBorrow(BorrowDTO borrowDTO) {
        borrowinSheetTable.removeBorrow(borrowDTO);
    }
    private void getEmployeeNReaderbyId() {
        int selectedRow = borrowinSheetTable.getSelectedRow();
        if (selectedRow != -1) {
            BorrowDTO selectedBorrow = borrowinSheetTable.getSelectedBorrow();
            currentEmployee = employeeBUS.getEmployeeById(selectedBorrow.getEmployeeId());
            currentReader = readerBUS.findReaderByID(selectedBorrow.getReaderId());
        }
    }

    private void updateEmployeeAndReaderInfo() {
        int selectedRow = borrowinSheetTable.getSelectedRow();
        if (selectedRow != -1) {
            BorrowDTO selectedBorrow = borrowinSheetTable.getSelectedBorrow();
            currentEmployee = employeeBUS.getEmployeeById(selectedBorrow.getEmployeeId());
            currentReader = readerBUS.findReaderByID(selectedBorrow.getReaderId());

            employeePanel.removeAll();
            employeePanel.setLayout(new GridLayout(2, 1));
            employeePanel.add(createStyledLabel("Mã NV: " + currentEmployee.getId()));
            employeePanel.add(createStyledLabel("Họ tên: " + currentEmployee.getFirstName() + " " + currentEmployee.getLastName()));
            employeePanel.setBorder(BorderFactory.createTitledBorder("Thông tin Nhân viên"));

            readerPanel.removeAll();
            readerPanel.setLayout(new GridLayout(5, 1));
            readerPanel.add(createStyledLabel("Mã DG: " + currentReader.getId()));
            readerPanel.add(createStyledLabel("Họ tên: " + currentReader.getLastName() + " " + currentReader.getFirstName()));
            readerPanel.add(createStyledLabel("Giới tính: " + currentReader.getGender().toString()));
            readerPanel.add(createStyledLabel("Điện thoại: " + currentReader.getPhone()));
            readerPanel.add(createStyledLabel("Địa chỉ: " + currentReader.getAddress()));
            readerPanel.setBorder(BorderFactory.createTitledBorder("Thông tin Độc giả"));

            employeePanel.revalidate();
            employeePanel.repaint();
            readerPanel.revalidate();
            readerPanel.repaint();
        }
    }
    private void updateBorrowDetailInfo() {
        int selectedRow = borrowinSheetTable.getSelectedRow();
        if (selectedRow != -1) {
            BorrowDTO selectedBorrow = borrowinSheetTable.getSelectedBorrow();
            System.out.println(selectedBorrow.getId());
            List<BorrowDetailDTO> borrowDetails = borrowDetailBUS.getBorrowDetailsBySheetId(Long.parseLong(selectedBorrow.getId().substring(2)));
            borrowDetailTable.setBorrowDetails(borrowDetails);
        }
    }
    public JPanel getSearchNavBarLabel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 10));
        topPanel.setBackground(Color.WHITE);

        String[] searchOptions = {"Mã phiếu mượn", "Mã Nhân Viên","Mã Độc Giả"};
        searchOptionsComboBox = new JComboBox<>(searchOptions);

        searchOptionsComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchOptionsComboBox.setBackground(Color.WHITE);
        searchOptionsComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return this;
            }
        });

        searchfield = new RoundedTextField(12, 15, 15);
        searchfield.setPlaceholder("Từ khóa tìm kiếm....");
        searchfield.setBackground(new Color(245, 245, 245));
        searchfield.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchfield.setBorderColor(new Color(200, 200, 200));
        searchfield.setFocusBorderColor(new Color(0, 120, 215));
        searchfield.addActionListener(e -> performSearch());

        searchfield.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                performSearch();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                performSearch();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                performSearch();
            }
        });

        buttonRefresh = new ButtonRefresh();
        buttonRefresh.addActionListener(e -> refreshData());

        topPanel.add(searchOptionsComboBox);
        topPanel.add(searchfield);
        topPanel.add(buttonRefresh);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        bottomPanel.setBackground(Color.WHITE);

        ActionListener radioListener = e -> {
            if (e.getSource() == allRadioButton) {
                borrowedRadioButton.setSelected(false);
                returnedRadioButton.setSelected(false);
                overdueRadioButton.setSelected(false);
            } else {
                allRadioButton.setSelected(false);
            }
            performSearch();
        };
        allRadioButton = new JRadioButton("Tất cả");
        borrowedRadioButton = new JRadioButton("Đang Mượn");
        returnedRadioButton = new JRadioButton("Đã Trả");
        overdueRadioButton = new JRadioButton("Phạt");

        allRadioButton.setBackground(Color.WHITE);
        borrowedRadioButton.setBackground(Color.WHITE);
        returnedRadioButton.setBackground(Color.WHITE);
        overdueRadioButton.setBackground(Color.WHITE);
        allRadioButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        borrowedRadioButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        returnedRadioButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        overdueRadioButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        borrowedRadioButton.addActionListener(radioListener);
        returnedRadioButton.addActionListener(radioListener);
        overdueRadioButton.addActionListener(radioListener);
        allRadioButton.addActionListener(radioListener);

        JLabel genderLabel = new JLabel("Trạng Thái:");
        genderLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JPanel emptyPanel = new JPanel();
        emptyPanel.setPreferredSize(new Dimension(-10, 0));

        buttonGroup = new ButtonGroup();
        buttonGroup.add(borrowedRadioButton);
        buttonGroup.add(returnedRadioButton);
        buttonGroup.add(overdueRadioButton);
        buttonGroup.add(allRadioButton);
        allRadioButton.setSelected(true);

        bottomPanel.add(emptyPanel);
        bottomPanel.add(genderLabel);
        bottomPanel.add(allRadioButton);
        bottomPanel.add(borrowedRadioButton);
        bottomPanel.add(returnedRadioButton);
        bottomPanel.add(overdueRadioButton);

        mainPanel.add(topPanel);
        mainPanel.add(bottomPanel);

        return mainPanel;
    }
    private void performSearch(){
        try{
            String searchText = searchfield.getText();
            int searchColumm = searchOptionsComboBox.getSelectedIndex();
            List<RowFilter<Object, Object>> filters = new ArrayList<>();
            if(!searchText.isEmpty()){
                filters.add(RowFilter.regexFilter("(?i)" + searchText, searchColumm));
            }
            if(!allRadioButton.isSelected()){
                if(borrowedRadioButton.isSelected() || returnedRadioButton.isSelected() || overdueRadioButton.isSelected()) {
                    Status statusFilter;
                    if (borrowedRadioButton.isSelected()) {
                        statusFilter = Status.Đang_Mượn;
                    } else if (returnedRadioButton.isSelected()) {
                        statusFilter = Status.Đã_Trả;
                    } else {
                        statusFilter = Status.Phạt;
                    }
                    filters.add(RowFilter.regexFilter(statusFilter.toString(), 6));
                }
            }
            if(filters.isEmpty()) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.andFilter(filters));
            }
        }catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    public void refreshData() {
        borrowinSheetTable.refreshTable();
        borrowDetailTable.refreshTable();
        sorter.setRowFilter(null);
        searchfield.setText("");
        buttonGroup.clearSelection();
        allRadioButton.setSelected(true);
    }
    public void actionPaidBook(BorrowDTO selectedBorrow){
        if (selectedBorrow == null) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn phiếu mượn cần trả", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (selectedBorrow.getStatus() != Status.Đang_Mượn){
            new AlertDialog(parentFrame, "Chỉ cho phép trạng thái đang mượn dùng nút bấm này").setVisible(true);
            return;
        }
        try {
            List<BorrowDetailDTO> borrowDetails = borrowDetailBUS.getBorrowDetailsBySheetId(Long.parseLong(selectedBorrow.getId().substring(2)));
            Date now = new Date();
            boolean hasOverDate = false;
            for (BorrowDetailDTO borrowDetail : borrowDetails) {
                SubStatus skipStatus = borrowDetail.getStatus();
                if (skipStatus == SubStatus.Đã_Trả || skipStatus == SubStatus.Hư_Sách || skipStatus == SubStatus.Mất_Sách || skipStatus == SubStatus.Quá_Hạn){
                    continue;
                }
                borrowDetail.setActualReturnDate(now);
                if (now.after(selectedBorrow.getDuedate())){
                    borrowDetail.setStatus(SubStatus.Quá_Hạn);
                    hasOverDate = true;
                }else {
                    borrowDetail.setStatus(SubStatus.Đã_Trả);
                }
                Book selectedBook = bookBUS.getBookById(borrowDetail.getBookId());
                selectedBook.setBorrowedQuantity(selectedBook.getBorrowedQuantity() - borrowDetail.getQuantity());
                bookBUS.updateBook(selectedBook);
                borrowDetailBUS.updateBorrowDetail(borrowDetail);
            }
            selectedBorrow.setActualReturnDate(now);
            if (hasOverDate) {
                selectedBorrow.setStatus(Status.Phạt);
            }else {
                selectedBorrow.setStatus(Status.Đã_Trả);
            }
            updateBorrowSheetStatus(selectedBorrow);
            BookPanel.loadData();
            refreshData();
            new AlertDialog(parentFrame, "Trả sách thành công").setVisible(true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void exportToPDF(BorrowDTO selectedBorrow) {
        if (selectedBorrow == null) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn phiếu mượn cần xuất", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Lấy thông tin cần thiết
            Employee employee = employeeBUS.getEmployeeById(selectedBorrow.getEmployeeId());
            ReaderDTO reader = readerBUS.findReaderByID(selectedBorrow.getReaderId());
            List<BorrowDetailDTO> borrowDetails = borrowDetailBUS.getBorrowDetailsBySheetId(
                    Long.parseLong(selectedBorrow.getId().substring(2))
            );

            String employeeName = employee.getFirstName() + " " + employee.getLastName();
            String readerName = reader.getLastName() + " " + reader.getFirstName();
            String readerPhone = reader.getPhone();
            String readerAddress = reader.getAddress();
            String borrowDate = formatDate(selectedBorrow.getBorrowedDate());
            String dueDate = formatDate(selectedBorrow.getDuedate());
            String actualReturnDate = selectedBorrow.getActualReturnDate() != null
                    ? formatDate(selectedBorrow.getActualReturnDate())
                    : "Chưa trả";
            String status = selectedBorrow.getStatus().toString();
            String borrowId = selectedBorrow.getId();

            // Hiển thị dialog chọn nơi lưu file
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Lưu file PDF");
            fileChooser.setSelectedFile(new File("PhieuMuon_" + borrowId + ".pdf"));

            int userSelection = fileChooser.showSaveDialog(null);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                if (!fileToSave.getAbsolutePath().endsWith(".pdf")) {
                    fileToSave = new File(fileToSave.getAbsolutePath() + ".pdf");
                }

                // Tạo tài liệu PDF
                PdfWriter writer = new PdfWriter(new FileOutputStream(fileToSave));
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf);

                // Tiêu đề
                String fontPath = "src/main/resources/fonts/NotoSans-Italic-VariableFont_wdth,wght.ttf";
                PdfFont unicodeFont = PdfFontFactory.createFont(fontPath, PdfFontFactory.EmbeddingStrategy.FORCE_EMBEDDED);
                Paragraph title = new Paragraph("PHIẾU MƯỢN SÁCH")
                        .setFont(unicodeFont)
                        .setFontSize(18)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setBold()
                        .setMarginBottom(20);
                document.add(title);

                // Bảng thông tin phiếu mượn
                Table infoTable = new Table(UnitValue.createPercentArray(new float[]{1, 2})).useAllAvailableWidth();
                addInfoRow(infoTable, "Mã phiếu mượn:", borrowId, unicodeFont);
                addInfoRow(infoTable, "Ngày mượn:", borrowDate, unicodeFont);
                addInfoRow(infoTable, "Hạn trả:", dueDate, unicodeFont);
                addInfoRow(infoTable, "Ngày trả thực tế:", actualReturnDate, unicodeFont);
                addInfoRow(infoTable, "Trạng thái:", status, unicodeFont);
                document.add(infoTable);

                // Bảng thông tin nhân viên
                document.add(new Paragraph("THÔNG TIN NHÂN VIÊN").setFont(unicodeFont).setBold().setMarginTop(15));
                Table empTable = new Table(UnitValue.createPercentArray(new float[]{1, 2})).useAllAvailableWidth();
                addInfoRow(empTable, "Mã nhân viên:", employee.getId().toString(), unicodeFont);
                addInfoRow(empTable, "Họ tên:", employeeName, unicodeFont);
                document.add(empTable);

                // Bảng thông tin độc giả
                document.add(new Paragraph("THÔNG TIN ĐỘC GIẢ").setFont(unicodeFont).setBold().setMarginTop(15));
                Table readerTable = new Table(UnitValue.createPercentArray(new float[]{1, 2})).useAllAvailableWidth();
                addInfoRow(readerTable, "Mã độc giả:", reader.getId().toString(), unicodeFont);
                addInfoRow(readerTable, "Họ tên:", readerName, unicodeFont);
                addInfoRow(readerTable, "Điện thoại:", readerPhone, unicodeFont);
                addInfoRow(readerTable, "Địa chỉ:", readerAddress, unicodeFont);
                document.add(readerTable);

                // Chi tiết sách mượn
                document.add(new Paragraph("CHI TIẾT SÁCH MƯỢN").setFont(unicodeFont).setBold().setMarginTop(15));
                if (!borrowDetails.isEmpty()) {
                    Table detailTable = new Table(UnitValue.createPercentArray(new float[]{1, 3, 1})).useAllAvailableWidth();
                    addTableHeader(detailTable, "Mã sách", unicodeFont);
                    addTableHeader(detailTable, "Tên sách", unicodeFont);
                    addTableHeader(detailTable, "Số lượng", unicodeFont);
                    for (BorrowDetailDTO detail : borrowDetails) {
                        Book book = bookBUS.getBookById(detail.getBookId());
                        addTableCell(detailTable, detail.getBookId().toString(), unicodeFont);
                        addTableCell(detailTable, book.getName(), unicodeFont);
                        addTableCell(detailTable, String.valueOf(detail.getQuantity()), unicodeFont);
                    }
                    document.add(detailTable);
                } else {
                    document.add(new Paragraph("Không có chi tiết sách mượn").setFont(unicodeFont).setMarginTop(10));
                }

                // Ngày xuất và chữ ký
                Paragraph sign = new Paragraph()
                        .add(new Text("\n\nNgày xuất: " + formatDate(new Date()) + "\n").setFont(unicodeFont))
                        .add(new Text("\t\t\t\tNgười lập phiếu\n").setFont(unicodeFont))
                        .add(new Text("\t\t\t\t(Ký và ghi rõ họ tên)").setFont(unicodeFont))
                        .setTextAlignment(TextAlignment.RIGHT);
                document.add(sign);

                document.close();

                JOptionPane.showMessageDialog(null, "Xuất PDF thành công!\nFile đã được lưu tại: " + fileToSave.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi xuất PDF: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addInfoRow(Table table, String label, String value, PdfFont font) {
        table.addCell(new Cell().add(new Paragraph(label).setFont(font).setBold()));
        table.addCell(new Cell().add(new Paragraph(value != null ? value : "").setFont(font)));
    }

    private void addTableHeader(Table table, String headerText, PdfFont font) {
        table.addCell(new Cell().add(new Paragraph(headerText).setFont(font).setBold()).setBackgroundColor(DeviceGray.GRAY));
    }

    private void addTableCell(Table table, String text, PdfFont font) {
        table.addCell(new Cell().add(new Paragraph(text).setFont(font)));
    }

    private String formatDate(Date date) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }
}