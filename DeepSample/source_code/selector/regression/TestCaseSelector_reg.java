package selector.regression;

import java.util.ArrayList;

import utility.regression.TestCase;


public abstract class TestCaseSelector_reg {

	public ArrayList<TestCase> potentialTestSuite;
	public ArrayList<ArrayList<TestCase>> partitions;
	public int numberOfPartitions;
	public int initialNumberOfTestCases;
	public int budget;
	public int numberOfFaults;
	
	public TestCaseSelector_reg(ArrayList<TestCase> _potentialTestSuite, ArrayList<ArrayList<TestCase>> _partitions, int _nPartitions, int _initialNumberOfTestCases, int _budget, int _numberOfFaults){
		
		potentialTestSuite =_potentialTestSuite; 
		partitions = _partitions;
		numberOfPartitions = _nPartitions;
		initialNumberOfTestCases = _initialNumberOfTestCases;
		budget = _budget;
		numberOfFaults = _numberOfFaults;
		if(budget>potentialTestSuite.size()){
			System.out.println("ERROR: BUDGET CANNOT BE GRETAER THAN THE NUMBER OF INPUTS");
			System.exit(0);  //SOSTITUIRE CON ECCEZIONI
		}
		if(initialNumberOfTestCases>budget){
			System.out.println("ERROR: INITIAL NUMBER OF TEST CASES CANNOT BE GREATER THAN THE BUDGET");
			System.exit(0);  //SOSTITUIRE CON ECCEZIONI
		}
		
		
	};
	
	public TestCaseSelector_reg(ArrayList<TestCase> _potentialTestSuite, ArrayList<ArrayList<TestCase>> _partitions, int _nPartitions, int _initialNumberOfTestCases, int _budget) {
		potentialTestSuite =_potentialTestSuite; 
		partitions = _partitions;
		numberOfPartitions = _nPartitions;
		initialNumberOfTestCases = _initialNumberOfTestCases;
		budget = _budget;
		if(budget>potentialTestSuite.size()){
			System.out.println("ERROR: BUDGET CANNOT BE GRETAER THAN THE NUMBER OF INPUTS");
			System.exit(0);  //SOSTITUIRE CON ECCEZIONI
		}
		if(initialNumberOfTestCases>budget){
			System.out.println("ERROR: INITIAL NUMBER OF TEST CASES CANNOT BE GREATER THAN THE BUDGET");
			System.exit(0);  //SOSTITUIRE CON ECCEZIONI
		}
	}

	public TestCaseSelector_reg(ArrayList<TestCase> _potentialTestSuite, int _initialNumberOfTestCases, int _budget) {
		potentialTestSuite =_potentialTestSuite; 
		initialNumberOfTestCases = _initialNumberOfTestCases;
		budget = _budget;
		if(budget>potentialTestSuite.size()){
			System.out.println("ERROR: BUDGET CANNOT BE GRETAER THAN THE NUMBER OF INPUTS");
			System.exit(0);  //SOSTITUIRE CON ECCEZIONI
		}
		if(initialNumberOfTestCases>budget){
			System.out.println("ERROR: INITIAL NUMBER OF TEST CASES CANNOT BE GREATER THAN THE BUDGET");
			System.exit(0);  //SOSTITUIRE CON ECCEZIONI
		}
		
	}

	abstract void selectAndRunTestCase();
	
}
