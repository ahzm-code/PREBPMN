package fr.blind.anonymous.rbpmn.automata;


public class ProbTransition {
	
	private State fromState;
	private State toState;
	private Inequality probConstraint;
	private double probability;
	
	public ProbTransition(State fromState, Inequality probInequality, State toState) {
		
		this.fromState = fromState;
		this.toState = toState;
		this.probConstraint = probInequality;
		this.probability = probInequality.getRightOperand();
	}

	public State getFromState() {
		return fromState;
	}

	public void setFromState(State fromState) {
		this.fromState = fromState;
	}

	public State getToState() {
		return toState;
	}

	public void setToState(State toState) {
		this.toState = toState;
	}

	public Inequality getProbConstraint() {
		return probConstraint;
	}

	public void setProbConstraint(Inequality probConstraint) {
		this.probConstraint = probConstraint;
	}

	@Override
	public String toString() {
		return "ProbTransition [fromState=" + fromState + ", toState=" + toState + ", probConstraint=" + probConstraint
				+ "]";
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}
	
	
}
