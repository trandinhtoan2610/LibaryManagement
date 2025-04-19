package GUI.Component.Dialog;

import BUS.ReaderBUS;
import DTO.Enum.Gender;
import DTO.ReaderDTO;
import GUI.Component.Button.ButtonRefresh;
import GUI.Component.Table.ReaderTable;
import GUI.Component.TextField.RoundedTextField;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ChooseReaderDialog extends JDialog {

    private final ReaderTable readerTable = new ReaderTable();
    private ReaderDTO selectedReader;
    public static ReaderBUS readerBUS;
    private RoundedTextField searchfield;
    private JComboBox<String> searchOptionsComboBox;
    private JRadioButton allRadioButton;
    private JRadioButton maleRadioButton;
    private JRadioButton femaleRadioButton;
    private ButtonGroup buttonGroup;
    private ButtonRefresh buttonRefresh;

    private TableRowSorter<TableModel> sorter;

    public ChooseReaderDialog(JDialog parent) {
        super(parent, "Chọn bạn đọc", true);
        setSize(600, 400);
        readerBUS = new ReaderBUS();
        sorter = new TableRowSorter<>(readerTable.getModel());
        readerTable.setRowSorter(sorter);
        setLocationRelativeTo(parent);
        if (parent != null) {
            Point location = this.getLocation();
            this.setLocation(location.x + 50, location.y + 90);
        }
        setLayout(new BorderLayout(10, 10));
        add(getSearchNavBarLabel(), BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(readerTable);
        add(scrollPane, BorderLayout.CENTER);
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        readerTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2 || evt.getClickCount() == 3) {
                    int selectedRow = readerTable.getSelectedRow();
                    if (selectedRow != -1) {
                        selectedReader = readerTable.getSelectedReader();
                        dispose();
                    }
                }
            }
        });
        loadData();
    }

    public ReaderDTO getSelectedReader() {
        return selectedReader;
    }

    private void loadData() {
        if (readerTable != null) {
            readerTable.resetTable();
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

        String[] searchOptions = {"Mã độc giả", "Tên độc giả", "Số điện thoại", "Địa chỉ", "Uy tín"};
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

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 80, 0, 0));

        ActionListener radioListener = e -> {
            if (e.getSource() == allRadioButton) {
                maleRadioButton.setSelected(false);
                femaleRadioButton.setSelected(false);
            } else {
                allRadioButton.setSelected(false);
            }
            performSearch();
        };
        allRadioButton = new JRadioButton("Tất cả");
        maleRadioButton = new JRadioButton("Nam");
        femaleRadioButton = new JRadioButton("Nữ");

        allRadioButton.setBackground(Color.WHITE);
        maleRadioButton.setBackground(Color.WHITE);
        femaleRadioButton.setBackground(Color.WHITE);
        allRadioButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        maleRadioButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        femaleRadioButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        maleRadioButton.addActionListener(radioListener);
        femaleRadioButton.addActionListener(radioListener);
        allRadioButton.addActionListener(radioListener);

        JLabel genderLabel = new JLabel("Giới tính:");
        genderLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JPanel emptyPanel = new JPanel();
        emptyPanel.setPreferredSize(new Dimension(30, 0));

        buttonGroup = new ButtonGroup();
        buttonGroup.add(maleRadioButton);
        buttonGroup.add(femaleRadioButton);
        buttonGroup.add(allRadioButton);
        allRadioButton.setSelected(true);

        bottomPanel.add(emptyPanel);
        bottomPanel.add(genderLabel);
        bottomPanel.add(allRadioButton);
        bottomPanel.add(maleRadioButton);
        bottomPanel.add(femaleRadioButton);

        mainPanel.add(topPanel);
        mainPanel.add(bottomPanel);

        return mainPanel;
    }
    private void performSearch(){
        try{
            String searchText = searchfield.getText();
            int searchColumm = searchOptionsComboBox.getSelectedIndex();
            if(searchColumm == 3){
                searchColumm = 4;
            }
            if(searchColumm == 2){
                searchColumm = 3;
            }
            if(searchColumm == 4){
                searchColumm = 5;
            }
            List<RowFilter<Object, Object>> filters = new ArrayList<>();

            //Theo tu khoa tim kiem
            if(!searchText.isEmpty()){
                filters.add(RowFilter.regexFilter("(?i)" + searchText, searchColumm));
            }
            //Theo gioi tinh
            if(!allRadioButton.isSelected()){
                if(maleRadioButton.isSelected() || femaleRadioButton.isSelected()) {
                    Gender genderFilter = maleRadioButton.isSelected() ? Gender.Nam : Gender.Nữ;
                    filters.add(RowFilter.regexFilter(genderFilter.toString(), 2));
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
        readerTable.resetTable();
        sorter.setRowFilter(null);
        searchfield.setText("");
        buttonGroup.clearSelection();
        allRadioButton.setSelected(true);
    }
}
