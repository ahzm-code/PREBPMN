package fr.blind.anonymous.rbpmn.enforcement;

import java.util.List;

import fr.blind.anonymous.rbpmn.bpmntask.BPMNTask;

public interface EnforcerBPMN {
	
	List<BPMNTask> enforce();
}
