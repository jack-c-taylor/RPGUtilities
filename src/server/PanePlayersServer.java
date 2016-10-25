package server;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class PanePlayersServer extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextArea chatWindow;
	private JTextArea chatInput;
	private AbstractButton chatButton;
	private ControllerServer controllerServer;

	public PanePlayersServer() {
		setLayout(new BorderLayout());
		controllerServer = ControllerServer.getInstance();
		chatWindow = new JTextArea("Chat Window:");
		chatWindow.setEditable(false);
		chatWindow.setLineWrap(true);
		chatWindow.setWrapStyleWord(true);
		chatInput = new JTextArea("");
		chatButton = new JButton("Send Message");
		chatButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (!chatInput.getText().equals("")) {
					chatWindow.append("\nThe DM: " +chatInput.getText());
					controllerServer.updateAll("PRINT The DM: " + chatInput.getText());
					chatInput.setText("");
				}
			}
		});
		JPanel chatPanel = new JPanel(new BorderLayout());
		chatPanel.add(chatWindow, BorderLayout.CENTER);
		chatPanel.add(chatInput, BorderLayout.SOUTH);
		add(chatPanel, BorderLayout.CENTER);
		add(chatButton, BorderLayout.SOUTH);

	}

	public JTextArea getChatWindow() {
		return chatWindow;
	}
}
