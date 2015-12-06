package nnet;

/**
 * Created by Jared on 06-Dec-15.
 */

import com.anji.polebalance.DoublePoleBalanceEvaluator;
import com.anji.polebalance.DoublePoleBalanceFitnessFunction;
import org.apache.log4j.Logger;
import org.jgap.Chromosome;
import org.jgap.Configuration;

import com.anji.persistence.Persistence;
import com.anji.util.DummyConfiguration;
import com.anji.util.Properties;

public class PingPongEvaluator {


        private static final Logger logger = Logger.getLogger( DoublePoleBalanceEvaluator.class );

        /**
         * @param args
         * @throws Exception
         */
        public static void main( String[] args ) throws Exception {
            DoublePoleBalanceFitnessFunction ff = new DoublePoleBalanceFitnessFunction();
            Properties props = new Properties();
            props.loadFromResource(args[0]);
            ff.init(props);
            Persistence db = (Persistence) props.newObjectProperty(Persistence.PERSISTENCE_CLASS_KEY);
            Configuration config = new DummyConfiguration();
            Chromosome chrom = db.loadChromosome(args[1], config);
            if (chrom == null)
                throw new IllegalArgumentException("no chromosome found: " + args[1]);
            ff.enableDisplay();
            ff.evaluate(chrom);
            logger.info("Fitness = " + chrom.getFitnessValue());
        }

}
