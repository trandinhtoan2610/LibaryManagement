package GUI;


import DTO.Book;
import DTO.Employee;
import raven.datetime.DatePicker;

import javax.swing.*;
import java.awt.*;


public class Main extends Thread {

    public static void main(String[] args) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException _) {
        }
        new LoginForm();
        DatePicker datePicker = new DatePicker();
        datePicker.setAnimationEnabled(true);
        datePicker.setEditor(new JFormattedTextField());
        datePicker.setColor(Color.WHITE);
        datePicker.setBorder(null);
        datePicker.setBackground(new Color(0, 0, 0, 0));
        datePicker.setCloseAfterSelected(true); 
    }
    public static void Sleep(long j) {
        try {
            Thread.sleep(j);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}