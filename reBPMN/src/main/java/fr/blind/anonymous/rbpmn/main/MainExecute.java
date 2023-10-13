package fr.blind.anonymous.rbpmn.main;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;

import fr.blind.anonymous.rbpmn.deploy.BPMNController;
import fr.blind.anonymous.rbpmn.deploy.BPMNProcess;
import fr.blind.anonymous.rbpmn.executor.ExecuteRuntimeTasks;

public class MainExecute {
	
	
	public static void main(String agrs[]) {
		
		
		String name = "ParallelProcess";
		String key = "ParallelProcess";

		BPMNController bpmn = new BPMNController(name, key);
		bpmn.bpmnDeployment();
		
		bpmn.generateInstances(2);
		
		ExecuteRuntimeTasks executor = new ExecuteRuntimeTasks(bpmn);
		executor.executor();
	}
}
