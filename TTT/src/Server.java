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

	String[] gameBoard = { ".", ".", ".", ".", ".", ".", ".", ".", "." };
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
			output.flush();
			while (notEnoughPlayers) // wait for 2 clients to connect then
										// assign player roles
			{
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
			connections.get(0).output.writeUTF("\nBegin round ");
			output.flush();

			// listen for Player to send their move and then forward to the
			// opponent

			while (true) {
				// This will wait until a line of text has been sent
				String chatInput = input.readUTF();
				
				String whosTurn = chatInput.substring(3);
				String buttonPressed = chatInput.substring(0, 2);

				//System.out.println(whosTurn + " " + buttonPressed);

				connections.get(0).output.writeUTF("Set " + whosTurn + " " + buttonPressed);
				connections.get(1).output.writeUTF("Set " + whosTurn + " " + buttonPressed);
				connections.get(0).output.flush();
				connections.get(1).output.flush();
				
			}

			// while (true)
			// {
			// String inputString = input.readUTF();
			// System.out.println(inputString);
			// String[] newStr = inputString.split(" ");
			// System.out.println(newStr[0]);
			// Thread.sleep(2500);
			// }

			// thread.sleep(15000);
			// output.writeUTF(winner + " ");
			// output.flush();

		} catch (IOException exception) {
			System.out.println("Uh oh, error: " + exception);
		}
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
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
				

				// Create a new custom thread to handle the connection
				Server clientThread = new Server(remote_client, ("Player " + x
						+ " (connected at: " + ip + ")"));
				x++;

				// Start the thread for that remote client
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
