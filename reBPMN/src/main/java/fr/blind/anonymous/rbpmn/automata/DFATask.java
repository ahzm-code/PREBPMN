package fr.blind.anonymous.rbpmn.automata;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.blind.anonymous.rbpmn.bpmntask.BPMNTask;

public class DFATask{
	
	private Map<State, Map<String, State>> transitions;
	private Set<State> states;
	private State initialState;
	private Set<State> finalStates;
	private Set<String> alphabets;
	private State currentState;
	
	public DFATask() {
		transitions = new HashMap<>();
		states = new HashSet<>();
		finalStates = new HashSet<>();
		alphabets = new HashSet<>(); 
		
	}
	
	public void addTransition(State from, String on, State to) {
		
		if (!states.contains(from)) {
			states.add(from);
		}
		if (!states.contains(to)) {
			states.add(to);
		}
		
		if(!alphabets.contains(on)) {
			alphabets.add(on);
		}

		Map<String, State> map = transitions.get(from);
		if (map == null) {
			map = new HashMap<>();
			transitions.put(from, map);
		}
		map.put(on, to);
		
	}
	
	
	public void setInitialState(State initialState) {
		this.initialState = initialState;
		this.currentState = initialState;
	}
	public void addFinalState(State state) {
		this.finalStates.add(state);
	}
	
	public boolean accepts(List<BPMNTask> tasks) {
		
		
		//State currentState = initialState;
		//FIXME currentState
		
		State currentState = this.currentState;
		
		for (int i = 0; i < tasks.size(); i++) {
			
			if(!alphabets.contains(tasks.get(i).getName())) {
				continue;
			}
			
			Map<String, State> map = transitions.get(currentState);
			
			if (map == null) {
				//System.out.println("i:" + i);
				return false;
			}
			
			currentState = map.get(tasks.get(i).getName());
			
			if (currentState == null) {
				//System.out.println("i:" + i);
				//continue;
				return false;
			}
		}
		return finalStates.contains(currentState);
	}
	
	
	public boolean checkTask(BPMNTask task) {
		
		if(this.checkNotInAplabets(task)) {
			return true;
		}
		
		Map<String, State> eventMap = transitions.get(currentState);
		System.out.println(currentState.getId());
        if (eventMap == null) {
            return false;
        }
        
        State nextState = eventMap.get(task.getName());
        
        if (nextState == null) {
            return false;
        }
        
        //currentState = nextState;
        
        return true;
	}
	
	
	public boolean checkTaskandToNext(BPMNTask task) {
		
		
		if(this.checkNotInAplabets(task)) {
			return true;
		}
		
		Map<String, State> eventMap = transitions.get(currentState);
		System.out.println(currentState.getId());
        if (eventMap == null) {
            return false;
        }
        
        State nextState = eventMap.get(task.getName());
        
        if (nextState == null) {
            return false;
        }
        
        currentState = nextState;
        
        return true;
	}
	
	private boolean checkNotInAplabets(BPMNTask task) {
		
		if(!alphabets.contains(task.getName())) {
			return true;
		}
		
		return false;
	}
	

	@Override
	public String toString() {
		return "DFATask [transitions=" + transitions + ", states=" + states + ", initialState=" + initialState
				+ ", finalStates=" + finalStates + ", alphabets=" + alphabets + ", currentState=" + currentState + "]";
	}

	public State getCurrentState() {
		return currentState;
	}

	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}
}
