package server;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;

public class PaneGMServer extends JPanel {

	private static final long serialVersionUID = 1L;
	private JRadioButton serverToggle;
	private JTextArea serverPortInput;
	private JButton changePortButton;
	private ControllerServer controllerServer;
	private JPanel serverSocketInfo;
	private String serverIP;
	private int serverPort;
	private JTextArea eventsInfo;

	public PaneGMServer() {
		controllerServer = ControllerServer.getInstance();
		serverPortInput = new JTextArea();
		serverToggle = new JRadioButton();
		changePortButton = new JButton("Change Port");
		serverSocketInfo = new JPanel(new GridLayout(0, 2));
		eventsInfo = new JTextArea("Server is now online.");
		eventsInfo.setEditable(false);
		eventsInfo.setLineWrap(true);
		eventsInfo.setWrapStyleWord(true);

		try {
			serverIP = InetAddress.getLocalHost().getHostAddress().toString();
		} catch (UnknownHostException e) {
			System.err.println("Err: UnknownHostException.");
			System.exit(-1);
		}
		serverPortInput.setText(controllerServer.getServerPort() + "");

		serverSocketInfo.add(new JLabel("Server IP: "));
		serverSocketInfo.add(new JLabel(serverIP));
		serverSocketInfo.add(new JLabel("Serving clients:"));
		serverSocketInfo.add(serverToggle);
		serverSocketInfo.add(new JLabel("Port number:"));
		serverSocketInfo.add(serverPortInput);
		serverSocketInfo.add(changePortButton);
		changePortButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changePortButton.setEnabled(false);
				try {
					serverPort = Integer.parseInt(serverPortInput.getText());
				} catch (NumberFormatException e1) {
					serverPort = 404;
					serverPortInput.setText("404");
				}
				controllerServer.setServerPort(serverPort);
				controllerServer.switchPort();
				serverToggle.setSelected(true);
				serverToggle.setEnabled(true);
				changePortButton.setEnabled(false);
				eventsInfo.append("\nServer is now online");
			}
		});
		serverToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (serverToggle.isSelected()) {
					changePortButton.setEnabled(false);
				} else {
					controllerServer.stopServing();
					changePortButton.setEnabled(true);
				}
			}
		});

		serverToggle.setSelected(true);
		changePortButton.setEnabled(false);
		controllerServer.setInfoPane(eventsInfo);
		controllerServer.setPaneGM(this);
		setLayout(new BorderLayout());
		add(serverSocketInfo, BorderLayout.PAGE_START);
		add(eventsInfo, BorderLayout.CENTER);
	}

	public void stopServing() {
		serverToggle.setSelected(true);
		serverToggle.setEnabled(false);
		changePortButton.setEnabled(true);
	}

}
