import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.text.MaskFormatter;

import java.awt.Font;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;

import javax.swing.JFormattedTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class ServerFrame {

	private JFrame frame;
	private JFormattedTextField txtPlayers,txtPort;
	private JButton btnStart;
	private ServerSocket serverSocket = null;
	private  Socket clientSocket = null;
	private int maxClientsCount,port;
	private  ClientThread[] threads;
	private  Turn turn;
	private  SumGame sg;
	private JLabel lblStatus;
	private JLabel lblCon,lblCopy;
	private Thread server;
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerFrame window = new ServerFrame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ServerFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("@Amit Kothiyal");
		frame.setResizable(false);
		frame.setBounds(100, 100, 256, 260);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblGameServer = new JLabel("Game Server");
		lblGameServer.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblGameServer.setBounds(69, 11, 102, 19);
		frame.getContentPane().add(lblGameServer);
		
		JLabel lblPlayers = new JLabel("Players :");
		lblPlayers.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblPlayers.setBounds(10, 41, 75, 19);
		frame.getContentPane().add(lblPlayers);
		
		JLabel lblPort = new JLabel("Port :");
		lblPort.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblPort.setBounds(35, 71, 50, 19);
		frame.getContentPane().add(lblPort);
		
		try {
			txtPlayers = new JFormattedTextField(new MaskFormatter("#"));
			txtPort = new JFormattedTextField(new MaskFormatter("####"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		txtPlayers.setFont(new Font("Tahoma", Font.BOLD, 13));
		txtPlayers.setBounds(95, 41, 120, 22);
		frame.getContentPane().add(txtPlayers);
		
		txtPort.setFont(new Font("Tahoma", Font.BOLD, 13));
		txtPort.setBounds(95, 68, 120, 22);
		frame.getContentPane().add(txtPort);
		
		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
				maxClientsCount=Integer.parseInt(txtPlayers.getText());
				port=Integer.parseInt(txtPort.getText());
				if(port<2000 || maxClientsCount<=1 || maxClientsCount>5){
					JOptionPane.showMessageDialog(null, "Port number must be greater than or equal to 2000. Max limit for players is 5");
				}
				else{
				threads=new ClientThread[maxClientsCount];
				turn=new Turn(maxClientsCount);
				sg=new SumGame(maxClientsCount);	
				lblStatus.setText("Status : Running");
				lblCon.setText("Players Connected : 0");
				btnStart.setEnabled(false);
				txtPort.setEditable(false);
				txtPlayers.setEditable(false);
				server=new Thread(new GameServer());	
				server.start();
				}
			}
		});
		btnStart.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnStart.setBounds(135, 101, 95, 35);
		frame.getContentPane().add(btnStart);
		
		lblStatus = new JLabel("Status : Not Running");
		lblStatus.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblStatus.setBounds(10, 160, 220, 19);
		frame.getContentPane().add(lblStatus);
		
		lblCon = new JLabel("Players Connected : ");
		lblCon.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblCon.setBounds(10, 188, 220, 19);
		frame.getContentPane().add(lblCon);
		
		lblCopy = new JLabel("Developed By : Amit Kothiyal | Copyright (C), All rights reserved");
		lblCopy.setFont(new Font("Segoe UI", Font.ITALIC, 9));
		lblCopy.setBounds(5, 210, 240, 19);
		frame.getContentPane().add(lblCopy);
	}
	
	class GameServer implements Runnable {

		public void run() {
			 try {
			      serverSocket = new ServerSocket(port);
			    } catch (IOException e) {
			      System.out.println(e);
			    }
			    int count=0;
			    while (count<maxClientsCount) {
			      try {
			        clientSocket = serverSocket.accept();
			        int i = 0;
			        for (i = 0; i < maxClientsCount; i++) {
			          if (threads[i] == null) {
			            threads[i] = new ClientThread(clientSocket, threads,turn,count+1,sg);
			            count++;
			            lblCon.setText("Players Connected : "+count);
			            break;
			          }
			        }   
			      } catch (IOException e) {
			        System.out.println(e);
			      }
			    }
		        for (int i = 0; i < maxClientsCount; i++) 
			            threads[i].start();
		        lblCon.setText("Status : Game Started");
		}
		
	}
}
