package selector.regression;

import java.util.ArrayList;

import utility.regression.TestFrame;

public class SUPS_histogram {
	
	public double[] weigthedOperationalTesting(ArrayList<TestFrame> tfa_in, int num){	
		ArrayList<TestFrame> tfa = new ArrayList<>(tfa_in);
		double[] takenProb = new double[tfa.size()];
		double[] newtakenProb = new double[tfa.size()];
		
		int failedTC = 0;
		
		takenProb[0] = tfa.get(0).getFailureProb();
		
		for(int i=1; i<tfa.size(); i++){
			takenProb[i] =  tfa.get(i).getFailureProb()+takenProb[i-1];
		}
		
		double norm = 0;
		for(int i=0; i<tfa.size(); i++){
			newtakenProb[i] =  tfa.get(i).getFailureProb()/takenProb[takenProb.length-1];
			norm = norm + newtakenProb[i];
		}
//		System.out.println("[DEBUG] "+takenProb[takenProb.length-1]);
//		System.out.println("[DEBUG] Normalization = "+norm);
		
		double rand = 0;
		double sommatoria = 0.0;
		int k=0;
		boolean outcome;
		double diff;
		int failure_eps_1 = 0;
		int failure_eps_2 = 0;
		int failure_eps_3 = 0;
		int failure_eps_4 = 0;
		int failure_eps_5 = 0;
		int failure_eps_6 = 0;
		int failure_eps_7 = 0;
		int failure_eps_8 = 0;
		int failure_eps_9 = 0;
		int failure_eps_10 = 0;

		for(int i=0; i<num; i++){
			rand = Math.random()*takenProb[takenProb.length-1];
			k=0;

			while(k < takenProb.length-1 && (rand >= takenProb[k])){
				k++;
			}
			
//			System.out.println(k+") "+tfa.get(k).getFailureProb());

			sommatoria += tfa.get(k).extractAndExecuteTestCaseSquaredError()/newtakenProb[k];
			
			
			diff = tfa.get(k).getLabel()-tfa.get(k).getPred();
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
			
			
		}
		
//		System.out.println(takenProb.length);
//		System.out.println(tfa.size());
//		System.out.println(tfa_in.size());

		double mse = sommatoria/(num*tfa_in.size());
		double[] ret = new double[11];
		
		ret[0] = mse;
		ret[1] = (double)failure_eps_1;
		ret[2] = (double)failure_eps_2;
		ret[3] = (double)failure_eps_3;
		ret[4] = (double)failure_eps_4;
		ret[5] = (double)failure_eps_5;
		ret[6] = (double)failure_eps_6;
		ret[7] = (double)failure_eps_7;
		ret[8] = (double)failure_eps_8;
		ret[9] = (double)failure_eps_9;
		ret[10] = (double)failure_eps_10;
		
		return ret;

	}

}