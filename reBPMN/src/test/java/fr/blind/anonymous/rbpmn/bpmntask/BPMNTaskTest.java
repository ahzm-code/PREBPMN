package fr.blind.anonymous.rbpmn.bpmntask;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import fr.blind.anonymous.rbpmn.bpmntask.BPMNTask;

class BPMNTaskTest {

	@Test
	void testTaskName() {
		BPMNTask taskA = new BPMNTask("a");
		
		BPMNTask taskB = new BPMNTask("b");
		
		assertEquals(taskA.getName(), "a");
		assertEquals(taskB.getName(), "b");
		
		System.out.println();
	}

}
