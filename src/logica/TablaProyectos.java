/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;
// añadir después a la página del dashboard
//import gui.Home;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.lang.model.util.Types;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

/**
 *
 * @author Carlos
 */
public class TablaProyectos {
    
    public void mostrartablap(JTable tabla, String proyecto, String fecha) {
    try {
        String query1 = "CALL filtrarproyecto(?,?)";
        
        
        ResultSet rs1 = null;
        
       
        PreparedStatement ps1 = null;
        
        persistance.sqlconectar objetoconector = new persistance.sqlconectar();

        ps1 = objetoconector.sqlconectando().prepareStatement(query1);
       

        DefaultTableModel model = new DefaultTableModel();
        
        model.addColumn("Proyecto");
        model.addColumn("Gastos");
        model.addColumn("Contrato");
      
        tabla.setModel(model);
        tabla.setDefaultEditor(Object.class, null);//hace que no se pueda modificar el contenido de la tabla
         byte[] archivos = null;
           if(fecha.equals("")){
            
            ps1.setNull(2, java.sql.Types.DATE);
           
       
            }else{
            ps1.setString(2, fecha);
          
            }
           if(proyecto.equals("Proyecto")){
            proyecto = "";
           }
           ps1.setString(1, proyecto);
           
            
           
            rs1 = ps1.executeQuery();
           
            while (rs1.next()) {
                String[] datos = new String[6];
                
                datos[0] = rs1.getString(1);
                datos[1] = rs1.getString(2);
                
                archivos = rs1.getBytes(3);//añadir pdf
                model.addRow(datos);
                
                
             }
           

            
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "error" + e.toString());
    }
}
    
  

           
 }
