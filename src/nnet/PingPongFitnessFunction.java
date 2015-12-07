package nnet;

import java.nio.DoubleBuffer;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import PingPong.Board;
import PingPong.Screen;
import com.anji.util.Properties;
import com.anji.util.Randomizer;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.jgap.Chromosome;

import com.anji.imaging.IdentifyImageFitnessFunction;
import com.anji.integration.Activator;
import com.anji.integration.ActivatorTranscriber;


/**
 * Created by Jared on 06-Dec-15.
 */
public class PingPongFitnessFunction {
    private final static String TRACK_LENGTH_KEY = "polebalance.track.length";

    private final static String TIMESTEPS_KEY = "polebalance.timesteps";

    private final static String NUM_TRIALS_KEY = "polebalance.trials";

    private final static String ANGLE_THRESHOLD_KEY = "polebalance.angle.threshold";

    private final static String INPUT_VELOCITY_KEY = "polebalance.input.velocities";

    private final static String POLE_1_LENGTH_KEY = "pole.1.length";

    private final static String POLE_2_LENGTH_KEY = "pole.2.length";

    private final static String START_POLE_ANGLE_1_KEY = "polebalance.pole.angle.start.1";

    private final static String START_POLE_ANGLE_2_KEY = "polebalance.pole.angle.start.2";

    private final static String START_POLE_ANGLE_RANDOM_KEY = "polebalance.pole.angle.start.random";

    private final static String PENALIZE_FOR_ENERGY_USE_KEY = "penalize.for.energy.use";

    private final static String PENALIZE_OSCILLATIONS_KEY = "penalize.oscillations";


    // Some useful physical model constants.
    private static final double GRAVITY = -9.8;

    private static final double MASSCART = 1.0;

    private static final double FORCE_MAG = 10.0;

    /**
     * seconds between state updates
     */
    private static final double TIME_DELTA = 0.01;

    private static final double FOURTHIRDS = 4.0 / 3.0;

    private static final double MUP = 0.000002;

    /**
     * 0.0174532; 2pi/360
     */
    private static final double ONE_DEGREE = Math.PI / 180.0;

    /**
     * 0.1047192
     */
    private static final double SIX_DEGREES = Math.PI / 30.0;

    /**
     * 0.2094384;
     */
    private static final double TWELVE_DEGREES = Math.PI / 15.0;

    /**
     * 0.3141592;
     */
    private static final double EIGHTEEN_DEGREES = Math.PI / 10.0;

    /**
     * 0.4188790;
     */
    private static final double TWENTYFOUR_DEGREES = Math.PI / 7.5;

    /**
     * 0.628329;
     */
    private static final double THIRTYSIX_DEGREES = Math.PI / 5.0;

    /**
     * 0.87266;
     */
    private static final double FIFTY_DEGREES = Math.PI / 3.6;

    /**
     * 1.256637;
     */
    private static final double SEVENTYTWO_DEGREES = Math.PI / 2.5;

    private Screen display = null;

    private final static double DEFAULT_TRACK_LENGTH = 4.8;

    private double trackLength = DEFAULT_TRACK_LENGTH;

    private double trackLengthHalfed;

    private final static int DEFAULT_TIMESTEPS = 10000;

    private int maxTimesteps = DEFAULT_TIMESTEPS;

    private final static int DEFAULT_NUM_TRIALS = 10;

    private int numTrials = DEFAULT_NUM_TRIALS;


    private final static Logger logger = Logger.getLogger( PingPongFitnessFunction.class );

    private ActivatorTranscriber factory;

    private Random rand;

    private void setTrackLength( double aTrackLength ) {
        trackLength = aTrackLength;
        trackLengthHalfed = trackLength / 2;
    }

    /**
     * @see com.anji.util.Configurable#init(com.anji.util.Properties)
     */

