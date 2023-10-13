package fr.blind.anonymous.rbpmn.property;

import fr.blind.anonymous.rbpmn.automata.DFATask;
import fr.blind.anonymous.rbpmn.automata.State;

public class PropertyFBY {
	
	
	private DFATask dfaTask;
	
	public PropertyFBY() {
		this.dfaTask = new DFATask();
		State state1 = new State(1);
		State state2 = new State(2);
		dfaTask.setInitialState(state1);
		dfaTask.addTransition(state1, "a", state2);
		dfaTask.addTransition(state2, "b", state1);
		dfaTask.addFinalState(state1);
	}
	
	public DFATask getDFATask() {
		return this.dfaTask;
	}
}	
