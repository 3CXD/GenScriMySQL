import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//Creación de la ventana en base a la clase Window
public class main extends JFrame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainWindow().setVisible(true);
            }
        });
    }
}

