
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

public class boardPosition implements Comparable<boardPosition>{

	private int[] position = new int[3];
	private boolean playable;
	
	private tile thisTile;
	private boardPosition eastNeighbors;
	private boardPosition westNeighbors;
	private boardPosition belowNeighbors;
	private boardPosition aboveNeighbors;
	
	protected JFrame passedInJFrame;
	protected JLabel positionJLabel;
	protected JLayeredPane layeredPane;
	protected JLabel shadow;
	
	protected int shadowOffset = 3;
	
	public boardPosition(tile thisTile, JFrame gameJFrame)
	{
		this.thisTile = thisTile;
		this.passedInJFrame = gameJFrame;
		
		positionJLabel = new JLabel();
		positionJLabel.setBounds (10, 10, 10, 10); // arbitrary, will change later
        positionJLabel.setVisible(false);
		positionJLabel.setIcon(thisTile.getImage());
		shadow = new JLabel();
		shadow.setBounds (10, 10, 10, 10); // arbitrary, will change later
        shadow.setVisible(false);
        passedInJFrame.getContentPane().add(positionJLabel);
        passedInJFrame.getContentPane().add(shadow);
		/*layeredPane = new JLayeredPane();
		layeredPane.setBounds(10,10,10,10);
		layeredPane.setVisible(false);
		layeredPane.setPosition(thisTile.getImage(),thisTile.);*/
       //passedInJFrame.getContentPane().add(positionJLabel);
       //passedInJFrame.getContentPane().add(shadow);
       
		playable = false;
		eastNeighbors = null;
		westNeighbors = null;
		belowNeighbors = null;
		aboveNeighbors = null;
	}
	
	/////////////////////////////////////////////////////////
	///////////////// Get Methods //////////////////////////
	/////////////////////////////////////////////////////////
	
	public int getX()
	{
		return position[0];
	}
	
	public int getY()
	{
		return position[1];
	}
	
	public int getZ()
	{
		return position[2];
	}
	
	public int[] getPosition() {
		return position;
	}
	
	public tile getThisTile()
	{
		return thisTile;
	}
	
	public boolean getPlayable()
	{
		return playable;
	}
	
	public boardPosition getEastNeighbors()
	{
		if(eastNeighbors != null && eastNeighbors.getThisTile().getOnBoard())
			return eastNeighbors;
		else
			return null;
	}
	
	public boardPosition getWestNeighbors()
	{
		if(westNeighbors != null && westNeighbors.getThisTile().getOnBoard())
			return westNeighbors;
		else
			return null;
	}
	
	public boardPosition getBelowNeighbors()
	{
		if(belowNeighbors != null && belowNeighbors.getThisTile().getOnBoard())
			return belowNeighbors;
		else 
			return null;
	}
	
	public boardPosition getAboveNeighbors()
	{
		return aboveNeighbors;
	}
	
	public boardPosition getPlayableBelowNeighbors()
	{
		if(belowNeighbors != null && belowNeighbors.getPlayable())
			return belowNeighbors;
		else 
			return null;
	}
	
	public JLabel getJLabel()
	{
		return positionJLabel;
	}
	
	public JLabel getShadow()
	{
		return shadow;
	}
	
	/////////////////////////////////////////////////////////
	///////////////// Set Methods //////////////////////////
	/////////////////////////////////////////////////////////

	public void setPosition(int x, int y, int z)
	{
		this.position[0] = x;
		this.position[1] = y;
		this.position[2] = z;
	}
	
	public void setPosition(int[] position)
	{
		if(position.length == 3) {
			this.position = position;
		}
	}
	
	public void setThisTile(tile thisTile)
	{
		this.thisTile = thisTile;
	}
	
	public void setPlayable(boolean playable)
	{
		this.playable = playable;
	}
	
	public void setPlayable(int playableInt)
	{
		if (playableInt==9)
		{
			this.playable = true;
		}
		else
			this.playable=false;
	}
	
	public void setEastNeighbors(boardPosition eastNeighbors)
	{
		this.eastNeighbors = eastNeighbors;
	}
	
	public void setWestNeighbors(boardPosition westNeighbors)
	{
		this.westNeighbors = westNeighbors;
	}
	
	public void setBelowNeighbors(boardPosition belowNeighbors)
	{
		this.belowNeighbors = belowNeighbors;
	}
	
	public void setAboveNeighbors(boardPosition aboveNeighbors)
	{
		this.aboveNeighbors = aboveNeighbors;
	}
	
	/////////////////////////////////////////////////////////
	///////////////// Public Methods /////////////////////////
	////////////////////////////////////////////////////////
	
	/**
	 * When the timer goes off, two tiles switch places. Rather than 
	 * 	updating each boardPosition's neighbors and positions, their 
	 * 	thisTiles are switched. This Tile is updated and the function 
	 * 	returns the other updated Tile. 	
	 * @param newTile
	 * @return 
	 */
	public boardPosition switchNeighbors(boardPosition newTile)
	{
		if(newTile.getThisTile().getOnBoard())
		{
			tile tempTile = newTile.getThisTile();
			
			newTile.setThisTile(this.thisTile);
			this.thisTile = tempTile;
			tempTile = null;
		}else
		{
			newTile = null;
		}
		
		return newTile;
	}
	
