package utility.regression;




public class TestFrame{

	private String name;
	private String tfID;
	private double failureProb;
	private double occurrenceProb;
	private String output;
	private boolean fail;
	
	private double label;
	private double pred;

	public TestFrame(String _name, String _tfID, double _failureProb, double _occurrenceProb, String _output, boolean _fail) {
		super();
		this.name = _name;
		this.tfID = _tfID;
		this.failureProb = _failureProb;
		this.occurrenceProb = _occurrenceProb;
		this.output = _output;
		this.fail = _fail;
	}
	
	public TestFrame(String _name, String _tfID, double _failureProb, double _occurrenceProb, double _label, double _pred) {
		super();
		this.name = _name;
		this.tfID = _tfID;
		this.failureProb = _failureProb;
		this.occurrenceProb = _occurrenceProb;
		this.label = _label;
		this.pred = _pred;
	}


	public boolean extractAndExecuteTestCase(){
		return this.fail;
	}
	
	public double extractAndExecuteTestCaseSquaredError(){
		double est = Math.pow((label - pred),2);
		return est;
	}

	public String getOutput() {
		return output;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTfID() {
		return tfID;
	}

	public void setTfID(String tfID) {
		this.tfID = tfID;
	}

	public double getFailureProb() {
		return failureProb;
	}

	public void setFailureProb(double failureProb) {
		this.failureProb = failureProb;
	}

	public double getOccurrenceProb() {
		return occurrenceProb;
	}

	public void setOccurrenceProb(double occurrenceProb) {
		this.occurrenceProb = occurrenceProb;
	}

	public double getLabel() {
		return label;
	}

	public double getPred() {
		return pred;
	}

}
