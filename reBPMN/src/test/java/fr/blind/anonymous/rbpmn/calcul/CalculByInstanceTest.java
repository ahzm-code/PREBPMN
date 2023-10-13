package fr.blind.anonymous.rbpmn.calcul;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.List;
import java.util.HashSet;
import java.util.Set;

import org.activiti.engine.history.HistoricProcessInstance;
import org.junit.jupiter.api.Test;

import fr.blind.anonymous.rbpmn.automata.PTSTask;
import fr.blind.anonymous.rbpmn.calcul.CalculByInstance;
import fr.blind.anonymous.rbpmn.deploy.BPMNProcess;

class CalculByInstanceTest {

	@Test
	void testGetFinishedInstances() {
		
		CalculByInstance calculByInstance = new CalculByInstance(new BPMNProcess("ParallelProcess", "ParallelProcess"));
		
		//List<HistoricProcessInstance> hisInstances;
		for(HistoricProcessInstance hisInstance: calculByInstance.getFinishedInstances()){
			System.out.println(hisInstance.getId());
		}
		
	}
	
	@Test
	void testGetLastFinishedInstances() {
		
		CalculByInstance calculByInstance = new CalculByInstance(new BPMNProcess("ParallelProcess", "ParallelProcess"));
		
		//List<HistoricProcessInstance> hisInstances;
		for(HistoricProcessInstance hisInstance: calculByInstance.getLastFinishedInstances(10)){
			System.out.println(hisInstance.getId());
			System.out.println(hisInstance.getEndTime());
		}
	}
	
	
	@Test
	void testGetLastFinishedInstances2() {
		
		CalculByInstance calculByInstance = new CalculByInstance(new BPMNProcess("ParallelProcess", "ParallelProcess"));
		
		//List<HistoricProcessInstance> hisInstances;
		for(HistoricProcessInstance hisInstance: calculByInstance.getFinishedTaskInstances()){
			System.out.println(hisInstance.getId());
			System.out.println(calculByInstance.getFinishedTasksByInstance(hisInstance));
		}
	}
	
	@Test
	void testGetFinishedTaskNamesByInstance() {
		
		CalculByInstance calculByInstance = new CalculByInstance(new BPMNProcess("ParallelProcess", "ParallelProcess"));
		//List<HistoricProcessInstance> hisInstances;
		for(HistoricProcessInstance hisInstance: calculByInstance.getFinishedTaskInstances()){
			System.out.println(hisInstance.getId());
			System.out.println(calculByInstance.getFinishedTaskNamesByInstance(hisInstance));
		}
	}
	
	@Test
	void testGetFinishedTaskNamesByInstance2() {
		
		CalculByInstance calculByInstance = new CalculByInstance(new BPMNProcess("SimpleExclusive", "SimpleExclusive"));
		//List<HistoricProcessInstance> hisInstances;
		for(HistoricProcessInstance hisInstance: calculByInstance.getFinishedTaskInstances()){
			System.out.println(hisInstance.getId() + " - " + hisInstance.getEndTime());
			System.out.println(calculByInstance.getFinishedTaskNamesByInstance(hisInstance));
		}
	}
	
	@Test
	void testGetLastFinishedInstancesWithNumber() {
		
		CalculByInstance calculByInstance = new CalculByInstance(new BPMNProcess("SimpleExclusive", "SimpleExclusive"));
		
		//List<HistoricProcessInstance> hisInstances;
		for(HistoricProcessInstance hisInstance: calculByInstance.getLastFinishedInstances(10)){
			System.out.println(hisInstance.getId() + " - "+ hisInstance.getEndTime().getTime());
			System.out.println(calculByInstance.getFinishedTaskNamesByInstance(hisInstance));
		}
	}
	
	@Test
	void testGetAllLastFinishedTaskNamesWithNumber() {
		
		CalculByInstance calculByInstance = new CalculByInstance(new BPMNProcess("SimpleExclusive", "SimpleExclusive"));
		
		System.out.println(calculByInstance.getAllLastFinishedTaskNamesByInstance(10));
		assertEquals(10, calculByInstance.getAllLastFinishedTaskNamesByInstance(10).size());
	}
	
	@Test
	void testGetAllLastFinishedTaskNamesWithNumber2() {
		
		CalculByInstance calculByInstance = new CalculByInstance(new BPMNProcess("SimpleExclusive", "SimpleExclusive"));
		assertEquals(20, calculByInstance.getAllLastFinishedTaskNamesByInstance(20).size());
	}
	
	@Test
	void testGetAllLastFinishedTaskNameswithPTS() {
		
		PTSTask pts = new PTSTask();
		pts.addTransition("0", "taskA", "1");
		pts.addTransition("1", "taskB", "2");
		pts.addTransition("1", "taskC", "2");
		pts.setInitState("0");
		pts.addFinalState("2");
		//System.out.println("Matches 'a'? " + pts.matcheswithPaths(input));
		
		CalculByInstance calculByInstance = new CalculByInstance(new BPMNProcess("SimpleExclusive", "SimpleExclusive"));
		for(java.util.List<String> instanceTasks : calculByInstance.getAllLastFinishedTaskNamesByInstance(30)) {
				System.out.println("Matches 'a'? " + pts.matcheswithPaths(instanceTasks));
				pts.printCounter();
		}
	}
	
	
	@Test
	/**Ongoing instance test*/
	void testGetAllTaskNamesByUnfinishedInstance() {
		
		CalculByInstance calculByInstance = new CalculByInstance(new BPMNProcess("SimpleExclusive", "SimpleExclusive"));
		
		for(HistoricProcessInstance unfinishedInstance : calculByInstance.getUnfinishedInstances()) {
			System.out.println(calculByInstance.getTaskNamesByUnfinishedInstance(unfinishedInstance));
		}
		
	}
	
	@Test
	/**Ongoing instances test, without the instances in buffer*/
	void testGetAllTaskNamesExceptInstancesInBuffer() {
		
		CalculByInstance calculByInstance = new CalculByInstance(new BPMNProcess("SimpleExclusive", "SimpleExclusive"));
		Set<String> instanceID = new HashSet<>();
		instanceID.add("5");
		
		for(HistoricProcessInstance unfinishedInstance : calculByInstance.getUnfinishedInstances()) {
			System.out.println(calculByInstance.getTaskNamesByUnfinishedInstance(unfinishedInstance));
		}
		
		System.out.println(calculByInstance.getAllTaskNamesByUnfinishedInstance(instanceID));
	}

}
