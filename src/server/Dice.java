package server;
import javax.swing.JButton;

public class Dice extends JButton {
	
	private static final long serialVersionUID = 1L;
	private int maxRand;
	
	public Dice(int maxRand){
		this.maxRand=maxRand;
		setText(maxRand+"");
	}
	
	public int getMaxRand(){
		return maxRand;
	}
}
