package main;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import selector.regression.GBSSelector;
import selector.regression.TwoUPSSelector;
import selector.regression.PartitionInfo;
import selector.regression.RHCSSelector;
import selector.regression.StratifiedRandomSelectorWOR;
import utility.regression.TestCase;
import utility.regression.TestSuiteFileLoader;

public class Regression_main_jar {

	public static void main(String[] args) {
		String model = args[0];
		String path = "./dataset/"+model+".csv";
		int key = Integer.parseInt(args[1]);
		int size = Integer.parseInt(args[2]);
		
		String aux;
		if(key==3) {
			aux = "VAE";
		} else if(key==4) {
			aux="SAE";
		} else {
			aux ="LSA";
		}

		/****INITIALIZATION ****/

		int NPartitionsSimulation = 10;
		TestSuiteFileLoader tsfl = new TestSuiteFileLoader();
		tsfl.loadTestSuiteSimulation_reg(path, key, size);

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
					FileWriter writer = new FileWriter("./Results/Regression/SSRS/"+model+"_"+aux+"_"+budget+".txt");

					writer.write("mse,ep1,eps2,eps3,eps4,eps5,eps6,eps7,eps8,eps9,eps10\n");

					for(int j=0; j<rep; j++) {
						StratifiedRandomSelectorWOR srs = new StratifiedRandomSelectorWOR(completeTestSuite, partitions, NPartitionsSimulation, 0, budget , 0);
						srs.selectAndRunTestCase();
						System.out.println(""+srs.REstimate+"	"+srs.getFailure_eps_1()+"	"+srs.getFailure_eps_2()+"	"+srs.getFailure_eps_3()+"	"+srs.getFailure_eps_4()+"	"+srs.getFailure_eps_5()+"	"+srs.getFailure_eps_6()+"	"+srs.getFailure_eps_7()+"	"+srs.getFailure_eps_8()+"	"+srs.getFailure_eps_9()+"	"+srs.getFailure_eps_10());
						writer.write(""+srs.REstimate+","+srs.getFailure_eps_1()+","+srs.getFailure_eps_2()+","+srs.getFailure_eps_3()+","+srs.getFailure_eps_4()+","+srs.getFailure_eps_5()+","+srs.getFailure_eps_6()+","+srs.getFailure_eps_7()+","+srs.getFailure_eps_8()+","+srs.getFailure_eps_9()+","+srs.getFailure_eps_10()+"\n");
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
				FileWriter writer = new FileWriter("./Results/Regression/2-UPS/"+model+"_"+aux+"_"+budget+".txt");

				writer.write("mse,ep1,eps2,eps3,eps4,eps5,eps6,eps7,eps8,eps9,eps10\n");

				for(int j=0; j<rep; j++) {
					TwoUPSSelector opt = new TwoUPSSelector(completeTestSuite, partitions, NPartitionsSimulation, 0, budget);
					opt.selectAndRunTestCase();
					System.out.println(""+opt.REstimate+"	"+opt.getFailure_eps_1()+"	"+opt.getFailure_eps_2()+"	"+opt.getFailure_eps_3()+"	"+opt.getFailure_eps_4()+"	"+opt.getFailure_eps_5()+"	"+opt.getFailure_eps_6()+"	"+opt.getFailure_eps_7()+"	"+opt.getFailure_eps_8()+"	"+opt.getFailure_eps_9()+"	"+opt.getFailure_eps_10());
					writer.write(""+opt.REstimate+","+opt.getFailure_eps_1()+","+opt.getFailure_eps_2()+","+opt.getFailure_eps_3()+","+opt.getFailure_eps_4()+","+opt.getFailure_eps_5()+","+opt.getFailure_eps_6()+","+opt.getFailure_eps_7()+","+opt.getFailure_eps_8()+","+opt.getFailure_eps_9()+","+opt.getFailure_eps_10()+"\n");
				}
				

				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("GBS: budget " + budget);
			try {
				FileWriter writer = new FileWriter("./Results/Regression/GBS/"+model+"_"+aux+"_"+budget+".txt");

				writer.write("mse,ep1,eps2,eps3,eps4,eps5,eps6,eps7,eps8,eps9,eps10\n");

				for(int j=0; j<rep; j++) {
					GBSSelector ats = new GBSSelector(completeTestSuite, partitions, NPartitionsSimulation, 0, budget);
					ats.selectAndRunTestCase();
					System.out.println(""+ats.REstimate+"	"+ats.getFailure_eps_1()+"	"+ats.getFailure_eps_2()+"	"+ats.getFailure_eps_3()+"	"+ats.getFailure_eps_4()+"	"+ats.getFailure_eps_5()+"	"+ats.getFailure_eps_6()+"	"+ats.getFailure_eps_7()+"	"+ats.getFailure_eps_8()+"	"+ats.getFailure_eps_9()+"	"+ats.getFailure_eps_10());
					writer.write(""+ats.REstimate+","+ats.getFailure_eps_1()+","+ats.getFailure_eps_2()+","+ats.getFailure_eps_3()+","+ats.getFailure_eps_4()+","+ats.getFailure_eps_5()+","+ats.getFailure_eps_6()+","+ats.getFailure_eps_7()+","+ats.getFailure_eps_8()+","+ats.getFailure_eps_9()+","+ats.getFailure_eps_10()+"\n");
				}
				

				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			
			System.out.println("RHC-S: budget " + budget);
			try {
				FileWriter writer = new FileWriter("./Results/Regression/RHC-S/"+model+"_"+aux+"_"+budget+".txt");

				writer.write("mse,ep1,eps2,eps3,eps4,eps5,eps6,eps7,eps8,eps9,eps10\n");

				for(int j=0; j<rep; j++) {
					completeTestSuite_copy = new ArrayList<TestCase>(completeTestSuite);
					RHCSSelector rdts = new RHCSSelector(completeTestSuite_copy, budget, budget);
					rdts.selectAndRunTestCase(95);
					System.out.println(""+rdts.estimate+"	"+rdts.getFailure_eps_1()+"	"+rdts.getFailure_eps_2()+"	"+rdts.getFailure_eps_3()+"	"+rdts.getFailure_eps_4()+"	"+rdts.getFailure_eps_5()+"	"+rdts.getFailure_eps_6()+"	"+rdts.getFailure_eps_7()+"	"+rdts.getFailure_eps_8()+"	"+rdts.getFailure_eps_9()+"	"+rdts.getFailure_eps_10());
					writer.write(""+rdts.estimate+","+rdts.getFailure_eps_1()+","+rdts.getFailure_eps_2()+","+rdts.getFailure_eps_3()+","+rdts.getFailure_eps_4()+","+rdts.getFailure_eps_5()+","+rdts.getFailure_eps_6()+","+rdts.getFailure_eps_7()+","+rdts.getFailure_eps_8()+","+rdts.getFailure_eps_9()+","+rdts.getFailure_eps_10()+"\n");
				}
				

				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

			
		}

		
	}

}
