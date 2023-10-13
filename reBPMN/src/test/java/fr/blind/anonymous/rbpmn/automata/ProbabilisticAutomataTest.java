package fr.blind.anonymous.rbpmn.automata;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import fr.blind.anonymous.rbpmn.automata.GuardVariable;
import fr.blind.anonymous.rbpmn.automata.Inequality;
import fr.blind.anonymous.rbpmn.automata.ProbTransition;
import fr.blind.anonymous.rbpmn.automata.ProbabilisticAutomata;
import fr.blind.anonymous.rbpmn.automata.State;

class ProbabilisticAutomataTest {

	@Test
	void testProbabilisticAutomata() {
		
		State state1 =new State(1);
		State state2 =new State(2);
		
		GuardVariable variable = new GuardVariable("x");
		Inequality probConstraint1 = new Inequality(variable, 0.5, "<");
		Inequality probConstraint2 = new Inequality(variable, 0.5, ">=");
		
		ProbTransition probTransition1 = new ProbTransition(state1, probConstraint1, state1);
		
		ProbTransition probTransition2 = new ProbTransition(state1, probConstraint2, state2);
		
		
		ProbabilisticAutomata pa =new ProbabilisticAutomata(state1);
		
		pa.addTransitions(probTransition1);
		pa.addTransitions(probTransition2);
		
		
		assertTrue(pa.checkProb(0.49));
		System.out.println(pa.toString());
		
		assertTrue(pa.checkProb(0.51));
		System.out.println(pa.toString());
		
		assertTrue(pa.checkProb(0.51));
		System.out.println(pa.toString());
	}

}
