import com.google.common.primitives.Ints;
import org.neuroph.contrib.neat.gen.Organism;
import org.neuroph.contrib.neat.gen.impl.SimpleNeatParameters;
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

    static List<OrganismFitnessScore> fitnesses = new List<OrganismFitnessScore>() {
        public int size() {
            return 0;
        }

        public boolean isEmpty() {
            return false;
        }

        public boolean contains(Object o) {
            return false;
        }

        public Iterator<OrganismFitnessScore> iterator() {
            return null;
        }

        public Object[] toArray() {
            return new Object[0];
        }

        public <T> T[] toArray(T[] a) {
            return null;
        }

        public boolean add(OrganismFitnessScore organismFitnessScore) {
            return false;
        }

        public boolean remove(Object o) {
            return false;
        }

        public boolean containsAll(Collection<?> c) {
            return false;
        }

        public boolean addAll(Collection<? extends OrganismFitnessScore> c) {
            return false;
        }

        public boolean addAll(int index, Collection<? extends OrganismFitnessScore> c) {
            return false;
        }

        public boolean removeAll(Collection<?> c) {
            return false;
        }

        public boolean retainAll(Collection<?> c) {
            return false;
        }

        public void clear() {

        }

        public OrganismFitnessScore get(int index) {
            return null;
        }

        public OrganismFitnessScore set(int index, OrganismFitnessScore element) {
            return null;
        }

        public void add(int index, OrganismFitnessScore element) {

        }

        public OrganismFitnessScore remove(int index) {
            return null;
        }

        public int indexOf(Object o) {
            return 0;
        }

        public int lastIndexOf(Object o) {
            return 0;
        }

        public ListIterator<OrganismFitnessScore> listIterator() {
            return null;
        }

        public ListIterator<OrganismFitnessScore> listIterator(int index) {
            return null;
        }

        public List<OrganismFitnessScore> subList(int fromIndex, int toIndex) {
            return null;
        }
    };

    public static void main(String[] args){
        evaluate(fitnesses);
    }
    
    public static void evaluate(List<OrganismFitnessScore> fitnesses) {
        for(OrganismFitnessScore ofs : fitnesses) {
            Board test = new Board();
            Organism o = ofs.getOrganism();
            NeuralNetwork net = ofs.getNeuralNetwork();


            ofs.setFitness(test.fitnessValue());
        }
    }
}
