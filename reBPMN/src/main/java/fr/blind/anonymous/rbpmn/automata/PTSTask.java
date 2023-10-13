package fr.blind.anonymous.rbpmn.automata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.StringJoiner;

import fr.blind.anonymous.rbpmn.bpmntask.BPMNTask;

/**
 * @author ahzm
 * @date: Feb 03, 2023 update: March 16, 2023
 * 
 * Class: PTSTask 
 * Description: Used to describe LTS and PTS.
 */

public class PTSTask {
	
	/**All states of LTS/PTS*/
	private Set<String> states;
	
	/**All transitions of LTS/PTS*/
	private Map<String, Map<String, Set<PTSTransition>>> transitions;
	
	/** Initial state */
	private String initState;
	
	/** End states (There may be multiple end states).*/
	private Set<String> finalStates;
	
	/**Names of all events/tasks */
	private Set<String> alphabets;
	
	/** Record the execution path of completed instances */
	private List<TransitionPath> completePath;
	
	/** Record all execution paths for unfinished instances.
	 * The LTS is non-deterministic due to the presence of an INCLUSIVE gateway. */
	private List<List<TransitionPath>> incompletePaths;

	// private Set<Integer> currentStates;
	
	/**Names of all key tasks form the given property*/
	private Set<String> keyTasks;
	
	/**property transition paths form the given property*/
	private List<List<TransitionPath>> propertyPaths;
	
	/** Initialization */
	public PTSTask() {
		transitions = new HashMap<>();
		states = new HashSet<>();
		finalStates = new HashSet<>();
		alphabets = new HashSet<>();
		completePath = new ArrayList<>();
		incompletePaths = new ArrayList<>();
		keyTasks  = new HashSet<>();
		propertyPaths = new ArrayList<>();
	}
	
	/** Adding a transition (s1 -> a ->s2)*/
	public void addTransition(String sourceState, String eventName, String toState) {

		if (!states.contains(sourceState)) {
			states.add(sourceState);
		}
		if (!states.contains(toState)) {
			states.add(toState);
		}

		if (!alphabets.contains(eventName)) {
			alphabets.add(eventName);
		}

		Map<String, Set<PTSTransition>> stateTransitions = transitions.get(sourceState);

		if (stateTransitions == null) {
			stateTransitions = new HashMap<>();
			transitions.put(sourceState, stateTransitions);
		}

		Set<PTSTransition> toStates = stateTransitions.get(eventName);

		if (toStates == null) {
			toStates = new HashSet<>();
			stateTransitions.put(eventName, toStates);
		}

		toStates.add(new PTSTransition(eventName, toState));
	}
	
	/** Check if the PTS matches the task list for the given instance */
	public boolean matches(List<BPMNTask> tasks) {

		// TODO UnitTest
		Set<String> currentStates = new HashSet<>();
		Set<String> nextStates = new HashSet<>();

		currentStates.add(this.initState);

		for (int i = 0; i < tasks.size(); i++) {

			BPMNTask task = tasks.get(i);

			for (String state : currentStates) {

				if (this.getNextSates(state, task.getName()) != null) {
					for (PTSTransition ongoingTransition : this.getNextSates(state, task.getName())) {
						nextStates.add(ongoingTransition.getTargetState());
					}
				}
			}

			currentStates = nextStates;
			nextStates = new HashSet<>();
		}

		for (String state : currentStates) {
			if (this.finalStates.contains(state)) {
				return true;
			}
		}

		return false;
	}
	
	/** Get the state(s) after the transition */
	private Set<PTSTransition> getNextSates(String currentState, String taskName) {
		Map<String, Set<PTSTransition>> currentStateTransitions = this.transitions.get(currentState);

		if (currentStateTransitions != null) {
			return currentStateTransitions.get(taskName);
		}
		return null;
	}

	// ------ Start for Paths Informations ---------
	public boolean matcheswithPaths(List<String> input) {

		Set<List<String>> allPaths = getAllPathsFromState(initState, input);

		List<List<String>> completePaths = new ArrayList<>();
		List<List<String>> incompletePaths = new ArrayList<>();

		for (List<String> path : allPaths) {
			if (finalStates.contains(path.get(path.size() - 1))) {
				completePaths.add(path);
			} else {
				incompletePaths.add(path);
			}
		}
		if (completePaths.size() > 0) {
			// printCompletePaths(completePaths);
			printCompletePathsWithEvents(completePaths, input);
			return true;
		} else {
			// printAllPaths(incompletePaths);
			printAllPathsWithEvents(incompletePaths, input);
			return false;
		}
	}
	
