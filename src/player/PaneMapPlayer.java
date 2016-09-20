package player;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

public class PaneMapPlayer extends JPanel {

	private static final long serialVersionUID = 1L;
	private ArrayList<MapTile> mapTiles;
	private int width;
	private int height;
	
	public PaneMapPlayer(){
		width=10;
		height=7;
		setLayout(new GridLayout(height, width));
		mapTiles=new ArrayList<MapTile>();
		for (int i=0;i<width*height;i++){
			mapTiles.add(new MapTile());
			add(mapTiles.get(i));
		}
		
	}
	
	public void setMapTiles(int[] input){
		for (int i=0; i<input.length;i++){
			mapTiles.get(i).setIntValue(input[i]);
		}
	}
}
