package selector.classification;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import utility.TestCase;

//DA FARE COME OMPLEMETNAZIONEDI CLASSE ASTRATTA
public class GBSSelector extends TestCaseSelector {
	
	
	 public double REstimate;
	 public double trueReliability;
	 public int numberOfDetectedFailurePoints;
	  public int numberOfExecutedTestCases;
	  public long executionTime;
	
	  
	public GBSSelector(ArrayList<TestCase> _potentialTestSuite, ArrayList<ArrayList<TestCase>> _partitions, int _nPartitions, int _initialNumberOfTestCases, int _budget){
		super(_potentialTestSuite,  _partitions, _nPartitions, _initialNumberOfTestCases, _budget);
	};
	
	public void selectAndRunTestCase(){
		
		
//		System.out.println("\n\nStarting the AT Algorithm ... \n");
		int indexPartition = 0; 
		TestCase testCaseToExecute;
		
		double[] executedtTestCasesPerPartition = new double[this.numberOfPartitions]; //ni
		double[] failureRates = new double[this.numberOfPartitions]; //theta
		int[] failedTestCases  =  new int[this.numberOfPartitions]; //z
		double[] gradient = new double[this.numberOfPartitions];
		double[] domainProbSum = new double[this.numberOfPartitions];  //DOMAIN SIZE: p
		int totalTests = this.budget;
		int indexCurrentTest =0;
		int totalFailurePoint=0;
		
		long initTime = System.currentTimeMillis(); 
		boolean testOutcome;
		//STEP 1: initialize 
		for (indexPartition=0; indexPartition< this.numberOfPartitions; indexPartition++) {
			executedtTestCasesPerPartition [indexPartition] = 1;
			failedTestCases[indexPartition] =0;
			domainProbSum[indexPartition] = 0;    
		}
		
		//READ PARTITIONS and initialize probabilities
		for (indexPartition=0; indexPartition<this.numberOfPartitions;indexPartition++){
				for(int indexInTheList=0; indexInTheList<this.partitions.get(indexPartition).size();indexInTheList++)
		    		domainProbSum[indexPartition] = domainProbSum[indexPartition] +this.partitions.get(indexPartition).get(indexInTheList).getExpectedOccurrenceProbability();
		}
		

		//STEP 2 and 3: select one test case from each subdomain, and execute it against the software under test; record the result
		for (indexPartition=0; indexPartition< this.numberOfPartitions; indexPartition++){ 
			testCaseToExecute = selectFromPartition(indexPartition);
			testOutcome = testCaseToExecute.runTestCase("FITTIZIO");
			if (testOutcome==false) {failedTestCases[indexPartition]++;totalFailurePoint++;} 
			indexCurrentTest++;
			executedtTestCasesPerPartition[indexPartition]++; //LO AGGIUNGO IO
		}
		
		
		double maximumGradient;
		int maximumGradientPartition=0;
		while(indexCurrentTest < totalTests){
			
			//STEP 5: update estimate of theta 
			for (indexPartition=0; indexPartition< this.numberOfPartitions; indexPartition++){
				if (failedTestCases[indexPartition] == 0)
					failureRates[indexPartition] = (failedTestCases[indexPartition]+1)/(executedtTestCasesPerPartition[indexPartition] +1);
				else failureRates[indexPartition] = (failedTestCases[indexPartition])/(executedtTestCasesPerPartition[indexPartition]);
				
				gradient[indexPartition] = (Math.pow(domainProbSum[indexPartition],2)*failureRates[indexPartition] *(1-failureRates[indexPartition]))/Math.pow(executedtTestCasesPerPartition[indexPartition], 2);
	
			}
			
			//STEP 6 select the subdomain with the GREATEST GRADEINT
			maximumGradient=gradient[0];
			maximumGradientPartition=0;;
			for (indexPartition=1; indexPartition< this.numberOfPartitions; indexPartition++){
				if(gradient[indexPartition] > maximumGradient){
					maximumGradient =gradient[indexPartition];
					maximumGradientPartition = indexPartition; 
				}
			}
	
			testCaseToExecute = selectFromPartition(maximumGradientPartition);
			testOutcome = testCaseToExecute.runTestCase("FITTIZIO");
			if (testOutcome==false) {
				failedTestCases[maximumGradientPartition]++;
				totalFailurePoint++;
				}
			executedtTestCasesPerPartition[maximumGradientPartition]++;
			
			indexCurrentTest++;
		}
		
		//last update of theta
	//	if (failedTestCases[maximumGradientPartition] == 0)
	//		failureRates[maximumGradientPartition] = (failedTestCases[maximumGradientPartition]+1)/(executedtTestCasesPerPartition[maximumGradientPartition] +1);
	//	else 
		failureRates[maximumGradientPartition] = (failedTestCases[maximumGradientPartition])/(executedtTestCasesPerPartition[maximumGradientPartition]);
		
		
		//STEP 8 EVALUATE THE RELIABILITY
		double sumFailureRate = 0.0;
		for (indexPartition=0; indexPartition< this.numberOfPartitions; indexPartition++){
			sumFailureRate =  sumFailureRate + domainProbSum[indexPartition]*failureRates[indexPartition];  
		}
		this.REstimate = 1 - sumFailureRate; 
		this.trueReliability = computeTrueReliability();
		
		long endTime = System.currentTimeMillis();
		this.executionTime = endTime - initTime; 
		this.numberOfExecutedTestCases = indexCurrentTest;
		this.numberOfDetectedFailurePoints = totalFailurePoint;
		
//		System.out.println("\n ************ True Reliability: "+this.trueReliability);
//		System.out.println("\n ************ Reliability Estimate: "+this.REstimate);
//		System.out.println("\n ************ Offset: "+(this.REstimate - this.trueReliability));
//		System.out.println("\n ************ squared error of "+Math.pow((this.REstimate - this.trueReliability),2));
//		System.out.println("\n ************ Detected Failures: "+totalFailurePoint);
		
	}
	
	private double computeTrueReliability(){
		
		
		boolean outcome;
		double unrel=0.0;
		int totalFailPoints=0;
		for (int i=0; i<this.potentialTestSuite.size(); i++){
			outcome =  this.potentialTestSuite.get(i).getOutcome();
			if (outcome==false) {
				unrel = unrel +  this.potentialTestSuite.get(i).getExpectedOccurrenceProbability();
				totalFailPoints++;
			}
		}
	/*	System.out.println("potentialTestSuite.size(): "+potentialTestSuite.size());
		System.out.println("Unreliability: "+unrel);
		System.out.println("TotFailPoint From the beginning "+totalFailPoints);
		*/
		return (1- unrel);
	}
	
	private TestCase selectFromPartition(int maximumGradientPartition){
			
		//SELECT RANDOMLY A TEST IN THE PARTITION
		int randIndex =  new Random().nextInt(this.partitions.get(maximumGradientPartition).size());	
		return  this.partitions.get(maximumGradientPartition).get(randIndex);
		
	}
		
} 