package fr.blind.anonymous.rbpmn.executor;

import java.util.List;
import java.util.Set;

import fr.blind.anonymous.rbpmn.automata.PTSTask;
import fr.blind.anonymous.rbpmn.calcul.CalculByInstance;
import fr.blind.anonymous.rbpmn.deploy.BPMNProcess;
import fr.blind.anonymous.rbpmn.enforcement.CriticalTasks;
import fr.blind.anonymous.rbpmn.property.RegularFormula;

public class ProbabilisticModelChecking {

	private BPMNProcess bpmnProcess;
	private PTSTask pts;
	private RegularFormula rfProperty;
	private CriticalTasks ct;
	private CalculByInstance calculByInstance;
	private int lastNbrInstance;

	public ProbabilisticModelChecking(BPMNProcess bpmnProcess, PTSTask pts, RegularFormula rfProperty, int lastNbrInstance) {

		this.bpmnProcess = bpmnProcess;
		this.pts = pts;

		this.rfProperty = rfProperty;
		this.ct = new CriticalTasks(pts, rfProperty);
		this.ct.getKeyTasks();

		this.calculByInstance = new CalculByInstance(this.bpmnProcess);

		this.lastNbrInstance = lastNbrInstance;

	}
	
	public ProbabilisticModelChecking(BPMNProcess bpmnProcess, PTSTask pts, RegularFormula rfProperty) {

		this.bpmnProcess = bpmnProcess;
		this.pts = pts;

		this.rfProperty = rfProperty;
		this.ct = new CriticalTasks(pts, rfProperty);
		this.ct.getKeyTasks();

		this.calculByInstance = new CalculByInstance(this.bpmnProcess);
		
	}
	
	public double getProbability() {
		return this.computePropertyProbability();
	}
	
	/**Get property probability with ongoing instances*/
	public double getOngingProbability() {
		this.computePropertyProbability();
		return this.computePropertyProbabilityWithOngingInstances();
	}
	
	/**Get property probability with ongoing instances by instance Ids*/
	public double getOngingProbabilityByInstanceIds(Set<String> instanceIds) {
		this.computePropertyProbability();
		return this.computePropertyProbabilityWithOngingInstancesId(instanceIds);
	}
	
	public double getCumulProbability() {
		return this.computeCumulPropertyProbability();
	}
	
	public Set<String> getKeyTasks(){
		return this.computeKeyTasks();
	}
	
	private Set<String> computeKeyTasks(){
		return this.pts.getKeyTasks();
	}
	
	private double computePropertyProbability() {

		this.pts.initProbability();
		this.ct.getKeyTasks();

		for (List<String> instanceTasks : calculByInstance
				.getAllLastFinishedTaskNamesByInstance(this.lastNbrInstance)) {
			// System.out.println("Matches "+ instanceTasks +"?" +
			// pts.matcheswithPaths(instanceTasks));
			pts.matcheswithPaths(instanceTasks);
			pts.printCounter();
		}
		return pts.getPropertyProb();
	}
	
	/**Cumulative probability - Get the task names of all completed instances*/
	private double computeCumulPropertyProbability() {

		this.pts.initProbability();
		this.ct.getKeyTasks();

		for (List<String> instanceTasks : calculByInstance.getAllFinishedTaskNames()) {
			// System.out.println("Matches "+ instanceTasks +"?" +
			// pts.matcheswithPaths(instanceTasks));
			pts.matcheswithPaths(instanceTasks);
			pts.printCounter();
		}
		return pts.getPropertyProb();
	}
	
	/**Property probability with ongoing instances*/
	private double computePropertyProbabilityWithOngingInstances() {

		for (List<String> instanceTasks : calculByInstance.getAllTaskNamesByUnfinishedInstance()) {
			// System.out.println("Matches "+ instanceTasks +"?" +
			// pts.matcheswithPaths(instanceTasks));
			pts.matcheswithPaths(instanceTasks);
			pts.printCounter();
		}
		return pts.getPropertyProb();
	}
	
	/**Property probability with ongoing instances by instances Id*/
	private double computePropertyProbabilityWithOngingInstancesId(Set<String> instanceIds) {
		
//		System.out.println("Instances size:" + instanceIds.size());
		for (List<String> instanceTasks : calculByInstance.getAllTaskNamesByInstanceId(instanceIds)) {
			// System.out.println("Matches "+ instanceTasks +"?" +
			// pts.matcheswithPaths(instanceTasks));
			pts.matcheswithPaths(instanceTasks);
			pts.printCounter();
		
		}

		return pts.getPropertyProb();
	}

}
