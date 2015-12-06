package PingPong.nnet;

import org.neuroph.contrib.neat.gen.NeatParameters;
import org.neuroph.contrib.neat.gen.impl.SimpleNeatParameters;
import org.neuroph.contrib.neat.gen.operations.MutationOperation;
import org.neuroph.contrib.neat.gen.operations.ReproductionOperation;
import org.neuroph.contrib.neat.gen.operations.mutation.AddConnectionMutationOperation;
import org.neuroph.contrib.neat.gen.operations.mutation.AddNeuronMutationOperation;
import org.neuroph.contrib.neat.gen.operations.mutation.WeightMutationOperation;
import org.neuroph.contrib.neat.gen.operations.reproduction.AbstractReproductionOperation;
import org.neuroph.contrib.neat.gen.operations.selector.NaturalSelectionOrganismSelector;
import org.neuroph.contrib.neat.gen.operations.speciator.DynamicThresholdSpeciator;
import org.neuroph.contrib.neat.gen.persistence.impl.DirectoryOutputPersistence;
import org.neuroph.contrib.neat.gen.persistence.impl.serialize.JavaSerializationDelegate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;

/**
 * Created by razrs on 06-Dec-15.
 */
public class NeatParametersBuilder {
    public static final String BASE_DIRECTORY = "robot-output";

    public static NeatParameters neatParameters;
    public static long organismId;



    private JButton worldChooserButton;
    private JTextField organismIdField;
    private boolean useOrganismId;

    private NeatParametersBuilder(boolean useOrganismId) {
        this.useOrganismId = useOrganismId;

    }


    private long getOrganismId() {
        return Long.valueOf(organismIdField.getText());
    }

    public static void getTrainParams() {
        NeatParametersBuilder paramsPan = new NeatParametersBuilder(false);
        neatParameters = paramsPan.createNeatParameters();
    }

    public NeatParameters createNeatParameters() {
        SimpleNeatParameters params = new SimpleNeatParameters();
        params.setMaximumGenerations(750);
        params.setPopulationSize(250);
        //params.setMaximumFitness(builder.getWorstFitnessScore());
        params.setFitnessFunction(new PingPongFitnessFunction());

        DynamicThresholdSpeciator speciator = new DynamicThresholdSpeciator();
        speciator.setMaxSpecies(45);
        params.setSpeciator(speciator);

        NaturalSelectionOrganismSelector selector = (NaturalSelectionOrganismSelector) params
                .getOrganismSelector();
        selector.setKillUnproductiveSpecies(true);

        java.util.List<MutationOperation> ops = new ArrayList<MutationOperation>();

        AddNeuronMutationOperation addNeuron = new AddNeuronMutationOperation(
                0.5);
        AddConnectionMutationOperation addConnection = new AddConnectionMutationOperation(
                0.10);
        WeightMutationOperation weightMutation = new WeightMutationOperation(
                0.8);

        ops.add(addNeuron);
        ops.add(addConnection);
        ops.add(weightMutation);

        params.setMutationOperators(ops);

        for (ReproductionOperation op : params.getReproductionOperators()) {
            ((AbstractReproductionOperation) op).setUseFitnessBias(false);
        }

        // if you change this to a different persistence mechanism, make sure to change the value
        // in the replay as well.
        params.setPersistence(new DirectoryOutputPersistence(BASE_DIRECTORY, new JavaSerializationDelegate(false)));

        return params;
    }
}