/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import com.mysql.cj.jdbc.Blob;
import com.sun.jdi.connect.spi.Connection;
import com.toedter.calendar.JDateChooser;
import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.lang.model.util.Types;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import persistance.ConexionPool;

/**
 *
 * @author Carlos
 */
public class TablaPersonal {
    Archivos procesa = new Archivos();
    FileInputStream factura = null;
    



    
    private Cache cache;

    public TablaPersonal() {
        // Expiración de caché de 10 minutos
        this.cache = new Cache(TimeUnit.MINUTES.toMillis(10));
    }
    
    public void vistatabla(JTable tablav) {
    String query1 = "SELECT * FROM totalactividades";
    String query2 = "SELECT factura FROM actividad ORDER BY idactividad DESC";
    ResultSet rs1 = null;
    PreparedStatement ps1 = null;
    ResultSet rs2 = null;
    PreparedStatement ps2 = null;
    List<String> pdfFiles = new ArrayList<>();

    try (java.sql.Connection connection = ConexionPool.getConnection()) {
        ps1 = connection.prepareStatement(query1);
        ps2 = connection.prepareStatement(query2);
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 7 ? String.class : super.getColumnClass(columnIndex);
            }
        };

        model.addColumn("ID");
        model.addColumn("Folio");
        model.addColumn("Proyecto");
        model.addColumn("Costo");
        model.addColumn("Fecha");
        model.addColumn("Descripción");
        model.addColumn("Autorización");
        model.addColumn("Factura");
        model.addColumn("Responsable");
        model.addColumn("Estatus");
        model.addColumn("Concepto");

        tablav.setModel(model);
        tablav.setDefaultEditor(Object.class, null);

        rs1 = ps1.executeQuery();
        List<Blob> facturaBlobs = new ArrayList<>();

        while (rs1.next()) {
            String[] datos = new String[11];
            for (int i = 0; i < 11; i++) {
                datos[i] = rs1.getString(i + 1);
            }
            model.addRow(datos);
        }

        rs2 = ps2.executeQuery();
        while (rs2.next()) {
            Blob facturaBlob = (Blob) rs2.getBlob("factura");
            facturaBlobs.add(facturaBlob);
        }

        tablav.getColumnModel().getColumn(7).setCellRenderer(procesa.iconRenderer);

