import java.awt.image.BufferedImage;

public class tile implements Comparable<tile>
{
	BufferedImage image;
	String type;
	Boolean onBoard;
	
	tile()
	{
		image=null;
		type=null;
		onBoard=false;
	}
	
	tile (BufferedImage myImage, String tileType, Boolean isItOnBoard)
	{
		this.image=myImage;
		this.type=tileType;
		this.onBoard=isItOnBoard;
	}
	
	tile (tile t)
	{
		this.image=t.image;
		this.type=t.type;
		this.onBoard=t.onBoard;
	}
	
	public BufferedImage getImage()
	{
		return image;
	}
	
	public String getType()
	{
		return type;
	}
	
	public Boolean getOnBoard()
	{
		return onBoard;
	}
	
	
	public void setImage(BufferedImage newImage)
	{
		image= newImage;
	}
	
	public void setType(String newType)
	{
		type=newType;
	}
	
	public void setOnBoard(Boolean newOnBoard)
	{
		onBoard=newOnBoard;
	}
	
	
	public String toString()
	{
		return String.format("Tile Type: %s %n Is it on the board? %s%n ", type);
	}
	
	public boolean equals(tile other)
	{
		return toString().equals((other.toString()));
	}
	
	public int compareTo(tile otherT) 
    {
    		int compare= getType().compareTo(otherT.getType());
    		
        return compare;
    }
}
