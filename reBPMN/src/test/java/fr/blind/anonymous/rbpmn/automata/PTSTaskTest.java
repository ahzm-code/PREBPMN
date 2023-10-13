package fr.blind.anonymous.rbpmn.automata;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import fr.blind.anonymous.rbpmn.automata.PTSTask;
import fr.blind.anonymous.rbpmn.enforcement.CriticalTasks;
import fr.blind.anonymous.rbpmn.property.RegularFormula;

class PTSTaskTest {

	@Test
	void testNFACompletePaths() {
		// Test NFA with complete paths
		PTSTask pts = new PTSTask();
		pts.addTransition("0", "a", "3");
		//pts.addTransition("0", "b", "3");
		pts.addTransition("0", "a", "1");
		//pts.addTransition("1", "b", "3");
		//pts.addTransition("0", "b", "2");
		//pts.addTransition("2", "a", "3");
		
		pts.addTransition("3", "c", "4");
		
		pts.setInitState("0");
		pts.addFinalState("4");
		
		
		List<String> input = new ArrayList<>();
		input.add("a");
		//input.add("b");
		input.add("c");
		System.out.println("Matches 'a'? " + pts.matcheswithPaths(input));
		
	}
	
	
	@Test
	void testNFAIncompletePaths() {
		// Test NFA with incomplete paths
		PTSTask pts = new PTSTask();
		pts.addTransition("0", "a", "3");
		//pts.addTransition("0", "b", "3");
		pts.addTransition("0", "a", "1");
		//pts.addTransition("1", "b", "3");
		//pts.addTransition("0", "b", "2");
		//pts.addTransition("2", "a", "3");
		
		pts.addTransition("3", "c", "4");
		
		pts.setInitState("0");
		pts.addFinalState("4");
		
		
		List<String> input = new ArrayList<>();
		input.add("a");
		
		//input.add("b");
		//input.add("c");
		System.out.println("Matches 'a'? " + pts.matcheswithPaths(input));
		
	}
	
	@Test
	void testCounter() {
		
		PTSTask pts = new PTSTask();
		pts.addTransition("0", "a", "3");
		pts.addTransition("0", "a", "1");
		pts.addTransition("3", "c", "4");
		
		pts.setInitState("0");
		pts.addFinalState("4");
		
		List<String> input = new ArrayList<>();
		input.add("a");
		input.add("c");
		System.out.println("Matches 'ac'? " + pts.matcheswithPaths(input));
		pts.printCounter();
		System.out.println("Matches 'ac'? " + pts.matcheswithPaths(input));
		pts.printCounter();
		
		System.out.println("Matches 'ac'? " + pts.matcheswithPaths(input));
		pts.printCounter();
		
		System.out.println("Matches 'ac'? " + pts.matcheswithPaths(input));
		pts.printCounter();
	}
	
	
	@Test
	void testComputeProbability() {
		
		PTSTask pts = new PTSTask();
		pts.addTransition("0", "a", "3");
		pts.addTransition("0", "a", "1");
		pts.addTransition("3", "c", "4");
		pts.addTransition("1", "b", "4");
		
		pts.setInitState("0");
		pts.addFinalState("4");
		
		List<String> input = new ArrayList<>();
		input.add("a");
		input.add("c");
		System.out.println("Matches 'ac'? " + pts.matcheswithPaths(input));
		pts.printCounter();
		System.out.println("Matches 'ac'? " + pts.matcheswithPaths(input));
		pts.printCounter();
		
		input.clear();
		input.add("a");
		input.add("b");
		
		System.out.println("Matches 'ab'? " + pts.matcheswithPaths(input));
		pts.printCounter();
		System.out.println("Matches 'ab'? " + pts.matcheswithPaths(input));
		pts.printCounter();
		System.out.println("Matches 'ab'? " + pts.matcheswithPaths(input));
		pts.printCounter();
	}
	