        new Thread(() -> {
            for (Blob facturaBlob : facturaBlobs) {
                if (facturaBlob != null) {
                    try {
                        File facturaFile = procesa.saveBlobToFile(facturaBlob);
                        synchronized (pdfFiles) {
                            pdfFiles.add(facturaFile != null ? facturaFile.getAbsolutePath() : null);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    pdfFiles.add(null);
                }
            }

            SwingUtilities.invokeLater(() -> procesa.listenerarchivos(tablav, pdfFiles, 7));
        }).start();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "error" + e.toString());
        e.printStackTrace();
    } finally {
        try {
            if (rs1 != null) rs1.close();
            if (rs2 != null) rs2.close();
            if (ps1 != null) ps1.close();
            if (ps2 != null) ps2.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

    private void fillTableFromCache(List<Object[]> cacheData, JTable tabla, List<String> pdfFiles) {
        DefaultTableModel model = (DefaultTableModel) tabla.getModel();
    for (Object[] row : cacheData) {
        model.addRow(row);
        // Simula la adición de rutas de archivos PDF a la lista pdfFiles si es necesario
        pdfFiles.add(null); // O usa una ruta de archivo real si tienes esa información
    }
    tabla.getColumnModel().getColumn(7).setCellRenderer(procesa.iconRenderer);
    }
    
public void mostrartabla(JTable tabla, String fecha, String nombre, String proyecto, String name, String estatus, String concepto) {
    String query1 = "CALL filtraractividad(?,?,?,?,?,?)";
    String query3 = "SELECT factura FROM actividad ORDER BY idactividad DESC";
    ResultSet rs1 = null;
    ResultSet rs3 = null;
    PreparedStatement ps1 = null;
    PreparedStatement ps3 = null;
    List<String> pdfFiles = new ArrayList<>();
    persistance.sqlconectar objetoconector = new persistance.sqlconectar();

    try {
        /*
        String cacheKey = fecha + "-" + nombre + "-" + proyecto + "-" + name + "-" + estatus;
        Object cachedResult = cache.get(cacheKey);
        if (cachedResult != null) {
            System.out.println("Datos recuperados de la caché");
            fillTableFromCache((List<Object[]>) cachedResult, tabla, pdfFiles);
            procesa.listenerarchivos(tabla, pdfFiles, 7);
            return;
        }
*/
        ps1 = objetoconector.sqlconectando().prepareStatement(query1);
        ps3 = objetoconector.sqlconectando().prepareStatement(query3);

        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 7 ? String.class : super.getColumnClass(columnIndex);
            }
        };
        model.addColumn("ID");
        model.addColumn("Folio");
        model.addColumn("Proyecto");
        model.addColumn("Costo");
        model.addColumn("Fecha");
        model.addColumn("Descripción");
        model.addColumn("Autorización");
        model.addColumn("Factura");
        model.addColumn("Responsable");
        model.addColumn("Estatus");
        model.addColumn("Concepto");
        tabla.setModel(model);
        tabla.setDefaultEditor(Object.class, null); // hace que no se pueda modificar el contenido de la tabla

        if (fecha.equals("")) {
            ps1.setNull(1, java.sql.Types.DATE);
        } else {
            ps1.setString(1, fecha);
        }

        ps1.setString(2, nombre.equals("") ? null : nombre);
        ps1.setString(3, proyecto.equals("Proyecto") ? "" : proyecto);
        ps1.setString(4, name.equals("Responsable") ? "" : name);
        ps1.setString(5, estatus.equals("Estatus") ? "" : estatus);
        ps1.setString(6, estatus.equals("Concepto") ? "" : concepto);

        rs1 = ps1.executeQuery();
        rs3 = ps3.executeQuery();
        pdfFiles.clear();
        List<Object[]> cacheData = new ArrayList<>();

        while (rs1.next() && rs3.next()) {
            String[] datos = new String[11];
            datos[0] = rs1.getString(1);
            datos[1] = rs1.getString(2);
            datos[2] = rs1.getString(3);
            datos[3] = rs1.getString(4);
            datos[4] = rs1.getString(5);
            datos[5] = rs1.getString(6);
            datos[6] = rs1.getString(7);
            datos[7] = rs1.getString(8);
            datos[8] = rs1.getString(9);
            datos[9] = rs1.getString(10);
            datos[10] = rs1.getString(11);

            File contratoFile = procesa.saveBlobToFile((Blob) rs3.getBlob("factura"));
            pdfFiles.add(contratoFile != null ? contratoFile.getAbsolutePath() : null);
            //datos[7] = contratoFile != null ? contratoFile.getAbsolutePath() : null;

            //cacheData.add(datos);
            SwingUtilities.invokeLater(() -> {
                model.addRow(datos);
            });
        }
        tabla.getColumnModel().getColumn(7).setCellRenderer(procesa.iconRenderer);
        //ache.put(cacheKey, cacheData);
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "error" + e.toString());
    } finally {
        // Cerrar los recursos en el bloque finally
        try {
            if (rs1 != null) rs1.close();
            if (ps1 != null) ps1.close();
            if (rs3 != null) rs3.close();
            if (ps3 != null) ps3.close();
            if (objetoconector.sqlconectando() != null) objetoconector.sqlconectando().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    procesa.listenerarchivos(tabla, pdfFiles, 7);
    pdfFiles.add(null);
}

    
   public void pintaactividad(JTable tabla, JLabel id, JDateChooser limite, JTextField folio, JComboBox proyecto, JComboBox name, JTextField monto, JTextArea descripcion, JComboBox responsable, JComboBox status,JTextField concepto) throws SQLException, FileNotFoundException, IOException, ParseException {
    try {
        int fila = tabla.getSelectedRow();

        if (fila >= 0) {
            String idActividad = (String) (tabla.getValueAt(fila, 0) != null ? tabla.getValueAt(fila, 0).toString() : "");
            String folioActividad = (String) (tabla.getValueAt(fila, 1) != null ? tabla.getValueAt(fila, 1).toString() : "");
            String proyectoActividad = (String) (tabla.getValueAt(fila, 2) != null ? tabla.getValueAt(fila, 2).toString() : "");
            String montoActividad = (String) (tabla.getValueAt(fila, 3) != null ? tabla.getValueAt(fila, 3).toString() : "");
            String fechaActividad = (String) (tabla.getValueAt(fila, 4) != null ? tabla.getValueAt(fila, 4).toString() : "");
            String descripcionActividad = (String) (tabla.getValueAt(fila, 5) != null ? tabla.getValueAt(fila, 5).toString() : "");
            String nameActividad = (String) (tabla.getValueAt(fila, 7) != null ? tabla.getValueAt(fila, 7).toString() : "");
            String namePersonal = (String) (tabla.getValueAt(fila, 8) != null ? tabla.getValueAt(fila, 8).toString() : "");
            String nameStatus = (String) (tabla.getValueAt(fila, 9) != null ? tabla.getValueAt(fila, 9).toString() : "");
            String idconcepto = (String) (tabla.getValueAt(fila, 10) != null ? tabla.getValueAt(fila, 10).toString() : "");
            
            id.setText(idActividad);
            folio.setText(folioActividad);
            proyecto.setSelectedItem(proyectoActividad);
            monto.setText(montoActividad);
            responsable.setSelectedItem(namePersonal);
            status.setSelectedItem(nameStatus);
            concepto.setText(idconcepto);
            
            if (!fechaActividad.isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date fecha = sdf.parse(fechaActividad);
                limite.setDate(fecha);
            } else {
                limite.setDate(null);
            }

            descripcion.setText(descripcionActividad);
            name.setSelectedItem(nameActividad);

            String idfactura = id.getText();
            String cacheKey = "factura_" + idfactura;
            File cachedFile = (File) cache.get(cacheKey);

            if (cachedFile != null) {
                // Usa el archivo de la caché
                factura = new FileInputStream(cachedFile);
            } else {
                String query = "SELECT factura FROM actividad WHERE idactividad = ?";
                persistance.sqlconectar objetoconector = new persistance.sqlconectar();
                try (PreparedStatement ps = objetoconector.sqlconectando().prepareStatement(query)) {
                    ps.setString(1, idfactura);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            Blob blob = (Blob) rs.getBlob("factura");
                            if (blob != null) {
                                InputStream inputStream = blob.getBinaryStream();
                                File tempFile = new File("tempFactura_" + idfactura + ".pdf");
                                try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
                                    byte[] buffer = new byte[1024];
                                    int bytesRead;
                                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                                        outputStream.write(buffer, 0, bytesRead);
                                    }
                                }
                                factura = new FileInputStream(tempFile);
                                cache.put(cacheKey, tempFile);
                            }
                        }
                    }
                }
            }
        }
    } catch (SQLException | IOException | ParseException e) {
        JOptionPane.showMessageDialog(null, "error" + e.toString());
    }
}

    
    public FileInputStream setfactura(){
        return factura;
    }

           
 }
       
       
   
    

