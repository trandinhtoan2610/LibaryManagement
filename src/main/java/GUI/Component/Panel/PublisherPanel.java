package GUI.Component.Panel;

import BUS.PublisherBUS;
import DTO.Enum.Gender;
import DTO.PublisherDTO;
import GUI.Component.Button.*;
import GUI.Component.Dialog.AddPublisherDialog;
import GUI.Component.Dialog.AlertDialog;
import GUI.Component.Dialog.DeletePublisherDialog;
import GUI.Component.Dialog.UpdatePublisherDialog;
import GUI.Component.Panel.Components.SearchNavBarLabel;
import GUI.Component.Table.PublisherTable;
import GUI.Component.TextField.RoundedTextField;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
        buttonImportExcel = new ButtonImportExcel();
        searchNavBarLabel = getSearchNavBarLabel();
        buttonPanel.add(buttonAdd);
        buttonPanel.add(buttonUpdate);
        buttonPanel.add(buttonDelete);
        buttonPanel.add(buttonExportExcel);
        buttonPanel.add(buttonImportExcel);
        buttonPanel.add(Box.createRigidArea(new Dimension(60, 0)));
        buttonPanel.add(searchNavBarLabel);
        return buttonPanel;
    }

    private void loadData() {
        List<PublisherDTO> publishers = publisherBUS.getAllPublishers();
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
}
