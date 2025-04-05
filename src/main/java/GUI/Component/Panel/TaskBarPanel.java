package GUI.Component.Panel;

import DTO.Employee;
import GUI.Component.Panel.Components.AccountNameLabel;
import GUI.Component.Panel.Components.ItemSideBarLabel;
import GUI.Component.Panel.Components.SidebarListener;

import javax.swing.*;
import java.awt.*;

public class TaskBarPanel extends JPanel {
    ItemSideBarLabel itemSideBarLabel;
    AccountNameLabel accountNameLabel;
    JFrame parentFrame;
    public TaskBarPanel(JFrame parentFrame, SidebarListener listener, Employee currentEmployee) {
        String fullName = currentEmployee.getFirstName() + " " + currentEmployee.getLastName();
        Long roleId = currentEmployee.getRoleID();
        String roleName = "";
        if (roleId == 1L) {
            roleName = "Admin";
        } else if (roleId == 2L) {
            roleName = "Quản lý";
        } else if (roleId == 3L) {
            roleName = "Nhân viên";
        }
        this.parentFrame = parentFrame;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setPreferredSize(new Dimension(252, parentFrame.getHeight()));
        accountNameLabel = new AccountNameLabel(fullName, roleName);
        add(accountNameLabel);
        itemSideBarLabel = new ItemSideBarLabel(parentFrame, listener);
        this.add(itemSideBarLabel);
    }
    public void setSelectedItem(String itemName) {
        itemSideBarLabel.setSelectedItem(itemName);
    }
}
