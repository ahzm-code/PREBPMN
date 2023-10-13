package fr.blind.anonymous.rbpmn.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.blind.anonymous.rbpmn.automata.PTSTask;

public class Aut2Auto {

	private String initState;
	private Integer nbrOfStates;
	private Integer nbrOfTransitions;
	
	private Set<String> finalStates;
	private Set<String> states;
	private Set<String> events;
	private List<List<String>> transitions;

	public Aut2Auto() {

		this.states = new HashSet<>(); // all states
		this.finalStates = new HashSet<>(); // all final states
		this.events = new HashSet<>(); // all events
		this.transitions = new ArrayList<>(); // all transitions
	}

	public void readFile(String filePath) {

		File file = new File(filePath);

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {

			// first line is Descriptor for LTS
			String firstline = br.readLine();
			this.firstLine(firstline);

			String line;
			// System.out.println("Trainsition");
			while ((line = br.readLine()) != null) {

				System.out.println(line);
				this.transitionLine(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.generateFinalStates();
	}

	private void generateStates(int nbrOfStates) {
		for (int i = 0; i < nbrOfStates; i++) {
			this.states.add(Integer.toString(i));
		}
	}

	private void firstLine(String firstLine) {

		List<String> numbers = new ArrayList<>();

		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(firstLine);

		while (matcher.find()) {
			numbers.add(matcher.group());
		}
		System.out.println(numbers);
		//this.initState = Integer.parseInt(numbers.get(0));
		this.initState = numbers.get(0);

		this.nbrOfStates = Integer.parseInt(numbers.get(2));
		this.generateStates(this.nbrOfStates);

		this.nbrOfTransitions = Integer.parseInt(numbers.get(1));
		System.out.println("InitState:" + this.initState);
	}

	private void transitionLine(String transitionLine) {

		List<String> elements = new ArrayList<>();
		Pattern pattern = Pattern.compile("\\((.*?)\\)");
		Matcher matcher = pattern.matcher(transitionLine);

		if (matcher.find()) {
			String[] parts = matcher.group(1).split(",");
			for(int i=0;i<parts.length; i++) {
				parts[i] = parts[i].trim();
			}
			elements.addAll(Arrays.asList(parts));
		}
		System.out.println(elements);
		
		if (!this.events.contains(elements.get(1))) {
			this.events.add(elements.get(1));
		}

		this.transitions.add(elements);
	}
	
	/**
	 * Description: Get the end states of the system based on the input file.
	 * Main idea: No out-degree status.
	 * 
	 * */
	private void generateFinalStates() {
		
//		Set<Integer> fromStates = new HashSet<>();
//		for(List<String> transition: this.transitions) {
//			//System.out.println("final_transition:" + transition.get(0));
//			fromStates.add(Integer.parseInt(transition.get(0)));
//		}
		
		Set<String> fromStates = new HashSet<>();
		for(List<String> transition: this.transitions) {
			//System.out.println("final_transition:" + transition.get(0));
			fromStates.add(transition.get(0));
		}
		
		this.finalStates.addAll(this.states);
		this.finalStates.removeAll(fromStates);
	}

	public String getInitState() {
		return initState;
	}

	public void setInitState(String initState) {
		this.initState = initState;
	}

	public Integer getNbrOfStates() {
		return nbrOfStates;
	}

	public void setNbrOfStates(Integer nbrOfStates) {
		this.nbrOfStates = nbrOfStates;
	}

	public Integer getNbrOfTransitions() {
		return nbrOfTransitions;
	}

	public void setNbrOfTransitions(Integer nbrOfTransitions) {
		this.nbrOfTransitions = nbrOfTransitions;
	}

	public Set<String> getStates() {
		return states;
	}

	public void setStates(Set<String> states) {
		this.states = states;
	}

	public Set<String> getEvents() {
		return events;
	}

	public void setEvents(Set<String> events) {
		this.events = events;
	}

	public List<List<String>> getTransitions() {
		return transitions;
	}

	public void setTransitions(List<List<String>> transitions) {
		this.transitions = transitions;
	}

	public Set<String> getFinalStates() {
		return finalStates;
	}

	public void setFinalStates(Set<String> finalStates) {
		this.finalStates = finalStates;
	}
	
	private PTSTask generateInitPTS() {
		
		PTSTask init_pts = new PTSTask();
		init_pts.setInitState(this.initState);
		init_pts.setFinalStates(this.finalStates);
		init_pts.setStates(this.states);
		
		for(List<String> transition: this.transitions) {
			
			//Integer fromState = Integer.parseInt(transition.get(0));
			String fromState = transition.get(0);
			
			String eventName = transition.get(1);
			
			//Integer toState = Integer.parseInt(transition.get(2));
			String toState = transition.get(2);
			
			init_pts.addTransition(fromState, eventName, toState);
		}
		
		return  init_pts;
	}
	
	public PTSTask getInitPTS() {
		return this.generateInitPTS();
	}
	
	@Override
	public String toString() {
		return "Aut2Auto [des (" + initState + "," + nbrOfTransitions + ","
				+ nbrOfStates  + ")] \nstates=" + states + "\nevents=" + events + "\ntransitions=" + transitions + "";
	}

}
