/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Carlos
 */
public class TablasVehiculos {
    
    public void rellenovehiculos(JComboBox boxv){
       String query1="SELECT nombrev FROM vehiculos";
    
       try{
           ResultSet rs1; 
         
   
           PreparedStatement ps1;//provoca que cuando pongas (?) se inserten los strings
      
          
           persistance.sqlconectar objetoconector = new persistance.sqlconectar();
            
             
            ps1 = objetoconector.sqlconectando().prepareStatement(query1);
        
           
            
            rs1 = ps1.executeQuery();
       
            
            
            while(rs1.next()){
                boxv.addItem(rs1.getString("nombrev"));
            }
         
            
         
       }catch(Exception e){
          JOptionPane.showMessageDialog(null,"error"+e.toString()); 
       }
       
    }
    
    public void rellenovehiculos2(JComboBox boxpe,JComboBox boxpy){
       String query1="SELECT nombre FROM trabajador";
       String query2 = "SELECT nombreproyecto FROM proyecto";
       try{
           ResultSet rs1; 
           ResultSet rs2; 
   
           PreparedStatement ps1;//provoca que cuando pongas (?) se inserten los strings
           PreparedStatement ps2 ;
          
           persistance.sqlconectar objetoconector = new persistance.sqlconectar();
            
             
            ps1 = objetoconector.sqlconectando().prepareStatement(query1);
            ps2 = objetoconector.sqlconectando().prepareStatement(query2);
           
            
            rs1 = ps1.executeQuery();
            rs2 = ps2.executeQuery();
            
            
            while(rs1.next()){
                boxpe.addItem(rs1.getString("nombre"));
            }
            while(rs2.next()){
                boxpy.addItem(rs2.getString("nombreproyecto"));
            }
            
         
       }catch(Exception e){
          JOptionPane.showMessageDialog(null,"error"+e.toString()); 
       }
       
    }
    
    public void tablamenuv(JTable tablav){
        try {
        String query1 = "SELECT * FROM menuvehiculo";
        ResultSet rs1 = null;
        PreparedStatement ps1 = null;
        persistance.sqlconectar objetoconector = new persistance.sqlconectar();

        ps1 = objetoconector.sqlconectando().prepareStatement(query1);
       
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Vehículo");
        model.addColumn("Estatus");
        model.addColumn("Proyecto");
        model.addColumn("Personal");
        model.addColumn("GastosMes");
        model.addColumn("Próximo pago");
        model.addColumn("Motivo");
        tablav.setModel(model);
        tablav.setDefaultEditor(Object.class, null);//hace que no se pueda modificar el contenido de la tabla
             
            rs1 = ps1.executeQuery();
           
            while (rs1.next()) {
                String[] datos = new String[7];
                
                datos[0] = rs1.getString(1);
                datos[1] = rs1.getString(2);
                datos[2] = rs1.getString(3);
                datos[3] = rs1.getString(4);
                datos[4] = rs1.getString(5);
                datos[5] = rs1.getString(6);
                datos[6] = rs1.getString(7);
                model.addRow(datos);
                
                
             }
           
        }catch (Exception e) {
        JOptionPane.showMessageDialog(null, "error" + e.toString());
        }
    } 

   
     public void mostrarvehiculo(JTable tabla,JLabel vehiculo, JComboBox proyecto,JComboBox personal, JRadioButton disponibilidad,JRadioButton nodisponibilidad){
       try{
           int fila = tabla.getSelectedRow();
           String radios = null;
           if(fila >= 0){
             vehiculo.setText(tabla.getValueAt(fila, 0).toString());
             radios = tabla.getValueAt(fila, 1).toString();
             proyecto.setSelectedItem(tabla.getValueAt(fila,2));
             personal.setSelectedItem(tabla.getValueAt(fila,3));
             
     
             if(radios.equals("No")){
               nodisponibilidad.setSelected(true);
               disponibilidad.setSelected(false);
             }
             if(radios.equals("Disponible")){
                 disponibilidad.setSelected(true);
                 nodisponibilidad.setSelected(false);
             }
             
            
           }
       }catch(Exception e){
           JOptionPane.showMessageDialog(null,"error"+e.toString()); 
       }
       
     }
    
     
      public void updatevehiculo(JTable tabla,JLabel vehiculo, JComboBox proyecto,JComboBox personal, JRadioButton disponibilidad,JRadioButton nodisponibilidad ){
       try{
            String query1 = "CALL updatevehiculo((?),(?),(?),(?))";
            String nombrev = vehiculo.getText();
            String nombrepy = (String) proyecto.getSelectedItem();
            String nombrepe = (String) personal.getSelectedItem();
            String disponible = null;
            
            if(disponibilidad.isSelected()){
                disponible = "Disponible";
            }
            
            if(nodisponibilidad.isSelected()){
                disponible = "No";
            }
            
            ResultSet rs1 = null;
            PreparedStatement ps1 = null;
            persistance.sqlconectar objetoconector = new persistance.sqlconectar();
            
            ps1 = objetoconector.sqlconectando().prepareStatement(query1);
            
            ps1.setString(1, nombrev);
            ps1.setString(2, nombrepe);
            ps1.setString(3, nombrepy);
            ps1.setString(4, disponible);
            
            rs1 = ps1.executeQuery();
            
            if(rs1.next()){
               JOptionPane.showMessageDialog(null,"El estatus de su vehículo ha sido actualizado.");   
            }
            
           
       }catch(Exception e){
           JOptionPane.showMessageDialog(null,"El estatus de su vehículo ha sido actualizado.");  
       }
       
      
     }
     
