import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class TicTacToe{
	Player current;
	
	
class Player extends Thread{
	
	Socket clientSocket;
	BufferedReader in;
	PrintWriter out;
	String name;
	String[] gameBoard;
	char player;
	
	
	Player(Socket clientSocket, char player) throws IOException{
		this.clientSocket = clientSocket;
		this.player = player;
		in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		out = new PrintWriter(clientSocket.getOutputStream(), true);
		out.println("Welcome");
	}
	
	public void run(){
			try {
				while(true){
					String move = in.readLine();
				if(move.startsWith("Hello")){
					out.println("Welcome Client");
				}
				
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					clientSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
	}
}


}
