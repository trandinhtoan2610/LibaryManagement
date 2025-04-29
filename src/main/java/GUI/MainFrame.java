package GUI;

import DTO.Employee;
import DTO.Enum.Gender;
import GUI.Component.Dialog.LogOutDialog;
import GUI.Component.Panel.*;
import GUI.Component.Panel.Components.SidebarListener;
import GUI.Component.Panel.Statistics.StatisticsPanel;
import com.formdev.flatlaf.FlatLightLaf;

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
    private HomePagePanel homePagePanel;
    private SupplierPanel supplierPanel;
    private PurchaseOrderPanel purchaseOrderPanel;
    private StatisticsPanel statisticsPanel;
    private CategoryPanel categoryPanel;
    private Employee currentEmployee;

    public MainFrame(Employee currentEmployee) {
        FlatLightLaf.setup();
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
        mainContentPanel.add(categoryPanel, "Thể loại");
        mainContentPanel.add(readerPanel, "Độc giả");
        mainContentPanel.add(authorPanel, "Tác giả");
        mainContentPanel.add(publisherPanel, "Nhà xuất bản");
        mainContentPanel.add(employeePanel, "Nhân viên");
        mainContentPanel.add(borrowPanel, "Phiếu mượn");
        mainContentPanel.add(penaltyPanel, "Phiếu phạt");
        mainContentPanel.add(purchaseOrderPanel, "Phiếu nhập");
        mainContentPanel.add(supplierPanel, "Nhà cung cấp");
        mainContentPanel.add(statisticsPanel, "Thống kê");
        add(mainContentPanel, BorderLayout.CENTER);
    }

    private void initPanels() {
        categoryPanel = new CategoryPanel(this);
        readerPanel = new ReaderPanel();
        employeePanel = new EmployeePanel(this);
        authorPanel = new AuthorPanel();
        penaltyPanel = new PenaltyPanel();
        borrowPanel = new BorrowPanel(this);
        publisherPanel = new PublisherPanel(this);
        homePagePanel = new HomePagePanel();
        supplierPanel = new SupplierPanel();
        purchaseOrderPanel = new PurchaseOrderPanel(this);
        bookPanel = new BookPanel(this);
        statisticsPanel = new StatisticsPanel();
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
            case "Thể loại":
                cardLayout.show(mainContentPanel, "Thể loại");
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
            case "Phiếu phạt":
                cardLayout.show(mainContentPanel, "Phiếu phạt");
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | UnsupportedLookAndFeelException | InstantiationException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            MainFrame mainFrame = new MainFrame(new Employee(1L, "Hoàng", "Quý", Gender.Nam, "admin", "admin", 1L, "0329997881", "90 36 35", 10L));
            mainFrame.setVisible(true);
        });
    }
}