package DAL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    
     public static Connection getConnection() throws SQLException {
        Connection c = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); 
            String url = "jdbc:mySQL://localhost:3306/library_management";
            String username = "root";
            String password = "";
            c = DriverManager.getConnection(url, username, password);
            
        }catch(ClassNotFoundException e){
            throw new RuntimeException("Không tìm th driver MySQL", e);
        }
        return c;
    }
    
    public static void closeConnection(Connection c){
        try {
            if (c != null)
                c.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
//    private static DatabaseConnection instance;
//    private Connection connection;
//    private final String url;
//    private final String user;
//    private final String password;
//
//    private DatabaseConnection() {
//        try {
//            Properties props = new Properties();
//            props.load(DatabaseConnection.class.getResourceAsStream("/database.properties"));
//            url = props.getProperty("db.url");
//            user = props.getProperty("db.user");
//            password = props.getProperty("db.password");
//            Class.forName(props.getProperty("db.driver"));
//        } catch (Exception e) {
//            throw new RuntimeException("Lỗi khởi tạo kết nối database", e);
//        }
//    }
//
//    public static synchronized DatabaseConnection getInstance() {
//        if (instance == null) {
//            instance = new DatabaseConnection();
//        }
//        return instance;
//    }
//
//    public Connection getConnection() throws SQLException {
//        if (connection == null || connection.isClosed()) {
//            connection = DriverManager.getConnection(url, user, password);
//        }
//        return connection;
//    }
}
