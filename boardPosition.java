import javax.swing.JFrame;
import javax.swing.JLabel;

public class boardPosition implements Comparable<boardPosition>{

	private int[] position = new int[3];
	private boolean playable;
	
	private tile thisTile;
	private boardPosition eastNeighbors;
	private boardPosition westNeighbors;
	private boardPosition belowNeighbors;
	
	protected JFrame passedInJFrame;
	protected JLabel positionJLabel;
	
	public boardPosition(tile thisTile, JFrame gameJFrame)
	{
		this.thisTile = thisTile;
		this.passedInJFrame = gameJFrame;
		
		positionJLabel = new JLabel();
		positionJLabel.setBounds (10, 10, 10, 10); // arbitrary, will change later
        positionJLabel.setVisible(false);
		positionJLabel.setIcon(thisTile.getImage());
        passedInJFrame.getContentPane().add(positionJLabel);
		
		playable = false;
		eastNeighbors = null;
		westNeighbors = null;
		belowNeighbors = null;
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
		return eastNeighbors;
	}
	
	public boardPosition getWestNeighbors()
	{
		return westNeighbors;
	}
	
	public boardPosition getBelowNeighbors()
	{
		return belowNeighbors;
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
	
	public void render()
	{
		
	}
	
	public void notifyNeighbors(boolean onBoard)
	{
		thisTile.setOnBoard(onBoard);
		
		if(!onBoard)
		{
			if(eastNeighbors != null)
				eastNeighbors.setPlayable(true);;
			if(westNeighbors != null)
				westNeighbors.setPlayable(true);
			if(belowNeighbors != null)
				checkBelowNeighbors();
		}
	}
	
	
	public String toString()
	{
		return String.format("%s %nX: %f Y: %f Z: %f", thisTile.getType(), position[0], position[1], position[2]);
	}
	
	/////////////////////////////////////////////////////////
	///////////////// Private Methods ///////////////////////
	////////////////////////////////////////////////////////
	
		
	private void checkBelowNeighbors()
	{
		if(belowNeighbors.getEastNeighbors().getZ() != belowNeighbors.getZ() || belowNeighbors.getWestNeighbors().getZ() != belowNeighbors.getZ())
		{
			belowNeighbors.setPlayable(true);
		}
	}
	
	public int compareTo(boardPosition bp) 
    {
    		int compare= getThisTile().getType().compareTo(bp.getThisTile().getType());
    		
        return compare;
    }
	
	public void drawPosition()
	{
		// set bounds only accepts integers - positions are doubles....
		System.out.println(thisTile.getType());
		positionJLabel.setBounds(position[0], position[1], thisTile.getImage().getIconWidth(), thisTile.getImage().getIconHeight());
		positionJLabel.setVisible(true);
	}
	
}
