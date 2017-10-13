package ufdev2;



import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.io.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;

//import threadstesting.MyTask;

import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class jRun 
{
	public UFDevMgr myparent;
	public void start()
	{
		JFrame form=new JFrame("jRun");
		form.addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				// Window open event
    	        Dimension windowSize = form.getSize();
	            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	            Point centerPoint = ge.getCenterPoint();

	            int dx = centerPoint.x - windowSize.width / 2;
	            int dy = centerPoint.y - windowSize.height / 2;    
	            form.setLocation(dx, dy);
	            getDevInfo();
				//dbload();
				//hideParent();
			}
		});
		form.setSize(845,540);
		
		JLabel lblId = new JLabel("ID:");
		
		lblDevid = new JLabel("DevID");
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		JLabel lblDevice = new JLabel("Device:");
		
		JLabel lblIp = new JLabel("IP:");
		
		lblDevname = new JLabel("DevName");
		
		lblRemoteipaddress = new JLabel("RemoteIPAddress");
		
		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"--- Select Command ---", "show run", "show version", "show interface description"}));
		
		JButton btnRunNow = new JButton("Run Now");
		btnRunNow.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//run rcmd threaded
				if (comboBox.getSelectedIndex() == 0) {
					JOptionPane.showMessageDialog(null,"Please select a command to run");
				}
				else {
				ltask();
				} //end else
			}
		});
		
		JLabel lblType = new JLabel("Type:");
		
		lblMachinetype = new JLabel("MachineType");
		
		JLabel lblStatus = new JLabel("Status:");
		
		lblCurstatus = new JLabel("n/a");
		GroupLayout groupLayout = new GroupLayout(form.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(21)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblId)
						.addComponent(lblIp)
						.addComponent(lblType))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblDevid)
						.addComponent(lblRemoteipaddress)
						.addComponent(lblMachinetype))
					.addPreferredGap(ComponentPlacement.RELATED, 324, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnRunNow))
					.addGap(49))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 803, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblDevice)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblDevname)))
					.addContainerGap())
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblStatus)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblCurstatus)
					.addContainerGap(743, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblId)
								.addComponent(lblDevid))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblDevice)
								.addComponent(lblDevname))
							.addGap(12)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblIp)
								.addComponent(lblRemoteipaddress)))
						.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblType)
						.addComponent(lblMachinetype)
						.addComponent(btnRunNow))
					.addGap(22)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblStatus)
						.addComponent(lblCurstatus))
					.addGap(18)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 296, GroupLayout.PREFERRED_SIZE)
					.addGap(23))
		);
		
		txtOutput = new JTextArea();
		txtOutput.setLineWrap(true);
		txtOutput.setText("");
		scrollPane.setViewportView(txtOutput);
		form.getContentPane().setLayout(groupLayout);
		form.setVisible(true);
	}
	
	void ltask() {
		try {

			//runCmd();
			
			MyTask process = new MyTask();
			process.myparent = myparent;
			process.rfrm = this;
	        try {
	          process.execute();
	        } catch (Exception e) {
	          e.printStackTrace();
	        }//end catch
	    	} //end of try
	    	catch (Exception e) {
	    		System.out.println(e.getMessage());
	    		e.printStackTrace();    		
	    	}
	}//end of ltask
	
	
	protected String readProcessOutput(Process p) throws Exception{
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String response = "";
        String line;
        while ((line = reader.readLine()) != null) {
            response += line+"\n";
        	//response += line;
        }
        reader.close();
        return response;
    }
	

	
	private void runCmd() throws InterruptedException {
	
	}// end of run cmd
	
	
	private void getDevInfo() {
		
    String sql = "Select rowid, devicename,remoteipaddress,MachineType from tblinv where rowid = " + myparent.lblLbdid.getText() + " order by devicename";
    System.out.println("Query to run against is: " + sql);
    String result = "";
    ArrayList<String> alColumns = new ArrayList<String>();
    try (Connection conn = this.connect();
         Statement stmt  = conn.createStatement();
         ResultSet rs    = stmt.executeQuery(sql))
    	{
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
         
         //update screen from db
         rs.next();
         System.out.println("Found: " + rs.getString("DeviceName"));
         lblDevname.setText(rs.getString("DeviceName"));
         lblDevid.setText(rs.getString("rowid"));
         lblRemoteipaddress.setText(rs.getString("RemoteIPAddress"));
         lblMachinetype.setText(rs.getString("MachineType"));
         //lblDevname.setText("ouch");
         
    	}//end of try
    catch (SQLException e) {
        System.out.println(e.getMessage());
    }
    
	}//end of getdevinfo
	
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
		
	public static void main(String args[])
	{
		new jRun().start();
	}

	//WindowBuilder doesnt define these - netbeans does!
protected JLabel lblDevname;
protected JLabel lblDevid;
protected JLabel lblRemoteipaddress;
protected JLabel lblMachinetype;
protected JLabel lblCurstatus;
protected javax.swing.JTextArea txtOutput; 
protected javax.swing.JComboBox comboBox;
}

class MyTask extends SwingWorker {
	  UFDevMgr myparent;
	  jRun rfrm;
	  protected Object doInBackground() throws Exception {
		  Integer retresult=0;

	      try {
	    	//System.out.println("Worker Active");
	    	  
	  		String cmd = (String) rfrm.comboBox.getSelectedItem();
			String stordir = "jrunstor";
			if (cmd.equals("show run")) {
				stordir = "jrunstore/cfg";
			}
			
			if (cmd.equals("show version")) {
				stordir = "jrunstore/shver";
			}
			if (cmd.equals("show interface description")) {
				stordir = "jrunstore/shidesc";
			}
			
			// Original working command had path to python.exe
			String[] commandAndArguments = {"cmd","/C","python.exe","h:/speterman/pyfiles/rcmd/rc.py", "1", rfrm.lblRemoteipaddress.getText(), rfrm.lblDevname.getText(), stordir, cmd};
			
			
			//String[] commandAndArguments = {"cmd","/C","python.exe","h:/speterman/pyfiles/rcmd/rc.py"};
			//String[] commandAndArguments = {"cmd","/C","dir"};
			rfrm.lblCurstatus.setText("Running cmd... please wait..");
			int result = JOptionPane.showConfirmDialog( null, "Run Command?",
	                "alert", JOptionPane.OK_CANCEL_OPTION);
			if (result ==0) {

	    	try {
	    		
	    		//Thread.sleep(1000);
	    		//python: subprocess.call(["/1-ULA~1/Apps/Python27/python-2.7.10/python.exe", "/pyfiles/rcmd/rc.py", "1", row['RemoteIPAddress'], row['DeviceName'], "cfg", 'show run'])
		        Runtime runtime = Runtime.getRuntime();     //getting Runtime object
		        
		        Process process = runtime.exec(commandAndArguments);        //opens "sample.txt" in notepad
		        
		        String listing = rfrm.readProcessOutput(process);
		        System.out.println(listing);
		        rfrm.txtOutput.setText(listing);
		        rfrm.lblCurstatus.setText("Command Completed");
		        System.out.println("Command Completed");
		        //Thread.sleep(5000);
		        //process.destroy();
	    	} //end of try
	    	catch (Exception e)
	        {
	    		System.out.println(e.getMessage());
	            e.printStackTrace(); 
	        } //end catch
			}//end if
			else {
				rfrm.lblCurstatus.setText("cmd ... cancelled");
				
			}

	    	  
	    //run command here
	        Thread.sleep(5000);
	      } catch (Exception e) {
	        e.printStackTrace();
	      }
	    
	    return retresult;
	  }
}


