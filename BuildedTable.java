import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

//Clase de la tabla a construir
class BuildedTable extends JPanel {
    private Point initialClick;
    private JPanel mainPanel = new JPanel();
    private JPanel topPanel = new JPanel();
    private JLabel NameOfTableBuilded;
    private JButton newRow = new JButton("Nueva Fila");
    private JButton saveTable = new JButton("Guardar Tabla");
    private int localNumberOfTable;

	//Función de la tabla construida, agrega lso valores que se le han ingresado desde la clase Table
    public BuildedTable(String TableName,
    	ArrayList<String> Name,
	    ArrayList<String> DataType,
	    ArrayList<String> KeyType,
	    ArrayList<Boolean> Null,
	    ArrayList<String> ForeignKey,
	    int NumberOfColumns) {
	    
	    //Establecemos el valor máximo de columnas
	    if(Window.maxNumberOfColumns < NumberOfColumns){
	    	Window.maxNumberOfColumns = NumberOfColumns;
	    }
	    
	    //Da un índice predefinido para la tabla
	    localNumberOfTable = Window.numberOfTable;
	    Window.numberOfTable++;
	    
	    //Establece el nombre de la tabla
	    NameOfTableBuilded = new JLabel(TableName);
	    
	    //Establece datos predefinidos para las tablas, y valores vacíos para los ArrayList globales (los cuales contienen toda la información)
	    Window.AllTables.add("");
    	String[] dataTypes = DataType.toArray(new String[0]);
    	boolean[] nullOrNot = convertToArray(Null); 
        Object[] rowData = new Object[NumberOfColumns];
        ArrayList<ArrayList<String>> Lista2D = new ArrayList<>();
        for(int i=0;i<NumberOfColumns;i++){
        	ArrayList<String> Lista1D = new ArrayList<>();
        	for(int j=0;j<5;j++){
        		Lista1D.add("");
        	}
        	Lista2D.add(Lista1D);
        	String input = "";
        	if(!nullOrNot[i]){
		    	switch(dataTypes[i]){
		    		case "NUMBER":
		    			input = "0";
		    		break;
		    		case "VARCHAR2":
		    			input = "TEXT";
		    		break;
		    		case "DATE":
		    			input = "29-12-2004";
		    		break;
		    	}
        	}
            rowData[i] = input;
        }
        Window.AllColumnsWithConditions.add(Lista2D);
        ArrayList<ArrayList<String>> Lista2Daaa = new ArrayList<>();
        
        for(int i=0;i<NumberOfColumns;i++){
        	ArrayList<String> Lista1D = new ArrayList<>();
        	for(int j=0;j<100;j++){
        		Lista1D.add("");
        	}
        	Lista2Daaa.add(Lista1D);
        }
        Window.AllData.add(Lista2Daaa);
        
        //Generamos las columnas que va a tener la tabla, y con un objeto para filas vacío
	    ArrayList<String> columnNames = new ArrayList<>();
        columnNames = Name;
        Object[][] data = {
        };

		//Generamos y añadimos la tabla a la ventana
        String[] columnNamesNew = columnNames.toArray(new String[0]); 
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNamesNew);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        setBackground(Color.GRAY);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        add(mainPanel);
    	mainPanel.add(topPanel);
    	topPanel.add(NameOfTableBuilded);
    	scrollPane.setPreferredSize(new Dimension(150*NumberOfColumns, 150));
		scrollPane.setMinimumSize(new Dimension(100, 50));
		scrollPane.setMaximumSize(new Dimension(1000, 1000));

		//Establecemos que los paneles sean visibles
        mainPanel.add(scrollPane);
        topPanel.add(newRow);
        topPanel.add(saveTable);
    	setVisible(true);
    	
    	//Esta acción añade una nueva fila a la tabla
    	newRow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                tableModel.addRow(rowData);
                mainPanel.revalidate();
                mainPanel.repaint();
			}
		});
    	
    	//Guarda toda la información de la tabla en los ArrayLists globales
    	saveTable.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Window.AllTables.set(localNumberOfTable, TableName);
				ArrayList<ArrayList<String>> Lista2D2 = new ArrayList<>();
				for(int i=0; i<NumberOfColumns; i++){
					ArrayList<String> Lista1D = new ArrayList<>();
					Lista1D.add(Name.get(i));
					Lista1D.add(DataType.get(i));
					Lista1D.add(KeyType.get(i));
					if(nullOrNot[i]){
						Lista1D.add("NULL");
					}else{
						Lista1D.add("NOT NULL");
					}
					Lista1D.add(ForeignKey.get(i));
					Lista2D2.add(Lista1D);
				}
				Window.AllColumnsWithConditions.set(localNumberOfTable, Lista2D2);
				getTableData(localNumberOfTable, table);
			}
		});

		//Las siguientes dos funciones son para el movimiento del panel guiado por el mouse
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
                getComponentAt(initialClick);
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                int thisX = getLocation().x;
                int thisY = getLocation().y;
                
                int xMoved = e.getX() - initialClick.x;
                int yMoved = e.getY() - initialClick.y;

                int X = thisX + xMoved;
                int Y = thisY + yMoved;
                setLocation(X, Y);
            }
        });
        
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
    
    //Esta función conviete un ArrayList de booleanos a Array (necesario para la conversión del ArrayList "Null")
	public static boolean[] convertToArray(ArrayList<Boolean> booleanList) {
	    boolean[] booleanArray = new boolean[booleanList.size()];
	    for (int i = 0; i < booleanList.size(); i++) {
	        booleanArray[i] = booleanList.get(i);
	    }
	    return booleanArray;
	}
	
	//Obtiene toda la iformación de la tabla y la guarda en el ArrayList global "AllData"
	public static void getTableData(int local, JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int rowCount = model.getRowCount();
        int columnCount = model.getColumnCount();

		ArrayList<ArrayList<String>> tableDataList = new ArrayList<>();

		for (int i = 0; i < rowCount; i++) {
			ArrayList<String> rowDataList = new ArrayList<>();
			
			for (int j = 0; j < columnCount; j++) {
				rowDataList.add(model.getValueAt(i, j).toString());
			}
			
			tableDataList.add(rowDataList);
		}
		Window.AllData.set(local, tableDataList);


    }
}

