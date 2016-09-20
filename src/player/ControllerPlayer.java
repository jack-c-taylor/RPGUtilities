package player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JTextArea;

public class ControllerPlayer {

	private static ControllerPlayer instance = null;
	private Socket server;
	private PrintWriter toServer;
	private BufferedReader fromServer;
	private boolean online;
	private String serverIP;
	private int serverPort;
	private String name;
	private JTextArea eventsInfo;
	private JTextArea chatWindow;
	private PaneMapPlayer paneMap;

	private ControllerPlayer() {
		online = true;
		serverIP = "localhost";
		serverPort = 404;
		name = "";
		toServer = null;
		fromServer = null;
	}

	public static ControllerPlayer getInstance() {
		if (instance == null) {
			instance = new ControllerPlayer();
		}
		return instance;
	}

	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	/**
	 * Returns the server IP.
	 */
	public String getServerIP() {
		return serverIP;
	}

	public synchronized int getServerPort() {
		return serverPort;
	}

	public synchronized void setServerPort(int serverPort) {
		this.serverPort = serverPort;
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

	/**
	 * Attempts to connect to the server, closing any existing connections
	 * first.
	 */
	public synchronized void changeServer() {
		try {
			online = true;
			if (server != null) {
				server.close();
			}
			server = new Socket(serverIP, serverPort);
			toServer = new PrintWriter(server.getOutputStream(), true);
			fromServer = new BufferedReader(new InputStreamReader(server.getInputStream()));
		} catch (IOException e) {
			server = null;
			toServer = null;
			fromServer = null;
			online = false;
		}
		waitFor(200);
		if (online) {
			eventsInfo.append("\nConnected to server.");
			new Thread() {
				public void run() {
					toServer.println("NAME " + name);
					update();
				}
			}.start();
			waitFor(100);
			new Thread() {
				public void run() {
					while (online) {
						toServer.println("MAP");
						waitFor(200);
					}
				}
			}.start();
		} else {
			eventsInfo.append("\nThe server was not available.");
		}
	}

	protected void update() {
		String response;
		String[] command;
		try {
			while (online && (response = fromServer.readLine()) != null) {
				command = response.trim().split(" ");
				switch (command[0]) {
				case "MAP":
					String[] map = response.substring(4).split(", ");
					int[] mapTiles = new int[map.length];
					for (int i = 0; i < map.length; i++) {
						try {
							mapTiles[i] = Integer.parseInt(map[i]);
						} catch (NumberFormatException e) {
							System.out.println(response);
							break;
						}
					}
					paneMap.setMapTiles(mapTiles);
					break;
				case "PRINT":
					chatWindow.append("\n" + response.substring(6));
					break;
				case "NAME":
					name=response.substring(5);
					eventsInfo.append("\nConnected as '"+name+"'.");
				default:
					break;
				}
			}
			online = false;
			server.close();
			eventsInfo.append("\nCommunications with the server have been interrupted.");
		} catch (IOException e) {
			try {
				online = false;
				server.close();
				eventsInfo.append("\nCommunications with the server have been interrupted.");
			} catch (IOException e1) {
				e.printStackTrace();
			}
		}

	}

	public String getName() {
		return name;
	}

	public synchronized void setOutput(String output) {
		eventsInfo.append(output);
		if (toServer != null) {
			toServer.println("PRINT " + output.substring(1));
		}
	}

	public void setInfoPane(JTextArea eventsInfo) {
		this.eventsInfo = eventsInfo;

	}
	
	public void setChatWindow(JTextArea chatWindow){
		this.chatWindow=chatWindow;
	}

	public void setPaneMap(PaneMapPlayer paneMap) {
		this.paneMap = paneMap;
	}

	public void setName(String name) {
		this.name = name;
		if (toServer != null) {
			toServer.println("NAME " + name);
		}
	}
	public void sendMessage(String message){
		if (toServer != null) {
			toServer.println("PRINT " + message);
		}
	}

}
