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
import java.util.Date;
import java.util.EventObject;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
    private JLabel timerLabel;
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
    static long startTime=0;
    private java.util.Timer gameTimer = new java.util.Timer();
    private long timerOff=0;
    private Date oldTime=null;
    private Date newTime=null;
    public static final int shufflingTileTime = 700; 
    
	public static void main(String[] args) 
	{
		try {
			BoardController controller = new BoardController("simple.txt", "Mahjong Solitaire", 750, 700, 20, 20);
			//startTime = System.currentTimeMillis();
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	
	public BoardController(String filename, String windowTitle, int windowWidth, int windowHeight, int xlocation, int ylocation) throws IOException
	{
		currentArrangement = new boardArrangements(new File(filename));
		width=62;
		height=82;
		//timer = new Timer();
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
        oldTime= new Date();
        
        TimerTask repeatedTask = new TimerTask() {
            public void run() 
            {
            		Date trash=new Date();
            		newTime=trash;
            		timerOff=Math.abs(newTime.getTime()-oldTime.getTime());
                System.out.println("Task performed on " + timerOff+ new Date()+ newTime.getSeconds());
            }
        };
        Timer timer = new Timer("Timer");
         
        long delay  = 1000L;
        long period = 1000L;
        timer.scheduleAtFixedRate(repeatedTask, delay, period);
		
	}

	/**
	 * Initializes all the tiles -  adds the type and photos to the tile class
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private void initializeTiles() throws FileNotFoundException, IOException
	{
		String directory = "mahjong/";
		for (int a=0; a<4; a++) //total of 108 number tiles
		{
			for (int i=0; i<9; i++)
			{
				allTiles[(a*27)+3*i] = new tile(null, "Dot "+(i+1), true);
				allTiles[(a*27)+i*3].setImage(ImageIO.read(new File(directory+"circle"+(i+1)+".png")), width, height);
				allTiles[(a*27)+i*3+1] = new tile(null, "Bamboo "+(i+1), true);
				allTiles[(a*27)+i*3+1].setImage(ImageIO.read(new File(directory+"bamboo"+(i+1)+".png")), width, height);
				allTiles[(a*27)+i*3+2] = new tile(null, "Character"+(i+1), true);
				allTiles[(a*27)+i*3+2].setImage(ImageIO.read(new File(directory+"char"+(i+1)+".png")), width, height);
			}
		}
		
		for (int a=0; a<4; a++)
		{
			allTiles[108+7*a]= new tile(null, "Red", true);
			allTiles[108+7*a].setImage(ImageIO.read(new File(directory+"red.png")), width, height);
			allTiles[108+7*a+1]= new tile(null, "Green", true);
			allTiles[108+7*a+1].setImage(ImageIO.read(new File(directory+"green.png")), width, height);
			allTiles[108+7*a+2]= new tile(null, "Black", true);
			allTiles[108+7*a+2].setImage(ImageIO.read(new File(directory+"black.png")), width, height);
			allTiles[108+7*a+3]= new tile(null, "North", true);
			allTiles[108+7*a+3].setImage(ImageIO.read(new File(directory+"north.png")), width, height);
			allTiles[108+7*a+4]= new tile(null, "South", true);
			allTiles[108+7*a+4].setImage(ImageIO.read(new File(directory+"south.png")), width, height);
			allTiles[108+7*a+5]= new tile(null, "East", true);
			allTiles[108+7*a+5].setImage(ImageIO.read(new File(directory+"east.png")), width, height);
			allTiles[108+7*a+6]= new tile(null, "West", true);
			allTiles[108+7*a+6].setImage(ImageIO.read(new File(directory+"west.png")), width, height);	
		}
		allTiles[136]= new tile(null, "Season", true);
		allTiles[136].setImage(ImageIO.read(new File(directory+"fall.png")), width, height);	
		allTiles[137]= new tile(null, "Season", true);
		allTiles[137].setImage(ImageIO.read(new File(directory+"summer.png")), width, height);	
		allTiles[138]= new tile(null, "Season", true);
		allTiles[138].setImage(ImageIO.read(new File(directory+"spring.png")), width, height);	
		allTiles[139]= new tile(null, "Season", true);
		allTiles[139].setImage(ImageIO.read(new File(directory+"winter.png")), width, height);	
		allTiles[140]= new tile(null, "Plant", true);
		allTiles[140].setImage(ImageIO.read(new File(directory+"bamboo.png")), width, height);	
		allTiles[141]= new tile(null, "Plant", true);
		allTiles[141].setImage(ImageIO.read(new File(directory+"lilly.png")), width, height);	
		allTiles[142]= new tile(null, "Plant", true);
		allTiles[142].setImage(ImageIO.read(new File(directory+"plum.png")), width, height);	
		allTiles[143]= new tile(null, "Plant", true);
		allTiles[143].setImage(ImageIO.read(new File(directory+"rose.png")), width, height);	
		System.out.print("Complete");
		
	}

	/**
	 * (1) Adds the initialized tiles to positions (2) Adds the neighbors to each positino (3) assigns the physical location on screen
	 */
	private void initializePositions(JFrame gameJFrame)
	{
		shuffleTiles();
		positions = new boardPosition[currentArrangement.getHeight()][currentArrangement.getRow()][currentArrangement.getColumn()];
		int counter = 0;
		
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

						if(positions[l][r][c].getPlayable())
						{
							validTiles.add(positions[l][r][c]);
						}
												
						if(r != 0) // if not the first column, link to the position on the left
						{
							if(positions[l][r-1][c] != null)
							{
								positions[l][r][c].setWestNeighbors(positions[l][r-1][c]);
							}
						}
						if(r != 0) //currentArrangement.getRow()-1 ) // if first column, link prior position to this position (right neighbor)
						{
							if(positions[l][r-1][c] != null)
							{
								positions[l][r-1][c].setEastNeighbors(positions[l][r][c]);
							}
						}
						if(l != 0) // if not the bottom layer, link to below
						{
							//System.out.println("l: " + l + "Below: " + (l-1) + "" + r + "" + c);
							if(positions[l-1][r][c] != null)
							{
								positions[l][r][c].setBelowNeighbors(positions[l-1][r][c]);
								positions[l-1][r][c].setAboveNeighbors(positions[l][r][c]);
							}
						}
						
						System.out.println(positions[l][r][c]);
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
        timerLabel= new JLabel("0");
        timerLabel.setBounds(0,0,50,20);
        timerLabel.setVisible(true);
        gameContentPane.add(timerLabel);
        
       // gameTimer.schedule(this, 0, shufflingTileTime); 

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
    	timerLabel.setText(Long.toString(timerOff));
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
							if(selectedPositions[0] == null)
							{
								selectedPositions[0] = positions[l][r][c];
								System.out.println("First " + positions[l][r][c].getThisTile().getType());
							}else if (selectedPositions[0].differentCoordinates(positions[l][r][c]))
							{
								System.out.println("Second " + positions[l][r][c].getThisTile().getType());
								selectedPositions[1] = positions[l][r][c];
								c = positions[l][r].length-1;
								r = positions[l].length-1;
								l = positions.length-1;
							}
							else if(positions[l][r][c].equals(selectedPositions[0]))
							{
								selectedPositions[0].deselect();
								selectedPositions[0] = null;
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
			else
			{
				selectedPositions[0].deselect();
				selectedPositions[1].deselect();
			}
			// check if valid
			selectedPositions[0] = null;
			selectedPositions[1] = null;
		} 
		
		System.out.println(selectedPositions[1] + " " + selectedPositions[0]);
		
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
