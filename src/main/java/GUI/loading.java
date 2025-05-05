package GUI;

import java.awt.*;
import java.io.IOException; // Cần thiết cho openStream
import java.net.URL;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class loading extends JFrame {
    private JPanel contentPane;
    private JLabel animationLabel; // Giữ tham chiếu để có thể kiểm tra

    public loading() {
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // --- THAY ĐỔI 1: Đặt kích thước JFrame bằng kích thước GIF ---
        setSize(500, 500); // Kích thước mới 500x500

        contentPane = new JPanel();
        // Bỏ EmptyBorder nếu không cần khoảng trống nào bên trong cửa sổ
        // contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBorder(null); // Hoặc setBorder(null)
        contentPane.setLayout(null); // Sử dụng null layout
        contentPane.setOpaque(false); // Làm cho JPanel trong suốt
        setContentPane(contentPane);

        animationLabel = new JLabel(); // Không cần setIcon ban đầu

        // --- THAY ĐỔI 2: Đặt kích thước và vị trí JLabel để vừa với GIF và JFrame ---
        // Đặt vị trí (0, 0) và kích thước (500, 500)
        animationLabel.setBounds(0, 0, 500, 500);
        contentPane.add(animationLabel);

        // --- Cách tải GIF đơn giản (giữ nguyên logic) ---
        try {
            // Đường dẫn tới file GIF trong classpath
            // *** QUAN TRỌNG: Đảm bảo tên file GIF này đúng là file 500x500 của bạn ***
            String gifPath = "/images/bar-penguin (1).gif";
            URL gifUrl = getClass().getResource(gifPath);

            if (gifUrl != null) {
                ImageIcon gifIcon = new ImageIcon(gifUrl);
                if (gifIcon.getIconWidth() > 0) {
                    animationLabel.setIcon(gifIcon);
                } else {
                    System.err.println("ImageIcon được tạo nhưng không hợp lệ (width=0). Kiểm tra file GIF.");
                    showError("Lỗi: File GIF không hợp lệ.");
                }
            } else {
                System.err.println("Không tìm thấy file GIF tại đường dẫn: " + gifPath);
                showError("Lỗi: Không tìm thấy file GIF!");
            }
        } catch (Exception e) {
            System.err.println("Lỗi không mong muốn khi tải GIF: " + e.getMessage());
            e.printStackTrace();
            showError("Lỗi khi tải GIF!");
        }

        setLocationRelativeTo(null); // Căn giữa màn hình
        setBackground(new Color(0, 0, 0, 0)); // Làm cho nền của JFrame trong suốt
    }

    private void showError(String message) {
        animationLabel.setBounds(0, 0, 500, 500); // Giữ nguyên kích thước label
        animationLabel.setIcon(null);
        animationLabel.setText("<html><div style='text-align: center;'>" + message + "</div></html>"); // Cho phép xuống dòng nếu text dài
        animationLabel.setForeground(Color.RED);
        animationLabel.setFont(new Font("Segoe UI", Font.BOLD, 16)); // Có thể tăng cỡ chữ lỗi
        animationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        animationLabel.setVerticalAlignment(SwingConstants.CENTER); // Căn giữa theo chiều dọc
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}