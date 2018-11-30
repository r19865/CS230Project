import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
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
import javax.swing.ImageIcon;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JOptionPane;

public class BoardController extends TimerTask implements MouseListener, ActionListener
{
	
	private final int TOTAL_TILES= 144;
	private final String[] options = {"Yes, please", "No, thank you"};
	private final int NUMBEROFMATCHES = 72;
	private final int TIMERSCHEDULE = 500;
	private final int DEFAULTSHUFFLETIME = 12500; 
	private final String[] levels =  {"Super Easy", "Easy", "Eh, It's Aight", "Average", "Hard", "Very Hard", "This is Literally Impossible"};
	private final String[] arrangements = {"Bowtie", "Castle", "Carroll College", "Pyramid"};
	private final String[] files = {"bowtie.txt", "castle.txt", "cc.txt", "simple.txt"};
	private final int[][] offsets = { {175, 200}, {350,125}, {75, 125}, {250, 100} };

	
	private tile allTiles[]= new tile[TOTAL_TILES];
	private boardPosition[][][] positions;
	private boardPosition[] selectedPositions = new boardPosition[2];
	
	private boardArrangements currentArrangement;
	private static Random random;
	private List<boardPosition> validTiles = new ArrayList<>();
	private int[] validTilesCounter;
	private Random generator = new Random();
	private Stack<boardPosition> matchedTiles = new Stack<>();
	
    private JFrame gameJFrame;
    private JLabel timerLabel;
    private Container gameContentPane;
    private JLabel validPairsLabel;
    private JButton undoButton;
    private JMenuItem helpMenuItem;
    private int width;
    private int height;

	private int xMouseOffsetToContentPaneFromJFrame = 0;
    private int yMouseOffsetToContentPaneFromJFrame = 0;
    private int printBuffer = 4;
    private int XOffset = 100;
    private int YOffset = 100;

    private java.util.Timer gameTimer = new java.util.Timer();
    private int timeCounter=0;
    private boolean gameIsReady = false;
    public int shuffleTime = DEFAULTSHUFFLETIME;

    private int numberOfCompletedMatches = 0;

