import javax.swing.JFrame;

public class GUIServer extends JFrame{

	private static final long serialVersionUID = 1L;

	public GUIServer() {
		initializeComponents();
		addComponents();
		setTitle("RPG Utilities-GM Perspective");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void initializeComponents() {
		// Give components declared at the top particular values, eg
		// New tabDice (same for server or client)
		// New tabCharacterListServer
		// New tabCharacterCreatorServer
		// New panePlayers
		// New paneDice
		// New paneGM
		// New paneMap
	}
	
	private void addComponents() {
		// TODO Auto-generated method stub
		
	}

}
