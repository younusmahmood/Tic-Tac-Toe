import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;

public class Server implements Runnable {

	private static boolean notEnoughPlayers = true;

	private Thread thread;
	private String threadName;
	private Socket threadSocket;
	private ObjectOutputStream output;
	private ObjectInputStream input;

	static List<Server> connections = new ArrayList<Server>();

	String[] gameBoard = { null, null, null, null,null, null, null, null,null };
	char X = 'X';
	char O = 'O';

	public Server(Socket socket, String name) {
		threadName = name;
		threadSocket = socket;
	}

	public boolean gameOver(){
		if ((gameBoard[0] != null && gameBoard[0] == gameBoard[1] && gameBoard[0] == gameBoard[2])
				|| (gameBoard[3] != null && gameBoard[3] == gameBoard[4] && gameBoard[3] == gameBoard[5])
				|| (gameBoard[6] != null && gameBoard[6] == gameBoard[7] && gameBoard[6] == gameBoard[8])
				|| (gameBoard[0] != null && gameBoard[0] == gameBoard[3] && gameBoard[0] == gameBoard[6])
				|| (gameBoard[1] != null && gameBoard[1] == gameBoard[4] && gameBoard[1] == gameBoard[7])
				|| (gameBoard[2] != null && gameBoard[2] == gameBoard[5] && gameBoard[2] == gameBoard[8])
				|| (gameBoard[0] != null && gameBoard[0] == gameBoard[4] && gameBoard[0] == gameBoard[8])
				|| (gameBoard[2] != null && gameBoard[2] == gameBoard[4] && gameBoard[2] == gameBoard[6])) {
			return true;
		}
		return false;
	}
	
	public boolean tiedGame() {
		for (int i = 0; i < gameBoard.length; i++) {
			if (gameBoard[i] == null) {
				return false;
			}
		}
		return true;
	}
	
	public void run() {
		try {
			output = new ObjectOutputStream(threadSocket.getOutputStream());
			input = new ObjectInputStream(threadSocket.getInputStream());
			output.writeUTF("Welcome to Server 3001!  :  " + new Date());
			output.flush();
			while (notEnoughPlayers) {
				try {
					output.writeUTF("Please stand by for more players . . . ");
					output.flush();
					Thread.sleep(2500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			if (threadName.contains("Player 1")) {
				connections.get(0).output.writeUTF("Player X");
				System.out.println("Assigning player 1 role\n ");
			} else if (threadName.contains("Player 2")) {
				connections.get(1).output.writeUTF("Player O");
				System.out.println("Assigning player 2 role\n ");
			}

			output.flush();
			
			output.writeUTF("Begin!!!!!!!!");
			output.flush();
			
			while (true) {
				
//				String chatInput = input.readUTF();
				String chatInput;
		        while ((chatInput = input.readUTF()) != null) {
		        	System.out.println("Starting round");
					String whosTurn = "";
					String buttonPressed = chatInput.substring(0, 2);
					int buttonNumber = Integer.parseInt(chatInput.substring(1,2));

					System.out.println("received from " + threadName + ": " + chatInput);
					
					if (threadName.contains("Player 1")) {
						whosTurn = "O";
						gameBoard[buttonNumber-1] = whosTurn;
						
						if(gameOver()){
							output.writeUTF("Game over: Player X wins!!");
							output.flush();
							break;
						}
						if(tiedGame()){
							output.writeUTF("Cat's Game!");
							output.flush();
							break;
						}
						
						connections.get(1).output.writeUTF("X " + chatInput + " " +  whosTurn + " true");
						connections.get(1).output.flush();
						System.out.println("Sending player 2 the info\n ");
					} else if (threadName.contains("Player 2")) {
						whosTurn = "X";
						gameBoard[buttonNumber-1] = whosTurn;
						
						if(gameOver()){
							output.writeUTF("Game over: Player O wins!!");
							output.flush();
							break;
						}
						if(tiedGame()){
							output.writeUTF("Cat's Game!");
							output.flush();
							break;
						}
						
						
						//chatat17
						connections.get(0).output.writeUTF("O " + chatInput + " " + whosTurn + " true");
						connections.get(0).output.flush();
						System.out.println("Sending player 1 the info\n ");
					}
					

				}

		        }



		} catch (IOException exception) {
			System.out.println("Uh oh, error: " + exception);
		}
	}

	public static void main(String args[]) {
		try {
			ServerSocket serverSocket = new ServerSocket(3001);

			System.out.println("Server 3001 started at: " + new Date());

			InetAddress ip = null;
			int x = 1;
			// loop that accepts and starts thread for each connection
			while (connections.size() != 2) {

				// Wait for a client to connect, then accept that connection
				Socket remote_client = serverSocket.accept();
				ip = remote_client.getInetAddress();
				System.out.println(ip + " has connected!");

				Server clientThread = new Server(remote_client, ("Player " + x + " (connected at: " + ip + ")"));
				x++;

				clientThread.start();

				connections.add(clientThread);
			}
			Thread.sleep(1500);
			System.out.println("2 clients connected ...");
			notEnoughPlayers = false;

		} catch (IOException exception) {
			System.out.println("Error: " + exception);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	} 

	private void start() {
		System.out.println("Starting " + threadName);
		if (thread == null) {
			try {
				thread = new Thread(this, threadName);
				thread.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