	@Test
	void testComputeProbability2() {
		
		PTSTask pts = new PTSTask();
		pts.addTransition("0", "a", "3");
		pts.addTransition("0", "a", "1");
		pts.addTransition("3", "c", "4");
		pts.addTransition("1", "b", "4");
		
		pts.setInitState("0");
		pts.addFinalState("4");
		
		List<String> input1 = new ArrayList<>();
		input1.add("a");
		input1.add("c");
		
		List<String> input2 = new ArrayList<>();
		input2.add("a");
		input2.add("b");
		
		System.out.println("Matches 'ac'? " + pts.matcheswithPaths(input1));
		pts.printCounter();
		System.out.println("Matches 'ab'? " + pts.matcheswithPaths(input2));
		pts.printCounter();
		
		System.out.println("Matches 'ac'? " + pts.matcheswithPaths(input1));
		pts.printCounter();
		System.out.println("Matches 'ab'? " + pts.matcheswithPaths(input2));
		pts.printCounter();
		
		System.out.println("Matches 'ab'? " + pts.matcheswithPaths(input2));
		pts.printCounter();
	}
	
	
	@Test
	void testComputeProbability3() {
		
		PTSTask pts = new PTSTask();
		pts.addTransition("0", "a", "1");
		
		pts.addTransition("1", "b", "2");
		pts.addTransition("1", "c", "2");
		
		pts.addTransition("1", "b", "3");
		pts.addTransition("3", "c", "2");
		
		pts.addTransition("1", "c", "4");
		pts.addTransition("4", "b", "2");
		
		pts.addTransition("2", "d", "5");
		
		
		pts.setInitState("0");
		pts.addFinalState("5");
		
		List<String> input1 = new ArrayList<>();
		input1.add("a");
		input1.add("b");
		//input1.add("c");
		
		System.out.println("Matches 'abc'? " + pts.matcheswithPaths(input1));
		pts.printCounter();
		
		input1.add("d");
		
		System.out.println("True-");
		System.out.println("Matches 'abcd'? " + pts.matcheswithPaths(input1));
		pts.printCounter();
		
	}
	
	
	@Test
	void testNbrOfTranstions() {
		
		PTSTask pts = new PTSTask();
		pts.addTransition("0", "a", "3");
		pts.addTransition("0", "a", "1");
		pts.addTransition("3", "c", "4");
		pts.addTransition("1", "b", "4");
		
		pts.setInitState("0");
		pts.addFinalState("4");
		
		System.out.println(pts.getNbrOfTranstions());
		assert(pts.getNbrOfTranstions()==4);
		
		
	}
	
	
	@Test
	void testOneIncompletePath() {
		
		PTSTask pts = new PTSTask();
		pts.addTransition("0", "a", "1");
		
		pts.addTransition("1", "b", "2");
		pts.addTransition("1", "c", "2");
		
		pts.addTransition("1", "b", "3");
		pts.addTransition("3", "c", "2");
		
		pts.addTransition("1", "c", "4");
		pts.addTransition("4", "b", "2");
		
		pts.addTransition("2", "d", "5");
		
		
		pts.setInitState("0");
		pts.addFinalState("5");
		
		List<String> input1 = new ArrayList<>();
		input1.add("a");
		input1.add("b");
		input1.add("c");
		
		System.out.println("Matches 'abc'? " + pts.matcheswithPaths(input1));
		pts.printCounter();
		
	}
	
	
	@Test
	void testTwoIncompletePaths() {
		
		PTSTask pts = new PTSTask();
		pts.addTransition("0", "a", "1");
		
		pts.addTransition("1", "b", "2");
		pts.addTransition("1", "c", "2");
		
		pts.addTransition("1", "b", "3");
		pts.addTransition("3", "c", "2");
		
		pts.addTransition("1", "c", "4");
		pts.addTransition("4", "b", "2");
		
		pts.addTransition("2", "d", "5");
		
		
		pts.setInitState("0");
		pts.addFinalState("5");
		
		List<String> input1 = new ArrayList<>();
		input1.add("a");
		input1.add("b");
		//input1.add("c");
		
		System.out.println("Matches 'ab'? " + pts.matcheswithPaths(input1));
		pts.printCounter();
		
	}
	
