package GUI.Component.Panel;

import BUS.EmployeeBUS;
import BUS.PenaltyBUS;
import DTO.Employee;
import DTO.PenaltyDTO;
import GUI.Component.Table.EmployeeTable;
import GUI.Component.Table.PenaltyTable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PenaltyPanel extends JPanel {
    private PenaltyBUS penaltyBUS = new PenaltyBUS();
    private PenaltyTable penaltyTable = new PenaltyTable();

    private JFrame parentFrame;
    public PenaltyPanel(JFrame parentFrame) {
        this.parentFrame = parentFrame;

        setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(penaltyTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel emptyPanel = new JPanel();
        emptyPanel.setPreferredSize(new Dimension(0, 17));
        this.add(emptyPanel, BorderLayout.SOUTH);

        loadData();
    }
    private void loadData() {
        List<PenaltyDTO> employees = penaltyBUS.getAllPenalties();
        if (employees != null) {
            penaltyTable.setPenaltyList(employees);
        }else{
            System.out.println("Không có dữ liệu phiếu phạt");
        }
    }
}
