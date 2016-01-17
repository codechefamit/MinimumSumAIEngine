import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.text.MaskFormatter;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JList;

public class ClientFrame extends Thread{

	private JFrame frame;
	private JLabel lblCard2,lblCard1,lblCards,lblCard3,lblCard4,lblCard5;
	private Socket clientSocket = null;
	private static PrintStream os = null;
	private static BufferedReader is = null;
	private JLabel lblConnect,lblClientName;
	private JTextField txtIP;
	private JButton btnConnect;
	private JLabel lblPort;
	private JFormattedTextField txtPort;
	private JLabel lblName;
	private JTextField txtName;
	private  JLabel lblStatus;
	private JLabel lblCardFace;
	private JLabel lblDiscardPile;
	private String status;
	private JButton btnPlay;
	private JList<String> stList;
	private DefaultListModel<String> listModel;
	private List<String> fromOthers,myStuff;
	private Bot bot;

	public ClientFrame(Bot b) {
		bot=b;
		initialize();
	}

	private void initialize() {
		setFrame(new JFrame("Minimum Sum - Amit Kothiyal"));
		getFrame().setResizable(false);
		getFrame().getContentPane().setForeground(Color.BLACK);
		getFrame().getContentPane().setBackground(Color.LIGHT_GRAY);
		getFrame().setBounds(100, 100, 450, 475);
		getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getFrame().getContentPane().setLayout(null);
		
		lblClientName = new JLabel("Name");
		lblClientName.setVisible(false);
		
		fromOthers=new ArrayList<String>();
		myStuff=new ArrayList<String>();
		lblCard2 = new JLabel("Card2");
		lblCard2.setBounds(65, 100, 80, 120);
		lblCard2.setIcon(new ImageIcon(getClass().getResource("res/130.jpg")));
		lblCard2.setVisible(false);
		
		lblCard3 = new JLabel("Card3");
		lblCard3.setBounds(90, 100, 80, 120);
		lblCard3.setIcon(new ImageIcon(getClass().getResource("res/120.jpg")));
		lblCard3.setVisible(false);
		
		lblCard4 = new JLabel("Card4");
		lblCard4.setBounds(115, 100, 80, 120);
		lblCard4.setIcon(new ImageIcon(getClass().getResource("res/110.jpg")));
		lblCard4.setVisible(false);
		
		lblCard5 = new JLabel("Card5");
		lblCard5.setBounds(140, 100, 80, 120);
		lblCard5.setIcon(new ImageIcon(getClass().getResource("res/100.jpg")));
		lblCard5.setVisible(false);
		getFrame().getContentPane().add(lblCard5);
		getFrame().getContentPane().add(lblCard4);
		getFrame().getContentPane().add(lblCard3);
		getFrame().getContentPane().add(lblCard2);		
		lblClientName.setForeground(Color.BLACK);
		lblClientName.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblClientName.setBounds(310, 11, 110, 19);
		getFrame().getContentPane().add(lblClientName);
		
		lblCard1 = new JLabel("Card1");
		lblCard1.setBounds(40, 100, 80, 120);
		lblCard1.setIcon(new ImageIcon(getClass().getResource("res/10.jpg")));
		lblCard1.setVisible(false);
		getFrame().getContentPane().add(lblCard1);
		
		lblCards = new JLabel("Your Cards :");
		lblCards.setForeground(Color.BLACK);
		lblCards.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblCards.setBounds(30, 70, 100, 19);
		getFrame().getContentPane().add(lblCards);
		
		lblConnect = new JLabel("IP :");
		lblConnect.setForeground(Color.BLACK);
		lblConnect.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblConnect.setBounds(30, 45, 32, 19);
		getFrame().getContentPane().add(lblConnect);
		
		txtIP = new JTextField();
		txtIP.setFont(new Font("Tahoma", Font.BOLD, 13));
		txtIP.setBounds(65, 45, 125, 20);
		getFrame().getContentPane().add(txtIP);
		txtIP.setColumns(10);
		
		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name=txtName.getText().trim();
				String ip=txtIP.getText().trim();
				int port=Integer.parseInt(txtPort.getText());
				try {
				      clientSocket = new Socket(ip,port);
				      os = new PrintStream(clientSocket.getOutputStream());
				      is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				    } catch (UnknownHostException e1) {
				      System.err.println("Don't know about host " + ip);
						lblStatus.setText("Status : Don't know about host "+ ip);
				    } catch (IOException e2) {
				      System.err.println("Couldn't get I/O for the connection to the host "+ ip);
						lblStatus.setText("Status : Couldn't get I/O for the connection to the host "+ ip);
				    }
				try{
				btnConnect.setEnabled(false);
				txtName.setEditable(false);
				txtPort.setEditable(false);
				txtIP.setEditable(false);
				lblClientName.setText(name);
				lblClientName.setVisible(true);
				status="Status : Connected";
				lblStatus.setText(status);
				os.println(name);
				new Minion().execute();
			}catch(Exception e1){
				lblStatus.setText("Not Connected");
			}
			}
		});
		btnConnect.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnConnect.setBounds(320, 45, 100, 20);
		getFrame().getContentPane().add(btnConnect);
		
		lblPort = new JLabel("Port :");
		lblPort.setForeground(Color.BLACK);
		lblPort.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblPort.setBounds(197, 45, 46, 19);
		getFrame().getContentPane().add(lblPort);
		
		try {
			txtPort = new JFormattedTextField(new MaskFormatter("####"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		txtPort.setFont(new Font("Tahoma", Font.BOLD, 13));
		txtPort.setBounds(245, 45, 66, 20);
		getFrame().getContentPane().add(txtPort);
		
		lblName = new JLabel("Name :");
		lblName.setForeground(Color.BLACK);
		lblName.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblName.setBounds(30, 16, 60, 19);
		getFrame().getContentPane().add(lblName);
		
		txtName = new JTextField();
		txtName.setFont(new Font("Tahoma", Font.BOLD, 13));
		txtName.setColumns(10);
		txtName.setBounds(90, 17, 100, 20);
		getFrame().getContentPane().add(txtName);
		
		lblStatus=new JLabel("Status : Not Connected");
		lblStatus.setForeground(Color.BLACK);
		lblStatus.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblStatus.setBounds(254, 401, 180, 19);
		getFrame().getContentPane().add(lblStatus);
		
		lblCardFace = new JLabel("");
		lblCardFace.setBounds(310, 100, 80, 120);
		getFrame().getContentPane().add(lblCardFace);
		
		lblDiscardPile = new JLabel("Discard Pile :");
		lblDiscardPile.setForeground(Color.BLACK);
		lblDiscardPile.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblDiscardPile.setBounds(295, 70, 100, 19);
		getFrame().getContentPane().add(lblDiscardPile);
		
		JLabel lblYourMove = new JLabel("Your Move : ");
		lblYourMove.setForeground(Color.BLACK);
		lblYourMove.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblYourMove.setBounds(20, 250, 100, 19);
		getFrame().getContentPane().add(lblYourMove);
		
		btnPlay = new JButton("Play");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String move = null;
				move=bot.getMove(myStuff,fromOthers);
				myStuff.clear();
				fromOthers.clear();
				if(move.equals("0")){
					btnPlay.setEnabled(false);
					os.println("0");
					new Minion().execute();
				}
				else{
				os.println(move);
				btnPlay.setEnabled(false);
				lblCard1.setIcon(null);
		 		lblCard1.setText(null);
		 		lblCard1.setVisible(false);
		 		lblCard2.setIcon(null);
		 		lblCard2.setText(null);
		 		lblCard2.setVisible(false);
		 		lblCard3.setIcon(null);
		 		lblCard3.setText(null);
		 		lblCard3.setVisible(false);
		 		lblCard4.setIcon(null);
		 		lblCard4.setText(null);
		 		lblCard4.setVisible(false);
		 		lblCard5.setIcon(null);
		 		lblCard5.setText(null);
		 		lblCard5.setVisible(false);
		 		new Minion().execute();
				}
			}
		});
		btnPlay.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnPlay.setBounds(65, 296, 125, 40);
		btnPlay.setEnabled(false);
		getFrame().getContentPane().add(btnPlay);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(254, 267, 180, 123);
		getFrame().getContentPane().add(scrollPane);
		

		listModel=new DefaultListModel<String>();
		stList = new JList<String>(listModel);
		stList.setVisibleRowCount(8);
		stList.setAutoscrolls(true);
		stList.setFont(new Font("Segoe UI", Font.ITALIC, 11));
		scrollPane.setViewportView(stList);
		
		JLabel lblTicker = new JLabel("Ticker : ");
		lblTicker.setForeground(Color.BLACK);
		lblTicker.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblTicker.setBounds(250, 235, 60, 19);
		getFrame().getContentPane().add(lblTicker);
		
		JLabel label = new JLabel("Developed By : Amit Kothiyal | Copyright (C), All rights reserved");
		label.setForeground(Color.DARK_GRAY);
		label.setFont(new Font("Segoe UI", Font.ITALIC, 13));
		label.setBounds(45, 425, 360, 19);
		frame.getContentPane().add(label);
	}
		
	
