/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import com.mysql.cj.jdbc.Blob;
import com.mysql.cj.xdevapi.Statement;
import com.sun.jdi.connect.spi.Connection;
import com.toedter.calendar.JDateChooser;
import java.io.File;
import java.io.FileInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.util.Map;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import persistance.ConexionPool;

/**
 *
 * @author Carlos
 */
public class GuardarP {
    
    public void rellenocombos(JComboBox boxp){
       String query1="SELECT nombreproyecto FROM proyecto";
      
       try{
           ResultSet rs1; 
         
           PreparedStatement ps1;//provoca que cuando pongas (?) se inserten los strings
     
           persistance.sqlconectar objetoconector = new persistance.sqlconectar();
            
             
           
            ps1 = objetoconector.sqlconectando().prepareStatement(query1);
            
            
            rs1 = ps1.executeQuery();
           
            
            while(rs1.next()){
                boxp.addItem(rs1.getString("nombreproyecto"));
            }
           
           
       }catch(Exception e){
          JOptionPane.showMessageDialog(null,"error"+e.toString()); 
       }
     
      
       
    }
       public void rellenoresponsables(JComboBox boxp){
       String query1="SELECT nombre FROM trabajador";
      
       try{
           ResultSet rs1; 
         
           PreparedStatement ps1;//provoca que cuando pongas (?) se inserten los strings
     
           persistance.sqlconectar objetoconector = new persistance.sqlconectar();
            
             
           
            ps1 = objetoconector.sqlconectando().prepareStatement(query1);
            
            
            rs1 = ps1.executeQuery();
           
            
            while(rs1.next()){
                boxp.addItem(rs1.getString("nombre"));
            }
           
           
       }catch(Exception e){
          JOptionPane.showMessageDialog(null,"error"+e.toString()); 
       }
     
      
       
    }
    
    private Cache cache;

     public GuardarP() {//constructor para generar el caché
        // Expiración de caché de 10 minutos
        this.cache = new Cache(TimeUnit.MINUTES.toMillis(10));
    }
       
   
    
   public void saveactividad(String nombresesion, String nombrepersona, String nombreproyecto, String fecha, String costo, FileInputStream factura, int sizep, String desc, String personal, String status,String idconcepto) {
    String query = "CALL crearactividad(?,?,?,?,?,?,?,?,?,?)";
    ResultSet rs = null;
    PreparedStatement ps = null;
   // persistance.ConexionPool objetopool = new persistance.ConexionPool();
    try (java.sql.Connection connection =   ConexionPool.getConnection()) {
        String cacheKey = nombresesion + "-" + nombreproyecto + "-" + costo + "-" + fecha + "-" + desc + "-" + nombrepersona + "-" + personal + "-" + status;

        Object cachedActividad = cache.get(cacheKey);
        if (cachedActividad != null) {
            JOptionPane.showMessageDialog(null, "Actividad recuperada de la caché");
            System.out.println("Actividad recuperada de la caché");
            return;
        }

        ps = connection.prepareStatement(query);

        ps.setString(1, nombresesion);
        ps.setString(2, nombreproyecto);
        ps.setString(3, costo);
        ps.setBinaryStream(4, factura, sizep);
        ps.setString(5, fecha);
        ps.setString(6, desc);
        ps.setString(7, nombrepersona);
        ps.setString(8, status);
        ps.setString(9, personal);
        ps.setString(10, idconcepto);
        int rowsAffected = ps.executeUpdate();

        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Actividad registrada con éxito");
            System.out.println("Actividad registrada con éxito");

            cache.put(cacheKey, "Actividad registrada");
        } else {
            System.out.println("No se encontraron resultados.");
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        System.out.println("Error: " + e.toString());
    } finally {
        try {
            if (ps != null) ps.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

    
    public void updateactividad(JLabel id, JDateChooser limite,JTextField folio,JComboBox proyecto,JComboBox name,JTextField monto,FileInputStream contrato,int filesize, JTextArea descripcion,JLabel user,JComboBox status,JTextField idc ){
       try{
           String query1 = "CALL updateactividad(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
           String identificador = id.getText();
           String fecha;
           Date saveselectedDate = limite.getDate();
           SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
           fecha = dateFormat.format(saveselectedDate);
           String infolio = folio.getText();
           String nproyecto = proyecto.getSelectedItem().toString();
           String responsable = name.getSelectedItem().toString();
           String inmonto = monto.getText();
           String desc = descripcion.getText();
           String nameuser = user.getText();
           String estatus = status.getSelectedItem().toString();
           String idconcepto = idc.getText();
           if(estatus.equals("Estatus")){
               estatus = null;
           }
           
            
            ResultSet rs1 = null;
            PreparedStatement ps1 = null;
            persistance.sqlconectar objetoconector = new persistance.sqlconectar();
            
            ps1 = objetoconector.sqlconectando().prepareStatement(query1);
            
            ps1.setString(1, identificador);
            if(fecha.equals(null)){
            
            ps1.setNull(2, java.sql.Types.DATE);
           
       
            }else{
            ps1.setString(2, fecha);
          
            }
            //ps1.setString(2, fecha);
            ps1.setString(3, infolio);
            ps1.setString(4, nproyecto);
            ps1.setString(5, responsable);
            ps1.setString(6, inmonto);
            ps1.setBinaryStream(7, contrato, filesize);
            ps1.setString(8, desc);
            ps1.setString(9, nameuser);
            ps1.setString(10, estatus);
            ps1.setString(11, idconcepto);
            
            
            rs1 = ps1.executeQuery();
            
            if(rs1.next()){
               JOptionPane.showMessageDialog(null,"Su proyecto ha sido actualizado.");   
            }
            
           
       }catch(Exception e){
          JOptionPane.showMessageDialog(null,"error"+e.toString());  
       }
    }
    
    
    public void elminaractividad(String id) {
        String query1 = "CALL eliminaractividad(?)";
       
        ResultSet rs1 = null;
        PreparedStatement ps1 = null;
      

        try (java.sql.Connection connection = ConexionPool.getConnection()) {
            // Preparar las consultas utilizando la conexión del pool
            ps1 = connection.prepareStatement(query1);
            
            ps1.setString(1, id);

            rs1 = ps1.executeQuery();
        

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "error" + e.toString());
        } finally {
            try {
                if (rs1 != null) rs1.close();
             
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
      
    }
}   
