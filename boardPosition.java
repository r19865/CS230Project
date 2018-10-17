
public class boardPosition {

	private double[] position = new double[3];
	
	private Tile thisTile;
	private Tile eastNeighbors;
	private Tile westNeighbors;
	private Tile belowNeighbors;
	
	public boardPosition(double[] position)
	{
		setPosition(position);
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
	
	public Tile getThisTile()
	{
		return thisTile;
	}
	
	public Tile getEastNeighbors()
	{
		return eastNeighbors;
	}
	
	public Tile getWestNeighbors()
	{
		return westNeighbors;
	}
	
	public Tile getBelowNeighbors()
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
	
	public void setThisTile(Tile thisTile)
	{
		this.thisTile = thisTile;
	}
	
	public void setEastNeighbors(Tile eastNeighbors)
	{
		this.eastNeighbors = eastNeighbors;
	}
	
	public void setWestNeighbors(Tile westNeighbors)
	{
		this.westNeighbors = westNeighbors;
	}
	
	public void setBelowNeighbors(Tile belowNeighbors)
	{
		this.belowNeighbors = belowNeighbors;
	}
	
	/////////////////////////////////////////////////////////
	///////////////// Other Methods /////////////////////////
	////////////////////////////////////////////////////////
	
	/**
	 * When the timer goes off, two tiles switch places. Rather than 
	 * 	updating each boardPosition's neighbors and positions, their 
	 * 	thisTiles are switched. This Tile is updated and the function 
	 * 	returns the other updated Tile. 	
	 * @param newTile
	 * @return 
	 */
	public Tile switchNeighbors(boardPosition newTile)
	{
		Tile tempTile = newTile.getThisTile();
		
		newTile.setThisTile(this.thisTile);
		this.thisTile = tempTile;
		tempTile = null;
		
		return newTile;
	}
	
	
	
}
