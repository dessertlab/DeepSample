package utility;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class InitializerTF {
	
	private String csvFile;
	
	public InitializerTF(String path) {
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
            double val = 0;
            String outcome;
            boolean fail=true;
            
            //per diminuire il costo assumo di riceverlo in input
            double occ = 1/(double)size;
            int i = 0;
            
            while ((line = br.readLine()) != null) {
            	i++;
                // use comma as separator
                parser = line.split(",");
                outcome = parser[1];
            	if(outcome.equals("Pass")) {
            		fail=false;
            	} else {
            		fail=true;
            	}
            	
            	val = Double.parseDouble(parser[key]);
            	
                if(key==3||key==6) {
                	val = 1-val;
//                	inserisco un epsilon alla confidenza.
//                	if(val == 0) {
//                		val = 0.000000001;
//                	}
                	
                	aux.add(new TestFrame(""+aux.size(), ""+aux.size(), val, occ, parser[2], fail));
                } else {
                	aux.add(new TestFrame(""+aux.size(), ""+aux.size(), val, occ, parser[2], fail));
                }
            }
            
            if(i!=size)
            	System.out.println("[WARNING] The size is lower/greater!!! "+i);

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

    
    public double[][] weightedMatrixComputation(ArrayList<TestFrame> tf, int key){
    	double[][] wm = new double[tf.size()][tf.size()];
    	double conf_i, conf_j;
    	double threshold;
    	
    	
    	if(key == 3) {
    		threshold = 0.3;
    	} else if(key == 4) {
    		//threshold = 1.557+2*0.224;//CIFAR10VGG 1.337+0.398;//MNIST LeNet5 0.387+2*0.185; //CIFAR100: 1.557+2*0.224;//CIFAR10: 0.93+2*0.322;	//MNIST: 0.371+2*0.168;
    		threshold = 0.371+2*0.168; //CIFAR10VGG 1.337+2*0.398 //CIFAR100 1.557+2*0.473
    	} else {
    		//threshold = 104.55+2.71;//CIFAR10VGG 1685.297+36.97;//MNIST LENet5 998.34+35.78; //CIFAR100: 104.55+2.71;//CIFAR10: 451.95+8.25;	//MNIST: 30141+320;
    		threshold = 30141+102.43; //MNIST 30141+102.43; //CIFAR10VGG 1685.297+ 1367.10 //CIFAR100 104.55 + 7.348
    	}
    	
    	
    	for(int i=0; i<tf.size(); i++) {
    		conf_i=tf.get(i).getFailureProb();
    		for(int j=0; j<tf.size(); j++) {
    			conf_j=tf.get(j).getFailureProb();
    			//if(tf.get(i).getOutput().equals(tf.get(j).getOutput()) && (conf_i-conf_j)>min && (conf_i-conf_j)<max){
    			if(conf_j > threshold){
    				wm[i][j] = conf_j;
    			} else {
    				wm[i][j] = 0;
    			}
    		}
    	}
    	
    	return wm;
    }
    
    public double[][] weightedMatrixComputation_threshold(ArrayList<TestFrame> tf, int key, double threshold){
    	double[][] wm = new double[tf.size()][tf.size()];
    	double conf_j;
    	
    	
    	for(int i=0; i<tf.size(); i++) {
    		for(int j=0; j<tf.size(); j++) {
    			conf_j=tf.get(j).getFailureProb();
    			//if(tf.get(i).getOutput().equals(tf.get(j).getOutput()) && (conf_i-conf_j)>min && (conf_i-conf_j)<max){
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