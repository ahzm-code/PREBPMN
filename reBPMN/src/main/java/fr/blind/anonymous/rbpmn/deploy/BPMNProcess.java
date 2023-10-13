package fr.blind.anonymous.rbpmn.deploy;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;

public class BPMNProcess {

	private String processId;
	private String bpmnName;
	private String processDefinitionId;
	private Random random;

	public BPMNProcess(String processId, String bpmnName) {

		super();
		this.processId = processId;
		this.bpmnName = bpmnName;
		
		this.random = new Random(50); //random for exclusive gateway
	}

	public void Deployment() {

		System.out.println("Deployment: " + this.getBpmnName() + ".bpmn; ID: " + this.getProcessId());

		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		RepositoryService service = engine.getRepositoryService();
		Deployment deploy = service.createDeployment().addClasspathResource("BPMN/" + this.getBpmnName() + ".bpmn")
				.name(this.getProcessId()).deploy();
		System.out.println(" -- Deployment Success -- ");
		System.out.println("Process Id: " + deploy.getId() + "; Process Name: " + deploy.getName());
		
		// //Error: Automatic generation of an instance
		//this.InitDefinitionId();
		//System.out.println("definitionId:" + this.processDefinitionId);
		System.out.println(" -- Deployment End -- ");
	}
	
	// get process definition ID, which is real processID in DataBase
//	private void InitDefinitionId() {
//		//FIXME Errors are generated when variables are required
//		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
//		RuntimeService runtimeService = engine.getRuntimeService();
//		ProcessInstance processInstance =
//				runtimeService.startProcessInstanceByKey(this.processId);
//		
//		this.processDefinitionId = processInstance.getProcessDefinitionId();
//	}
	
	// Create instances without variables
	public void generateInstances(int nbraInstance) {
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		RuntimeService runtimeService = engine.getRuntimeService();
		
		String key = this.getProcessId();

		for (int i = 1; i <= nbraInstance; i++) {

			ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(key);
			
			//Real Process Definition ID in Database
			this.processDefinitionId = processInstance.getProcessDefinitionId();
//			System.out.println("Process de finition ID：" + processInstance.getProcessDefinitionId());
//			System.out.println("Process Instance ID：" + processInstance.getId());
		}
		
	}
	
	
	// Create instances with variables
		public void generateInstances(int nbrInstance, Map<String, Object> bpmnVariables) {

			ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
			RuntimeService runtimeService = engine.getRuntimeService();

			Map<String, Object> variables = new HashMap<>();
			
			variables.putAll(bpmnVariables);
			
			System.out.println("- Generate -:");
			
			String key = this.getProcessId();
			int random_number;
			for (int i = 1; i <= nbrInstance; i++) {

				System.out.println("-" + i + "-:");
				
				for (String v_key: variables.keySet()) {
					
					//random_number = 1 + random.nextInt(10);
					random_number = 1 + random.nextInt((int)bpmnVariables.get(v_key));
					
					variables.put(v_key, random_number);
					System.out.println(v_key + ", random:" + random_number);
					System.out.println(variables);
				}
				
				ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(key, variables);
				//Real Process Definition ID in Database
				this.processDefinitionId = processInstance.getProcessDefinitionId();
				System.out.println("Process de finition ID：" + processInstance.getProcessDefinitionId());
				System.out.println("Process Instance ID：" + processInstance.getId());
			}
		}
	
	

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getBpmnName() {
		return bpmnName;
	}

	public void setBpmnName(String bpmnName) {
		this.bpmnName = bpmnName;
	}

	@Override
	public String toString() {
		return "BPMNProcess [processId=" + processId + ", bpmnName=" + bpmnName + "]";
	}

	public String getProcessDefinitionId() {
		return processDefinitionId;
	}

	public void setProcessDefinitionId(String processDefinitionId) {
		this.processDefinitionId = processDefinitionId;
	}

}
