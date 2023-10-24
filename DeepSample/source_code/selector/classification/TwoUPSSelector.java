package selector.classification;

import java.util.ArrayList;
import java.util.Random;

import utility.TestCase;

public class TwoUPSSelector extends TestCaseSelector {
	
	 public double REstimate;
	 public double trueReliability;
	 public int numberOfDetectedFailurePoints;
	 public int numberOfExecutedTestCases;
	 public long executionTime;
	  
	public TwoUPSSelector(ArrayList<TestCase> _potentialTestSuite, ArrayList<ArrayList<TestCase>> _partitions, int _nPartitions, int _initialNumberOfTestCases, int _budget){
		super(_potentialTestSuite,  _partitions, _nPartitions, _initialNumberOfTestCases, _budget);
	};
	
	public void selectAndRunTestCase(){
	
		/********** FROZEN CODE VERSION WITHOUT PARTITIONS*********/ 
		
		System.out.println("\n\nStarting the OP Algorithm ... \n");
		
		ArrayList<ArrayList<TestCase>> samplPartitions = new ArrayList<ArrayList<TestCase>>(); 
		
		for(int i=0; i<this.partitions.size();i++){
//			System.out.println("[DEBUG] original size: "+this.partitions.get(i).size());
			ArrayList<TestCase> tempTC = new ArrayList<TestCase>(this.partitions.get(i));
			samplPartitions.add(tempTC);
		}
		
		TestCase testCaseToExecute;
		boolean testOutcome;
		int totalFailurePoint=0;
		int indexCurrentTest = 0;
		int totalTests = this.budget;
		double[] executedtTestCasesPerPartition = new double[this.numberOfPartitions]; //ni
		int[] failedTestCases  =  new int[this.numberOfPartitions]; //z
		double[] domainProbSum = new double[this.numberOfPartitions];  //DOMAIN SIZE: p
		int indexPartition = 0; 
		
		for (indexPartition=0; indexPartition< this.numberOfPartitions; indexPartition++) {
			domainProbSum[indexPartition] = 0;
			executedtTestCasesPerPartition [indexPartition] = 0;
			failedTestCases[indexPartition] =0;
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
					break;
					}
			}
			
			if(samplPartitions.get(selectedPartition).size()>0) {
				int randIndex = new Random().nextInt(samplPartitions.get(selectedPartition).size());
				testCaseToExecute = samplPartitions.get(selectedPartition).get(randIndex);

				testOutcome = testCaseToExecute.runTestCase(" ");
				if (testOutcome==false) {
					failedTestCases[selectedPartition]++;
					totalFailurePoint++;
				}

				samplPartitions.get(selectedPartition).remove(randIndex);

				executedtTestCasesPerPartition[selectedPartition]++;	
				indexCurrentTest++;
			}
		}
		
		
		//SECTION 6.3
		
		double sumFailureRate = 0.0;
		int num = 0;
		
		for (indexPartition=0; indexPartition< this.numberOfPartitions; indexPartition++){
			if(executedtTestCasesPerPartition[indexPartition]!=0) {
				sumFailureRate =  sumFailureRate + ((double)failedTestCases[indexPartition]/executedtTestCasesPerPartition[indexPartition])*(this.partitions.get(indexPartition).size())/domainProbSum[indexPartition]; //*(this.partitions.get(indexPartition).size()); //domainProbSum[indexPartition]);  
				num = num + 1;
			}
		}
		sumFailureRate = sumFailureRate/num;
		sumFailureRate =  sumFailureRate/this.potentialTestSuite.size();
		
		this.REstimate = 1 - sumFailureRate;
		this.trueReliability = computeTrueReliability();
		long endTime = System.currentTimeMillis(); 
		this.executionTime = endTime - initTime; 
		this.numberOfExecutedTestCases = indexCurrentTest;
		this.numberOfDetectedFailurePoints = totalFailurePoint; 
		
//		System.out.println("\n ************ True Reliability: "+trueReliability);
//		System.out.println("\n ************ Reliability Estimate: "+REstimate);
//		System.out.println("\n ************ Offset: "+(REstimate - trueReliability));
//		System.out.println("\n ************ squared error of "+Math.pow((REstimate - trueReliability),2));
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
	private TestCase selectFromPartition(int indexPartition){
		//SELECT RANDOMLY A TEST IN THE PARTITION
		int randIndex =  new Random().nextInt(this.partitions.get(indexPartition).size());	
		return  this.partitions.get(indexPartition).get(randIndex);
		
	}
} 