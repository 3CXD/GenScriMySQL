import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

//Lca clase Table, es para la configuración inicial de una tabla nueva
class Table extends JPanel {
    private Point initialClick;
    private JPanel mainPanel = new JPanel();
    private JPanel topPanel = new JPanel();
    private JPanel columnsPanel = new JPanel();
    private JTextField name = new JTextField("Nueva Tabla");
    private JButton newColumnButton = new JButton("Nueva Columna");
    private JScrollPane scrollPane; 
    public static ArrayList<String> keys = new ArrayList<>();
    private int numberOfColumn = 0;
    private JButton buildTable = new JButton("Construir Tabla");
	
	//Esta es la función de la clase Table, en fin, el funcionamiento principal, 
    public Table() { 
    	numberOfColumn = 0;
	    //Window.numberOfTable++;
		keys.add("NOONE");
	    ArrayList<String> columnNameList = new ArrayList<>(); //Nombre
	    ArrayList<String> columnDataTypeList = new ArrayList<>(); //Tipo de dato
	    ArrayList<String> columnKeyTypeList = new ArrayList<>(); //Tipo de llave
	    ArrayList<Boolean> columnNullOrNotList = new ArrayList<>(); //Nulo o no
	    ArrayList<String> columnForeignKeyList = new ArrayList<>(); //Perteneciente a
        setBackground(Color.GRAY);
    	name = new JTextField("NuevaTabla" + Window.numberOfTable);
    	
    	//Añadimos todos ls widgets al panel que se encuentra en la parte superior del panel de la configuración de la tabla
		topPanel.add(name);
    	topPanel.add(newColumnButton);
    	topPanel.add(buildTable);

        // Configuración de mainPanel
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(topPanel); // Se agrega primero topPanel
        mainPanel.add(Box.createVerticalStrut(10)); // Espacio vertical entre topPanel y scrollPane
		
        //Añadimos una columna, que es la que contiene las columnas a crear y sus configuraciones
        columnsPanel.setLayout(new BoxLayout(columnsPanel, BoxLayout.Y_AXIS));
        mainPanel.add(columnsPanel);
    	
    	//Establecemos que el panel sea visible
        add(mainPanel);
    	setVisible(true);
        
        //Esta función establece que se añada una nueva columna que llevará la tabla
        newColumnButton.addActionListener(new ActionListener() {
        	@Override
            public void actionPerformed(ActionEvent e) {
            	//Inicializamos todos los widgets
            	int LocalNumberOfColumn = numberOfColumn;
            	int localNumberOfPrimaryKey = Window.numberOfPrimaryKeys;
            	Window.numberOfPrimaryKeys++;
            	numberOfColumn++;
                JPanel interfacePanel = new JPanel();
					interfacePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				JCheckBox NN = new JCheckBox("NOT NULL");
				String[] columnType = {"NORMAL", "PK", "FK"};
				JComboBox<String> columnTypeList = new JComboBox<>(columnType);
				String[] dataTypes = {"NUMBER", "VARCHAR2", "DATE"};
				JComboBox<String> dataTypeList = new JComboBox<>(dataTypes); 
				JTextField columnName = new JTextField("ColumnaNueva" + LocalNumberOfColumn);
				JLabel fromTableLabel = new JLabel("Perteneciente a:");
				String[] fromTable = {"NOONE"};
                JComboBox<String> fromTableList = new JComboBox<>(fromTable);
				
				//Añadimos los widgets al panel de la configuración de la columna
				interfacePanel.add(columnName);
				interfacePanel.add(dataTypeList);
				interfacePanel.add(columnTypeList);
				interfacePanel.add(NN);
				interfacePanel.add(fromTableLabel);
                fromTableLabel.setEnabled(false);
                interfacePanel.add(fromTableList);
                fromTableList.setEnabled(false);
                
                //Añadimos datos preliminares a los ArrayLists de las columnas y sus configuraciones
                columnNameList.add(columnName.getText());
                columnDataTypeList.add("NUMBER");
                columnKeyTypeList.add("NORMAL");
	            columnNullOrNotList.add(true);
	            columnForeignKeyList.add("NOONE");

				//Esta función establece cuando una columna es una PrimaryKey, ForeignKey, o una variable normal (PK, FK, NORMAL), establce cambios como habilitar o desabilitar algunos widgets, así como añadir una nueva llave
				columnTypeList.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JComboBox cb = (JComboBox)e.getSource();
                        String selectedItem = (String)cb.getSelectedItem();
                        if ("PK".equals(selectedItem)) {
                            NN.setEnabled(false);
                            NN.setSelected(true);
                            fromTableLabel.setEnabled(false);
                            fromTableList.setEnabled(false);
                            keys.add(columnName.getText());
			                columnName.setEnabled(true);
			                dataTypeList.setEnabled(true);
			                Window.primaryKeys[localNumberOfPrimaryKey][0] = columnName.getText();//Nombre
			                Window.primaryKeys[localNumberOfPrimaryKey][1] = (String) dataTypeList.getSelectedItem();//Tipo de dato
			                Window.primaryKeys[localNumberOfPrimaryKey][2] = name.getText();//Tabla perteneciente
			                
                        } else if("FK".equals(selectedItem)){
			                Window.primaryKeys[localNumberOfPrimaryKey][0] = null;//Nombre
			                Window.primaryKeys[localNumberOfPrimaryKey][1] = null;//Tipo de dato
			                Window.primaryKeys[localNumberOfPrimaryKey][2] = null;//Tabla perteneciente
                            NN.setEnabled(false);
                            NN.setSelected(true);
                            fromTableLabel.setEnabled(true);
                            fromTableList.setEnabled(true);
                            keys.remove(columnName.getText());
                            String[] fromTableNew = new String[50];
                            for(int i=0;i<50;i++){
                            	if(Window.primaryKeys[i][0] != null){
                            			fromTableNew[i] = Window.primaryKeys[i][0] + "_FK_" + Window.primaryKeys[i][2]; 
                            	}
                            }      
                			DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(fromTableNew);
			                fromTableList.setModel(model);
			                columnName.setEnabled(false);
			                dataTypeList.setEnabled(false);
			                String selectedValue = (String) fromTableList.getSelectedItem();
			                columnName.setText(selectedValue);
        					columnNameList.set(LocalNumberOfColumn, selectedValue);
        					dataTypeList.setSelectedItem(Window.primaryKeys[0][1]);
        					columnDataTypeList.set(LocalNumberOfColumn, Window.primaryKeys[0][1]);
			            	columnForeignKeyList.set(LocalNumberOfColumn, selectedValue);
			                
                        }else if("NORMAL".equals(selectedItem)){
			                Window.primaryKeys[localNumberOfPrimaryKey][0] = null;//Nombre
			                Window.primaryKeys[localNumberOfPrimaryKey][1] = null;//Tipo de dato
			                Window.primaryKeys[localNumberOfPrimaryKey][2] = null;//Tabla perteneciente
                        	NN.setEnabled(true);
                            fromTableLabel.setEnabled(false);
                            fromTableList.setEnabled(false);
                            keys.remove(columnName.getText());
			                columnName.setEnabled(true);
			                dataTypeList.setEnabled(true);
                        }
                        columnKeyTypeList.set(LocalNumberOfColumn, selectedItem);
                    }
                });
                
                //Actualiza el valor del nombre de la columna añadiendo un "_FK_"
				fromTableList.addActionListener(new ActionListener() {
                    @Override
	                public void actionPerformed(ActionEvent e) {
                        JComboBox cb = (JComboBox)e.getSource();
                        String selectedItem = (String)cb.getSelectedItem();
			            columnName.setText(selectedItem);
			            int currentIndex = fromTableList.getSelectedIndex();
			            dataTypeList.setSelectedItem(Window.primaryKeys[currentIndex][1]);
			            columnForeignKeyList.set(LocalNumberOfColumn, selectedItem);
        				columnNameList.set(LocalNumberOfColumn, selectedItem);
        				columnDataTypeList.set(LocalNumberOfColumn, Window.primaryKeys[currentIndex][1]);
	                }
                });
				
				ActionListener updateComboBox = new ActionListener() {
				    @Override
				    public void actionPerformed(ActionEvent e) {
				        String[] fromTableNew = new String[50];
                        for(int i=0;i<50;i++){
                        	if(Window.primaryKeys[i][0] != null){
                        			fromTableNew[i] = Window.primaryKeys[i][0] + "_FK_" + Window.primaryKeys[i][2]; 
                        	}
                        }      
            			DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(fromTableNew);
		                fromTableList.setModel(model);
				    }
				};
                
                fromTableList.addPopupMenuListener(new PopupMenuListener() {
				    @Override
				    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				        // Actualizar el combo box
				        updateComboBox.actionPerformed(null);
				    }

				    @Override
				    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				        // No se necesita hacer nada aquí
				    }

				    @Override
				    public void popupMenuCanceled(PopupMenuEvent e) {
				        // No se necesita hacer nada aquí
				    }
				});
				
				
                
				// Dibuja el panel de configuración de la columna
				columnsPanel.add(interfacePanel);
				columnsPanel.revalidate();
				columnsPanel.repaint();
				mainPanel.revalidate();
				mainPanel.repaint();
            	
            	//Cada que se actualiza el valor del nombre de la columna se cambia el valor almacenado en la variable asignada con el índice local en el ArrayList de nombre de la columna
				columnName.getDocument().addDocumentListener(new DocumentListener() {
				    @Override
				    public void insertUpdate(DocumentEvent e) {
				        saveText();
				    }
				    @Override
				    public void removeUpdate(DocumentEvent e) {
				        saveText();
				    }
				    @Override
				    public void changedUpdate(DocumentEvent e) {
				        saveText();
				    }
				    private void saveText() {
				    	String savedText = columnName.getText();
        				if(Window.primaryKeys[localNumberOfPrimaryKey][0] != null){
			                Window.primaryKeys[localNumberOfPrimaryKey][0] = savedText;//Nombre
        				}
        				columnNameList.set(LocalNumberOfColumn, savedText);
				    }
				});
				
				//Se cambia el tipo de dato de la columna en la tabla
				dataTypeList.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JComboBox cb = (JComboBox)e.getSource();
        				columnDataTypeList.set(LocalNumberOfColumn, (String)cb.getSelectedItem());
        				if(Window.primaryKeys[localNumberOfPrimaryKey][0] != null){
			                Window.primaryKeys[localNumberOfPrimaryKey][1] = (String) dataTypeList.getSelectedItem();//Nombre
        				}
                    }
                });
                
                //Establece si la columna puede permanecer o no nula
                NN.addItemListener(new ItemListener() {
				    @Override
				    public void itemStateChanged(ItemEvent e) {
				        if (e.getStateChange() == ItemEvent.SELECTED) {
				            columnNullOrNotList.set(LocalNumberOfColumn, false); //si el checkbox está seleccionado, entonces no es nulo
				        } else {
				            columnNullOrNotList.set(LocalNumberOfColumn, true);
				        }
				    }
				});
            }
        });
        
        //Cuando se va a construir la tabla, hace saltar un aviso, y después crea un nuevo panel que contiene la tabla con las columnas configuradas, también eliminará el panel de configuración de la tabla
        buildTable.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(mainPanel, 
                                      "Si ha seleccionado columnas no nulas, al presionar el botón 'Nueva Fila' se mostrarán datos automáticos de ejemplo.", 
                                      "¡AVISO!", 
                                      JOptionPane.INFORMATION_MESSAGE);
                BuildedTable newTable = new BuildedTable(name.getText(), columnNameList, columnDataTypeList, columnKeyTypeList, columnNullOrNotList, columnForeignKeyList, numberOfColumn);
                newTable.setBounds(50, 50, 20*numberOfColumn, 50); // Initial position and size
                Container parent = mainPanel.getParent();
                Container parentDelParent = parent.getParent();
                parentDelParent.add(newTable);
				parentDelParent.remove(parent);
                parentDelParent.revalidate();
                parentDelParent.repaint();
                System.out.println(numberOfColumn);
			}
		});

		//Las siguientes dos funciones son para el movimiento del panel mediante su arrastre con el mouse
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
}

