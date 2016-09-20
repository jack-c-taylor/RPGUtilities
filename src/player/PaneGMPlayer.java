package player;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class PaneGMPlayer extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPanel serverSocketInfo;
	private JTextArea serverIPInput;
	private JTextArea serverPortInput;
	private ControllerPlayer controllerPlayer;
	private JButton changePortButton;
	private JLabel serverOnlineStatusIndicator;
	private JTextArea eventsInfo;

	public PaneGMPlayer() {
		controllerPlayer = ControllerPlayer.getInstance();
		changePortButton = new JButton("Switch server");
		serverIPInput = new JTextArea(1, 10);
		serverPortInput = new JTextArea(1, 10);
		serverOnlineStatusIndicator = new JLabel("Server Status: Online ");
		serverSocketInfo = new JPanel(new GridLayout(3, 2));
		eventsInfo = new JTextArea("Connecting to server...");
		eventsInfo.setEditable(false);
		eventsInfo.setLineWrap(true);
		eventsInfo.setWrapStyleWord(true);

		serverIPInput.setText(controllerPlayer.getServerIP());
		serverPortInput.setText(controllerPlayer.getServerPort() + "");

		serverSocketInfo.add(new JLabel("Server IP:"));
		serverSocketInfo.add(serverIPInput);
		serverSocketInfo.add(new JLabel("Server port:"));
		serverSocketInfo.add(serverPortInput);
		serverSocketInfo.add(changePortButton);
		serverSocketInfo.add(serverOnlineStatusIndicator);
		changePortButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					controllerPlayer.setServerPort(Integer.parseInt(serverPortInput.getText()));
				} catch (NumberFormatException e1) {
					controllerPlayer.setServerPort(404);
					serverPortInput.setText("404");
				}
				controllerPlayer.setServerIP(serverIPInput.getText());
				controllerPlayer.changeServer();
			}
		});
		controllerPlayer.setInfoPane(eventsInfo);
		setLayout(new BorderLayout());
		add(serverSocketInfo, BorderLayout.PAGE_START);
		add(eventsInfo, BorderLayout.CENTER);
		controllerPlayer.changeServer();
	}

}
