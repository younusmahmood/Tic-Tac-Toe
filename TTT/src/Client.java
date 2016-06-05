import java.awt.BorderLayout;
import java.awt.Dimension;
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

	boolean btn = false;

	private boolean gameOver = false;
	private static String whosTurn = "X"; // indicate who's turn it is
	private static boolean move = true;

	private static ObjectOutputStream output;
	private static ObjectInputStream input;
	private Socket socket;

	String name;
	static String buttonPressed = "";
	boolean ImPlayer1 = false;
	boolean ImPlayer2 = false;
	boolean commandReceived = false;
	boolean nextRound = false;

	private static JFrame frame;
	private JButton connectBtn = new JButton("Connect to Opponent");

	private JTextArea playerRole = new JTextArea();
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

		JPanel panel = new JPanel(new GridLayout(4, 3));

		panel.add(connectBtn);
		panel.add(playerRole);
		panel.add(B1);
		panel.add(B2);
		panel.add(B3);
		panel.add(B4);
		panel.add(B5);
		panel.add(B6);
		panel.add(B7);
		panel.add(B8);
		panel.add(B9);

		setLayout(new BorderLayout());
		add(panel, BorderLayout.CENTER);
		add(playerRole, BorderLayout.SOUTH);
		add(connectBtn, BorderLayout.NORTH);

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
		btn = true;
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
		} else if (source == B2) {
			buttonPressed = "B2";
			B2.setText(whosTurn);
		} else if (source == B3) {
			buttonPressed = "B3";
			B3.setText(whosTurn);
		} else if (source == B4){
			buttonPressed = "B4";
			B4.setText(whosTurn);
		} else if (source == B5) {
			buttonPressed = "B5";
			B5.setText(whosTurn);
		} else if (source == B6) {
			buttonPressed = "B6";
			B6.setText(whosTurn);
		} else if (source == B7) {
			buttonPressed = "B7";
			B7.setText(whosTurn);
		} else if (source == B8) {
			buttonPressed = "B8";
			B8.setText(whosTurn);
		} else if (source == B9) {
			buttonPressed = "B9";
			B9.setText(whosTurn);
		}

	}

	public class ClientThread implements Runnable {

		public void run() {
			try {
				socket = new Socket("localhost", 3001);
				output = new ObjectOutputStream(socket.getOutputStream());
				input = new ObjectInputStream(socket.getInputStream());

				while (!gameOver) {
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
						nextRound = true;
						break;
					}
				}

				try {
				String chatInput;
				String[] serverCommands = {"F", "F"};
		        while ((chatInput = input.readUTF()) != null) {
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
							Thread.sleep(2000);
		        		}

		        	}
		        	System.out.println("Round finished.");
		        }
				String turn;
				String[] previousMove;

					while (true) {
						System.out.println("AM I IN HERE?!");
						if (nextRound = true) {
							nextRound = false;
							System.out.println("It is Player " + whosTurn + "'s turn.");
							
							if (whosTurn.equals("X")) {
								
								if (ImPlayer1 == true) {
									System.out.println("Player X's thread. Your Turn.");
									playerRole.setText("Your Move");
									while (buttonPressed.equals("")) {
										Thread.sleep(2000);
									}
									
									while (buttonPressed.equals(""));
									System.out.println("Button Pressed!" + buttonPressed);
									output.writeUTF(buttonPressed);
									output.flush();
									buttonPressed = "";
									Thread.sleep(2000);
								}
								else if (ImPlayer2 == true) {
									System.out.println("Player O's thread. NOT YOUR TURN.");
							        while ((turn = input.readUTF()) != null) {
							        	System.out.println("server says: " + turn);
										previousMove = turn.split(" ");
										System.out.println(previousMove[0] + " " + previousMove[1] + " " + previousMove[2]);
										updateBoard(previousMove[1], previousMove[0]);
										whosTurn = previousMove[2];
										if (previousMove[3].equals("true")) nextRound = true;
										break;
							        }

								}
							}
							else if (whosTurn.equals("O")) { // player 2's turn
								
								if (ImPlayer1 == true) {
									System.out.println("Player X's thread. NOT YOUR TURN.");
							        while ((turn = input.readUTF()) != null) {
							        	System.out.println("server says: " + turn);
										previousMove = turn.split(" ");
										System.out.println(previousMove[0] + " " + previousMove[1] + " " + previousMove[2]);
										updateBoard(previousMove[1], previousMove[0]);
										whosTurn = previousMove[2];
										if (previousMove[3].equals("true")) nextRound = true;
										break;
							        }
//									Thread.sleep(2000);
//									turn = input.readUTF();
//									System.out.println("received from server: " + turn);
//									previousMove = turn.split(" ");
//									System.out.println(previousMove[0] + " " + previousMove[1] + " " + previousMove[2]);
//									updateBoard(previousMove[1], previousMove[0]);
//									playerRole.setText("YOUR MOVE");
//									whosTurn = previousMove[2];
//									if (previousMove[3].equals("true")) nextRound = true;
//									Thread.sleep(2000);
								}
								else if (ImPlayer2 == true) {
									System.out.println("Player O's thread. Your Turn.");
									playerRole.setText("Your Move");
									while (buttonPressed.equals("")) {
										Thread.sleep(2000);
									}
									while (buttonPressed.equals(""));
									System.out.println("Button Pressed!" + buttonPressed);
									output.writeUTF(buttonPressed);
									output.flush();
									buttonPressed = "";
									Thread.sleep(2000);
								}
							}
						}
						

//						System.out.println("server returned: " + whosTurn + " "
//								+ buttonPressed);

						if (buttonPressed == "B1" && B1.getText() == "") {
							B1.setText(whosTurn);
							B1.setEnabled(false);
							move = !move;
						} else if (buttonPressed == "B2" && B2.getText() == "") {
							B2.setText(whosTurn);
							B2.setEnabled(false);
							move = !move;
						} else if (buttonPressed == "B3" && B3.getText() == "") {
							B3.setText(whosTurn);
							B3.setEnabled(false);
							move = !move;
						} else if (buttonPressed == "B4" && B4.getText() == "") {
							B4.setText(whosTurn);
							B4.setEnabled(false);
							move = !move;	
						} else if (buttonPressed == "B5" && B5.getText() == "") {
							B5.setText(whosTurn);
							B5.setEnabled(false);
							move = !move;
						} else if (buttonPressed == "B6"&& B6.getText() == "") {
							B6.setText(whosTurn);
							B6.setEnabled(false);
							move = !move;
						} else if (buttonPressed == "B7" && B7.getText() == "") {
							B7.setText(whosTurn);
							B7.setEnabled(false);
							move = !move;
						} else if (buttonPressed == "B8" && B8.getText() == "") {
							B8.setText(whosTurn);
							B8.setEnabled(false);
							move = !move;
						} else if (buttonPressed == "B9" && B9.getText() == "") {
							B9.setText(whosTurn);
							B9.setEnabled(false);
							move = !move;
						}

						//repaintButtons();
						
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
		
		/**
		 * enable/disable buttons depending on who's turn
		 */
		public void enableButtons() {

			B1.setEnabled(true);
			B2.setEnabled(true);
			B3.setEnabled(true);
			B4.setEnabled(true);
			B5.setEnabled(true);
			B6.setEnabled(true);
			B7.setEnabled(true);
			B8.setEnabled(true);
			B9.setEnabled(true);

		}

		public void disableButtons() {

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
		
		public void repaintButtons() {

			B1.revalidate();
			B1.repaint();
			
			B2.revalidate();
			B2.repaint();

			B3.revalidate();
			B3.repaint();
			
			B4.revalidate();
			B4.repaint();

			B5.revalidate();
			B5.repaint();

			B6.revalidate();
			B6.repaint();
			
			B7.revalidate();
			B7.repaint();
			
			B8.revalidate();
			B8.repaint();
			
			B9.revalidate();
			B9.repaint();
		}


	}

	public static void playGame() throws Exception {

	}


	public static void main(String[] args) throws Exception {
		frame = new JFrame("Tic Tac Toe Online");
		frame.add(new Client());
		frame.setSize(500, 500);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(true);
		// playGame();
	}

}
