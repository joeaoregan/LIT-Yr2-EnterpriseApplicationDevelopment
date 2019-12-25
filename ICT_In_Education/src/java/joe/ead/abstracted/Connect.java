/**
 *
 * @author joeaooregan
 */
package joe.ead.abstracted;

import java.net.URISyntaxException;
//import java.sql.Connection;
import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
public class Connect {
    // public static final String url = "jdbc:mysql://localhost:3306/";
    // public static final String url = "jdbc:mysql://12.0.0.1:3306/";
    // public static final String dbName = "JoeCA";
    // public static final String userName = "root";
    // public static final String password = "admin";
    // public static final String databaseURL="jdbc:mysql://<host>:<port>/<database>?user=<username>&password=<password>;

    public static Connection getConnection() throws URISyntaxException, SQLException {
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        //return (Connection) DriverManager.getConnection (Connect.url+Connect.dbName,Connect.userName,Connect.password);
        return (Connection) DriverManager.getConnection(dbUrl);
    }
}
