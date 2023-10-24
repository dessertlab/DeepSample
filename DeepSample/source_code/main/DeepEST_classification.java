package main;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import utility.TestFrame;
import utility.InitializerTF;
import selector.classification.DeepESTSelector;

public class DeepEST_classification {

	public static void main(String[] args) {
		String help = "Please specify:\n"
				+ "	- dataset path\n"
				+ "	- auxiliary variable: confidence, lsa, dsa, combo\n"
				+ "	- threshold\n"
				+ "	- budget\n"
				+ "	- dataset size\n"
				+ "	- results path";
		
		if (args.length < 4) {
			System.out.println(help);
			return; 
		}

		InitializerTF csvR = new InitializerTF(args[0]);
		String key_s = args[1];
		Double threshold = Double.parseDouble(args[2]);
		int budget = 25;
		
		int key = 4;
		if(key_s.equalsIgnoreCase("confidence")) {
			key = 3;
			threshold = 1-threshold;
		} else if(key_s.equalsIgnoreCase("dsa")) {
			key = 4;
		} else if(key_s.equalsIgnoreCase("lsa")) {
			key = 5;
		} else {
			key = 6;
			threshold = 1-threshold;
		}
		
		System.out.println("Approach execution on "+args[0]+" with auxiliary variable "+args[1]+" and budget "+args[3]);
		
		int rep = 30;
		
		ArrayList<TestFrame> tf = csvR.readTestFrames(key, Integer.parseInt(args[4]));
		
		DeepESTSelector aws= new DeepESTSelector();
		double[][] weightsMatrix = csvR.weightedMatrixComputation_threshold(tf, key, threshold);
		
		for(int j=0; j<5; j++) {
			budget = budget*2;

			double[] rel; 
			double[] rel_arr = new double[rep];
			int[] num_fp = new int[rep];

			for(int i=0; i<rep; i++) {
				rel = aws.selectAndRunTestCase(budget, tf, weightsMatrix, 0.8);
				rel_arr[i] = 1-rel[0];
				num_fp[i] = aws.getnumfp();
			}

			try {
				FileWriter writer = new FileWriter(args[5]+"_"+budget+".csv");

				writer.write("accuracy,failures\n");

				for(int i=0; i<rep; i++) {
					writer.write(rel_arr[i]+","+num_fp[i]+"\n");
					System.out.println(rel_arr[i]+","+num_fp[i]);
				}

				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
