package fr.blind.anonymous.rbpmn.automata;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ProbabilisticAutomata {

	private Set<State> states;
	private State currentState;
	private Map<State, Map<Inequality, State>> transitions;

	private State initialState;
	private Set<State> finalStates;
	private double currentProb;

	public ProbabilisticAutomata(State initialState) {
		
		transitions = new HashMap<>();
		states = new HashSet<>();
		finalStates = new HashSet<>();
		currentState = initialState;
	}
	

	public void addTransitions(ProbTransition transition) {

		if (!states.contains(transition.getFromState())) {
			states.add(transition.getFromState());
		}
		if (!states.contains(transition.getToState())) {
			states.add(transition.getToState());
		}
		if (!transitions.containsKey(transition.getFromState())) {
			transitions.put(transition.getFromState(), new HashMap<>());
		}
		
		transitions.get(transition.getFromState()).put(transition.getProbConstraint(), transition.getToState());
	}
	
	
	public State getCurrentState() {
        return currentState;
    }

	public boolean checkProb(double probValue) {
		
		Map<Inequality, State> nextStates = transitions.get(currentState);
		
		this.currentProb = probValue;
		
		if (nextStates == null) {
            return false;
        }
		
		
		for(Inequality key : nextStates.keySet()){
			
			key.getLeftOperand().setValue(probValue);
			
			if(key.evaluate()) {
				
				
				System.out.println("prob:" + probValue +", probConstraint:" + key + ", nextState->" + nextStates.get(key));
				
				currentState = nextStates.get(key);
				System.out.println(currentState);
				return true;
			}
		}
		
		return false;
	}


	@Override
	public String toString() {
		return "PA [currentState=" + currentState + ", transitions="
				+ transitions + ", currentProb="
				+ currentProb + "]";
	}

}
