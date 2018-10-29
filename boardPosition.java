
public class boardPosition {

	private double[] position = new double[3];
	private boolean playable;
	
	private tile thisTile;
	private boardPosition eastNeighbors;
	private boardPosition westNeighbors;
	private boardPosition belowNeighbors;
	
	public boardPosition()
	{
		playable = false;
	}
	
	/////////////////////////////////////////////////////////
	///////////////// Get Methods //////////////////////////
	/////////////////////////////////////////////////////////
	
	public double getX()
	{
		return position[0];
	}
	
	public double getY()
	{
		return position[1];
	}
	
	public double getZ()
	{
		return position[2];
	}
	
	public double[] getPosition() {
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

	public void setPosition(double x, double y, double z)
	{
		this.position[0] = x;
		this.position[1] = y;
		this.position[2] = z;
	}
	
	public void setPosition(double[] position)
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
	
}
