import java.awt.image.BufferedImage;

public class tile 
{
	BufferedImage image;
	String type;
	Boolean onBoard;
	Boolean playable;
	
	tile()
	{
		image=null;
		type=null;
		onBoard=false;
		playable=false;
	}
	
	tile (BufferedImage myImage, String tileType, Boolean isItOnBoard, Boolean isItPlayable)
	{
		this.image=myImage;
		this.type=tileType;
		this.onBoard=isItOnBoard;
		this.playable=isItPlayable;
	}
	
	tile (tile t)
	{
		this.image=t.image;
		this.type=t.type;
		this.onBoard=t.onBoard;
		this.playable=t.playable;
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
	
	public Boolean getPlayable()
	{
		return playable;
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
	
	public void setPlayable(Boolean newPlayable)
	{
		playable=newPlayable;
	}
	
	public String toString()
	{
		return String.format("Tile Type: %s%n Is it Playable?: %s%n Is it on the board? %s%n ", type, onBoard,playable);
	}
	
	public int compareTo(tile other)
	{
		return type.compareTo(other.type);
	}
}