    public void init( Properties props ) throws Exception {
        try {
            BasicConfigurator.configure();
            factory = (ActivatorTranscriber) props.singletonObjectProperty( ActivatorTranscriber.class );
            setTrackLength( props.getDoubleProperty( TRACK_LENGTH_KEY, DEFAULT_TRACK_LENGTH ) );
            maxTimesteps = props.getIntProperty( TIMESTEPS_KEY, DEFAULT_TIMESTEPS );
            numTrials = props.getIntProperty( NUM_TRIALS_KEY, DEFAULT_NUM_TRIALS );
            Randomizer randomizer = (Randomizer) props.singletonObjectProperty( Randomizer.class );
            rand = randomizer.getRand();
        }
        catch ( Exception e ) {
            throw new IllegalArgumentException( "invalid properties: " + e.getClass().toString() + ": "
                    + e.getMessage() );
        }
    }

    /**
     * @see org.jgap.BulkFitnessFunction#evaluate(java.util.List)
     * @see IdentifyImageFitnessFunction#evaluate(Chromosome)
     */
    public void evaluate( List genotypes ) {

        // evaluate each chromosome
        Iterator it = genotypes.iterator();
        while ( it.hasNext() ) {
            Chromosome c = (Chromosome) it.next();
            evaluate( c );
        }
    }

    /**
     * Evaluate chromosome and set fitness.
     * @param c
     */
    public void evaluate( Chromosome c ) {
        try {
            Activator activator = factory.newActivator( c );

            // calculate fitness, sum of multiple trials
            int fitness = 0;
            for ( int i = 0; i < numTrials; i++ )
                fitness += singleTrial( activator );
            c.setFitnessValue( fitness );
        }
        catch ( Throwable e ) {
            logger.warn( "error evaluating chromosome " + c.toString(), e );
            c.setFitnessValue( 0 );
        }
    }

    /**
     * @return 6-dimensional array with the following data
     *
     * [0] - Cart Position (meters).
     *
     * [1] - Cart velocity (m/s).
     *
     * [2] - Pole 1 angle (radians)
     *
     * [3] - Pole 1 angular velocity (radians/sec).
     *
     * [4] - Pole 2 angle (radians)
     *
     * [5] - Pole 2 angular velocity (radians/sec).
     */

    private double[] newState() {
        double[] state = new double[ 6 ];
        state[ 0 ] = state[ 1 ] = state[ 3 ] = state[ 5 ] = 0;
        return state;
    }

