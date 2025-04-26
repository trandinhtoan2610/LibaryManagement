package GUI.Component.Panel.Statistics;

import java.awt.*;
import javax.swing.*;


public class StatisticsPanel extends javax.swing.JPanel {
    private final Color defaultColor = new Color(255,255,255);
    private final Color selectedColor = new Color(100, 200, 250);
    private final Color hoverColor = new Color(180, 220, 250);
    private final Font defaultFont = new Font("Segoe UI", Font.PLAIN, 12);
    private final Font selectedFont = new Font("Segoe UI", Font.BOLD, 15);
    private JPanel selectedPanelTab;

    //Khai báo panel thống kê tại đây :
    private JPanel testOverviewPanel;
    private JPanel testPanel2;
    private JPanel testPanel3;

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private final String overviewAlias = "OVERVIEW";
    private final String bookAlias = "BOOK";
    private final String purchaseAlias = "PURCHASE";
    private final String borrowAlias = "BORROW";
    private final String penaltyAlias = "PENALTY";


    public StatisticsPanel() {
        initComponents();
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        contentPanel.add(cardPanel);

        //Ví dụ :
        testOverviewPanel = new JPanel();
        testPanel2 = new JPanel();
        testPanel3 = new JPanel();
        testOverviewPanel.setBackground(Color.RED);
        testPanel3.setBackground(Color.blue);

        //cardPanel.add( <<JPanel>> , <<Alias>>). VD :
        cardPanel.add(testOverviewPanel, overviewAlias);
        cardPanel.add(testPanel3, bookAlias);

        //Mặc định là thống kê overview(Tổng quan) :
        isSelected(overviewTab, overviewAlias);  //Kéo xuống dưới có các hàm MouseClick -> ghi isSelected phù hợp.
    }