    public void insertacombustible(String nombre, String monto){
         try{
            String query1 = "CALL insertarcombustibles((?),(?))";
            
         
            ResultSet rs1 = null;
            PreparedStatement ps1 = null;
            persistance.sqlconectar objetoconector = new persistance.sqlconectar();
            
            ps1 = objetoconector.sqlconectando().prepareStatement(query1);
            
            ps1.setString(1, nombre);
            ps1.setString(2, monto);
           
            
            rs1 = ps1.executeQuery();
         
       }catch(Exception e){
          JOptionPane.showMessageDialog(null,"error" + e.toString());  
       }
       
    }
    
    public void insertamantenimiento(String nombre, String fecha, String monto){
        //System.out.println(fecha);
         try{
            String query1 = "CALL insertarmantenimiento(?,?,?)";
            
         
            ResultSet rs1 = null;
            PreparedStatement ps1 = null;
            persistance.sqlconectar objetoconector = new persistance.sqlconectar();
            
            ps1 = objetoconector.sqlconectando().prepareStatement(query1);
            
            ps1.setString(1, nombre);
            ps1.setString(2, fecha);
            ps1.setString(3, monto);
            
            rs1 = ps1.executeQuery();
         
       }catch(Exception e){
          JOptionPane.showMessageDialog(null,"error" + e.toString());  
       }
       
    }
    
    public void insertarconsumibles(String nombre, String monto){
        try{
            String query1 = "CALL insertarconsumibles(?,?)";
            
         
            ResultSet rs1 = null;
            PreparedStatement ps1 = null;
            persistance.sqlconectar objetoconector = new persistance.sqlconectar();
            
            ps1 = objetoconector.sqlconectando().prepareStatement(query1);
            
            ps1.setString(1, nombre);
            ps1.setString(2, monto);
           
 
            rs1 = ps1.executeQuery();
         
       }catch(Exception e){
          JOptionPane.showMessageDialog(null,"error" + e.toString());  
       }
    }
    
    public void insertarrefacciones(String nombre, String monto){
          try{
            String query1 = "CALL insertarrefacciones(?,?)";
            
         
            ResultSet rs1 = null;
            PreparedStatement ps1 = null;
            persistance.sqlconectar objetoconector = new persistance.sqlconectar();
            
            ps1 = objetoconector.sqlconectando().prepareStatement(query1);
            
            ps1.setString(1, nombre);
            ps1.setString(2, monto);
           
 
            rs1 = ps1.executeQuery();
         
       }catch(Exception e){
          JOptionPane.showMessageDialog(null,"error" + e.toString());  
       }
    }
    
