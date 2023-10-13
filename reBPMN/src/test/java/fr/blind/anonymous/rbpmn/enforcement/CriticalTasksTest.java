package fr.blind.anonymous.rbpmn.enforcement;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import fr.blind.anonymous.rbpmn.automata.PTSTask;
import fr.blind.anonymous.rbpmn.enforcement.CriticalTasks;
import fr.blind.anonymous.rbpmn.property.RegularFormula;

class CriticalTasksTest {

	@Test
	void testIncompetePath() {
		PTSTask pts = new PTSTask();
		pts.addTransition("0", "a", "1");
		pts.addTransition("1", "b", "2");
		pts.addTransition("1", "c", "2");
		pts.addTransition("2", "d", "3");
		pts.setInitState("0");
		pts.addFinalState("3");
		
		System.out.println(pts);
		
		RegularFormula rf = new RegularFormula("r1");
		rf.concatRegular("a", "b");
		
		CriticalTasks ct = new CriticalTasks(pts, rf);
		System.out.println(ct.getKeyTasks());
		
	}
	
	
	@Test
	void testSequenceTranstion() {
		PTSTask pts = new PTSTask();
		pts.addTransition("0", "a", "1");
		pts.addTransition("1", "b", "2");
		//pts.addTransition("1", "c", "2");
		pts.addTransition("2", "d", "3");
		pts.setInitState("0");
		pts.addFinalState("3");
		
		System.out.println(pts);
		
		RegularFormula rf = new RegularFormula("r1");
		rf.concatRegular("a", "b");
		
		CriticalTasks ct = new CriticalTasks(pts, rf);
		System.out.println(ct.getKeyTasks());
		assertEquals("a", ct.getKeyTasks().toArray()[0]);
		
	}
	
	@Test
	void testMultiKeyTasks() {
		PTSTask pts = new PTSTask();
		pts.addTransition("0", "a", "1");
		pts.addTransition("1", "b", "2");
		pts.addTransition("1", "c", "3");
		pts.addTransition("2", "d", "3");
		pts.addTransition("2", "e", "3");
		pts.setInitState("0");
		pts.addFinalState("3");
		
		System.out.println(pts);
		
		RegularFormula rf = new RegularFormula("r1");
		rf.concatRegular("a", "b");
		rf.concatRegular("d");
		CriticalTasks ct = new CriticalTasks(pts, rf);
		System.out.println(ct.getKeyTasks());
		
	}
	
	
	@Test
	void testMultiKeyTasks2() {
		PTSTask pts = new PTSTask();
		pts.addTransition("0", "a", "1");
		pts.addTransition("1", "b", "2");
		pts.addTransition("1", "c", "3");
		pts.addTransition("2", "d", "3");
		pts.addTransition("2", "e", "3");
		pts.addTransition("0", "f", "3");
		pts.setInitState("0");
		pts.addFinalState("3");
		
		System.out.println(pts);
		
		RegularFormula rf = new RegularFormula("r1");
		rf.concatRegular("a", "b");
		rf.concatRegular("d");
		CriticalTasks ct = new CriticalTasks(pts, rf);
		System.out.println(ct.getKeyTasks());
	
	}
	
	@Test
	void testPropertyIncompletePaths() {
		PTSTask pts = new PTSTask();
		pts.addTransition("0", "a", "1");
		pts.addTransition("1", "b", "2");
		pts.addTransition("1", "c", "2");
		pts.addTransition("2", "d", "3");
		pts.setInitState("0");
		pts.addFinalState("3");
		
		System.out.println(pts);
		
		RegularFormula rf = new RegularFormula("r1");
		rf.concatRegular("a", "b");
		
		CriticalTasks ct = new CriticalTasks(pts, rf);
		System.out.println(ct.getKeyTasks());
		
		System.out.println(pts.getPropertyPaths());
		
	}
	
	@Test
	void testPropertyCompletePaths() {
		PTSTask pts = new PTSTask();
		pts.addTransition("0", "a", "1");
		pts.addTransition("1", "b", "2");
		pts.addTransition("1", "c", "3");
		pts.addTransition("2", "d", "3");
		pts.addTransition("2", "e", "3");
		pts.addTransition("0", "f", "3");
		pts.setInitState("0");
		pts.addFinalState("3");
		
		System.out.println(pts);
		
		RegularFormula rf = new RegularFormula("r1");
		rf.concatRegular("a", "b");
		rf.concatRegular("d");
		CriticalTasks ct = new CriticalTasks(pts, rf);
		System.out.println(ct.getKeyTasks());
		System.out.println(pts.getPropertyPaths());
	}
	
	@Test
	void testPropertywithInclusviceGataway() {
		
		
		PTSTask pts = new PTSTask();
		pts.addTransition("0", "taskA", "1");
		pts.addTransition("1", "taskB", "2");
		pts.addTransition("1", "taskC", "2");
		
		pts.addTransition("1", "taskB", "3");
		pts.addTransition("1", "taskC", "4");
		pts.addTransition("3", "taskC", "2");
		pts.addTransition("4", "taskB", "2");
		
		pts.addTransition("2", "taskD", "5");
		pts.setInitState("0");
		pts.addFinalState("5");
		
		RegularFormula rf = new RegularFormula("r1");
		rf.concatRegular("taskA", "taskB");
		rf.concatRegular("taskC");
		
		CriticalTasks ct = new CriticalTasks(pts, rf);
		System.out.println(ct.getKeyTasks());
		System.out.println(pts.getPropertyPaths());
		
		
	}
	
}
