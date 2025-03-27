package GUI.Component.Panel;

import javax.swing.*;
import java.awt.*;


public class StatisticsPanel extends javax.swing.JPanel {
    private final Color defaultColor = new Color(255, 255, 255);
    private final Color selectedColor = new Color(100, 200, 250);
    private final Color hoverColor = new Color(180, 220, 250);


    private JPanel selectedPanel;

    public StatisticsPanel() {


        initComponents();
        OverviewStatTab.setOpaque(true);
        ReaderStatTab.setOpaque(true);
        EmployeeStatTab.setOpaque(true);
        InventoryStatTab.setOpaque(true);
        LoanSlipStatTab.setOpaque(true);
        SupplierStatTab.setOpaque(true);
        isSelected(OverviewStatTab);
    }

    public void clearSelect() {
        if (selectedPanel != null) {
            selectedPanel.setBackground(defaultColor);
            selectedPanel.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 1, new java.awt.Color(204, 204, 204)));
            selectedPanel.revalidate();
            selectedPanel.repaint();

        }
    }


    public void isSelected(JPanel p) {
        clearSelect();
        selectedPanel = p;
        p.setBackground(selectedColor);
        p.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 0, Color.BLUE));
        p.setOpaque(true);  // Đảm bảo JPanel không bị trong suốt
        this.revalidate();
        this.repaint();
    }


    public void addHover(JPanel p) {
        if (selectedPanel != p) {
            p.setBackground(hoverColor);
        }
    }

    public void deleteHover(JPanel p) {
        if (selectedPanel != p) {
            p.setBackground(defaultColor);
            p.setOpaque(true);  // Đảm bảo JPanel hiển thị màu nền
            p.revalidate();
            p.repaint();
        }
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        LayerPane = new javax.swing.JLayeredPane();
        divTaskbar = new javax.swing.JPanel();
        statisticsTaskbar = new javax.swing.JPanel();
        OverviewStatTab = new javax.swing.JPanel();
        lblOverviewStatTab = new javax.swing.JLabel();
        ReaderStatTab = new javax.swing.JPanel();
        lblReaderStatTab = new javax.swing.JLabel();
        EmployeeStatTab = new javax.swing.JPanel();
        lblEmployeeStatTab = new javax.swing.JLabel();
        InventoryStatTab = new javax.swing.JPanel();
        lblInventoryStatTab = new javax.swing.JLabel();
        LoanSlipStatTab = new javax.swing.JPanel();
        lblLoanSlipStatTab = new javax.swing.JLabel();
        SupplierStatTab = new javax.swing.JPanel();
        lblSupplierStatTab = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        divTaskbar.setBackground(new java.awt.Color(255, 255, 255));
        divTaskbar.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(51, 51, 51)));
        divTaskbar.setPreferredSize(new java.awt.Dimension(892, 30));
        divTaskbar.setLayout(new java.awt.BorderLayout(5, 5));

        statisticsTaskbar.setPreferredSize(new java.awt.Dimension(600, 50));
        statisticsTaskbar.setLayout(new java.awt.GridLayout(1, 5, 0, 5));

        OverviewStatTab.setBackground(new java.awt.Color(255, 255, 255));
        OverviewStatTab.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 1, new java.awt.Color(204, 204, 204)));
        OverviewStatTab.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        OverviewStatTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                OverviewStatTabMouseClicked(evt);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                OverviewStatTabMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                OverviewStatTabMouseExited(evt);
            }
        });

        lblOverviewStatTab.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        lblOverviewStatTab.setText("Tổng quan");
        OverviewStatTab.add(lblOverviewStatTab);

        statisticsTaskbar.add(OverviewStatTab);

        ReaderStatTab.setBackground(new java.awt.Color(255, 255, 255));
        ReaderStatTab.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 1, new java.awt.Color(204, 204, 204)));
        ReaderStatTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ReaderStatTabMouseClicked(evt);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ReaderStatTabMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                ReaderStatTabMouseExited(evt);
            }
        });

        lblReaderStatTab.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        lblReaderStatTab.setText("Độc giả");
        ReaderStatTab.add(lblReaderStatTab);

        statisticsTaskbar.add(ReaderStatTab);

        EmployeeStatTab.setBackground(new java.awt.Color(255, 255, 255));
        EmployeeStatTab.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 1, new java.awt.Color(204, 204, 204)));
        EmployeeStatTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                EmployeeStatTabMouseClicked(evt);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                EmployeeStatTabMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                EmployeeStatTabMouseExited(evt);
            }
        });

        lblEmployeeStatTab.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        lblEmployeeStatTab.setText("Nhân viên");
        EmployeeStatTab.add(lblEmployeeStatTab);

        statisticsTaskbar.add(EmployeeStatTab);

        InventoryStatTab.setBackground(new java.awt.Color(255, 255, 255));
        InventoryStatTab.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 1, new java.awt.Color(204, 204, 204)));
        InventoryStatTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                InventoryStatTabMouseClicked(evt);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                InventoryStatTabMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                InventoryStatTabMouseExited(evt);
            }
        });

        lblInventoryStatTab.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        lblInventoryStatTab.setText("Tồn kho");
        InventoryStatTab.add(lblInventoryStatTab);

        statisticsTaskbar.add(InventoryStatTab);

        LoanSlipStatTab.setBackground(new java.awt.Color(255, 255, 255));
        LoanSlipStatTab.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 1, new java.awt.Color(204, 204, 204)));
        LoanSlipStatTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                LoanSlipStatTabMouseClicked(evt);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                LoanSlipStatTabMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                LoanSlipStatTabMouseExited(evt);
            }
        });

        lblLoanSlipStatTab.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        lblLoanSlipStatTab.setText("Phiếu mượn");
        LoanSlipStatTab.add(lblLoanSlipStatTab);

        statisticsTaskbar.add(LoanSlipStatTab);

        SupplierStatTab.setBackground(new java.awt.Color(255, 255, 255));
        SupplierStatTab.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 1, new java.awt.Color(204, 204, 204)));
        SupplierStatTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SupplierStatTabMouseClicked(evt);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                SupplierStatTabMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                SupplierStatTabMouseExited(evt);
            }
        });

        lblSupplierStatTab.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        lblSupplierStatTab.setText("Nhà cung cấp");
        SupplierStatTab.add(lblSupplierStatTab);

        statisticsTaskbar.add(SupplierStatTab);

        divTaskbar.add(statisticsTaskbar, java.awt.BorderLayout.LINE_START);

        LayerPane.setLayer(divTaskbar, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout LayerPaneLayout = new javax.swing.GroupLayout(LayerPane);
        LayerPane.setLayout(LayerPaneLayout);
        LayerPaneLayout.setHorizontalGroup(
                LayerPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, LayerPaneLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(divTaskbar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        LayerPaneLayout.setVerticalGroup(
                LayerPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(LayerPaneLayout.createSequentialGroup()
                                .addComponent(divTaskbar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 534, Short.MAX_VALUE))
        );

        add(LayerPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void OverviewStatTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OverviewStatTabMouseClicked
        isSelected(OverviewStatTab);
    }//GEN-LAST:event_OverviewStatTabMouseClicked

    private void ReaderStatTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ReaderStatTabMouseClicked
        isSelected(ReaderStatTab);
    }//GEN-LAST:event_ReaderStatTabMouseClicked

    private void EmployeeStatTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EmployeeStatTabMouseClicked
        isSelected(EmployeeStatTab);
    }//GEN-LAST:event_EmployeeStatTabMouseClicked

    private void InventoryStatTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_InventoryStatTabMouseClicked
        isSelected(InventoryStatTab);
    }//GEN-LAST:event_InventoryStatTabMouseClicked

    private void LoanSlipStatTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LoanSlipStatTabMouseClicked
        isSelected(LoanSlipStatTab);
    }//GEN-LAST:event_LoanSlipStatTabMouseClicked

    private void SupplierStatTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SupplierStatTabMouseClicked
        isSelected(SupplierStatTab);
    }//GEN-LAST:event_SupplierStatTabMouseClicked

    private void ReaderStatTabMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ReaderStatTabMouseEntered
        addHover(ReaderStatTab);
    }//GEN-LAST:event_ReaderStatTabMouseEntered

    private void ReaderStatTabMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ReaderStatTabMouseExited
        deleteHover(ReaderStatTab);
    }//GEN-LAST:event_ReaderStatTabMouseExited

    private void OverviewStatTabMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OverviewStatTabMouseEntered
        addHover(OverviewStatTab);
    }//GEN-LAST:event_OverviewStatTabMouseEntered

    private void OverviewStatTabMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_OverviewStatTabMouseExited
        deleteHover(OverviewStatTab);
    }//GEN-LAST:event_OverviewStatTabMouseExited

    private void EmployeeStatTabMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EmployeeStatTabMouseEntered
        addHover(EmployeeStatTab);
    }//GEN-LAST:event_EmployeeStatTabMouseEntered

    private void EmployeeStatTabMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_EmployeeStatTabMouseExited
        deleteHover(EmployeeStatTab);
    }//GEN-LAST:event_EmployeeStatTabMouseExited

    private void InventoryStatTabMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_InventoryStatTabMouseEntered
        addHover(InventoryStatTab);
    }//GEN-LAST:event_InventoryStatTabMouseEntered

    private void InventoryStatTabMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_InventoryStatTabMouseExited
        deleteHover(InventoryStatTab);
    }//GEN-LAST:event_InventoryStatTabMouseExited

    private void LoanSlipStatTabMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LoanSlipStatTabMouseEntered
        addHover(LoanSlipStatTab);
    }//GEN-LAST:event_LoanSlipStatTabMouseEntered

    private void LoanSlipStatTabMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_LoanSlipStatTabMouseExited
        deleteHover(LoanSlipStatTab);
    }//GEN-LAST:event_LoanSlipStatTabMouseExited

    private void SupplierStatTabMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SupplierStatTabMouseEntered
        addHover(SupplierStatTab);
    }//GEN-LAST:event_SupplierStatTabMouseEntered

    private void SupplierStatTabMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SupplierStatTabMouseExited
        deleteHover(SupplierStatTab);
    }//GEN-LAST:event_SupplierStatTabMouseExited


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel EmployeeStatTab;
    private javax.swing.JPanel InventoryStatTab;
    private javax.swing.JLayeredPane LayerPane;
    private javax.swing.JPanel LoanSlipStatTab;
    private javax.swing.JPanel OverviewStatTab;
    private javax.swing.JPanel ReaderStatTab;
    private javax.swing.JPanel SupplierStatTab;
    private javax.swing.JPanel divTaskbar;
    private javax.swing.JLabel lblEmployeeStatTab;
    private javax.swing.JLabel lblInventoryStatTab;
    private javax.swing.JLabel lblLoanSlipStatTab;
    private javax.swing.JLabel lblOverviewStatTab;
    private javax.swing.JLabel lblReaderStatTab;
    private javax.swing.JLabel lblSupplierStatTab;
    private javax.swing.JPanel statisticsTaskbar;
    // End of variables declaration//GEN-END:variables
}
