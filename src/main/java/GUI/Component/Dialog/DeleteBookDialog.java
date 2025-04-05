package GUI.Component.Dialog;

import BUS.BookBUS;

import javax.swing.*;
import java.awt.*;

public class DeleteBookDialog extends JDialog {
    private final BookBUS bookBUS;

    public DeleteBookDialog(JFrame parent) {
        super(parent, "Xóa Sách", true);
        this.bookBUS = new BookBUS();
        initComponent();
        pack();
        setLocationRelativeTo(parent);
    }

    public void initComponent(){
        getContentPane().setLayout(new BorderLayout(10, 10));
        JPanel titlePanel = settitle();
        getContentPane().add(titlePanel, BorderLayout.NORTH);

        JPanel contentPanel = setContentPanel();
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        JPanel bottomPanel = setBottomPanel();
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel settitle() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JLabel txt = new JLabel("Xóa Sách");
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 36));
        txt.setHorizontalAlignment(SwingConstants.CENTER);
        txt.setVerticalAlignment(SwingConstants.CENTER);
        txt.setForeground(Color.WHITE);
        panel.add(txt, BorderLayout.CENTER);
        panel.setForeground(Color.WHITE);
        panel.setBackground(new Color(0, 120, 215));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(2, 7, 2, 7),
                BorderFactory.createLineBorder(new Color(200, 200, 200), 2)
        ));
        return panel;
    }

    private JPanel setContentPanel() {
        JPanel panel = new JPanel();
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
//        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));20, 5, 20;));

        JLabel messageLabel = new JLabel("Bạn có chắc chắn muôốn xóa cuốn sách này");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(messageLabel,BorderLayout.CENTER);
        return panel;
    }

    private JPanel setBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        JButton cancelButton = new JButton("Hủy bỏ");
        cancelButton.setBackground(new Color(218, 42, 0));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        cancelButton.setPreferredSize(new Dimension(120, 40));
        cancelButton.addActionListener(e -> dispose());

        JButton deleteButton = new JButton("Dồng ý");
        deleteButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        deleteButton.setPreferredSize(new Dimension(180, 40));
        deleteButton.setBackground(new Color(0, 120, 215));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.addActionListener(e -> deleteBook());

        bottomPanel.add(cancelButton);
        bottomPanel.add(deleteButton);
        return bottomPanel;
    }
//viết hàm để khi click vào row trong table thì trả vee id
    private void deleteBook() {
//        bookBUS.deleteBook();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        DeleteBookDialog dialog = new DeleteBookDialog(frame);
        dialog.setVisible(true);

//        if (dialog.isConfirmed()) {
//            System.out.println("Người dùng đã đồng ý xóa");
//        } else {
//            System.out.println("Người dùng đã hủy bỏ");
//        }
    }

}
