import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.Random;

public class BoardController 
{
	
	private final int TOTAL_TILES= 144;
	//private final int NUMBER_OF_ARRANGEMENTS = 1;
	
	private tile allTiles[]= new tile[TOTAL_TILES];
	private boardPosition[][][] positions;
	
	//private String[] arrangmentFiles = new String[NUMBER_OF_ARRANGEMENTS];
	//private int indexOfCurrentArrangement = 0;
	private boardArrangements currentArrangement;
	private static Random random;
	private List<boardPosition> validTiles = new ArrayList<>();

	public static void main(String[] args) 
	{
		try {
			BoardController controller = new BoardController("simple.txt");
		}catch(IOException e)
		{}
	}
	
	public BoardController(String filename) throws IOException
	{
		currentArrangement = new boardArrangements(new File(filename));
		initializeTiles();
		initializePositions();
		findValidPairs();
		
	}

	/**
	 * Initializes all the tiles -  adds the type and photos to the tile class
	 */
	private void initializeTiles()
	{
		//initialize tiles in a 1D array
		int counter=0;
		int hierarchy=0;
		
		for (int i=0; i<16; i++)
		{
			for (int j=1; j<10; j++)
			{
				if (i<4)
				{
					allTiles[counter] = new tile(null, "Dot "+j, true);
				}
				else if (i>3 && i<8)
				{
					allTiles[counter] = new tile(null, "Bamboo "+j, true);
				}
				else if (i>7 && i<12)
				{
					allTiles[counter] = new tile(null, "Character "+j, true);
				}
				else if (i>11 && i<16)
				{
					if (j==1)
					{
						allTiles[counter] = new tile(null, "North", true);
					}
					if (j==2)
					{
						allTiles[counter] = new tile(null, "South", true);
					}
					if (j==3)
					{
						allTiles[counter] = new tile(null, "East", true);
					}
					if (j==4)
					{
						allTiles[counter] = new tile(null, "West", true);
					}
					if (j==5)
					{
						allTiles[counter] = new tile(null, "Red", true);
					}
					if (j==6)
					{
						allTiles[counter] = new tile(null, "Green", true);
					}
					if (j==7)
					{
						allTiles[counter] = new tile(null, "White", true);
					}
					if (j==8)
					{
						allTiles[counter] = new tile(null, "Flower", true);
					}
					if (j==9)
					{
						allTiles[counter] = new tile(null, "Season", true);
					}
				}
				//Print out the tiles
				//System.out.print(allTiles[counter].toString());
				counter++;
			}
		}
	}

	/**
	 * (1) Adds the initialized tiles to positions (2) Adds the neighbors to each positino (3) assigns the physical location on screen
	 */
	private void initializePositions()
	{
		shuffleTiles();
		positions = new boardPosition[currentArrangement.getHeight()][currentArrangement.getRow()][currentArrangement.getColumn()];
		int counter = 0;
		
		// loop over the number of levels
		for(int l = 0; l < currentArrangement.getHeight(); l++)
		{
			// loop over the rows
			for(int r = 0; r < currentArrangement.getRow(); r++)
			{
				
				// loop over the columns
				for(int c = 0; c < currentArrangement.getColumn(); c++)
				{
					
					if(currentArrangement.getPosition(r,c,l) == 0)
					{
						positions[l][r][c] = null;
					}
					else
					{
						// initializes the positions, sets whether playable, and give physical position on the GUI
						positions[l][r][c] = new boardPosition(allTiles[counter]);
						counter++;
						positions[l][r][c].setPlayable(currentArrangement.getPosition(r, c, l));
						positions[l][r][c].setPosition(r, c, l);
						if(positions[l][r][c].getPlayable())
						{
							validTiles.add(positions[l][r][c]);
						}
						
						//System.out.println(positions.length);
						
						if(c != 0) // if not the first column, link to the position on the left
						{
							//System.out.println("(" + l + "," + r + "," + c + ") West: " + l + "" + r + "" + (c-1));
							if(positions[l][r][c] != null)
								positions[l][r][c].setWestNeighbors(positions[l][r][c-1]);
						}
						if(c != 0 ) // if first column, link prior position to this position (right neighbor)
						{
							//System.out.println("(" + l + "," + r + "," + (c-1) + ") East: " + l + "" + r + "" + (c));
							if(positions[l][r][c-1] != null)
								positions[l][r][c-1].setEastNeighbors(positions[l][r][c]);
						}
						if(l != 0) // if not the bottom layer, link to below
						{
							//System.out.println("l: " + l + "Below: " + (l-1) + "" + r + "" + c);
							if(positions[l][r][c] != null)
								positions[l][r][c].setBelowNeighbors(positions[l-1][r][c]);
						}
					}
					
				}
				
			}
			
		}
		
	}
	
	
	/**
	 * Modified from Collections.shuffle()
	 * @param array
	 */
	private void shuffleTiles() {
        if (random == null) random = new Random();
        int count = allTiles.length;
        for (int i = count; i > 1; i--) {
            swap(i - 1, random.nextInt(i));
        }
    }

	/**
	 * Modified from Collection.shuffle()
	 * @param array
	 * @param i
	 * @param j
	 */
    private void swap(int i, int j) {
        tile temp = allTiles[i];
        allTiles[i] = allTiles[j];
        allTiles[j] = temp;
    }
    
   private int findValidPairs()
   {
	   int counter=0;
	   int validPair=0;
	   boardPosition mySpot=null;
	   Collections.sort( validTiles);
	   while (counter<validTiles.size()-1)
	   {
		   mySpot=validTiles.get(counter);
		   if (mySpot.getThisTile().getType().equals(validTiles.get(counter+1).getThisTile().getType()))
		   {
			   validPair++;
			   counter=counter+2;
		   }
		   else
		   {
			   counter++;
		   }
	   }
	   return validPair;
   }
   
}