	public static void main(String[] args) 
	{
		try {
			BoardController controller = new BoardController("Mahjong Solitaire", 900, 800, 20, 20);
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	public BoardController(String windowTitle, int windowWidth, int windowHeight, int xlocation, int ylocation) throws IOException
	{
		width=62;
		height=82;

		//tilesImage = ImageIO.read(new FileInputStream(new File("tiles.jpg")));
		//this.width = tilesImage.getWidth() / 12;
   // 	this.height = tilesImage.getHeight() /12;
    	
    	//System.out.print("Width: " + width + "Height: " + height); // 62 x 82

		chooseArrangmentAndDifficulty();

		initializeGUI(windowTitle, windowWidth, windowHeight, xlocation, ylocation);
		initializeTiles();
		initializePositions();
		drawBoard();
		
		gameIsReady=true;
		gameJFrame.setVisible(true);
        findValidPairs();
        gameTimer.schedule(this, 0, TIMERSCHEDULE);
        gameJFrame.setVisible(true);
		
		
	}

	public void run() 
	{ 
		timeCounter++;
		if (gameIsReady)
		{
	    	if(timeCounter%2 == 0)
	    	{
				timerLabel.setText("Time: " + Integer.toString(timeCounter/2) + " sec");
		    	timerLabel.repaint();
	    	}
	    	
	    	if(TIMERSCHEDULE*timeCounter % shuffleTime == 0)
	    	{
		    	boardPosition first = validTiles.get(getRandomInt(validTiles.size()));
	
		    	if(first.getZ() > 1)
		    	{
		    		first.switchNeighbors(positions[getRandomInt(first.getZ())][first.getArrayX()][first.getArrayY()]);
		    	}else if(first.getZ() > 0)
		    	{
		    		first.switchNeighbors(positions[0][first.getArrayX()][first.getArrayY()]);
		    	}
		    	
		    	first.getJLabel().repaint();
	    	}
		}
		       
	}
	
	private int getRandomInt(int below)
	{
		return generator.nextInt(below);
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
//		System.out.print("Complete");
		
	}

	/**
	 * (1) Adds the initialized tiles to positions (2) Adds the neighbors to each positino (3) assigns the physical location on screen
	 */
	private void initializePositions()
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
						positions[l][r][c].setArrayPosition(r,c);
						
						if(l > 0)
							positions[l][r][c].setPosition(width*r - (2*l)*printBuffer + XOffset, height*c - (2*l)*printBuffer + YOffset, l);
						else
							positions[l][r][c].setPosition(width*r + XOffset, height*c + YOffset, l);
						
						positions[l][r][c].getThisTile().setOnBoard(true);

						if(positions[l][r][c].getPlayable())
						{
							validTiles.add(positions[l][r][c]);
							validTilesCounter[l]++;
						}
												
						if(r != 0) // if not the first column, link to the position on the left
						{
							if(positions[l][r-1][c] != null)
							{
								positions[l][r][c].setWestNeighbors(positions[l][r-1][c]);
							}
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
						if(c != 0)
						{
							if(positions[l][r][c-1] != null)
							{
//								System.out.print(positions[l][r][c-1]);
								positions[l][r][c-1].setSouthNeighbors(positions[l][r][c]);
//								System.out.println(positions[l][r][c]+ " " +(positions[l][r][c-1]));
							}
						}
						
//						System.out.println(positions[l][r][c]);
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
	   validTiles.removeAll(Arrays.asList("", null));
	   Collections.sort(validTiles);
	   
	   while (counter<validTiles.size()-1)
	   {
		   if (validTiles.get(counter).equals(validTiles.get(counter+1)))
		   {
			   validPair++;
//			   System.out.println(validTiles.get(counter).toString() + " " + validTiles.get(counter+1).toString());
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

        timerLabel= new JLabel();
        timerLabel.setLocation(425,5);
        timerLabel.setSize(300, 40);
        timerLabel.setFont(new Font(timerLabel.getName(), Font.PLAIN, 24));
        gameContentPane.add(timerLabel);

        
        validPairsLabel = new JLabel();
        validPairsLabel.setLocation(8, 5);
        validPairsLabel.setSize(300, 40);
        validPairsLabel.setFont(new Font(validPairsLabel.getName(), Font.PLAIN, 24));
        gameContentPane.add(validPairsLabel);

        
        undoButton = new JButton();
        undoButton.setText("Undo");
        undoButton.setSize(150, 40);
        undoButton.setFont(validPairsLabel.getFont());
        undoButton.setLocation(gameJFrame.getWidth() - undoButton.getWidth() - 25, 5);
        undoButton.addActionListener(this);
        gameContentPane.add(undoButton);
        
        JMenuBar menuBar  = new JMenuBar();
        menuBar.setBounds(0,0, gameJFrame.getWidth(), 20);
        gameJFrame.setJMenuBar(menuBar);
        
        JMenu menu = new JMenu("File");
        menuBar.add(menu);
        
        helpMenuItem = new JMenuItem("How To Play Mahjong");
        helpMenuItem.addActionListener(this);
        menu.add(helpMenuItem);
        
        
        // Event mouse position is given relative to JFrame, where dolphin's image in JLabel is given relative to ContentPane,
        //  so adjust for the border
        int borderWidth = (width - gameContentPane.getWidth())/2;  // 2 since border on either side
        xMouseOffsetToContentPaneFromJFrame = borderWidth;
        yMouseOffsetToContentPaneFromJFrame = height - gameContentPane.getHeight()-borderWidth; // assume side border = bottom border; ignore title bar
        //gameIsReady=true;
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
//							if(positions[l][r][c].getPlayable())
//							{
								gameContentPane.add(positions[l][r][c].drawShadow(),-1);
//							}
						}
					}
				}
			}
		}
    	
    	gameContentPane.add(undoButton);
    	gameContentPane.add(validPairsLabel);
    	int valid = findValidPairs();
    	validPairsLabel.setText("Valid Pairs On Board: " + valid);
    	gameContentPane.add(timerLabel);
    	if(valid == 0)
    	{
    		endOfGameDialog();
    	}
    	
	}
    
    private synchronized void updateBoard()
    {
		gameIsReady=false;
    	gameContentPane.removeAll();
    	drawBoard();
    	gameContentPane.repaint();
    	gameIsReady=true;
    }
    
    private void chooseArrangmentAndDifficulty()
    {
		try {
			layoutDialog();
		} catch (IOException e) {
			e.printStackTrace();
		}
		validTilesCounter = new int[currentArrangement.getHeight()];
		DifficultyDialog();
    }
   
    private void afterMatch(int index)
    {
    	selectedPositions[index].remove();
		
		validTiles.remove(selectedPositions[index]);
		validTilesCounter[selectedPositions[index].getZ()]++;
		matchedTiles.push(selectedPositions[index]);
		
		selectedPositions[index].notifyNeighbors(false);
		if(!validTiles.contains(selectedPositions[index].getPlayableEastNeighbors()))
		{
			if(selectedPositions[index].getPlayableEastNeighbors() != null)
				validTiles.add(selectedPositions[index].getPlayableEastNeighbors());
			if(selectedPositions[index].getPlayableEastNeighbors() != null)
				validTilesCounter[selectedPositions[index].getPlayableEastNeighbors().getZ()]++;
		}
		if(!validTiles.contains(selectedPositions[index].getPlayableWestNeighbors()))
		{
			if(selectedPositions[index].getPlayableWestNeighbors() != null)
				validTiles.add(selectedPositions[index].getPlayableWestNeighbors());
			if(selectedPositions[index].getPlayableWestNeighbors() != null)
				validTilesCounter[selectedPositions[index].getPlayableWestNeighbors().getZ()]++;
		}
		if(!validTiles.contains(selectedPositions[index].getPlayableBelowNeighbors()))
		{
			if(selectedPositions[index].getPlayableBelowNeighbors() != null)
				validTiles.add(selectedPositions[index].getPlayableBelowNeighbors());
			if(selectedPositions[index].getPlayableBelowNeighbors() != null)
				validTilesCounter[selectedPositions[index].getPlayableBelowNeighbors().getZ()]++;
		}
		
//		System.out.println(selectedPositions[index].getPlayableEastNeighbors() + " " + selectedPositions[index].getPlayableWestNeighbors() + " " + selectedPositions[index].getPlayableBelowNeighbors() + "\n");
    }
    
	@Override
	public void mouseClicked(MouseEvent event) {
	if (gameIsReady==true)
	{
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
//								System.out.println("First " + positions[l][r][c].getThisTile().getType());
							}else if (selectedPositions[0].differentCoordinates(positions[l][r][c]))
							{
//								System.out.println("Second " + positions[l][r][c].getThisTile().getType());
								selectedPositions[1] = positions[l][r][c];
								c = positions[l][r].length-1;
								r = positions[l].length-1;
								l = positions.length-1;
							}
							else if(positions[l][r][c].sameCoordinates(selectedPositions[0]))
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
				afterMatch(0);
				afterMatch(1);
				
				updateBoard();
				numberOfCompletedMatches++;

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
	}
		
