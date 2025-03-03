import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

// --------------------------------------------------
// Clase para manejar la edición del archivo SQL
class ProyectoEditor extends JFrame {
    
    public ProyectoEditor(String contenido) {
        setTitle("Editor SQL");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JTextArea textArea = new JTextArea(contenido);
        JScrollPane scrollPane = new JScrollPane(textArea);
        
        add(scrollPane);
    }
}
