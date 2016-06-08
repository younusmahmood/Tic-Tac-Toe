/*
 * Welcome To Tic-Tac-Toe!
 * Author: Younus Mahmood
 * Client.Java
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class Client extends JPanel implements ActionListener {

	Timer timer;
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
	boolean ImPlayer1 = false; // will be set to true in whichever thread belongs to player 1 and false in the other thread
	boolean ImPlayer2 = false; // will be set to true in whichever thread belongs to player 1 and false in the other thread
	boolean commandReceived = false; // threads will wait for a command from server

	private static JFrame frame;
	private JButton connectBtn = new JButton("Connect to Opponent");
	private JButton helpBtn = new JButton("Help");
	private JButton tipBtn = new JButton("Tip");
	private JButton quitBtn = new JButton("Quit");

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

	List<JButton> btnList = new ArrayList<JButton>();

	public Client() {

		// setting up layout
		JPanel panel = new JPanel(new GridLayout(4, 3));

		btnList.add(B1);
		btnList.add(B2);
		btnList.add(B3);
		btnList.add(B4);
		btnList.add(B5);
		btnList.add(B6);
		btnList.add(B7);
		btnList.add(B8);
		btnList.add(B9);

		B1.setName("B1");
		B2.setName("B2");
		B3.setName("B3");
		B4.setName("B4");
		B5.setName("B5");
		B6.setName("B6");
		B7.setName("B7");
		B8.setName("B8");
		B9.setName("B9");

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

		playerRole.setPreferredSize(new Dimension(300, 75));
		playerRole.setEditable(false);
		connectBtn.setPreferredSize(new Dimension(300, 30));

		connectBtn.addActionListener(this);
		helpBtn.addActionListener(this);
		quitBtn.addActionListener(this);
		tipBtn.addActionListener(this);
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

	/*
	 * This Method handles all actions performed. For instance, when a Button is pressed
	 * A flag is set so the server can see which button was pressed
	 * and make a move on their.
	 * 
	 * Buttons Included: Connect Button, Actual pieces, and help/tip/quit buttons which all
	 * have unique functionalities
	 */
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		playerRole.setText("");

		tipBtn.setText("Tip");
		if (source == connectBtn) {
			try {
				connectBtn.setEnabled(false);

				ClientThread t1 = new ClientThread();

				new Thread(t1).start();

			} catch (Exception ex) {
				System.out.println("ERROR: " + ex.getMessage());
			}
		}

		if (source == helpBtn) {
			JOptionPane
					.showMessageDialog(
							null,
							"The object of Tic Tac Toe is to get three in a row.\n "
									+ "You play on a three by three game board. The first player is known as X\n "
									+ "and the second is O. Players alternate placing Xs and Os on the game board\n "
									+ "until either oppent has three in a row or all nine squares are filled.\n",
							null, 1);
		}

		if (source == tipBtn) {
			if (B1.getText() == "") {
				tipBtn.setText("1");
			} else if (B5.getText() == "") {
				tipBtn.setText("5");
			} else if (B7.getText() == "") {
				tipBtn.setText("7");
			} else if (B4.getText() == "") {
				tipBtn.setText("4");
			} else if (B6.getText() == "") {
				tipBtn.setText("6");
			} else if (B2.getText() == "") {
				tipBtn.setText("2");
			} else if (B8.getText() == "") {
				tipBtn.setText("8");
			} else if (B9.getText() == "") {
				tipBtn.setText("9");
			} else if (B3.getText() == "") {
				tipBtn.setText("3");
			}
		}

		if (source == quitBtn) {
			System.exit(0);
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

		} else if (source == B4) {
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
				//Sets up connections with the client and server
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
							frame.setTitle("Welcome to Tic Tac Toe: Player " + name);
							ImPlayer1 = true;
						}
						if (name.equals("O")) {
							frame.setTitle("Welcome to Tic Tac Toe: Player " + name);
							ImPlayer2 = true;
						}
						playerRole.setText("Welcome to Tic Tac Toe: Player " + name);
						readyToPlay = true;
					}
				}

				/*
				 * Now we start to communicate more with the server and see what it sends us
				 * Each move made by the client is first sent to the server
				 * The server takes the move, and checks all cases, and sends back information
				 * to both of the clients. The clients then take the moves and place them
				 * on their boards, and so on so forth.
				 * 
				 * Once the server responds with a game won or tied, the client takes that information
				 * and deals with it by responding in a message box letting the clients know who won the game.
				 * 
				 * Other functionalities in this run method include: Timer, and score
				 * 
				 * The timer: You have 15 seconds to make a move, once you exceed the limit; a move is made for you
				 * Score: After each game, the respected winner is incremented in their score and is output to the
				 * client screens
				 */
				
				try {
					String chatInput;
					String[] serverCommands = { "F", "F" };
					while ((chatInput = input.readUTF()) != null) {
						if (chatInput.equals("Game over: Player X wins!!")) {
							winner = chatInput.substring(18, 19);
							xScore += 1;
							whosTurn.equals("X");

							int result = JOptionPane.showConfirmDialog(null,
									"Player X Wins!!", null,
									JOptionPane.DEFAULT_OPTION);
							System.exit(0);
						} else if (chatInput.equals("Game over: Player O wins!!")) {
							winner = chatInput.substring(18, 19);
							oScore += 1;
							whosTurn.equals("O");

							int result = JOptionPane.showConfirmDialog(null,
									"Player O Wins!!", null,
									JOptionPane.DEFAULT_OPTION);
							System.exit(0);
						} else if (chatInput.equals("Tied")) {
							int result = JOptionPane.showConfirmDialog(null,
									"Cat's Game!!", null,
									JOptionPane.DEFAULT_OPTION);
							System.exit(0);
						}

						if (chatInput.contains(" ")) {
							serverCommands = chatInput.split(" ");
							whosTurn = serverCommands[2];
							System.out.println("server says: Player "
									+ serverCommands[0] + " pressed "
									+ serverCommands[1]);
							commandReceived = true;
						}
						if (chatInput.contains("Begin")) {
							System.out.println("server says: " + chatInput);
							whosTurn = "X";
						}
						System.out.println("It is Player " + whosTurn
								+ "'s turn.");
						if (whosTurn.equals("X")) {

							if (ImPlayer1) { // its my turn: need to make a move, send to server, wait for my turn again
								updateBoard(serverCommands[1],
										serverCommands[0]);
								commandReceived = false;

								int timer = 15;
								enableButtons();
								while (buttonPressed.equals("")) {
									if (timer == 0) {
										playerRole.setText("Time's up!");
										disableButtons();
										automateMove("X");
										break;
									}
									playerRole.setText("Your move :)\nTimer: "
											+ timer + "\nPlayer X: " + xScore
											+ "\nPlayer O: " + oScore);
									Thread.sleep(1000);
									timer--;
								}
								System.out.println("here");
								while (buttonPressed.equals(""))
									;
								System.out.println(buttonPressed
										+ " Pressed! Sending to Server.");
								output.writeUTF(buttonPressed);
								output.flush();
								buttonPressed = "";
								disableButtons();
								Thread.sleep(2000);

							} else if (ImPlayer2 && commandReceived) { // not my turn: need  to listen for command from server, update my board, then make a move

								System.out.println("Updating board. Setting "
										+ serverCommands[1] + " to "
										+ serverCommands[0]);
								updateBoard(serverCommands[1],
										serverCommands[0]);
								commandReceived = false;
							} else {
								System.out.println(ImPlayer1 + " or "
										+ ImPlayer2 + " and I'm here");
							}
						}

						else if (whosTurn.equals("O")) {
							if (ImPlayer1) { // not my turn: need to listen for command from server, update my board, then make a move

								System.out.println("Updating board");
								updateBoard(serverCommands[1],
										serverCommands[0]);
								commandReceived = false;
							} else if (ImPlayer2) { // its my turn: need to make a move, send to server, wait for my turn again
								updateBoard(serverCommands[1],
										serverCommands[0]);
								commandReceived = false;

								int timer = 15;
								enableButtons();
								while (buttonPressed.equals("")) {
									if (timer == 0) {
										playerRole.setText("Time's up!");
										disableButtons();
										automateMove("O");
										break;
									}
									playerRole.setText("Your move :)\nTimer: "
											+ timer + "\nPlayer X: " + xScore
											+ "\nPlayer O: " + oScore);
									Thread.sleep(1000);
									timer--;
								}
								System.out.println(buttonPressed
										+ " Pressed! Sending to Server.");
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

			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		/*
		 * UpdateBoard takes the output from the server and places the letters on the board
		 * and disables any buttons that have been pressed.
		 */

		public void updateBoard(String buttonToUpdate, String setTextTo) {

			if (buttonToUpdate.equals("F")) { // first round
				// do nothing
			} else {
				System.out.println("Updating board. Setting " + buttonToUpdate
						+ " to " + setTextTo);
				if (buttonToUpdate.equals("B1")) {
					B1.setText(setTextTo);
					B1.setEnabled(false);
				} else if (buttonToUpdate.equals("B2")) {
					B2.setText(setTextTo);
					B2.setEnabled(false);
				} else if (buttonToUpdate.equals("B3")) {
					B3.setText(setTextTo);
					B3.setEnabled(false);
				} else if (buttonToUpdate.equals("B4")) {
					B4.setText(setTextTo);
					B4.setEnabled(false);
				} else if (buttonToUpdate.equals("B5")) {
					B5.setText(setTextTo);
					B5.setEnabled(false);
				} else if (buttonToUpdate.equals("B6")) {
					B6.setText(setTextTo);
					B6.setEnabled(false);
				} else if (buttonToUpdate.equals("B7")) {
					B7.setText(setTextTo);
					B7.setEnabled(false);
				} else if (buttonToUpdate.equals("B8")) {
					B8.setText(setTextTo);
					B8.setEnabled(false);
				} else if (buttonToUpdate.equals("B9")) {
					B9.setText(setTextTo);
					B9.setEnabled(false);
				}

			}

		}

		/**
		 * Automate a move if the player takes too long
		 */
		public void automateMove(String setTextTo) {
			Random rand = new Random();
			int i;
			boolean done = false;

			while (!done) {
				i = rand.nextInt(10);
				if (btnList.get(i).getText().equals("")) {
					btnList.get(i).setText(setTextTo);
					buttonPressed = btnList.get(i).getName();
					done = true;
				}
			}
		}

		/**
		 * Enable/Disable buttons depending on who's turn
		 */
		public void enableButtons() {
			if (B1.getText().equals("")) {
				B1.setEnabled(true);
			}
			if (B2.getText().equals("")) {
				B2.setEnabled(true);
			}
			if (B3.getText().equals("")) {
				B3.setEnabled(true);
			}
			if (B4.getText().equals("")) {
				B4.setEnabled(true);
			}
			if (B5.getText().equals("")) {
				B5.setEnabled(true);
			}
			if (B6.getText().equals("")) {
				B6.setEnabled(true);
			}
			if (B7.getText().equals("")) {
				B7.setEnabled(true);
			}
			if (B8.getText().equals("")) {
				B8.setEnabled(true);
			}
			if (B9.getText().equals("")) {
				B9.setEnabled(true);
			}
		}

		public void disableButtons() {
			if (B1.getText().equals("")) {
				B1.setEnabled(false);
			}
			if (B2.getText().equals("")) {
				B2.setEnabled(false);
			}
			if (B3.getText().equals("")) {
				B3.setEnabled(false);
			}
			if (B4.getText().equals("")) {
				B4.setEnabled(false);
			}
			if (B5.getText().equals("")) {
				B5.setEnabled(false);
			}
			if (B6.getText().equals("")) {
				B6.setEnabled(false);
			}
			if (B7.getText().equals("")) {
				B7.setEnabled(false);
			}
			if (B8.getText().equals("")) {
				B8.setEnabled(false);
			}
			if (B9.getText().equals("")) {
				B9.setEnabled(false);
			}
		}

		/**
		 * Reset all pieces on the board when game is over
		 */
		public void reset() {
			B1.setText("");
			B2.setText("");
			B3.setText("");
			B4.setText("");
			B5.setText("");
			B6.setText("");
			B7.setText("");
			B8.setText("");
			B9.setText("");
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
