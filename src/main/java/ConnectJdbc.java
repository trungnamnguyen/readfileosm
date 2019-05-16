import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectJdbc {
    public Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/giasusinhvien?useSSL=false";
        String user = "giasusinhvien";
        String password = "Ntn@123456";
        return DriverManager.getConnection(url,user,password);
    }
}
