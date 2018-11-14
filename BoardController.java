import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.EventObject;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class BoardController implements MouseListener
{
	
	private final int TOTAL_TILES= 144;
	//private final int NUMBER_OF_ARRANGEMENTS = 1;
	
	private tile allTiles[]= new tile[TOTAL_TILES];
	private boardPosition[][][] positions;
	private boardPosition[] selectedPositions = new boardPosition[2];
	
	//private String[] arrangmentFiles = new String[NUMBER_OF_ARRANGEMENTS];
	//private int indexOfCurrentArrangement = 0;
	private boardArrangements currentArrangement;
	private static Random random;
	private List<boardPosition> validTiles = new ArrayList<>();
	
    private JFrame gameJFrame;
    private Container gameContentPane;
    private BufferedImage tilesImage;
    private int width;
    private int height;
	private Border border = BorderFactory.createLineBorder(Color.BLUE, 5);
	private Border borderYELLOW = BorderFactory.createLineBorder(Color.YELLOW, 5);
	private Border borderRED = BorderFactory.createLineBorder(Color.RED, 5);
	private Border borderGREEN = BorderFactory.createLineBorder(Color.GREEN, 5);
	private Border borderCYAN = BorderFactory.createLineBorder(Color.CYAN, 5);

	private int xMouseOffsetToContentPaneFromJFrame = 0;
    private int yMouseOffsetToContentPaneFromJFrame = 0;

	public static void main(String[] args) 
	{
		try {
			BoardController controller = new BoardController("simple.txt", "Mahjong Solitaire", 700, 700, 20, 20);
		}catch(IOException e)
		{}
	}
	
	public BoardController(String filename, String windowTitle, int windowWidth, int windowHeight, int xlocation, int ylocation) throws IOException
	{
		currentArrangement = new boardArrangements(new File(filename));
		
		tilesImage = ImageIO.read(new FileInputStream(new File("tiles.jpg")));
		this.width = tilesImage.getWidth() / 12;
    	this.height = tilesImage.getHeight() /12;
    	
    	//System.out.print("Width: " + width + "Height: " + height); // 62 x 82
		initializeGUI(windowTitle, windowWidth, windowHeight, xlocation, ylocation);
		initializeTiles();
		initializePositions(gameJFrame);
		drawBoard();
        gameJFrame.setVisible(true);


		findValidPairs();
		
	}

	/**
	 * Initializes all the tiles -  adds the type and photos to the tile class
	 */
	private void initializeTiles()
	{
		for(int n = 0; n<9; n++)
		{
			for(int m = 0; m<4; m++)
			{
				allTiles[12*n+3*m] = new tile(null, "Dot "+(n+1), true);
				allTiles[12*n+3*m].setImage(getIcon(width*n, 0, width, height));
				allTiles[12*n+3*m+1] = new tile(null, "Bamboo "+(n+1), true);
				allTiles[12*n+3*m+1].setImage(getIcon(width*n, 4*height, width, height));
				allTiles[12*n+3*m+2] = new tile(null, "Character"+(n+1), true);
				allTiles[12*n+3*m+2].setImage(getIcon(width*n, 8*height, width, height));
			}
		}
		for(int m = 0; m<4; m++)
		{	
			allTiles[9*m+108] = new tile(null, "Season", true);
			allTiles[9*m+108].setImage(new ImageIcon(tilesImage.getSubimage(11*width, 8*height +m*height, width, height)));
			allTiles[9*m+109] = new tile(null, "Flower", true);
			allTiles[9*m+109].setImage(new ImageIcon(tilesImage.getSubimage(11*width, 4*height + m*height, width, height)));
			
			allTiles[9*m+110] = new tile(null, "North", true);
			allTiles[9*m+110].setImage(getIcon(10*width, 8*height + m*height, width, height));
			allTiles[9*m+111] = new tile(null, "South", true);
			allTiles[9*m+111].setImage(getIcon(10*width, 4*height + m*height, width, height));
			allTiles[9*m+112] = new tile(null, "East", true);
			allTiles[9*m+112].setImage(getIcon(9*width, 4*height + m*height, width, height));
			allTiles[9*m+113] = new tile(null, "West", true);
			allTiles[9*m+113].setImage(getIcon(9*width, 8*height + m*height, width, height));
			
			allTiles[9*m+114] = new tile(null, "Red", true);
			allTiles[9*m+114].setImage(getIcon(9*width, m*height, width, height));
			allTiles[9*m+115] = new tile(null, "Green", true);
			allTiles[9*m+115].setImage(getIcon(10*width, m*height, width, height));
			allTiles[9*m+116] = new tile(null, "White", true);
			allTiles[9*m+116].setImage(getIcon(11*width, m*height, width, height));
			
		}
	}
	
	private ImageIcon getIcon(int sw, int sh, int w , int h)
	{
		return (new ImageIcon(tilesImage.getSubimage(sw, sh, w, h)));
	}

	/**
	 * (1) Adds the initialized tiles to positions (2) Adds the neighbors to each positino (3) assigns the physical location on screen
	 */
	private void initializePositions(JFrame gameJFrame)
	{
//		shuffleTiles();
		positions = new boardPosition[currentArrangement.getHeight()][currentArrangement.getRow()][currentArrangement.getColumn()];
		int counter = 0;
//		System.out.println(currentArrangement.getRow() + " " + currentArrangement.getColumn());
		
		// loop over the number of levels currentArrangement.getHeight()
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
						positions[l][r][c] = new boardPosition(allTiles[counter], gameJFrame);
						counter++;
						positions[l][r][c].setPlayable(currentArrangement.getPosition(r, c, l));
						positions[l][r][c].setPosition(width*r, height*c, l);
//						System.out.println(positions[l][r][c].toString() + " isPlayable" + currentArrangement.getPosition(r, c, l));
						if(positions[l][r][c].getPlayable())
						{
							validTiles.add(positions[l][r][c]);
						}
						
						//System.out.println(positions.length);
						
						if(r != 0) // if not the first column, link to the position on the left
						{
//							System.out.println("(" + l + "," + r + "," + c + ") West: " + l + "" + r + "" + (c-1));
							if(positions[l][r-1][c] != null)
							{
								//positions[l][r][c].setWestNeighbors(positions[l][r][c-1]);
								positions[l][r][c].setWestNeighbors(positions[l][r-1][c]);
//								System.out.println(positions[l][r][c].toString() + " West N: " + positions[l][r-1][c].toString());
							}
						}
						if(r != 0) //currentArrangement.getRow()-1 ) // if first column, link prior position to this position (right neighbor)
						{
							//System.out.println("(" + l + "," + r + "," + (c-1) + ") East: " + l + "" + r + "" + (c));
							if(positions[l][r-1][c] != null)
							{
								//positions[l][r][c-1].setEastNeighbors(positions[l][r][c]);
								positions[l][r-1][c].setEastNeighbors(positions[l][r][c]);
//								System.out.println(positions[l][r-1][c].toString() + " East N: " + positions[l][r][c].toString());
							}
						}
						if(l != 0) // if not the bottom layer, link to below
						{
							//System.out.println("l: " + l + "Below: " + (l-1) + "" + r + "" + c);
							if(positions[l-1][r][c] != null)
							{
								positions[l][r][c].setBelowNeighbors(positions[l-1][r][c]);
								positions[l-1][r][c].setAboveNeighbors(positions[l][r][c]);
//								System.out.println(positions[l][r][c].toString() + " Below N: " + positions[l-1][r][c].toString());
							}
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
		   if (mySpot.equals(validTiles.get(counter+1)))
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

    private void initializeGUI(String windowTitle, int width, int height, int xlocation, int ylocation)
    {
        gameJFrame = new JFrame(windowTitle);
        gameJFrame.setSize(width, height);
        gameJFrame.setLocation(xlocation, ylocation);
        gameJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameContentPane = gameJFrame.getContentPane();
        gameContentPane.setLayout(null); // not need layout, will use absolute system
        gameContentPane.setBackground(Color.gray);
        gameContentPane.setBounds(10, 10, width-10, height-10);
        gameContentPane.addMouseListener(this);

        // Event mouse position is given relative to JFrame, where dolphin's image in JLabel is given relative to ContentPane,
        //  so adjust for the border
        int borderWidth = (width - gameContentPane.getWidth())/2;  // 2 since border on either side
        xMouseOffsetToContentPaneFromJFrame = borderWidth;
        yMouseOffsetToContentPaneFromJFrame = height - gameContentPane.getHeight()-borderWidth; // assume side border = bottom border; ignore title bar
    }
    
    private void drawBoard()
    {
    	
    	for(int l = positions.length-1; l > -1; l--)
    //	for(int l = 0; l < positions.length; l++)
		{
			// loop over the rows
			for(int r = 0; r < positions[l].length; r++)
			{
				
				// loop over the columns
				for(int c = 0; c < positions[l][r].length; c++)
				{
					
					if(positions[l][r][c] != null)
					{
						if(positions[l][r][c].getThisTile().getOnBoard())
						{
							if(positions[l][r][c].getPlayable())
							{
								gameContentPane.add(positions[l][r][c].drawPosition(border),-1);
							}
							else
							{
								gameContentPane.add(positions[l][r][c].drawPosition(),-1);
							}

							gameContentPane.add(positions[l][r][c].drawShadow(),-1);
						}
					}
				}
			}
		}
	}
    
   
    private void afterMatch(int index)
    {
    	selectedPositions[index].remove();
    	gameContentPane.remove(selectedPositions[index].getJLabel());
		
		validTiles.remove(selectedPositions[index]);
		
		selectedPositions[index].notifyNeighbors(false);
	
		if(selectedPositions[index].getEastNeighbors() != null)
		{
			validTiles.add(selectedPositions[index].getEastNeighbors());
			
			if(selectedPositions[index].getEastNeighbors().getPlayable())
			{
				System.out.println(selectedPositions[index].getEastNeighbors().getThisTile().getType());
				selectedPositions[index].getEastNeighbors().getJLabel();
//				gameContentPane.add(selectedPositions[index].getEastNeighbors().drawPosition(border),-1);
				for(int l = currentArrangement.getHeight()-1; l > -1; l--)
				{
					if(positions[l][selectedPositions[index].getEastNeighbors().getX()/width][selectedPositions[index].getEastNeighbors().getY()/height] != null)
					{
						gameContentPane.remove(positions[l][selectedPositions[index].getEastNeighbors().getX()/width][selectedPositions[index].getEastNeighbors().getY()/height].getJLabel());
						gameContentPane.add(positions[l][selectedPositions[index].getEastNeighbors().getX()/width][selectedPositions[index].getEastNeighbors().getY()/height].drawPosition(border),-1);
					}
					
				}

			}
		}
		if(selectedPositions[index].getWestNeighbors() != null)
		{
			validTiles.add(selectedPositions[index].getWestNeighbors());
//			gameContentPane.remove(selectedPositions[index].getWestNeighbors().getJLabel());
//			gameContentPane.add(selectedPositions[index].getWestNeighbors().drawPosition(border),-1);
		}
		if(selectedPositions[index].getPlayableBelowNeighbors() != null)
		{
			validTiles.add(selectedPositions[index].getPlayableBelowNeighbors());
//			gameContentPane.remove(selectedPositions[index].getBelowNeighbors().getJLabel());
//			gameContentPane.add(selectedPositions[index].getBelowNeighbors().drawPosition(border),-1);
		}
    }

    
	@Override
	public void mouseClicked(MouseEvent event) {
		
		// loop over the levels
		for(int l = 0; l < positions.length; l++)
		{
			// loop over the rows
			for(int r = 0; r < positions[l].length; r++)
			{
				
				// loop over the columns
				for(int c = 0; c < positions[l][r].length; c++)
				{
					if(positions[l][r][c] != null)
					{
						if(positions[l][r][c].wasSelected(event.getX()-xMouseOffsetToContentPaneFromJFrame, event.getY() - yMouseOffsetToContentPaneFromJFrame))
						{
//							System.out.println(event.getX() + " " + event.getY());
//							System.out.println("Selected: " + positions[l][r][c].toString());
							if(selectedPositions[0] == null)
							{
								selectedPositions[0] = positions[l][r][c];
								System.out.println("Found First Tile");
							}else if (selectedPositions[0].differentCoordinates(positions[l][r][c]))
							{
								selectedPositions[1] = positions[l][r][c];
								c = positions[l][r].length-1;
								r = positions[l].length-1;
								l = positions.length-1;
								System.out.println("Found Second Tile");
							}
						}
					}
				}
			}
		}
		
		if(selectedPositions[1] != null) {
			if(selectedPositions[0].equals(selectedPositions[1]))
			{
				System.out.println("Match!");
				afterMatch(0);
				afterMatch(1);
		    	gameContentPane.repaint();

			}
			// check if valid
			selectedPositions[0] = null;
			selectedPositions[1] = null;
		} 
		
	}

	@Override
	public void mousePressed(MouseEvent event) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
    
    
}
