package selector.classification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.apache.commons.math3.distribution.*;

import utility.TestCase;

import org.apache.commons.lang3.mutable.*;

public class RHCSSelector extends TestCaseSelector {
	
	
	  public double estimate;
	  public double averageEstimate;
	  public double averageEstimateOffset;
	  public double varianceEstimate;
	  public double bestVarianceEstimate;
	  public double bestVariance;
	  public double bestVarianceEstimateOffset;
	  public double bestOffsetEstimate;
	  public double bestOffsetEstimateOffset;
	  public double trueReliability;
	  public int numberOfRemovedFaults;
	  public int numberOffailedTestCases; 
	  public int numberOfExecutedTestCases;
	  public long executionTime;
	  //public int removedFailurePoints; IN THE TSE CASE. HERE REPLACED WITH failurePoints
	  public int failurePoints;
	  public ArrayList<TestCase> failedTestCasesList;

	  public ArrayList<TestCase> copy;
	
	
	public RHCSSelector(ArrayList<TestCase> _potentialTestSuite, int _initialNumberOfTestCases, int _budget) {
		super(_potentialTestSuite,  _initialNumberOfTestCases, _budget);
		failurePoints=0;  
		copy = new ArrayList<TestCase>();
		copy.addAll(_potentialTestSuite);
		failedTestCasesList = new ArrayList<TestCase>();
	}
	
	
	public void selectAndRunTestCase(){}
	public void selectAndRunTestCase(double _desiredPercentageAccuracy) {
	
		System.out.println("\n\nStarting the RDT Algorithm ... \n");		
		int totalTests = this.budget; 
	    //double desiredPercentageAccuracy = _desiredPercentageAccuracy;    
	    
	    ArrayList<Integer> TCperIteration = new ArrayList<Integer>();
	    ArrayList<TestCase> FailedTestCases = new ArrayList<TestCase>();	     
	    ArrayList<TestCase> testListCopy = new ArrayList<TestCase>();
	    testListCopy.addAll(this.potentialTestSuite);
	    int numberOfTestCurrentIteration = this.initialNumberOfTestCases;
	     
	    int Iteration =0;
	    int indexCurrentTest = 0;
	    int TotNumberOfFailurePointsPerIteration=0;
	    this.failurePoints=0;
		TotNumberOfFailurePointsPerIteration=0;
				
		MutableDouble sampleOutput = null; 
		MutableDouble estimatedOutput=null;
		MutableDouble estimatedVariance= null;
		MutableDouble estiamtedCI_HalfWidth=null; 

	   long initTime = System . currentTimeMillis () ;
	   while (indexCurrentTest < totalTests){
	    	
	    	/************ RDT SELECTION ALGORITHM ************/
	    	sampleOutput=new MutableDouble(0); estimatedOutput=new MutableDouble(0); estimatedVariance = new MutableDouble(0); estiamtedCI_HalfWidth=new MutableDouble(0);
	    	try {// WE USE THE POTENTIAL TEST SUITE: IN CASE OF MLTIPLE ITERATIONS, THIS SUITE IS REDUCED EACH TIME
				selectAndExecute_RHC(this.potentialTestSuite, numberOfTestCurrentIteration, sampleOutput, estimatedOutput, estimatedVariance, estiamtedCI_HalfWidth, FailedTestCases);
			} catch (Exception e) {e.printStackTrace();}
	    			
	    	TotNumberOfFailurePointsPerIteration = this.failurePoints - TotNumberOfFailurePointsPerIteration;
	    	indexCurrentTest=indexCurrentTest+numberOfTestCurrentIteration;
	    
			TCperIteration.add(Iteration, numberOfTestCurrentIteration);
	   		Iteration++;
	    }
	    long endTime = System.currentTimeMillis();    	   
	    
	  //  System.out.println("\n\n ************ RESULTS FOR ONE REPLICA:\n ");
	    
	    if (estimatedOutput.doubleValue()>1){
			System.out.println("The failure probability estiamte provided a value greater to 1 (the RHC is agnositc with respect to the probability nature of the estimate). For consistency , it is forced to 1.");
			estimatedOutput.setValue(1);
		}
	  //  System.out.println("\n ************ Failure Probability Estimate: "+(estimatedOutput.doubleValue()));
	  //  System.out.println("\n ************ Reliability Estimate: "+(1-estimatedOutput.doubleValue()));
	  //  System.out.println("\n ************ Variance Estimate: "+estimatedVariance.doubleValue());
	  //  System.out.println("\n ************ Total number of executed test cases: "+indexCurrentTest);
	  //  System.out.println("\n ************ Total number of detected failed test cases: "+FailedTestCases.size());
	  //  System.out.println("\n ************ Total number of executed test cases: "+indexCurrentTest);
	   	
	    /* OUTPUT NEL CASO DI ITERAZIONE SINGOLA */
	    this.estimate = (1-estimatedOutput.doubleValue());
	    this.varianceEstimate = estimatedVariance.doubleValue();
	    this.numberOfExecutedTestCases = indexCurrentTest;
	    this.numberOffailedTestCases= FailedTestCases.size(); 
	    this.executionTime = endTime - initTime;
	    this.failedTestCasesList = FailedTestCases;
	}
	
