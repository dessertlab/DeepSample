package selector.classification;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import utility.*;

public class DeepESTSelector {
	private int numfp;

	private double[] z;

	private ArrayList<String> failedRequest;
	
	public DeepESTSelector() {
		super();
		numfp = 0;
		failedRequest = new ArrayList<String>();
	}

	public double[] getZ() {
		return z;
	}
	
	public int getnumfp(){
		return numfp;
	}
	
	public ArrayList<String> getfailedRequest(){
		return failedRequest;
	}

	/**********************Selection and execution************************/	
	public double[] selectAndRunTestCase(int n, ArrayList<TestFrame> testFrame, double[][] weightsMatrix, double d){
		failedRequest.clear();
		numfp = 0;

		//Verifica dei dati in input
		if(n > testFrame.size() || n <= 0){
			double[] error =  {-1.0, -1.0};
			return error;
		}
		
		if(d <= 0 || d >= 1){
			double[] error =  {-2.0, -2.0};
			return error;
		}

		/**************Initialization****************/
		ArrayList<Integer> scompl = new ArrayList<Integer>();
		double[] occurrenceProb = new double[testFrame.size()];

		for(int i = 0; i < testFrame.size(); i++){
			scompl.add(i);
			occurrenceProb[i] = testFrame.get(i).getOccurrenceProb();
			//			System.out.println(occurrenceProb[i]);
		}

		int randomNum = ThreadLocalRandom.current().nextInt(0, testFrame.size());


		ActiveSet ak = new ActiveSet(n, testFrame.size(), occurrenceProb);

		String name = testFrame.get(randomNum).getTfID();
		boolean esito = testFrame.get(randomNum).extractAndExecuteTestCase();
		String tc = testFrame.get(randomNum).getName();

		ak.activeSetUpdate(name , randomNum, esito, weightsMatrix);
		scompl.remove(randomNum);

		double y = 0;

		if (esito) {
			y = 1;
			numfp++;
			this.failedRequest.add(tc);
		}


		double[] estimationX = new double[n];


		estimationX[0] = (testFrame.size())*(occurrenceProb[randomNum]*y);


		double ziX = 0;
		int current_tf = 0;
		int k = 1;
		int ck = 0;
		double weightsSum = 0;
		double prob = 0;

		/****************Sampling*****************/
		while(k < n){
			weightsSum = 0;
			for(int i=0; i<scompl.size(); i++){
				weightsSum = weightsSum + ak.getWeights(scompl.get(i));
			}

			if(weightsSum == 0){
				prob = d + 0.1;
			} else{
				prob = Math.random();
			}

			if (prob <= d){
				current_tf = ak.testFrameExtraction(d);
				name = testFrame.get(current_tf).getTfID();
				esito = testFrame.get(current_tf).extractAndExecuteTestCase();
				
				ziX = ak.getOutcomeSumX();

				if(esito){
					//					System.out.println("[DEBUG] qi: "+ak.qi);
					ziX = ziX + occurrenceProb[current_tf]/ak.qi;
					numfp++;
					tc = testFrame.get(current_tf).getName();
					this.failedRequest.add(tc);
				}

				ak.activeSetUpdate(name , current_tf, esito, weightsMatrix);

				ck = 0;
				while(ck < scompl.size() && scompl.get(ck)!=current_tf){
					ck++;
				}

				if(ck == scompl.size()){
					System.out.println("[ERROR] OUT OF RANGE scompl 'Ramo 1'");
				}else{
					scompl.remove(ck);
				}

			} else {
				randomNum = ThreadLocalRandom.current().nextInt(0, scompl.size());

				current_tf = scompl.get(randomNum);

				name = testFrame.get(current_tf).getTfID();
				esito = testFrame.get(current_tf).extractAndExecuteTestCase();
			
				ziX = ak.getOutcomeSumX();

				if(esito){
					ak.qiCalculation(d, current_tf);
					ziX = ziX + (occurrenceProb[current_tf]/ak.qi);
					numfp++;
					tc = testFrame.get(current_tf).getName();
					this.failedRequest.add(tc);
				}

				//				System.out.println("[DEBUG] qi =" + ak.qi);	
				//				System.out.println("[DEBUG] z"+k+" =" + ziX);

				ak.activeSetUpdate(name , current_tf, esito, weightsMatrix);
				scompl.remove(randomNum);
				//				System.out.println("[DEBUG] scompl" + scompl);
			}

			estimationX[k] = ziX;
			k++;
		}

		//		ak.printSelectedTestFrame();
		/***************Estimation***************/
		this.z = estimationX;
		return this.estimatorBoCSP(n, estimationX);

	}


	/**********************Estimator************************/
	private double[] estimatorBoCSP(int n, double[] estimationX){
		double sumX = 0;

		for(int i=0; i<n; i++){
//			System.out.println("[DEBUG] "+(i)+") " + estimationX[i]);
			sumX = sumX + estimationX[i];
		}

		double[] stima = new double[2];

		stima[0] = (1/(double)(n))*sumX;

		double num = 0;

		for(int i=1; i<n; i++){
			num = num + Math.pow((estimationX[i] - estimationX[0]), 2);
		}

		stima[1]= num/(double)(n*(n-1));


		return stima;
	}

}

