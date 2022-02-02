import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import oracle.jdbc.driver.OracleDriver;

public class Main {

	public static void main(String[] args) {
    try {
      DriverManager.registerDriver(new OracleDriver());
      String url = "jdbc:oracle:thin:@oracle1.ensimag.fr:1521:oracle1";
      String user = "akdimy";
      String pass = "akdimy";
      Connection connection = DriverManager.getConnection(url, user, pass);
			new Home(connection);
    }catch (SQLException e) {
      e.printStackTrace ();
    }
  }
}