	private double computeVarianceEstimator(MutableDouble estimatedOutput, int sampleSize, int populationSize, double[] pValue, double[] q, double[] unreliability, double sumSquaredGroupsSize ) {
		double firstTerm = (sumSquaredGroupsSize-populationSize)/(Math.pow(populationSize, 2) - sumSquaredGroupsSize);
		double sumSecondTerm =0.0;
    	for (int r=0; r<sampleSize;r++)
    		sumSecondTerm = sumSecondTerm + q[r]*Math.pow((unreliability[r]/pValue[r] - estimatedOutput.doubleValue()),2);
		
    	return (firstTerm*sumSecondTerm);
	}
	
	private void selectAndExecute_RHC(ArrayList<TestCase> population, int sampleSize, MutableDouble sampleOutput, MutableDouble estimatedOutput, MutableDouble estimatedVariance, MutableDouble estiamtedCI_HalfWidth, ArrayList<TestCase> FailedTestCases) throws Exception {
	
			System.out.println("Size of the population "+population.size());
			System.out.println("Size of the sample "+sampleSize);
			int originalSize= population.size();
			TestCase testCaseToExecute = null;	 //Test Case = Sampling Unit
			double[] pValue =  new double[sampleSize];
			double[] q = new double[sampleSize];
			double[] unreliability = new double[sampleSize];
			double[] p = new double[population.size()]; 
			double[] size = new double[population.size()]; //FOR A PPS SAMPLING
			double sumOverSize = 0.0; double sumOverP=0.0;
			for (int t=0 ; t < population.size(); t++){
				size[t] = population.get(t).getExpectedOccurrenceProbability()*population.get(t).getExpectedFailureLikelihood();
		//		System.out.println("size values "+size[t]);
				sumOverSize = sumOverSize + size[t];
			}
			for (int t=0 ; t < population.size(); t++){
				p[t] = size[t]/sumOverSize;
				sumOverP += p[t];
		//		System.out.println("p "+p[t]);
			}
			/********************** 	STEP 1: rescale domain: it appleis in case the RHC is applied at partition-level (within a partition) ************************/
		/*	double sumOverP = 0.0;
			for (int t=0 ; t < population.size(); t++){
				p[t] = population.get(t).getExpectedOccurrenceProbability();
				sumOverP = sumOverP + p[t];
			}
			// NEL CASO JSS2017 LA SOMMA DOVREBBE ESSERE 1, QUINDI QUSTO PASSAGGIO NON HA EFFETTO
			
			double sumOverPControl=0.0;
			for (int t=0 ; t < population.size(); t++){
				p[t] = p[t]/sumOverP;
				sumOverPControl += p[t];
			}
		 */
			
			/**********************	STEP 2: crea g gruppi omogenei **********************/
			int numberOfGroups = sampleSize;
	//		System.out.println("Test cases to execute in Partition "+indexPartition+" at Iteration "+Iteration+": "+numberOfGroups);

			int groupSize = (int)Math.floor(p.length/sampleSize); 
	//		System.out.println("\ngroup size "+groupSize);
			
			double[][] GMatrix;
			int[][] GMatrixIndex;
			if (groupSize*numberOfGroups==p.length){
				GMatrix = new double[numberOfGroups][groupSize];
				GMatrixIndex = new int[numberOfGroups][groupSize];
			}
			else{
				GMatrix = new double[numberOfGroups][groupSize+1];
				GMatrixIndex = new int[numberOfGroups][groupSize+1];
			}	    			
		
			ArrayList<Integer> listOfIndexes = new ArrayList<Integer>();
			for(int i=0; i< p.length; i++)
				listOfIndexes.add(i);
	
			int indexForGroup;
			for (int indexGroup1=0; indexGroup1< GMatrix.length;indexGroup1++){
				for (int indexGroup2=0; indexGroup2< GMatrix[0].length-1;indexGroup2++){
					int randomInt =new Random().nextInt(listOfIndexes.size());
					indexForGroup  = listOfIndexes.remove(randomInt); 
					GMatrix[indexGroup1][indexGroup2] = p[indexForGroup]; 
					//CONSERVARE IN MATRICE DI INDICI
					GMatrixIndex[indexGroup1][indexGroup2] = indexForGroup;
				}
			}
		
			int toAssign = listOfIndexes.size();
			for(int i=0; i<GMatrix.length; i++){
				if(!listOfIndexes.isEmpty()){
					indexForGroup = listOfIndexes.remove(0);
					GMatrix[i][GMatrix[0].length-1] = p[indexForGroup];
				 	GMatrixIndex[i][GMatrixIndex[0].length-1] = indexForGroup;
				}	    						
				else{
					GMatrix[i][GMatrix[0].length-1] = 0;
					GMatrixIndex[i][GMatrixIndex[0].length-1] = -1;
					}
				}
					    				
			/********************** 	DIAGNOSTICA **********************/
	//			System.out.println("GMatrix.length "+GMatrix.length);
		//		System.out.println("GMatrix[0].length "+GMatrix[0].length);
			double sumProva = 0;
			for (int i=0; i < p.length ; i++){
	//			System.out.println("p[t] : "+i+": "+p[i]);
				sumProva += p[i];
			}
			sumProva  = 0;
			for (int i=0; i < GMatrix.length; i++){
				for (int j=0; j<GMatrix[0].length; j++){
	//					System.out.println("GMatrix: "+i+","+j+": "+GMatrix[i][j]);
		//				System.out.println("GMatrix Index: "+i+","+j+": "+GMatrixIndex[i][j]);
					sumProva += GMatrix[i][j];
				}
			}
			//	System.out.println("Somma G: "+sumProva);
			
			 /********************** step 3 of RHC: draw a unit from the group with a PPS method **********************/
			
			boolean[] outputValues = new boolean[numberOfGroups];  // IN THIS CASE, THE OUTPUT IS BINARY: IT CAN BE CHANGED TO ANY NUMERIC TYPE 
			double[] probSumOfGroup = new double[numberOfGroups];
			double[][] cumProbOfGroup = new double[GMatrix.length][GMatrix[0].length];
			for (int i=0; i < GMatrix.length; i++){ //numberOfGroups
				for (int j=0; j<GMatrix[0].length; j++){ //groupSize
					probSumOfGroup[i] =probSumOfGroup[i] + GMatrix[i][j]; //VALORE CUMULATIVO
					if (j==0){
						cumProbOfGroup[i][j] = GMatrix[i][j];}
					else{
						cumProbOfGroup[i][j] =cumProbOfGroup[i][j-1] +GMatrix[i][j];//VETTORE DELLE PROB CUMULATIVE	
					}  
	//				System.out.println("cumProbOfGroup: "+i+"."+j+": "+cumProbOfGroup[i][j]);
				}
		//		System.out.println("probSumOfGroup: "+i+": "+probSumOfGroup[i]);
			}
			
			/********************** SELECT UNIT WITH PPS METHOD **********************/
			double randomDouble;
			int indexOfUnitToRead=-1;
			ArrayList<Integer> indexesToRemove = new ArrayList<Integer>();
			for(int i=0;i<numberOfGroups; i++){	
		//		System.out.println("numberOfGroups: "+numberOfGroups);
			//	System.out.println("groupSize: " +groupSize);
				randomDouble =(new Random().nextDouble())*probSumOfGroup[i];
				for (int j=0; j<GMatrix[0].length; j++){ //groupSize
	//				System.out.println("cumprob: "+cumProbOfGroup[i][j]);
					if(randomDouble <= cumProbOfGroup[i][j]){
						indexOfUnitToRead = j;
						break;
					}
				}
//					System.out.println("\nprobSumOfGroup[i]: " +probSumOfGroup[i]);

//				System.out.println("randomDouble: "+randomDouble);
	//			System.out.println("indexOfTestCaseToExecute: "+indexOfTestCaseToExecute);

				
				/********** TO NOT SELECT THE ALREADY SELECTED UNIT************/
				
				if (indexOfUnitToRead==-1){
					System.out.println("Error, no unit to read: "+indexOfUnitToRead);
					System.exit(0);//cambia in stato di errore
				}
				int indexInP = GMatrixIndex[i][indexOfUnitToRead];
	//			System.out.println("indexes in GIndex "+i+", "+indexOfUnitToRead);
				testCaseToExecute = population.get(indexInP);
				//System.out.println("Selcted unit: "+testCaseToExecute.getName());
				

				/********************** EXECUTE TEST CASE and collect output **********************/
				
				outputValues[i] = testCaseToExecute.runTestCase("running...");
				pValue[i] = p[indexInP]; //SIZE=testCaseToExecute.getExpectedOccurrenceProbability()*FailLikelihood 
					
				if (outputValues[i]==false) {
					FailedTestCases.add(testCaseToExecute);
					unreliability[i] = testCaseToExecute.getExpectedOccurrenceProbability(); 
//SOLO NEL CASO TSE	population.get(indexInP).setOutcome(true);
//SOLO NEL CASO TSE	unreliability = unreliability + removeFault(potentialTestList,indexPartition);
					}
				else{
					unreliability[i]=0;
				}
		
				//WITHOUT REPLACEMENT SELECTION: REMOVE FROM POETNTIAL TEST SUITE
				indexesToRemove.add(i,indexInP);//[i] = indexInP;
		//		System.out.println("index to remove "+indexInP);
		//		System.out.println("size of potential list "+potentialTestList.get(indexPartition).size());

			}
		
			
//			System.out.println("\nExecuted test cases in Partition "+indexPartition+" at iteration "+Iteration+": "+testOutcomes.length);
			//WITHOUT REPLACEMENT SELECTION: REMOVE EXECUTED TEST FROM LIST OF POTENTIAL TESTS
			Collections.sort(indexesToRemove, Collections.reverseOrder());
			for (int ind=0;ind<numberOfGroups;ind++) population.remove(indexesToRemove.get(ind).intValue());
	//		System.out.println("size of potential list after "+potentialTestList.get(indexPartition).size());


			/********************** END UNITS READING (i.e., TEST CASE SELECTION AND EXEC) **********************/
								
			/********************** ESTIMATES  **********************/
			
			double sumQ =0;
			for (int r=0;r<numberOfGroups; r++){
				q[r]= probSumOfGroup[r];
				sumQ +=q[r]; 
			}
			double Y =0;   //IN JSS2017 NON E' BINARIO, LA QUANTITA' DA STIMARE E PHI. QUINDI IL VALOER DELL'UNITA' LETTO E' OUTCOME * p(seelection) dell'unita'
			for (int r=0; r<pValue.length;r++){
				if (outputValues[r] == false){
					/*****QUESTO VA TOLOT; APPARTIENE AL CHIAMANTE****/
					this.failurePoints++; 	
					Y = Y + unreliability[r]/(pValue[r]/q[r]);
					if (Double.isNaN(Y)){ //NON DOVREBBE ESSERE POSSIBILE: se pValues fosse 0, non sarebbe selezionato
						throw new Exception("Fatal Error in the computation of reliability estimator");
					}
					sampleOutput.setValue(sampleOutput.doubleValue() + unreliability[r]);
					}
			}
			
			/*********** ESTIMATOR OF THE VARIANCE OF THE ESTIMATE Y ************/
			double sumSquaredGroupsSize = 0.0;
			double actualSize = 0.0;
	    	for (int r=0; r<sampleSize; r++){
	    		actualSize = 0.0;
	    		if(GMatrixIndex[r][GMatrix[0].length-1] == -1){
	    			actualSize = GMatrix[0].length-1;
	    		}
	    		else {
	    			actualSize =  GMatrix[0].length;
	    		}
	    		sumSquaredGroupsSize = sumSquaredGroupsSize + Math.pow(actualSize, 2);
	    	}
	    	estimatedOutput.setValue(Y);
	    	
	    	estimatedVariance.setValue(computeVarianceEstimator(estimatedOutput, numberOfGroups, originalSize, pValue, q, unreliability, sumSquaredGroupsSize));
	    	TDistribution t = new TDistribution(numberOfGroups-1);
	    	estiamtedCI_HalfWidth.setValue(t.inverseCumulativeProbability(0.975) * Math.sqrt(estimatedVariance.doubleValue()));    
	}


};
 