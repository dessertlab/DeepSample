package selector.regression;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import utility.regression.ActiveSet;
import utility.regression.TestFrame;

public class DeepESTRegressionSelector_histogram {
	private int failure_eps_1;
	private int failure_eps_2;
	private int failure_eps_3;
	private int failure_eps_4;
	private int failure_eps_5;
	private int failure_eps_6;
	private int failure_eps_7;
	private int failure_eps_8;
	private int failure_eps_9;
	private int failure_eps_10;
	
	public DeepESTRegressionSelector_histogram(){
		super();
		failure_eps_1 = 0;
		failure_eps_2 = 0;
		failure_eps_3 = 0;
		failure_eps_4 = 0;
		failure_eps_5 = 0;
		failure_eps_6 = 0;
		failure_eps_7 = 0;
		failure_eps_8 = 0;
		failure_eps_9 = 0;
		failure_eps_10 = 0;
	}
	
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


	public double[] selectAndRunTestCase(int n, ArrayList<TestFrame> testFrame, double[][] weightsMatrix, double d){

		if(n > testFrame.size() || n <= 0){
			double[] error =  {-1.0, -1.0};
			return error;
		}
		
		if(d <= 0 || d >= 1){
			double[] error =  {-2.0, -2.0};
			return error;
		}
		
		failure_eps_1 = 0;
		failure_eps_2 = 0;
		failure_eps_3 = 0;
		failure_eps_4 = 0;
		failure_eps_5 = 0;
		failure_eps_6 = 0;
		failure_eps_7 = 0;
		failure_eps_8 = 0;
		failure_eps_9 = 0;
		failure_eps_10 = 0;
		
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
		Double esito = testFrame.get(randomNum).extractAndExecuteTestCaseSquaredError();
		Double sommatoria = esito;
		
		double diff = testFrame.get(randomNum).getLabel()-testFrame.get(randomNum).getPred();
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
		
		String tc = testFrame.get(randomNum).getName();

		ak.activeSetUpdate(name , randomNum, false, weightsMatrix);
		scompl.remove(randomNum);

		double y = 0;
		y = esito;

		double[] estimationX = new double[n];

		estimationX[0] = (testFrame.size())*y;
		//		System.out.println("[DEBUG] z0X = "+estimationX[0]);

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
				esito = testFrame.get(current_tf).extractAndExecuteTestCaseSquaredError();
				
				diff = testFrame.get(current_tf).getLabel()-testFrame.get(current_tf).getPred();
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

				ziX = sommatoria/k;
				sommatoria = sommatoria + esito;
				
					//					System.out.println("[DEBUG] qi: "+ak.qi);
				ziX = ziX + (sommatoria/(k+1))/ak.qi;
				tc = testFrame.get(current_tf).getName();
				

				ak.activeSetUpdate(name , current_tf, false, weightsMatrix);

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
				esito = testFrame.get(current_tf).extractAndExecuteTestCaseSquaredError();
				
				diff = testFrame.get(current_tf).getLabel()-testFrame.get(current_tf).getPred();
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

				ziX = sommatoria/k;
				sommatoria = sommatoria + esito;
				ak.qiCalculation(d, current_tf);
				
				ziX = ziX + (sommatoria/(k+1))/ak.qi;
				tc = testFrame.get(current_tf).getName();

				//				System.out.println("[DEBUG] qi =" + ak.qi);	
				//				System.out.println("[DEBUG] z"+k+" =" + ziX);

				ak.activeSetUpdate(name , current_tf, false, weightsMatrix);
				scompl.remove(randomNum);
				//				System.out.println("[DEBUG] scompl" + scompl);
			}

			estimationX[k] = ziX;
			k++;
		}		
		return this.estimatorBoCSP(testFrame.size(), n, estimationX);

	}
	
	private double[] estimatorBoCSP(int N, int n, double[] estimationX){
		double sumX = 0;

		for(int i=0; i<n; i++){
			sumX = sumX + estimationX[i];
		}

		double[] stima = new double[2];


		stima[0] = (1/(double)(N*n))*sumX;


		double num = 0;

		for(int i=1; i<n; i++){
			num = num + Math.pow((estimationX[i] - estimationX[0]), 2);
		}

		stima[1]= num/(double)(n*(n-1));


		return stima;
	}
}
