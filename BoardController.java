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
    private int printBuffer = 4;
    private int XOffset = 25;
    private int YOffset = 25;

	public static void main(String[] args) 
	{
		try {
			BoardController controller = new BoardController("simple.txt", "Mahjong Solitaire", 750, 700, 20, 20);
		}catch(IOException e)
		{}
	}
	
	public BoardController(String filename, String windowTitle, int windowWidth, int windowHeight, int xlocation, int ylocation) throws IOException
	{
		currentArrangement = new boardArrangements(new File(filename));
		width=120;
		height=180;
		//tilesImage = ImageIO.read(new FileInputStream(new File("tiles.jpg")));
		//this.width = tilesImage.getWidth() / 12;
   // 	this.height = tilesImage.getHeight() /12;
    	
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
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private void initializeTiles() throws FileNotFoundException, IOException
	{
		for (int a=0; a<4; a++) //total of 108 number tiles
		{
			for (int i=0; i<9; i++)
			{
				allTiles[(a*27)+3*i] = new tile(null, "Dot "+(i+1), true);
				allTiles[(a*27)+i*3].setImage(new ImageIcon(ImageIO.read(new FileInputStream(new File("circle"+(i+1)+".png")))));
				allTiles[(a*27)+i*3+1] = new tile(null, "Bamboo "+(i+1), true);
				allTiles[(a*27)+i*3+1].setImage(new ImageIcon(ImageIO.read(new FileInputStream(new File("bamboo"+(i+1)+".png")))));
				allTiles[(a*27)+i*3+2] = new tile(null, "Character"+(i+1), true);
				allTiles[(a*27)+i*3+2].setImage(new ImageIcon(ImageIO.read(new FileInputStream(new File("char"+(i+1)+".png")))));
			}
		}
		
		for (int a=0; a<4; a++)
		{
			allTiles[108+7*a]= new tile(null, "Red", true);
			allTiles[108+7*a].setImage(new ImageIcon(ImageIO.read(new FileInputStream(new File("red.png")))));
			allTiles[108+7*a+1]= new tile(null, "Green", true);
			allTiles[108+7*a+1].setImage(new ImageIcon(ImageIO.read(new FileInputStream(new File("green.png")))));
			allTiles[108+7*a+2]= new tile(null, "Black", true);
			allTiles[108+7*a+2].setImage(new ImageIcon(ImageIO.read(new FileInputStream(new File("black.png")))));
			allTiles[108+7*a+3]= new tile(null, "North", true);
			allTiles[108+7*a+3].setImage(new ImageIcon(ImageIO.read(new FileInputStream(new File("north.png")))));
			allTiles[108+7*a+4]= new tile(null, "South", true);
			allTiles[108+7*a+4].setImage(new ImageIcon(ImageIO.read(new FileInputStream(new File("south.png")))));
			allTiles[108+7*a+5]= new tile(null, "East", true);
			allTiles[108+7*a+5].setImage(new ImageIcon(ImageIO.read(new FileInputStream(new File("east.png")))));
			allTiles[108+7*a+6]= new tile(null, "West", true);
			allTiles[108+7*a+6].setImage(new ImageIcon(ImageIO.read(new FileInputStream(new File("west.png")))));	
		}
		allTiles[136]= new tile(null, "Season", true);
		allTiles[136].setImage(new ImageIcon(ImageIO.read(new FileInputStream(new File("fall.png")))));	
		allTiles[137]= new tile(null, "Season", true);
		allTiles[137].setImage(new ImageIcon(ImageIO.read(new FileInputStream(new File("summer.png")))));	
		allTiles[138]= new tile(null, "Season", true);
		allTiles[138].setImage(new ImageIcon(ImageIO.read(new FileInputStream(new File("spring.png")))));	
		allTiles[139]= new tile(null, "Season", true);
		allTiles[139].setImage(new ImageIcon(ImageIO.read(new FileInputStream(new File("winter.png")))));	
		allTiles[140]= new tile(null, "Plant", true);
		allTiles[140].setImage(new ImageIcon(ImageIO.read(new FileInputStream(new File("bamboo.png")))));	
		allTiles[141]= new tile(null, "Plant", true);
		allTiles[141].setImage(new ImageIcon(ImageIO.read(new FileInputStream(new File("lilly.png")))));	
		allTiles[142]= new tile(null, "Plant", true);
		allTiles[142].setImage(new ImageIcon(ImageIO.read(new FileInputStream(new File("plum.png")))));	
		allTiles[143]= new tile(null, "Plant", true);
		allTiles[143].setImage(new ImageIcon(ImageIO.read(new FileInputStream(new File("rose.png")))));	
		System.out.print("Complete");
		
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
						positions[l][r][c].setPosition(width*r + r*printBuffer + XOffset, height*c + c*printBuffer + YOffset, l);
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
        gameContentPane.setBackground(Color.LIGHT_GRAY);
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
							gameContentPane.add(positions[l][r][c].drawPositionWithBorder(),-1);
							if(positions[l][r][c].getPlayable())
							{
								gameContentPane.add(positions[l][r][c].drawShadow(currentArrangement.getRow(), currentArrangement.getColumn()),-1);
							}
						}
					}
				}
			}
		}
	}
    
   
    private void afterMatch(int index)
    {
    	selectedPositions[index].remove();
		
		validTiles.remove(selectedPositions[index]);
		
		selectedPositions[index].notifyNeighbors(false);
		gameContentPane.removeAll();
		drawBoard();
		validTiles.add(selectedPositions[index].getEastNeighbors());
		validTiles.add(selectedPositions[index].getWestNeighbors());
		validTiles.add(selectedPositions[index].getPlayableBelowNeighbors());

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
