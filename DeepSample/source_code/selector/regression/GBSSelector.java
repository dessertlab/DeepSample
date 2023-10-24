package selector.regression;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import utility.regression.TestCase;

//DA FARE COME OMPLEMETNAZIONEDI CLASSE ASTRATTA
public class GBSSelector extends TestCaseSelector_reg {


	public double REstimate;
	public double trueReliability;
	public int numberOfDetectedFailurePoints;
	public int numberOfExecutedTestCases;
	public long executionTime;

	public int failure_eps_1 = 0;
	public int failure_eps_2 = 0;
	public int failure_eps_3 = 0;
	public int failure_eps_4 = 0;
	public int failure_eps_5 = 0;
	public int failure_eps_6 = 0;
	public int failure_eps_7 = 0;
	public int failure_eps_8 = 0;
	public int failure_eps_9 = 0;
	public int failure_eps_10 = 0;
	
	public int getFailure_eps_1() {
		return failure_eps_1;
	}

	public int getFailure_eps_2() {
		return failure_eps_2;
	}
	
	public int getFailure_eps_3() {
		return failure_eps_3;
	}
	
	public int getFailure_eps_4() {
		return failure_eps_4;
	}

	public int getFailure_eps_5() {
		return failure_eps_5;
	}
	
	public int getFailure_eps_6() {
		return failure_eps_6;
	}
	
	public int getFailure_eps_7() {
		return failure_eps_7;
	}

	public int getFailure_eps_8() {
		return failure_eps_8;
	}
	
	public int getFailure_eps_9() {
		return failure_eps_9;
	}
	
	public int getFailure_eps_10() {
		return failure_eps_10;
	}



	public GBSSelector(ArrayList<TestCase> _potentialTestSuite, ArrayList<ArrayList<TestCase>> _partitions, int _nPartitions, int _initialNumberOfTestCases, int _budget){
		super(_potentialTestSuite,  _partitions, _nPartitions, _initialNumberOfTestCases, _budget);
	};

