/*
    Player.java - Paddle Ball Game
	Contributer(s): Ron Parrott
	
    Contact: opengamesbeginners@gmail.com
*/
package PingPong.world;
public class Player{
	//variables
	
	//size of the paddle
	private final int WIDTH = 5;
	private final int HEIGHT = 50;
	
	//location of the player
	private int pos_x, pos_y;
	
	//speed of the player
	private int speed;
	
	//constructor
	public Player(int pos_x, int pos_y, int s){
		this.pos_x = pos_x;
		this.pos_y = pos_y;
		this.speed = s;
	}
	
	public int getWidth(){
		return WIDTH;
	}
	
	public int getHeight(){
		return HEIGHT;
	}
	
	public int getX(){
		return pos_x;
	}
	
	public int getY(){
		return pos_y;
	}
	
	public int getSpeed(){
		return speed;
	}
	
	public void setY(int nextY){
		pos_y = nextY;
	}
	
	public void setSpeed(int s){
		speed = s;
	}
}