	@Test
	void testMultiIncompletePaths() {
		
		PTSTask pts = new PTSTask();
		pts.addTransition("0", "a", "1");
		
		pts.addTransition("1", "b", "2");
		pts.addTransition("1", "c", "2");
		
		pts.addTransition("1", "b", "3");
		pts.addTransition("3", "c", "2");
		
		pts.addTransition("1", "c", "4");
		pts.addTransition("4", "b", "2");
		
		pts.addTransition("2", "d", "5");
		
		pts.setInitState("0");
		pts.addFinalState("5");
		
		List<String> input1 = new ArrayList<>();
		input1.add("a");
		input1.add("b");
		//input1.add("c");
		
		System.out.println("Matches 'ab'? " + pts.matcheswithPaths(input1));
		pts.printCounter();
		System.out.println("Matches 'ab'? " + pts.matcheswithPaths(input1));
		pts.printCounter();
		
		
		input1.add("c");
		//input1.add("d");
		
		System.out.println("Matches 'abc'? " + pts.matcheswithPaths(input1));
		pts.printCounter();
		System.out.println("Matches 'abc'? " + pts.matcheswithPaths(input1));
		pts.printCounter();
			
	}
	
	@Test
	void testMultiIncompletePaths2() {
		
		PTSTask pts = new PTSTask();
		pts.addTransition("0", "a", "1");
		
		pts.addTransition("1", "b", "2");
		pts.addTransition("1", "c", "2");
		
		pts.addTransition("1", "b", "3");
		pts.addTransition("3", "c", "2");
		
		pts.addTransition("1", "c", "4");
		pts.addTransition("4", "b", "2");
		
		pts.addTransition("2", "d", "5");
		
		pts.setInitState("0");
		pts.addFinalState("5");
		
		List<String> input1 = new ArrayList<>();
		input1.add("a");
		input1.add("c");
		//input1.add("c");
		
		System.out.println("Matches 'ac'? " + pts.matcheswithPaths(input1));
		pts.printCounter();
		System.out.println("Matches 'ac'? " + pts.matcheswithPaths(input1));
		pts.printCounter();
		
		
		input1.add("b");
		//input1.add("d");
		System.out.println("Matches 'acb'? " + pts.matcheswithPaths(input1));
		pts.printCounter();
		System.out.println("Matches 'acb'? " + pts.matcheswithPaths(input1));
		pts.printCounter();
		
	}
	
	
	@Test
	void testMultiIncompletePaths3() {
		
		PTSTask pts = new PTSTask();
		pts.addTransition("0", "a", "1");
		
		pts.addTransition("1", "b", "2");
		pts.addTransition("1", "c", "2");
		
		pts.addTransition("1", "b", "3");
		pts.addTransition("3", "c", "2");
		
		pts.addTransition("1", "c", "4");
		pts.addTransition("4", "b", "2");
		
		pts.addTransition("2", "d", "5");
		
		pts.setInitState("0");
		pts.addFinalState("5");
		
		List<String> input1 = new ArrayList<>();
		input1.add("a");
		input1.add("c");
		input1.add("b");
		
		List<String> input2 = new ArrayList<>();
		input2.add("a");
		input2.add("b");
		input2.add("c");
		
		System.out.println("Matches 'acb'? " + pts.matcheswithPaths(input1));
		pts.printCounter();
		
		System.out.println("Matches 'abc'? " + pts.matcheswithPaths(input2));
		pts.printCounter();
		
		System.out.println("Matches 'acb'? " + pts.matcheswithPaths(input1));
		pts.printCounter();
		
		System.out.println("Matches 'abc'? " + pts.matcheswithPaths(input2));
		pts.printCounter();
		
	}
	
