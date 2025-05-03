package GUI.Component.Panel.Statistics.Components;

import BUS.BookBUS;
import DTO.Book;
import DTO.Statistics.LostBookPreciousData;
import GUI.Component.Table.JTableCustom;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LostBookTable extends JTableCustom {
    private final static String[] tblHeader = {"Sách", "Quý 1", "Quý 2", "Quý 3", "Quý 4", "Tổng cộng"};
    private DefaultTableModel tableModel;
    private List<LostBookPreciousData>  lostBookList;
    private BookBUS bookBUS;

    public LostBookTable(){
        super(new DefaultTableModel(tblHeader,0));
        this.tableModel = (DefaultTableModel) getModel();
        bookBUS = new BookBUS();
        lostBookList = new ArrayList<>();
        setAutoCreateRowSorter(true);
    }

    public void setList(List<LostBookPreciousData> list){
        this.lostBookList = list;
    }

    public void renderLostBookTable(){
        if(lostBookList == null) return;

        tableModel.setRowCount(0);
        Long totalCountQ1 = 0L; Long totalCountQ2 = 0L; Long totalCountQ3 = 0L; Long totalCountQ4 = 0L; Long totalCountAll = 0L;
        for(LostBookPreciousData data : lostBookList){
            Book book = bookBUS.getBookById(data.getBookID());

            Object[] rowData = {
                    book.getName(),
                    data.getSumQ1(),
                    data.getSumQ2(),
                    data.getSumQ3(),
                    data.getSumQ4(),
                    data.sumQuarter()
            };
            tableModel.addRow(rowData);

            totalCountQ1 += data.getSumQ1();
            totalCountQ2 += data.getSumQ2();
            totalCountQ3 += data.getSumQ3();
            totalCountQ4 += data.getSumQ4();
            totalCountAll+= data.sumQuarter();
        }
        Object[] totalData = {
                "Tổng cộng", totalCountQ1, totalCountQ2, totalCountQ3, totalCountQ4, totalCountAll
        };
        tableModel.addRow(totalData);
    }



}
