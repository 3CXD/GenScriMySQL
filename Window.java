import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

//Esta es la clase de la ventana en sí
public class Window extends JFrame {
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
	
	//Esta es la dunción de la ventana
    public Window() {
    	//Establecemos la configuración básica para la ventana
        setTitle("GenScriMySQL - Nuevo Proyecto");
        setSize(screenSize.width, screenSize.height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//Creamos el panel principal
        mainPanel = new JPanel(null);
        mainPanel.setLayout(new BorderLayout());
		
		//Creamos el primer botón y un ArrayList para las tablas
        newTableButton = new JButton("Crear nueva tabla");
        listOfTables = new ArrayList<>();

		//Añadimos los botones al panel que se encuentra en la parte posterior de la ventana
        JPanel topPanel = new JPanel();
        topPanel.add(newTableButton);
        topPanel.add(generateSQL);

		//Creamos el panel para el area de dseño, el cual contiene las configuraciones de las tablas, así como también las tablas ya generadas
		JPanel designAreaPanel = new JPanel();

		//Añadimos los dos paneles anteriormente mencionados al panel principal
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(designAreaPanel, BorderLayout.CENTER);

		//Establecemos que el sea visible
        add(mainPanel);
        setVisible(true);
        
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
}

