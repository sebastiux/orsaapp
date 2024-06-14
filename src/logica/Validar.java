/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;
import gui.Home;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

public class Validar {
    int confirma=0;
    int incorrecto=0;
    int error = 0;
   
    public void validaruser(String username, String password){
        try {
            ResultSet rs = null;
            PreparedStatement ps = null;//provoca que cuando pongas (?) se inserten los strings
            
            persistance.sqlconectar objetoconector = new persistance.sqlconectar();
            String query;
            query = "CALL prvalidauser((?),(?))";
             
            ps = objetoconector.sqlconectando().prepareStatement(query);
            ps.setString(1,username);
            ps.setString(2,password); //enviar strings al statement
            
            rs = ps.executeQuery();
            
            if(rs.next()){
                 confirma=1;
                 Home pantallahome = new Home();
                 pantallahome.setVisible(true);//hacer que aparezca la pantalla
                 pantallahome.setLocationRelativeTo(null);//poner la pantalla en medio
                 pantallahome.settextjlabeluser(username);
                
                 
                 
            }else{
             
               JOptionPane.showMessageDialog(null,"datos incorrectos");  
               
            }
        }catch(Exception e){
         
           JOptionPane.showMessageDialog(null,"error"+e.toString()); 
          
        }
    }

    public int confirmar() {
    return confirma;
    }
    
  
    
}












