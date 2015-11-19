import com.google.common.primitives.Ints;
import org.neuroph.contrib.neat.gen.Organism;
import org.neuroph.contrib.neat.gen.impl.SimpleNeatParameters;
import org.neuroph.contrib.neat.gen.operations.FitnessFunction;
import org.neuroph.contrib.neat.gen.operations.OrganismFitnessScore;
import org.neuroph.core.NeuralNetwork;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by razrs on 18-Nov-15.
 */

public class ArtificialIntelligence {
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
    private final int P1_SPEED = 2;
    private final int P2_SPEED = 2;

    //speed of the ball
    private final int B_SPEED = 2;

    static List<OrganismFitnessScore> fitnesses;

    public static void main(String[] args){
        SimpleNeatParameters params = new SimpleNeatParameters();
        Board test = new Board();
        //fitnesses.add(new OrganismFitnessScore(0));
        params.setFitnessFunction(new FitnessFunction() {
            public void evaluate(List<OrganismFitnessScore> list) {
                for(OrganismFitnessScore ofs : fitnesses) {
                    Board test = new Board();
                    Organism o = ofs.getOrganism();
                    NeuralNetwork net = ofs.getNeuralNetwork();


                    ofs.setFitness(test.fitnessValue(net));
                }
            }
        });
        params.setPopulationSize(5);
        params.setMaximumFitness(1000);
        params.setMaximumGenerations(10);
        //evaluate(fitnesses);
        params.getMaximumFitness();


    }
    /*public static int fitness(){
        Board test = new Board();
        return test.fitnessValue(n);
    }*/
    /*
    public static void evaluate(List<OrganismFitnessScore> fitnesses) {
        for(OrganismFitnessScore ofs : fitnesses) {
            Board test = new Board();
            Organism o = ofs.getOrganism();
            NeuralNetwork net = ofs.getNeuralNetwork();


            ofs.setFitness(test.fitnessValue(net));
        }
    }*/
}
