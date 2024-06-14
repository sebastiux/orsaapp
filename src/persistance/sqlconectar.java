
package persistance;
import java.sql.Connection;
import java.sql.DriverManager;


public class sqlconectar {
        Connection conexion;
        
        
    public Connection sqlconectando(){
        String host = "";
        String port = "";
        String namedb = "ORSA";
        String usuario = "";
        String key = "";
        //Driver
        
        String driver = "com.mysql.cj.jdbc.Driver";
        
        // URL
        String url = "jdbc:mysql:///ORSA";
        
        
        try {
            Class.forName(driver);
            conexion = DriverManager.getConnection(url,usuario,key);
            System.out.println("Base de datos conectada");
        } catch (Exception e) {
            
        }
        return conexion;
    }
}
    
   