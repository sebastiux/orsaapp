/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import com.mysql.cj.jdbc.Blob;
import com.sun.jdi.connect.spi.Connection;
import com.toedter.calendar.JDateChooser;
import java.awt.Component;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.lang.model.util.Types;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import persistance.ConexionPool;

/**
 *
 * @author Carlos
 */
public class TablasProyecto {
    int id = -1;
    int control;
    Archivos procesa = new Archivos();
   FileInputStream contract = null;
    public void rellenoproyectos(JComboBox boxp){
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
public void muestratablamenup(JTable tablav) {
    String query = "SELECT * FROM vistamenuproyecto";
    String query2 = "SELECT idproyecto, contrato FROM proyecto"; // Modificado para obtener también el id del proyecto
    ResultSet rs = null;
    PreparedStatement ps = null;
    ResultSet rs2 = null;
    PreparedStatement ps2 = null;
    List<String> pdfFiles = new ArrayList<>();

    try (java.sql.Connection connection = ConexionPool.getConnection()) {
        ps = connection.prepareStatement(query);
        ps2 = connection.prepareStatement(query2);
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 2 ? String.class : super.getColumnClass(columnIndex);
            }
        };

        model.addColumn("Proyecto");
        model.addColumn("Cotización");
        model.addColumn("Contrato");
        model.addColumn("Deadline");

        tablav.setModel(model);
        tablav.setDefaultEditor(Object.class, null); // Hace que no se pueda modificar el contenido de la tabla

        rs = ps.executeQuery();
        Map<Integer, Blob> contratoBlobs = new HashMap<>();

        while (rs.next()) {
            String[] datos = new String[4];
            datos[0] = rs.getString(1);
            datos[1] = rs.getString(2);
            datos[2] = rs.getString(3);
            datos[3] = rs.getString(4);

            model.addRow(datos);
        }

        rs2 = ps2.executeQuery();
        while (rs2.next()) {
            int id = rs2.getInt("idproyecto");
            Blob contratoBlob = (Blob) rs2.getBlob("contrato");
            contratoBlobs.put(id, contratoBlob);
        }

        tablav.getColumnModel().getColumn(2).setCellRenderer(procesa.iconRenderer);

        // Descargar los Blobs en segundo plano
        new Thread(() -> {
            for (Map.Entry<Integer, Blob> entry : contratoBlobs.entrySet()) {
                Blob contratoBlob = entry.getValue();
                if (contratoBlob != null) {
                    try {
                        File contratoFile = procesa.saveBlobToFile(contratoBlob);
                        synchronized (pdfFiles) {
                            pdfFiles.add(contratoFile != null ? contratoFile.getAbsolutePath() : null);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    pdfFiles.add(null);
                }
            }

            // Actualizar la tabla con los archivos descargados
            SwingUtilities.invokeLater(() -> procesa.listenerarchivos(tablav, pdfFiles, 2));
        }).start();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "error" + e.toString());
    } finally {
        try {
            if (rs != null) rs.close();
            if (rs2 != null) rs2.close();
            if (ps != null) ps.close();
            if (ps2 != null) ps2.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


    
    public void tablamenup(JTable tablav){
        String query1 = "SELECT * FROM vistamenuproyecto";
        String query2 = "SELECT contrato FROM proyecto";
        ResultSet rs1 = null;
        PreparedStatement ps1 = null;
        ResultSet rs2 = null;
        PreparedStatement ps2 = null;
         List<String> pdfFiles = new ArrayList<>();
        persistance.sqlconectar objetoconector = new persistance.sqlconectar();
        try{
       
        
        ps1 = objetoconector.sqlconectando().prepareStatement(query1);
        ps2 = objetoconector.sqlconectando().prepareStatement(query2);
        DefaultTableModel model = new DefaultTableModel() {
                @Override
                public Class<?> getColumnClass(int columnIndex) {
                    return columnIndex == 2 ? String.class : super.getColumnClass(columnIndex);
                }
            };
        model.addColumn("Proyecto");
        model.addColumn("Cotización");
        model.addColumn("Contrato");
        model.addColumn("Deadline");
     
        tablav.setModel(model);
        tablav.setDefaultEditor(Object.class, null);//hace que no se pueda modificar el contenido de la tabla
             
            rs1 = ps1.executeQuery();
            rs2 = ps2.executeQuery();
            
            pdfFiles.clear();
            while (rs1.next()&& rs2.next()) {
                String[] datos = new String[4];
                
                datos[0] = rs1.getString(1);
                datos[1] = rs1.getString(2);
                datos[2] = rs1.getString(3);
                datos[3] = rs1.getString(4);
               
               
                model.addRow(datos);
                File contratoFile = procesa.saveBlobToFile((Blob) rs2.getBlob("contrato"));
                
                pdfFiles.add(contratoFile != null ? contratoFile.getAbsolutePath() : null);
                
             }
             tablav.getColumnModel().getColumn(2).setCellRenderer(procesa.iconRenderer);
           
          
           
           
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
         procesa.listenerarchivos(tablav, pdfFiles,2);
         pdfFiles.add(null);
    } 
    
      public void crearproyecto(JTextField nombre,JTextField cotiza, FileInputStream contrato,int filesize, String limite ){
       try{
            String query1 = "CALL crearproyecto(?,?,?,?)";
            String nombrep = nombre.getText();
            String cotizacion = cotiza.getText();
            
            
            ResultSet rs1 = null;
            PreparedStatement ps1 = null;
            persistance.sqlconectar objetoconector = new persistance.sqlconectar();
            
            ps1 = objetoconector.sqlconectando().prepareStatement(query1);
            
            
            ps1.setString(1, nombrep);
            ps1.setString(2, cotizacion);
            ps1.setBinaryStream(3, contrato, filesize);
            
            if(limite.equals(null)){
            
            ps1.setNull(4, java.sql.Types.DATE);
           
       
            }else{
            ps1.setString(4, limite);
          
            }
            
            rs1 = ps1.executeQuery();
            
            if(rs1.next()){
               JOptionPane.showMessageDialog(null,"Su proyecto ha sido guardado exitosamente.");   
            }
            
           
       }catch(Exception e){
          JOptionPane.showMessageDialog(null,"error" + e.toString());  
       }
}
    
    
    public void mostrarproyecto(JTable tabla,JTextField nombre,JTextField cotiza, FileInputStream contrato,int filesize, JDateChooser limite) throws SQLException, FileNotFoundException, IOException, ParseException{
       try{
           int fila = tabla.getSelectedRow();

           if(fila >= 0){
             nombre.setText(tabla.getValueAt(fila, 0).toString());
             cotiza.setText( tabla.getValueAt(fila, 1).toString());
            
             String fechaString = tabla.getValueAt(fila, 3).toString();
             SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
             Date fecha = sdf.parse(fechaString);
    
             // Establecer la fecha en el JDateChooser
             limite.setDate(fecha);
           }
           String nombreProyecto = nombre.getText();
           String query = "SELECT contrato FROM proyecto WHERE nombreproyecto = ?";
            
            persistance.sqlconectar objetoconector = new persistance.sqlconectar();
            PreparedStatement ps = objetoconector.sqlconectando().prepareStatement(query);
            ps.setString(1, nombreProyecto);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Blob blob = (Blob) rs.getBlob("contrato");
                if (blob != null) {
                    InputStream inputStream = blob.getBinaryStream();
                    File tempFile = new File("tempContrato.pdf");
                    try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                    }
                    // Ahora puedes usar el archivo tempFile
                   contract = new FileInputStream(tempFile);
                    // Aquí puedes hacer lo que necesites con contratoStream
                    // Por ejemplo, podrías mostrar el archivo en algún visor PDF integrado o externo
                }
            }

            rs.close();
            ps.close();
        
           
         }catch(SQLException | IOException  | ParseException e ){
           JOptionPane.showMessageDialog(null,"error"+e.toString()); 
       }
      }
    

     
    
    public FileInputStream setcontrato(){
       return contract; 
    }
    
    
   public void updateproyecto(JTable tabla,JTextField nombre,JTextField cotiza, FileInputStream contrato,int filesize, String limite ){
       try{
           String query1 = "CALL updateproyecto(?, ?, ?, ?)";
           String nombrep = nombre.getText();
           String cotizacion = cotiza.getText();
            
            
            ResultSet rs1 = null;
            PreparedStatement ps1 = null;
            persistance.sqlconectar objetoconector = new persistance.sqlconectar();
            
            ps1 = objetoconector.sqlconectando().prepareStatement(query1);
            
            ps1.setString(1, nombrep);
            ps1.setString(2, cotizacion);
            ps1.setBinaryStream(3, contrato, filesize);
            
            
            if(limite.equals(null)){
            
            ps1.setNull(4, java.sql.Types.DATE);
           
       
            }else{
            ps1.setString(4, limite);
          
            }
            
            rs1 = ps1.executeQuery();
            
            if(rs1.next()){
               JOptionPane.showMessageDialog(null,"Su proyecto ha sido actualizado.");   
            }
            
           
       }catch(Exception e){
         JOptionPane.showMessageDialog(null,"Su proyecto ha sido actualizado.");   
       }
    }
    
    public void filtroproyecto(JTable tablep, String nombre){
        String query1 = "SELECT * FROM vistamenuproyecto";
        String query2 = "SELECT * FROM vistamenuproyecto WHERE Proyecto = ?";
        String query3 = "SELECT contrato FROM proyecto WHERE nombreproyecto = ?";
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        ResultSet rs3 = null;
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        PreparedStatement ps3 = null;
        List<String> pdfFiles = new ArrayList<>();
        persistance.sqlconectar objetoconector = new persistance.sqlconectar();
         try {
             ps1 = objetoconector.sqlconectando().prepareStatement(query1);
             ps2 = objetoconector.sqlconectando().prepareStatement(query2);
             ps3 = objetoconector.sqlconectando().prepareStatement(query3);
             
            if(nombre.equals("Proyecto")){
            
        
           DefaultTableModel model = new DefaultTableModel(){
        @Override
                public Class<?> getColumnClass(int columnIndex) {
                    return columnIndex == 2 ? String.class : super.getColumnClass(columnIndex);
                }
            };
            model.addColumn("Proyecto");
            model.addColumn("Cotización");
            model.addColumn("Contrato");
            model.addColumn("Deadline");

            tablep.setModel(model);
            tablep.setDefaultEditor(Object.class, null);//hace que no se pueda modificar el contenido de la tabla

                rs1 = ps1.executeQuery();
                rs3 = ps3.executeQuery();
                pdfFiles.clear();
                while (rs1.next() && rs3.next() ) {
                    String[] datos = new String[4];

                    datos[0] = rs1.getString(1);
                    datos[1] = rs1.getString(2);
                    datos[2] = rs1.getString(3);
                    datos[3] = rs1.getString(4);

                    model.addRow(datos);
                    File contratoFile = procesa.saveBlobToFile((Blob) rs3.getBlob("contrato"));
                
                    pdfFiles.add(contratoFile != null ? contratoFile.getAbsolutePath() : null);

                 } 
                tablep.getColumnModel().getColumn(2).setCellRenderer(procesa.iconRenderer);
                procesa.listenerarchivos(tablep, pdfFiles,2);
                pdfFiles.add(null);
         }else{
        
        ps2.setString(1,nombre);
        ps3.setString(1,nombre);
        DefaultTableModel model = new DefaultTableModel(){
        @Override
                public Class<?> getColumnClass(int columnIndex) {
                    return columnIndex == 2 ? String.class : super.getColumnClass(columnIndex);
                }
            };
            model.addColumn("Proyecto");
            model.addColumn("Cotización");
            model.addColumn("Contrato");
            model.addColumn("Deadline");

        
        tablep.setModel(model);
        tablep.setDefaultEditor(Object.class, null);//hace que no se pueda modificar el contenido de la tabla
             
                rs2 = ps2.executeQuery();
                rs3 = ps3.executeQuery();
                pdfFiles.clear();
                while (rs2.next() && rs3.next() ) {
                    String[] datos = new String[4];

                    datos[0] = rs2.getString(1);
                    datos[1] = rs2.getString(2);
                    datos[2] = rs2.getString(3);
                    datos[3] = rs2.getString(4);

                    model.addRow(datos);
                    File contratoFile = procesa.saveBlobToFile((Blob) rs3.getBlob("contrato"));
                
                    pdfFiles.add(contratoFile != null ? contratoFile.getAbsolutePath() : null);

                 } 
                tablep.getColumnModel().getColumn(2).setCellRenderer(procesa.iconRenderer);
                procesa.listenerarchivos(tablep, pdfFiles,2);
                pdfFiles.add(null);
            }
         }catch(Exception e){
           JOptionPane.showMessageDialog(null,"error"+e.toString()); 
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
         
         
    }
    
    
    
    public void rellenoproyectoa(JComboBox boxp){
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
    
   public void tablaasignados(JTable tablaa) {
  PreparedStatement ps1 = null;
    ResultSet rs1 = null;
    persistance.sqlconectar objetoconector = new persistance.sqlconectar();

    try {
        // Query para obtener los nombres de los trabajadores
        String query1 = "SELECT idtrabajador AS ID, nombre AS Nombre FROM trabajador";
        ps1 = objetoconector.sqlconectando().prepareStatement(query1);

        // Crear el modelo de la tabla
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // La tercera columna (índice 2) es de tipo Boolean
                return columnIndex == 2 ? Boolean.class : String.class;
            }
        };
        model.addColumn("ID");
        model.addColumn("Nombre");
        model.addColumn("Seleccionar");

        // Asignar el modelo a la tabla
        tablaa.setModel(model);

        // Ejecutar la consulta y llenar la tabla con los nombres
        rs1 = ps1.executeQuery();
        while (rs1.next()) {
            String id = rs1.getString("ID");
            String nombre = rs1.getString("Nombre");
            boolean seleccionado = false; // Por defecto, ningún checkbox seleccionado
            model.addRow(new Object[]{id, nombre, seleccionado});
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error: " + e.toString());
    } finally {
        // Cerrar los recursos en el bloque finally
        try {
            if (rs1 != null) rs1.close();
            if (ps1 != null) ps1.close();
            if (objetoconector.sqlconectando() != null) objetoconector.sqlconectando().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }
    
    public void tablavistaasignaciones(JTable tablav) {
       
        PreparedStatement ps1 = null;
        ResultSet rs1 = null;
        persistance.sqlconectar objetoconector = new persistance.sqlconectar();

        try {
           

            // Query para obtener los datos de la vista
            String query1 = "SELECT * FROM vistaasignacion";
            ps1 = objetoconector.sqlconectando().prepareStatement(query1);

            // Crear el modelo de la tabla
            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID");
            model.addColumn("Proyecto");
            model.addColumn("Estatus");
            model.addColumn("Descripción");
            model.addColumn("Actualización");

            // Asignar el modelo a la tabla
            tablav.setModel(model);
            tablav.setDefaultEditor(Object.class, null); // Deshabilitar la edición de la tabla

            // Ejecutar la consulta y llenar la tabla con los datos
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

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        } finally {
            // Cerrar los recursos en el bloque finally
            try {
                if (rs1 != null) rs1.close();
                if (ps1 != null) ps1.close();
                if (objetoconector.sqlconectando() != null) objetoconector.sqlconectando().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    
    public void insertarasignacion(String nombreproyecto, String estatus, String descripcion) {
       
        PreparedStatement ps1 = null;
        ResultSet rs1 = null;
        persistance.sqlconectar objetoconector = new persistance.sqlconectar();

        try {
           

            // Query para llamar al procedimiento almacenado
            String query1 = "CALL insertarasignacion(?, ?, ?)";
            ps1 = objetoconector.sqlconectando().prepareStatement(query1);

            // Establecer los parámetros del procedimiento almacenado
            ps1.setString(1, nombreproyecto);
            ps1.setString(2, estatus);
            ps1.setString(3, descripcion);

            // Ejecutar el procedimiento almacenado
            rs1 = ps1.executeQuery();

            // Mostrar mensaje de éxito si la asignación se ha insertado correctamente
            if (rs1.next()) {
                JOptionPane.showMessageDialog(null, "Su asignación ha sido insertada.");
            }

        } catch (Exception e) {
             JOptionPane.showMessageDialog(null, "Su asignación ha sido insertada.");
        } finally {
            // Cerrar los recursos en el bloque finally
            try {
                if (rs1 != null) rs1.close();
                if (ps1 != null) ps1.close();
                if (objetoconector.sqlconectando() != null) objetoconector.sqlconectando().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
   public void insertarpersonalasignado(JTable personas, int idasignacion) {
    PreparedStatement ps1 = null;
    persistance.sqlconectar objetoconector = new persistance.sqlconectar();
    String estatusActivo = "Activo";
    String estatusInactivo = "Inactivo";

    try {
        // Query para llamar al procedimiento almacenado con el estatus
        String query1 = "CALL insertarasignacionfechatrabajador(?, ?, ?)";
        ps1 = objetoconector.sqlconectando().prepareStatement(query1);

        for (int i = 0; i < personas.getRowCount(); i++) {
            Boolean isSelected = (Boolean) personas.getValueAt(i, 1);
            String nombreTrabajador = (String) personas.getValueAt(i, 0);
            int trabajadorId = getTrabajadorId(nombreTrabajador);  // Método para obtener el ID del trabajador

            if (isSelected != null && isSelected) {
                // Insertar con estado 'Activo'
                ps1.setString(1, nombreTrabajador);
                ps1.setInt(2, idasignacion);
                ps1.setString(3, estatusActivo);
                ps1.executeUpdate();
            } else {
                // Insertar con estado 'Inactivo'
                ps1.setString(1, nombreTrabajador);
                ps1.setInt(2, idasignacion);
                ps1.setString(3, estatusInactivo);
                ps1.executeUpdate();
            }
        }
        JOptionPane.showMessageDialog(null, "Personal asignado con estado actualizado.");
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error al asignar personal.");
        e.printStackTrace();
    } finally {
        // Cerrar los recursos en el bloque finally
        try {
            if (ps1 != null) ps1.close();
            if (objetoconector.sqlconectando() != null) objetoconector.sqlconectando().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

private int getTrabajadorId(String nombreTrabajador) {
    PreparedStatement ps = null;
    ResultSet rs = null;
    
    persistance.sqlconectar objetoconector = new persistance.sqlconectar();
    int trabajadorId = -1; // Valor por defecto en caso de no encontrar el ID

    try {
        

        // Query para buscar el ID del trabajador por nombre
        String query = "SELECT idtrabajador FROM Trabajador WHERE nombre = ?";
        ps = objetoconector.sqlconectando().prepareStatement(query);
        ps.setString(1, nombreTrabajador);

        // Ejecutar la consulta
        rs = ps.executeQuery();

        // Procesar el resultado
        if (rs.next()) {
            trabajadorId = rs.getInt("idtrabajador");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        // Cerrar los recursos en el bloque finally
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (objetoconector.sqlconectando() != null) objetoconector.sqlconectando().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    return trabajadorId;
}


    
     public void mostrarasignaciones(JTable tabla, JTextField id, JComboBox estatus, JComboBox proyecto, JTextPane actualizar){
        try{
           int fila = tabla.getSelectedRow();
           String radios = null;
           if(fila >= 0){
             id.setText(tabla.getValueAt(fila, 0).toString());
             proyecto.setSelectedItem(tabla.getValueAt(fila,1));
             estatus.setSelectedItem(tabla.getValueAt(fila,2));
             actualizar.setText(tabla.getValueAt(fila, 3).toString());
           }
       }catch(Exception e){
           JOptionPane.showMessageDialog(null,"error"+e.toString()); 
       }
       
      }
     
     public void getidasignacion() {
      
        PreparedStatement ps2 = null;
        ResultSet rs2 = null;
        persistance.sqlconectar objetoconector = new persistance.sqlconectar();

        try {
       

            // Query para obtener el id de asignacion más reciente
            String query2 = "SELECT idasignacion AS id FROM asignacion ORDER BY fechaultimamodificacion DESC LIMIT 1";
            ps2 = objetoconector.sqlconectando().prepareStatement(query2);
            

            // Ejecutar la consulta
            rs2 = ps2.executeQuery();
            if (rs2.next()) {
                 id = rs2.getInt("id");
                System.out.println("ID de asignación más reciente: " + id);
            } 
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        } finally {
            // Cerrar los recursos en el bloque finally
            try {
                if (rs2 != null) rs2.close();
                if (ps2 != null) ps2.close();
                if (objetoconector.sqlconectando() != null) objetoconector.sqlconectando().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
     
     public int setidasignacion(){
        return id; 
     }
     
    public void mostrarpersonal(JTable tablaa, String idasignacion) {
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        //Connection conn = null;
        persistance.sqlconectar objetoconector = new persistance.sqlconectar();
        
        try {
            // Conexión a la base de datos
            //conn = (Connection) objetoconector.sqlconectando();
            
            // Query para obtener todos los nombres de la tabla trabajador
            String query1 = "SELECT idtrabajador AS ID, nombre AS Nombre FROM trabajador";
            ps1 = objetoconector.sqlconectando().prepareStatement(query1);
            rs1 = ps1.executeQuery();

            // Crear el modelo de la tabla
            DefaultTableModel model = new DefaultTableModel() {
                @Override
                public Class<?> getColumnClass(int columnIndex) {
                    return columnIndex == 2 ? Boolean.class : String.class;
                }
            };
            model.addColumn("ID");
            model.addColumn("Nombre");
            model.addColumn("Seleccionar");

            tablaa.setModel(model);

            // Crear un set para almacenar los nombres seleccionados
            Set<String> nombresSeleccionados = new HashSet<>();

            // Query para obtener los nombres seleccionados por el procedimiento almacenado
            String query2 = "CALL recuperanombresasignacion(?)";//Aquí se va recuperar el id 
            ps2 = objetoconector.sqlconectando().prepareStatement(query2);
            ps2.setString(1, idasignacion);
            rs2 = ps2.executeQuery();

            while (rs2.next()) {
                nombresSeleccionados.add(rs2.getString("nombre"));
            }

             rs1 = ps1.executeQuery();
            while (rs1.next()) {
                String id = rs1.getString("ID");
                String nombre = rs1.getString("Nombre");
                //boolean seleccionado = false; // Por defecto, ningún checkbox seleccionado
                boolean seleccionado = nombresSeleccionados.contains(nombre);
                model.addRow(new Object[]{id, nombre, seleccionado});
         }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        } finally {
            // Cerrar los recursos en el bloque finally
            try {
                if (rs1 != null) rs1.close();
                if (rs2 != null) rs2.close();
                if (ps1 != null) ps1.close();
                if (ps2 != null) ps2.close();
                if (objetoconector.sqlconectando() != null) objetoconector.sqlconectando().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

 
   
    public void updateasignacion(String idasignacion, String estatus) {
      
        PreparedStatement ps1 = null;
        ResultSet rs1 = null;
        persistance.sqlconectar objetoconector = new persistance.sqlconectar();

        try {
           

            // Query para actualizar el estatus de la asignación
            String query1 = "CALL updateestatusasignacion(?, ?)";
           ps1 = objetoconector.sqlconectando().prepareStatement(query1);
            ps1.setString(1, idasignacion);
            ps1.setString(2, estatus);

            // Ejecutar la actualización
            rs1 = ps1.executeQuery();
            if (rs1.next()) {
                JOptionPane.showMessageDialog(null, "La asignación ha sido actualizada.");
            }
        } catch (Exception e) {
              JOptionPane.showMessageDialog(null, "La asignación ha sido actualizada.");
        } finally {
            // Cerrar los recursos en el bloque finally
            try {
                if (rs1 != null) rs1.close();
                if (ps1 != null) ps1.close();
                if (objetoconector.sqlconectando() != null) objetoconector.sqlconectando().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void filtroasignados(JTable tablav, String proyecto, String estatus){
       try {
        String query1 = "CALL filtrarasignacion(?,?)";
        ResultSet rs1 = null;
        PreparedStatement ps1 = null;
        persistance.sqlconectar objetoconector = new persistance.sqlconectar();

        ps1 = objetoconector.sqlconectando().prepareStatement(query1);
        ps1.setString(1,proyecto);
        ps1.setString(2,estatus);
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Proyecto");
        model.addColumn("Estatus");
        model.addColumn("Descripción");
        model.addColumn("Actualización");
     
        tablav.setModel(model);
        tablav.setDefaultEditor(Object.class, null);//hace que no se pueda modificar el contenido de la tabla
             
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
    
}
