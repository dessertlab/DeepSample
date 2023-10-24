package utility.regression;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class InitializerTFReg {
	private String csvFile;
	
	public InitializerTFReg(String path) {
		csvFile = path;
	}

    public ArrayList<TestFrame> readTestFrames(int key, int size) {
        BufferedReader br = null;
        String line = "";
        ArrayList<TestFrame> aux = new ArrayList<TestFrame>();

        try {

            br = new BufferedReader(new FileReader(csvFile));
            line = br.readLine();
            String[] parser = line.split(",");
            System.out.println(parser[key]);
            double label;
            double SUT;
            double val = 0;
            int i = 0;
            double occ = 1/(double)size;

            while ((line = br.readLine()) != null) {
            	i++;
                // use comma as separator
                parser = line.split(",");
                label = Double.parseDouble(parser[1]);
                SUT = Double.parseDouble(parser[2]);
            	
            	val = Double.parseDouble(parser[key]);
            	
                
                aux.add(new TestFrame(""+aux.size(), ""+aux.size(), val, occ, label, SUT));
                
            }
            
            if(i!=size)
            	System.out.println("[WARNING] The size is lower/greater!!!");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
		return aux;

    }
    
    public double[][] weightedMatrixComputation_threshold(ArrayList<TestFrame> tf, double threshold){
    	double[][] wm = new double[tf.size()][tf.size()];
    	double conf_j;
    	
    	
    	for(int i=0; i<tf.size(); i++) {
    		for(int j=0; j<tf.size(); j++) {
    			conf_j=tf.get(j).getFailureProb();
    			if(conf_j > threshold){
    				wm[i][j] = conf_j;
    			} else {
    				wm[i][j] = 0;
    			}
    		}
    	}
    	
    	return wm;
    }
}
