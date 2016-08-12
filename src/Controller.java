import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class Controller {
	
	private static Controller instance=null;
	private int serverPort;
	private ServerSocket server;
	private String output;
	private boolean online;
	private Socket client;
	private ArrayList<Socket> clients;
	private ArrayList<Integer> players;
	
	private Controller(){
		players=new ArrayList<Integer>();
		serverPort=404;
		online=true;
		output="Server is now online.";
		switchPort();
	}
	
	public static Controller getInstance() {
		if (instance==null){
			instance=new Controller();
		}
		return instance;
	}

	public synchronized int getServerPort() {
		return serverPort;
	}

	public synchronized void setServerPort(int serverPort) {
		this.serverPort=serverPort;
	}

	public void switchPort() {
		new Thread(){
			public void run(){
				try {
					server = new ServerSocket(serverPort);
					while (online){
						try{
							client=server.accept();
						}catch(SocketException e){
							break;
						}
						clients.add(client);
						new Thread(new ClientThread(client)).start();
						waitFor(50);
					}
				} catch (BindException e){
					setOutput("Port already in use.\n");
					stopServing();
				} catch (IOException e) {
					setOutput("Server-side error in communicating with a client.");
				} 
			}
		}.start();
	}

	public void stopServing() {
		try {
			if (server!=null){
				if (!server.isClosed()){
					server.setSoTimeout(1000); //Stops the server listening for new clients.
				}
				server.close();
				online=false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		setOutput("Server is now offline.\n");
	}

	public synchronized String getOutput() {
		String output=this.output;
		this.output="";
		return output;
	}

	public synchronized void setOutput(String output) {
		this.output = output;
	}
	
	public void waitFor(int milliseconds){
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			System.err.println("Unable to suspend thread.");
		}
	}
	
	public synchronized boolean isOnline(){
		return online;
	}
	
	public synchronized void addPlayer(int playerAddress){
		players.add(playerAddress);
		output=playerAddress+" has connected.\n";
	}
	public class ClientThread implements Runnable {
		
		private Socket threadClient;
		private PrintWriter output;
		private BufferedReader input;
		
		/**
		 * Constructor. Saves the socket connection passed in.
		 * @param threadClient - the socket connection to a client.
		 */
		public ClientThread(Socket threadClient){
			this.threadClient=threadClient;
		}
		
		/**
		 * Attempts to set map if this is the first thread created. Continually receives and interprets string based commands from the client.
		 */
		public void run() {
			try{
				//Initialises the necessary variables to communicate with the client.
				output = new PrintWriter(threadClient.getOutputStream(), true);
		        input = new BufferedReader(new InputStreamReader(threadClient.getInputStream()));
		        String currentLine;
		        String command;
		        //Initialises the player somewhere in the map, then interprets each input from them as a command.
	        	addPlayer(threadClient.getPort());
		        while ((currentLine=input.readLine())!=null){
		        	if (!online){
		        		break;
		        	}
		        	command = currentLine.trim();
		        	switch (command){
		        	case "ROLL":
		    			break;
		    		case "CHAT":
		    			break;
		    		case "QUIT":
		    			System.exit(0);
		    			break;
		    		default:
		    			output.println("FAIL");
		    		}
		        }
		        removeClient();
		        
			} catch (IOException e) {
				removeClient();
			}
		}
		
		/**
		 * Removes the client from the map and list of clients: if they are the last client on the server, it will also close.
		 */
		private void removeClient(){
	        setOutput(threadClient.getPort()+" has disconnected.\n");
	        players.remove(threadClient.getPort());
        	clients.remove(threadClient);
	        if (clients.isEmpty()){
	        	stopServing();
	        }
		}
	}
}
