package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import javax.swing.JTextArea;

public class ControllerServer {

	private static ControllerServer instance = null;
	private int serverPort;
	private ServerSocket server;
	private boolean online;
	private Socket client;
	private ArrayList<ClientThread> clients;
	private ArrayList<String> players;
	private ArrayList<PrintWriter> outputs;
	private String name;
	private JTextArea eventsInfo;
	private PaneMapServer paneMap;
	private PaneGMServer paneGM;
	private JTextArea chatWindow;

	private ControllerServer() {
		players = new ArrayList<String>();
		clients = new ArrayList<ClientThread>();
		outputs = new ArrayList<PrintWriter>();
		serverPort = 404;
		online = true;
		name = "The DM";
		switchPort();
	}

	public static ControllerServer getInstance() {
		if (instance == null) {
			instance = new ControllerServer();
		}
		return instance;
	}

	public synchronized int getServerPort() {
		return serverPort;
	}

	public synchronized void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public void switchPort() {
		online = true;
		new Thread() {
			public void run() {
				try {
					ClientThread clientThread;
					server = new ServerSocket(serverPort);
					while (online) {
						try {
							client = server.accept();
						} catch (SocketException e) {
							break;
						}
						if (online) {
							clientThread = new ClientThread(client);
							clients.add(clientThread);
							new Thread(clientThread).start();
						}
						waitFor(50);
					}
				} catch (BindException e) {
					waitFor(500);
					eventsInfo.append("\nPort already in use.");
					stopServing();
				} catch (IOException e) {
					waitFor(500);
					eventsInfo.append("\nServer-side error in communicating with a client.");
				}
			}
		}.start();
	}

	public void stopServing() {
		online = false;
		try {
			if (server != null) {
				if (!server.isClosed()) {
					server.setSoTimeout(100); // Stops the server listening for
												// new clients.
				}
				server.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		eventsInfo.append("\nServer is now offline.");
		paneGM.stopServing();
	}

	public synchronized void setOutput(String output) {
		eventsInfo.append("\n"+output);
	}

	public void waitFor(int milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			System.err.println("Unable to suspend thread.");
		}
	}

	public synchronized boolean isOnline() {
		return online;
	}

	public synchronized void addPlayer(String playerName) {
		players.add(playerName);
		eventsInfo.append("\n" + playerName + " has connected.");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setInfoPane(JTextArea eventsInfo) {
		this.eventsInfo = eventsInfo;

	}

	public void updateAll(String text) {
		for (int i = 0; i < outputs.size(); i++) {
			outputs.get(i).println(text);
		}
	}

	public class ClientThread implements Runnable {

		private Socket threadClient;
		private PrintWriter output;
		private BufferedReader input;
		private String playerName;

		/**
		 * Constructor. Saves the socket connection passed in.
		 * 
		 * @param threadClient
		 *            - the socket connection to a client.
		 */
		public ClientThread(Socket threadClient) {
			this.threadClient = threadClient;
		}

		/**
		 * Attempts to set map if this is the first thread created. Continually
		 * receives and interprets string based commands from the client.
		 */
		public void run() {
			try {
				// Initialises the necessary variables to communicate with the
				// client.
				output = new PrintWriter(threadClient.getOutputStream(), true);
				outputs.add(output);
				input = new BufferedReader(new InputStreamReader(threadClient.getInputStream()));
				String currentLine;
				String[] command;
				while (online) {
					if ((currentLine = input.readLine()).startsWith("NAME ")) {
						if (currentLine.length() == 5) {
							playerName = threadClient.getPort() + "";
							output.println("NAME " + playerName);
						} else {
							playerName = currentLine.substring(5);
						}
						break;
					}
					waitFor(100);
				}
				// Initialises the player somewhere in the map, then interprets
				// each input from them as a command.
				addPlayer(playerName);
				while ((currentLine = input.readLine()) != null) {
					if (!online) {
						break;
					}
					command = currentLine.trim().split(" ");
					switch (command[0]) {
					case "PRINT":
						chatWindow.append("\n" + playerName + ": " + currentLine.substring(6));
						updateAll("PRINT "+playerName + ": " + currentLine.substring(6));
						break;
					case "MAP":
						output.println("MAP " + paneMap.getMapTiles());
						break;
					case "DICE":
						eventsInfo.append("\n" + currentLine.substring(5));
						updateAll(currentLine);
						break;
					case "NAME":
						String lastName = playerName;
						playerName = currentLine.substring(5);
						eventsInfo.append("\n" + lastName + " changed their name to '" + playerName + "'.");
						updateAll("PRINT "+lastName + " changed their name to '" + playerName + "'.");
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
		 * Removes the client from the map and list of clients: if they are the
		 * last client on the server, it will also close.
		 */
		private void removeClient() {
			eventsInfo.append("\n" + playerName + " has disconnected.");
			players.remove(playerName);
			clients.remove(this);
			outputs.remove(output);
			if (clients.isEmpty()) {
				stopServing();
			}
		}
	}

	public void setChatWindow(JTextArea chatWindow) {
		this.chatWindow = chatWindow;
	}

	public void setPaneMap(PaneMapServer paneMap) {
		this.paneMap = paneMap;
	}

	public void setPaneGM(PaneGMServer paneGM) {
		this.paneGM = paneGM;
	}
}
