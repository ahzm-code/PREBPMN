package fr.blind.anonymous.rbpmn.calcul;

import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;

import fr.blind.anonymous.rbpmn.deploy.BPMNProcess;

public class CalculByTask {
	
	private BPMNProcess bpmnProcess;
	
	public CalculByTask(BPMNProcess bpmnProcess) {
		this.bpmnProcess = bpmnProcess;
	}
	
	public List<HistoricActivityInstance> getAllTasks() {
		
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		HistoryService historyService = engine.getHistoryService();
		
		// DataBase: actinst table
		HistoricActivityInstanceQuery instanceQuery =
				historyService.createHistoricActivityInstanceQuery()
					.processDefinitionId(this.bpmnProcess.getProcessDefinitionId())
					.orderByHistoricActivityInstanceStartTime().asc();
					//.orderByHistoricActivityInstanceStartTime().desc();
				
		System.out.println(instanceQuery.list());
		return instanceQuery.list();
	}
	
	
	public List<HistoricActivityInstance> getFinishedTasks() {
		
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		HistoryService historyService = engine.getHistoryService();
		
		// DataBase: actinst table
		HistoricActivityInstanceQuery instanceQuery =
				historyService.createHistoricActivityInstanceQuery();
		
		instanceQuery.processDefinitionId(this.bpmnProcess.getProcessDefinitionId());
		instanceQuery.orderByHistoricActivityInstanceStartTime().asc();
		//instanceQuery.orderByHistoricActivityInstanceStartTime().desc();
				
		//List<HistoricActivityInstance> list = instanceQuery.list();
		return instanceQuery.finished().list();
	}
	
	public List<HistoricActivityInstance> getUnfinishedTasks() {
		
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		HistoryService historyService = engine.getHistoryService();
		
		// DataBase: actinst table
		HistoricActivityInstanceQuery instanceQuery =
				historyService.createHistoricActivityInstanceQuery();
		
		instanceQuery.processDefinitionId(this.bpmnProcess.getProcessDefinitionId());
		instanceQuery.orderByHistoricActivityInstanceStartTime().asc();
		//instanceQuery.orderByHistoricActivityInstanceStartTime().desc();
				
		//List<HistoricActivityInstance> list = instanceQuery.list();
		return instanceQuery.unfinished().list();
	}
	
}
