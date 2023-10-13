package fr.blind.anonymous.rbpmn.enforcement;

import java.util.HashSet;
import java.util.Set;

import fr.blind.anonymous.rbpmn.automata.PTSTask;
import fr.blind.anonymous.rbpmn.property.RegularFormula;

public class CriticalTasks {
	
	private PTSTask pts;
	private RegularFormula rf;
	private Set<String> keyTasks;
	
	public CriticalTasks(PTSTask pts, RegularFormula rf) {
		this.pts = pts;
		this.rf = rf;
		this.keyTasks = new HashSet<>();
	}

	public Set<String> getKeyTasks() {
		this.computeKeyTasks();
		return keyTasks;
	}
	
	
	private void computeKeyTasks() {
		pts.matcheswithPaths(rf.getTaskList(), true);
		this.keyTasks.addAll(pts.getKeyTasks());
	}

}
