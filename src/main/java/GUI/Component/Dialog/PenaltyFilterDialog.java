/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/AWTForms/Dialog.java to edit this template
 */
package GUI.Component.Dialog;

import BUS.EmployeeBUS;
import BUS.PenaltyBUS;
import BUS.ReaderBUS;
import DTO.Employee;
import DTO.Enum.PayStatus;
import DTO.PenaltyDTO;
import DTO.ReaderDTO;
import GUI.Component.Panel.PenaltyPanel;
import GUI.Controller.SearchCondition;
import com.toedter.calendar.JDateChooser;

import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author keika
 */
public class PenaltyFilterDialog extends JDialog {
    private EmployeeBUS employeeBUS;
    private ReaderBUS readerBUS;
    private PenaltyBUS penaltyBUS;
    private static final Color offColor = new Color(245,245,245);

    public PenaltyFilterDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        readerBUS = new ReaderBUS();
        employeeBUS = new EmployeeBUS();
        penaltyBUS = new PenaltyBUS();
        initComponents();
        txtPayDateStart.setDateFormatString("dd/MM/yyyy");
        txtPayDateEnd.setDateFormatString("dd/MM/yyyy");
        txtPenaltyStart.setDateFormatString("dd/MM/yyyy");
        txtPenaltyEnd.setDateFormatString("dd/MM/yyyy");
        setMinimumSize(new Dimension(650,600));
        setMaximumSize(new Dimension(650,600));
        setSize(650, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        initialize();

        txtReaderID.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { showReaderInfo(); }

            @Override
            public void removeUpdate(DocumentEvent e) {showReaderInfo();}

            @Override
            public void changedUpdate(DocumentEvent e) {showReaderInfo();}
        });

        txtEmployeeID.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {showEmployeeInfo();}

            @Override
            public void removeUpdate(DocumentEvent e) {showEmployeeInfo();}

            @Override
            public void changedUpdate(DocumentEvent e) {showEmployeeInfo();}
        });
    }

    public void initialize(){

        employeeInputPanel.setBackground(offColor);
        readerInputPanel.setBackground(offColor);
        payDateInputPanel.setBackground(offColor);
        penaltyDateInputPanel.setBackground(offColor);
        amountInputPanel.setBackground(offColor);
        statusInputPanel.setBackground(offColor);

        checkboxStatus.setVisible(false);
        checkboxReader.setVisible(false);
        checkboxEmployee.setVisible(false);
        checkboxPayDate.setVisible(false);
        checkboxPenaltyDate.setVisible(false);
        checkboxAmount.setVisible(false);

        turnOffTxt(searchEmployeeIdPanel, searchEmployeeNamePanel, txtEmployeeID,  btnChooseEmployee);
        turnOffTxt(searchReaderIdPanel,searchReaderNamePanel,txtReaderID, btnChooseReader);
        turnOffDate(searchStartPayDatePanel, searchEndPayDatePanel, txtPayDateStart, txtPayDateEnd);
        turnOffDate(searchStartPenaltyDatePanel, searchEndPenaltyDatePanel, txtPenaltyStart, txtPenaltyEnd);
        turnOffSpn(searchMinAmountPanel, searchMaxAmountPanel, txtMinAmount, txtMaxAmount);
        turnOffRad(subStatusPanel1, subStatusPanel2, subStatusPanel3);
    }

    //Hiển thị nhân viên lên textfield :
    private void showEmployeeInfo() {
        String employeeId = txtEmployeeID.getText();
        if (employeeId.isEmpty()) {
            txtEmployeeName.setText("");
        } else {
            try {
                Long employee = Long.parseLong(employeeId);
                Employee e = employeeBUS.getEmployeeById(employee);
                txtEmployeeName.setText(e.getFirstName() + " " + e.getLastName());
            } catch (Exception e) {
                txtEmployeeName.setText("");
            }
        }
    }

    //Hiển thị độc giả lên textfield :
    private void showReaderInfo(){
        String readerID = txtReaderID.getText();
        if(readerID.isEmpty()){
            txtReaderName.setText("");
        }
        else{
            try{
                ReaderDTO reader = readerBUS.findReaderByID(readerID);
                txtReaderName.setText(reader.getLastName() + " " + reader.getFirstName());
            } catch (Exception e){
                txtReaderName.setText("");
            }
        }
    }


    //Hàm kiểm tra điều kiện lọc :
    private boolean isMatchCondition(PenaltyDTO penalty, SearchCondition searchCondition){
        Object value = searchCondition.getSearchValue();

        switch (searchCondition.getSearchField()) {
            case EMPLOYEE -> {
                Employee emp = penaltyBUS.getEmployeeHandlePenalty(penalty);
                if(emp == null)
                    return false;
                System.out.println(emp.getId().toString() + " = " + value.toString());
                return emp.getId().toString().equals(value.toString());
            }

            case READER -> {
                try {
                    ReaderDTO reader = penaltyBUS.getPenaltyReader(penalty);
                    System.out.println(reader.getId() + " = " + value.toString());
                    return reader != null && reader.getId().equals(value.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

            case STATUS -> {
                String input = value.toString();
                if (input.equals("Tất cả")) return true;
                String status = penalty.getPayStatus().equals(PayStatus.Chưa_Thanh_Toán) ? "Chưa thanh toán" : "Đã thanh toán";
                System.out.println(penalty.getPayStatus() + " = " + input);
                System.out.println(status.equals(input));
                return status.equals(input);
            }

            case AMOUNT -> {
                if (value instanceof Long[]) {
                    Long[] amountRange = (Long[]) value;
                    Long min = amountRange[0];
                    Long max = amountRange[1];
                    long actual = penalty.getTotalAmount();
                    System.out.println(searchCondition.debugString());
                    System.out.println("min : " + min + " max : " + max );
                    return actual >= min && actual <= max;
                }
                System.out.println(searchCondition.debugString());
                return false;
            }

            case PAY_DATE -> {
                if (value instanceof Date[]) {
                    Date[] dates = (Date[]) value;
                    Date start = dates[0];
                    Date end = dates[1];
                    Date actual = penalty.getPayDate();
                    System.out.println(searchCondition.debugString());
                    System.out.println("start : " + start + "  end :  " + end);

                    if (actual == null) return false;
                    if(start == null && end != null) return !actual.after(end);
                    else if(start != null && end == null) return  !actual.before(start);
                }
                return false;
            }

            case PENALTY_DATE -> {
                if (value instanceof Date[]) {
                    Date[] dates = (Date[]) value;
                    Date start = dates[0];
                    Date end = dates[1];
                    Date actual = penalty.getPenaltyDate();
                    System.out.println("start: " +  start);
                    System.out.println("END : " + end );
                    if (actual == null) return false;
                    if(start == null && end != null) return !actual.after(end);
                    else if(start != null && end == null) return  !actual.before(start);
                }
                return false;
            }
        }
        return false;
    }


    private List<PenaltyDTO> filterPenaltyList(List<SearchCondition> searchConditionList) {
        return PenaltyBUS.penaltyList.stream().filter(penalty -> {
            boolean isFirstAnd = true;
            boolean andCorrect = false;
            boolean orCorrect = false;

            for (SearchCondition sc : searchConditionList) {
                boolean isMatch = isMatchCondition(penalty, sc);
                System.out.println(isMatch);
                if(sc.isAnd()){
                    if(isFirstAnd){
                        andCorrect = isMatch;
                        isFirstAnd = false;
                    }else {
                        andCorrect = andCorrect && isMatch;
                    }
                }
                else {
                    orCorrect = orCorrect || isMatch;
                }
            }
            System.out.println("PenaltyID : " +penalty.getId());
            System.out.println("And correct : " + andCorrect);
            System.out.println("Or correct :  " + orCorrect);
            return andCorrect || orCorrect;

        }).collect(Collectors.toList());
    }

    private void turnOffTxt(JPanel panel, JPanel panel2, JTextField txtField, JButton button){
        panel.setBackground(offColor);
        panel2.setBackground(offColor);
        txtField.setText("");
        txtField.setEnabled(false);
        txtField.setEditable(false);
        button.setEnabled(false);
    }

    private void turnOnTxt(JPanel panel, JPanel panel2, JTextField txtField, JButton button){
        panel.setBackground(Color.white);
        panel2.setBackground(Color.white);
        txtField.setEnabled(true);
        txtField.setEditable(true);
        button.setEnabled(true);
    }

    private void turnOnDate( JPanel pnl1, JPanel pnl2 ,JDateChooser dateChooser, JDateChooser dateChooser2){
        pnl1.setBackground(Color.white);
        pnl2.setBackground(Color.white);
        dateChooser.setEnabled(true);
        dateChooser2.setEnabled(true);
    }

    private void turnOffDate( JPanel pnl1, JPanel pnl2 ,JDateChooser dateChooser, JDateChooser dateChooser2){
        pnl1.setBackground(offColor);
        pnl2.setBackground(offColor);
        dateChooser.setDate(null);
        dateChooser2.setDate(null);
        dateChooser.setEnabled(false);
        dateChooser2.setEnabled(false);
    }

    private void turnOffSpn(JPanel pnl1, JPanel pnl2, JSpinner spn1, JSpinner spn2){
        pnl1.setBackground(offColor);
        pnl2.setBackground(offColor);
        spn1.setValue(0);
        spn2.setValue(0);
        spn1.setEnabled(false);
        spn2.setEnabled(false);
    }

    private void turnOnSpn(JPanel pnl1, JPanel pnl2, JSpinner spn1, JSpinner spn2){
        pnl1.setBackground(Color.white);
        pnl2.setBackground(Color.white);
        spn1.setEnabled(true);
        spn2.setEnabled(true);
    }

    private void turnOnRad(JPanel pnl1, JPanel pnl2, JPanel pnl3){
        pnl1.setBackground(Color.white);
        pnl2.setBackground(Color.white);
        pnl3.setBackground(Color.white);
        radStatusAll.setEnabled(true);
        radHavePaid.setEnabled(true);
        radNotPay.setEnabled(true);
        radStatusAll.setSelected(true);
    }

    private void turnOffRad(JPanel pnl1, JPanel pnl2, JPanel pnl3){
        pnl1.setBackground(offColor);
        pnl2.setBackground(offColor);
        pnl3.setBackground(offColor);
        radStatusAll.setSelected(true);
        radStatusAll.setEnabled(false);
        radHavePaid.setEnabled(false);
        radNotPay.setEnabled(false);

    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGroupStatus = new javax.swing.ButtonGroup();
        buttonsPanel = new javax.swing.JPanel();
        leftButtonsPanel = new javax.swing.JPanel();
        btnCombineAll = new javax.swing.JButton();
        rightButtonsPanel = new javax.swing.JPanel();
        btnDeleteCombine = new javax.swing.JButton();
        middleButtonsPanel = new javax.swing.JPanel();
        btnSubmit = new javax.swing.JButton();
        btnReject = new javax.swing.JButton();
        headerPanel = new javax.swing.JPanel();
        lblHeader = new javax.swing.JLabel();
        bodyPanel = new javax.swing.JPanel();
        descriptionPanel = new javax.swing.JPanel();
        searchPanel = new javax.swing.JPanel();
        employeePanel = new javax.swing.JPanel();
        employeeHeaderPanel = new javax.swing.JPanel();
        checkboxSearchEmployee = new javax.swing.JCheckBox();
        checkboxEmployee = new javax.swing.JCheckBox();
        employeeInputPanel = new javax.swing.JPanel();
        searchEmployeeIdPanel = new javax.swing.JPanel();
        lblEmployeeID = new javax.swing.JLabel();
        txtEmployeeID = new javax.swing.JTextField();
        btnChooseEmployee = new GUI.Component.Button.ButtonChosen();
        searchEmployeeNamePanel = new javax.swing.JPanel();
        lblEmployeeName = new javax.swing.JLabel();
        txtEmployeeName = new javax.swing.JTextField();
        readerPanel = new javax.swing.JPanel();
        readerHeaderPanel = new javax.swing.JPanel();
        checkboxSearchReader = new javax.swing.JCheckBox();
        checkboxReader = new javax.swing.JCheckBox();
        readerInputPanel = new javax.swing.JPanel();
        searchReaderIdPanel = new javax.swing.JPanel();
        lblReaderID = new javax.swing.JLabel();
        txtReaderID = new javax.swing.JTextField();
        btnChooseReader = new GUI.Component.Button.ButtonChosen();
        searchReaderNamePanel = new javax.swing.JPanel();
        lblReaderName = new javax.swing.JLabel();
        txtReaderName = new javax.swing.JTextField();
        amountPanel = new javax.swing.JPanel();
        amountHeaderPanel = new javax.swing.JPanel();
        checkboxSearchAmount = new javax.swing.JCheckBox();
        checkboxAmount = new javax.swing.JCheckBox();
        amountInputPanel = new javax.swing.JPanel();
        searchMinAmountPanel = new javax.swing.JPanel();
        lblMinAmount = new javax.swing.JLabel();
        txtMinAmount = new javax.swing.JSpinner();
        searchMaxAmountPanel = new javax.swing.JPanel();
        lblMaxAmount = new javax.swing.JLabel();
        txtMaxAmount = new javax.swing.JSpinner();
        statusPanel = new javax.swing.JPanel();
        statusHeaderPanel = new javax.swing.JPanel();
        checkboxSearchStatus = new javax.swing.JCheckBox();
        checkboxStatus = new javax.swing.JCheckBox();
        statusInputPanel = new javax.swing.JPanel();
        subStatusPanel1 = new javax.swing.JPanel();
        radStatusAll = new javax.swing.JRadioButton();
        subStatusPanel2 = new javax.swing.JPanel();
        radHavePaid = new javax.swing.JRadioButton();
        subStatusPanel3 = new javax.swing.JPanel();
        radNotPay = new javax.swing.JRadioButton();
        payDatePanel = new javax.swing.JPanel();
        subPanel = new javax.swing.JPanel();
        checkboxSearchPayDate = new javax.swing.JCheckBox();
        checkboxPayDate = new javax.swing.JCheckBox();
        payDateInputPanel = new javax.swing.JPanel();
        searchStartPayDatePanel = new javax.swing.JPanel();
        lblStartPayDate = new javax.swing.JLabel();
        txtPayDateStart = new com.toedter.calendar.JDateChooser();
        searchEndPayDatePanel = new javax.swing.JPanel();
        lblEndPayDate = new javax.swing.JLabel();
        txtPayDateEnd = new com.toedter.calendar.JDateChooser();
        penaltyDatePanel = new javax.swing.JPanel();
        penaltyDateHeaderPanel = new javax.swing.JPanel();
        checkboxSearchPenaltyDate = new javax.swing.JCheckBox();
        checkboxPenaltyDate = new javax.swing.JCheckBox();
        penaltyDateInputPanel = new javax.swing.JPanel();
        searchStartPenaltyDatePanel = new javax.swing.JPanel();
        lblStartPenaltyDate = new javax.swing.JLabel();
        txtPenaltyStart = new com.toedter.calendar.JDateChooser();
        searchEndPenaltyDatePanel = new javax.swing.JPanel();
        lblEndPenaltyDate = new javax.swing.JLabel();
        txtPenaltyEnd = new com.toedter.calendar.JDateChooser();

        setBackground(new java.awt.Color(255, 255, 255));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });
        getContentPane().setLayout(new java.awt.BorderLayout(0, 5));

        buttonsPanel.setBackground(new java.awt.Color(255, 255, 255));
        buttonsPanel.setPreferredSize(new java.awt.Dimension(100, 40));
        buttonsPanel.setRequestFocusEnabled(false);
        buttonsPanel.setLayout(new java.awt.BorderLayout());

        leftButtonsPanel.setBackground(new java.awt.Color(255, 255, 255));
        leftButtonsPanel.setPreferredSize(new java.awt.Dimension(140, 40));

        btnCombineAll.setText("Kết hợp tất cả");
        btnCombineAll.setPreferredSize(new java.awt.Dimension(120, 30));
        btnCombineAll.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnCombineAllMouseClicked(evt);
            }
        });
        leftButtonsPanel.add(btnCombineAll);

        buttonsPanel.add(leftButtonsPanel, java.awt.BorderLayout.LINE_START);

        rightButtonsPanel.setBackground(new java.awt.Color(255, 255, 255));
        rightButtonsPanel.setMinimumSize(new java.awt.Dimension(150, 100));
        rightButtonsPanel.setPreferredSize(new java.awt.Dimension(140, 100));

        btnDeleteCombine.setText("Xóa kết hợp");
        btnDeleteCombine.setPreferredSize(new java.awt.Dimension(102, 30));
        btnDeleteCombine.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDeleteCombineMouseClicked(evt);
            }
        });
        rightButtonsPanel.add(btnDeleteCombine);

        buttonsPanel.add(rightButtonsPanel, java.awt.BorderLayout.LINE_END);

        middleButtonsPanel.setBackground(new java.awt.Color(255, 255, 255));

        btnSubmit.setText("Tìm kiếm");
        btnSubmit.setPreferredSize(new java.awt.Dimension(80, 30));
        btnSubmit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnSubmitMouseClicked(evt);
            }
        });
        btnSubmit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnSubmitKeyPressed(evt);
            }
        });
        middleButtonsPanel.add(btnSubmit);

        btnReject.setText("Hủy bỏ");
        btnReject.setPreferredSize(new java.awt.Dimension(80, 30));
        btnReject.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRejectMouseClicked(evt);
            }
        });
        middleButtonsPanel.add(btnReject);

        buttonsPanel.add(middleButtonsPanel, java.awt.BorderLayout.CENTER);

        getContentPane().add(buttonsPanel, java.awt.BorderLayout.SOUTH);

        headerPanel.setBackground(new java.awt.Color(255, 255, 255));
        headerPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(153, 153, 153)));
        headerPanel.setPreferredSize(new java.awt.Dimension(100, 50));
        headerPanel.setLayout(new java.awt.BorderLayout());

        lblHeader.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblHeader.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblHeader.setText("Tìm kiếm phiếu phạt");
        headerPanel.add(lblHeader, java.awt.BorderLayout.CENTER);

        getContentPane().add(headerPanel, java.awt.BorderLayout.NORTH);

        bodyPanel.setBackground(new java.awt.Color(255, 255, 255));
        bodyPanel.setLayout(new java.awt.BorderLayout());

        descriptionPanel.setBackground(new java.awt.Color(255, 255, 255));
        descriptionPanel.setLayout(new java.awt.BorderLayout());
        bodyPanel.add(descriptionPanel, java.awt.BorderLayout.PAGE_START);

        searchPanel.setBackground(new java.awt.Color(255, 255, 255));
        searchPanel.setLayout(new java.awt.GridLayout(3, 2, 5, 5));

        employeePanel.setBackground(new java.awt.Color(255, 255, 255));
        employeePanel.setLayout(new java.awt.BorderLayout());

        employeeHeaderPanel.setBackground(new java.awt.Color(255, 255, 255));
        employeeHeaderPanel.setPreferredSize(new java.awt.Dimension(364, 20));
        employeeHeaderPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        checkboxSearchEmployee.setText("Tìm");
        checkboxSearchEmployee.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                checkboxSearchEmployeeMouseClicked(evt);
            }
        });
        employeeHeaderPanel.add(checkboxSearchEmployee);

        checkboxEmployee.setText("Kết hợp (AND)");
        employeeHeaderPanel.add(checkboxEmployee);

        employeePanel.add(employeeHeaderPanel, java.awt.BorderLayout.PAGE_START);

        employeeInputPanel.setBackground(new java.awt.Color(255, 255, 255));
        employeeInputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Nhân viên"));
        employeeInputPanel.setLayout(new java.awt.GridLayout(2, 0));

        searchEmployeeIdPanel.setBackground(new java.awt.Color(255, 255, 255));
        searchEmployeeIdPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 5));

        lblEmployeeID.setText("Mã NV :");
        lblEmployeeID.setPreferredSize(new java.awt.Dimension(45, 16));
        searchEmployeeIdPanel.add(lblEmployeeID);

        txtEmployeeID.setPreferredSize(new java.awt.Dimension(100, 22));
        searchEmployeeIdPanel.add(txtEmployeeID);

        btnChooseEmployee.setPreferredSize(new java.awt.Dimension(35, 22));
        btnChooseEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChooseEmployeeActionPerformed(evt);
            }
        });
        searchEmployeeIdPanel.add(btnChooseEmployee);

        employeeInputPanel.add(searchEmployeeIdPanel);

        searchEmployeeNamePanel.setBackground(new java.awt.Color(255, 255, 255));
        searchEmployeeNamePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 5));

        lblEmployeeName.setText("Tên NV :");
        searchEmployeeNamePanel.add(lblEmployeeName);

        txtEmployeeName.setEditable(false);
        txtEmployeeName.setPreferredSize(new java.awt.Dimension(160, 22));
        searchEmployeeNamePanel.add(txtEmployeeName);

        employeeInputPanel.add(searchEmployeeNamePanel);

        employeePanel.add(employeeInputPanel, java.awt.BorderLayout.CENTER);

        searchPanel.add(employeePanel);

        readerPanel.setBackground(new java.awt.Color(255, 255, 255));
        readerPanel.setLayout(new java.awt.BorderLayout());

        readerHeaderPanel.setBackground(new java.awt.Color(255, 255, 255));
        readerHeaderPanel.setPreferredSize(new java.awt.Dimension(364, 20));
        readerHeaderPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        checkboxSearchReader.setText("Tìm");
        checkboxSearchReader.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                checkboxSearchReaderMouseClicked(evt);
            }
        });
        readerHeaderPanel.add(checkboxSearchReader);

        checkboxReader.setText("Kết hợp (AND)");
        readerHeaderPanel.add(checkboxReader);

        readerPanel.add(readerHeaderPanel, java.awt.BorderLayout.PAGE_START);

        readerInputPanel.setBackground(new java.awt.Color(255, 255, 255));
        readerInputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Độc giả"));
        readerInputPanel.setLayout(new java.awt.GridLayout(2, 0));

        searchReaderIdPanel.setBackground(new java.awt.Color(255, 255, 255));
        searchReaderIdPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 5));

        lblReaderID.setText("Mã ĐG :");
        lblReaderID.setPreferredSize(new java.awt.Dimension(45, 16));
        searchReaderIdPanel.add(lblReaderID);

        txtReaderID.setPreferredSize(new java.awt.Dimension(100, 22));
        searchReaderIdPanel.add(txtReaderID);

        btnChooseReader.setPreferredSize(new java.awt.Dimension(35, 22));
        searchReaderIdPanel.add(btnChooseReader);

        readerInputPanel.add(searchReaderIdPanel);

        searchReaderNamePanel.setBackground(new java.awt.Color(255, 255, 255));
        searchReaderNamePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 5));

        lblReaderName.setText("Tên ĐG :");
        searchReaderNamePanel.add(lblReaderName);

        txtReaderName.setEditable(false);
        txtReaderName.setPreferredSize(new java.awt.Dimension(160, 22));
        searchReaderNamePanel.add(txtReaderName);

        readerInputPanel.add(searchReaderNamePanel);

        readerPanel.add(readerInputPanel, java.awt.BorderLayout.CENTER);

        searchPanel.add(readerPanel);

        amountPanel.setBackground(new java.awt.Color(255, 255, 255));
        amountPanel.setLayout(new java.awt.BorderLayout());

        amountHeaderPanel.setBackground(new java.awt.Color(255, 255, 255));
        amountHeaderPanel.setPreferredSize(new java.awt.Dimension(364, 20));
        amountHeaderPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        checkboxSearchAmount.setText("Tìm");
        checkboxSearchAmount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                checkboxSearchAmountMouseClicked(evt);
            }
        });
        amountHeaderPanel.add(checkboxSearchAmount);

        checkboxAmount.setText("Kết hợp (AND)");
        amountHeaderPanel.add(checkboxAmount);

        amountPanel.add(amountHeaderPanel, java.awt.BorderLayout.PAGE_START);

        amountInputPanel.setBackground(new java.awt.Color(255, 255, 255));
        amountInputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Tiền phạt"));
        amountInputPanel.setLayout(new java.awt.GridLayout(2, 0));

        searchMinAmountPanel.setBackground(new java.awt.Color(255, 255, 255));
        searchMinAmountPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 5));

        lblMinAmount.setText("Giá thấp nhất :");
        lblMinAmount.setPreferredSize(new java.awt.Dimension(80, 16));
        searchMinAmountPanel.add(lblMinAmount);

        txtMinAmount.setModel(new javax.swing.SpinnerNumberModel(0L, 0L, null, 10000L));
        txtMinAmount.setPreferredSize(new java.awt.Dimension(100, 22));
        txtMinAmount.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                txtMinAmountStateChanged(evt);
            }
        });
        searchMinAmountPanel.add(txtMinAmount);

        amountInputPanel.add(searchMinAmountPanel);

        searchMaxAmountPanel.setBackground(new java.awt.Color(255, 255, 255));
        searchMaxAmountPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 5));

        lblMaxAmount.setText("Giá cao nhất :");
        lblMaxAmount.setPreferredSize(new java.awt.Dimension(80, 16));
        searchMaxAmountPanel.add(lblMaxAmount);

        txtMaxAmount.setModel(new javax.swing.SpinnerNumberModel(0L, 0L, null, 10000L));
        txtMaxAmount.setPreferredSize(new java.awt.Dimension(100, 22));
        txtMaxAmount.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                txtMaxAmountStateChanged(evt);
            }
        });
        searchMaxAmountPanel.add(txtMaxAmount);

        amountInputPanel.add(searchMaxAmountPanel);

        amountPanel.add(amountInputPanel, java.awt.BorderLayout.CENTER);

        searchPanel.add(amountPanel);

        statusPanel.setBackground(new java.awt.Color(255, 255, 255));
        statusPanel.setLayout(new java.awt.BorderLayout());

        statusHeaderPanel.setBackground(new java.awt.Color(255, 255, 255));
        statusHeaderPanel.setPreferredSize(new java.awt.Dimension(364, 20));
        statusHeaderPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        checkboxSearchStatus.setText("Tìm");
        checkboxSearchStatus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                checkboxSearchStatusMouseClicked(evt);
            }
        });
        statusHeaderPanel.add(checkboxSearchStatus);

        checkboxStatus.setText("Kết hợp (AND)");
        statusHeaderPanel.add(checkboxStatus);

        statusPanel.add(statusHeaderPanel, java.awt.BorderLayout.PAGE_START);

        statusInputPanel.setBackground(new java.awt.Color(255, 255, 255));
        statusInputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Trạng thái"));
        statusInputPanel.setLayout(new java.awt.GridLayout(3, 0));

        subStatusPanel1.setBackground(new java.awt.Color(255, 255, 255));
        subStatusPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 5));

        btnGroupStatus.add(radStatusAll);
        radStatusAll.setText("Tất cả");
        subStatusPanel1.add(radStatusAll);

        statusInputPanel.add(subStatusPanel1);

        subStatusPanel2.setBackground(new java.awt.Color(255, 255, 255));
        subStatusPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 5));

        btnGroupStatus.add(radHavePaid);
        radHavePaid.setText("Đã thanh toán");
        subStatusPanel2.add(radHavePaid);

        statusInputPanel.add(subStatusPanel2);

        subStatusPanel3.setBackground(new java.awt.Color(255, 255, 255));
        subStatusPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 5));

        btnGroupStatus.add(radNotPay);
        radNotPay.setText("Chưa thanh toán");
        subStatusPanel3.add(radNotPay);

        statusInputPanel.add(subStatusPanel3);

        statusPanel.add(statusInputPanel, java.awt.BorderLayout.CENTER);

        searchPanel.add(statusPanel);

        payDatePanel.setBackground(new java.awt.Color(255, 255, 255));
        payDatePanel.setLayout(new java.awt.BorderLayout());

        subPanel.setBackground(new java.awt.Color(255, 255, 255));
        subPanel.setPreferredSize(new java.awt.Dimension(364, 20));
        subPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        checkboxSearchPayDate.setText("Tìm");
        checkboxSearchPayDate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                checkboxSearchPayDateMouseClicked(evt);
            }
        });
        subPanel.add(checkboxSearchPayDate);

        checkboxPayDate.setText("Kết hợp (AND)");
        subPanel.add(checkboxPayDate);

        payDatePanel.add(subPanel, java.awt.BorderLayout.PAGE_START);

        payDateInputPanel.setBackground(new java.awt.Color(255, 255, 255));
        payDateInputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Ngày nộp phạt"));
        payDateInputPanel.setLayout(new java.awt.GridLayout(2, 0));

        searchStartPayDatePanel.setBackground(new java.awt.Color(255, 255, 255));
        searchStartPayDatePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 7));

        lblStartPayDate.setText("Từ ngày :");
        lblStartPayDate.setPreferredSize(new java.awt.Dimension(60, 16));
        searchStartPayDatePanel.add(lblStartPayDate);

        txtPayDateStart.setPreferredSize(new java.awt.Dimension(120, 22));
        searchStartPayDatePanel.add(txtPayDateStart);

        payDateInputPanel.add(searchStartPayDatePanel);

        searchEndPayDatePanel.setBackground(new java.awt.Color(255, 255, 255));
        searchEndPayDatePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 5));

        lblEndPayDate.setText("Đến ngày :");
        lblEndPayDate.setPreferredSize(new java.awt.Dimension(60, 16));
        searchEndPayDatePanel.add(lblEndPayDate);

        txtPayDateEnd.setPreferredSize(new java.awt.Dimension(120, 22));
        searchEndPayDatePanel.add(txtPayDateEnd);

        payDateInputPanel.add(searchEndPayDatePanel);

        payDatePanel.add(payDateInputPanel, java.awt.BorderLayout.CENTER);

        searchPanel.add(payDatePanel);

        penaltyDatePanel.setBackground(new java.awt.Color(255, 255, 255));
        penaltyDatePanel.setLayout(new java.awt.BorderLayout());

        penaltyDateHeaderPanel.setBackground(new java.awt.Color(255, 255, 255));
        penaltyDateHeaderPanel.setPreferredSize(new java.awt.Dimension(364, 20));
        penaltyDateHeaderPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        checkboxSearchPenaltyDate.setText("Tìm");
        checkboxSearchPenaltyDate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                checkboxSearchPenaltyDateMouseClicked(evt);
            }
        });
        penaltyDateHeaderPanel.add(checkboxSearchPenaltyDate);

        checkboxPenaltyDate.setText("Kết hợp (AND)");
        penaltyDateHeaderPanel.add(checkboxPenaltyDate);

        penaltyDatePanel.add(penaltyDateHeaderPanel, java.awt.BorderLayout.PAGE_START);

        penaltyDateInputPanel.setBackground(new java.awt.Color(255, 255, 255));
        penaltyDateInputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Ngày phạt"));
        penaltyDateInputPanel.setLayout(new java.awt.GridLayout(2, 0));

        searchStartPenaltyDatePanel.setBackground(new java.awt.Color(255, 255, 255));
        searchStartPenaltyDatePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 7));

        lblStartPenaltyDate.setText("Từ ngày :");
        lblStartPenaltyDate.setPreferredSize(new java.awt.Dimension(60, 16));
        searchStartPenaltyDatePanel.add(lblStartPenaltyDate);

        txtPenaltyStart.setPreferredSize(new java.awt.Dimension(120, 22));
        searchStartPenaltyDatePanel.add(txtPenaltyStart);

        penaltyDateInputPanel.add(searchStartPenaltyDatePanel);

        searchEndPenaltyDatePanel.setBackground(new java.awt.Color(255, 255, 255));
        searchEndPenaltyDatePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 5));

        lblEndPenaltyDate.setText("Đến ngày :");
        lblEndPenaltyDate.setPreferredSize(new java.awt.Dimension(60, 16));
        searchEndPenaltyDatePanel.add(lblEndPenaltyDate);

        txtPenaltyEnd.setPreferredSize(new java.awt.Dimension(120, 22));
        searchEndPenaltyDatePanel.add(txtPenaltyEnd);

        penaltyDateInputPanel.add(searchEndPenaltyDatePanel);

        penaltyDatePanel.add(penaltyDateInputPanel, java.awt.BorderLayout.CENTER);

        searchPanel.add(penaltyDatePanel);

        bodyPanel.add(searchPanel, java.awt.BorderLayout.CENTER);

        getContentPane().add(bodyPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Closes the dialog
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog

    private void btnCombineAllMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCombineAllMouseClicked
        employeeInputPanel.setBackground(Color.white);
        readerInputPanel.setBackground(Color.white);
        payDateInputPanel.setBackground(Color.white);
        penaltyDateInputPanel.setBackground(Color.white);
        amountInputPanel.setBackground(Color.white);
        statusInputPanel.setBackground(Color.white);

        turnOnTxt(searchEmployeeIdPanel, searchEmployeeNamePanel, txtEmployeeID,  btnChooseEmployee);
        turnOnTxt(searchReaderIdPanel,searchReaderNamePanel,txtReaderID, btnChooseReader);
        turnOnDate(searchStartPayDatePanel, searchEndPayDatePanel, txtPayDateStart, txtPayDateEnd);
        turnOnDate(searchStartPenaltyDatePanel, searchEndPenaltyDatePanel, txtPenaltyStart, txtPenaltyEnd);
        turnOnSpn(searchMinAmountPanel, searchMaxAmountPanel, txtMinAmount, txtMaxAmount);
        turnOnRad(subStatusPanel1, subStatusPanel2, subStatusPanel3);

        checkboxSearchStatus.setSelected(true);
        checkboxSearchEmployee.setSelected(true);
        checkboxSearchPenaltyDate.setSelected(true);
        checkboxSearchReader.setSelected(true);
        checkboxSearchPayDate.setSelected(true);
        checkboxSearchAmount.setSelected(true);

        checkboxEmployee.setVisible(true);
        checkboxReader.setVisible(true);
        checkboxPenaltyDate.setVisible(true);
        checkboxAmount.setVisible(true);
        checkboxPayDate.setVisible(true);
        checkboxStatus.setVisible(true);

        checkboxEmployee.setSelected(true);
        checkboxReader.setSelected(true);
        checkboxPenaltyDate.setSelected(true);
        checkboxAmount.setSelected(true);
        checkboxPayDate.setSelected(true);
        checkboxStatus.setSelected(true);
    }//GEN-LAST:event_btnCombineAllMouseClicked

    private void btnDeleteCombineMouseClicked(java.awt.event.MouseEvent evt) {

//GEN-FIRST:event_btnDeleteCombineMouseClicked
        checkboxEmployee.setSelected(false);
        checkboxReader.setSelected(false);
        checkboxPenaltyDate.setSelected(false);
        checkboxAmount.setSelected(false);
        checkboxPayDate.setSelected(false);
        checkboxStatus.setSelected(false);
    }//GEN-LAST:event_btnDeleteCombineMouseClicked

    private void btnSubmitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnSubmitKeyPressed
    }//GEN-LAST:event_btnSubmitKeyPressed

    private void btnSubmitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSubmitMouseClicked
        //Lưu các điều kiện vào list :
        List<SearchCondition> conditionList = new ArrayList<>();

        if(!txtReaderID.getText().isEmpty()){
            System.out.println(txtReaderID.getText());
            conditionList.add(new SearchCondition(SearchCondition.Field.READER,
                    txtReaderID.getText(), checkboxReader.isSelected()));
        }

        if(!txtEmployeeID.getText().isEmpty()){
            System.out.println(txtEmployeeID.getText());
            conditionList.add(new SearchCondition(SearchCondition.Field.EMPLOYEE,
                    txtEmployeeID.getText(), checkboxEmployee.isSelected()));
        }

        String selectedStatus = radStatusAll.isSelected() ? "Tất cả" :
                radHavePaid.isSelected() ? "Đã thanh toán" : "Chưa thanh toán";

        System.out.println(selectedStatus);
        conditionList.add(new SearchCondition(SearchCondition.Field.STATUS,
                    selectedStatus, checkboxStatus.isSelected()));



        Number minNum = (Number) txtMinAmount.getValue();
        Number maxNum = (Number) txtMaxAmount.getValue();

        long min = minNum.longValue();
        long max = maxNum.longValue();

        if (max != 0) {
            Long[] amountRange = new Long[]{min, max};
            System.out.println(amountRange[0] + "   " + amountRange[1]);
            conditionList.add(new SearchCondition(SearchCondition.Field.AMOUNT, amountRange, checkboxAmount.isSelected()));
        }

        //Chỉ cần 1 trong 2 ngày không null :
        if(txtPayDateEnd.getDate() != null || txtPayDateStart.getDate() != null){
            Date[] payDateRange = new Date[]{txtPayDateStart.getDate(), txtPayDateEnd.getDate()};
            System.out.println(payDateRange[0] + "   " + payDateRange[1]);
            conditionList.add(new SearchCondition(SearchCondition.Field.PAY_DATE, payDateRange, checkboxPayDate.isSelected()));
        }

        if(txtPenaltyStart.getDate() != null || txtPenaltyEnd.getDate() != null){
            Date[] penaltyDateRange = new Date[]{txtPenaltyStart.getDate(), txtPenaltyEnd.getDate()};
            System.out.println(penaltyDateRange[0] + "   " + penaltyDateRange[1]);
            conditionList.add(new SearchCondition(SearchCondition.Field.PENALTY_DATE, penaltyDateRange, checkboxPenaltyDate.isSelected()));
        }

        System.out.println("Size : " + conditionList.size());
        List<PenaltyDTO> filteredList = filterPenaltyList(conditionList);
        System.out.println("Result size : " + filteredList.size());
        PenaltyPanel.advanceFilterTable(filteredList);
        dispose();
    }//GEN-LAST:event_btnSubmitMouseClicked

    private void btnRejectMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRejectMouseClicked
        dispose();
    }//GEN-LAST:event_btnRejectMouseClicked

    private void btnChooseEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChooseEmployeeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnChooseEmployeeActionPerformed

    private void checkboxSearchEmployeeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_checkboxSearchEmployeeMouseClicked
        if(checkboxSearchEmployee.isSelected()) {
            turnOnTxt(searchEmployeeIdPanel, searchEmployeeNamePanel, txtEmployeeID, btnChooseEmployee);
            employeeInputPanel.setBackground(Color.white);
            checkboxEmployee.setVisible(true);
        }
        else {
            turnOffTxt(searchEmployeeIdPanel, searchEmployeeNamePanel, txtEmployeeID, btnChooseEmployee);
            employeeInputPanel.setBackground(offColor);
            checkboxEmployee.setVisible(false);
            checkboxEmployee.setSelected(false);
        }
    }//GEN-LAST:event_checkboxSearchEmployeeMouseClicked

    private void checkboxSearchReaderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_checkboxSearchReaderMouseClicked
        if(checkboxSearchReader.isSelected()) {
            turnOnTxt(searchReaderIdPanel, searchReaderNamePanel, txtReaderID, btnChooseReader);
            readerInputPanel.setBackground(Color.white);
            checkboxReader.setVisible(true);
        }
        else {
            turnOffTxt(searchReaderIdPanel, searchReaderNamePanel, txtReaderID, btnChooseReader);
            readerInputPanel.setBackground(offColor);
            checkboxReader.setVisible(false);
            checkboxReader.setSelected(false);
        }
    }//GEN-LAST:event_checkboxSearchReaderMouseClicked

    private void checkboxSearchPayDateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_checkboxSearchPayDateMouseClicked
        if(checkboxSearchPayDate.isSelected()) {
            turnOnDate(searchStartPayDatePanel, searchEndPayDatePanel, txtPayDateStart, txtPayDateEnd);
            payDateInputPanel.setBackground(Color.white);
            checkboxPayDate.setVisible(true);
        }
        else {
            turnOffDate(searchStartPayDatePanel, searchEndPayDatePanel, txtPayDateStart, txtPayDateEnd);
            payDateInputPanel.setBackground(offColor);
            checkboxPayDate.setVisible(false);
            checkboxPayDate.setSelected(false);
        }
    }//GEN-LAST:event_checkboxSearchPayDateMouseClicked

    private void checkboxSearchPenaltyDateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_checkboxSearchPenaltyDateMouseClicked
        if(checkboxSearchPenaltyDate.isSelected()){
            turnOnDate(searchStartPenaltyDatePanel, searchEndPenaltyDatePanel, txtPayDateStart, txtPenaltyEnd);
            penaltyDateInputPanel.setBackground(Color.white);
            checkboxPenaltyDate.setVisible(true);
        }
        else {
            turnOffDate(searchStartPenaltyDatePanel, searchEndPenaltyDatePanel, txtPenaltyStart, txtPenaltyEnd);
            penaltyDateInputPanel.setBackground(offColor);
            checkboxPenaltyDate.setSelected(false);
            checkboxPenaltyDate.setVisible(false);
        }
    }//GEN-LAST:event_checkboxSearchPenaltyDateMouseClicked


    private void checkboxSearchAmountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_checkboxSearchAmountMouseClicked
        if(checkboxSearchAmount.isSelected()) {
            turnOnSpn(searchMinAmountPanel, searchMaxAmountPanel, txtMinAmount, txtMaxAmount);
            amountInputPanel.setBackground(Color.white);
            checkboxAmount.setVisible(true);
        }
        else {
            turnOffSpn(searchMinAmountPanel, searchMaxAmountPanel, txtMinAmount, txtMaxAmount);
            amountInputPanel.setBackground(offColor);
            checkboxAmount.setSelected(false);
            checkboxAmount.setVisible(false);
        }
    }//GEN-LAST:event_checkboxSearchAmountMouseClicked

    private void checkboxSearchStatusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_checkboxSearchStatusMouseClicked
        if(checkboxSearchStatus.isSelected()){
            turnOnRad(subStatusPanel1, subStatusPanel2, subStatusPanel3);
            statusInputPanel.setBackground(Color.white);
            checkboxStatus.setVisible(true);
        }
        else{
            turnOffRad(subStatusPanel1, subStatusPanel2, subStatusPanel3);
            statusInputPanel.setBackground(offColor);
            checkboxStatus.setVisible(false);
            checkboxStatus.setSelected(false);
        }
    }//GEN-LAST:event_checkboxSearchStatusMouseClicked

    private void txtMinAmountStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_txtMinAmountStateChanged
        Long min = Long.parseLong(txtMinAmount.getValue().toString());
        Long max = Long.parseLong(txtMaxAmount.getValue().toString());

        if(min > max){
            txtMaxAmount.setValue(min);
        }
    }//GEN-LAST:event_txtMinAmountStateChanged

    private void txtMaxAmountStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_txtMaxAmountStateChanged
        Long min = Long.parseLong(txtMinAmount.getValue().toString());
        Long max = Long.parseLong(txtMaxAmount.getValue().toString());

        if(min > max){
            txtMinAmount.setValue(max);
        }
    }//GEN-LAST:event_txtMaxAmountStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel amountHeaderPanel;
    private javax.swing.JPanel amountInputPanel;
    private javax.swing.JPanel amountPanel;
    private javax.swing.JPanel bodyPanel;
    private GUI.Component.Button.ButtonChosen btnChooseEmployee;
    private GUI.Component.Button.ButtonChosen btnChooseReader;
    private javax.swing.JButton btnCombineAll;
    private javax.swing.JButton btnDeleteCombine;
    private javax.swing.ButtonGroup btnGroupStatus;
    private javax.swing.JButton btnReject;
    private javax.swing.JButton btnSubmit;
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JCheckBox checkboxAmount;
    private javax.swing.JCheckBox checkboxEmployee;
    private javax.swing.JCheckBox checkboxPayDate;
    private javax.swing.JCheckBox checkboxPenaltyDate;
    private javax.swing.JCheckBox checkboxReader;
    private javax.swing.JCheckBox checkboxSearchAmount;
    private javax.swing.JCheckBox checkboxSearchEmployee;
    private javax.swing.JCheckBox checkboxSearchPayDate;
    private javax.swing.JCheckBox checkboxSearchPenaltyDate;
    private javax.swing.JCheckBox checkboxSearchReader;
    private javax.swing.JCheckBox checkboxSearchStatus;
    private javax.swing.JCheckBox checkboxStatus;
    private javax.swing.JPanel descriptionPanel;
    private javax.swing.JPanel employeeHeaderPanel;
    private javax.swing.JPanel employeeInputPanel;
    private javax.swing.JPanel employeePanel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel lblEmployeeID;
    private javax.swing.JLabel lblEmployeeName;
    private javax.swing.JLabel lblEndPayDate;
    private javax.swing.JLabel lblEndPenaltyDate;
    private javax.swing.JLabel lblHeader;
    private javax.swing.JLabel lblMaxAmount;
    private javax.swing.JLabel lblMinAmount;
    private javax.swing.JLabel lblReaderID;
    private javax.swing.JLabel lblReaderName;
    private javax.swing.JLabel lblStartPayDate;
    private javax.swing.JLabel lblStartPenaltyDate;
    private javax.swing.JPanel leftButtonsPanel;
    private javax.swing.JPanel middleButtonsPanel;
    private javax.swing.JPanel payDateInputPanel;
    private javax.swing.JPanel payDatePanel;
    private javax.swing.JPanel penaltyDateHeaderPanel;
    private javax.swing.JPanel penaltyDateInputPanel;
    private javax.swing.JPanel penaltyDatePanel;
    private javax.swing.JRadioButton radHavePaid;
    private javax.swing.JRadioButton radNotPay;
    private javax.swing.JRadioButton radStatusAll;
    private javax.swing.JPanel readerHeaderPanel;
    private javax.swing.JPanel readerInputPanel;
    private javax.swing.JPanel readerPanel;
    private javax.swing.JPanel rightButtonsPanel;
    private javax.swing.JPanel searchEmployeeIdPanel;
    private javax.swing.JPanel searchEmployeeNamePanel;
    private javax.swing.JPanel searchEndPayDatePanel;
    private javax.swing.JPanel searchEndPenaltyDatePanel;
    private javax.swing.JPanel searchMaxAmountPanel;
    private javax.swing.JPanel searchMinAmountPanel;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JPanel searchReaderIdPanel;
    private javax.swing.JPanel searchReaderNamePanel;
    private javax.swing.JPanel searchStartPayDatePanel;
    private javax.swing.JPanel searchStartPenaltyDatePanel;
    private javax.swing.JPanel statusHeaderPanel;
    private javax.swing.JPanel statusInputPanel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JPanel subPanel;
    private javax.swing.JPanel subStatusPanel1;
    private javax.swing.JPanel subStatusPanel2;
    private javax.swing.JPanel subStatusPanel3;
    private javax.swing.JTextField txtEmployeeID;
    private javax.swing.JTextField txtEmployeeName;
    private javax.swing.JSpinner txtMaxAmount;
    private javax.swing.JSpinner txtMinAmount;
    private com.toedter.calendar.JDateChooser txtPayDateEnd;
    private com.toedter.calendar.JDateChooser txtPayDateStart;
    private com.toedter.calendar.JDateChooser txtPenaltyEnd;
    private com.toedter.calendar.JDateChooser txtPenaltyStart;
    private javax.swing.JTextField txtReaderID;
    private javax.swing.JTextField txtReaderName;
    // End of variables declaration//GEN-END:variables
}
