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
import GUI.Controller.Controller;
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
        contentPanel = new javax.swing.JPanel();
        searchEmployeePanel = new javax.swing.JPanel();
        checkboxEmployee = new javax.swing.JCheckBox();
        employeeInputPanel = new javax.swing.JPanel();
        lblEmployeeID = new javax.swing.JLabel();
        lblEmployeeName = new javax.swing.JLabel();
        btnChooseEmployee = new GUI.Component.Button.ButtonChosen();
        txtEmployeeName = new javax.swing.JTextField();
        txtEmployeeID = new javax.swing.JTextField();
        searchReaderPanel = new javax.swing.JPanel();
        checkboxReader = new javax.swing.JCheckBox();
        readerInputPanel = new javax.swing.JPanel();
        lblReaderID = new javax.swing.JLabel();
        txtReaderID = new javax.swing.JTextField();
        btnChooseReader = new GUI.Component.Button.ButtonChosen();
        txtReaderName = new javax.swing.JTextField();
        lblReaderName = new javax.swing.JLabel();
        searchAmountPanel = new javax.swing.JPanel();
        checkboxAmount = new javax.swing.JCheckBox();
        amountInputPanel = new javax.swing.JPanel();
        txtMinAmount = new javax.swing.JSpinner();
        lblMinAmount = new javax.swing.JLabel();
        lblMaxAmount = new javax.swing.JLabel();
        txtMaxAmount = new javax.swing.JSpinner();
        searchStatusPanel = new javax.swing.JPanel();
        checkboxStatus = new javax.swing.JCheckBox();
        statusInputPanel = new javax.swing.JPanel();
        radStatusAll = new javax.swing.JRadioButton();
        radHavePaid = new javax.swing.JRadioButton();
        radNotPay = new javax.swing.JRadioButton();
        searchPayDate = new javax.swing.JPanel();
        checkboxPayDate = new javax.swing.JCheckBox();
        payDateInputPanel = new javax.swing.JPanel();
        lblPayDateStart = new javax.swing.JLabel();
        txtPayDateStart = new com.toedter.calendar.JDateChooser();
        lblPayDateEnd = new javax.swing.JLabel();
        txtPayDateEnd = new com.toedter.calendar.JDateChooser();
        searchPenaltyDate = new javax.swing.JPanel();
        checkboxPenaltyDate = new javax.swing.JCheckBox();
        penaltyDateInputPanel = new javax.swing.JPanel();
        lblPenaltyStart = new javax.swing.JLabel();
        txtPenaltyStart = new com.toedter.calendar.JDateChooser();
        txtPenaltyEnd = new com.toedter.calendar.JDateChooser();
        lblPenaltyEnd = new javax.swing.JLabel();

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

        contentPanel.setBackground(new java.awt.Color(255, 255, 255));
        contentPanel.setLayout(new java.awt.GridLayout(3, 2, 5, 10));

        searchEmployeePanel.setBackground(new java.awt.Color(255, 255, 255));
        searchEmployeePanel.setLayout(new java.awt.BorderLayout());

        checkboxEmployee.setBackground(new java.awt.Color(255, 255, 255));
        checkboxEmployee.setText("Kết hợp");
        searchEmployeePanel.add(checkboxEmployee, java.awt.BorderLayout.PAGE_START);

        employeeInputPanel.setBackground(new java.awt.Color(255, 255, 255));
        employeeInputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Nhân viên xử lý"));

        lblEmployeeID.setText("Mã NV :");
        lblEmployeeID.setPreferredSize(new java.awt.Dimension(42, 25));

        lblEmployeeName.setText("Họ tên :");
        lblEmployeeName.setPreferredSize(new java.awt.Dimension(42, 25));

        btnChooseEmployee.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnChooseEmployeeMouseClicked(evt);
            }
        });

        txtEmployeeName.setEditable(false);
        txtEmployeeName.setPreferredSize(new java.awt.Dimension(73, 25));

        txtEmployeeID.setPreferredSize(new java.awt.Dimension(73, 25));

        javax.swing.GroupLayout employeeInputPanelLayout = new javax.swing.GroupLayout(employeeInputPanel);
        employeeInputPanel.setLayout(employeeInputPanelLayout);
        employeeInputPanelLayout.setHorizontalGroup(
            employeeInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(employeeInputPanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(employeeInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblEmployeeID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblEmployeeName, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(employeeInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, employeeInputPanelLayout.createSequentialGroup()
                        .addComponent(txtEmployeeID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnChooseEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtEmployeeName, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(67, Short.MAX_VALUE))
        );
        employeeInputPanelLayout.setVerticalGroup(
            employeeInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(employeeInputPanelLayout.createSequentialGroup()
                .addGap(0, 15, Short.MAX_VALUE)
                .addGroup(employeeInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lblEmployeeID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtEmployeeID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnChooseEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(employeeInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEmployeeName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEmployeeName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );

        searchEmployeePanel.add(employeeInputPanel, java.awt.BorderLayout.CENTER);

        contentPanel.add(searchEmployeePanel);

        searchReaderPanel.setBackground(new java.awt.Color(255, 255, 255));
        searchReaderPanel.setLayout(new java.awt.BorderLayout());

        checkboxReader.setBackground(new java.awt.Color(255, 255, 255));
        checkboxReader.setText("Kết hợp");
        searchReaderPanel.add(checkboxReader, java.awt.BorderLayout.PAGE_START);

        readerInputPanel.setBackground(new java.awt.Color(255, 255, 255));
        readerInputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Độc giả"));

        lblReaderID.setText("Mã ĐG :");
        lblReaderID.setPreferredSize(new java.awt.Dimension(42, 25));

        txtReaderID.setPreferredSize(new java.awt.Dimension(73, 25));

        btnChooseReader.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnChooseReaderMouseClicked(evt);
            }
        });

        txtReaderName.setEditable(false);
        txtReaderName.setPreferredSize(new java.awt.Dimension(73, 25));

        lblReaderName.setText("Họ tên :");
        lblReaderName.setPreferredSize(new java.awt.Dimension(42, 25));

        javax.swing.GroupLayout readerInputPanelLayout = new javax.swing.GroupLayout(readerInputPanel);
        readerInputPanel.setLayout(readerInputPanelLayout);
        readerInputPanelLayout.setHorizontalGroup(
            readerInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(readerInputPanelLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(readerInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblReaderID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblReaderName, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(readerInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(readerInputPanelLayout.createSequentialGroup()
                        .addComponent(txtReaderID, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnChooseReader, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtReaderName, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(68, Short.MAX_VALUE))
        );
        readerInputPanelLayout.setVerticalGroup(
            readerInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(readerInputPanelLayout.createSequentialGroup()
                .addGap(0, 15, Short.MAX_VALUE)
                .addGroup(readerInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(lblReaderID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtReaderID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnChooseReader, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(readerInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtReaderName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblReaderName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );

        searchReaderPanel.add(readerInputPanel, java.awt.BorderLayout.CENTER);

        contentPanel.add(searchReaderPanel);

        searchAmountPanel.setBackground(new java.awt.Color(255, 255, 255));
        searchAmountPanel.setLayout(new java.awt.BorderLayout());

        checkboxAmount.setBackground(new java.awt.Color(255, 255, 255));
        checkboxAmount.setText("Kết hợp");
        searchAmountPanel.add(checkboxAmount, java.awt.BorderLayout.PAGE_START);

        amountInputPanel.setBackground(new java.awt.Color(255, 255, 255));
        amountInputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Tiền phạt"));

        txtMinAmount.setModel(new javax.swing.SpinnerNumberModel(0L, 0L, null, 10000L));
        txtMinAmount.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                txtMinAmountStateChanged(evt);
            }
        });

        lblMinAmount.setText("Giá thấp nhất :");
        lblMinAmount.setPreferredSize(new java.awt.Dimension(77, 30));

        lblMaxAmount.setText("Giá cao nhất :");
        lblMaxAmount.setPreferredSize(new java.awt.Dimension(72, 30));

        txtMaxAmount.setModel(new javax.swing.SpinnerNumberModel(0L, 0L, null, 10000L));
        txtMaxAmount.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                txtMaxAmountStateChanged(evt);
            }
        });

        javax.swing.GroupLayout amountInputPanelLayout = new javax.swing.GroupLayout(amountInputPanel);
        amountInputPanel.setLayout(amountInputPanelLayout);
        amountInputPanelLayout.setHorizontalGroup(
            amountInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(amountInputPanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(amountInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblMinAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMaxAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(amountInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtMaxAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMinAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(62, Short.MAX_VALUE))
        );
        amountInputPanelLayout.setVerticalGroup(
            amountInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(amountInputPanelLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(amountInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblMinAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMinAmount, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(amountInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMaxAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMaxAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        searchAmountPanel.add(amountInputPanel, java.awt.BorderLayout.PAGE_END);

        contentPanel.add(searchAmountPanel);

        searchStatusPanel.setBackground(new java.awt.Color(255, 255, 255));
        searchStatusPanel.setLayout(new java.awt.BorderLayout());

        checkboxStatus.setBackground(new java.awt.Color(255, 255, 255));
        checkboxStatus.setText("Kết hợp");
        searchStatusPanel.add(checkboxStatus, java.awt.BorderLayout.PAGE_START);

        statusInputPanel.setBackground(new java.awt.Color(255, 255, 255));
        statusInputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Trạng thái"));

        radStatusAll.setBackground(new java.awt.Color(255, 255, 255));
        btnGroupStatus.add(radStatusAll);
        radStatusAll.setSelected(true);
        radStatusAll.setText("Tất cả");
        radStatusAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radStatusAllActionPerformed(evt);
            }
        });

        radHavePaid.setBackground(new java.awt.Color(255, 255, 255));
        btnGroupStatus.add(radHavePaid);
        radHavePaid.setText("Đã thanh toán");
        radHavePaid.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                radHavePaidStateChanged(evt);
            }
        });

        radNotPay.setBackground(new java.awt.Color(255, 255, 255));
        btnGroupStatus.add(radNotPay);
        radNotPay.setText("Chưa thanh toán");

        javax.swing.GroupLayout statusInputPanelLayout = new javax.swing.GroupLayout(statusInputPanel);
        statusInputPanel.setLayout(statusInputPanelLayout);
        statusInputPanelLayout.setHorizontalGroup(
            statusInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusInputPanelLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(statusInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(radStatusAll, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(radHavePaid, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(radNotPay, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(175, Short.MAX_VALUE))
        );
        statusInputPanelLayout.setVerticalGroup(
            statusInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusInputPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(radStatusAll)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radHavePaid)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radNotPay)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        searchStatusPanel.add(statusInputPanel, java.awt.BorderLayout.CENTER);

        contentPanel.add(searchStatusPanel);

        searchPayDate.setBackground(new java.awt.Color(255, 255, 255));
        searchPayDate.setLayout(new java.awt.BorderLayout());

        checkboxPayDate.setBackground(new java.awt.Color(255, 255, 255));
        checkboxPayDate.setText("Kết hợp");
        searchPayDate.add(checkboxPayDate, java.awt.BorderLayout.PAGE_START);

        payDateInputPanel.setBackground(new java.awt.Color(255, 255, 255));
        payDateInputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Ngày nộp phạt"));

        lblPayDateStart.setText("Từ ngày :");

        txtPayDateStart.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                txtPayDateStartPropertyChange(evt);
            }
        });

        lblPayDateEnd.setText("Đến ngày :");

        txtPayDateEnd.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                txtPayDateEndPropertyChange(evt);
            }
        });

        javax.swing.GroupLayout payDateInputPanelLayout = new javax.swing.GroupLayout(payDateInputPanel);
        payDateInputPanel.setLayout(payDateInputPanelLayout);
        payDateInputPanelLayout.setHorizontalGroup(
            payDateInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(payDateInputPanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(payDateInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPayDateEnd)
                    .addComponent(lblPayDateStart))
                .addGap(18, 18, 18)
                .addGroup(payDateInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtPayDateStart, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPayDateEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(69, Short.MAX_VALUE))
        );
        payDateInputPanelLayout.setVerticalGroup(
            payDateInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(payDateInputPanelLayout.createSequentialGroup()
                .addGap(0, 22, Short.MAX_VALUE)
                .addGroup(payDateInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPayDateStart, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPayDateStart, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(payDateInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtPayDateEnd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblPayDateEnd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(15, 15, 15))
        );

        searchPayDate.add(payDateInputPanel, java.awt.BorderLayout.CENTER);

        contentPanel.add(searchPayDate);

        searchPenaltyDate.setBackground(new java.awt.Color(255, 255, 255));
        searchPenaltyDate.setLayout(new java.awt.BorderLayout());

        checkboxPenaltyDate.setBackground(new java.awt.Color(255, 255, 255));
        checkboxPenaltyDate.setText("Kết hợp");
        searchPenaltyDate.add(checkboxPenaltyDate, java.awt.BorderLayout.PAGE_START);

        penaltyDateInputPanel.setBackground(new java.awt.Color(255, 255, 255));
        penaltyDateInputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Ngày phạt"));

        lblPenaltyStart.setText("Từ ngày :");

        txtPenaltyStart.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                txtPenaltyStartPropertyChange(evt);
            }
        });

        txtPenaltyEnd.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                txtPenaltyEndPropertyChange(evt);
            }
        });

        lblPenaltyEnd.setText("Đến ngày :");

        javax.swing.GroupLayout penaltyDateInputPanelLayout = new javax.swing.GroupLayout(penaltyDateInputPanel);
        penaltyDateInputPanel.setLayout(penaltyDateInputPanelLayout);
        penaltyDateInputPanelLayout.setHorizontalGroup(
            penaltyDateInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(penaltyDateInputPanelLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(penaltyDateInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPenaltyEnd, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPenaltyStart, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(penaltyDateInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtPenaltyStart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtPenaltyEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(58, Short.MAX_VALUE))
        );
        penaltyDateInputPanelLayout.setVerticalGroup(
            penaltyDateInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(penaltyDateInputPanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(penaltyDateInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPenaltyStart, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPenaltyStart, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(penaltyDateInputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtPenaltyEnd, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblPenaltyEnd, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        searchPenaltyDate.add(penaltyDateInputPanel, java.awt.BorderLayout.CENTER);

        contentPanel.add(searchPenaltyDate);

        getContentPane().add(contentPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Closes the dialog
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog

    private void radStatusAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radStatusAllActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_radStatusAllActionPerformed

    private void btnChooseEmployeeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnChooseEmployeeMouseClicked
        ChooseEmployeeDialog chooseEmployeeDialog = new ChooseEmployeeDialog(this);
        chooseEmployeeDialog.setVisible(true);
        if (chooseEmployeeDialog.getSelectedEmployee() != null) {
            Employee currentEmployee = chooseEmployeeDialog.getSelectedEmployee();
            txtEmployeeID.setText(currentEmployee.getId().toString());
            txtEmployeeName.setText(Controller.formatFullName(currentEmployee.getFirstName()+ " "+ currentEmployee.getLastName()));
        }
    }//GEN-LAST:event_btnChooseEmployeeMouseClicked

    private void btnChooseReaderMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnChooseReaderMouseClicked
        ChooseReaderDialog chooseReaderDialog = new ChooseReaderDialog(this);
        chooseReaderDialog.setVisible(true);
        if(chooseReaderDialog.getSelectedReader() != null ){
            ReaderDTO reader = chooseReaderDialog.getSelectedReader();
            txtReaderID.setText(reader.getId());
            txtReaderName.setText(Controller.formatFullName(reader.getLastName() + " " + reader.getFirstName()));
        }
    }//GEN-LAST:event_btnChooseReaderMouseClicked

    private void btnCombineAllMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCombineAllMouseClicked
        checkboxEmployee.setSelected(true);
        checkboxReader.setSelected(true);
        checkboxPenaltyDate.setSelected(true);
        checkboxAmount.setSelected(true);
        checkboxPayDate.setSelected(true);
        checkboxStatus.setSelected(true);
    }//GEN-LAST:event_btnCombineAllMouseClicked

    private void btnDeleteCombineMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDeleteCombineMouseClicked
        checkboxEmployee.setSelected(false);
        checkboxReader.setSelected(false);
        checkboxPenaltyDate.setSelected(false);
        checkboxAmount.setSelected(false);
        checkboxPayDate.setSelected(false);
        checkboxStatus.setSelected(false);
    }//GEN-LAST:event_btnDeleteCombineMouseClicked

    private void txtMinAmountStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_txtMinAmountStateChanged
        Long minPrice = (Long)txtMinAmount.getValue();
        Long maxPrice = (Long)txtMaxAmount.getValue();
        
        if(minPrice > maxPrice){
            txtMaxAmount.setValue(minPrice);
        }
    }//GEN-LAST:event_txtMinAmountStateChanged

    private void txtMaxAmountStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_txtMaxAmountStateChanged
        Long minPrice = (Long)txtMinAmount.getValue();
        Long maxPrice = (Long)txtMaxAmount.getValue();
        
        if(maxPrice < minPrice){
            txtMinAmount.setValue(maxPrice);
        }
    }//GEN-LAST:event_txtMaxAmountStateChanged

    private void txtPayDateStartPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_txtPayDateStartPropertyChange
        Date startDate = txtPayDateStart.getDate();
        Date endDate = txtPayDateEnd.getDate();
        if(startDate == null && endDate == null )
            return;
        if(startDate != null && endDate != null ) {
            if (startDate.after(endDate)) {
                txtPayDateEnd.setDate(startDate);
            }
        }
    }//GEN-LAST:event_txtPayDateStartPropertyChange

    private void txtPayDateEndPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_txtPayDateEndPropertyChange
        Date startDate = txtPayDateStart.getDate();
        Date endDate = txtPayDateEnd.getDate();
        if(startDate == null && endDate == null )
            return;
        if(startDate != null && endDate != null ) {
            if (endDate.before(startDate)) {
                txtPayDateStart.setDate(endDate);
            }
        }
    }//GEN-LAST:event_txtPayDateEndPropertyChange

    private void txtPenaltyStartPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_txtPenaltyStartPropertyChange
        Date startDate = txtPenaltyStart.getDate();
        Date endDate = txtPenaltyEnd.getDate();
        if(startDate == null && endDate == null )
            return;
        if(startDate != null && endDate != null ) {
            if (startDate.after(endDate)) {
                txtPenaltyEnd.setDate(startDate);
            }
        }
    }//GEN-LAST:event_txtPenaltyStartPropertyChange

    private void txtPenaltyEndPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_txtPenaltyEndPropertyChange
        Date startDate = txtPenaltyStart.getDate();
        Date endDate = txtPenaltyEnd.getDate();
        if(startDate == null && endDate == null )
            return;
        if(startDate != null && endDate != null ) {
            if (endDate.before(startDate)) {
                txtPenaltyStart.setDate(endDate);
            }
        }
    }//GEN-LAST:event_txtPenaltyEndPropertyChange

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



        if( (Long)txtMaxAmount.getValue() != 0 ){
            Long[] amountRange = new Long[]{(Long)txtMinAmount.getValue(), (Long)txtMaxAmount.getValue()};
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

    private void radHavePaidStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_radHavePaidStateChanged
        if(checkboxStatus.isSelected() && checkboxPayDate.isSelected()){
            if(radHavePaid.isSelected()){
                txtPayDateStart.setEnabled(true);
                txtPayDateEnd.setEnabled(true);
            }
            else {
                txtPayDateStart.setDate(null);
                txtPayDateEnd.setDate(null);
                txtPayDateStart.setEnabled(false);
                txtPayDateEnd.setEnabled(false);
            }
        }
    }//GEN-LAST:event_radHavePaidStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel amountInputPanel;
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
    private javax.swing.JCheckBox checkboxStatus;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JPanel employeeInputPanel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel lblEmployeeID;
    private javax.swing.JLabel lblEmployeeName;
    private javax.swing.JLabel lblHeader;
    private javax.swing.JLabel lblMaxAmount;
    private javax.swing.JLabel lblMinAmount;
    private javax.swing.JLabel lblPayDateEnd;
    private javax.swing.JLabel lblPayDateStart;
    private javax.swing.JLabel lblPenaltyEnd;
    private javax.swing.JLabel lblPenaltyStart;
    private javax.swing.JLabel lblReaderID;
    private javax.swing.JLabel lblReaderName;
    private javax.swing.JPanel leftButtonsPanel;
    private javax.swing.JPanel middleButtonsPanel;
    private javax.swing.JPanel payDateInputPanel;
    private javax.swing.JPanel penaltyDateInputPanel;
    private javax.swing.JRadioButton radHavePaid;
    private javax.swing.JRadioButton radNotPay;
    private javax.swing.JRadioButton radStatusAll;
    private javax.swing.JPanel readerInputPanel;
    private javax.swing.JPanel rightButtonsPanel;
    private javax.swing.JPanel searchAmountPanel;
    private javax.swing.JPanel searchEmployeePanel;
    private javax.swing.JPanel searchPayDate;
    private javax.swing.JPanel searchPenaltyDate;
    private javax.swing.JPanel searchReaderPanel;
    private javax.swing.JPanel searchStatusPanel;
    private javax.swing.JPanel statusInputPanel;
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
