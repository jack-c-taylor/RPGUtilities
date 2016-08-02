import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class MapTile extends JLabel {
	
	private static final long serialVersionUID = 1L;
	private int tileNumber;
	
	public MapTile(){
		setIcon(new ImageIcon("map_tiles/1.png"));
		tileNumber=1;
		addMouseListener(new MouseAdapter(){
			@Override
            public void mousePressed(MouseEvent e) {
                changeTile();
            }
		});
	}
	
	public void changeTile() {
		tileNumber++;
		if (new ImageIcon("map_tiles/"+tileNumber+".png").getImage().getWidth(null)==-1){
			tileNumber=1;
			setIcon(new ImageIcon("map_tiles/1.png"));
		}else{
			setIcon(new ImageIcon("map_tiles/"+tileNumber+".png"));
		}
	}
	
	public int getIntValue(){
		return tileNumber;
	}
}