    public void insertarseguro(String nombre, String fecha, String monto){
        //System.out.println(fecha);
         try{
            String query1 = "CALL insertarseguro(?,?,?)";
            
         
            ResultSet rs1 = null;
            PreparedStatement ps1 = null;
            persistance.sqlconectar objetoconector = new persistance.sqlconectar();
            
            ps1 = objetoconector.sqlconectando().prepareStatement(query1);
            
            ps1.setString(1, nombre);
            ps1.setString(2, monto);
            ps1.setString(3, fecha);
            
            rs1 = ps1.executeQuery();
         
       }catch(Exception e){
          JOptionPane.showMessageDialog(null,"error" + e.toString());  
       }
       
    }
    
    public void insertarverificacion(String nombre, String fecha, String monto){
        //System.out.println(fecha);
         try{
            String query1 = "CALL insertarverificacion(?,?,?)";
            
         
            ResultSet rs1 = null;
            PreparedStatement ps1 = null;
            persistance.sqlconectar objetoconector = new persistance.sqlconectar();
            
            ps1 = objetoconector.sqlconectando().prepareStatement(query1);
            
            ps1.setString(1, nombre);
            ps1.setString(2, monto);
            ps1.setString(3, fecha);
            
            rs1 = ps1.executeQuery();
         
       }catch(Exception e){
          JOptionPane.showMessageDialog(null,"error" + e.toString());  
       }
       
    }
    
     public void insertartenencias(String nombre, String fecha, String monto){
        //System.out.println(fecha);
         try{
            String query1 = "CALL insertartenencias(?,?,?)";
            
         
            ResultSet rs1 = null;
            PreparedStatement ps1 = null;
            persistance.sqlconectar objetoconector = new persistance.sqlconectar();
            
            ps1 = objetoconector.sqlconectando().prepareStatement(query1);
            
            ps1.setString(1, nombre);
            ps1.setString(2, monto);
            ps1.setString(3, fecha);
            
            rs1 = ps1.executeQuery();
         
       }catch(Exception e){
          JOptionPane.showMessageDialog(null,"error" + e.toString());  
       }
       
    }
      public void filtrarvehiculo(JTable tabla,String nombre, String disponibilidad){
          try{
            String query1 = "CALL filtrarvehiculo(?,?)";
            
         
            ResultSet rs1 = null;
            PreparedStatement ps1 = null;
            persistance.sqlconectar objetoconector = new persistance.sqlconectar();
            
            ps1 = objetoconector.sqlconectando().prepareStatement(query1);
            
            ps1.setString(1, nombre);
            ps1.setString(2, disponibilidad);
           
 
            rs1 = ps1.executeQuery();
            
            DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Vehículo");
        model.addColumn("Estatus");
        model.addColumn("Proyecto");
        model.addColumn("Personal");
        model.addColumn("GastosMes");
        model.addColumn("Próximo pago");
        model.addColumn("Motivo");
        tabla.setModel(model);
        tabla.setDefaultEditor(Object.class, null);//hace que no se pueda modificar el contenido de la tabla
             
            rs1 = ps1.executeQuery();
           
            while (rs1.next()) {
                String[] datos = new String[7];
                
                datos[0] = rs1.getString(1);
                datos[1] = rs1.getString(2);
                datos[2] = rs1.getString(3);
                datos[3] = rs1.getString(4);
                datos[4] = rs1.getString(5);
                datos[5] = rs1.getString(6);
                datos[6] = rs1.getString(7);
                model.addRow(datos);
            }
         
       }catch(Exception e){
          JOptionPane.showMessageDialog(null,"error" + e.toString());  
       }
     }  
      
     
    
}
