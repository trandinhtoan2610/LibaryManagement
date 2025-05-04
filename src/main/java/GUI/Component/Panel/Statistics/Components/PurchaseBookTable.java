package GUI.Component.Panel.Statistics.Components;

import BUS.BookBUS;
import DTO.Book;
import DTO.Statistics.StatisticsPreciousData;
import DTO.SupplierDTO;
import GUI.Component.Table.JTableCustom;
import GUI.Controller.Controller;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class PurchaseBookTable extends JTableCustom {
    private final static String[] tblHeader = {"Sách", "Quý 1", "Quý 2", "Quý 3", "Quý 4", "Tổng cộng"};
    private DefaultTableModel tableModel;
    private List<StatisticsPreciousData<Long>> bookList;
    private BookBUS bookBUS;

    public PurchaseBookTable(){
        super(new DefaultTableModel(tblHeader,0));
        this.tableModel = (DefaultTableModel) getModel();
        bookBUS = new BookBUS();
        bookList = new ArrayList<>();
        setAutoCreateRowSorter(true);
    }

    public void setList(List<StatisticsPreciousData<Long>> list){
        bookList = list;
    }

    public void renderPurchaseFeeTable(){
        if(bookList == null) return;

        tableModel.setRowCount(0);
        Long sumTotalQ1 = 0L; Long sumTotalQ2 = 0L; Long sumTotalQ3 = 0L; Long sumTotalQ4 = 0L; Long sumTotalAll = 0L;
        for(StatisticsPreciousData<Long> data : bookList){
            Book book = bookBUS.getBookById(data.getId());
            Object[] rowData = {
                    book.getName(),
                    Controller.formatVND(data.getTotalQ1()),
                    Controller.formatVND(data.getTotalQ2()),
                    Controller.formatVND(data.getTotalQ3()),
                    Controller.formatVND(data.getTotalQ4()),
                    Controller.formatVND(data.sumTotal()),

            };

            sumTotalQ1 += data.getTotalQ1();
            sumTotalQ2 += data.getTotalQ2();
            sumTotalQ3 += data.getTotalQ3();
            sumTotalQ4 += data.getTotalQ4();
            sumTotalAll += data.sumTotal();
            tableModel.addRow(rowData);
        }
        Object[] totalData = {
                "Tổng cộng",
                Controller.formatVND(sumTotalQ1),
                Controller.formatVND(sumTotalQ2),
                Controller.formatVND(sumTotalQ3),
                Controller.formatVND(sumTotalQ4),
                Controller.formatVND(sumTotalAll)
        };
        tableModel.addRow(totalData);

    }

    public void renderPurchaseQuantiyTable(){
        if(bookList == null) return;
        tableModel.setRowCount(0);

        Long sumCountQ1 = 0L; Long sumCountQ2 = 0L; Long sumCountQ3 = 0L; Long sumCountQ4 = 0L; Long sumCountAll = 0L;
        for(StatisticsPreciousData<Long> data : bookList){
            Book book = bookBUS.getBookById(data.getId());
            Object[] rowData = {
                    book.getName(),
                    data.getCountQ1(),
                    data.getCountQ2(),
                    data.getCountQ3(),
                    data.getCountQ4(),
                    data.sumCount()
            };
            tableModel.addRow(rowData);
            sumCountQ1 += data.getCountQ1();
            sumCountQ2 += data.getCountQ2();
            sumCountQ3 += data.getCountQ3();
            sumCountQ4 += data.getCountQ4();
            sumCountAll += data.sumCount();
        }
        Object[] totalRow = {
                "Tổng cộng", sumCountQ1, sumCountQ2, sumCountQ3, sumCountQ4, sumCountAll
        };
        tableModel.addRow(totalRow);
    }




}
