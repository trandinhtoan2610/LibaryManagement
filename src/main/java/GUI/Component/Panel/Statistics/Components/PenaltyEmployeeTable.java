package GUI.Component.Panel.Statistics.Components;

import BUS.EmployeeBUS;
import DTO.Employee;
import DTO.Statistics.StatisticsPreciousData;
import GUI.Component.Table.JTableCustom;
import GUI.Controller.Controller;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class PenaltyEmployeeTable extends JTableCustom {
    private final static String[] tblHeader = {"Nhân viên", "Quý 1", "Quý 2", "Quý 3", "Quý 4", "Tổng cộng"};
    private DefaultTableModel tableModel;
    private EmployeeBUS employeeBUS;
    private List<StatisticsPreciousData<Long>> employeeList;

    public PenaltyEmployeeTable(){
        super(new DefaultTableModel(tblHeader, 0));
        this.tableModel = (DefaultTableModel) getModel();
        employeeList = new ArrayList<>();
        employeeBUS = new EmployeeBUS();
    }

    public void setList(List<StatisticsPreciousData<Long>> list){
        this.employeeList = list;
    }

    public void renderPenaltySheetTable(){
        if(employeeList == null) return;

        tableModel.setRowCount(0);
        Long totalQ1 = 0L; Long totalQ2 = 0L; Long totalQ3 = 0L; Long totalQ4 = 0L; Long totalAll = 0L;
        for(StatisticsPreciousData<Long> data : employeeList){
            Employee employee = employeeBUS.getEmployeeById(data.getId());
            Object[] rowData = {
                    employee.getFirstName() + ' ' + employee.getLastName(),
                    data.getCountQ1(),
                    data.getCountQ2(),
                    data.getCountQ3(),
                    data.getCountQ4(),
                    data.sumCount()
            };
            totalQ1 += data.getCountQ1();
            totalQ2 += data.getCountQ2();
            totalQ3 += data.getCountQ3();
            totalQ4 += data.getCountQ4();
            totalAll += data.sumCount();
            tableModel.addRow(rowData);
        }
        Object[] totalData = {
                "Tổng cộng", totalQ1, totalQ2, totalQ3, totalQ4, totalAll
        };
        tableModel.addRow(totalData);
    }

    public void renderPenaltyFeeTable(){
        if(employeeList == null) return;

        tableModel.setRowCount(0);
        Long sumTotalQ1 = 0L; Long sumTotalQ2 = 0L; Long sumTotalQ3 = 0L; Long sumTotalQ4 = 0L; Long sumTotalAll = 0L;
        for(StatisticsPreciousData<Long> data : employeeList){
            Employee employee = employeeBUS.getEmployeeById(data.getId());
            Object[] rowData = {
                    employee.getFirstName() + ' ' + employee.getLastName(),
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
}
