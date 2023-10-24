package selector.regression;

import java.util.ArrayList;
import java.util.Random;

import utility.regression.TestCase;

//DA FARE COME OMPLEMETNAZIONEDI CLASSE ASTRATTA
public class TwoUPSSelector extends TestCaseSelector_reg {
	
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
	  
	public TwoUPSSelector(ArrayList<TestCase> _potentialTestSuite, ArrayList<ArrayList<TestCase>> _partitions, int _nPartitions, int _initialNumberOfTestCases, int _budget){
		super(_potentialTestSuite,  _partitions, _nPartitions, _initialNumberOfTestCases, _budget);
	};
	
	public void selectAndRunTestCase(){

		// DECIDERE QUALE VERSIONE: 
		// 1: SE RIMUOVERE I FAULT OPPURE NO
		// 2: SE FARLA SULLE PARTIZIONI O SUI VALORI SINGOLI
	
		/********** FROZEN CODE VERSION WITHOUT PARTITIONS*********/ 
		
		System.out.println("\n\nStarting the OP Algorithm ... \n");
		
		ArrayList<ArrayList<TestCase>> samplPartitions = new ArrayList<ArrayList<TestCase>>(); 
		
		for(int i=0; i<this.partitions.size();i++){
//			System.out.println("[DEBUG] original size: "+this.partitions.get(i).size());
			ArrayList<TestCase> tempTC = new ArrayList<TestCase>(this.partitions.get(i));
			samplPartitions.add(tempTC);
		}
		
		TestCase testCaseToExecute;
		
		int totalFailurePoint=0;
		int indexCurrentTest = 0;
		int totalTests = this.budget;
		double diff = 0;
		double[] executedtTestCasesPerPartition = new double[this.numberOfPartitions]; //ni
		double[] squared_error = new double[this.numberOfPartitions];
		double[] domainProbSum = new double[this.numberOfPartitions];  //DOMAIN SIZE: p
		int indexPartition = 0; 
		
		for (indexPartition=0; indexPartition< this.numberOfPartitions; indexPartition++) {
			domainProbSum[indexPartition] = 0;
			executedtTestCasesPerPartition [indexPartition] = 0;
			squared_error[indexPartition] =0;
		}
		
		double domainProbSum_sum = 0;
	
		//Prima inizializzazione (dalla seconda devo sottrarre i)
		double[] cumulativePVector = new double[this.potentialTestSuite.size()];
		for (indexPartition=0; indexPartition<this.numberOfPartitions;indexPartition++){
			for(int indexInTheList=0; indexInTheList<samplPartitions.get(indexPartition).size();indexInTheList++)
	    		domainProbSum[indexPartition] = domainProbSum[indexPartition] +samplPartitions.get(indexPartition).get(indexInTheList).getExpectedFailureLikelihood();
				domainProbSum_sum = domainProbSum_sum + domainProbSum[indexPartition];
		}
		
		for(int i =0; i<domainProbSum.length; i++) {
			domainProbSum[i] = domainProbSum[i]/domainProbSum_sum;
		}
		
//		//[DEBUG]
//		double sum = 0;
//		for(int i =0; i<domainProbSum.length; i++) {
//			sum = sum + domainProbSum[i];
//		}
//		System.out.println("[DEBUG] Probability of partitions normalized = "+ sum);
//		//[DEBUG]
		
		cumulativePVector[0]=domainProbSum[0];
		for (indexPartition=1; indexPartition<this.numberOfPartitions;indexPartition++)
			cumulativePVector[indexPartition]=cumulativePVector[indexPartition-1]+domainProbSum[indexPartition]; 
				
		long initTime = System.currentTimeMillis();
		
		while(indexCurrentTest < totalTests){				
			double rand = Math.random();
			int selectedPartition=-1;		
			for(int index =0; index<this.numberOfPartitions;index++){
				if (rand <= cumulativePVector[index]) {
					selectedPartition = index;
//					System.out.println("[DEBUG] "+index);
					break;
					}
			}
			
			if(samplPartitions.get(selectedPartition).size()>0) {
				int randIndex = new Random().nextInt(samplPartitions.get(selectedPartition).size());
				testCaseToExecute = samplPartitions.get(selectedPartition).get(randIndex);

				squared_error[selectedPartition] += testCaseToExecute.runTestCase_squared_error("FITTIZIO");

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

				samplPartitions.get(selectedPartition).remove(randIndex);

				executedtTestCasesPerPartition[selectedPartition]++;	
				indexCurrentTest++;
			}

		}
		//SECTION 6.3
		
		double squared_error_total = 0.0;
		int num = 0;
		
		for (indexPartition=0; indexPartition< this.numberOfPartitions; indexPartition++){
			if(executedtTestCasesPerPartition[indexPartition]!=0) {
				squared_error_total =  squared_error_total + ((double)squared_error[indexPartition]/executedtTestCasesPerPartition[indexPartition])*(this.partitions.get(indexPartition).size())/domainProbSum[indexPartition]; //*(this.partitions.get(indexPartition).size()); //domainProbSum[indexPartition]);  
				num = num + 1;
			}
		}
		squared_error_total = squared_error_total/num;
		squared_error_total =  squared_error_total/this.potentialTestSuite.size();
		
		this.REstimate = squared_error_total;
		this.trueReliability = computeTrueReliability();
		long endTime = System.currentTimeMillis(); 
		this.executionTime = endTime - initTime; 
		this.numberOfExecutedTestCases = indexCurrentTest;
		this.numberOfDetectedFailurePoints = totalFailurePoint; 
		
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