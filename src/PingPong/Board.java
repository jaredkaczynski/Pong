/*
    Board.java - Paddle Ball Game
	Contributer(s): Ron Parrott
	
    Contact: opengamesbeginners@gmail.com
*/
package PingPong;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Board extends JFrame implements ActionListener{
	//variables
	
	//size of the board
	private final int WIDTH = 600;
	private final int HEIGHT = 400;
	
	//starting location of the players
	//player 1
	private final int P1_X = 30;
	private final int P1_Y = 175;
	//player 2
	private final int P2_X = 550;
	private final int P2_Y = 175;
	
	//speed of the players
	private final int P1_SPEED = 1;
	private final int P2_SPEED = 1;
	
	//speed of the ball
	private final int B_SPEED = 2;
	
	private String background_img = "../pong/img/board.png";
	
	//objects that actions are performed on
	private JMenuItem fileRestart;
	
	//objects that are added to the content pane (through the Screen)
	private Board board;
	public Screen screen;
	
	//constructor
	public Board(){
		//set the title of the frame
		super("Paddle Ball");
		board = this;
		//get screen information
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenDimension = kit.getScreenSize();
		int screenWidth = (int)screenDimension.getWidth();
		int screenHeight = (int)screenDimension.getHeight();
		
		//set location at center of screen
		int gameX = (screenWidth/2) - (WIDTH/2);
		int gameY = (screenHeight/2) - (HEIGHT/2);
		
		//set frame information
		this.setSize(WIDTH, HEIGHT);;
		this.setResizable(false);
		this.setLocation(gameX, gameY);

		
		//create players
		Player p1 = new Player(P1_X, P1_Y, P1_SPEED);	
		Player p2 = new Player(P2_X, P2_Y, P2_SPEED);
		
		//create ball
		Ball b = new Ball(B_SPEED);
		
		//create the scoreboard
		ScoreBoard sb = new ScoreBoard();
		
		//create the menu
		JMenuBar menubar = new JMenuBar();
		JMenu file = new JMenu("File");
		menubar.add(file);
		
		fileRestart = new JMenuItem("Restart");
		fileRestart.addActionListener(this);
		file.add(fileRestart);
		
		this.setJMenuBar(menubar);
		
		//draw the components
		screen = new Screen(p1, p2, b, sb, this);
        if(this.screen.HumanSpeed) {
            this.setVisible(true);
        }else{
            this.setVisible(true);
        }
		this.getContentPane().add(screen);

		//System.out.println(fitnessValue());
	}
	public double getfitness(){
        double paddleHits = this.screen.paddleHit;
        double paddleMisses = this.screen.paddleMiss;
		//return ((paddleHits)/(double)(paddleHits+paddleMisses));
		return paddleHits;
	}
	public double getfitnessDistance(){
		double paddleDistance = this.screen.paddleDistance;
		//return ((paddleHits)/(double)(paddleHits+paddleMisses));
		//System.out.println(paddleDistance);
		return paddleDistance;
	}
	public boolean getfinished(){return this.screen.gameover;}
	public void reset(){
		Player p1 = new Player(P1_X, P1_Y, P1_SPEED);
		Player p2 = new Player(P2_X, P2_Y, P2_SPEED);

		//create ball
		Ball b = new Ball(B_SPEED);
		ScoreBoard sb = new ScoreBoard();
		this.screen.reset( P1_Y, P1_SPEED, P2_Y, P2_SPEED, B_SPEED);
	}


	public String getBackgroundImage(){
		return background_img;
	}
	public void delete(){
		this.dispose();
	}
    public boolean getRealTime(){return this.screen.HumanSpeed;}
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == fileRestart){
			
			//resets the screen so the game can be played again
			screen.reset(P1_Y, P1_SPEED, P2_Y, P2_SPEED, B_SPEED);
		}
	}
}
