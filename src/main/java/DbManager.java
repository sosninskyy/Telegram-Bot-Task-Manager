import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbManager {

    public static Connection getConnection() {
        Connection connection = null;
        try {
            String DataBasePostgreSQLUrl = "";
            String user = "";
            String password = "";
            connection = DriverManager.getConnection(DataBasePostgreSQLUrl, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
