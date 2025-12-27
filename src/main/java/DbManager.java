import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbManager {

    public static Connection getConnection() {
        Connection connection = null;
        try {
            String DataBasePostgreSQLUrl = System.getenv("DB_URL");
            String user = System.getenv("DB_USER");
            String password = System.getenv("DB_PASS");
            connection = DriverManager.getConnection(DataBasePostgreSQLUrl, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
