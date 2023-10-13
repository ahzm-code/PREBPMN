package fr.blind.anonymous.rbpmn.automata;

import java.time.Duration;
import java.util.Map;

import fr.blind.anonymous.rbpmn.bpmntask.BPMNTask;

public class Transition {
	
	private State fromState;
	private State toState;
	private BPMNTask bpmnTask;
	private Inequalities allConstraints;
	private Inequality probConstraint;
	
	public Transition(State fromState, BPMNTask bpmnTask, State toState) {
		
		this.fromState = fromState;
		this.toState = toState;
		this.bpmnTask = bpmnTask;
		//this.allConstraints = new Inequalities();
	}
	
	public void addConstraint(Inequality inequality) {
		
		this.allConstraints.addInequality(inequality);
	}
	
	public void addConstraints(Inequalities inequalities) {
		this.allConstraints = inequalities;
	}
	
	public void addProbConstraint(Inequality probInequality) {
		this.probConstraint  = probInequality;
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

	public BPMNTask getBpmnTask() {
		return bpmnTask;
	}

	public void setBpmnTask(BPMNTask bpmnTask) {
		this.bpmnTask = bpmnTask;
	}

	public Inequalities getAllConstraints() {
		return allConstraints;
	}

	public void setAllConstraints(Inequalities allConstraints) {
		this.allConstraints = allConstraints;
	}

	public Inequality getProbConstraint() {
		return probConstraint;
	}

	public void setProbConstraint(Inequality probConstraint) {
		this.probConstraint = probConstraint;
	}

	@Override
	public String toString() {
		return "Transition [fromState=" + fromState + ", toState=" + toState + ", bpmnTask=" + bpmnTask
				+ ", allConstraints=" + allConstraints + ", probConstraint=" + probConstraint + "]";
	}
	
}
