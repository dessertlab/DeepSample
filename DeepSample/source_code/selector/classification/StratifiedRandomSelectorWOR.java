package selector.classification;
import java.util.ArrayList;
import java.util.Random;

import utility.TestCase;

//DA FARE COME OMPLEMETNAZIONEDI CLASSE ASTRATTA
public class StratifiedRandomSelectorWOR extends TestCaseSelector {

	
	 public double REstimate;
	 public double trueReliability;

	 public double numberOfDetectedFailurePoints;
	  public double numberOfExecutedTestCases;
	  public long executionTime;
	  
	  
	public StratifiedRandomSelectorWOR(ArrayList<TestCase> _potentialTestSuite, ArrayList<ArrayList<TestCase>> _partitions, int _nPartitions, int _initialNumberOfTestCases, int _budget, int  _numberOfFaults){
		super(_potentialTestSuite,  _partitions, _nPartitions, _initialNumberOfTestCases, _budget, _numberOfFaults);
	};
	
	public void selectAndRunTestCase(){
		TestCase testCaseToExecute;
		boolean testOutcome;
		int totalFailurePoint=0;
		int indexCurrentTest = 0;
		int totalTests = this.budget;
		double[] executedtTestCasesPerPartition = new double[this.numberOfPartitions]; //ni
		double[] failureRates = new double[this.numberOfPartitions]; //theta
		int[] failedTestCases  =  new int[this.numberOfPartitions]; //z
		double[] domainProbSum = new double[this.numberOfPartitions];  //DOMAIN SIZE: p
		int indexPartition = 0; 
		
		
		//allocazione ottima, ma sulla variabile ausiliaria (tiene conto sia del numero di campioni, come allocazione proporzionale, sia della varianza della var. Aux)

        //1. Calcola media e varianza intra-partitions
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
          //  System.out.println("[DEBUG] PartitionSize: "+this.partitions.get(indexPartition).size());
          //  System.out.println("[DEBUG] testCasesPerPartition: "+testCasesPerPartition[indexPartition]);
        }
        
        int residual_tests = totalTests-temp_tot;
        int add_tests=0;
        
        if(residual_tests>0) {
        //	System.out.println("[DEBUG] Residual: "+ (totalTests-temp_tot));
        //	System.out.println("[DEBUG] Second try");
        	for (indexPartition=0; indexPartition< this.numberOfPartitions; indexPartition++) {
        		add_tests = Math.min(this.partitions.get(indexPartition).size()-testCasesPerPartition[indexPartition], (int) Math.round(residual_tests * this.partitions.get(indexPartition).size() * Math.sqrt(varianceFailureLikelihood[indexPartition])/(sumOfWeightedSTD)));
        		testCasesPerPartition[indexPartition] = add_tests + testCasesPerPartition[indexPartition];
        		temp_tot = temp_tot + add_tests;
        	//	System.out.println("[DEBUG] PartitionSize: "+this.partitions.get(indexPartition).size());
        	//	System.out.println("[DEBUG] testCasesPerPartition: "+testCasesPerPartition[indexPartition]);
        	}

        //	System.out.println("[DEBUG] Residual: "+ (totalTests-temp_tot));
        	residual_tests = totalTests-temp_tot;

        	indexPartition = 0;
        	while(residual_tests>0) {
        	//	System.out.println("[DEBUG] Final try");
        		if(this.partitions.get(indexPartition).size()-testCasesPerPartition[indexPartition]>0) {
        			add_tests = Math.min(residual_tests, this.partitions.get(indexPartition).size()-testCasesPerPartition[indexPartition]);
        			testCasesPerPartition[indexPartition] = add_tests + testCasesPerPartition[indexPartition];
        			temp_tot = temp_tot + add_tests;
        			residual_tests = totalTests-temp_tot;
        		//	System.out.println("[DEBUG] PartitionSize: "+this.partitions.get(indexPartition).size());
        		//	System.out.println("[DEBUG] testCasesPerPartition: "+testCasesPerPartition[indexPartition]);
        		//	System.out.println("[DEBUG] Residual: "+ (totalTests-temp_tot));
        		}
        		indexPartition++;
        	}
        }
        
//      //	System.out.println(testCasesPerPartition[indexPartition]);
		/************************/
		
		
        long initTime = System.currentTimeMillis();    

        

        double[] failureRatePerPartition = new double[this.numberOfPartitions]; 
        double sumFailureRate = 0.0; 
        for (indexPartition=0; indexPartition< this.numberOfPartitions; indexPartition++) {
                    failureRatePerPartition[indexPartition] = 0.0; 
                    
//                    System.out.println("[DEBUG] original size: " + this.partitions.get(indexPartition).size());
                    ArrayList<TestCase> samplTestCase = new ArrayList<TestCase>(this.partitions.get(indexPartition));
                    
                    for (int numberOfTests =0; numberOfTests < testCasesPerPartition[indexPartition]; numberOfTests++) {
                    	
                    	int randIndex = new Random().nextInt(samplTestCase.size());
                        testCaseToExecute = samplTestCase.get(randIndex);
                        
                        testOutcome = testCaseToExecute.runTestCase("FITTIZIO");
                        if (testOutcome==false) {
                            failedTestCases[indexPartition]++;
                            totalFailurePoint++;
                            }
                        executedtTestCasesPerPartition[indexPartition]++;
                        indexCurrentTest++;
                        samplTestCase.remove(randIndex);
                    }
                    
//                    System.out.println("[DEBUG] sampled test array size: " + samplTestCase.size());
//            		System.out.println("[DEBUG] original size: " + this.partitions.get(indexPartition).size());
                    if(executedtTestCasesPerPartition[indexPartition]>0) {
                    failureRatePerPartition[indexPartition] = failedTestCases[indexPartition]/executedtTestCasesPerPartition[indexPartition]; 
//                    System.out.println("[DEBUG] executedtTestCasesPerPartition: " + executedtTestCasesPerPartition[indexPartition]);
//                    System.out.println("[DEBUG] failureRatePerPartition: " + failureRatePerPartition[indexPartition]);
                    sumFailureRate = sumFailureRate + failureRatePerPartition[indexPartition]*this.partitions.get(indexPartition).size();
                    }
        }
        sumFailureRate = sumFailureRate/potentialTestSuite.size();

      //  System.out.println("[DEBUG] estimate: " + sumFailureRate);
 

        this.REstimate = 1 - sumFailureRate;
        this.trueReliability = computeTrueReliability();
        long endTime = System.currentTimeMillis(); 
        this.executionTime = endTime - initTime; 
        this.numberOfExecutedTestCases = indexCurrentTest;
        this.numberOfDetectedFailurePoints = totalFailurePoint; 

      //  System.out.println("\n ************ True Reliability: "+trueReliability);
      //  System.out.println("\n ************ Reliability Estimate: "+REstimate);
      //  System.out.println("\n ************ Offset: "+(REstimate - trueReliability));
      //  System.out.println("\n ************ squared error of "+Math.pow((REstimate - trueReliability),2));
      //  System.out.println("\n ************ Detected Failures: "+totalFailurePoint);

        
 

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
    /*    System.out.println("potentialTestSuite.size(): "+potentialTestSuite.size());
        System.out.println("Unreliability: "+unrel);
        System.out.println("TotFailPoint From the beginning "+totalFailPoints);
        */
        return (1- unrel);
    }


} 
