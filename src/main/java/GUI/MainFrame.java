package GUI;

import GUI.Component.Dialog.LogOutDialog;
import GUI.Component.Panel.*;
import GUI.Component.Panel.Components.SidebarListener;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame implements SidebarListener {
    private TaskBarPanel taskBarPanel;
    private PublisherPanel publisherPanel;
    private EmployeePanel employeePanel;
    private BorrowPanel borrowPanel;
    private PenaltyPanel penaltyPanel;
    private BookPanel bookPanel;
    private ReaderPanel readerPanel;
    private AuthorPanel authorPanel;
    private JPanel mainContentPanel;
    private CardLayout cardLayout;

    public MainFrame() {
        setupUI();
    }

    private void setupUI() {
        setTitle("Quản lý thư viện");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1600, 800);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        initPanels();

        taskBarPanel = new TaskBarPanel(this, this);
        taskBarPanel.setPreferredSize(new Dimension(252, getHeight()));
        add(taskBarPanel, BorderLayout.WEST);

        mainContentPanel = new JPanel();
        cardLayout = new CardLayout();
        mainContentPanel.setLayout(cardLayout);

        mainContentPanel.add(new JPanel(), "Trang chủ");
        mainContentPanel.add(bookPanel, "Sách");
        mainContentPanel.add(readerPanel, "Độc giả");
        mainContentPanel.add(authorPanel, "Tác giả");
        mainContentPanel.add(publisherPanel, "Nhà xuất bản");
        mainContentPanel.add(employeePanel, "Nhân viên");
        mainContentPanel.add(borrowPanel, "Phiếu mượn");
        mainContentPanel.add(new JPanel(), "Phiếu nhập");
        mainContentPanel.add(new JPanel(), "Nhà cung cấp");
        mainContentPanel.add(penaltyPanel, "Thống kê");

        add(mainContentPanel, BorderLayout.CENTER);
    }

    private void initPanels() {
        readerPanel = new ReaderPanel();
        employeePanel = new EmployeePanel(this);
        authorPanel = new AuthorPanel();
        bookPanel = new BookPanel(this);
        penaltyPanel = new PenaltyPanel(this);
        borrowPanel = new BorrowPanel(this);
        publisherPanel = new PublisherPanel(this);
    }

    @Override
    public void sideBarItemClicked(String itemName) {
        System.out.println("Clicked " + itemName);

        switch (itemName) {
            case "Trang chủ":
                cardLayout.show(mainContentPanel, "Trang chủ");
                break;
            case "Sách":
                cardLayout.show(mainContentPanel, "Sách");
                break;
            case "Độc giả":
                cardLayout.show(mainContentPanel, "Độc giả");
                break;
            case "Tác giả":
                cardLayout.show(mainContentPanel, "Tác giả");
                break;
            case "Nhà xuất bản":
                cardLayout.show(mainContentPanel, "Nhà xuất bản");
                break;
            case "Nhân viên":
                cardLayout.show(mainContentPanel, "Nhân viên");
                break;
            case "Phiếu mượn":
                cardLayout.show(mainContentPanel, "Phiếu mượn");
                break;
            case "Phiếu nhập":
                cardLayout.show(mainContentPanel, "Phiếu nhập");
                break;
            case "Nhà cung cấp":
                cardLayout.show(mainContentPanel, "Nhà cung cấp");
                break;
            case "Thống kê":
                cardLayout.show(mainContentPanel, "Thống kê");
                break;
            case "Đăng xuất":
                LogOutDialog logOutDialog = new LogOutDialog(this);
                logOutDialog.setVisible(true);

                if (logOutDialog.isConfirmed()) {
                    this.dispose();
                    new LoginForm().setVisible(true);
                }
                break;
        }
    }
}