package GUI.Component.Panel.Components;

import GUI.Component.Dialog.LogOutDialog;
import GUI.Component.Panel.EmployeeRightPanel;
import GUI.Component.Panel.ReaderPanel;
import GUI.Component.TextField.CustomTextField;
import GUI.LoginForm;
import org.apache.batik.swing.JSVGCanvas;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class ItemSideBarLabel extends JPanel {
    private JLayeredPane layeredPane;
    private JFrame parentFrame;
    private ReaderPanel readerPanel;
    private EmployeeRightPanel employeeRightPanel;
    private String st[][] = {
            {"Trang chủ", "/icons/homepage.svg"},
            {"Sách", "/icons/book.svg"},
            {"Độc giả", "/icons/reader.svg"},
            {"Tác giả", "/icons/author.svg"},
            {"Nhà xuất bản", "/icons/publisher.svg"},
            {"Nhân viên", "/icons/employee.svg"},
            {"Phiếu mượn", "/icons/borrow.svg"},
            {"Phiếu nhập", "/icons/enter.svg"},
            {"Nhà cung cấp", "/icons/supplier.svg"},
            {"Thống kê", "/icons/statistics.svg"},
            {"Đăng xuất", "/icons/signout.svg"}
    };

    public ItemSideBarLabel(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));
        this.setBackground(new Color(240, 240, 240));
        this.setPreferredSize(new Dimension(250, 680));
        for (String item[] : st) {
            JPanel panel = createItem(item[0], item[1]);
            panel.setBorder(new LineBorder(Color.BLACK));
            panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
            add(panel);
            add(Box.createRigidArea(new Dimension(0, 5)));
        }
    }

    private JPanel createItem(String text, String iconPath) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new RoundBorder(10, Color.BLACK), // Border bo tròn
                BorderFactory.createEmptyBorder(5, 15, 2, 15)  // Padding
        ));
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(225, 240, 255));
                panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(Color.WHITE);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                panel.setBackground(new Color(239, 88, 88));
                System.out.println("Clicked: " + text);
                switch (text){
                    case "Trang chủ":{}
                    break;
                    case "Sách":{}
                    break;
                    case "Độc giả":{
                        readerPanel = new ReaderPanel();
                        readerPanel.setVisible(true);

                        break;
                    }
                    case "Tác giả": {
                        break;
                    }

                    case "Nhà xuất bản": {}
                    break;
                    case "Nhân viên":{
                         employeeRightPanel = new EmployeeRightPanel(parentFrame);
                         employeeRightPanel.setVisible(true);
                    }
                    break;
                    case "Phiếu mượn":{}
                    break;
                    case "Phiếu nhập":{}
                    break;
                    case "Nhà cung cấp":{}
                    break;
                    case "Thống kê":{}
                    break;
                    case "Đăng xuất":{
                        LogOutDialog logOutDialog = new LogOutDialog(parentFrame);
                        logOutDialog.setVisible(true);

                        if(logOutDialog.isConfirmed()){
                            parentFrame.setVisible(false);
                            new LoginForm();
                        }else{
                            logOutDialog.dispose();
                        }
                        break;
                    }

                }
            }
        });
        URL url = getClass().getResource(iconPath);
        if (url == null) {
            throw new RuntimeException("Icon not found: " + iconPath);
        }

        JSVGCanvas svgCanvas = new JSVGCanvas();
        svgCanvas.setURI(url.toString());
        svgCanvas.setPreferredSize(new Dimension(30, 30));

        panel.add(svgCanvas);

        JLabel label = new JLabel(text);
        label.setFont(new Font("Verdana", Font.PLAIN, 20));
        panel.add(label);

        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.BLACK),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return panel;
    }
    private static class RoundBorder extends AbstractBorder {
        private int radius;
        private Color color;

        public RoundBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width-1, height-1, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius/2, radius/2, radius/2, radius/2);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.right = radius/2;
            insets.top = insets.bottom = radius/2;
            return insets;
        }
    }
}