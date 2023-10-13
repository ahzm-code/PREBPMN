package fr.blind.anonymous.rbpmn.automata;

import java.util.Set;

import org.tensorflow.op.summary.TensorSummary;


public class PTSTransition {
	
	private String targetState;
	private String eventName;
	private double probability;
	private int counter;
	
	
	public PTSTransition(String eventName, String targetState) {
		this.eventName = eventName;
		this.targetState = targetState;
		this.probability = 0.0;
		this.counter = 0;
	}
	
	/**
	 * @param int sum: The sum value is the sum of the same starting state
	 * @return boolean: False return value when sum is zero.
	 * 
	 * */
	public boolean updateProb(int sum) {
		
		if(sum != 0) {
			this.probability = (double) this.counter / sum;
			return false;
		}
		
		return true;
	}
	
	
	public String getLabel() {
		return this.eventName;
	}
	
	public String getTargetState() {
		return this.targetState;
	}
	
	public double getProbability() {
		return this.probability;
	}
	
	public boolean equals(String compareState) {
		return this.targetState.equals(compareState);
	}
	
	public void addCounter() {
		this.counter += 1;
	}

	@Override
	public String toString() {
		return "[targetState=" + targetState + ", prob:" + probability + "]";
		//return "[targetState=" + targetState + ", counter:" + this.counter + "]";
	}

	public int getCounter() {
		return counter;
	}
	
	/**Initializing the counter*/
	public void setCounter(int newCounter) {
		this.counter = newCounter;
	}
	
	/**IInitializing the probability value*/
	public void setProbability(double newProbability) {
		this.probability = newProbability;
	}
	
}
