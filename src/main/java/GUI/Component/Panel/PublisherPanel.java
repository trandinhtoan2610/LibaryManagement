package GUI.Component.Panel;

import BUS.PublisherBUS;
import DTO.PublisherDTO;
import GUI.Component.Button.*;
import GUI.Component.Dialog.AddPublisherDialog;
import GUI.Component.Dialog.AlertDialog;
import GUI.Component.Dialog.DeletePublisherDialog;
import GUI.Component.Dialog.UpdatePublisherDialog;
import GUI.Component.Table.PublisherTable;
import GUI.Component.TextField.RoundedTextField;
import GUI.ExcelxPDF.ExcelMaster;
import com.formdev.flatlaf.FlatLightLaf;
import com.itextpdf.io.exceptions.IOException;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class PublisherPanel extends JPanel {
    private final PublisherBUS publisherBUS = new PublisherBUS();
    private final PublisherTable publisherTable = new PublisherTable();
    private ButtonAdd buttonAdd;
    private ButtonUpdate buttonUpdate;
    private ButtonDelete buttonDelete;
    private ButtonExportExcel buttonExportExcel;
    private ButtonImportExcel buttonImportExcel;
    private ButtonExportPDF buttonExportPDF;
    private JPanel searchNavBarLabel;
    private RoundedTextField searchfield;
    private JComboBox<String> searchOptionsComboBox;

    private TableRowSorter<TableModel> sorter;


    private JFrame parentFrame;

    public PublisherPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;

        sorter = new TableRowSorter<>(publisherTable.getModel());
        publisherTable.setRowSorter(sorter);

        setLayout(new BorderLayout());
        setLayout(new BorderLayout());
        this.add(buttonPanel(parentFrame), BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(publisherTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel emptyPanel = new JPanel();
        emptyPanel.setPreferredSize(new Dimension(0, 17));
        this.add(emptyPanel, BorderLayout.SOUTH);
        loadData();
    }

    public JPanel buttonPanel(JFrame parentFrame) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        buttonPanel.setBackground(Color.WHITE);
        buttonAdd = new ButtonAdd();
        buttonAdd.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AddPublisherDialog addPublisherDialog = new AddPublisherDialog(parentFrame, PublisherPanel.this);
                addPublisherDialog.setVisible(true);
            }
        });
        buttonUpdate = new ButtonUpdate();
        buttonUpdate.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                PublisherDTO publisher = publisherTable.getSelectedPublisher();
                if (publisher == null) {
                    AlertDialog updateAlert = new AlertDialog(parentFrame, "Vui lòng chọn nhà xuất bản cần sửa");
                    updateAlert.setVisible(true);
                } else {
                    UpdatePublisherDialog updatePublisherDialog = new UpdatePublisherDialog(parentFrame, PublisherPanel.this, publisher);
                    updatePublisherDialog.setVisible(true);
                }
            }
        });
        buttonDelete = new ButtonDelete();
        buttonDelete.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                PublisherDTO publisher = publisherTable.getSelectedPublisher();
                if (publisher == null) {
                    AlertDialog deleteAlert = new AlertDialog(parentFrame, "Vui lòng chọn nhà xuất bản cần xóa");
                    deleteAlert.setVisible(true);
                } else {
                    DeletePublisherDialog deletePublisherDialog = new DeletePublisherDialog(parentFrame, PublisherPanel.this, publisher);
                    deletePublisherDialog.setVisible(true);
                }
            }
        });
        buttonExportExcel = new ButtonExportExcel();
        buttonExportExcel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                exportExcelData();
            }
        });
        buttonImportExcel = new ButtonImportExcel();
        buttonImportExcel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                importExcelData();
            }
        });
        buttonExportPDF = new ButtonExportPDF();
        buttonExportPDF.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                exportToPDF();
            }
        });
        searchNavBarLabel = getSearchNavBarLabel();
        buttonPanel.add(buttonAdd);
        buttonPanel.add(buttonUpdate);
        buttonPanel.add(buttonDelete);
        buttonPanel.add(buttonExportExcel);
        buttonPanel.add(buttonImportExcel);
        buttonPanel.add(buttonExportPDF);
        buttonPanel.add(Box.createRigidArea(new Dimension(100, 0)));
        buttonPanel.add(searchNavBarLabel);
        return buttonPanel;
    }

    private void loadData() {
        List<PublisherDTO> publishers = PublisherBUS.publisherList;
        if (publishers != null) {
            publisherTable.setPublisherList(publishers);
        } else {
            JOptionPane.showMessageDialog(this, "Không có nhà xuất bản nào trong cơ sở dữ liệu.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void refreshTable() {
        publisherTable.refreshTable();
    }

    public void addPublisher(PublisherDTO publisher) {
        if (publisher != null) {
            publisherTable.addPublisher(publisher);
        }
    }

    public void updatePublisher(PublisherDTO publisher) {
        if (publisher != null) {
            publisherTable.updatePublisher(publisher);
        }
    }

    public void deletePublisher(PublisherDTO publisher) {
        if (publisher != null) {
            publisherTable.removePublisher(publisher);
        }
    }

    public PublisherDTO getSelectedPublisher() {
        return publisherTable.getSelectedPublisher();
    }
    public JPanel getSearchNavBarLabel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 10));
        topPanel.setBackground(Color.WHITE);

        String[] searchOptions = {"Mã nhà xuất bản", "Tên nhà xuất bản", "Số điện thoại", "Địa chỉ"};
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
                performSearch(); // Gọi tìm kiếm khi có nội dung mới
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                performSearch(); // Gọi tìm kiếm khi xóa nội dung
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                performSearch(); // Gọi tìm kiếm khi thay đổi thuộc tính
            }
        });



        topPanel.add(searchOptionsComboBox);
        topPanel.add(searchfield);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        bottomPanel.setBackground(Color.WHITE);

        mainPanel.add(topPanel);
        mainPanel.add(bottomPanel);

        return mainPanel;
    }
    private void performSearch(){
        try{
            String searchText = searchfield.getText();
            int searchColumm = searchOptionsComboBox.getSelectedIndex();
            List<RowFilter<Object, Object>> filters = new ArrayList<>();

            //Theo tu khoa tim kiem
            if(!searchText.isEmpty()){
                filters.add(RowFilter.regexFilter("(?i)" + searchText, searchColumm));
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
    private void importExcelData() {
        try {
            // Thiết lập giao diện
            FlatLightLaf.setup();
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn file Excel nhập dữ liệu");

            // Thiết lập bộ lọc file
            FileNameExtensionFilter filter = new FileNameExtensionFilter(
                    "Excel Files (*.xlsx)", "xlsx");
            fileChooser.setFileFilter(filter);

            // Hiển thị dialog chọn file
            int fileChooserResult = fileChooser.showOpenDialog(parentFrame);

            // Nếu người dùng không chọn file hoặc hủy
            if (fileChooserResult != JFileChooser.APPROVE_OPTION) {
                return;
            }

            File selectedFile = fileChooser.getSelectedFile();
            ExcelMaster excelMaster = new ExcelMaster();

            // Đọc file Excel
            excelMaster.setExcelFile(selectedFile.getAbsolutePath(), "Nhà xuất bản");
            List<PublisherDTO> publishersFromExcel = excelMaster.readPublisherFromExcel();

            // Kiểm tra dữ liệu
            if (publishersFromExcel.isEmpty()) {
                new AlertDialog(parentFrame, "File Excel không có dữ liệu nhà xuất bản").setVisible(true);
                return;
            }

            // Tạo custom dialog xác nhận
            JPanel panel = new JPanel(new BorderLayout());
            panel.add(new JLabel("<html><b>Bạn có chắc chắn muốn nhập " + publishersFromExcel.size() +
                            " nhà xuất bản từ file Excel?</b><br>File: " + selectedFile.getName() + "</html>"),
                    BorderLayout.CENTER);

            // Tùy chỉnh các nút
            Object[] options = {"Đồng ý", "Hủy bỏ"};
            int confirm = JOptionPane.showOptionDialog(
                    parentFrame,
                    panel,
                    "Xác nhận nhập Excel",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[1]); // Mặc định chọn "Hủy bỏ"

            // Xử lý kết quả
            if (confirm == JOptionPane.YES_OPTION) {
                // Thực hiện import
                int successCount = publisherBUS.addPublisherFromExcel(publishersFromExcel);

                // Cập nhật giao diện
                publisherTable.setPublisherList(PublisherBUS.publisherList);

                // Hiển thị kết quả
                new AlertDialog(parentFrame,
                        "Đã nhập thành công " + successCount + "/" + publishersFromExcel.size() + " nhà xuất bản")
                        .setVisible(true);
            } else {
                // Người dùng chọn hủy
                new AlertDialog(parentFrame, "Đã hủy thao tác nhập Excel").setVisible(true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            new AlertDialog(parentFrame,
                    "Lỗi khi nhập file Excel: " + ex.getMessage())
                    .setVisible(true);
        }
    }
    private void exportExcelData() {
        FlatLightLaf.setup();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn vị trí lưu file Excel");
        fileChooser.setSelectedFile(new File("DanhSachNhaXuatBan.xlsx"));

        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Excel Files (*.xlsx)", "xlsx");
        fileChooser.setFileFilter(filter);

        int userSelection = fileChooser.showSaveDialog(parentFrame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();

            // Đảm bảo có đuôi .xlsx
            if (!filePath.toLowerCase().endsWith(".xlsx")) {
                filePath += ".xlsx";
            }

            if (publisherBUS.exportToExcel(filePath)) {
                JOptionPane.showMessageDialog(parentFrame,
                        "Xuất Excel thành công!\nFile: " + filePath,
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(parentFrame,
                        "Xuất Excel thất bại",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private void exportToPDF() {
        FlatLightLaf.setup();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn vị trí lưu file PDF");
        fileChooser.setSelectedFile(new File("DanhSachNhaXuatBan.pdf"));
        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files (*.pdf)", "pdf"));

        if (fileChooser.showSaveDialog(parentFrame) == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".pdf")) {
                filePath += ".pdf";
            }

            try {
                // 1. Tạo PDF Writer
                PdfWriter writer = new PdfWriter(filePath);

                // 2. Tạo PDF Document
                PdfDocument pdf = new PdfDocument(writer);

                // 3. Tạo Document với font hỗ trợ Unicode
                Document document = new Document(pdf);

                // 4. Sử dụng font có sẵn hỗ trợ tiếng Việt
                PdfFont font = PdfFontFactory.createFont(
                        "C:/Windows/Fonts/Arial.ttf",  // Đường dẫn đến font Arial trên Windows
                        PdfEncodings.IDENTITY_H,
                        PdfFontFactory.EmbeddingStrategy.PREFER_EMBEDDED
                );
                document.setFont(font);

                // 5. Thêm tiêu đề
                document.add(new Paragraph("DANH SÁCH NHÀ XUẤT BẢN")
                        .setTextAlignment(TextAlignment.CENTER)
                        .setFontSize(16)
                        );

                // 6. Tạo bảng (9 cột)
                Table table = new Table(new float[]{1, 3, 2, 2})
                        .useAllAvailableWidth();

                // 7. Thêm header
                String[] headers = {"Mã Nhà Xuất Bản", "Tên Nhà Xuất Bản", "Số Điện Thoại", "Địa Chỉ"};
                for (String header : headers) {
                    table.addCell(new Paragraph(header));
                }

                // 8. Thêm dữ liệu
                for (PublisherDTO pub : PublisherBUS.publisherList) {
                    addCell(table, pub.getId().toString());
                    addCell(table, pub.getName() != null ? pub.getName() : "");
                    addCell(table, pub.getPhone() != null ? pub.getPhone() : "");
                    addCell(table, pub.getAddress() != null ? pub.getAddress() : "");
                }

                document.add(table);
                document.close();

                JOptionPane.showMessageDialog(parentFrame,
                        "Xuất PDF thành công!\nFile: " + filePath,
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException e) {
                JOptionPane.showMessageDialog(parentFrame,
                        "Lỗi khi xuất PDF: " + e.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (java.io.IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // Helper method để thêm cell
    private void addCell(Table table, String content) {
        table.addCell(new Paragraph(content).setFontSize(10));
    }
}
