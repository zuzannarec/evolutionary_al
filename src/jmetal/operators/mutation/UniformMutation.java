//  UniformMutation.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package jmetal.operators.mutation;

import jmetal.core.Solution;
import jmetal.encodings.solutionType.ArrayRealSolutionType;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.wrapper.XReal;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * This class implements a uniform mutation operator.
 */
public class UniformMutation extends Mutation{
  /**
   * Valid solution types to apply this operator 
   */
  private static final List VALID_TYPES = Arrays.asList(RealSolutionType.class,
  		                                            ArrayRealSolutionType.class) ;
  /**
   * Stores the value used in a uniform mutation operator
   */
  private Double perturbation_ = 0.9; // range of mutation
  
  private Double mutationProbability_ = 0.2; // how many of genes will be mutated

  /** 
   * Constructor
   * Creates a new uniform mutation operator instance
   */
  public UniformMutation(HashMap<String, Object> parameters) {
  	super(parameters) ;
  	
  	if (parameters.get("probability") != null)
  		mutationProbability_ = (Double) parameters.get("probability") ;  		
  	if (parameters.get("perturbation") != null)
  		perturbation_ = (Double) parameters.get("perturbation") ;  		

  } // UniformMutation


  /**
   * Constructor
   * Creates a new uniform mutation operator instance
   */
  //public UniformMutation(Properties properties) {
  //  this();
  //} // UniformMutation


  /**
  * Performs the operation
  * @param probability Mutation probability
  * @param solution The solution to mutate
   * @throws JMException 
  */
  public void doMutation(double probability, Solution solution) throws JMException {  
  	XReal x = new XReal(solution) ;
    Random random = new Random();
    for (int var = 0; var < solution.getDecisionVariables().length; var++) {
      //if (PseudoRandom.randDouble() < probability) {
      if (random.nextDouble() < probability) {
      //if (random.nextGaussian() < probability) {
        //double rand = PseudoRandom.randDouble();
        double rand = random.nextDouble() ; //values 0-1 from uniform distribution
        //double rand = random.nextGaussian(); //values 0-1 from normal distribution
        double tmp = (rand - 0.5)*perturbation_.doubleValue();
        //System.out.println("tmp" + tmp);
                                
        tmp += x.getValue(var);
                
        if (tmp < x.getLowerBound(var))
          tmp = x.getLowerBound(var);
        else if (tmp > x.getUpperBound(var))
          tmp = x.getUpperBound(var);
                
        x.setValue(var, tmp) ;
      } // if
    } // for
  } // doMutation
  
  /**
  * Executes the operation
  * @param object An object containing the solution to mutate
   * @throws JMException 
  */
  public Object execute(Object object) throws JMException {
    Solution solution = (Solution )object;
    
		if (!VALID_TYPES.contains(solution.getType().getClass())) {
      Configuration.logger_.severe("UniformMutation.execute: the solution " +
          "is not of the right type. The type should be 'Real', but " +
          solution.getType() + " is obtained");

      Class cls = java.lang.String.class;
      String name = cls.getName(); 
      throw new JMException("Exception in " + name + ".execute()") ;
    } // if 
    
    doMutation(mutationProbability_,solution);
        
    return solution;
  } // execute                  
} // UniformMutation
