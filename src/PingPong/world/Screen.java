package PingPong.world;
/*
    Screen.java - Paddle Ball Game
	Contributer(s): Ron Parrott
	
    Contact: opengamesbeginners@gmail.com
*/

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.util.TransferFunctionType;
import org.neuroph.nnet.learning.BackPropagation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;






public class Screen extends JPanel implements Runnable, KeyListener{
	//variables
	//player 1 information
	private Player p1;
	private boolean p1_up = false;
	private boolean p1_down = false;
	
	//player 2 information
	private Player p2;
	private boolean p2_up = false;
	private boolean p2_down = false;
	
	//ball information
	private Ball b;
	private boolean b_right = true;
	private boolean b_up = true;
	
	//scoreboard information
	private ScoreBoard score;
	
	//board-court information
	private Board board;
	
	//other info
	private Thread thread;
	private boolean gameover = false;

    //learning stuff
    int paddleHit = 0;
    private NeuralNetwork netWork;

	//constructor
	public Screen(Player player_1, Player player_2, Ball ball, ScoreBoard score_board, Board game_board, NeuralNetwork network){
		//set player 1 information
		p1 = player_1;
		
		//set player 2 information
		p2 = player_2;
		
		//set ball information
		b = ball;
		
		//set scoreboard information
		score = score_board;
		
		//set board information
		board = game_board;
		
		//Screen details
		this.setFocusable(true);
		this.addKeyListener(this);
		
		this.add(score);
		
		thread = new Thread(this);
		thread.start();	
	}
	
	public void paintComponent(Graphics g){
		setOpaque(false);
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		
		//draw background
		Image background = new ImageIcon(board.getBackgroundImage()).getImage();
		if(!g2.drawImage(background, 0, 0, this)){
			System.out.println("Error with image.");
		}
		
		//draw player blocks
		g2.setColor(Color.WHITE);
		g2.fillRect(p1.getX(), p1.getY(), p1.getWidth(), p1.getHeight());
		
		g2.fillRect(p2.getX(), p2.getY(), p2.getWidth(), p2.getHeight());
		
		//draw ball
		g2.setColor(Color.WHITE);
		g2.fillOval(b.getX(), b.getY(), b.getWidth(), b.getHeight());

	}

