package PingPong.nnet;

import PingPong.world.Board;
import org.neuroph.contrib.neat.gen.operations.FitnessFunction;
import org.neuroph.contrib.neat.gen.operations.OrganismFitnessScore;
import org.neuroph.core.NeuralNetwork;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by razrs on 18-Nov-15.
 */


public class PingPongFitnessFunction implements FitnessFunction {
    /**
     * The number of steps to run the simulation for.
     */
    public static final long MAX_STEPS = 500;



    public void evaluate(List<OrganismFitnessScore> fitnesses) {
        // leave 1 CPU spare for the OS etc., lets hog the rest!
        int numberOfParallelExecutions = Runtime.getRuntime().availableProcessors() - 1;

        // defect fix : this assumed that there was always > 1 processors, so take the minimum
        // of number of numberOfParallelExecutions (could potentially be 0 on single-core machines)
        // and 1.
        ExecutorService executor = Executors.newFixedThreadPool(Math.max(numberOfParallelExecutions, 1));

        for(OrganismFitnessScore ofs : fitnesses) {
            PingPongEvaluator eval = new PingPongEvaluator(ofs, ofs.getNeuralNetwork());
            executor.submit(eval);
        }

        try {
            // this will block until all of the <code>Runnable</code>s have finished
            // executing.
            executor.shutdown();

            // if we have waited more than an hour, then break out and shut the evolution
            // down, the processing shouldn't take more than an hour!
            executor.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e1) {
            throw new IllegalStateException(e1);
        }
    }


    /**
     * Simple <code>Runnable</code> that will perform the evaluation of the robot.
     *
     * @author Aidan Morgan
     */
    private class PingPongEvaluator implements Runnable {
        /**
         * The <code>NeuralNetwork</code> under evaluation.
         */
        private NeuralNetwork network;

        /**
         * The <code>OrganismFitnessScore</code> to update with the fitness once the evaluation is complete.
         */
        private OrganismFitnessScore score;

        /**
         * Constructor.
         *
         * @param score the <code>OrganismFitnessScore</code> to update with the fitness once the evaluation is complete.
         * @param network the <code>NeuralNetwork</code> under evaluation.
         */
        public PingPongEvaluator(OrganismFitnessScore score, NeuralNetwork network) {
            this.network = network;
            this.score = score;
        }

        public void run() {
            Board agent = new Board(network);


            /*
            Robot agent = new Robot(network);
			AgentWorld world = builder.createWorld();
			world.setRobot(agent);

			for(long i = 0; i < MAX_STEPS; i++) {
				world.step(i);
			}

			// as NEAT requires a maximisation problem, we take the worst possible case that could arise,
			// and subtract our actual score from it - creating a maximisation problem...
			score.setFitness(world.getWorstFitnessScore() - agent.getDistanceToGoal());
             */


        }
    }
}

