import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;





@SuppressWarnings("serial")
public class Client extends JFrame implements Runnable {
	
	private boolean begin = false;

	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Socket socket;
	String name;

	
	private JFrame frame;
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
	private JTextArea PlayerRole;
	
	
	public Client(){
		frame = new JFrame("Tic Tac Toe");
		B1 = new JButton("X");
		B2 = new JButton("X");
		B3 = new JButton("X");
		B4 = new JButton("");
		B5 = new JButton("");
		B6 = new JButton("");
		B7 = new JButton("");
		B8 = new JButton("");
		B9 = new JButton("");
		
		panel = getContentPane();
		panel.setLayout(new GridLayout(3,3));
		
		panel.add(B1);
		panel.add(B2);
		panel.add(B3);
		panel.add(B4);
		panel.add(B5);
		panel.add(B6);
		panel.add(B7);
		panel.add(B8);
		panel.add(B9);
		
		PlayerRole = new JTextArea(0,30);
		
		frame.add(panel);
		
		frame.setSize(875, 550);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(false);
		
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	public void run() {

		try {
			socket = new Socket("localhost", 3001);
			output = new ObjectOutputStream(socket.getOutputStream());
			input = new ObjectInputStream(socket.getInputStream());
		

			while (true) {
				String inputString = input.readUTF();
				System.out.println(inputString);
				 PlayerRole.setText(inputString);
			}

		} catch (IOException EOF) {
			System.out.println("EOF: " + EOF.getMessage());

		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		Client t1 = new Client();
		
		new Thread(t1).start();
	}

}