	public int compareTo(boardPosition bp) 
    {
    	int compare= getThisTile().getType().compareTo(bp.getThisTile().getType());
    		
        return compare;
    }
	
	public boolean equals(boardPosition position)
	{
		return (thisTile.equals(position.getThisTile()) && (this.getX() != position.getX() || this.getY() != position.getY()));
	}
	
	public boolean differentCoordinates(boardPosition position)
	{
		return (this.getX() != position.getX() || this.getY() != position.getY() || this.getZ() != position.getZ());
	}
	
	public JLabel drawPosition(int buffer)
	{
		positionJLabel.setBounds(position[0] + buffer*(position[0]/thisTile.getImage().getIconWidth()), position[1] + buffer*(position[1]/thisTile.getImage().getIconHeight()), thisTile.getImage().getIconWidth(), thisTile.getImage().getIconHeight());
		if(aboveNeighbors == null) 
			positionJLabel.setVisible(true);
		else if(!aboveNeighbors.getThisTile().getOnBoard())
			positionJLabel.setVisible(true);

		return positionJLabel;
				
	}
	
	public JLabel drawPositionWithBorder()
	{
		positionJLabel.setBounds(position[0], position[1], thisTile.getImage().getIconWidth(), thisTile.getImage().getIconHeight());
		Color colors[] = {Color.RED, Color.BLUE, Color.YELLOW, Color.GREEN, Color.MAGENTA};
		
		Border raisedBorder = BorderFactory.createBevelBorder(BevelBorder.RAISED, colors[getZ()], colors[getZ()]);
		positionJLabel.setBorder(raisedBorder);
		positionJLabel.setVisible(true);

		
		return positionJLabel;
	}
	
	public JLabel drawShadow(int arrangementWidth, int arrangementHeightm)
	{
//		if(playable)
//			shadow.setBounds(position[0]+shadowOffset, position[1]+shadowOffset, thisTile.getImage().getIconWidth(), thisTile.getImage().getIconHeight());

			
		if(playable && position[0]/thisTile.getImage().getIconWidth() > arrangementWidth/2)
			shadow.setBounds(position[0], position[1] , thisTile.getImage().getIconWidth(), thisTile.getImage().getIconHeight());
		else if(playable && position[0]/thisTile.getImage().getIconWidth() < arrangementWidth/2)
		shadow.setBounds(position[0], position[1] , thisTile.getImage().getIconWidth(), thisTile.getImage().getIconHeight());
		
		shadow.setOpaque(true);
		shadow.setBackground(new Color(64,64,64));
		shadow.setVisible(true);
			
		return shadow;
	}
	
	public void notifyNeighbors(boolean onBoard)
	{
		thisTile.setOnBoard(onBoard);
		
		if(!onBoard)
		{
			if(eastNeighbors != null && eastNeighbors.getThisTile().getOnBoard())
			{
				if(eastNeighbors.getAboveNeighbors() == null)
					eastNeighbors.setPlayable(true);
				else if(!eastNeighbors.getAboveNeighbors().getThisTile().getOnBoard())
					eastNeighbors.setPlayable(true);
			}
			if(westNeighbors != null && westNeighbors.getThisTile().getOnBoard())
			{
//				if(!westNeighbors.getAboveNeighbors().getThisTile().getOnBoard())
					westNeighbors.setPlayable(true);
			}
			if(belowNeighbors != null)
			{
				checkBelowNeighbors();
			}
		}
		
//		System.out.println("Tile: " + thisTile.getType() + " X: " + getX() + " E: " + getEastNeighbors() + " W: " + getWestNeighbors() + " B: " + getPlayableBelowNeighbors());
	}	
	
	public boolean wasSelected(int x, int y)
	{
		boolean selected = (position[0] < x && position[0]+thisTile.getImage().getIconWidth() > x && position[1] < y && position[1]+thisTile.getImage().getIconHeight() > y && playable);
		
		if(selected)
		{
			shadow.setBackground(Color.YELLOW);
			positionJLabel.repaint();
			shadow.repaint();
		}
		
		return selected;
	}
	
	public void remove()
	{
		positionJLabel.setVisible(false);
		shadow.setVisible(false);
		playable = false;
		thisTile.setOnBoard(false);
	}
	
	public String toString()
	{
		return String.format("Type: %s X: %d Y: %d Z: %d", thisTile.getType(), position[0]/62, position[1]/82, position[2]);
	}
	
	/////////////////////////////////////////////////////////
	///////////////// Private Methods ///////////////////////
	////////////////////////////////////////////////////////
	
		
	private void checkBelowNeighbors()
	{
		if(belowNeighbors.getEastNeighbors() != null && belowNeighbors.getWestNeighbors() != null)
		{
			if(belowNeighbors.getEastNeighbors().getZ() != belowNeighbors.getZ() || belowNeighbors.getWestNeighbors().getZ() != belowNeighbors.getZ())
			{
				belowNeighbors.setPlayable(true);
			}
		}
	}
	
}
