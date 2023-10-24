package main;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import utility.regression.TestFrame;
import utility.regression.InitializerTFReg;
import selector.regression.DeepESTRegressionSelector_histogram;

public class DeepEST_reg_jar {

	public static void main(String[] args) {
		String help = "Please specify:\n"
				+ "	- dataset path\n"
				+ "	- auxiliary variable: VAE, SAE, lsa\n"
				+ "	- budget\n"
				+ "	- dataset size\n"
				+ "	- results path";
		
		if (args.length < 4) {
			System.out.println(help);
			return; 
		}

		InitializerTFReg csvR = new InitializerTFReg(args[0]);
		String key_s = args[1];
		int budget = 25;
		
		int key = 3;
		if(key_s.equalsIgnoreCase("VAE")) {
			key = 3;
		} else if(key_s.equalsIgnoreCase("SAE")) {
			key = 4;
		} else if(key_s.equalsIgnoreCase("lsa")) {
			key = 5;
		}
		
		System.out.println("Approach execution on "+args[0]+" with auxiliary variable "+args[1]+" and budget "+args[3]);
		
		int rep = 30;
		
		ArrayList<TestFrame> tf = csvR.readTestFrames(key, Integer.parseInt(args[3]));
		
//		System.out.println(tf.get(0).getFailureProb());
		
		DeepESTRegressionSelector_histogram aws= new DeepESTRegressionSelector_histogram();
		double[][] weightsMatrix = csvR.weightedMatrixComputation_threshold(tf, 0);
		
		for(int j=0; j<5; j++) {
			budget = budget*2;

			double[] rel_arr = new double[rep];

			try {
				FileWriter writer = new FileWriter(args[4]+"_"+budget+".csv");

				System.out.println("MSE,eps1,eps2,eps3,eps4,eps5,eps6,eps7,eps8,eps9,eps10");
				writer.write("MSE,eps1,eps2,eps3,eps4,eps5,eps6,eps7,eps8,eps9,eps10\n");

				for(int i=0; i<rep; i++) {
					rel_arr[i] = aws.selectAndRunTestCase(budget, tf, weightsMatrix, 0.8)[0];
					System.out.println(""+rel_arr[i]+"	"+aws.getFailure_eps_1()+"	"+aws.getFailure_eps_2()+"	"+aws.getFailure_eps_3()+"	"+aws.getFailure_eps_4()+"	"+aws.getFailure_eps_5()+"	"+aws.getFailure_eps_6()+"	"+aws.getFailure_eps_7()+"	"+aws.getFailure_eps_8()+"	"+aws.getFailure_eps_9()+"	"+aws.getFailure_eps_10());
					writer.write(""+rel_arr[i]+","+aws.getFailure_eps_1()+","+aws.getFailure_eps_2()+","+aws.getFailure_eps_3()+","+aws.getFailure_eps_4()+","+aws.getFailure_eps_5()+","+aws.getFailure_eps_6()+","+aws.getFailure_eps_7()+","+aws.getFailure_eps_8()+","+aws.getFailure_eps_9()+","+aws.getFailure_eps_10()+"\n");
				}
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
