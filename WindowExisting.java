import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.regex.*;

//Esta es la clase de la ventana en sí
public class WindowExisting extends JFrame {
    private JPanel mainPanel;
    private JButton newTableButton;
    private ArrayList<Table> listOfTables;
    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private JButton generateSQL = new JButton("Generar Código SQL");
    public static int numberOfTable = 0;
    public static ArrayList<ArrayList<ArrayList<String>>> AllData = new ArrayList<>();
    public static ArrayList<ArrayList<ArrayList<String>>> AllColumnsWithConditions = new ArrayList<>();
    public static ArrayList<String> AllTables = new ArrayList<>();
    public static int maxNumberOfColumns = 0;
    public static int numberOfPrimaryKeys = 0;
    public static String[][] primaryKeys = new String[50][3];

	String columnName;
	String dataType;
	String nullable;
	String constraint;



	
	//Esta es la dunción de la ventana
    public WindowExisting() {


        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecciona un archivo SQL");
        int seleccion = fileChooser.showOpenDialog(this);


		//Añadimos los botones al panel que se encuentra en la parte posterior de la ventana
		JPanel topPanel = new JPanel();

		//Creamos el panel para el area de dseño, el cual contiene las configuraciones de las tablas, así como también las tablas ya generadas
		JPanel designAreaPanel = new JPanel();

		//Establecemos la configuración básica para la ventana
        setTitle("GenScriMySQL - Edición de Proyecto");
        setSize(screenSize.width, screenSize.height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//Creamos el panel principal
        mainPanel = new JPanel(null);
        mainPanel.setLayout(new BorderLayout());
		
		//Creamos el primer botón y un ArrayList para las tablas
        newTableButton = new JButton("Crear nueva tabla");
        listOfTables = new ArrayList<>();

		//Añadimos los botones al panel que se encuentra en la parte posterior de la ventana
        topPanel.add(newTableButton);
        topPanel.add(generateSQL);

		//Añadimos los dos paneles anteriormente mencionados al panel principal
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(designAreaPanel, BorderLayout.CENTER);

		//Establecemos que el sea visible
        add(mainPanel);
        setVisible(true);

        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivoSQL = fileChooser.getSelectedFile();
			
            StringBuilder contenido = new StringBuilder();
			try (BufferedReader br = new BufferedReader(new FileReader(archivoSQL))) {
				String linea;
				while ((linea = br.readLine()) != null) {
					contenido.append(linea).append("\n");
				}
			} catch (IOException e) {
				JOptionPane.showMessageDialog(this, "Error al leer el archivo", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Dividir las instrucciones SQL por ";"
			String[] statements = contenido.toString().split(";");

			for (String statement : statements) {
				statement = statement.trim(); // Eliminar espacios en blanco

				if (statement.isEmpty()) continue;

				// Clasificar la instrucción con un switch
				switch (getSQLType(statement)) {
					case "CREATE_TABLE":
						System.out.println("Se detectó una creación de tabla: \n" + statement);

						Table newTable = new Table();
						newTable.setBounds(50, 50, 100, 100);
						listOfTables.add(newTable);
						designAreaPanel.add(newTable);
						designAreaPanel.revalidate();
						designAreaPanel.repaint();

						int iteracionDeseada = 1; // Aquí puedes definir la iteración que deseas obtener
						String[] columnData = extractColumnAtIteration(statement, iteracionDeseada);

						if (columnData != null) {
							System.out.println("Columna en la iteración " + iteracionDeseada + ":");
							System.out.println("  Nombre: " + columnData[0]);
							System.out.println("  Tipo de dato: " + columnData[1]);
							System.out.println("  Nulabilidad: " + columnData[2]);
							System.out.println("  Restricción: " + columnData[3]);
						} else {
							System.out.println("No se encontró la iteración deseada.");
						}
						break;



					case "INSERT":
						System.out.println("Se detectó una inserción de datos: \n" + statement);
						break;

					case "FOREIGN_KEY":
						System.out.println("Se detectó una clave foránea: \n" + statement);
						break;

					default:
						System.out.println("Instrucción no reconocida: \n" + statement);
						break;
				}

				System.out.println("---------------------------------------------------");
			}
        }

    	
        
        //Este botón genera un nuevo panel de configuración de una tabla
        newTableButton.addActionListener(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent e) {
                Table newTable = new Table();
                newTable.setBounds(50, 50, 100, 100); // Initial position and size
                listOfTables.add(newTable);
                designAreaPanel.add(newTable);
                designAreaPanel.revalidate();
                designAreaPanel.repaint();
            }
        });
        
        //Genera el código SQL
        generateSQL.addActionListener(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent e) {
            	//Obtenemos todos los nombres de las tablas
				String[] AllTablesArray = AllTables.toArray(new String[0]);
				
				//Obtenemos todas las columnas y sus condiciones (si es nulo o no, tipo de dato, etc...) de todas las tablas
				String[][][] AllColumnsWithConditionsArray = new String[numberOfTable][maxNumberOfColumns][5];

				for (int i = 0; i < numberOfTable; i++) {
					ArrayList<ArrayList<String>> table = AllColumnsWithConditions.get(i);
					int dimension2alpha = AllColumnsWithConditions.get(i).size();
					for (int j = 0; j < dimension2alpha; j++) {
						ArrayList<String> column = table.get(j);
						int dimension3alpha = AllColumnsWithConditions.get(i).get(j).size();
						for (int k = 0; k < dimension3alpha; k++) {
							AllColumnsWithConditionsArray[i][j][k] = column.get(k);
							System.out.println("Guardando columnas["+i+"]["+j+"]["+k+"]..."+AllColumnsWithConditionsArray[i][j][k]);
						}
					}
				}
				
				//Obtenemos todos los datos de todas las tablas
				String[][][] AllDataArray = new String[numberOfTable][100][maxNumberOfColumns];

				for (int i = 0; i < numberOfTable; i++) {
					ArrayList<ArrayList<String>> table = AllData.get(i);
					int dimension2beta = AllData.get(i).size();
					for (int j = 0; j < dimension2beta; j++) {
						ArrayList<String> column = table.get(j);
						int dimension3beta = AllData.get(i).get(j).size();
						for (int k = 0; k < dimension3beta; k++) {
							AllDataArray[i][j][k] = column.get(k);
							System.out.println("Guardando datos["+i+"]["+j+"]["+k+"]..."+AllDataArray[i][j][k]);
						}
					}
				}
				
				int MaxNumberOfRows = 0;
				//Generación de prueba
				for(int i=0;i<numberOfTable; i++){
					System.out.println(AllTablesArray[i]);
					for(int j=0;j<maxNumberOfColumns; j++){
						System.out.print("    "+AllColumnsWithConditionsArray[i][j][0]+" (");
						for(int k=1;k<5; k++){
							System.out.print(AllColumnsWithConditionsArray[i][j][k]+", ");
						}
						System.out.println(")");
						int dimension2beta = AllData.get(i).size();
						MaxNumberOfRows = dimension2beta;
						for(int k=0;k<dimension2beta;k++){
							System.out.println("        "+AllDataArray[i][k][j]);
						}
					} 
				}
				
				//Generación del código SQL
				StringBuilder sqlContent = new StringBuilder();
				for(int i=0;i<numberOfTable; i++){
					sqlContent.append("CREATE TABLE "+AllTablesArray[i]+" (\n");
					for(int j=0;j<maxNumberOfColumns; j++){
						if(AllColumnsWithConditionsArray[i][j][0] != null){
							if(j > 0){
								sqlContent.append(",\n");
							}
							String temporalInput = "";
							switch(AllColumnsWithConditionsArray[i][j][2]){
								case "NORMAL":
									temporalInput="";
								break;
								case "PK":
									temporalInput="PRIMARY KEY";
								break;
							}
							String AnotherTemporalInput = "";
							switch(AllColumnsWithConditionsArray[i][j][3]){
								case "NULL":
									AnotherTemporalInput="";
								break;
								case "NOT NULL":
									AnotherTemporalInput="NOT NULL";
								break;
							}
							
							sqlContent.append("    "+AllColumnsWithConditionsArray[i][j][0]+" "+AllColumnsWithConditionsArray[i][j][1]);
							if(AllColumnsWithConditionsArray[i][j][1].equals("VARCHAR2")){
								sqlContent.append("(50)");
							}
							sqlContent.append(" "+AnotherTemporalInput+" "+temporalInput);
							
							int index = 0;
							if(AllColumnsWithConditionsArray[i][j][2] == "FK"){
								if(j > 0){
									sqlContent.append(",\n");
								}
								for(int a=0;a<50;a++){
									if(AllColumnsWithConditionsArray[i][j][4].equals(primaryKeys[a][0] + "_FK_" + primaryKeys[a][2])){
										index = a;
										break;
									}
								}
								//FOREIGN KEY (CustomerID) REFERENCES Customers(CustomerID)
								sqlContent.append("    FOREIGN KEY ("+AllColumnsWithConditionsArray[i][j][0]+") REFERENCES "+primaryKeys[index][2]+"("+primaryKeys[index][0]+")");
							}
							
							
							
							
							/*for(int k=1;k<5; k++){
								System.out.print(AllColumnsWithConditionsArray[i][j][k]+", ");
							}
							System.out.println(")");
							int dimension2beta = AllData.get(i).size();
							for(int k=0;k<dimension2beta;k++){
								System.out.println("        "+AllDataArray[i][k][j]);
							}*/
						}
						
						
					}
					
					sqlContent.append("\n);\n\n");
					for(int a =0;a<MaxNumberOfRows;a++){
						sqlContent.append("INSERT INTO "+AllTablesArray[i]+" (");
						for(int j=0;j<maxNumberOfColumns; j++){
							if(AllColumnsWithConditionsArray[i][j][0] != null){
								if(j > 0){
									sqlContent.append(", ");
								}
								sqlContent.append(AllColumnsWithConditionsArray[i][j][0]);
							}
						}
						sqlContent.append(") VALUES (");
						for(int k=0;k<maxNumberOfColumns; k++){
							if(AllDataArray[i][a][k] != null){
								if(k > 0){
									sqlContent.append(", ");
								}
								switch(AllColumnsWithConditionsArray[i][k][1]){
									case "NUMBER":
										sqlContent.append(AllDataArray[i][a][k]);
									break;
									case "VARCHAR2":
										sqlContent.append("'"+AllDataArray[i][a][k]+"'");
									break;
									case "DATE":
										sqlContent.append("TO_DATE('"+AllDataArray[i][a][k]+"', 'DD-MM-YYYY')");
									break;
								}
							}
						}
					sqlContent.append(");\n");
					}
				}
				// Ruta del archivo SQL
				String filePath = "output.sql";

				// Escribir el contenido SQL al archivo
				try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
				    writer.write(sqlContent.toString());
				    System.out.println("Archivo SQL escrito exitosamente en: " + filePath);
				} catch (IOException d) {
				    System.err.println("Error al escribir el archivo SQL: " + d.getMessage());
				}
				
		        //Cerramos la ventana
		        JOptionPane.showMessageDialog(mainPanel, 
		                                  "Se geneneró de manera correcta el código SQL con el nombre 'output.sql', se cerrará la aplicación.", 
		                                  "Código generado correctamente", 
		                                  JOptionPane.INFORMATION_MESSAGE);
				System.exit(0);
		        }

				
            
        });
    }

	private void mostrarContenidoSQL(File archivo) {
        
    }

    // Función para determinar el tipo de instrucción SQL
    private static String getSQLType(String statement) {
        statement = statement.toUpperCase(); // Convertir a mayúsculas para evitar problemas de case-sensitive

        if (statement.startsWith("CREATE TABLE")) {
			/*Table newTable = new Table();
			newTable.setBounds(50, 50, 100, 100); // Initial position and size
			listOfTables.add(newTable);
			designAreaPanel.add(newTable);
			designAreaPanel.revalidate();
			designAreaPanel.repaint();
			*/
            return "CREATE_TABLE";
        } else if (statement.startsWith("INSERT INTO")) {
            return "INSERT";
        } else if (statement.contains("FOREIGN KEY")) {
            return "FOREIGN_KEY";
        } else {
            return "UNKNOWN";
        }
    }

	private String[] extractColumnAtIteration(String tableStatement, int iteration) {
		Pattern columnPattern = Pattern.compile(
			"(\\w+)\\s+(\\w+\\(\\d+\\)|\\w+)(?:\\s+(NOT NULL|NULL))?(?:\\s+(PRIMARY KEY|UNIQUE|CHECK|DEFAULT\\s+\\S+|AUTO_INCREMENT))?",
			Pattern.CASE_INSENSITIVE
		);

		Matcher matcher = columnPattern.matcher(tableStatement);
		int currentIteration = 0;

		while (matcher.find()) {
			if (currentIteration == iteration) {
				if(matcher.group(1).equals("CREATE")){}else{
					String columnName = matcher.group(1);
					String dataType = matcher.group(2);
					String nullable = matcher.group(3) != null ? matcher.group(3) : "NULL";
					String constraint = matcher.group(4) != null ? matcher.group(4) : "";

					return new String[]{columnName, dataType, nullable, constraint};

				}
			}
			currentIteration++;
		}
		
		return null; // Retorna null si no se encuentra la iteración especificada
	}


}