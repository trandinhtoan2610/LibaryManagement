package GUI.Component.Dialog;

import BUS.ReaderBUS;
import DTO.ReaderDTO;
import GUI.Component.Table.ReaderTable;

import javax.swing.*;
import java.awt.*;

public class ChooseReaderDialog extends JDialog {

    private final ReaderTable readerTable = new ReaderTable();
    private ReaderDTO selectedReader;
    public static ReaderBUS readerBUS;

    public ChooseReaderDialog(JDialog parent) {
        super(parent, "Chọn bạn đọc", true);
        setSize(500, 300);
        readerBUS = new ReaderBUS();
        setLocationRelativeTo(parent);
        if (parent != null) {
            Point location = this.getLocation();
            this.setLocation(location.x + 50, location.y + 90);
        }
        setLayout(new BorderLayout(10, 10));
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
}
