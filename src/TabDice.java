import java.util.ArrayList;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class TabDice extends JPanel {

	private static final long serialVersionUID = 1L;

	public TabDice(){
		Random r=new Random();
		JLabel output=new JLabel();
		int[] numbers={4,6,8,10,12,20,100};
		ArrayList<Dice> pl = new ArrayList<Dice>();
		for (int i=0;i<numbers.length;i++){
			pl.add(new Dice(numbers[i]));
			pl.forEach(p-> p.addActionListener(e -> output.setText(r.nextInt(p.getMaxRand())+"")));
		}
		pl.forEach(p-> add(p));
		add(output);
	}
}
