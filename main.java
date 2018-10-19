public class main 
{

	public static void main(String[] args) 
	{
		//initialize tiles in a 1D array
		int totalTiles= 144;
		tile allTiles[]= new tile[totalTiles];
		int counter=0;
		
		for (int i=0; i<16; i++)
		{
			for (int j=1; j<10; j++)
			{
				if (i<4)
				{
					allTiles[counter] = new tile(null, "Dot "+j, true, true);
				}
				else if (i>3 && i<8)
				{
					allTiles[counter] = new tile(null, "Bamboo "+j, true, true);
				}
				else if (i>7 && i<12)
				{
					allTiles[counter] = new tile(null, "Character "+j, true, true);
				}
				else if (i>11 && i<16)
				{
					if (j==1)
					{
						allTiles[counter] = new tile(null, "North", true, true);
					}
					if (j==2)
					{
						allTiles[counter] = new tile(null, "South", true, true);
					}
					if (j==3)
					{
						allTiles[counter] = new tile(null, "East", true, true);
					}
					if (j==4)
					{
						allTiles[counter] = new tile(null, "West", true, true);
					}
					if (j==5)
					{
						allTiles[counter] = new tile(null, "Red", true, true);
					}
					if (j==6)
					{
						allTiles[counter] = new tile(null, "Green", true, true);
					}
					if (j==7)
					{
						allTiles[counter] = new tile(null, "White", true, true);
					}
					if (j==8)
					{
						allTiles[counter] = new tile(null, "Flower", true, true);
					}
					if (j==9)
					{
						allTiles[counter] = new tile(null, "Season", true, true);
					}
				}
				//Print out the tiles
				//System.out.print(allTiles[counter].toString());
				counter++;
			}
		}
		
	}

}
