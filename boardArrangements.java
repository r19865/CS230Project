import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class boardArrangements 
{
	private boardArrangements[] arrangement = new boardArrangements[10];
	private boardArrangements thisArrangement;
	private int xsize;
	private int ysize;
	private int zsize;
	private String[] lineRead=new String[ysize];
	private int[][][] layout;
	
	public boardArrangements()
	{
		xsize=0;
		ysize=0;
		zsize=0;
	}
	
	public boardArrangements(int x,int y, int z)
	{
		this.xsize=0;
		this.ysize=0;
		this.zsize=0;
	}
	
	public boardArrangements(boardArrangements a)
	{
		this.xsize=a.xsize;
		this.ysize=a.ysize;
		this.zsize=a.zsize;
	}
	
	public boardArrangements(File myFile) throws IOException
	{
		readFile(myFile);
	}
	
	private void readFile(File theFile) throws IOException
	{
		FileReader reader = new FileReader(theFile);
		BufferedReader br = new BufferedReader(reader);
		String dimension= br.readLine();
		String [] size=dimension.split(" ");
		ysize=Integer.parseInt(size[0]);
		xsize=Integer.parseInt(size[1]);
		zsize=Integer.parseInt(size[2]);

		layout = new int[xsize][ysize][zsize];

		for (int i=0; i<zsize; i++)//zsize
		{
			br.readLine();
			for (int j=0; j<ysize; j++) //ysize
			{
				//lineRead[j]=Integer.parseInt(br.readLine());
				String nextLine=br.readLine();
				char[] temp= nextLine.toCharArray();

				for (int k=0; k<xsize; k++) //xsize
				{
					layout[k][j][i] = Character.getNumericValue(temp[k]);
				}
				
			}
		}
		return;
	}
	
	//get set row
	public int getRow()
	{
		return xsize;
	}
	
	public void setRow(int i)
	{
		xsize=i;
	}
	
	public int getColumn()
	{
		return ysize;
	}
	
	public void setColumn(int j)
	{
		ysize=j;
	}
	
	public int getHeight()
	{
		return zsize;
	}
	
	public void setHeight(int k)
	{
		zsize=k;
	}
	
	public int getPosition(int i,int j, int k)
	{
		return layout[i][j][k];
	}
	
}
