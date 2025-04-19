package GUI.Component.Dialog;

import BUS.BookBUS;
import DTO.BookViewModel;
import DTO.Enum.Gender;
import GUI.Component.Button.ButtonRefresh;
import GUI.Component.Table.BookTable;
import GUI.Component.TextField.RoundedTextField;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ChooseBookDialog extends JDialog {
    private static final String HEADER[] = {
            "Tên sách", "Thể loại", "Tác giả",
            "Nhà xuất bản", "Số lượng", "Năm XB"
    };
    private final BookTable bookTable = new BookTable(HEADER);
    private final BookBUS bookBUS = new BookBUS();
    private BookViewModel selectedBook;
    private RoundedTextField searchfield;
    private JComboBox<String> searchOptionsComboBox;
    private ButtonRefresh buttonRefresh;
    private JDialog parentDialog;
    private TableRowSorter<TableModel> sorter;
    public ChooseBookDialog(JDialog parentDialog) {
        super(parentDialog, "Chọn sách", true);
        this.parentDialog = parentDialog;
        sorter = new TableRowSorter<>(bookTable.getModel());
        bookTable.setRowSorter(sorter);
        setSize(600, 400);
        setLocationRelativeTo(parentDialog);
        if (parentDialog != null) {
            Point location = this.getLocation();
            this.setLocation(location.x +50, location.y + 90);
        }
        setLayout(new BorderLayout(10, 10));
        add(getSearchNavBarLabel(), BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(bookTable);
        add(scrollPane, BorderLayout.CENTER);
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bookTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2 || evt.getClickCount() == 3) {
                    int selectedRow = bookTable.getSelectedRow();
                    if (selectedRow != -1) {
                        selectedBook = bookTable.getSelectedBook();
                        dispose();
                    }
                }
            }
        });
        loadData();
    }

    public BookViewModel getSelectedBook() {
        return selectedBook;
    }

    private void loadData() {
        List<BookViewModel> books = bookBUS.getAllBooksForDisplay();
        if (books != null) {
            bookTable.setBookCustom(books);
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

        String[] searchOptions = {"Tên Sách", "Thể Loại", "Tác Giả", "Nhà Xuất Bản", "Số Lượng", "Năm XB"};
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
    public void refreshData() {
        bookTable.refreshTableCustom();
        sorter.setRowFilter(null);
        searchfield.setText("");
    }
}