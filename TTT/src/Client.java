import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class Client extends JPanel implements ActionListener {

	private boolean readyToPlay = false;
	private static String whosTurn = "X"; // indicate who's turn it is

	private static ObjectOutputStream output;
	private static ObjectInputStream input;
	private Socket socket;
	String winner;
	int xScore;
	int oScore;

	String name;
	static String buttonPressed = ""; // 
	boolean ImPlayer1 = false; //will be set to true in whichever thread belongs to player 1 and false in the other thread
	boolean ImPlayer2 = false; //will be set to true in whichever thread belongs to player 1 and false in the other thread
	boolean commandReceived = false; //threads will wait for a command from server

	private static JFrame frame;
	private JButton connectBtn = new JButton("Connect to Opponent");
	private JButton helpBtn = new JButton("Help");
	private JButton tipBtn = new JButton("Tip");
	private JButton quitBtn = new JButton("Quit");


	private JTextArea playerRole = new JTextArea();
	private JTextArea playerScore = new JTextArea();
	private JButton B1 = new JButton("");
	private JButton B2 = new JButton("");
	private JButton B3 = new JButton("");
	private JButton B4 = new JButton("");
	private JButton B5 = new JButton("");
	private JButton B6 = new JButton("");
	private JButton B7 = new JButton("");
	private JButton B8 = new JButton("");
	private JButton B9 = new JButton("");
	

	public Client() {

		// setting up layout
		JPanel panel = new JPanel(new GridLayout(4, 3));
		panel.add(connectBtn);
		panel.add(playerRole);
		panel.add(playerScore);

		panel.add(B1);
		panel.add(B2);
		panel.add(B3);
		panel.add(B4);
		panel.add(B5);
		panel.add(B6);
		panel.add(B7);
		panel.add(B8);
		panel.add(B9);
		
		panel.add(helpBtn);
		panel.add(quitBtn);
		panel.add(tipBtn);

		quitBtn.setBackground(Color.BLACK);
		quitBtn.setOpaque(true);
		helpBtn.setBackground(Color.BLACK);
		helpBtn.setOpaque(true);
		tipBtn.setBackground(Color.BLACK);
		tipBtn.setOpaque(true);
		
		setLayout(new BorderLayout());
		add(panel, BorderLayout.CENTER);
		add(playerRole, BorderLayout.SOUTH);
		add(connectBtn, BorderLayout.NORTH);
		add(playerScore, BorderLayout.WEST);
		

		playerRole.setPreferredSize(new Dimension(300, 75));
		playerRole.setEditable(false);
		connectBtn.setPreferredSize(new Dimension(300, 30));
		

		connectBtn.addActionListener(this);
		B1.addActionListener(this);
		B2.addActionListener(this);
		B3.addActionListener(this);
		B4.addActionListener(this);
		B5.addActionListener(this);
		B6.addActionListener(this);
		B7.addActionListener(this);
		B8.addActionListener(this);
		B9.addActionListener(this);
		
		B1.setEnabled(false);
		B2.setEnabled(false);
		B3.setEnabled(false);
		B4.setEnabled(false);
		B5.setEnabled(false);
		B6.setEnabled(false);
		B7.setEnabled(false);
		B8.setEnabled(false);
		B9.setEnabled(false);

	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		playerRole.setText("");
		if (source == connectBtn) {
			try {
				connectBtn.setEnabled(false);

				ClientThread t1 = new ClientThread();

				new Thread(t1).start();

			} catch (Exception ex) {
				System.out.println("ERROR: " + ex.getMessage());
			}
		}

		if (source == B1) {
			buttonPressed = "B1";
			B1.setText(whosTurn);
			B1.setEnabled(false);
		} else if (source == B2) {
			buttonPressed = "B2";
			B2.setText(whosTurn);
			B2.setEnabled(false);
			
		} else if (source == B3) {
			buttonPressed = "B3";
			B3.setText(whosTurn);
			B3.setEnabled(false);
			
		} else if (source == B4){
			buttonPressed = "B4";
			B4.setText(whosTurn);
			B4.setEnabled(false);
			
		} else if (source == B5) {
			buttonPressed = "B5";
			B5.setText(whosTurn);
			B5.setEnabled(false);

		} else if (source == B6) {
			buttonPressed = "B6";
			B6.setText(whosTurn);
			B6.setEnabled(false);

		} else if (source == B7) {
			buttonPressed = "B7";
			B7.setText(whosTurn);
			B7.setEnabled(false);

		} else if (source == B8) {
			buttonPressed = "B8";
			B8.setText(whosTurn);
			B8.setEnabled(false);

		} else if (source == B9) {
			buttonPressed = "B9";
			B9.setText(whosTurn);
			B9.setEnabled(false);
		}

	}

	public class ClientThread implements Runnable {

		public void run() {
			try {
				// set up connections
				socket = new Socket("localhost", 3001);
				output = new ObjectOutputStream(socket.getOutputStream());
				input = new ObjectInputStream(socket.getInputStream());

				
				while (!readyToPlay) {
					String inputString = input.readUTF();
					System.out.println(inputString);
					playerRole.setText(inputString);
					if (inputString.startsWith("Player")) {
						name = inputString.substring(7);
						frame.setTitle("Welcome to Tic Tac Toe: Player " + name);

						if (name.equals("X")) {
//							System.out.println("IMx should be true now -> " + name);
							frame.setTitle("Welcome to Tic Tac Toe: Player " + name);
							ImPlayer1 = true;
//							System.out.println("also, I'm player1 = " + ImPlayer1);
							
						}
						if (name.equals("O")) {
//							System.out.println("IMo should be true now -> " + name);
							frame.setTitle("Welcome to Tic Tac Toe: Player " + name);
							ImPlayer2 = true;
//							System.out.println("also, I'm player2 = " + ImPlayer2);
						}
						playerRole.setText("Welcome to Tic Tac Toe: Player " + name);
						readyToPlay = true;
					}
				}

				try {
				String chatInput;
				String[] serverCommands = {"F", "F"};
		        while ((chatInput = input.readUTF()) != null) {
		        	
		        	if(chatInput.startsWith("Game")){
		        		winner  = chatInput.substring(18,19);
		        		if(winner.equals("X")){
		        			xScore++;
		        		}else if(winner.equals("O")){
		        			oScore++;
		        		}
		        		
		        		playerScore.setText("Player X : " + xScore + " " + "Player O : " + oScore);
		        		
		        	}
		        	
		        	if(chatInput.startsWith("Cat's")){
		        		playerScore.setText("Player X : " + xScore + " " + "Player O : " + oScore);
		        	}
		        	
		        	if (chatInput.contains(" ")) {
		        		serverCommands = chatInput.split(" ");
		        		whosTurn = serverCommands[2];
		        		System.out.println("server says: Player " + serverCommands[0] + " pressed " + serverCommands[1]);
		        		commandReceived = true;
		        	}
		        	if (chatInput.contains("Begin")) {
		        		System.out.println("server says: " + chatInput);
		        		whosTurn = "X";
		        	}
		        	System.out.println("It is Player " + whosTurn + "'s turn.");
		        	if (whosTurn.equals("X")) {
		        		
		        		if (ImPlayer1) { // its my turn: need to make a move, send to server, wait for my turn again
		        			updateBoard(serverCommands[1], serverCommands[0]);
		        			commandReceived = false;		        			

		        			playerRole.setText("Your move :)");
		        			enableButtons();
							while (buttonPressed.equals("")) {
								Thread.sleep(2000);
							}
//							while (buttonPressed.equals(""));
							System.out.println(buttonPressed +" Pressed! Sending to Server.");
							output.writeUTF(buttonPressed);
							output.flush();
							buttonPressed = "";
							disableButtons();
							Thread.sleep(2000);
		        		}
		        		else if (ImPlayer2 && commandReceived) { // not my turn: need to listen for command from server, update my board, then make a move? 
		        			System.out.println("Updating board. Setting " + serverCommands[1] + " to " + serverCommands[0]);
		        			updateBoard(serverCommands[1], serverCommands[0]);
		        			commandReceived = false;		        			
		        		}
		        		else {
		        			System.out.println(ImPlayer1 + " or " + ImPlayer2 + " and I'm here");
		        		}
		        	}
		        	
		        	else if (whosTurn.equals("O")) {
		        		
		        		if (ImPlayer1) { // not my turn: need to listen for command from server, update my board, then make a move?
		        			System.out.println("Updating board");
		        			updateBoard(serverCommands[1], serverCommands[0]);
		        			commandReceived = false;		        			
		        		}
		        		else if (ImPlayer2) { // its my turn: need to make a move, send to server, wait for my turn again
		        			updateBoard(serverCommands[1], serverCommands[0]);
		        			commandReceived = false;		        			

		        			playerRole.setText("Your move :)");
		        			enableButtons();
							while (buttonPressed.equals("")) {
								Thread.sleep(2000);
							}
//							while (buttonPressed.equals(""));
							System.out.println(buttonPressed +" Pressed! Sending to Server.");
							output.writeUTF(buttonPressed);
							output.flush();
							buttonPressed = "";
							disableButtons();
							Thread.sleep(2000);
		        		}

		        	}
		        	System.out.println("Round finished.");
		        }

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 

			} catch (EOFException eof) {
				System.out.println("EOF: " + eof.getMessage());

			} catch (IOException exception) {

				playerRole.setText("Error: " + exception.getMessage());
				System.out.println(exception.getMessage());
				connectBtn.setEnabled(true);

			}finally {
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		public void updateBoard(String buttonToUpdate, String setTextTo) {

			if (buttonToUpdate.equals("F")) { // first round
				// do nothing
			} else {
				System.out.println("Updating board. Setting " + buttonToUpdate + " to " + setTextTo);
				if (buttonToUpdate.equals("B1")) {
					B1.setText(setTextTo);
					B1.setEnabled(false);
				}
				else if (buttonToUpdate.equals("B2")) {
					B2.setText(setTextTo);
					B2.setEnabled(false);
				}
				else if (buttonToUpdate.equals("B3")) {
					B3.setText(setTextTo);
					B3.setEnabled(false);
				}
				else if (buttonToUpdate.equals("B4")) {
					B4.setText(setTextTo);
					B4.setEnabled(false);
				}
				else if (buttonToUpdate.equals("B5")) {
					B5.setText(setTextTo);
					B5.setEnabled(false);
				}
				else if (buttonToUpdate.equals("B6")) {
					B6.setText(setTextTo);
					B6.setEnabled(false);
				}
				else if (buttonToUpdate.equals("B7")) {
					B7.setText(setTextTo);
					B7.setEnabled(false);
				}
				else if (buttonToUpdate.equals("B8")) {
					B8.setText(setTextTo);
					B8.setEnabled(false);
				}
				else if (buttonToUpdate.equals("B9")) {
					B9.setText(setTextTo);
					B9.setEnabled(false);
				}

			}

		}
		
		private boolean playAgain(String winner) {
			int response = JOptionPane.showConfirmDialog(frame,
					"The winner is Player " + winner + "!!!!", "Want to rematch?",
					JOptionPane.YES_NO_OPTION);
			frame.dispose();
			return response == JOptionPane.YES_OPTION;
		}
		
		public void enableButtons() {			
			if (B1.getText().equals("")) {
				B1.setEnabled(true);
			} if (B2.getText().equals("")) {
				B2.setEnabled(true);
			} if (B3.getText().equals("")) {
				B3.setEnabled(true);
			} if (B4.getText().equals("")) {
				B4.setEnabled(true);
			} if (B5.getText().equals("")) {
				B5.setEnabled(true);
			} if (B6.getText().equals("")) {
				B6.setEnabled(true);
			} if (B7.getText().equals("")) {
				B7.setEnabled(true);
			} if (B8.getText().equals("")) {
				B8.setEnabled(true);
			} if (B9.getText().equals("")) {
				B9.setEnabled(true);
			}
		}

		public void disableButtons() {			
			if (B1.getText().equals("")) {
				B1.setEnabled(false);
			} if (B2.getText().equals("")) {
				B2.setEnabled(false);
			} if (B3.getText().equals("")) {
				B3.setEnabled(false);
			} if (B4.getText().equals("")) {
				B4.setEnabled(false);
			} if (B5.getText().equals("")) {
				B5.setEnabled(false);
			} if (B6.getText().equals("")) {
				B6.setEnabled(false);
			} if (B7.getText().equals("")) {
				B7.setEnabled(false);
			} if (B8.getText().equals("")) {
				B8.setEnabled(false);
			} if (B9.getText().equals("")) {
				B9.setEnabled(false);
			}
		}
		

	}


	public static void main(String[] args) throws Exception {
		frame = new JFrame("Tic Tac Toe Online");
		frame.add(new Client());
		frame.setSize(500, 500);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(true);
	}

}
