package GUI.Component;

import javax.swing.*;
import java.awt.*;

public class NavBarJPanel extends JPanel {
    private JPanel leftPanel;
    private JPanel rightPanel;

    private JLabel add;
    private JLabel edit;
    private JLabel remove;
    private JLabel details;

    String listCategory[] = new String[]{"Tất cả", "Trinh Thám", "Kinh dị", "Ma thuật"};
    private JComboBox<String> category;
    private JTextField searchField;
    private JButton refresh;

    public NavBarJPanel() {
        initComponents();
    }

    private void initComponents() {
        this.setPreferredSize(new Dimension(1000, 100));
        this.setLayout(new BorderLayout());
        this.setBackground(Color.WHITE);
        setLeftPanel();
        setRightPanel();
    }

    private void setLeftPanel() {
        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.X_AXIS));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(0,15,0,0));
        add = createIconLabel("Hinh/Add.png", "Thêm");
        leftPanel.add(add);

        leftPanel.add(Box.createHorizontalStrut(-4));

        edit = createIconLabel("Hinh/Edit.png", "Cập Nhật");
        leftPanel.add(edit);

        leftPanel.add(Box.createHorizontalStrut(-2));

        remove = createIconLabel("Hinh/Delete.png", "Xóa");
        leftPanel.add(remove);

        leftPanel.add(Box.createHorizontalStrut(-2));

        details = createIconLabel("Hinh/List.png", "Chi Tiết");
        leftPanel.add(details);


        this.add(leftPanel, BorderLayout.WEST);
    }

    private void setRightPanel() {
        rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(34, 0, 0, 0));

        // Tạo một panel phụ để chứa các thành phần
        JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        contentPanel.setBackground(Color.WHITE);

        // Thêm các thành phần vào panel phụ
        category = new JComboBox<>(listCategory);
        contentPanel.add(category);

        contentPanel.add(new JLabel("Search:"));
        searchField = new JTextField(15);
        contentPanel.add(searchField);

        refresh = new JButton("Refresh");
        contentPanel.add(refresh);

        rightPanel.add(contentPanel, BorderLayout.CENTER);

        this.add(rightPanel, BorderLayout.EAST);
    }

    private JLabel createIconLabel(String imagePath, String fallbackText) {
        JLabel label = new JLabel();
        try {
            ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource(imagePath));
            Image image = icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
            icon = new ImageIcon(image);
            label.setIcon(icon);
            label.setText("<html>" + fallbackText + "</html>");
        } catch (Exception e) {
            label.setText(fallbackText);
        }
        label.setHorizontalTextPosition(SwingConstants.CENTER);
        label.setVerticalTextPosition(SwingConstants.BOTTOM);
        label.setPreferredSize(new Dimension(100, 50));
        return label;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new NavBarJPanel());
        frame.pack();
        frame.setVisible(true);
    }
}