public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}


private class Minion extends SwingWorker<List<String>,String>{
	
	protected void done() {
		  List<String> status;
		    try {
		    int ch=1;
		     status = get();
		     if(status==null){
		    	 	lblStatus.setText("Status : Game End");
					listModel.addElement("<-----Round----->");
					stList.ensureIndexIsVisible(stList.getModel().getSize()-1);
					new Minion().execute();
		     }
		     else{
		     for(String str:status){
		    	 if(str.contains("Bye")){
		    		 lblStatus.setText("Status : Eliminated");
		    	 }
		    	 else if(str.startsWith("faceUp")){
		    		 String parts[]=str.split("-");
			    	 Card cd=Card.convertToCard(parts[1]);
			    	 String url="res/"+cd.getValue()+cd.getSuit()+".jpg";
			    	lblCardFace.setText(""+cd);
			 		lblCardFace.setIcon(new ImageIcon(getClass().getResource(url)));
		    	 }
		     else{
	    	 Card cd=Card.convertToCard(str);
	    	 String url="res/"+cd.getValue()+cd.getSuit()+".jpg";
	    	 switch(ch){
	    	 case 1:
			 		lblCard1.setIcon(new ImageIcon(getClass().getResource(url)));
			 		lblCard1.setText(""+cd);
			 		lblCard1.setVisible(true);
	    		 break;
	    	 case 2:
	    		 lblCard2.setIcon(new ImageIcon(getClass().getResource(url)));
	    		 lblCard2.setText(""+cd);
	    		 lblCard2.setVisible(true);
	    		 break;
	    	 case 3:
	    		 lblCard3.setIcon(new ImageIcon(getClass().getResource(url)));
	    		 lblCard3.setText(""+cd);
	    		 lblCard3.setVisible(true);
	    		 break;
	    	 case 4:
	    		 lblCard4.setIcon(new ImageIcon(getClass().getResource(url)));
	    		 lblCard4.setText(""+cd);
	    		 lblCard4.setVisible(true);
	    		 break;
	    	 case 5:
	    		 lblCard5.setIcon(new ImageIcon(getClass().getResource(url)));
	    		 lblCard5.setText(""+cd);
	    		 lblCard5.setVisible(true);
	    		 break;
	    	 }
	    	 ch++;
		     }
		     }
		     if(lblStatus.getText().contains("Your")){
		     btnPlay.setEnabled(true);
		     myStuff.addAll(status);
		     Thread.sleep(100);
		     btnPlay.doClick();
		     }
		     else
		     {
		    	 new Minion().execute();
		     }
		    }
		     }
		    catch (InterruptedException e) {
		    	System.err.println("Exception:  " + e);
		    } catch (ExecutionException e) {
		    	System.err.println("Exception:  " + e);
		    }
	}
	
	protected void process(List<String> chunks) {
		for(String s:chunks){
			if(s.startsWith("<")){
				if(s.contains("turn") || s.contains("Game")){
				lblStatus.setText("Status : "+s);
				}
				if(s.contains("Your"))
					myStuff.add(s);
				else
					fromOthers.add(s);
				listModel.addElement(s);
				stList.ensureIndexIsVisible(stList.getModel().getSize()-1);
			}
		}
	}

	protected List<String> doInBackground() throws Exception {
		 String responseLine = null;
		List<String> list = new ArrayList<String>();
		 while ((responseLine = is.readLine()) != null) {
			 if(responseLine.startsWith("<"))
				 publish(responseLine);
			else if(responseLine.equals("round")){
				list=null;
				 break;
			}
			 else if(responseLine.equals("end"))
                 break;
			else if(responseLine.equals("Bye")){
				list.clear();
				list.add(responseLine);
				break;
			}
			else if(!responseLine.startsWith("<"))
		    	 	list.add(responseLine);
		 }
		return list;
	}
	
}

}

