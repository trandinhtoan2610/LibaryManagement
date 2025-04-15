package GUI.Component.Dialog;

import BUS.AuthorBUS;
import BUS.BookBUS;
import DTO.AuthorDTO;
import DTO.Book;
import GUI.Component.Dialog.AlertDialog;
import GUI.Component.Panel.AuthorPanel;

import javax.swing.*;
import java.awt.*;

public class DeleteBookDialog extends JDialog {
    private final BookBUS bookBUS;
    private final Long bookId; // ID của sách cần xóa
    private AuthorBUS authorBUS;
    private JFrame parent;

    public DeleteBookDialog(JFrame parent, Long bookId) {
        super(parent, "Xóa Sách", true);
        this.parent = parent;
        this.bookBUS = new BookBUS();
        this.bookId = bookId;
        authorBUS = new AuthorBUS();
        initComponent();
        pack();
        setLocationRelativeTo(parent);
    }

    public void initComponent() {
        getContentPane().setLayout(new BorderLayout(10, 10));
        JPanel titlePanel = setTitle();
        getContentPane().add(titlePanel, BorderLayout.NORTH);

        JPanel contentPanel = setContentPanel();
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        JPanel bottomPanel = setBottomPanel();
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel setTitle() {
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
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel messageLabel = new JLabel("Bạn có chắc chắn muốn xóa cuốn sách với ID: " + bookId + "?");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(messageLabel, BorderLayout.CENTER);
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

        JButton deleteButton = new JButton("Đồng ý");
        deleteButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        deleteButton.setPreferredSize(new Dimension(180, 40));
        deleteButton.setBackground(new Color(0, 120, 215));
        deleteButton.setForeground(Color.WHITE);
        deleteButton.addActionListener(e -> deleteBook());

        bottomPanel.add(cancelButton);
        bottomPanel.add(deleteButton);
        return bottomPanel;
    }


    private void deleteBook() {
        Book bookDelete = bookBUS.getBookById(bookId);

        if (bookId == null || bookDelete == null) {
            new AlertDialog(parent, "ID sách không hợp lệ hoặc sách không tồn tại!").setVisible(true);
            return;
        }

        boolean isSuccess = true;

        try {
            // Cập nhật lại số lượng tác phẩm của tác giả
            AuthorDTO bookAuthor = authorBUS.getAuthorById(bookDelete.getAuthorId());
            bookAuthor.setProductQuantity(bookAuthor.getProductQuantity() - 1);
            authorBUS.updateAuthor(bookAuthor);
            bookBUS.deleteBook(bookId);

            dispose();
            new AlertDialog(parent, "Xóa sách với ID: " + bookId + " thành công!").setVisible(true);

        } catch (Exception e) {

            isSuccess = false;

            try {
                // Rollback số lượng
                AuthorDTO bookAuthor = authorBUS.getAuthorById(bookDelete.getAuthorId());
                bookAuthor.setProductQuantity(bookAuthor.getProductQuantity() + 1);
                authorBUS.updateAuthor(bookAuthor);

                // Rollback sách
                bookBUS.addBook(bookDelete);
                AuthorPanel.tblAuthor.resetTable();

            } catch (Exception rollbackEx) {
                System.err.println("Rollback thất bại: " + rollbackEx.getMessage());
            }

            new AlertDialog(parent, "Lỗi khi xóa sách: " + e.getMessage()).setVisible(true);
        }

        if (isSuccess) {
            authorBUS.deleteUnusedAuthor();
            AuthorPanel.tblAuthor.resetTable();
        }
    }


}