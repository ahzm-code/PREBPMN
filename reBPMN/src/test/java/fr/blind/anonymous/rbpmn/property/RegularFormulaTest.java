package fr.blind.anonymous.rbpmn.property;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import fr.blind.anonymous.rbpmn.property.RegularFormula;

class RegularFormulaTest {

	@Test
	void testTaskList() {
		RegularFormula rf = new RegularFormula("R1");
		
		rf.concatRegular("a","b");
		
		System.out.println(rf.getTaskList());
		assertEquals(2, rf.getTaskList().size());
		
		rf.concatRegular("c");
		
		assertEquals(3, rf.getTaskList().size());
		
		for(String a : rf.getTaskList()) {
			System.out.println(a);
		}
	}

}
