/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package logica;

import com.mysql.cj.jdbc.Blob;
import com.toedter.calendar.JDateChooser;

import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import static org.eclipse.persistence.logging.LogCategory.length;


public class Archivos {
    
    File archivopdf;
    String nombrepdf;
    FileInputStream inputStream = null;
    int controlarchivo = 0;
    
    public void recuperapdf(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecciona un archivo PDF");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        // Agregar filtro para mostrar solo archivos PDF
        FileNameExtensionFilter pdfFilter = new FileNameExtensionFilter("Archivos PDF", "pdf");
        fileChooser.setFileFilter(pdfFilter);
        
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (selectedFile != null && selectedFile.getName().toLowerCase().endsWith(".pdf")) {
                nombrepdf = selectedFile.getName();
                archivopdf = selectedFile;
            } else {
                JOptionPane.showMessageDialog(null, "Por favor selecciona un archivo PDF válido.");
            }
        }
    }
    
    public File earchivopdf(){
      return archivopdf; 
    }
    
    public int sizearchivo(){
        int tamano = 0;
        tamano = (int) archivopdf.length();
        return tamano;
    }
    
    public FileInputStream streampdf() throws FileNotFoundException{
        inputStream = new FileInputStream(archivopdf);
        
        return inputStream;
    }
    
    public String regresanombre(){
        return nombrepdf;
    }
    
    //Regresar archivo BLOB como un archivo temporal 
    File saveBlobToFile(Blob blob) {
        if (blob == null) return null;

        try {
            InputStream is = blob.getBinaryStream();
            File tempFile = File.createTempFile("pdf", ".pdf");
            FileOutputStream fos = new FileOutputStream(tempFile);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }

            fos.close();
            is.close();
            // Programar la eliminación del archivo temporal cuando se cierre la aplicación
            tempFile.deleteOnExit();

            return tempFile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    //Añadir Mouse listener
    public void listenerarchivos(JTable tablav, List<String> pdfarchivo, int columnas){
     
        
        tablav.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tablav.rowAtPoint(e.getPoint());
                int col = tablav.columnAtPoint(e.getPoint());
                
             
                if (col == columnas ) {
                    int index = row;
                    String filePath = pdfarchivo.get(index);

                    if (filePath != null) {
                        try {
                            File file = new File(filePath);
                            if (file.exists()) {
                                Desktop.getDesktop().open(file);
                               
                            } else {
                                
                                JOptionPane.showMessageDialog(null, "El archivo no existe: " + filePath);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });
    }
    
    // Crear un renderizador de celdas personalizado para mostrar íconos en una tabla
DefaultTableCellRenderer iconRenderer = new DefaultTableCellRenderer() {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        String icono = "/fotos/images/mycontpdf.png";
       // Obtener la URL del recurso
        URL imageUrl = getClass().getResource(icono);
        ImageIcon icon = new ImageIcon(imageUrl); // Carga el ícono desde la URL

         
        // Llamar al método padre para configurar el estado básico de la celda
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Verificar si el valor es un ImageIcon
        if (value == null) {
            // Asignar el ícono al JLabel del renderizador
            setIcon(null);
            setText("No hay archivo"); // Limpiar cualquier texto en la celda
            setHorizontalAlignment(SwingConstants.CENTER); // Centrar el ícono en la celda
            controlarchivo = 0;//Establece que no hay archivo
        } else {
            // Si el valor no es un ImageIcon, mostrar un texto de error
            setIcon(icon); // Asignar el ícono al JLabel del renderizador
            setText(""); // Limpiar cualquier texto en la celda
            setHorizontalAlignment(SwingConstants.CENTER); // Centrar el ícono en la celda
            //setText("No hay ícono");
            //setIcon(null); // Asegurarse de que no haya ningún ícono en la celda
            controlarchivo = 1;
        }

        // Cambiar el color de fondo si la celda está seleccionada
        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        } else {
            setBackground(table.getBackground());
            setForeground(table.getForeground());
        }

        return this; // Devolver el componente renderizado
    }
};

public int setcontrol(){
    return controlarchivo;
}

}
    
