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
	private Controller controller;
	private JPanel serverSocketInfo;
	private String serverIP;
	private int serverPort;
	private JTextArea eventsInfo;
	
	public PaneGMServer(){
		controller=Controller.getInstance();
		serverPortInput=new JTextArea();
		serverToggle=new JRadioButton();
		changePortButton=new JButton("Change Port");
		serverSocketInfo=new JPanel(new GridLayout(0,2));
		eventsInfo=new JTextArea();
		
		try {
			serverIP=InetAddress.getLocalHost().getHostAddress().toString();
		} catch (UnknownHostException e) {
			System.err.println("Err: UnknownHostException.");
			System.exit(-1);
		}		
		serverPortInput.setText(controller.getServerPort()+"");
			
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
				try{
					serverPort=Integer.parseInt(serverPortInput.getText());
				}catch (NumberFormatException e1){
					serverPort=40004;
					serverPortInput.setText("40004");
				}
				controller.setServerPort(serverPort);
				controller.switchPort();
				serverToggle.setSelected(true);
				serverToggle.setEnabled(true);
				changePortButton.setEnabled(false);		
			} 
		});
		serverToggle.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent e) { 
				if (serverToggle.isSelected()){
					changePortButton.setEnabled(false);
				}else{
					controller.stopServing();
					changePortButton.setEnabled(true);
				}
			} 
		});
		
		serverToggle.setSelected(true);
		changePortButton.setEnabled(false);
		setLayout(new BorderLayout());
		add(serverSocketInfo, BorderLayout.PAGE_START);
		add(eventsInfo, BorderLayout.CENTER);
		new Thread() {
			public void run(){
				while (controller.isOnline()){
					eventsInfo.append(controller.getOutput());
					controller.waitFor(50);
				}
			}
		}.start();
	}

}
