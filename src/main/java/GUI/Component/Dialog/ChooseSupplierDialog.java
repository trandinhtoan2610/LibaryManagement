package GUI.Component.Dialog;

import BUS.SupplierBUS;
import DTO.SupplierDTO;
import GUI.Component.Button.ButtonRefresh;
import GUI.Component.Table.SupplierTable;
import GUI.Component.TextField.RoundedTextField;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.List;

public class ChooseSupplierDialog extends JDialog {
    private final SupplierTable supplierTable = new SupplierTable();
    private final SupplierBUS supplierBUS = new SupplierBUS();
    private SupplierDTO selectedSupplier;
    private RoundedTextField searchfield;
    private JComboBox<String> searchOptionsComboBox;
    private ButtonRefresh buttonRefresh;

    private TableRowSorter<TableModel> sorter;

    public ChooseSupplierDialog(JDialog parent) {
        super(parent, "Chọn nhà cung cấp", true);
        setSize(600, 400);
        sorter = new TableRowSorter<>(supplierTable.getModel());
        supplierTable.setRowSorter(sorter);
        setLocationRelativeTo(parent);
        if (parent != null) {
            Point location = this.getLocation();
            this.setLocation(location.x + 50, location.y + 90);
        }
        setLayout(new BorderLayout(10, 10));
        add(getSearchNavBarLabel(), BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(supplierTable);
        add(scrollPane, BorderLayout.CENTER);
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        supplierTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2 || evt.getClickCount() == 3) {
                    int selectedRow = supplierTable.getSelectedRow();
                    if (selectedRow != -1) {
                        selectedSupplier = supplierTable.getSelectedSupplier();
                        dispose();
                    }
                }
            }
        });
        loadData();
    }

    public SupplierDTO getSelectedSupplier() {
        return selectedSupplier;
    }

    private void loadData() {
        List<SupplierDTO> suppliers = SupplierBUS.supplierList;
        if (suppliers != null) {
            supplierTable.setSuppliers(suppliers);
        } else {
            System.out.println("Không có dữ liệu");
        }
    }

    public JPanel getSearchNavBarLabel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 10));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 80, 0, 0));

        String[] searchOptions = {"Mã nhà cung cấp", "Tên nhà cung cấp", "Số điện thoại", "Địa chỉ"};
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

        mainPanel.add(topPanel);

        return mainPanel;
    }

    private void performSearch() {
        try {
            String searchText = searchfield.getText();
            int searchColumn = searchOptionsComboBox.getSelectedIndex(); // 0 = ID, 1 = Name, 2 = Phone, 3 = Address
            if (!searchText.isEmpty()) {
                RowFilter<Object, Object> filter = RowFilter.regexFilter("(?i)" + searchText, searchColumn);
                sorter.setRowFilter(filter);
            } else {
                sorter.setRowFilter(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refreshData() {
        supplierTable.refreshTable();
        sorter.setRowFilter(null);
        searchfield.setText("");
    }
}
