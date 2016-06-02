import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class Client extends JFrame implements ActionListener {

	private boolean begin = false;
	static ObjectOutputStream output;
	static ObjectInputStream input;
	static PrintWriter out;
	static BufferedReader in;
	static Socket socket;
	static String name = "";

	static JFrame frame;
	private Container panel;
	private JButton B1;
	private JButton B2;
	private JButton B3;
	private JButton B4;
	private JButton B5;
	private JButton B6;
	private JButton B7;
	private JButton B8;
	private JButton B9;

	public Client() {
		frame = new JFrame("Tic Tac Toe " + name);
		B1 = new JButton("");
		B2 = new JButton("");
		B3 = new JButton("");
		B4 = new JButton("");
		B5 = new JButton("");
		B6 = new JButton("");
		B7 = new JButton("");
		B8 = new JButton("");
		B9 = new JButton("");

		B1.addActionListener(this);
		B2.addActionListener(this);
		B3.addActionListener(this);
		B4.addActionListener(this);
		B5.addActionListener(this);
		B6.addActionListener(this);
		B7.addActionListener(this);
		B8.addActionListener(this);
		B9.addActionListener(this);

		panel = getContentPane();
		panel.setLayout(new GridLayout(3, 3, 2, 2));
		// role = new JLabel(name);
		panel.add(B1);
		panel.add(B2);
		panel.add(B3);
		panel.add(B4);
		panel.add(B5);
		panel.add(B6);
		panel.add(B7);
		panel.add(B8);
		panel.add(B9);
		// panel.add(role);

		frame.add(panel);
		frame.setSize(875, 550);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

	}

	public static void main(String[] args) {
		TicTacToeThread client = new TicTacToeThread();

		new Thread(client).start();
		new Client();
	}

	static class TicTacToeThread implements Runnable {



		@Override
		public void run() {
				try {
					socket = new Socket("localhost", 3001);
					output = new ObjectOutputStream(socket.getOutputStream());
					input = new ObjectInputStream(socket.getInputStream());
					
					
				while (true) {
					String inputString = input.readUTF();
					System.out.println(inputString);

					if (inputString.startsWith("Player")) {
						name = inputString.substring(7);
						System.out.println(name);
						frame.setTitle("Welcome to Tic Tac Toe: Player "+ name);
					}
				}
				
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton clicked = (JButton) e.getSource();
		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			out = new PrintWriter(socket.getOutputStream(), true);
			while (true){
			if(clicked == B1){
				out.println("1 " + name + " " + "B1");
			}else if(clicked == B2){
				out.println("2" + name + " " + "B1");
			}else if(clicked == B3){
				out.println("3" + name + " " + "B1");
			}else if(clicked == B4){
				out.println("4" + name + " " + "B1");
			}else if(clicked == B5){
				out.println("5" + name + " " + "B1");
			}else if(clicked == B6){
				out.println("6" + name + " " + "B1");
			}else if(clicked == B7){
				out.println("7" + name + " " + "B1");
			}else if(clicked == B8){
				out.println("8" + name + " " + "B1");
			}else if(clicked == B9){
				out.println("9" + name + " " + "B1");
			}
			
			String command = in.readLine();
			String button = command.substring(2);
			String currentPlayer = command.substring(0, 1);
			
			if(command.contains("1")){
				B1.setText(currentPlayer);
				B1.setEnabled(false);
			}else if(command.contains("2")){
				B2.setText(currentPlayer);
				B2.setEnabled(false);
			}else if(command.contains("3")){
				B3.setText(currentPlayer);
				B3.setEnabled(false);
			}else if(command.contains("4")){
				B4.setText(currentPlayer);
				B4.setEnabled(false);
			}else if(command.contains("5")){
				B5.setText(currentPlayer);
				B5.setEnabled(false);
			}else if(command.contains("6")){
				B6.setText(currentPlayer);
				B6.setEnabled(false);
			}else if(command.contains("7")){
				B7.setText(currentPlayer);
				B7.setEnabled(false);
			}else if(command.contains("8")){
				B8.setText(currentPlayer);
				B8.setEnabled(false);
			}else if(command.contains("9")){
				B9.setText(currentPlayer);
				B9.setEnabled(false);
			}
				
				
			}
			
			
			
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}finally{
			try {
				socket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		
		
		
		
	
		
		
	}

}
