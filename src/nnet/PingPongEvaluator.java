package nnet;

/**
 * Created by Jared on 06-Dec-15.
 */

import org.apache.log4j.Logger;
import org.jgap.Chromosome;
import org.jgap.Configuration;

import com.anji.persistence.Persistence;
import com.anji.util.DummyConfiguration;
import com.anji.util.Properties;

public class PingPongEvaluator {


        private static final Logger logger = Logger.getLogger( PingPongEvaluator.class );

        /**
         * @param args
         * @throws Exception
         */
        public static void main( String[] args ) throws Exception {
            PingPongFitnessFunction ff = new PingPongFitnessFunction();
            Properties props = new Properties();
            props.loadFromResourceWithoutLogging(args[0]);
            System.out.println("Working Directory = " +
                    System.getProperty("user.dir"));

            ff.init(props);
            java.lang.Object test = props.newObjectProperty(Persistence.PERSISTENCE_CLASS_KEY);
            Persistence db =  (Persistence) test;
            Configuration config = new DummyConfiguration();
            logger.info( "random genotype" );
            db.init(props);
            Chromosome chrom = db.loadChromosome(args[1], config);
            if (chrom == null)
                throw new IllegalArgumentException("no chromosome found: " + args[1]);
            //ff.enableDisplay();
            ff.evaluate(chrom);
            logger.info("Fitness = " + chrom.getFitnessValue());
        }

}
