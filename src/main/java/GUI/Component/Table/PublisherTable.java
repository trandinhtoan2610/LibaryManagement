package GUI.Component.Table;

import DTO.PublisherDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PublisherTable extends JTableCustom {
    private static final String[] HEADER = {
            "ID", "Nhà Xuất Bản", "Số Điện Thoại", "Địa Chỉ"
    };

    private DefaultTableModel tableModel;
    private static List<PublisherDTO> publisherDTOS;
    private TableRowSorter<DefaultTableModel> rowSorter;


    public PublisherTable() {
        super(new DefaultTableModel(HEADER, 0));
        this.tableModel = (DefaultTableModel) getModel();
        setHeaderStyle(new Font("Segoe UI", Font.BOLD, 14), new Color(70, 130, 180));
        setCustomGrid(new Color(220, 220, 220), 30);
        getColumnModel().getColumn(0).setPreferredWidth(100);
        getColumnModel().getColumn(1).setPreferredWidth(100);
        getColumnModel().getColumn(2).setPreferredWidth(100);
        getColumnModel().getColumn(3).setPreferredWidth(100);
        setAutoCreateRowSorter(true);
        rowSorter = new TableRowSorter<>(tableModel);
        this.setRowSorter(rowSorter);
    }

    public void setPublisherList(List<PublisherDTO> publisherDTOS) {
        if (publisherDTOS != null) {
            this.publisherDTOS = publisherDTOS;
        }
        refreshTable();
    }

    public void addPublisher(PublisherDTO publisherDTO) {
        if (publisherDTO != null) {
            publisherDTOS.add(publisherDTO);
            refreshTable();
        }
    }

    public boolean updatePublisher(PublisherDTO publisherDTO) {
        PublisherDTO selectedPublisher = getSelectedPublisher();
        if (selectedPublisher != null) {
            int index = publisherDTOS.indexOf(selectedPublisher);
            if (index != -1) {
                publisherDTOS.set(index, publisherDTO);
                refreshTable();
                return true;
            }
        }
        return false;
    }

    public boolean removePublisher(PublisherDTO publisherDTO) {
        PublisherDTO selectedPublisher = getSelectedPublisher();
        if (selectedPublisher != null) {
            int index = publisherDTOS.indexOf(selectedPublisher);
            if (index != -1) {
                publisherDTOS.remove(index);
                refreshTable();
                return true;
            }
        }
        return false;
    }

    public PublisherDTO getSelectedPublisher() {
        int selectedRow = getSelectedRow();
        if (selectedRow >= 0) {
            int modelRow = convertRowIndexToModel(selectedRow);
            return publisherDTOS.get(modelRow);
        }
        return null;
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        for (PublisherDTO publisherDTO : publisherDTOS) {
            Object[] rowData = {
                    publisherDTO.getId(),
                    publisherDTO.getName(),
                    publisherDTO.getPhone(),
                    publisherDTO.getAddress()
            };
            tableModel.addRow(rowData);
        }
    }
    public void filterTable(String name, String phone, String address) {
        List<RowFilter<Object, Object>> filters = new ArrayList<>();

        // Nếu có giá trị name, thêm vào bộ lọc cột 1 (Tên Độc Giả)
        if (!name.isEmpty()) {
            filters.add(RowFilter.regexFilter("(?i)" + name, 1));
        }
        if(!phone.isEmpty()){
            filters.add(RowFilter.regexFilter("(?i)" + phone , 2));
        }

        if(!address.isEmpty()){
            filters.add(RowFilter.regexFilter("(?i)" + address, 3));
        }

        // Áp dụng bộ lọc
        if (filters.isEmpty()) {
            rowSorter.setRowFilter(null); // Nếu không có gì để lọc, reset bảng
        } else {
            rowSorter.setRowFilter(RowFilter.andFilter(filters)); // Chỉ hiện nếu THỎA CẢ HAI điều kiện
        }
    }
}
