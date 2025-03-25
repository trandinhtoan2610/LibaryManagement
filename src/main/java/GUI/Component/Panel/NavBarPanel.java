package GUI.Component.Panel;

import javax.swing.*;
import java.awt.*;

public class NavBarPanel extends JPanel {
    public NavBarPanel() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setPreferredSize(new Dimension(1200, 100));
        setBackground(Color.WHITE);
        setForeground(new Color(240, 240, 240));
    }
}