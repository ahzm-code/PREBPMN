package fr.blind.anonymous.rbpmn.executor;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import fr.blind.anonymous.rbpmn.automata.GuardVariable;
import fr.blind.anonymous.rbpmn.automata.Inequality;
import fr.blind.anonymous.rbpmn.automata.PTSTask;
import fr.blind.anonymous.rbpmn.deploy.BPMNProcess;
import fr.blind.anonymous.rbpmn.executor.ProbabilisticModelChecking;
import fr.blind.anonymous.rbpmn.property.RegularFormula;

class ProbabilisticModelCheckingTest {

	@Test
	void testPMCNonCumulative() {
		
		BPMNProcess bpmnProcess = new BPMNProcess("SimpleExclusive", "SimpleExclusive");
		
		PTSTask pts = new PTSTask();
		pts.addTransition("0", "taskA", "1");
		pts.addTransition("1", "taskB", "2");
		pts.addTransition("1", "taskC", "2");
		pts.setInitState("0");
		pts.addFinalState("2");
		
		RegularFormula rf = new RegularFormula("r1");
		rf.concatRegular("taskA", "taskB");
		
		for(int i = 10; i<= 100; i+=10) {
			ProbabilisticModelChecking pmc = new ProbabilisticModelChecking(bpmnProcess, pts, rf, i);
			System.out.println(pmc.getProbability());
			System.out.println(pmc.getKeyTasks());
		}
		
	}
	
	@Test
	void testPMCNonCumulativeWithEnforcer() {
		
		BPMNProcess bpmnProcess = new BPMNProcess("SimpleExclusive", "SimpleExclusive");
		
		PTSTask pts = new PTSTask();
		pts.addTransition("0", "taskA", "1");
		pts.addTransition("1", "taskB", "2");
		pts.addTransition("1", "taskC", "2");
		pts.setInitState("0");
		pts.addFinalState("2");
		
		RegularFormula rf = new RegularFormula("P(A.B)");
		rf.concatRegular("taskA", "taskB");
		
		String epName = rf.getPropertyName();
		GuardVariable enforcerProb = new GuardVariable(epName);
		
		
		Inequality enforcerProperty = new Inequality(enforcerProb, 0.5, "<=");
		
		
		int cnt = 0;
		for(int i = 10; i<= 100; i+=10) {
			System.out.println("****** i:" + i);
			
			ProbabilisticModelChecking pmc = new ProbabilisticModelChecking(bpmnProcess, pts, rf, i);
			//System.out.println(pmc.getProbability());
			//System.out.println(pmc.getKeyTasks());
			
			enforcerProperty.getLeftOperand().setValue(pmc.getProbability());
			System.out.println(enforcerProperty.evaluate());
			if (!enforcerProperty.evaluate()) {
				cnt +=1; 
			}
		}
		System.out.println(cnt);
		
	}
	
	@Test
	void testPMCCumulative() {
		
		BPMNProcess bpmnProcess = new BPMNProcess("SimpleExclusive", "SimpleExclusive");
		
		PTSTask pts = new PTSTask();
		pts.addTransition("0", "taskA", "1");
		pts.addTransition("1", "taskB", "2");
		pts.addTransition("1", "taskC", "2");
		pts.setInitState("0");
		pts.addFinalState("2");
		
		RegularFormula rf = new RegularFormula("r1");
		rf.concatRegular("taskA", "taskB");
		
		ProbabilisticModelChecking pmc = new ProbabilisticModelChecking(bpmnProcess, pts, rf);
		System.out.println(pmc.getCumulProbability());
		System.out.println(pmc.getKeyTasks());
		
	}
	
	
	@Test
	void testPMCOngingProbability() {
		
		BPMNProcess bpmnProcess = new BPMNProcess("SimpleExclusive", "SimpleExclusive");
		
		PTSTask pts = new PTSTask();
		pts.addTransition("0", "taskA", "1");
		pts.addTransition("1", "taskB", "2");
		pts.addTransition("1", "taskC", "2");
		pts.setInitState("0");
		pts.addFinalState("2");
		
		RegularFormula rf = new RegularFormula("r1");
		rf.concatRegular("taskA", "taskB");
		
		ProbabilisticModelChecking pmc = new ProbabilisticModelChecking(bpmnProcess, pts, rf);
		System.out.println(pmc.getOngingProbability());
		System.out.println(pmc.getKeyTasks());
		
	}

}
