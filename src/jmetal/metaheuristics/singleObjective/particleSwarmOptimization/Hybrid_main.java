package jmetal.metaheuristics.singleObjective.particleSwarmOptimization;

import jmetal.core.AlgorithmHybrid;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.metaheuristics.singleObjective.geneticAlgorithm.gGA;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.Mutation;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.problems.singleObjective.Rastrigin;
import jmetal.qualityIndicator.QualityIndicator;
import jmetal.util.Configuration;
import jmetal.util.JMException;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class Hybrid_main {
    public static Logger logger_ ;      // Logger object
    public static FileHandler fileHandler_ ; // FileHandler object

    /**
     * @param args Command line arguments. The first (optional) argument specifies
     *             the problem to solve.
     * @throws JMException
     * @throws IOException
     * @throws SecurityException
     * Usage: three options
     *      - jmetal.metaheuristics.mocell.MOCell_main
     *      - jmetal.metaheuristics.mocell.MOCell_main problemName
     *      - jmetal.metaheuristics.mocell.MOCell_main problemName ParetoFrontFile
     */
    public static void main(String [] args)
            throws JMException, IOException, ClassNotFoundException {
        Problem problem   ;  // The problem to solve
        AlgorithmHybrid algorithmPSO ;  // The algorithm to use
        Mutation mutationPSO  ;  // "Turbulence" operator

        AlgorithmHybrid algorithmGA ;         // The algorithm to use
        Operator crossoverGA ;         // Crossover operator
        Operator  mutationGA  ;         // Mutation operator
        Operator  selectionGA ;         // Selection operator

        QualityIndicator indicators ; // Object to get quality indicators

        HashMap parametersPSO ; // Operator parameters
        HashMap  parametersGA ; // Operator parameters

        // Logger object and file to store log messages
        logger_      = Configuration.logger_ ;
        fileHandler_ = new FileHandler("PSO_main.log");
        logger_.addHandler(fileHandler_) ;

        problem = new Rastrigin("Real", 20);

        algorithmPSO = new PSO(problem) ;

        algorithmGA = new gGA(problem) ; // Generational GA


        // Algorithm parameters GA
        algorithmGA.setInputParameter("populationSize",20);
        algorithmGA.setInputParameter("maxEvaluations", 1000);

        // Mutation and Crossover for Real codification
        parametersGA = new HashMap() ;
        parametersGA.put("probability", 0.8) ;
        parametersGA.put("distributionIndex", 20.0) ;
        crossoverGA = CrossoverFactory.getCrossoverOperator("SBXCrossover", parametersGA);

        parametersGA = new HashMap() ;
        parametersGA.put("probability", 1.0/problem.getNumberOfVariables()) ;
        parametersGA.put("distributionIndex", 20.0) ;
        mutationGA = MutationFactory.getMutationOperator("PolynomialMutation", parametersGA);

        // Selection Operator GA
        parametersGA = null ;
        selectionGA = SelectionFactory.getSelectionOperator("BinaryTournament", parametersGA) ;

        // Add the operators to the algorithm GA
        algorithmGA.addOperator("crossover",crossoverGA);
        algorithmGA.addOperator("mutation",mutationGA);
        algorithmGA.addOperator("selection",selectionGA);


        // Algorithm parameters PSO
        algorithmPSO.setInputParameter("swarmSize",20);
        algorithmPSO.setInputParameter("maxIterations",50);

        parametersPSO = new HashMap() ;
        parametersPSO.put("probability", 1.0/problem.getNumberOfVariables()) ;
        parametersPSO.put("distributionIndex", 20.0) ;
        mutationPSO = MutationFactory.getMutationOperator("PolynomialMutation", parametersPSO);

        algorithmPSO.addOperator("mutation", mutationPSO);



        // Execute the Algorithm
        long initTime = System.currentTimeMillis();
        // Hyrbrid PSO-GA
//        SolutionSet populationPSO1 = algorithmPSO.execute();
//        SolutionSet populationGA1 = algorithmGA.execute(populationPSO1, algorithmPSO.get_ev_value());
//        SolutionSet populationPSO2 = algorithmPSO.execute(populationGA1, algorithmGA.get_ev_value());
//        SolutionSet populationGA2 = algorithmGA.execute(populationPSO2, algorithmPSO.get_ev_value());
//        SolutionSet populationPSO3 = algorithmPSO.execute(populationGA2, algorithmGA.get_ev_value());
//        SolutionSet populationGA3 = algorithmGA.execute(populationPSO3, algorithmPSO.get_ev_value());

        // Hybrid PSO-GA
        SolutionSet populationGA1 = algorithmGA.execute();
        SolutionSet populationPSO1 = algorithmPSO.execute(populationGA1, algorithmGA.get_ev_value());
        SolutionSet populationGA2 = algorithmGA.execute(populationPSO1, algorithmPSO.get_ev_value());
        SolutionSet populationPSO2 = algorithmPSO.execute(populationGA2, algorithmGA.get_ev_value());
        SolutionSet populationGA3 = algorithmGA.execute(populationPSO2, algorithmPSO.get_ev_value());
        SolutionSet populationPSO3 = algorithmPSO.execute(populationGA3, algorithmGA.get_ev_value());

        long estimatedTime = System.currentTimeMillis() - initTime;

    } //main
} // PSO_hybrid_main