	public void selectAndRunTestCase(){


		System.out.println("\n\nStarting the AT Algorithm ... \n");
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
		double[] squared_error = new double[this.numberOfPartitions];

		long initTime = System.currentTimeMillis(); 
		boolean testOutcome;
		//STEP 1: initialize 
		for (indexPartition=0; indexPartition< this.numberOfPartitions; indexPartition++) {
			executedtTestCasesPerPartition [indexPartition] = 1;
			failedTestCases[indexPartition] =0;
			domainProbSum[indexPartition] = 0;    
			squared_error[indexPartition] = 0;
		}

		//READ PARTITIONS and initialize probabilities
		for (indexPartition=0; indexPartition<this.numberOfPartitions;indexPartition++){
			for(int indexInTheList=0; indexInTheList<this.partitions.get(indexPartition).size();indexInTheList++)
				domainProbSum[indexPartition] = domainProbSum[indexPartition] +this.partitions.get(indexPartition).get(indexInTheList).getExpectedOccurrenceProbability();
		}

		double diff = 0;
		//STEP 2 and 3: select one test case from each subdomain, and execute it against the software under test; record the result
		for (indexPartition=0; indexPartition< this.numberOfPartitions; indexPartition++){ 
			testCaseToExecute = selectFromPartition(indexPartition);
			squared_error[indexPartition] += testCaseToExecute.runTestCase_squared_error("FITTIZIO");

			diff = testCaseToExecute.getLabel()-testCaseToExecute.getSUT();
			if(diff <0) diff=-1*diff;

			if(diff >= 1) {
				failure_eps_1++;
				failure_eps_2++;
				failure_eps_3++;
				failure_eps_4++;
				failure_eps_5++;
				failure_eps_6++;
				failure_eps_7++;
				failure_eps_8++;
				failure_eps_9++;
				failure_eps_10++;
			} else if (diff >= 0.9) {
				failure_eps_1++;
				failure_eps_2++;
				failure_eps_3++;
				failure_eps_4++;
				failure_eps_5++;
				failure_eps_6++;
				failure_eps_7++;
				failure_eps_8++;
				failure_eps_9++;
			} else if (diff >= 0.8) {
				failure_eps_1++;
				failure_eps_2++;
				failure_eps_3++;
				failure_eps_4++;
				failure_eps_5++;
				failure_eps_6++;
				failure_eps_7++;
				failure_eps_8++;
			} else if (diff >= 0.7) {
				failure_eps_1++;
				failure_eps_2++;
				failure_eps_3++;
				failure_eps_4++;
				failure_eps_5++;
				failure_eps_6++;
				failure_eps_7++;
			} else if (diff >= 0.6) {
				failure_eps_1++;
				failure_eps_2++;
				failure_eps_3++;
				failure_eps_4++;
				failure_eps_5++;
				failure_eps_6++;
			} else if (diff >= 0.5) {
				failure_eps_1++;
				failure_eps_2++;
				failure_eps_3++;
				failure_eps_4++;
				failure_eps_5++;
			} else if (diff >= 0.4) {
				failure_eps_1++;
				failure_eps_2++;
				failure_eps_3++;
				failure_eps_4++;
			} else if (diff >= 0.3) {
				failure_eps_1++;
				failure_eps_2++;
				failure_eps_3++;
			} else if (diff >= 0.2) {
				failure_eps_1++;
				failure_eps_2++;
			} else if (diff >= 0.1) {
				failure_eps_1++;
			}
			indexCurrentTest++;
			executedtTestCasesPerPartition[indexPartition]++; //LO AGGIUNGO IO
		}


		double maximumGradient;
		int maximumGradientPartition=0;
		while(indexCurrentTest < totalTests){

			//STEP 5: update estimate of theta 
			for (indexPartition=0; indexPartition< this.numberOfPartitions; indexPartition++){
				gradient[indexPartition]= (Math.pow(domainProbSum[indexPartition],2)*squared_error[indexPartition] *(1-squared_error[indexPartition]))/Math.pow(executedtTestCasesPerPartition[indexPartition], 2);
			}

			//STEP 6 select the subdomain with the GREATEST GRADEINT
			maximumGradient=gradient[0];
			maximumGradientPartition=0;
			for (indexPartition=1; indexPartition < this.numberOfPartitions; indexPartition++){
				if(gradient[indexPartition] > maximumGradient){
					maximumGradient =gradient[indexPartition];
					maximumGradientPartition = indexPartition; 
				}
			}

			testCaseToExecute = selectFromPartition(maximumGradientPartition);
			squared_error[maximumGradientPartition] += testCaseToExecute.runTestCase_squared_error("FITTIZIO");

			diff = testCaseToExecute.getLabel()-testCaseToExecute.getSUT();
			if(diff <0) diff=-1*diff;

			if(diff >= 1) {
				failure_eps_1++;
				failure_eps_2++;
				failure_eps_3++;
				failure_eps_4++;
				failure_eps_5++;
				failure_eps_6++;
				failure_eps_7++;
				failure_eps_8++;
				failure_eps_9++;
				failure_eps_10++;
			} else if (diff >= 0.9) {
				failure_eps_1++;
				failure_eps_2++;
				failure_eps_3++;
				failure_eps_4++;
				failure_eps_5++;
				failure_eps_6++;
				failure_eps_7++;
				failure_eps_8++;
				failure_eps_9++;
			} else if (diff >= 0.8) {
				failure_eps_1++;
				failure_eps_2++;
				failure_eps_3++;
				failure_eps_4++;
				failure_eps_5++;
				failure_eps_6++;
				failure_eps_7++;
				failure_eps_8++;
			} else if (diff >= 0.7) {
				failure_eps_1++;
				failure_eps_2++;
				failure_eps_3++;
				failure_eps_4++;
				failure_eps_5++;
				failure_eps_6++;
				failure_eps_7++;
			} else if (diff >= 0.6) {
				failure_eps_1++;
				failure_eps_2++;
				failure_eps_3++;
				failure_eps_4++;
				failure_eps_5++;
				failure_eps_6++;
			} else if (diff >= 0.5) {
				failure_eps_1++;
				failure_eps_2++;
				failure_eps_3++;
				failure_eps_4++;
				failure_eps_5++;
			} else if (diff >= 0.4) {
				failure_eps_1++;
				failure_eps_2++;
				failure_eps_3++;
				failure_eps_4++;
			} else if (diff >= 0.3) {
				failure_eps_1++;
				failure_eps_2++;
				failure_eps_3++;
			} else if (diff >= 0.2) {
				failure_eps_1++;
				failure_eps_2++;
			} else if (diff >= 0.1) {
				failure_eps_1++;
			}
			
			executedtTestCasesPerPartition[maximumGradientPartition]++;

			indexCurrentTest++;
		}

		//last update of theta
		//	if (failedTestCases[maximumGradientPartition] == 0)
		//		failureRates[maximumGradientPartition] = (failedTestCases[maximumGradientPartition]+1)/(executedtTestCasesPerPartition[maximumGradientPartition] +1);
		//	else 


		//STEP 8 EVALUATE THE RELIABILITY
		double mse_estimate = 0.0;
		for (indexPartition=0; indexPartition< this.numberOfPartitions; indexPartition++){
			mse_estimate =  mse_estimate + domainProbSum[indexPartition]*(squared_error[indexPartition]/executedtTestCasesPerPartition[indexPartition]);  
		}
		this.REstimate = mse_estimate; 
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
		double outcome=0;
		double unrel=0.0;
		for (int i=0; i<this.potentialTestSuite.size(); i++){
			outcome += this.potentialTestSuite.get(i).get_squared_error();
		}
		unrel = outcome/(double)this.potentialTestSuite.size();

		/*    System.out.println("potentialTestSuite.size(): "+potentialTestSuite.size());
        System.out.println("Unreliability: "+unrel);
        System.out.println("TotFailPoint From the beginning "+totalFailPoints);
		 */
		return unrel;
	}

	private TestCase selectFromPartition(int maximumGradientPartition){

		//SELECT RANDOMLY A TEST IN THE PARTITION
		int randIndex =  new Random().nextInt(this.partitions.get(maximumGradientPartition).size());	
		return  this.partitions.get(maximumGradientPartition).get(randIndex);

	}

} 