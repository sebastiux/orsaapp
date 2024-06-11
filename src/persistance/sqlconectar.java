
package persistance;
import java.sql.Connection;
import java.sql.DriverManager;


public class sqlconectar {
        Connection conexion;
        String host = "servermysqlorsa.mysql.database.azure.com";
        String port = "3306";
        String namedb = "ORSA";
        String usuario = "orsamysql";
        String key = "sA*2002$";
        //Driver
        
        String driver = "com.mysql.cj.jdbc.Driver";
        
        // URL
        String url = "jdbc:mysql://servermysqlorsa.mysql.database.azure.com:3306/ORSA";
        
    public Connection sqlconectando(){
        
        
        
        try {
            Class.forName(driver);
            conexion = DriverManager.getConnection(url,usuario,key);
            System.out.println("Base de datos conectada");
        } catch (Exception e) {
            
        }
        return conexion;
    }
}
    
   