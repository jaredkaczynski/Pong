package PingPong;

import javax.swing.*;

/**
 * Created by razrs on 11-Dec-15.
 */
public class PingPongNoGui {

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

    boolean gameover = false;

    boolean HumanSpeed = false;

    //learning stuff
    int paddleHit = 0;
    int paddleMiss = 0;

    


    public PingPongNoGui(Player player_1, Player player_2, Ball ball, ScoreBoard score_board){

        //set player 1 information
        p1 = player_1;

        //set player 2 information
        p2 = player_2;

        //set ball information
        b = ball;

        //set scoreboard information
        score = score_board;

    }
    public double getfitness(){
        double paddleHits = this.paddleHit;
        double paddleMisses = this.paddleMiss;
        //return ((paddleHits)/(double)(paddleHits+paddleMisses));
        return paddleHits;
    }
    public boolean getfinished(){return this.gameover;}

    public boolean getRealTime(){return this.HumanSpeed;}


    public double[] step(double[] action) {

        if(paddleHit > 100){
            gameover = true;
            System.out.println("Paddle Hit game over \n\n\n\n\n\n\n\n");
        }
        //System.out.println(action[0] + " this is the input for move" + action[1]);

        //double[] inputArray = {b.getY(),b.getX(),p1.getY(),p2.getY()};
        double[] inputArray = {b.getY()/400.0,b.getX()/600.0,p2.getY()/400.0};

        moveBall();

        double[] input = {b.getX(), b.getY(), p1.getX(), p1.getY(), p1.getSpeed()};

        p1.setY(b.getY());
        if(Math.abs(action[0]) > .5 && p2.getY() > 0){
            p2.setY(p2.getY() - p2.getSpeed());
        }
        else if(Math.abs(action[1]) > .5 && p2.getY() < (400 - p2.getHeight())){
            p2.setY(p2.getY() + p2.getSpeed());
        }


        //determine if there is a winner
        if(score.p1Wins() || score.p2Wins()){
            //System.out.println("This hit " + paddleHit);
            gameover = true;
        }

        //System.out.println(b.getY()/400.0 + " " + b.getX()/600.0 + " " + p2.getY()/400.0);
        //System.out.println(b.getY() + " " + b.getX() + " " + p1.getY() + " " + p2.getY());
        return(inputArray);
    }

    public void moveBall(){
        //x-direction motion
        if(b_right && hitPaddle(true)){ //hits the right paddle
            b.setX(b.getX() - b.getSpeed());
            b_right = false;
            paddleHit++;
        }
        else if(b_right && b.getX() < (600 - b.getWidth())){ //if moving right and not at max
            b.setX(b.getX() + b.getSpeed());
        }
        else if(b_right && b.getX() >= (600 - b.getWidth())){ //if moving right, but at max (point for p2)
            b.setOriginalPos();
            b_right = false;
            score.pointP1();
            paddleMiss++;
        }

        else if(!b_right && hitPaddle(false)){ //hits the left paddle
            b.setX(b.getX() + b.getSpeed());
            b_right = true;
            //paddleHit++;
        }
        else if(!b_right && b.getX() > 0){ //if moving left, but not at max
            b.setX(b.getX() - b.getSpeed());
        }
        else if(!b_right && b.getX() <= 0){ //if moving left, but at max (point p1)
            b.setOriginalPos();
            b_right = true;
            score.pointP2();
        }

        //y-direction motion
        if(b_up && b.getY() > 0){ //if moving up and not at max
            b.setY(b.getY() - b.getSpeed());
        }
        else if(b_up && b.getY() <= 0){ //if moving up, but at max
            b.setY(b.getY() + b.getSpeed());
            b_up = false;
        }
        else if(!b_up && b.getY() < (400 - b.getHeight())){ //if moving down, but not at max
            b.setY(b.getY() + b.getSpeed());
        }
        else if(!b_up && b.getY() >= (400 - b.getHeight())){ //if moving down, but at max
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
}
