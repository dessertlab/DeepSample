package main;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import utility.regression.TestFrame;
import utility.regression.InitializerTFReg;
import selector.regression.SUPS_histogram;

public class SUPS_reg_jar {

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
		
		SUPS_histogram wo = new SUPS_histogram();
		
		for(int j=0; j<5; j++) {
			budget = budget*2;

			double[] res = new double[11];

			try {
				FileWriter writer = new FileWriter(args[4]+"_"+budget+".csv");

				System.out.println("MSE,eps1,eps2,eps3,eps4,eps5,eps6,eps7,eps8,eps9,eps10");
				writer.write("MSE,eps1,eps2,eps3,eps4,eps5,eps6,eps7,eps8,eps9,eps10\n");
				for(int i=0; i<rep; i++) {
					res = wo.weigthedOperationalTesting(tf, budget);
					System.out.println(""+res[0]+"	"+(int)res[1]+"	"+(int)res[2]+"	"+(int)res[3]+"	"+(int)res[4]+"	"+(int)res[5]+"	"+(int)res[6]+"	"+(int)res[7]+"	"+(int)res[8]+"	"+(int)res[9]+"	"+(int)res[10]);
					writer.write(""+res[0]+","+(int)res[1]+","+(int)res[2]+","+(int)res[3]+","+(int)res[4]+","+(int)res[5]+","+(int)res[6]+","+(int)res[7]+","+(int)res[8]+","+(int)res[9]+","+(int)res[10]+"\n");
				}

				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
