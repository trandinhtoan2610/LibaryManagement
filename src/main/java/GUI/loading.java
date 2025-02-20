package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.util.Objects;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class loading extends JFrame {

    private JPanel contentPane;


    /**
     * Create the frame.
     */
    public loading() {
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 659, 465);
        setBackground(Color.cyan);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("");
        lblNewLabel
                .setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/images/unnamed.gif"))));
        lblNewLabel.setBounds(230, 73, 294, 272);
        contentPane.add(lblNewLabel);
        contentPane.setBackground(new Color(0, 0, 0, 0));
        setLocationRelativeTo(null);
        setBackground(new Color(0, 0, 0, 0));
    }

}
