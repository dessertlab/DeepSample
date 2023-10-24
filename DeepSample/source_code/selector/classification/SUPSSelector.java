package selector.classification;

import java.util.ArrayList;

import utility.TestFrame;

public class SUPSSelector {
	
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
		
		double rand = 0;
		double failProb = 0.0;
		int k=0;
		boolean outcome;

		for(int i=0; i<num; i++){
			rand = Math.random()*takenProb[takenProb.length-1];
			k=0;

			while(k < takenProb.length-1 && (rand >= takenProb[k])){
				k++;
			}

			outcome = tfa.get(k).extractAndExecuteTestCase();
			if(outcome){
				failedTC++;
				failProb += 1/((double)num * newtakenProb[k]);
			}
			
			
		}
		
		failProb = failProb/(double)tfa_in.size();
		
		double rel = 1-failProb;
		double[] ret = new double[2];
		
		ret[0] = rel;
		ret[1] = failedTC;
		
		return ret;

	}

}
