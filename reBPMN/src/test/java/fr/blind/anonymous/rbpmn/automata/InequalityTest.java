package fr.blind.anonymous.rbpmn.automata;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import fr.blind.anonymous.rbpmn.automata.GuardVariable;
import fr.blind.anonymous.rbpmn.automata.Inequality;

class InequalityTest {

	@Test
	void test() {
		GuardVariable variale = new GuardVariable("x");
		Inequality inequaliti = new Inequality(variale, 5, ">");
		
		assertFalse(inequaliti.evaluate());
		
		variale.setValue(10);
		
		assertTrue(inequaliti.evaluate());
		
	}
	
	@Test
	void testEnforcerAutomata() {
		GuardVariable variale = new GuardVariable("P(A.B)");
		Inequality inequality = new Inequality(variale, 0.5, "<=");
		
		
		variale.setValue(0.6);
		assertFalse(inequality.evaluate());
		System.out.println(inequality.evaluate());
		System.out.println(inequality.getLeftOperand().getValue());
		
		variale.setValue(0.4);
		
		System.out.println(inequality.evaluate());
		System.out.println(inequality.getLeftOperand().getValue());
		
		assertTrue(inequality.evaluate());
		
	}

}
