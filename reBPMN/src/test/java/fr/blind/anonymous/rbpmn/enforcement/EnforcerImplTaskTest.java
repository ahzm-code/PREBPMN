package fr.blind.anonymous.rbpmn.enforcement;

import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;
import java.util.List;

import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;

import fr.blind.anonymous.rbpmn.automata.DFATask;
import fr.blind.anonymous.rbpmn.automata.State;
import fr.blind.anonymous.rbpmn.bpmntask.BPMNTask;
import fr.blind.anonymous.rbpmn.enforcement.EnforcerImplTask;

class EnforcerImplTaskTest {
	
	
	@Test
	void testTrue() {
		DFATask pa = new DFATask();
		State initState = new State(1);
		State secondState = new State(2);
		
		pa.setInitialState(initState);
		
		pa.addFinalState(initState);
		pa.addFinalState(secondState);
		
		pa.addTransition(initState, "a", secondState);
		pa.addTransition(secondState, "b", initState);
		
		System.out.println(pa);
		
		List<BPMNTask> inputTask = new LinkedList<>();
		
		inputTask.add(new BPMNTask("1", "a"));
		inputTask.add(new BPMNTask("2", "b"));
		inputTask.add(new BPMNTask("3", "a"));
		inputTask.add(new BPMNTask("4", "b"));
		
		EnforcerImplTask enforcer = new EnforcerImplTask(inputTask, pa,"test");
		System.out.println(enforcer.enforce());
	}
	
	@Test
	void testBuffer() {
		DFATask pa = new DFATask();
		State initState = new State(1);
		State secondState = new State(2);
		
		pa.setInitialState(initState);
		
		pa.addFinalState(initState);
		pa.addFinalState(secondState);
		
		pa.addTransition(initState, "a", secondState);
		pa.addTransition(secondState, "b", initState);
		
		System.out.println(pa);
		
		List<BPMNTask> inputTask = new LinkedList<>();
		
		inputTask.add(new BPMNTask("1", "a"));
		inputTask.add(new BPMNTask("2", "b"));
		inputTask.add(new BPMNTask("3", "a"));
		inputTask.add(new BPMNTask("4", "b"));
		inputTask.add(new BPMNTask("5", "b"));
		
		
		EnforcerImplTask enforcer = new EnforcerImplTask(inputTask, pa, "test");
		System.out.println(enforcer.enforce());
	}
	
	@Test
	void testReordering() {
		DFATask pa = new DFATask();
		State initState = new State(1);
		State secondState = new State(2);
		
		pa.setInitialState(initState);
		
		pa.addFinalState(initState);
		pa.addFinalState(secondState);
		
		pa.addTransition(initState, "a", secondState);
		pa.addTransition(secondState, "b", initState);
		
		System.out.println(pa);
		
		List<BPMNTask> inputTask = new LinkedList<>();
		
		inputTask.add(new BPMNTask("1", "a"));
		inputTask.add(new BPMNTask("2", "b"));
		inputTask.add(new BPMNTask("3", "a"));
		inputTask.add(new BPMNTask("4", "b"));
		inputTask.add(new BPMNTask("5", "b"));
		inputTask.add(new BPMNTask("6", "a"));
		
		
		EnforcerImplTask enforcer = new EnforcerImplTask(inputTask, pa,"test");
		System.out.println(enforcer.enforce());
	}
	
	
	@Test
	void testEventNotInAlphabet() {
		DFATask pa = new DFATask();
		State initState = new State(1);
		State secondState = new State(2);
		
		pa.setInitialState(initState);
		
		pa.addFinalState(initState);
		pa.addFinalState(secondState);
		
		pa.addTransition(initState, "a", secondState);
		pa.addTransition(secondState, "b", initState);
		
		System.out.println(pa);
		
		List<BPMNTask> inputTask = new LinkedList<>();
		
		inputTask.add(new BPMNTask("1", "a"));
		inputTask.add(new BPMNTask("2", "b"));
		inputTask.add(new BPMNTask("3", "a"));
		inputTask.add(new BPMNTask("4", "c"));
		inputTask.add(new BPMNTask("5", "b"));
		inputTask.add(new BPMNTask("6", "a"));
		
		
		EnforcerImplTask enforcer = new EnforcerImplTask(inputTask, pa,"test");
		System.out.println(enforcer.enforce());
	}

}
