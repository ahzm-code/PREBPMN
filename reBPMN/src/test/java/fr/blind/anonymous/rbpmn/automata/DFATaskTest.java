package fr.blind.anonymous.rbpmn.automata;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import fr.blind.anonymous.rbpmn.automata.DFATask;
import fr.blind.anonymous.rbpmn.automata.State;
import fr.blind.anonymous.rbpmn.bpmntask.BPMNTask;

class DFATaskTest {

	@Test
	void test() {
		
		DFATask pfb = new DFATask();
		
		State q1 = new State(1);
        State q2 = new State(2);
        
        pfb.setInitialState(q1);
        pfb.addFinalState(q1);
        pfb.addFinalState(q2);
        
        pfb.addTransition(q1, "a", q2);
        pfb.addTransition(q2, "b", q1);
        
        BPMNTask a = new BPMNTask("a");
        BPMNTask b = new BPMNTask("b");
        
        List<BPMNTask> tasks = new ArrayList<>();
        tasks.add(a);
        tasks.add(b);
        
        //assertTrue(pfb.accepts(tasks));
        
        assertTrue(pfb.checkTask(a));
        assertTrue(pfb.checkTask(b));
        assertTrue(pfb.checkTask(b));
        assertTrue(pfb.checkTask(a));
	}

}