	// ------ key tasks methods start -------- //
	/**This function is used to get the key tasks.
	 * Based on the given properties, it filters out the key tasks.
	 * */
	public boolean matcheswithPaths(List<String> input, Boolean flag) {

		Set<List<String>> allPaths = getAllPathsFromState(initState, input);

		List<List<String>> completePaths = new ArrayList<>();
		List<List<String>> incompletePaths = new ArrayList<>();

		for (List<String> path : allPaths) {
			if (finalStates.contains(path.get(path.size() - 1))) {
				completePaths.add(path);
			} else {
				incompletePaths.add(path);
			}
		}
		if (completePaths.size() > 0) {
			// printCompletePaths(completePaths);
			computeCompletePathsWithEvents(completePaths, input);
			return true;
		} else {
			// printAllPaths(incompletePaths);
			computeAllPathsWithEvents(incompletePaths, input);
			return false;
		}
	}
	
	/**Get the execution Incomplete paths for a given property.*/
	private void computeAllPathsWithEvents(List<List<String>> paths, List<String> input) {

		System.out.println("Incomplete paths:");
		for (List<String> path : paths) {
			List<TransitionPath> incompletePath = new ArrayList<>();
			StringJoiner sj = new StringJoiner(" -> ");
			for (int i = 0; i < path.size() - 1; i++) {
				sj.add(path.get(i) + "," + input.get(i) + "," + path.get(i + 1));
				incompletePath.add(new TransitionPath(path.get(i), input.get(i), path.get(i + 1)));
			}
			System.out.println(sj);
			this.incompletePaths.add(incompletePath);
		}

		this.computeKeyTasksInCompletePaths();
	}
	
	/**Get the key tasks from all Incomplete paths.*/
	private Set<String> computeKeyTasksInCompletePaths() {
		
		//this.keyTasks = new HashSet<>();
		
		for(List<TransitionPath> incompletePath : this.incompletePaths) {
			for (TransitionPath transition : incompletePath) {
				String sourceState = transition.getSourceState();
				String eventName = transition.getEventName();

				if (this.transitions.get(sourceState).get(eventName) != null) {
					
					if(this.transitions.get(sourceState).size() > 1) {
						keyTasks.add(eventName);
					}
				}
			}
			
			if(keyTasks.isEmpty()) {
				keyTasks.add(incompletePath.get(0).getEventName());
			}
		}
		
		//Store propertyPaths transitions used to compute simple probabilities
		//FIXME
		this.propertyPaths.addAll(this.incompletePaths);
		// After getting the keyTasks, the execution paths needs to be cleared
		this.incompletePaths.clear();
		System.out.println("keyTasks:" +  keyTasks);
		
		return keyTasks;
	}
	
	/**Get the execution complete path for a given property.*/
	private void computeCompletePathsWithEvents(List<List<String>> paths, List<String> input) {

		System.out.println("Complete paths:");
		for (List<String> path : paths) {
			StringJoiner sj = new StringJoiner(" -> ");
			for (int i = 0; i < path.size() - 1; i++) {
				sj.add(path.get(i) + "," + input.get(i) + "," + path.get(i + 1));

				completePath.add(new TransitionPath(path.get(i), input.get(i), path.get(i + 1)));
			}
			System.out.println(sj);
		}

		this.computeKeyTasksCompletePaths();
	}
	
	/**Get the key tasks from the complete path.*/
	private Set<String> computeKeyTasksCompletePaths() {
		
		this.keyTasks = new HashSet<>();
		for (TransitionPath transition : this.completePath) {
			String sourceState = transition.getSourceState();
			String eventName = transition.getEventName();
			String targetState = transition.getTargetState();

			if (this.transitions.get(sourceState).get(eventName) != null) {
				
				if(this.transitions.get(sourceState).size() > 1) {
					keyTasks.add(eventName);
				}
			}
		}
		
		if(keyTasks.isEmpty()) {
			keyTasks.add(this.completePath.get(0).getEventName());
		}
		
		//Store propertyPaths transitions used to compute simple probabilities
		//FIXME Need to refine the computation of paths
		//Depth assignment, otherwise null 
		this.propertyPaths.add(new ArrayList<>(this.completePath));
		
		// After updating the counters, the execution paths needs to be cleared
		this.completePath.clear();
		System.out.println("keyTasks:" +  keyTasks);
		
		return keyTasks;
	}
	// ------ key tasks methods end -------- //
	
	// ------ computeProertyProb Start -------- //
	//FIXME
	//The exact version should be: Compute the probability via PMC and receive its return value.
	//This is only a pre-test version
	public double getPropertyProb() {
		int sleepTime = new Random().nextInt(1500) + 500; //  between 500 and 2000 ms
        try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.computePropertyProb();
	}
	
