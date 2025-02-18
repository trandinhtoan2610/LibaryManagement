package GUI;
import DAL.DatabaseConnection;
import DAL.EmployeeDAL;
import DTO.Employee;

import javax.swing.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;




public class Main extends Thread{

    public static void main(String[] args) {

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException _) {

        }
       LoginForm login = new LoginForm();

//       Login();
    }

    public static void Sleep(long j) {
        try {
            Thread.sleep(j);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
//    public static void Login() {
//        System.out.println("Hello word");
//        while(!LoginForm.isLogin) {
//            Sleep(1000);
//        }
//        if(LoginForm.isLogin) {
//            loading load = new loading();
//            load.setVisible(true);
//            Sleep(2000);
//            load.setVisible(false);
//            load.dispose();
//        }
//    }
}