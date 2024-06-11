/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistance;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;
/**
 *
 * @author Carlos
 */
public class ConexionPool {
     private static final int INITIAL_POOL_SIZE = 10;
    private static final String URL = "jdbc:mysql://servermysqlorsa.mysql.database.azure.com:3306/ORSA";
    private static final String USER = "orsamysql";
    private static final String PASSWORD = "sA*2002$";

    private static Queue<Connection> pool = new LinkedList<>();

    static {
        try {
            for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
                pool.add(createNewConnection());
            }
        } catch (SQLException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private static Connection createNewConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static synchronized Connection getConnection() throws SQLException {
        if (pool.isEmpty()) {
            return createNewConnection();
        } else {
            return pool.poll();
        }
    }

    public static synchronized void releaseConnection(Connection connection) {
        pool.add(connection);
    }
}