    private int singleTrial( Activator activator ) {
        double energyUsed = 0;
        double f2 = 0.0;
        int fitness = 0;

        // Run the pole-balancing simulation.
        int currentTimestep = 0;
        Board trial = new Board();
        double[] networkInput;
        networkInput = trial.screen.step(50);

        for (currentTimestep = 0; currentTimestep < maxTimesteps; currentTimestep++) {
            // Network activation values



            // Store the accumulated state variables for cart and pole 1 within the oscillation buffer.
            // Activate the network.

            double networkOutput = activator.next(networkInput)[0];
            trial.screen.step(networkOutput);
            energyUsed += networkOutput;
            /*
            performAction(networkOutput, state);
            if (display != null) {
                // display.setStatus( Arrays.toString( state ) );
                display.step();
            }
            */
            //SimulateTimestep(network.getOutputSignal(0)>0.5);

        }
        return fitness;
    }
    /*
    private void performAction( double output, double[] state ) {
        int i;
        double[] dydx = new double[ 6 ];

        boolean RK4 = true; //Set to Runge-Kutta 4th order integration method
        double EULER_TAU = TIME_DELTA / 4;

	//--- Apply action to the simulated cart-pole --
        if ( RK4 ) {
            for ( i = 0; i < 2; ++i ) {
                dydx[ 0 ] = state[ 1 ];
                dydx[ 2 ] = state[ 3 ];
                dydx[ 4 ] = state[ 5 ];
                step( output, state, dydx );
                rk4( output, state, dydx, state );
            }
        }
        else {
            for ( i = 0; i < 8; ++i ) {
                step( output, state, dydx );
                state[ 0 ] += EULER_TAU * dydx[ 0 ];
                state[ 1 ] += EULER_TAU * dydx[ 1 ];
                state[ 2 ] += EULER_TAU * dydx[ 2 ];
                state[ 3 ] += EULER_TAU * dydx[ 3 ];
                state[ 4 ] += EULER_TAU * dydx[ 4 ];
                state[ 5 ] += EULER_TAU * dydx[ 5 ];
            }
        }
    }
    */
    /*
    private void step( double action, double[] st, double[] derivs ) {
        double force, costheta_1, costheta_2, sintheta_1, sintheta_2, gsintheta_1, gsintheta_2, temp_1, temp_2, ml_1, ml_2, fi_1, fi_2, mi_1, mi_2;

        force = ( action - 0.5 ) * FORCE_MAG * 2;
        costheta_1 = Math.cos( st[ 2 ] );
        sintheta_1 = Math.sin( st[ 2 ] );
        gsintheta_1 = GRAVITY * sintheta_1;
        costheta_2 = Math.cos( st[ 4 ] );
        sintheta_2 = Math.sin( st[ 4 ] );
        gsintheta_2 = GRAVITY * sintheta_2;

        ml_1 = poleLength1 * poleMass1;
        ml_2 = poleLength2 * poleMass2;
        temp_1 = MUP * st[ 3 ] / ml_1;
        temp_2 = MUP * st[ 5 ] / ml_2;

        fi_1 = ( ml_1 * st[ 3 ] * st[ 3 ] * sintheta_1 )
                + ( 0.75 * poleMass1 * costheta_1 * ( temp_1 + gsintheta_1 ) );

        fi_2 = ( ml_2 * st[ 5 ] * st[ 5 ] * sintheta_2 )
                + ( 0.75 * poleMass2 * costheta_2 * ( temp_2 + gsintheta_2 ) );

        mi_1 = poleMass1 * ( 1 - ( 0.75 * costheta_1 * costheta_1 ) );
        mi_2 = poleMass2 * ( 1 - ( 0.75 * costheta_2 * costheta_2 ) );

        derivs[ 1 ] = ( force + fi_1 + fi_2 ) / ( mi_1 + mi_2 + MASSCART );
        derivs[ 3 ] = -0.75 * ( derivs[ 1 ] * costheta_1 + gsintheta_1 + temp_1 ) / poleLength1;
        derivs[ 5 ] = -0.75 * ( derivs[ 1 ] * costheta_2 + gsintheta_2 + temp_2 ) / poleLength2;
    }

    private void rk4( double f, double[] y, double[] dydx, double[] yout ) {
        int i;

        double hh, h6;
        double[] dym = new double[ 6 ];
        double[] dyt = new double[ 6 ];
        double[] yt = new double[ 6 ];

        hh = TIME_DELTA * 0.5;
        h6 = TIME_DELTA / 6.0;
        for ( i = 0; i <= 5; i++ )
            yt[ i ] = y[ i ] + hh * dydx[ i ];
        step( f, yt, dyt );
        dyt[ 0 ] = yt[ 1 ];
        dyt[ 2 ] = yt[ 3 ];
        dyt[ 4 ] = yt[ 5 ];
        for ( i = 0; i <= 5; i++ )
            yt[ i ] = y[ i ] + hh * dyt[ i ];
        step( f, yt, dym );
        dym[ 0 ] = yt[ 1 ];
        dym[ 2 ] = yt[ 3 ];
        dym[ 4 ] = yt[ 5 ];
        for ( i = 0; i <= 5; i++ ) {
            yt[ i ] = y[ i ] + TIME_DELTA * dym[ i ];
            dym[ i ] += dyt[ i ];
        }
        step( f, yt, dyt );
        dyt[ 0 ] = yt[ 1 ];
        dyt[ 2 ] = yt[ 3 ];
        dyt[ 4 ] = yt[ 5 ];
        for ( i = 0; i <= 5; i++ )
            yout[ i ] = y[ i ] + h6 * ( dydx[ i ] + dyt[ i ] + 2.0 * dym[ i ] );
    }*/

    /**
     * @see org.jgap.BulkFitnessFunction#getMaxFitnessValue()
     */
    public int getMaxFitnessValue() {
        return ( numTrials * maxTimesteps );
    }

    /**
     * enable GUI display of pole balancing
     */
    /*
    public void enableDisplay() {
        display = new PoleBalanceDisplay( trackLength, new double[] { poleLength1, poleLength2 },
                maxTimesteps );
        display.setVisible( true );
    }*/

}
