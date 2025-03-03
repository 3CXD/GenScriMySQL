import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;

public class OpenProject {
    public static void openSQLFile(Window window) {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            parseSQLFile(selectedFile, window);
        }
    }
    
    private static void parseSQLFile(File file, Window window) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            StringBuilder sqlContent = new StringBuilder();
            
            while ((line = reader.readLine()) != null) {
                sqlContent.append(line).append("\n");
            }
            
            processSQL(sqlContent.toString(), window);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al leer el archivo SQL", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static void processSQL(String sql, Window window) {
        Pattern createTablePattern = Pattern.compile("CREATE TABLE (\\w+) \\((.*?)\\);", Pattern.DOTALL);
        Matcher matcher = createTablePattern.matcher(sql);
        
        while (matcher.find()) {
            String tableName = matcher.group(1);
            String columnsBlock = matcher.group(2);
            
            ArrayList<String> columnNames = new ArrayList<>();
            ArrayList<String> dataTypes = new ArrayList<>();
            ArrayList<String> keyTypes = new ArrayList<>();
            ArrayList<Boolean> nullConstraints = new ArrayList<>();
            ArrayList<String> foreignKeys = new ArrayList<>();
            
            String[] columns = columnsBlock.split(",");
            for (String column : columns) {
                column = column.trim();
                String[] parts = column.split(" ");
                columnNames.add(parts[0]);
                dataTypes.add(parts[1]);
                
                boolean isPrimaryKey = column.toUpperCase().contains("PRIMARY KEY");
                boolean isNullable = !column.toUpperCase().contains("NOT NULL");
                boolean isForeignKey = column.toUpperCase().contains("REFERENCES");
                
                keyTypes.add(isPrimaryKey ? "PRIMARY" : "");
                nullConstraints.add(isNullable);
                foreignKeys.add(isForeignKey ? "Sí" : "No");
            }
            
            Table newTable = new Table(tableName, columnNames, dataTypes, keyTypes, nullConstraints, foreignKeys);
            window.addTable(newTable);
        }
    }
}
