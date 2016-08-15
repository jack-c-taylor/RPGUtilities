package server;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JPanel;

public class PaneDiceServer extends JPanel {

	private static final long serialVersionUID = 1L;
	
	public PaneDiceServer(){
		add(Box.createRigidArea(new Dimension(200,10)));
	}
}
