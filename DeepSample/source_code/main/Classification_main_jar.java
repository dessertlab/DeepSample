package main;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import selector.classification.GBSSelector;
import selector.classification.TwoUPSSelector;
import selector.classification.PartitionInfo;
import selector.classification.RHCSSelector;
import selector.classification.StratifiedRandomSelectorWOR;
import utility.TestCase;
import utility.TestSuiteFileLoader;

public class Classification_main_jar {

	public static void main(String[] args) {
		String model = args[0];
		String path = "./dataset/"+model+".csv";
		int key = Integer.parseInt(args[1]);
		int size = Integer.parseInt(args[2]);
		
		String aux;
		if(key==3) {
			aux = "confidence";
		} else if(key==4) {
			aux="dsa";
		} else {
			aux ="lsa";
		}

		/****INITIALIZATION ****/
		
		int NPartitionsSimulation = 10;
		TestSuiteFileLoader tsfl = new TestSuiteFileLoader();
		tsfl.loadTestSuiteSimulation_class(path, key, size);

		ArrayList<PartitionInfo> partitionsStructure = null;
		ArrayList<TestCase> completeTestSuite = new ArrayList<TestCase>();
		ArrayList<TestCase> completeTestSuite_copy;

		try { 
			partitionsStructure = tsfl.getPartitionsSimulation_kNN(NPartitionsSimulation, key, path); 
		} catch (Exception e) {e.printStackTrace();}

		ArrayList<ArrayList<TestCase>> partitions = new ArrayList<ArrayList<TestCase>>(); 
		for (int i=0; i< partitionsStructure.size();i++){
			partitions.add(partitionsStructure.get(i).getListOfTests());
			completeTestSuite.addAll(partitionsStructure.get(i).getListOfTests());
		}


		int budget = 25;
		int rep = 30;

		for(int i=0; i<5; i++) {
			budget=budget*2;
			System.out.println("SSRS: budget " + budget);
				try {
					FileWriter writer = new FileWriter("./Results/Classification/SSRS/"+model+"_"+aux+"_"+budget+".txt");

					writer.write("accuracy,failures\n");

					for(int j=0; j<rep; j++) {
						StratifiedRandomSelectorWOR srs = new StratifiedRandomSelectorWOR(completeTestSuite, partitions, NPartitionsSimulation, 0, budget , 0);
						srs.selectAndRunTestCase();
						System.out.println(""+srs.REstimate+"	"+srs.numberOfDetectedFailurePoints);
						writer.write(""+srs.REstimate+","+srs.numberOfDetectedFailurePoints+"\n");
					}
					

					writer.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			System.out.println("2-UPS: budget " + budget);
			
			for(int j=0; j<rep; j++) {
				TwoUPSSelector opt = new TwoUPSSelector(completeTestSuite, partitions, NPartitionsSimulation, 0, budget);
				opt.selectAndRunTestCase();
			}
			
			try {
				FileWriter writer = new FileWriter("./Results/Classification/2-UPS/"+model+"_"+aux+"_"+budget+".txt");

				writer.write("accuracy,failures\n");

				for(int j=0; j<rep; j++) {
					TwoUPSSelector opt = new TwoUPSSelector(completeTestSuite, partitions, NPartitionsSimulation, 0, budget);
					opt.selectAndRunTestCase();
					System.out.println(""+opt.REstimate+"	"+opt.numberOfDetectedFailurePoints);
					writer.write(""+opt.REstimate+","+opt.numberOfDetectedFailurePoints+"\n");
				}
				

				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("GBS: budget " + budget);
			try {
				FileWriter writer = new FileWriter("./Results/Classification/GBS/"+model+"_"+aux+"_"+budget+".txt");

				writer.write("accuracy,failures\n");

				for(int j=0; j<rep; j++) {
					GBSSelector ats = new GBSSelector(completeTestSuite, partitions, NPartitionsSimulation, 0, budget);
					ats.selectAndRunTestCase();
					System.out.println(""+ats.REstimate+"	"+ats.numberOfDetectedFailurePoints);
					writer.write(""+ats.REstimate+"	"+ats.numberOfDetectedFailurePoints+"\n");
				}
				

				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			
			System.out.println("RHC-S: budget " + budget);
			try {
				FileWriter writer = new FileWriter("./Results/Classification/RHC-S/"+model+"_"+aux+"_"+budget+".txt");

				writer.write("accuracy,failures\n");

				for(int j=0; j<rep; j++) {
					completeTestSuite_copy = new ArrayList<TestCase>(completeTestSuite);
					RHCSSelector rdts = new RHCSSelector(completeTestSuite_copy, budget, budget);
					rdts.selectAndRunTestCase(95);
					System.out.println(""+rdts.estimate+"	"+rdts.numberOffailedTestCases);
					writer.write(""+rdts.estimate+","+rdts.numberOffailedTestCases+"\n");
				}
				

				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

			
		}

		
	}

}
