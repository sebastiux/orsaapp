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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Carlos
 */
public class TablaConceptos {
    public void tablamenuc(JTable tablav){
        try {
        String query1 = "SELECT * FROM tablaconceptos";
        ResultSet rs1 = null;
        PreparedStatement ps1 = null;
        persistance.sqlconectar objetoconector = new persistance.sqlconectar();

        ps1 = objetoconector.sqlconectando().prepareStatement(query1);
       
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Clave");
        model.addColumn("Costo");
        model.addColumn("Descripción");
        tablav.setModel(model);
        tablav.setDefaultEditor(Object.class, null);//hace que no se pueda modificar el contenido de la tabla
             
            rs1 = ps1.executeQuery();
           
            while (rs1.next()) {
                String[] datos = new String[4];
                
                datos[0] = rs1.getString(1);
                datos[1] = rs1.getString(2);
                datos[2] = rs1.getString(3);
                datos[3] = rs1.getString(4);
          
                model.addRow(datos);
             }
        TableColumnModel columnModel = tablav.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50); // ID
        columnModel.getColumn(1).setPreferredWidth(100); // Clave
        columnModel.getColumn(2).setPreferredWidth(100); // Costo
        columnModel.getColumn(3).setPreferredWidth(400); // Descripción (adjust width as needed)

            
           
        }catch (Exception e) {
        JOptionPane.showMessageDialog(null, "error" + e.toString());
        }
    } 
    
      public void insertarconcepto(String clave, String descripcion,String costo){
         try{
            String query1 = "CALL insertarconcepto((?),(?),(?))";
            
         
            ResultSet rs1 = null;
            PreparedStatement ps1 = null;
            persistance.sqlconectar objetoconector = new persistance.sqlconectar();
            
            ps1 = objetoconector.sqlconectando().prepareStatement(query1);
            
            ps1.setString(1, clave);
            ps1.setString(2, descripcion);
            ps1.setString(3, costo);
           
            
            rs1 = ps1.executeQuery();
            if(rs1.next()){
               JOptionPane.showMessageDialog(null,"Concepto insertado correctamente.");    
            }
         
       }catch(Exception e){
          JOptionPane.showMessageDialog(null,"error" + e.toString());  
       }
       
    }
      
      public void updateconcepto(String idconcepto, String clave, String descripcion,String costo){
         try{
            String query1 = "CALL updateconcepto((?),(?),(?),(?))";
            
         
            ResultSet rs1 = null;
            PreparedStatement ps1 = null;
            persistance.sqlconectar objetoconector = new persistance.sqlconectar();
            
            ps1 = objetoconector.sqlconectando().prepareStatement(query1);
            
            ps1.setString(1, idconcepto);
            ps1.setString(2, clave);
            ps1.setString(3, descripcion);
            ps1.setString(4, costo);
           
            
            rs1 = ps1.executeQuery();
             if(rs1.next()){
               JOptionPane.showMessageDialog(null,"Concepto actualizado correctamente.");    
            }
         
       }catch(Exception e){
          JOptionPane.showMessageDialog(null,"error" + e.toString());  
       }
       
    }
      
      
      public void filtrarconcepto(String idconcepto){
         try{
            String query1 = "SELECT tablaconceptos WHERE idconcepto = ?";
            
         
            ResultSet rs1 = null;
            PreparedStatement ps1 = null;
            persistance.sqlconectar objetoconector = new persistance.sqlconectar();
            
            ps1 = objetoconector.sqlconectando().prepareStatement(query1);
            
            ps1.setString(1, idconcepto);
           
            
            rs1 = ps1.executeQuery();
         
       }catch(Exception e){
          JOptionPane.showMessageDialog(null,"error" + e.toString());  
       }
       
    }
      
       public void mostrarconceptos(JTable tabla,JLabel id, JTextField clave,JTextArea descripcion ,JTextField costo ){
       try{
           int fila = tabla.getSelectedRow();
           String radios = null;
           if(fila >= 0){
             id.setText(tabla.getValueAt(fila, 0).toString());
             clave.setText(tabla.getValueAt(fila, 1).toString());
             costo.setText(tabla.getValueAt(fila, 2).toString());
             descripcion.setText(tabla.getValueAt(fila, 3).toString());
           
           }
       }catch(Exception e){
           JOptionPane.showMessageDialog(null,"error"+e.toString()); 
       }
       
     }
    
}
