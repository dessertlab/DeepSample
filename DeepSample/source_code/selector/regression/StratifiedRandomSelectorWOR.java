package selector.regression;
import java.util.ArrayList;
import java.util.Random;

import utility.regression.TestCase;

public class StratifiedRandomSelectorWOR extends TestCaseSelector_reg {


	public double REstimate;
	public double trueReliability;

	public double numberOfDetectedFailurePoints;
	public double numberOfExecutedTestCases;
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


	public StratifiedRandomSelectorWOR(ArrayList<TestCase> _potentialTestSuite, ArrayList<ArrayList<TestCase>> _partitions, int _nPartitions, int _initialNumberOfTestCases, int _budget, int  _numberOfFaults){
		super(_potentialTestSuite,  _partitions, _nPartitions, _initialNumberOfTestCases, _budget, _numberOfFaults);
	};

	public void selectAndRunTestCase(){
		TestCase testCaseToExecute;
		double diff=0;
		int totalFailurePoint=0;
		int indexCurrentTest = 0;
		int totalTests = this.budget;
		double[] executedtTestCasesPerPartition = new double[this.numberOfPartitions]; //ni
		double[] squared_error = new double[this.numberOfPartitions]; //theta
		int[] failedTestCases  =  new int[this.numberOfPartitions]; //z
		double[] domainProbSum = new double[this.numberOfPartitions];  //DOMAIN SIZE: p
		int indexPartition = 0; 


		double meanFailureLikelihood[] = new double[this.numberOfPartitions]; 
		double varianceFailureLikelihood[] = new double[this.numberOfPartitions];
		double sumOfWeightedSTD = 0;

		for (indexPartition=0; indexPartition< this.numberOfPartitions; indexPartition++) {
			meanFailureLikelihood[indexPartition] = 0;
			for (int index = 0 ; index < this.partitions.get(indexPartition).size(); index++){
				meanFailureLikelihood[indexPartition] = meanFailureLikelihood[indexPartition] + this.partitions.get(indexPartition).get(index).getExpectedFailureLikelihood();
			} 
			meanFailureLikelihood[indexPartition] = meanFailureLikelihood[indexPartition]/this.partitions.get(indexPartition).size();
			varianceFailureLikelihood[indexPartition] = 0;
			for (int index = 0 ; index < this.partitions.get(indexPartition).size(); index++){
				varianceFailureLikelihood[indexPartition] = varianceFailureLikelihood[indexPartition] + Math.pow((this.partitions.get(indexPartition).get(index).getExpectedFailureLikelihood() - meanFailureLikelihood[indexPartition]), 2);
			} 
			varianceFailureLikelihood[indexPartition] = varianceFailureLikelihood[indexPartition]/(this.partitions.get(indexPartition).size() - 1); 
			sumOfWeightedSTD = sumOfWeightedSTD + Math.sqrt(varianceFailureLikelihood[indexPartition])*this.partitions.get(indexPartition).size(); 
		}

		int[] testCasesPerPartition  =  new int[this.numberOfPartitions];

		int temp_tot = 0;

		for (indexPartition=0; indexPartition< this.numberOfPartitions; indexPartition++) {
			testCasesPerPartition[indexPartition] = Math.min(this.partitions.get(indexPartition).size(), (int) Math.round(totalTests * this.partitions.get(indexPartition).size() * Math.sqrt(varianceFailureLikelihood[indexPartition])/(sumOfWeightedSTD)));
			temp_tot = temp_tot + testCasesPerPartition[indexPartition];
			System.out.println("[DEBUG] PartitionSize: "+this.partitions.get(indexPartition).size());
			System.out.println("[DEBUG] testCasesPerPartition: "+testCasesPerPartition[indexPartition]);
		}

		int residual_tests = totalTests-temp_tot;
		int add_tests=0;

		if(residual_tests>0) {
			System.out.println("[DEBUG] Residual: "+ (totalTests-temp_tot));
			System.out.println("[DEBUG] Second try");
			for (indexPartition=0; indexPartition< this.numberOfPartitions; indexPartition++) {
				add_tests = Math.min(this.partitions.get(indexPartition).size()-testCasesPerPartition[indexPartition], (int) Math.round(residual_tests * this.partitions.get(indexPartition).size() * Math.sqrt(varianceFailureLikelihood[indexPartition])/(sumOfWeightedSTD)));
				testCasesPerPartition[indexPartition] = add_tests + testCasesPerPartition[indexPartition];
				temp_tot = temp_tot + add_tests;
				System.out.println("[DEBUG] PartitionSize: "+this.partitions.get(indexPartition).size());
				System.out.println("[DEBUG] testCasesPerPartition: "+testCasesPerPartition[indexPartition]);
			}

			System.out.println("[DEBUG] Residual: "+ (totalTests-temp_tot));
			residual_tests = totalTests-temp_tot;

			indexPartition = 0;
			while(residual_tests>0) {
				System.out.println("[DEBUG] Final try");
				if(this.partitions.get(indexPartition).size()-testCasesPerPartition[indexPartition]>0) {
					add_tests = Math.min(residual_tests, this.partitions.get(indexPartition).size()-testCasesPerPartition[indexPartition]);
					testCasesPerPartition[indexPartition] = add_tests + testCasesPerPartition[indexPartition];
					temp_tot = temp_tot + add_tests;
					residual_tests = totalTests-temp_tot;
					System.out.println("[DEBUG] PartitionSize: "+this.partitions.get(indexPartition).size());
					System.out.println("[DEBUG] testCasesPerPartition: "+testCasesPerPartition[indexPartition]);
					System.out.println("[DEBUG] Residual: "+ (totalTests-temp_tot));
				}
				indexPartition++;
			}
		}

		//        System.out.println(testCasesPerPartition[indexPartition]);
		/************************/


		long initTime = System.currentTimeMillis();    



		double[] squared_errorPerPartition = new double[this.numberOfPartitions]; 
		double final_MSE = 0.0; 
		for (indexPartition=0; indexPartition< this.numberOfPartitions; indexPartition++) {
			squared_errorPerPartition[indexPartition] = 0.0; 

			//                    System.out.println("[DEBUG] original size: " + this.partitions.get(indexPartition).size());
			ArrayList<TestCase> samplTestCase = new ArrayList<TestCase>(this.partitions.get(indexPartition));

			for (int numberOfTests =0; numberOfTests < testCasesPerPartition[indexPartition]; numberOfTests++) {

				int randIndex = new Random().nextInt(samplTestCase.size());
				testCaseToExecute = samplTestCase.get(randIndex);

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


				executedtTestCasesPerPartition[indexPartition]++;
				indexCurrentTest++;
				samplTestCase.remove(randIndex);
			}

			//                    System.out.println("[DEBUG] sampled test array size: " + samplTestCase.size());
			//            		System.out.println("[DEBUG] original size: " + this.partitions.get(indexPartition).size());
			if(executedtTestCasesPerPartition[indexPartition]>0) {
				squared_errorPerPartition[indexPartition] = squared_error[indexPartition]/executedtTestCasesPerPartition[indexPartition]; 
				//                    System.out.println("[DEBUG] executedtTestCasesPerPartition: " + executedtTestCasesPerPartition[indexPartition]);
				//                    System.out.println("[DEBUG] failureRatePerPartition: " + failureRatePerPartition[indexPartition]);
				final_MSE = final_MSE + squared_errorPerPartition[indexPartition]*this.partitions.get(indexPartition).size();
			}
		}
		final_MSE = final_MSE/potentialTestSuite.size();

		System.out.println("[DEBUG] estimate: " + final_MSE);


		this.REstimate = final_MSE;
		this.trueReliability = computeTrueReliability();
		long endTime = System.currentTimeMillis(); 
		this.executionTime = endTime - initTime; 
		this.numberOfExecutedTestCases = indexCurrentTest;

//		System.out.println("\n ************ True Reliability: "+trueReliability);
//		System.out.println("\n ************ Reliability Estimate: "+REstimate);
//		System.out.println("\n ************ Offset: "+(REstimate - trueReliability));
//		System.out.println("\n ************ squared error of "+Math.pow((REstimate - trueReliability),2));



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


} 