    public void clearSelect() {
        if (selectedPanelTab != null) {
            selectedPanelTab.setBackground(defaultColor);
            if(selectedPanelTab == penaltyTab)
                selectedPanelTab.setBorder(null);
            else
                selectedPanelTab.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 1, new java.awt.Color(204, 204, 204)));
            selectedPanelTab.revalidate();
            selectedPanelTab.repaint();
        }
    }


    public void isSelected(JPanel p, String header) {
        clearSelect();
        selectedPanelTab = p;
        cardLayout.show(cardPanel,header);
        p.setBackground(selectedColor);
        p.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 0, Color.BLUE));
        p.setOpaque(true);
        this.revalidate();
        this.repaint();
    }

    public void addHover(JPanel p) {
        if (selectedPanelTab != p) {
            p.setBackground(hoverColor);
        }
    }

    public void deleteHover(JPanel p) {
        if (selectedPanelTab != p) {
            p.setBackground(defaultColor);
            p.setOpaque(true);  // Đảm bảo JPanel hiển thị màu nền
            p.revalidate();
            p.repaint();
        }
    }


    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerPanel = new javax.swing.JPanel();
        overviewTab = new javax.swing.JPanel();
        lblOverviewTab = new javax.swing.JLabel();
        bookTab = new javax.swing.JPanel();
        lblBookTab = new javax.swing.JLabel();
        purchaseOrderTab = new javax.swing.JPanel();
        lblPurchaseOrderTab = new javax.swing.JLabel();
        borrowSheetTab = new javax.swing.JPanel();
        lblBorrowTab = new javax.swing.JLabel();
        penaltyTab = new javax.swing.JPanel();
        lblPenaltyTab = new javax.swing.JLabel();
        contentPanel = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout());

        headerPanel.setBackground(new java.awt.Color(255, 255, 255));
        headerPanel.setPreferredSize(new java.awt.Dimension(1106, 30));
        headerPanel.setLayout(new java.awt.GridLayout(1, 5));

        overviewTab.setBackground(new java.awt.Color(255, 255, 255));
        overviewTab.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 1, new java.awt.Color(204, 204, 204)));
        overviewTab.setOpaque(false);
        overviewTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                overviewTabMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                overviewTabMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                overviewTabMouseExited(evt);
            }
        });

        lblOverviewTab.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblOverviewTab.setText("Tổng quan");
        lblOverviewTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblOverviewTabMouseClicked(evt);
            }
        });
        overviewTab.add(lblOverviewTab);

        headerPanel.add(overviewTab);

        bookTab.setBackground(new java.awt.Color(255, 255, 255));
        bookTab.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 1, new java.awt.Color(204, 204, 204)));
        bookTab.setOpaque(false);
        bookTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bookTabMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                bookTabMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                bookTabMouseExited(evt);
            }
        });

        lblBookTab.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBookTab.setText("Sách");
        bookTab.add(lblBookTab);

        headerPanel.add(bookTab);

        purchaseOrderTab.setBackground(new java.awt.Color(255, 255, 255));
        purchaseOrderTab.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 1, new java.awt.Color(204, 204, 204)));
        purchaseOrderTab.setOpaque(false);
        purchaseOrderTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                purchaseOrderTabMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                purchaseOrderTabMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                purchaseOrderTabMouseExited(evt);
            }
        });

        lblPurchaseOrderTab.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPurchaseOrderTab.setText("Phiếu nhập");
        purchaseOrderTab.add(lblPurchaseOrderTab);

        headerPanel.add(purchaseOrderTab);

        borrowSheetTab.setBackground(new java.awt.Color(255, 255, 255));
        borrowSheetTab.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 1, new java.awt.Color(204, 204, 204)));
        borrowSheetTab.setOpaque(false);
        borrowSheetTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                borrowSheetTabMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                borrowSheetTabMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                borrowSheetTabMouseExited(evt);
            }
        });

        lblBorrowTab.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBorrowTab.setText("Phiếu mượn");
        borrowSheetTab.add(lblBorrowTab);

        headerPanel.add(borrowSheetTab);

        penaltyTab.setBackground(new java.awt.Color(255, 255, 255));
        penaltyTab.setOpaque(false);
        penaltyTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                penaltyTabMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                penaltyTabMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                penaltyTabMouseExited(evt);
            }
        });

        lblPenaltyTab.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPenaltyTab.setText("Phiếu phạt");
        penaltyTab.add(lblPenaltyTab);

        headerPanel.add(penaltyTab);

        add(headerPanel, java.awt.BorderLayout.PAGE_START);

        contentPanel.setLayout(new java.awt.BorderLayout());
        add(contentPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void lblOverviewTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblOverviewTabMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lblOverviewTabMouseClicked


    //MOUSE CLICK CỦA TAB TỔNG QUAN :
    private void overviewTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_overviewTabMouseClicked
        isSelected(overviewTab, overviewAlias);   // Tham số 1 : panel thống kê - Tham số 2 : alias
    }//GEN-LAST:event_overviewTabMouseClicked



    private void overviewTabMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_overviewTabMouseEntered
        addHover(overviewTab);
    }//GEN-LAST:event_overviewTabMouseEntered

    private void overviewTabMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_overviewTabMouseExited
        deleteHover(overviewTab);
    }//GEN-LAST:event_overviewTabMouseExited


    //MOUSE CLICK CỦA TAB SÁCH :
    private void bookTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bookTabMouseClicked
        isSelected(bookTab, bookAlias);
    }//GEN-LAST:event_bookTabMouseClicked


    private void bookTabMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bookTabMouseEntered
        addHover(bookTab);
    }//GEN-LAST:event_bookTabMouseEntered

    private void bookTabMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bookTabMouseExited
        deleteHover(bookTab);
    }//GEN-LAST:event_bookTabMouseExited


    //MOUSE CLICK CỦA TAB PHIẾU NHẬP :
    private void purchaseOrderTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_purchaseOrderTabMouseClicked
        isSelected(purchaseOrderTab, purchaseAlias);
    }//GEN-LAST:event_purchaseOrderTabMouseClicked

    private void purchaseOrderTabMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_purchaseOrderTabMouseEntered
        addHover(purchaseOrderTab);
    }//GEN-LAST:event_purchaseOrderTabMouseEntered

    private void purchaseOrderTabMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_purchaseOrderTabMouseExited
        deleteHover(purchaseOrderTab);
    }//GEN-LAST:event_purchaseOrderTabMouseExited


    //MOUSE CLICK CỦA TAB PHIẾU MƯỢN :
    private void borrowSheetTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_borrowSheetTabMouseClicked
        isSelected(borrowSheetTab,borrowAlias);
    }//GEN-LAST:event_borrowSheetTabMouseClicked

    private void borrowSheetTabMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_borrowSheetTabMouseEntered
        addHover(borrowSheetTab);
    }//GEN-LAST:event_borrowSheetTabMouseEntered


    //MOUSE CLICK CỦA TAB PHIẾU PHẠT :
    private void penaltyTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_penaltyTabMouseClicked
        isSelected(penaltyTab, penaltyAlias);
    }//GEN-LAST:event_penaltyTabMouseClicked

    private void penaltyTabMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_penaltyTabMouseEntered
        addHover(penaltyTab);
    }//GEN-LAST:event_penaltyTabMouseEntered

    private void penaltyTabMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_penaltyTabMouseExited
        deleteHover(penaltyTab);
    }//GEN-LAST:event_penaltyTabMouseExited

    private void borrowSheetTabMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_borrowSheetTabMouseExited
        deleteHover(borrowSheetTab);
    }//GEN-LAST:event_borrowSheetTabMouseExited


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bookTab;
    private javax.swing.JPanel borrowSheetTab;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel lblBookTab;
    private javax.swing.JLabel lblBorrowTab;
    private javax.swing.JLabel lblOverviewTab;
    private javax.swing.JLabel lblPenaltyTab;
    private javax.swing.JLabel lblPurchaseOrderTab;
    private javax.swing.JPanel overviewTab;
    private javax.swing.JPanel penaltyTab;
    private javax.swing.JPanel purchaseOrderTab;
    // End of variables declaration//GEN-END:variables
}
