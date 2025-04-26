package GUI.Component.Table;

import BUS.BookBUS;
import BUS.PenaltyDetailsBUS;
import DTO.Book;
import DTO.PenaltyDetailsDTO;
import GUI.Controller.Controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class PenaltyDetailsTable extends JTableCustom {
    private static final String[] tblHeader = {"Tên sách", "Số lượng", "Vi phạm", "Tiền phạt"};
    private DefaultTableModel tblModel;

    public PenaltyDetailsTable(){
        super(new DefaultTableModel(tblHeader, 0));
        this.tblModel = (DefaultTableModel)getModel();
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public void resetTable(List<PenaltyDetailsDTO> penaltyDetailsList){
        tblModel.setRowCount(0);
        if(penaltyDetailsList == null) return;

        BookBUS bookBUS = new BookBUS();
        for (PenaltyDetailsDTO p : penaltyDetailsList){
            Book book = bookBUS.getBookById(p.getBookID());
            Object[] rowData = {
                    book.getName(),
                    p.getBookQuantity(),
                    p.getViolation(),
                    Controller.formatVND(p.getPunishFee())
            };

            tblModel.addRow(rowData);
        }
    }
}