//		System.out.println(selectedPositions[1] + " " + selectedPositions[0]);
		
	}

	@Override
	public void mousePressed(MouseEvent event) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}
	
	private void endOfGameDialog()
	{
		gameIsReady=false;
		String temp = String.format("There are no more valid moves. \nYou matched %.2f %% of the tiles! \nWould you like to play again?",
				 (numberOfCompletedMatches/ (double) NUMBEROFMATCHES)*100);
		/*String temp = String.format("There are no more valid moves. \nYou matched %d of %d of pairs. \nWould you like to play again?",
				 numberOfCompletedMatches, NUMBEROFMATCHES);*/
		int n = JOptionPane.showOptionDialog(gameJFrame,
			temp,
			"Play Again",
			JOptionPane.YES_NO_OPTION,
			JOptionPane.QUESTION_MESSAGE,
			null,     //do not use a custom Icon
			options,  //the titles of buttons
			options[0]); //default button title
		
		if ( n == 0)
		{
			initializePositions();
			updateBoard();
			gameIsReady = true;
		}else
		{
			System.exit(0);
		}
	}
	
	private void DifficultyDialog()
	{				
		String difficulty = (String) JOptionPane.showInputDialog(gameJFrame, 
		        "Difficulty Level",
		        "Select A Difficulty Level",
		        JOptionPane.QUESTION_MESSAGE, 
		        null, 
		        levels, 
		        levels[0]);

		if(difficulty == null)
			System.exit(0);
		else
		{
				for(int n=0; n<levels.length; n++)
				{
					if(difficulty.equals(levels[n]))
					{
						shuffleTime = DEFAULTSHUFFLETIME-2000*n;
					}
				}
		}
	}
	
	private void layoutDialog() throws IOException
	{
		String difficulty = (String) JOptionPane.showInputDialog(gameJFrame, 
		        "Tile Arrangements",
		        "Select A Tile Arrangement",
		        JOptionPane.QUESTION_MESSAGE, 
		        null, 
		        arrangements, 
		        arrangements[0]);

		if(difficulty == null)
			System.exit(0);
		else
		{
			for(int n=0; n<arrangements.length; n++)
			{
				if(difficulty.equals(arrangements[n]))
				{
					currentArrangement = new boardArrangements(new File(files[n]));
					XOffset = offsets[n][0];
					YOffset = offsets[n][1];
				}
			}
		}
	}
	
	private void helpDialog()
	{
		String temp = "Mahjong solitaire is a one player game where the player matches two of the same tiles. \n "
				+ "144 rectangular tiles are laid out on the table with some tiles layered on top of others.\n"
				+ " The player can only match tiles that are not stacked below other tiles and tiles who have a long edge not adjacent to another tile.\n"
				+ " The aim of this game is to clear the tiles. The matched tiles disappear from the board making some new tiles visible and/or playable.";
		
		JOptionPane.showMessageDialog(
			    gameJFrame,
			    temp,
			    "How To Play Mahjong",
			    JOptionPane.PLAIN_MESSAGE);
			
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		
		if(event.getSource().equals(undoButton))
		{
			if(matchedTiles.size() > 0)
			{
        		boardPosition temp = matchedTiles.pop();
        		temp.getThisTile().setOnBoard(true);
        		validTiles.remove(temp.putBackOnBoard());
        		// check playable and if playable add to valid tiles
        		
        		temp = matchedTiles.pop();
        		temp.getThisTile().setOnBoard(true);
        		validTiles.remove(temp.putBackOnBoard());
        		
        		updateBoard();
			}
        }else if(event.getSource().equals(helpMenuItem))
        {
        	helpDialog();
        }
	}
		    
    
}
