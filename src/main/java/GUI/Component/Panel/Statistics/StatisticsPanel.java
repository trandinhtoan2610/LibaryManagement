package GUI.Component.Panel.Statistics;

import java.awt.*;
import javax.swing.*;

public class StatisticsPanel extends javax.swing.JPanel {
    private final Color defaultColor = new Color(255, 255, 255);
    private final Color selectedColor = new Color(100, 200, 250);
    private final Color hoverColor = new Color(180, 220, 250);
    private final Font defaultFont = new Font("Segoe UI", Font.PLAIN, 12);
    private final Font selectedFont = new Font("Segoe UI", Font.BOLD, 15);
    private JPanel selectedPanelTab;

    // Panels cho các tab
    private BookStatisticsPanel bookStatisticsPanel;
    private PurchaseStatistics purchasePanel;
    private BorrowStatistics borrowPanel;
    private PenaltyStatistics penaltyPanel;

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private final String bookAlias = "BOOK";
    private final String purchaseAlias = "PURCHASE";
    private final String borrowAlias = "BORROW";
    private final String penaltyAlias = "PENALTY";

    public StatisticsPanel() {
        initComponents();
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        contentPanel.add(cardPanel);

        // Khởi tạo các panel
        bookStatisticsPanel = new BookStatisticsPanel();
        purchasePanel = new PurchaseStatistics();
        borrowPanel = new BorrowStatistics();
        penaltyPanel = new PenaltyStatistics();

        // Thiết lập màu nền tạm thời cho các panel
     
        purchasePanel.setBackground(Color.WHITE);
        borrowPanel.setBackground(Color.WHITE);
        penaltyPanel.setBackground(Color.WHITE);

        // Thêm các panel vào cardPanel
  
        cardPanel.add(bookStatisticsPanel, bookAlias);
        cardPanel.add(purchasePanel, purchaseAlias);
        cardPanel.add(borrowPanel, borrowAlias);
        cardPanel.add(penaltyPanel, penaltyAlias);

        // Mặc định hiển thị tab Tổng quan
        isSelected(bookTab, bookAlias);
    }

    public void clearSelect() {
        if (selectedPanelTab != null) {
            selectedPanelTab.setBackground(defaultColor);
            if (selectedPanelTab == penaltyTab)
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
        cardLayout.show(cardPanel, header);
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
            p.setOpaque(true);
            p.revalidate();
            p.repaint();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {
        headerPanel = new javax.swing.JPanel();
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
        headerPanel.setLayout(new java.awt.GridLayout(1, 4));

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
    }
    // </editor-fold>


    private void bookTabMouseClicked(java.awt.event.MouseEvent evt) {
        isSelected(bookTab, bookAlias);
    }

    private void bookTabMouseEntered(java.awt.event.MouseEvent evt) {
        addHover(bookTab);
    }

    private void bookTabMouseExited(java.awt.event.MouseEvent evt) {
        deleteHover(bookTab);
    }

    private void purchaseOrderTabMouseClicked(java.awt.event.MouseEvent evt) {
        isSelected(purchaseOrderTab, purchaseAlias);
    }

    private void purchaseOrderTabMouseEntered(java.awt.event.MouseEvent evt) {
        addHover(purchaseOrderTab);
    }

    private void purchaseOrderTabMouseExited(java.awt.event.MouseEvent evt) {
        deleteHover(purchaseOrderTab);
    }

    private void borrowSheetTabMouseClicked(java.awt.event.MouseEvent evt) {
        isSelected(borrowSheetTab, borrowAlias);
    }

    private void borrowSheetTabMouseEntered(java.awt.event.MouseEvent evt) {
        addHover(borrowSheetTab);
    }

    private void borrowSheetTabMouseExited(java.awt.event.MouseEvent evt) {
        deleteHover(borrowSheetTab);
    }

    private void penaltyTabMouseClicked(java.awt.event.MouseEvent evt) {
        isSelected(penaltyTab, penaltyAlias);
    }

    private void penaltyTabMouseEntered(java.awt.event.MouseEvent evt) {
        addHover(penaltyTab);
    }

    private void penaltyTabMouseExited(java.awt.event.MouseEvent evt) {
        deleteHover(penaltyTab);
    }

    // Variables declaration
    private javax.swing.JPanel bookTab;
    private javax.swing.JPanel borrowSheetTab;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel lblBookTab;
    private javax.swing.JLabel lblBorrowTab;
    private javax.swing.JLabel lblOverviewTab;
    private javax.swing.JLabel lblPenaltyTab;
    private javax.swing.JLabel lblPurchaseOrderTab;
    private javax.swing.JPanel penaltyTab;
    private javax.swing.JPanel purchaseOrderTab;
}