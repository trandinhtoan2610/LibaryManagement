package GUI.Component.Panel.Statistics.Components;

import BUS.ReaderBUS;
import DAL.PenaltyDAL;
import DTO.ReaderDTO;
import DTO.Statistics.PenaltyDateData;
import GUI.Component.Table.JTableCustom;
import GUI.Controller.Controller;

import javax.naming.ldap.Control;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class PenaltyDateStatisticsTable extends JTableCustom {
    private final static String[] tblHeader = {"Ngày phạt", "Mã phiếu phạt", "Độc giả vi phạm", "Sách tổn thất", "Tiền phạt"};
    private DefaultTableModel tableModel;
    private List<PenaltyDateData> dateList;
    private ReaderBUS readerBUS;

    public PenaltyDateStatisticsTable(){
        super(new DefaultTableModel(tblHeader,0));
        this.tableModel = (DefaultTableModel) getModel();
        dateList = new ArrayList<>();
        readerBUS = new ReaderBUS();
    }

    public void setList(List<PenaltyDateData> list){
        this.dateList = list;
    }

    public void renderDateTable(){
        if(dateList == null) return;

        tableModel.setRowCount(0);
        for(PenaltyDateData data : dateList){
            ReaderDTO reader = readerBUS.findReaderByID(data.getReaderID());
            Object[] rowData = {
                    data.getPenaltyDate(),
                    data.getPenaltyID(),
                    Controller.formatFullName(reader.getLastName() + " " + reader.getFirstName()),
                    data.getLostBooks(),
                    Controller.formatVND(data.getTotalAmount())
            };
            tableModel.addRow(rowData);
        }
    }



}
