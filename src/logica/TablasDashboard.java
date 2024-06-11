/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import com.mysql.cj.jdbc.Blob;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Carlos
 */
public class TablasDashboard {
     Archivos procesa = new Archivos();
    public void tablageneral(JTable tablaproyecto){
        String query1 = "CALL dashboardproyecto()";
        String query2 = "SELECT contrato FROM proyecto";
        ResultSet rs1 = null;
        PreparedStatement ps1 = null;
        ResultSet rs2 = null;
        PreparedStatement ps2 = null;
        List<String> pdfFiles = new ArrayList<>();
        persistance.sqlconectar objetoconector = new persistance.sqlconectar();

        try {
        
        ps1 = objetoconector.sqlconectando().prepareStatement(query1);
        ps2 = objetoconector.sqlconectando().prepareStatement(query2);
        DefaultTableModel model = new DefaultTableModel(){
        @Override
                public Class<?> getColumnClass(int columnIndex) {
                    return columnIndex == 1 ? String.class : super.getColumnClass(columnIndex);
                }
            };
        model.addColumn("Proyecto");
        //model.addColumn("Gastos");
        model.addColumn("Contrato");
        tablaproyecto.setModel(model);
        tablaproyecto.setDefaultEditor(Object.class, null);//hace que no se pueda modificar el contenido de la tabla
            
            rs1 = ps1.executeQuery();
            rs2 = ps2.executeQuery();
            pdfFiles.clear();
            while (rs1.next() && rs2.next()) {
                String[] datos = new String[2];
                
                datos[0] = rs1.getString(1);
                datos[1] = rs1.getString(2);
                //datos[2] = rs1.getString(3);
               
                model.addRow(datos);
                
                File contratoFile = procesa.saveBlobToFile((Blob) rs2.getBlob("contrato"));
                
                pdfFiles.add(contratoFile != null ? contratoFile.getAbsolutePath() : null);
             }
            tablaproyecto.getColumnModel().getColumn(1).setCellRenderer(procesa.iconRenderer);
           
        }catch (Exception e) {
        JOptionPane.showMessageDialog(null, "error" + e.toString());
        }finally {
            // Cerrar los recursos en el bloque finally
            try {
                if (rs1 != null) rs1.close();
                if (ps1 != null) ps1.close();
                if (objetoconector.sqlconectando() != null) objetoconector.sqlconectando().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
         procesa.listenerarchivos(tablaproyecto, pdfFiles,1);
         pdfFiles.add(null);
    } 

    
    public void tablaherramienta(JTable tablaherramienta){
     try {
   
        String query1 = "SELECT * FROM dashboardherramienta";
        ResultSet rs1 = null;
        PreparedStatement ps1 = null;
        persistance.sqlconectar objetoconector = new persistance.sqlconectar();

        ps1 = objetoconector.sqlconectando().prepareStatement(query1);
       
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Herramienta");
        model.addColumn("Proyecto");
        model.addColumn("Personal");
        model.addColumn("Estatus");
        model.addColumn("Última vez");
        tablaherramienta.setModel(model);
        tablaherramienta.setDefaultEditor(Object.class, null);//hace que no se pueda modificar el contenido de la tabla
                
            rs1 = ps1.executeQuery();
           
            while (rs1.next()) {
                String[] datos = new String[5];
                
                datos[0] = rs1.getString(1);
                datos[1] = rs1.getString(2);
                datos[2] = rs1.getString(3);
                datos[3] = rs1.getString(4);
                datos[4] = rs1.getString(5);
                model.addRow(datos);
                
                
             }
           
        }catch (Exception e) {
        JOptionPane.showMessageDialog(null, "error" + e.toString());
        }
   }
    
     
   public void filtroherramienta(JTable tableh, String disponibilidad, String nombre){
          try {
   
        String query1 = "CALL filtrarherramienta((?),(?))";
        ResultSet rs1 = null;
        PreparedStatement ps1 = null;
        persistance.sqlconectar objetoconector = new persistance.sqlconectar();

        ps1 = objetoconector.sqlconectando().prepareStatement(query1);
       
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Herramienta");
        model.addColumn("Proyecto");
        model.addColumn("Personal");
        model.addColumn("Estatus");
        model.addColumn("Última vez");
        tableh.setModel(model);
        tableh.setDefaultEditor(Object.class, null);//hace que no se pueda modificar el contenido de la tabla
        
           ps1.setString(1, disponibilidad);
           ps1.setString(2, nombre);
           
            rs1 = ps1.executeQuery();
           
            while (rs1.next()) {
                String[] datos = new String[5];
                
                datos[0] = rs1.getString(1);
                datos[1] = rs1.getString(2);
                datos[2] = rs1.getString(3);
                datos[3] = rs1.getString(4);
                datos[4] = rs1.getString(5);
                model.addRow(datos);
                
                
             }
           
        }catch (Exception e) {
        JOptionPane.showMessageDialog(null, "error" + e.toString());
        }
   }
   
     public void rellenoherramientas(JComboBox boxh,JComboBox boxpy,JComboBox boxpe){
       String query1="SELECT Herramienta FROM dashboardherramienta";
       String query2 = "SELECT nombreproyecto FROM proyecto";
       String query3 = "SELECT nombre FROM trabajador";
       try{
           ResultSet rs1; 
           ResultSet rs2; 
           ResultSet rs3; 
           PreparedStatement ps1;//provoca que cuando pongas (?) se inserten los strings
           PreparedStatement ps2 ;
           PreparedStatement ps3 ;
           persistance.sqlconectar objetoconector = new persistance.sqlconectar();
            
             
            ps1 = objetoconector.sqlconectando().prepareStatement(query1);
            ps2 = objetoconector.sqlconectando().prepareStatement(query2);
            ps3 = objetoconector.sqlconectando().prepareStatement(query3);
            
            rs1 = ps1.executeQuery();
            rs2 = ps2.executeQuery();
            rs3 = ps3.executeQuery();
            
            while(rs1.next()){
                boxh.addItem(rs1.getString("Herramienta"));
            }
            while(rs2.next()){
                boxpy.addItem(rs2.getString("nombreproyecto"));
            }
            
            while(rs3.next()){
                boxpe.addItem(rs3.getString("nombre"));
            }
           
       }catch(Exception e){
          JOptionPane.showMessageDialog(null,"error"+e.toString()); 
       }
       
       
     
        
    }
   
     public void mostrarherramienta(JTable tabla,JTextField herramienta, JComboBox proyecto,JComboBox personal, JRadioButton disponibilidad,JRadioButton nodisponibilidad){
       try{
           int fila = tabla.getSelectedRow();
           String radios = null;
           if(fila >= 0){
             herramienta.setText(tabla.getValueAt(fila, 0).toString());
             proyecto.setSelectedItem(tabla.getValueAt(fila, 1));
             personal.setSelectedItem(tabla.getValueAt(fila, 2));
             
             radios = tabla.getValueAt(fila, 3).toString();
             
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
     
     public void updateherramienta(JTable tabla,JTextField herramienta, JComboBox proyecto,JComboBox personal, JRadioButton disponibilidad,JRadioButton nodisponibilidad ){
       try{
            String query1 = "CALL updateherramienta((?),(?),(?),(?))";
            String nombreh = herramienta.getText();
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
            
            ps1.setString(1, nombreh);
            ps1.setString(2, nombrepy);
            ps1.setString(3, nombrepe);
            ps1.setString(4, disponible);
            
            rs1 = ps1.executeQuery();
            
            if(rs1.next()){
               JOptionPane.showMessageDialog(null,"El estatus de su herramienta ha sido actualizado.");   
            }
            
           
       }catch(Exception e){
          JOptionPane.showMessageDialog(null,"El estatus de su herramienta ha sido actualizado.");  
       }
     }
     
     public void tablavehiculo(JTable tablavehiculo){
                try {
        String query1 = "SELECT * FROM dashboardvehiculo";
        ResultSet rs1 = null;
        PreparedStatement ps1 = null;
        persistance.sqlconectar objetoconector = new persistance.sqlconectar();

        ps1 = objetoconector.sqlconectando().prepareStatement(query1);
       
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Vehículo");
        model.addColumn("Personal");
        model.addColumn("Estatus");
        tablavehiculo.setModel(model);
        tablavehiculo.setDefaultEditor(Object.class, null);//hace que no se pueda modificar el contenido de la tabla
                
            rs1 = ps1.executeQuery();
           
            while (rs1.next()) {
                String[] datos = new String[3];
                
                datos[0] = rs1.getString(1);
                datos[1] = rs1.getString(2);
                datos[2] = rs1.getString(3);
                
                model.addRow(datos);
                
                
             }
           
        }catch (Exception e) {
        JOptionPane.showMessageDialog(null, "error" + e.toString());
        }
    }
    
    
    
}
