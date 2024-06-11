
package logica;
 import gui.Screen;
import java.sql.SQLException;
import persistance.sqlconectar;
//import 'META-INF'.persistence;

public class Demoapp {

   
    public static void main(String[] args) throws SQLException {
        Screen pantalla = new Screen();
        
        pantalla.setVisible(true);//hacer que aparezca la pantalla
        pantalla.setLocationRelativeTo(null);//poner la pantalla en medio
        
    }
    
}
