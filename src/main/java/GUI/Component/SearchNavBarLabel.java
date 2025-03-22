package GUI.Component;

import com.kitfox.svg.app.beans.SVGIcon;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.URL;

public class SearchNavBarLabel extends Panel {
    public SearchNavBarLabel(){
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        String type[] = {"Tên", "Thể Loại", "Tác Giả", "Năm"};
        JComboBox searchtype = new JComboBox(type);
        searchtype.setSelectedIndex(0);
        searchtype.setEditable(false);
        searchtype.setPreferredSize(new Dimension(100, 30));
        searchtype.setBackground(Color.WHITE);
        searchtype.setForeground(new Color(50, 50, 50));
        add(searchtype);

        JTextField searchfield = new JTextField();
        searchfield.setPreferredSize(new Dimension(200, 30));
        searchfield.setBackground(new Color(66, 135, 245));
        add(searchfield);

        URL refreshurl = getClass().getResource("/icons/refresh.svg");
        if(refreshurl == null) {
            throw new RuntimeException("Refresh Icon Not Found");
        }
        SVGIcon refreshIcon = new SVGIcon();
        refreshIcon.setSvgURI(URI.create(refreshurl.toString()));
        refreshIcon.setPreferredSize(new Dimension(20,30));
        JButton refreshButton = new JButton("Làm mới", refreshIcon);
        refreshButton.setPreferredSize(new Dimension(130, 30));

        add(refreshButton);
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("SearchNavBarLabel Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SearchNavBarLabel searchNavBar = new SearchNavBarLabel();
        frame.add(searchNavBar);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
