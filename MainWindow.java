import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.*;

public class MainWindow extends JFrame {
    
    public MainWindow() {
        setTitle("GenScriMySQL - Inicio");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        JButton btnAbrir = new JButton("Abrir Proyecto");
        JButton btnNuevo = new JButton("Nuevo Proyecto");

        btnAbrir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirProyecto();
            }
        });

        btnNuevo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nuevoProyecto();
            }
        });

        add(btnAbrir);
        add(btnNuevo);
    }

    private Window window;
    private WindowExisting windowExisting;

    private void abrirProyecto() {
        new WindowExisting();
        /*
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecciona un archivo SQL");
        int seleccion = fileChooser.showOpenDialog(this);

        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivoSQL = fileChooser.getSelectedFile();
            mostrarContenidoSQL(archivoSQL);
        }
        */
    }

    private void nuevoProyecto() {
        new Window();
    }
    
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));
    }
}