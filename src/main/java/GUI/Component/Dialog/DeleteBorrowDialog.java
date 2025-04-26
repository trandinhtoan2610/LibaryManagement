package GUI.Component.Dialog;

import BUS.*;
import DTO.*;
import DTO.Enum.Status;
import GUI.Component.Button.ButtonAction;
import GUI.Component.Panel.BookPanel;
import GUI.Component.Panel.BorrowPanel;
import GUI.Component.Panel.EmployeePanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static GUI.Component.Panel.BookPanel.bookTable;

public class DeleteBorrowDialog extends JDialog {
    private final BookBUS bookBUS = new BookBUS();
    private final BorrowDetailBUS borrowDetailBUS = new BorrowDetailBUS();
    private boolean confirmed = false;
    private BorrowPanel borrowPanel;
    private BorrowDTO borrowToDelete;
    private BorrowSheetBUS borrowSheetBUS;
    private PenaltyBUS penaltyBUS;
    private PenaltyDetailsBUS penaltyDetailsBUS;
    public DeleteBorrowDialog(JFrame parent, BorrowPanel borrowPanel, BorrowDTO borrowToDelete) {
        super(parent, "Xóa Phiếu Mượn", true);
        this.borrowPanel = borrowPanel;
        borrowSheetBUS = new BorrowSheetBUS();
        this.borrowToDelete = borrowToDelete;
        penaltyBUS = new PenaltyBUS();
        penaltyDetailsBUS = new PenaltyDetailsBUS();
        initComponents();
        pack();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setLayout(new BorderLayout(20, 10));
        setSize(400, 200);
        setResizable(false);

        // Panel tiêu đề
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(0, 120, 215));
        JLabel titleLabel = new JLabel("XÁC NHẬN XÓA");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Panel nội dung
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JLabel messageLabel = new JLabel("Bạn có chắc chắn muốn xóa phiếu mượn này?");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(messageLabel, BorderLayout.CENTER);

        // Panel nút bấm
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));

        // Nút Đồng ý (màu xanh)
        ButtonAction yesButton = new ButtonAction("Đồng ý",
                new Color(0, 120, 215),  // normal
                new Color(0, 100, 190),   // hover
                new Color(0, 80, 170),    // press
                5);
        yesButton.addActionListener(e -> {
            try {
                if (borrowToDelete.getStatus() == Status.Đang_Mượn) {
                    List<BorrowDetailDTO> details = borrowDetailBUS.getBorrowDetailsBySheetId(
                            Long.parseLong(borrowToDelete.getId().substring(2))
                    );
                    for (BorrowDetailDTO detail : details) {
                        Book book = bookBUS.getBookById(detail.getBookId());
                        book.setBorrowedQuantity(book.getBorrowedQuantity() - detail.getQuantity());
                        bookBUS.updateBook(book);
                        borrowDetailBUS.deleteBorrowDetail(detail);
                    }
                }

                //Xóa phiếu phạt :
                String penaltyID = penaltyDetailsBUS.getPenaltyIDByBorrowSheet(borrowToDelete);
                PenaltyDTO penaltySheet = penaltyBUS.findPenaltySheetByID(penaltyID);
                if(penaltySheet != null ){
                    try{
                        penaltyDetailsBUS.deletePenaltyDetailsByPenaltyID(penaltySheet.getId());
                        penaltyBUS.deletePenaltySheet(penaltySheet);
                    }catch (Exception ex){
                        ex.printStackTrace();

                    }
                }
                //
                boolean success = borrowSheetBUS.deleteBorrowSheet(
                        Long.parseLong(borrowToDelete.getId().substring(2))
                );
                if (success) {
                    confirmed = true;
                    borrowPanel.refreshTable();
                    BookPanel.loadData();
                    dispose();
                    new AlertDialog(this, "Xóa phiếu mượn thành công !").setVisible(true);

                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    //ROLLBACK PHIẾU PHẠT :
                    penaltyBUS.addPenaltySheet(penaltySheet);
                    penaltyDetailsBUS.addPenaltyDetails(penaltySheet.getId(), borrowToDelete);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Nút Hủy bỏ (màu đỏ)
        ButtonAction noButton = new ButtonAction("Hủy bỏ",
                new Color(220, 80, 80),   // normal
                new Color(200, 60, 60),   // hover
                new Color(180, 40, 40),   // press
                5);
        noButton.addActionListener(e -> dispose());

        buttonPanel.add(noButton);  // Để nút Hủy bỏ trước
        buttonPanel.add(yesButton); // Nút Đồng ý sau

        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(contentPanel, BorderLayout.CENTER);
    }
    public boolean isConfirmed() {
        return confirmed;
    }
}
