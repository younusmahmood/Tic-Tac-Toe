import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;





@SuppressWarnings("serial")
public class Client extends JPanel implements ActionListener {
	
	
	private boolean begin = false;
	
	// only one of these values will be set to true in each thread
	// ie. player X's thread will have ImX set to true and ImO will be set to false (vice versa in player O's thread)
	private boolean ImX = false; 
	private boolean ImO = false; 
	boolean btn = false;
	
	private boolean gameOver = false;
	private String whosTurn = "X"; // indicate who's turn it is
//	private String move = "";
	private boolean move = true; // to hold the value of the players move (b1, b2, etc)

	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Socket socket;
	String name;
	String buttonPressed = "";
	boolean player1 = false;
	boolean player2 = true;
	
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
	    
//	    playerRole.setHorizontalAlignment(SwingConstants.RIGHT);
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
	
	public void actionPerformed(ActionEvent e) 
	{
	    Object source = e.getSource();
	    btn = true;
	    
	    if (source == connectBtn) 
	    {	
	    	try {
	    		connectBtn.setEnabled(false);
	    		
	    		ClientThread t1 = new ClientThread();
	    		
	    		new Thread(t1).start();
	    		
	    	} catch (Exception ex) 
	    	{
	    		System.out.println("ERROR: " + ex.getMessage());
	    	}
	    }
	    
	    if (source == B1) { 
	    	B1.setText(whosTurn);
	    	buttonPressed = "B1";
	    	System.out.println("B1 pressed");
	    }else if(source == B2){
	    	B2.setText(whosTurn);
	    	buttonPressed = "B2";
	    	move = true;
	    	System.out.println("B2 pressed");
	    }
	    else if(source == B3){
	    	B3.setText(whosTurn);
	    	buttonPressed = "B3";
	    	move = true;
	    	System.out.println("B3 pressed");
	    }
	}

	/**
	 * 
	 * @author 
	 *
	 */
    class ClientThread implements Runnable 
    {
    		
		@SuppressWarnings({ "unchecked", "deprecation" })
		public void run() {
			String turn;
		try {
			socket = new Socket("localhost", 3001);
			output = new ObjectOutputStream(socket.getOutputStream());
			input = new ObjectInputStream(socket.getInputStream());
		

			while (!gameOver) {
				String inputString = input.readUTF();
				System.out.println(inputString);
				playerRole.setText(inputString);
				if (inputString.startsWith("Player")) { // wait for players to join / assign roles
					name = inputString.substring(7);
					frame.setTitle("Welcome to Tic Tac Toe: Player "+ name);

					if (name.equals("X")) { // let the thread know who it belongs to (aka this thread belongs to player X)
						System.out.println("IMx should be true now -> " + name);
						ImX = true;
						frame.setTitle("Welcome to Tic Tac Toe: Player "+ name);
						enableButtons();
						whosTurn = "X";
					}
					if (name.equals("O")) {
						System.out.println("IMo should be true now -> " + name);
						ImO = true;
						enableButtons();
						frame.setTitle("Welcome to Tic Tac Toe: Player "+ name);
					}
					playerRole.setText("Welcome to Tic Tac Toe: Player "+ name);
					break;
				} 
			}
			
			
			/*********************
			 * 
			 * 
			 * Under here is where he error shows up
			 * 
			 *********************/
			System.out.println("outside of loop now");
			
			while (true){
				
				turn = input.readUTF();
				
				if(turn.startsWith("")){
					buttonPressed = turn.substring(6);
					whosTurn = turn.substring(4, 5);
				}

//				if(buttonPressed == "B1" && B1.getText().equals("")){
//					B1.setText(whosTurn);
//					move = !move;
//					B1.setEnabled(false);
//				}
				
				while (buttonPressed.equals(""));
				
				//System.out.println("button equals: "+ buttonPressed);
				output.writeUTF(buttonPressed + " " + whosTurn);
				output.flush();
				
//				System.out.println("This button equals: "+ buttonPressed);
//				output.writeUTF(buttonPressed + " " + whosTurn);
//				output.flush();			   
//				buttonPressed = "";
				
				
				
				
			 // System.out.println(turn);
				
				
				
			}
			
			
			
        } catch (EOFException eof)
        {
        	System.out.println("EOF: " + eof.getMessage());
        	
        } catch (IOException exception) {
        	
        	playerRole.setText("Error: " + exception.getMessage());
            System.out.println(exception.getMessage());
        	connectBtn.setEnabled(true);
        	
        }
		
	}
		

	/**
	 * enable/disable buttons 
	 * depending on who's turn
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
	
    } 
    
	public static void main(String[] args) {
		
        frame = new JFrame("Tic Tac Toe Online");
        frame.add(new Client());
        frame.setSize(500, 500);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(true);

	}

} // end Client class