    public long map(long x, long in_min, long in_max, long out_min, long out_max)
    {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
    public void runPassthrough(NeuralNetwork inputNet){
        netWork = inputNet;
        run();
    }
	public void run() {
		// TODO Auto-generated method stub
		
		//sleep the thread so the game doesn't start too early
		try{
			Thread.sleep(1000);
		}
		catch(InterruptedException e){
			
		}


		//run the game loop
		while(true){
			//allows for smooth motion of the game
			try{
				Thread.sleep(10);
			}
			catch(InterruptedException e){

			}

			moveBall();
			movePlayer(1);
			movePlayer(2);
            if(b.getX() <300){
                //System.out.println("player 1");
                double[] input = {b.getX(),b.getY(),p1.getX(),p1.getY(),p1.getSpeed()};
                //myMlPerceptron.setInput(input);
                //myMlPerceptron.calculate();
                netWork.setInput(input);
                netWork.calculate();
                /*p2_down = false;
                p2_up = false;
                p1_up = false;
                p1_down = false;*/
                System.out.println(netWork.getOutput()[0]);

                if(netWork.getOutput()[0] > 0){
                    p1_down = false;
                    p1_up = true;
                } else {
                    p1_down = true;
                    p1_up = false;
                }
            }else{
                //System.out.println("player 2");
                double[] input = {b.getX(),b.getY(),p1.getX(),p1.getY(),p1.getSpeed()};
                System.out.println(input);
                netWork.setInput(input);
                netWork.calculate();
                //double paddlePosition = (myMlPerceptron.getOutput()[0]);
                //p2.setY((int) paddlePosition);
                System.out.println(netWork.getOutput()[0]);
                if(netWork.getOutput()[0] > 0){
                    p2_down = true;
                    p2_up = false;
                } else {
                    p2_down = false;
                    p2_up = true;
                }
            }

			//determine if there is a winner
			if(score.p1Wins() || score.p2Wins()){
				gameover = true;
                paddleHit = 0;
				break;
			}
		}
        // save the trained network into file
        //myMlPerceptron.save(�test.nnet�);
    }
	
	public void movePlayer(int p){
		if(p == 1){
			if(p1_up && p1.getY() > 0){
				p1.setY(p1.getY() - p1.getSpeed());
			}
			else if(p1_down && p1.getY() < (this.getHeight() - p1.getHeight())){
				p1.setY(p1.getY() + p1.getSpeed());
			}
		}
		else if(p == 2){
			if(p2_up && p2.getY() > 0){
				p2.setY(p2.getY() - p2.getSpeed());
			}
			else if(p2_down && p2.getY() < (this.getHeight() - p2.getHeight())){
				p2.setY(p2.getY() + p2.getSpeed());
			}
		}
		repaint();
	}
	
	public void moveBall(){
		//x-direction motion
		if(b_right && hitPaddle(true)){ //hits the right paddle
			b.setX(b.getX() - b.getSpeed());
			b_right = false;
            paddleHit++;
		}
		else if(b_right && b.getX() < (this.getWidth() - b.getWidth())){ //if moving right and not at max
			b.setX(b.getX() + b.getSpeed());
		}
		else if(b_right && b.getX() >= (this.getWidth() - b.getWidth())){ //if moving right, but at max (point for p2)
			b.setOriginalPos();
			b_right = false;
			score.pointP1();
			//cause a pause before the game resumes
			try{
				Thread.sleep(500);
			}
			catch(InterruptedException e){
				
			}
		}
		
		else if(!b_right && hitPaddle(false)){ //hits the left paddle
			b.setX(b.getX() + b.getSpeed());
			b_right = true;
            paddleHit++;
		}
		else if(!b_right && b.getX() > 0){ //if moving left, but not at max
			b.setX(b.getX() - b.getSpeed());
		}
		else if(!b_right && b.getX() <= 0){ //if moving left, but at max (point p1)
			b.setOriginalPos();
			b_right = true;
			score.pointP2();
			//cause a pause before the game resumes
			try{
				Thread.sleep(500);
			}
			catch(InterruptedException e){
				
			}
		}
		
		//y-direction motion
		if(b_up && b.getY() > 0){ //if moving up and not at max
			b.setY(b.getY() - b.getSpeed());
		}
		else if(b_up && b.getY() <= 0){ //if moving up, but at max
			b.setY(b.getY() + b.getSpeed());
			b_up = false;
		}
		else if(!b_up && b.getY() < (this.getHeight() - b.getHeight())){ //if moving down, but not at max
			b.setY(b.getY() + b.getSpeed());
		}
		else if(!b_up && b.getY() >= (this.getHeight() - b.getHeight())){ //if moving down, but at max
			b.setY(b.getY() - b.getSpeed());
			b_up = true;
		}
	}
	
	//method to determine if the ball hits the paddle
	public boolean hitPaddle(boolean is_right_dir){
		if(is_right_dir){
			if(b.getX() >= p2.getX() && b.getX() < (p2.getX() + p2.getWidth()) && b.getY() >= p2.getY() && b.getY() <= p2.getY() + p2.getHeight()){
				return true;
			}
			else{
				return false;
			}
		}
		else{
			if(b.getX() <= p1.getX() && b.getX() > (p1.getX() - p1.getWidth()) && b.getY() >= p1.getY() && b.getY() <= p1.getY() + p1.getHeight()){
				return true;
			}
			else{
				return false;
			}
		}
	}

	public void keyPressed(KeyEvent evt) {
		// TODO Auto-generated method stub
		if(evt.getKeyCode() == 38){ //if player 1 is moving up
			p2_down = false;
			p2_up = true;
		}
		else if(evt.getKeyCode() == KeyEvent.VK_DOWN){ //if player 1 is moving down
			p2_down = true;
			p2_up = false;
		}
		else if(evt.getKeyCode() == 65){ //if player 2 is moving up
			p1_down = false;
			p1_up = true;
		}
		else if(evt.getKeyCode() == 90){ //if player 2 is moving down
			p1_down = true;
			p1_up = false;
		}
	}

	public void keyReleased(KeyEvent evt) {
		// TODO Auto-generated method stub
		if(evt.getKeyCode() == 38){ //if player 1 stops moving up
			p2_up = false;
		}
		else if(evt.getKeyCode() == 40){
			p2_down = false;
		}
		else if(evt.getKeyCode() == 65){ //if player 2 stops moving up
			p1_up = false;
		}
		else if(evt.getKeyCode() == 90){
			p1_down = false;
		}
	}

	public void keyTyped(KeyEvent evt) {
		// TODO Auto-generated method stub
		
	}
	
	//method to reset the screen back to what it was before
	public int reset(int p1y, int p1s, int p2y, int p2s, int bs){

		p1.setY(p1y);
		p2.setY(p2y);
		
		p1.setSpeed(p1s);
		p2.setSpeed(p2s);
		
		b.setOriginalPos();
		b.setSpeed(bs);
		
		score.reset();
		
		try{
			Thread.sleep(100);
		}
		catch(InterruptedException e){
			
		}
		
		//check if starting a game that has been won
		if(gameover){
			gameover = false;
			
			//start up a new thread
			thread = new Thread(this);
			thread.start();
		}
        return paddleHit;
	}

}

