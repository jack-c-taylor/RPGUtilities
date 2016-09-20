package player;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class MapTile extends JLabel {
	
	private static final long serialVersionUID = 1L;
	
	public MapTile(){
		setIcon(new ImageIcon("map_tiles/1.png"));
	}	

	public void setIntValue(int tileNumber) {
		if (new ImageIcon("map_tiles/"+tileNumber+".png").getImage().getWidth(null)==-1){
			setIcon(new ImageIcon("map_tiles/1.png"));
		}else{
			setIcon(new ImageIcon("map_tiles/"+tileNumber+".png"));
		}
	}
}
