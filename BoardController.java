import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Random;

public class BoardController 
{
	
	private final int TOTAL_TILES= 144;
	private final int NUMBER_OF_ARRANGEMENTS = 1;
	
	private tile allTiles[]= new tile[TOTAL_TILES];
	private boardPosition[][][] positions;
	
	private String[] arrangmentFiles = new String[NUMBER_OF_ARRANGEMENTS];
	private int indexOfCurrentArrangement = 0;
	private boardArrangements currentArrangement;
	private ListInterface<String> validTiles= new ArrayList<>();
	
	private static Random random;

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
		System.out.print("done");
	}

	/**
	 * Initializes all the tiles -  adds the type and photos to the tile class
	 */
	private void initializeTiles()
	{
		//initialize tiles in a 1D array
		int counter=0;
		
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
						positions[l][r][c] = new boardPosition();
						
						if(c != 0) // if not the first column, link to the position on the left
						{
							positions[l][r][c].setWestNeighbors(positions[l][r][c-1]);
						}
						if(c != currentArrangement.getColumn()) // if not last column, link prior position to this position (right neighbor)
						{
							positions[l][r][c-1].setEastNeighbors(positions[l][r][c]);
						}
						if(l != 0) // if not the bottom layer, link to below
						{
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
    

}