	private double computePropertyProb() {
		double prob = 1.0;
		//System.out.println("propertyPaths:" + this.propertyPaths);
		
		for(List<TransitionPath> transitionPath : this.propertyPaths) {
			
			//System.out.println("transitionPath:" + transitionPath);
			
			for(TransitionPath transition : transitionPath) {
				String sourceState = transition.getSourceState();
				String eventName = transition.getEventName();
				String targetState = transition.getTargetState();
				
				//System.out.println("ss:" + sourceState + ",e:" + eventName + ", es:" + targetState);
				if (this.transitions.get(sourceState).get(eventName) != null) {
					for (PTSTransition ptsTransition : this.transitions.get(sourceState).get(eventName)) {
						if (ptsTransition.equals(targetState)) {
							prob *= ptsTransition.getProbability();
						}
					}
				}
			}
		}
		System.out.println("* Property prob:" + prob);
		return prob;
	}
	// ------ computeProertyProb end -------- //
	
	private Set<List<String>> getAllPathsFromState(String state, List<String> remainingInput) {
		Set<List<String>> allPaths = new HashSet<>();
		if (remainingInput.size() == 0) {
			List<String> path = new ArrayList<>();
			path.add(state);
			allPaths.add(path);
			return allPaths;
		}
		String symbol = remainingInput.get(0);
		List<String> remainingSymbols = remainingInput.subList(1, remainingInput.size());
		for (String nextState : getNextStates(state, symbol)) {
			Set<List<String>> paths = getAllPathsFromState(nextState, remainingSymbols);
			for (List<String> path : paths) {
				path.add(0, state);
				allPaths.add(path);
			}
		}
		return allPaths;
	}

	private Set<String> getNextStates(String state, String symbol) {

		Set<String> nextStates = new HashSet<>();
		Map<String, Set<PTSTransition>> ongoingTransitons = this.transitions.get(state);

		if (ongoingTransitons != null && ongoingTransitons.get(symbol) != null) {
			Set<PTSTransition> ptsTransitions = ongoingTransitons.get(symbol);

			for (PTSTransition transition : ptsTransitions) {
				nextStates.add(transition.getTargetState());
			}
			// return nextStates;
		}

		return nextStates;
	}

	private void printCompletePaths(List<List<String>> paths) {

		System.out.println("Complete paths:");
		for (List<String> path : paths) {
			StringJoiner sj = new StringJoiner(" -> ");
			for (int i = 0; i < path.size() - 1; i++) {
				sj.add(path.get(i) + "," + path.get(i + 1));
			}
			System.out.println(sj);
		}
	}

	private void printCompletePathsWithEvents(List<List<String>> paths, List<String> input) {

		System.out.println("Complete paths:");
		for (List<String> path : paths) {
			StringJoiner sj = new StringJoiner(" -> ");
			for (int i = 0; i < path.size() - 1; i++) {
				sj.add(path.get(i) + "," + input.get(i) + "," + path.get(i + 1));

				completePath.add(new TransitionPath(path.get(i), input.get(i), path.get(i + 1)));
			}
			System.out.println(sj);
		}

		this.updateCounter();
	}

	private void printAllPaths(List<List<String>> paths) {
		System.out.println("Incomplete paths:");
		for (List<String> path : paths) {
			StringJoiner sj = new StringJoiner(" -> ");
			for (int i = 0; i < path.size() - 1; i++) {
				sj.add(path.get(i) + "," + path.get(i + 1));
			}
			System.out.println(sj);
		}
	}

	private void printAllPathsWithEvents(List<List<String>> paths, List<String> input) {

		System.out.println("Incomplete paths:");
		for (List<String> path : paths) {
			List<TransitionPath> incompletePath = new ArrayList<>();
			StringJoiner sj = new StringJoiner(" -> ");
			for (int i = 0; i < path.size() - 1; i++) {
				sj.add(path.get(i) + "," + input.get(i) + "," + path.get(i + 1));
				incompletePath.add(new TransitionPath(path.get(i), input.get(i), path.get(i + 1)));
			}
			System.out.println(sj);
			this.incompletePaths.add(incompletePath);
		}

		this.updateCounterForIncompletPaths();
	}

