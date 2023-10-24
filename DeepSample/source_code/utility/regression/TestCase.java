package utility.regression;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

public class TestCase{
	private String name;
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
	
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TestCase other = (TestCase) obj;
		if (tcID == null) {
			if (other.tcID != null)
				return false;
		} else if (!tcID.equals(other.tcID))
			return false;
		return true;
	}
	
	
	private String tcID;
	private int numberOfCommands;
	private ArrayList<String> listOfCommands;
	private ArrayList<?> inputs;  
	private int maxNumberOfInputs; 
	private Path pathRootDirectory; 
	enum Outcome {ok, notOK, undefined};  
	public enum ExecutionState {executed, notExecuted};
	private boolean outcome;
	private ExecutionState executionState;
	private static String stringOk = "" ;
	private static String stringNotOk = "different results";
	private double expectedOccurrenceProbability;
	private double realOccurrenceProbability;
	private double expectedFailureLikelihood;
	private boolean outcome_class;
	private double label;
	private double SUT;
	
	
	
	
	public double getLabel() {
		return label;
	}

	public double getSUT() {
		return SUT;
	}

	public double getExpectedFailureLikelihood() {
		return expectedFailureLikelihood;
	}

	public void setExpectedFailureLikelihood(double expectedFailureLikelihood) {
		this.expectedFailureLikelihood = expectedFailureLikelihood;
	}

	public TestCase(String _name, String _tcID, Path rootDirectory){
		name = _name; 
		tcID= _tcID; 
		outcome = false;
		executionState = ExecutionState.notExecuted;
		pathRootDirectory = rootDirectory;
	};
	
	public TestCase(TestCase tc){
		name = new String(tc.getName()); 
		tcID = new String (tc.getNumber()); 
		outcome = tc.getOutcome();
		numberOfCommands = tc.getNumberOfCommands();
		listOfCommands = new ArrayList<String>(tc.getListOfCommands());
		inputs=  tc.getInputs();  //CAMBIARE
		this.maxNumberOfInputs = tc.getMaxNumberOfInputs();
		this.expectedOccurrenceProbability = tc.getExpectedOccurrenceProbability();
		this.realOccurrenceProbability= tc.getRealOccurrenceProbability();
		//ALTRI ATTRIBUTI 
	};
	
	public TestCase(String _name, String _tcID){
		name = _name; 
		tcID = _tcID; 
		outcome = false;
		executionState = ExecutionState.notExecuted;
		pathRootDirectory = Paths.get(System.getProperty("user.dir")); 
		
	};
	
	public TestCase(String _name, String _tcID, boolean _outcome_class, double _occ, double _fail_prob){
		name = _name; 
		tcID = _tcID; 
		outcome_class = _outcome_class;
		outcome = !(_outcome_class);
		expectedFailureLikelihood = _fail_prob;
		expectedOccurrenceProbability = _occ;
		realOccurrenceProbability = _occ;
		executionState = ExecutionState.notExecuted;
		pathRootDirectory = Paths.get(System.getProperty("user.dir")); 
	};
	
	public TestCase(String _name, String _tcID, double _label, double _SUT, double _occ, double _fail_prob){
		name = _name; 
		tcID = _tcID; 
		
		label = _label;
		SUT = _SUT;
		
		expectedFailureLikelihood = _fail_prob;
		expectedOccurrenceProbability = _occ;
		realOccurrenceProbability = _occ;
		executionState = ExecutionState.notExecuted;
		pathRootDirectory = Paths.get(System.getProperty("user.dir")); 
	};
	
	//FARE CLASSE ECCEZIONE test case non eseguito, il metodo sotto lancia questa eccezione
	
	public TestCase(String _id) {
		tcID = _id; 
	}

	public ExecutionState getExecutionState() {
		return executionState;
	}

	public void setExecutionState(ExecutionState executionState) {
		this.executionState = executionState;
	}

	public boolean runTestCase(String string) {
		outcome = this.getOutcome();
		executionState = ExecutionState.executed;
		return outcome; 
	}
		
	public double runTestCase_squared_error(String string) {
		executionState = ExecutionState.executed;
		return Math.pow((this.label-this.SUT),2); 
	}
	
	public double get_squared_error() {
		return Math.pow((this.label-this.SUT),2); 
	}
	

	private File createTempScript(Path directoryPath) throws IOException {
	    
		File tempScript = File.createTempFile("script", null, new File(directoryPath.toString()));
		String cmdPerm = "chmod +x "+tempScript;
		String[] cmds = {"bash","-c", cmdPerm};
		try {
			Process process2 = Runtime.getRuntime().exec(cmds);//this.listOfCommands.get(0));
			process2.waitFor();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		FileWriter fileout = new FileWriter(tempScript); 
		BufferedWriter filebuf = new BufferedWriter(fileout); 
		
		//Writer streamWriter = new OutputStreamWriter(new FileOutputStream(      tempScript));
	    PrintWriter printWriter = new PrintWriter(filebuf);

	    printWriter.println("#!/bin/bash");
	    for(int i = 0; i<this.listOfCommands.size();i++)
	    	printWriter.println(this.listOfCommands.get(i));
	   // printWriter.println("echo END COMMANDS");
	    printWriter.close();
	    return tempScript;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNumber() {
		return tcID;
	}
	public void setNumber(String number) {
		this.tcID = number;
	}
	public int getNumberOfCommands() {
		return numberOfCommands;
	}
	public void setNumberOfCommands(int numberOfCommands) {
		this.numberOfCommands = numberOfCommands;
	}
	public ArrayList<String> getListOfCommands() {
		return listOfCommands;
	}
	public void setListOfCommands(ArrayList<String> listOfCommands) {
		this.listOfCommands = listOfCommands;
	}
	
	
	public double getExpectedOccurrenceProbability() {
		return expectedOccurrenceProbability;
	}
	public void setExpectedOccurrenceProbability(
			double expectedOccurrenceProbability) {
		this.expectedOccurrenceProbability = expectedOccurrenceProbability;
	}
	public double getRealOccurrenceProbability() {
		return realOccurrenceProbability;
	}
	public void setRealOccurrenceProbability(double realOccurrenceProbability) {
		this.realOccurrenceProbability = realOccurrenceProbability;
	}
	
	public void setOutcome(boolean outcome) {
		this.outcome = outcome;
	}
	public boolean getOutcome() {
		return this.outcome;
	}
	public ArrayList<?> getInputs() {
		return inputs;
	}
	public void setInputs(ArrayList<?> inputs) {
		this.inputs = inputs;
	}
	public int getMaxNumberOfInputs() {
		return maxNumberOfInputs;
	}
	public void setMaxNumberOfInputs(int maxNumberOfInputs) {
		this.maxNumberOfInputs = maxNumberOfInputs;
	}

	public String getTcID() {
		return tcID;
	}

	public void setTcID(String tcID) {
		this.tcID = tcID;
	}
	
}