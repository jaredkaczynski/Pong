/*
    Ball.java - Paddle Ball Game
	Contributer(s): Ron Parrott
	
    Contact: opengamesbeginners@gmail.com
*/

package PingPong;
public class Ball {
	//variables
	//size of the ball
	private final int WIDTH = 10;
	private final int HEIGHT = 10;
	
	//position of the ball
	private int x;
	private int y;
	
	//speed of the ball
	private int speed;
	
	//constructor
	public Ball(int s){
		x = 300;
		y = 200;
		speed = s;
	}
	
	public int getWidth(){
		return WIDTH;
	}
	
	public int getHeight(){
		return HEIGHT;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public int getSpeed(){
		return speed;
	}
	
	public void setX(int nextX){
		x = nextX;
	}
	
	public void setY(int nextY){
		y = nextY;
	}
	
	public void setOriginalPos(){
		x = 300;
		y = 200;
	}
	
	public void setSpeed(int s){
		speed = s;
	}
}
