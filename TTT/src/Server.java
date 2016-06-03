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
	private ObjectOutputStream sendBoard;
	PrintWriter messageToSend;
	BufferedReader messageReceived;
	Boolean player = true;
	String currentPlayer = "X";
	String buttonClicked = "";

	static List<Server> connections = new ArrayList<Server>();

	String[] gameBoard = { null, null, null, null, null, null, null, null, null };
	char X = 'X';
	char O = 'O';

	private int p1Score;
	private int p2Score;

	private static String winner = "Player ";

	public Server(Socket socket, String name) {
		threadName = name;
		threadSocket = socket;
	}

	public void run() {
		try {
			output = new ObjectOutputStream(threadSocket.getOutputStream());
			input = new ObjectInputStream(threadSocket.getInputStream());
			output.writeUTF("Welcome to Server 3001!  :  " + new Date());
			messageToSend = new PrintWriter(threadSocket.getOutputStream(),
					true);
			messageReceived = new BufferedReader(new InputStreamReader(
					threadSocket.getInputStream()));

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
			output.writeUTF("\nBegin game ");
			output.flush();

			while (true) {
				String message = messageReceived.readLine();
				buttonClicked = message.substring(4);
				// System.out.println(buttonClicked);
				if (message.startsWith("1")) {
					gameBoard[0] = currentPlayer;
				} else if (message.startsWith("2")) {
					gameBoard[1] = currentPlayer;
				} else if (message.startsWith("3")) {
					gameBoard[2] = currentPlayer;
				} else if (message.startsWith("4")) {
					gameBoard[3] = currentPlayer;
				} else if (message.startsWith("5")) {
					gameBoard[4] = currentPlayer;
				} else if (message.startsWith("6")) {
					gameBoard[5] = currentPlayer;
				} else if (message.startsWith("7")) {
					gameBoard[6] = currentPlayer;
				} else if (message.startsWith("8")) {
					gameBoard[7] = currentPlayer;
				} else if (message.startsWith("9")) {
					gameBoard[8] = currentPlayer;
				}

				messageToSend.println(currentPlayer + " " + buttonClicked);

			}

		} catch (IOException exception) {
			System.out.println("Uh oh, error: " + exception);
		}
	}

	public static void main(String args[]) {
		try {
			@SuppressWarnings("resource")
			ServerSocket serverSocket = new ServerSocket(3001);

			System.out.println("Server 3001 started at: " + new Date());

			InetAddress ip = null;
			int x = 1;
			while (connections.size() != 2) {

				Socket remote_client = serverSocket.accept();
				ip = remote_client.getInetAddress();
				System.out.println(ip + " has connected!");

				Server clientThread = new Server(remote_client, ("Player " + x
						+ " (connected at: " + ip + ")"));
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

	} // end main

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
