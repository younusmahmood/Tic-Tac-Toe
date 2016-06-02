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

	static String name = "";

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

		private ObjectOutputStream output;
		private ObjectInputStream input;
		private Socket socket;

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
					}
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

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton clicked = (JButton) e.getSource();
		
		if(clicked == B1){
			B1.setText(name);
			B1.setEnabled(false);
			
		}
		
		
		
	}

}