	@Test
	void testCompleteAndIncompletePaths() {
		
		PTSTask pts = new PTSTask();
		pts.addTransition("0", "a", "1");
		
		pts.addTransition("1", "b", "2");
		pts.addTransition("1", "c", "2");
		
		pts.addTransition("1", "b", "3");
		pts.addTransition("3", "c", "2");
		
		pts.addTransition("1", "c", "4");
		pts.addTransition("4", "b", "2");
		
		pts.addTransition("2", "d", "5");
		
		pts.setInitState("0");
		pts.addFinalState("5");
		
		
		List<String> input1 = new ArrayList<>();
		input1.add("a");
		input1.add("b");
		//input1.add("c");
		
		System.out.println("Matches 'ab'? " + pts.matcheswithPaths(input1));
		pts.printCounter();
		System.out.println("Matches 'ab'? " + pts.matcheswithPaths(input1));
		pts.printCounter();
		
		
		input1.add("c");
		input1.add("d");
		
		System.out.println("Matches 'abcd'? " + pts.matcheswithPaths(input1));
		pts.printCounter();
		System.out.println("Matches 'abcd'? " + pts.matcheswithPaths(input1));
		pts.printCounter();
		System.out.println("Matches 'abcd'? " + pts.matcheswithPaths(input1));
		pts.printCounter();
		System.out.println("Matches 'abcd'? " + pts.matcheswithPaths(input1));
		pts.printCounter();
	}

	
	@Test
	void testNormalisationProbability() {
		
		PTSTask pts = new PTSTask();
		pts.addTransition("0", "a", "1");
		
		pts.addTransition("1", "b", "2");
		pts.addTransition("1", "c", "2");
		
		pts.addTransition("1", "b", "3");
		pts.addTransition("3", "c", "2");
		
		pts.addTransition("1", "c", "4");
		pts.addTransition("4", "b", "2");
		
		pts.addTransition("2", "d", "5");
		
		
		pts.setInitState("0");
		pts.addFinalState("5");
		
		List<String> input1 = new ArrayList<>();
		input1.add("a");
		input1.add("b");
		//input1.add("c");
		
		System.out.println("Matches 'ab'? " + pts.matcheswithPaths(input1));
		pts.printCounter();
		
		List<String> input2 = new ArrayList<>();
		input2.add("a");
		input2.add("c");
		
		System.out.println("Matches 'ac'? " + pts.matcheswithPaths(input2));
		pts.printCounter();
	}
	
	
	@Test
	void testPropertyProbability() {
		
		
		PTSTask pts = new PTSTask();
		pts.addTransition("0", "a", "1");
		pts.addTransition("1", "b", "2");
		pts.addTransition("1", "c", "2");
		pts.setInitState("0");
		pts.addFinalState("2");
		
		System.out.println(pts);
		
		RegularFormula rf = new RegularFormula("r1");
		rf.concatRegular("a", "b");
		CriticalTasks ct = new CriticalTasks(pts, rf);
		System.out.println(ct.getKeyTasks());
		System.out.println(pts.getPropertyPaths());
		System.out.println(pts.getPropertyProb());
		
		
		List<String> input1 = new ArrayList<>();
		input1.add("a");
		input1.add("b");
		//input1.add("c");
		
		System.out.println("Matches 'ab'? " + pts.matcheswithPaths(input1));
		//pts.printCounter();
		System.out.println(pts.getPropertyProb());
		
		List<String> input2 = new ArrayList<>();
		input2.add("a");
		input2.add("c");
		
		System.out.println("Matches 'ac'? " + pts.matcheswithPaths(input2));
		pts.printCounter();
		System.out.println("Matches 'ac'? " + pts.matcheswithPaths(input2));
		pts.printCounter();
		System.out.println("Matches 'ac'? " + pts.matcheswithPaths(input2));
		pts.printCounter();
		
		System.out.println(pts.getPropertyProb());
	}

}
