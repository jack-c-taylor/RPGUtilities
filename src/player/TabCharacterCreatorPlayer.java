package player;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class TabCharacterCreatorPlayer extends JPanel {
	
	private static final long serialVersionUID = 1L;
	private ControllerPlayer controllerPlayer;
	private JTextArea namePane;
	private JButton nameButton;
	private JLabel nameLabel;

	public TabCharacterCreatorPlayer(){
		setLayout(new GridLayout(3, 3));
		controllerPlayer=ControllerPlayer.getInstance();
		namePane=new JTextArea(1,10);
		nameButton=new JButton("Set Name");
		nameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controllerPlayer.setName(namePane.getText());
				nameLabel.setText("Your name is: "+namePane.getText());
			}
		});
		nameLabel=new JLabel("Your name is: "+controllerPlayer.getName());
		add(nameLabel);
		add(namePane);
		add(nameButton);
	}
}
