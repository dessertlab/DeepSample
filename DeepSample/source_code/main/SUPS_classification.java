package main;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

import utility.TestFrame;
import utility.InitializerTF;
import selector.classification.SUPSSelector;

public class SUPS_classification {

	public static void main(String[] args) {

		String model = args[0];
		String dataset =args[1];
		int budget = Integer.parseInt(args[2]); 
		int key = Integer.parseInt(args[3]); 
		int size = Integer.parseInt(args[4]); 
		
		String path = "dataset/"+model+".csv"; 
		// TODO Auto-generated method stub
		InitializerTF csvR = new InitializerTF(path);
		//-Xmx51200m ABC
		//-Xmx12288m DEF
		
		int rep = 30;		
		
		
		ArrayList<TestFrame> tf = csvR.readTestFrames(key, size);
		
		/********SUPS*********/
		SUPSSelector wo = new SUPSSelector();
		double[] ret;
		double[] rel = new double[rep];
		double[] fp = new double[rep];
		
		
		for(int i = 0; i < rep; i++) {
			ret = wo.weigthedOperationalTesting(tf, budget);
			rel[i] = ret[0];
			fp[i] = ret[1];
		}

		try {
			FileWriter writer = new FileWriter("Results/Classification/SUPS/"+model+"_"+budget+".txt");
			
			writer.write("accuracy,failures\n");
			
			for(int i=0; i<rep; i++) {
				writer.write(rel[i]+","+fp[i]+"\n");
				System.out.println(rel[i]+","+fp[i]);
			}
			
		    writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

}