	// ------- end for Paths Informations ---------
	// Update the counter value
	private void updateCounter() {

		for (TransitionPath transition : this.completePath) {
			String sourceState = transition.getSourceState();
			String eventName = transition.getEventName();
			String targetState = transition.getTargetState();

			if (this.transitions.get(sourceState).get(eventName) != null) {
				for (PTSTransition ptsTransition : this.transitions.get(sourceState).get(eventName)) {
					if (ptsTransition.equals(targetState)) {
						ptsTransition.addCounter();
					}
				}
			}
		}
		// After updating the counters, the execution paths needs to be cleared
		this.completePath.clear();
		// this.incompletePaths.clear();
		this.computeProbability();
	}

	private void updateCounterForIncompletPaths() {

		int sizePaths = this.incompletePaths.size();

		if (sizePaths == 1) {
			for (TransitionPath transition : this.incompletePaths.get(0)) {
				String sourceState = transition.getSourceState();
				String eventName = transition.getEventName();
				String targetState = transition.getTargetState();

				if (this.transitions.get(sourceState).get(eventName) != null) {
					for (PTSTransition ptsTransition : this.transitions.get(sourceState).get(eventName)) {
						if (ptsTransition.equals(targetState)) {
							ptsTransition.addCounter();
						}
					}
				}
			}

		}

		Set<TransitionPath> transitionPathsSet = new HashSet<>();

		if (sizePaths >= 2) {

			System.out.println("IncompletPaths:" + sizePaths);

			int longestSize = this.incompletePaths.get(0).size();
			int pathIndex = 0;

			for (int i = 1; i < sizePaths; i++) {
				if (longestSize < this.incompletePaths.get(i).size()) {
					longestSize = this.incompletePaths.get(i).size();
					pathIndex = i;
				}
			}

			// System.out.println("pathIndex:" + pathIndex);

			for (int i = 0; i < this.incompletePaths.get(pathIndex).size(); i++) {

				for (int index = 0; index < sizePaths; index++) {
					if (!this.incompletePaths.get(index).get(i).equals(this.incompletePaths.get(pathIndex).get(i))) {

						pathIndex = i;

						break;
					}

				}

			}

			for (int i = 0; i < pathIndex; i++) {

				transitionPathsSet.add(this.incompletePaths.get(0).get(i));
			}

			for (int i = 0; i < sizePaths; i++) {

				for (int j = pathIndex; j < this.incompletePaths.get(i).size(); j++) {

					transitionPathsSet.add(this.incompletePaths.get(i).get(j));
				}

			}

		}

		System.out.println("transitionPathsSet:" + transitionPathsSet);

		Map<String, Map<String, Integer>> incompletTransitionCounterMap = new HashMap<>();

		for (TransitionPath transition : transitionPathsSet) {

			String sourceState = transition.getSourceState();
			String eventName = transition.getEventName();
			String targetState = transition.getTargetState();
			System.out.println(sourceState + "," + eventName + "," + targetState);

			for (PTSTransition ptsTransition : this.transitions.get(sourceState).get(eventName)) {
				if (ptsTransition.equals(targetState)) {
					ptsTransition.addCounter();
				}
			}

			if (!incompletTransitionCounterMap.containsKey(sourceState)) {
				incompletTransitionCounterMap.put(sourceState, new HashMap<>());
				incompletTransitionCounterMap.get(sourceState).put(eventName, 1);
			} else {
				if (!incompletTransitionCounterMap.get(sourceState).containsKey(eventName)) {
					incompletTransitionCounterMap.get(sourceState).put(eventName, 1);
				} else {
					int currentValue = incompletTransitionCounterMap.get(sourceState).get(eventName);
					incompletTransitionCounterMap.get(sourceState).replace(eventName, currentValue + 1);
				}
			}

		}

		System.out.println("incompletTransitionCounterMap:" + incompletTransitionCounterMap);

		Map<String, Map<String, Integer>> filterTranstionsMap = new HashMap<>();

		for (String sourceState : incompletTransitionCounterMap.keySet()) {
			for (String eventName : incompletTransitionCounterMap.get(sourceState).keySet()) {
				int cntNbr = incompletTransitionCounterMap.get(sourceState).get(eventName);
				if (cntNbr > 1) {

					if (!filterTranstionsMap.containsKey(sourceState)) {
						filterTranstionsMap.put(sourceState, new HashMap<>());
						filterTranstionsMap.get(sourceState).put(eventName, cntNbr - 1);
					} else {
						filterTranstionsMap.get(sourceState).put(eventName, cntNbr - 1);
					}
				}
			}

		}

		// System.out.println("filterTranstionsMap:" + filterTranstionsMap);
		this.computeQuasiProbability(filterTranstionsMap);

		this.incompletePaths.clear();

	}

