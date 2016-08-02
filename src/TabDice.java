import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class TabDice extends JPanel {

	private static final long serialVersionUID = 1L;
	private ArrayList<Dice> diceList;
	private JLabel output;
	private JTextArea customPane;
	private JButton customButton;
	private Random r;

	public TabDice(){
		output=new JLabel("You rolled a...");
		customPane=new JTextArea();
		customButton=new JButton("Roll");
		r=new Random();
		diceList=new ArrayList<Dice>();
		Arrays.asList(4,6,8,10,12,20,100).stream().forEach(p-> diceList.add(new Dice(p)));
		diceList.forEach(p-> p.addActionListener(e -> output.setText("You rolled a "+(r.nextInt(p.getMaxRand())+1)+".")));
		customButton.addActionListener(e -> output.setText(rollCustomNumber()));
		createGridBag();
	}
	
	public void createGridBag(){
		setLayout(new GridBagLayout());
		add(output,createGridBagConstraints(0,0));
		for (int i=0;i<diceList.size();i++){
			add(diceList.get(i),createGridBagConstraints(i,1));
		}
		add(new JLabel("Custom Dice Size: "),createGridBagConstraints(0,2));
		add(customPane,createGridBagConstraints(1,2));
		add(customButton,createGridBagConstraints(2,2));
	}
	
	public GridBagConstraints createGridBagConstraints(int x, int y){
		GridBagConstraints c = new GridBagConstraints();
		c.weightx=1;
		c.gridx=x;
		c.gridy=y;
		c.fill = GridBagConstraints.HORIZONTAL;
		return c;
	}
	
	public String rollCustomNumber(){
		try{
			int rand=r.nextInt(Integer.parseInt(customPane.getText()))+1;
			return "You rolled a "+rand+".";
		}catch (Exception e){}
		return "Invalid input.";
	}
}
