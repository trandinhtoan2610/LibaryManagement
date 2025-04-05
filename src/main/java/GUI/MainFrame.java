package GUI;

import DTO.Book;
import DTO.BorrowDTO;
import DTO.BorrowDetailDTO;
import DTO.Employee;
import DTO.Enum.Status;
import DTO.Enum.SubStatus;
import GUI.Component.Dialog.LogOutDialog;
import GUI.Component.Panel.*;
import GUI.Component.Panel.Components.SidebarListener;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

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
    private HomePagePanel homePagePanel;
    private SupplierPanel supplierPanel;
    private PurchaseOrderPanel purchaseOrderPanel;
    private Employee currentEmployee;

    public MainFrame(Employee currentEmployee) {
        setupUI(currentEmployee);
    }

    private void setupUI(Employee currentEmployee) {
        this.currentEmployee = currentEmployee;
        setTitle("Quản lý thư viện");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1600, 800);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        initPanels();

        taskBarPanel = new TaskBarPanel(this, this, currentEmployee);
        taskBarPanel.setPreferredSize(new Dimension(252, getHeight()));
        add(taskBarPanel, BorderLayout.WEST);

        mainContentPanel = new JPanel();
        cardLayout = new CardLayout();
        mainContentPanel.setLayout(cardLayout);

        taskBarPanel.setSelectedItem("Trang chủ");

        mainContentPanel.add(homePagePanel, "Trang chủ");
        mainContentPanel.add(bookPanel, "Sách");
        mainContentPanel.add(readerPanel, "Độc giả");
        mainContentPanel.add(authorPanel, "Tác giả");
        mainContentPanel.add(publisherPanel, "Nhà xuất bản");
        mainContentPanel.add(employeePanel, "Nhân viên");
        mainContentPanel.add(borrowPanel, "Phiếu mượn");
        mainContentPanel.add(purchaseOrderPanel, "Phiếu nhập");
        mainContentPanel.add(supplierPanel, "Nhà cung cấp");
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
        homePagePanel = new HomePagePanel();
        supplierPanel = new SupplierPanel();
        purchaseOrderPanel = new PurchaseOrderPanel(this);
    }

    @Override
    public void sideBarItemClicked(String itemName) {
        System.out.println("Clicked " + itemName);
        taskBarPanel.setSelectedItem(itemName);

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