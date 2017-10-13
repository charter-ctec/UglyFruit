package ufdev2;

import javax.swing.JFrame;
import javax.swing.*;
//import net.miginfocom.swing.MigLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.*;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Window;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Component;

public class devPick
   {
	public UFDevMgr myparent;// =  new UFDevMgr();
	private JTable table;
//	public void setParrent(UFDevMgr uparent) {
//		parent = uparent;
//	}
	public void start()
	{
		JFrame f=new JFrame("Select a device");
		f.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				// Window open event
    	        Dimension windowSize = f.getSize();
	            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	            Point centerPoint = ge.getCenterPoint();

	            int dx = centerPoint.x - windowSize.width / 2;
	            int dy = centerPoint.y - windowSize.height / 2;    
	            f.setLocation(dx, dy);

				dbload();
				//hideParent();
			}
		});
		//JLabel l=new JLabel("Anurag jain(csanuragjain)");
		//f.add(l);
		f.setSize(465,439);
		SpringLayout springLayout = new SpringLayout();
		f.getContentPane().setLayout(springLayout);
		
		JScrollPane scrollPane = new JScrollPane();
		springLayout.putConstraint(SpringLayout.WEST, scrollPane, 36, SpringLayout.WEST, f.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, scrollPane, -10, SpringLayout.SOUTH, f.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, scrollPane, -33, SpringLayout.EAST, f.getContentPane());
		f.getContentPane().add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		
		springLayout.putConstraint(SpringLayout.NORTH, table, 140, SpringLayout.NORTH, f.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, table, 125, SpringLayout.WEST, f.getContentPane());
		table.setFillsViewportHeight(true);
		table.setBorder(new LineBorder(new Color(0, 0, 0)));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Device", "IP", "Type"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, String.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		springLayout.putConstraint(SpringLayout.SOUTH, table, 348, SpringLayout.NORTH, f.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, table, 411, SpringLayout.WEST, f.getContentPane());
		
		JButton btnSelect = new JButton("Select");
		springLayout.putConstraint(SpringLayout.WEST, btnSelect, 36, SpringLayout.WEST, f.getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, scrollPane, 6, SpringLayout.SOUTH, btnSelect);
		springLayout.putConstraint(SpringLayout.NORTH, btnSelect, 10, SpringLayout.NORTH, f.getContentPane());
		btnSelect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				selectdev();
			}
		});
		f.getContentPane().add(btnSelect);
		f.setVisible(true);
	}
	
	private void showParent() {
		System.out.println("Parent->" + myparent );
		myparent.show();
	}
	private  void hideParent() {
		System.out.println("Parent->" + myparent );
		myparent.hide();
	}
	
	public void dbload() {
		
        String sql = "Select devicename,remoteipaddress,MachineType from tblinv order by devicename";
        String result = "";
        ArrayList<String> alColumns = new ArrayList<String>();
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
             ResultSetMetaData rsmd = rs.getMetaData();
             Integer colcount = rsmd.getColumnCount();
             System.out.println("Extracting Column Names...");
             System.out.println("Numbers of columns in qry: " + colcount.toString());

             //build a list of column names - 
             for (int i = 1; i <= colcount; i++) {
                System.out.print("column MetaData --> ");
                System.out.print("column number: " + i + " Name: ");
      // get the column's name.
                System.out.println(rsmd.getColumnName(i));
                alColumns.add(rsmd.getColumnName(i));
             }//end of for
             
             DefaultTableModel model = new DefaultTableModel();
             model.setColumnIdentifiers(alColumns.toArray());
             table.setModel(model);
             table.setFillsViewportHeight(true);
             
             // print column header - console only
            Iterator<String> itr = alColumns.iterator();
            System.out.print("|"); // Start the line with a | 
            while(itr.hasNext()){
                    System.out.print("|" + itr.next());
                }// end of itr while
                System.out.println("||");
               
            // loop through the result set
            while (rs.next()) {
                //result=result+rs.getString("DeviceName") + "   \t[" + rs.getString("RemoteIPAddress") +  "]\n";
                Iterator<String> itrrow = alColumns.iterator();
                System.out.print("|"); // Start the line with a |    
                result=result+"||";
                
                ArrayList<String> alWalkcolumn = new ArrayList<String>();
                String[] recstring;
                while(itrrow.hasNext()){
                    String item = "";
                    item = rs.getString(itrrow.next());
                    System.out.print("|" + item);
                    result = result +  "|" + item;
                    alWalkcolumn.add(item);
                }// end of itr while
                System.out.println("||"); // finishes the line with a CRLF       
                result = result + "||\n";
                
                recstring=alWalkcolumn.toArray(new String[alWalkcolumn.size()]);
                model.addRow(recstring);
            }//end of while rs
            
            //jTextArea2.setText(result);
            System.out.println(result);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
		
	}
	
	private Connection connect() {
        // SQLite connection string
        String sqliteDBFile;
        sqliteDBFile= myparent.lblSQLDBFile.getText();
        //sqliteDBFile= "inv.sqlite";
        String url = "jdbc:sqlite:" + sqliteDBFile;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
            System.out.println("Opening DB: " + sqliteDBFile);
            //jTextArea2.setText("Opening DB: " + lblSQLDBFile.getText());
            //jTextArea2.setText("connecting ... " + lblSQLDBFile.getText());
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Error connect() DB File:" + sqliteDBFile);
            
        }
        return conn;
    }
	
	//end of connect
	
	private void selectdev() {
		//http://www.java2s.com/Tutorial/Java/0240__Swing/TableSelectionEventsandListeners.htm
		int[] selectedRow = table.getSelectedRows();
        int[] selectedColumns = table.getSelectedColumns();
        String selectedData = null;

        for (int i = 0; i < selectedRow.length; i++) {
            for (int j = 0; j < selectedColumns.length; j++) {
              selectedData = (String) table.getValueAt(selectedRow[i], selectedColumns[j]);
            }
          }
        if (selectedColumns[0] == 2) {
        	System.out.println("Cant select by machine type");
        }
        if (selectedColumns[0] == 0) {
        	System.out.println("Selected Device:" + selectedData);
        	
        	//load parent here
        	myparent.selectAll(selectedData);
        	//System.dispose();
        	//JFrame f1 = (JFrame) SwingUtilities.getRoot(this);
        	showParent();
        	Window activeWindow = javax.swing.FocusManager.getCurrentManager().getActiveWindow();
        	activeWindow.dispose();
        	}
        else {
          System.out.println("Selection by IP Address not yet built: " + selectedData);
        }
        
        
        
        
	}
	public void main(String args[])
	{
		new devPick().start();
	}
	
}


