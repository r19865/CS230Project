import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

public class tile implements Comparable<tile>
{
	private ImageIcon image;
	private String type;
	private Boolean onBoard;
	
	tile()
	{
		image=null;
		type=null;
		onBoard=false;
	}
	
	tile (ImageIcon myImage, String tileType, Boolean isItOnBoard)
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
	
	public ImageIcon getImage()
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
	
	
	public void setImage(ImageIcon newImage)
	{
		image= newImage;
	}
	
	public void setImage(BufferedImage newImage, int newW, int newH)
	{
		Image tmp = newImage.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		image = new ImageIcon(tmp);
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
		return String.format("Tile Type: %s %n Is it on the board? %s%n ", type, onBoard);
	}
	
	public boolean equals(tile other)
	{
		return type.equals(other.getType());
	}
	
	public int compareTo(tile otherT) 
    {
    		int compare= getType().compareTo(otherT.getType());
    		
        return compare;
    }
}