	private void computeQuasiProbability(Map<String, Map<String, Integer>> filterTranstionsMap) {
		// modify "sum" value for incomplete paths

		for (String state : this.transitions.keySet()) {
			int sum = 0;
			for (String eventName : this.transitions.get(state).keySet()) {
				for (PTSTransition ptsTransition : this.transitions.get(state).get(eventName)) {
					sum += ptsTransition.getCounter();
				}
			}

			for (String eventName : this.transitions.get(state).keySet()) {
				if (filterTranstionsMap.containsKey(state)) {
					if (filterTranstionsMap.get(state).containsKey(eventName)) {
						sum -= filterTranstionsMap.get(state).get(eventName);
					}
				}
			}

			for (String eventName : this.transitions.get(state).keySet()) {
				for (PTSTransition ptsTransition : this.transitions.get(state).get(eventName)) {
					ptsTransition.updateProb(sum);
				}
			}
		}

		this.normalisationOfPrbability();
	}

	public void printCounter() {
		System.out.println(this.transitions);
	}

	private void computeProbability() {

		for (String state : this.transitions.keySet()) {
			int sum = 0;
			for (String eventName : this.transitions.get(state).keySet()) {
				for (PTSTransition ptsTransition : this.transitions.get(state).get(eventName)) {
					sum += ptsTransition.getCounter();
				}
			}

			for (String eventName : this.transitions.get(state).keySet()) {
				for (PTSTransition ptsTransition : this.transitions.get(state).get(eventName)) {
					ptsTransition.updateProb(sum);
				}
			}
		}

		this.normalisationOfPrbability();
	}

	private void normalisationOfPrbability() {

		for (String state : this.transitions.keySet()) {
			double sumProb = 0.0;

			for (String eventName : this.transitions.get(state).keySet()) {
				for (PTSTransition ptsTransition : this.transitions.get(state).get(eventName)) {

					//System.out.println("pts:" + ptsTransition.getTargetState());
					//System.out.println("value:" + ptsTransition.getProbability());
					sumProb += ptsTransition.getProbability();
				}
			}

			//System.out.println("sumProb:" + sumProb);

			if (sumProb != 1.0 && sumProb != 0.0) {
				double normProbValue = 0.0;
				for (String eventName : this.transitions.get(state).keySet()) {
					for (PTSTransition ptsTransition : this.transitions.get(state).get(eventName)) {
						normProbValue = (double) ptsTransition.getProbability() / sumProb;
						ptsTransition.setProbability(normProbValue);
					}
				}

			}
		}

	}
	
	/**Reinitializes the probability value of each transition*/
	public void initProbability() {
		
		this.incompletePaths.clear();
		this.completePath.clear();
		this.propertyPaths.clear();
		
		for (String state : this.transitions.keySet()) {
			for (String eventName : this.transitions.get(state).keySet()) {
				for (PTSTransition ptsTransition : this.transitions.get(state).get(eventName)) {
					ptsTransition.setProbability(0.0);
					ptsTransition.setCounter(0);
				}
			}

		}
	}


	public Set<String> getStates() {
		return states;
	}

	public void setStates(Set<String> states) {
		this.states = states;
	}

	public Map<String, Map<String, Set<PTSTransition>>> getTransitions() {
		return transitions;
	}

	public void setTransitions(Map<String, Map<String, Set<PTSTransition>>> transitions) {
		this.transitions = transitions;
	}

	public String getInitState() {
		return initState;
	}

	public void setInitState(String initState) {
		this.initState = initState;
	}

	public Set<String> getFinalStates() {
		return finalStates;
	}

	public void addFinalState(String finalState) {
		this.finalStates.add(finalState);
	}

	public void setFinalStates(Set<String> finalStates) {
		this.finalStates = finalStates;
	}

	public Set<String> getAlphabets() {
		return alphabets;
	}

	public void setAlphabets(Set<String> alphabets) {
		this.alphabets = alphabets;
	}
	
	public Set<String> getKeyTasks() {
		return keyTasks;
	}
	
	public int getNbrOfTranstions() {
		
		int nbrOfTranstions = 0;
		for (String state : this.transitions.keySet()) {
			for (String eventName : this.transitions.get(state).keySet()) {
				nbrOfTranstions += this.transitions.get(state).get(eventName).size();
			}
		}

		return nbrOfTranstions;
	}

	@Override
	public String toString() {
		return "PTS\n[states:\n" + states + ",\n transitions=" + transitions + ",\n initState=" + initState
				+ ",\n finalStates=" + finalStates + ",\n alphabets=" + alphabets + "]\n";
	}

	public List<List<TransitionPath>> getPropertyPaths() {
		return propertyPaths;
	}

}
