package fr.blind.anonymous.rbpmn.calcul;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;

import fr.blind.anonymous.rbpmn.deploy.BPMNProcess;


/**
 * @author ahzm
 * @date: Feb 02, 2023 update: March 16, 2023
 * 
 * Class: CalculByInstance 
 * Description:
 */

public class CalculByInstance {

	private BPMNProcess bpmnProcess;
	
	public CalculByInstance(BPMNProcess bpmnProcess) {
		this.bpmnProcess = bpmnProcess;
	}
	
	public List<HistoricProcessInstance> getFinishedInstances() {
		
		String processKey = this.bpmnProcess.getProcessId();
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		HistoryService hs = engine.getHistoryService();
		
		HistoricProcessInstanceQuery hil = hs.createHistoricProcessInstanceQuery()
				.processDefinitionKey(processKey).finished();
		
		return hil.list();
	}
	
	/**Get the last "number" completed instances*/
	public List<HistoricProcessInstance> getLastFinishedInstances(int number) {
		
		String processKey = this.bpmnProcess.getProcessId();
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		HistoryService hs = engine.getHistoryService();
		
		HistoricProcessInstanceQuery hil = hs.createHistoricProcessInstanceQuery()
				.processDefinitionKey(processKey).finished().orderByProcessInstanceEndTime().desc();
		
		System.out.println("hil size:" + hil.list().size());
		if (hil.list().size() <= number) {
			return hil.list();
		}
		return hil.list().subList(0, number);
		
	}
	
	/**Get the unfinished instances*/
	public List<HistoricProcessInstance> getUnfinishedInstances() {
		
		String processKey = this.bpmnProcess.getProcessId();
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		HistoryService hs = engine.getHistoryService();
		
		HistoricProcessInstanceQuery hil = hs.createHistoricProcessInstanceQuery()
				.processDefinitionKey(processKey).unfinished().orderByProcessInstanceStartTime().asc();
		
		return hil.list();
	}
	
	
	public List<HistoricProcessInstance> getFinishedTaskInstances() {
		
		String processKey = this.bpmnProcess.getProcessId();
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		HistoryService hs = engine.getHistoryService();
		
		HistoricProcessInstanceQuery hisInstances = hs.createHistoricProcessInstanceQuery()
				.processDefinitionKey(processKey).finished().orderByProcessInstanceEndTime().desc();
		
		HistoricTaskInstanceQuery test = hs.createHistoricTaskInstanceQuery().finished();
		
		for(HistoricProcessInstance instance: hisInstances.list()) {
			for(HistoricTaskInstance task :test.processInstanceId(instance.getId()).list()) {
				System.out.println(task.getName());
			}
		}
		
		return hisInstances.list();
	}
	
	/**Get the tasks under a given instance*/
	public List<HistoricTaskInstance> getFinishedTasksByInstance(HistoricProcessInstance instance) {
		
		String processKey = this.bpmnProcess.getProcessId();
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		HistoryService hs = engine.getHistoryService();
		
		HistoricTaskInstanceQuery tasksQuery = hs.createHistoricTaskInstanceQuery().processDefinitionKey(processKey).finished().orderByTaskCreateTime().asc();
		
		String instanceId = instance.getId();
		System.out.println("Instance ID:" + instance.getId());
		List<HistoricTaskInstance> tasks = tasksQuery.processInstanceId(instanceId).list();
		for(HistoricTaskInstance task : tasks) {
			System.out.println(task.getName());
		}
		
		return tasks;
	}
	
	/**Get the list of task names under a given instance*/
	public List<String> getFinishedTaskNamesByInstance(HistoricProcessInstance instance) {
		
		String processKey = this.bpmnProcess.getProcessId();
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		HistoryService hs = engine.getHistoryService();
		
		HistoricTaskInstanceQuery tasksQuery = hs.createHistoricTaskInstanceQuery().processDefinitionKey(processKey).finished().orderByTaskCreateTime().asc();
		
		String instanceId = instance.getId();
		//System.out.println("Instance ID:" + instance.getId());
		List<String> tasksName = new ArrayList<>();
		
		List<HistoricTaskInstance> tasks = tasksQuery.processInstanceId(instanceId).list();
		for(HistoricTaskInstance task : tasks) {
			tasksName.add(task.getName());
		}
		
		return tasksName;
	}
	
	/**Get the list of task names under all last "number" instances*/
	public List<List<String>> getAllLastFinishedTaskNamesByInstance(int number) {
		
		List<List<String>> lastTasksByInstance = new ArrayList<>();
		for(HistoricProcessInstance instance: this.getLastFinishedInstances(number)) {
			lastTasksByInstance.add(this.getFinishedTaskNamesByInstance(instance));
		}
		
		return lastTasksByInstance;
	}
	
	/**get the task names of all completed instances - cumulative  */
	public List<List<String>> getAllFinishedTaskNames() {
		
		List<List<String>> lastTasksByInstance = new ArrayList<>();
		for(HistoricProcessInstance instance: this.getFinishedTaskInstances()) {
			lastTasksByInstance.add(this.getFinishedTaskNamesByInstance(instance));
		}
		
		return lastTasksByInstance;
	}
	
	/**Get all task names from the ongoing instances*/
	public List<List<String>> getAllTaskNamesByUnfinishedInstance() {
		
		List<List<String>> lastTasksByInstance = new ArrayList<>();
		for(HistoricProcessInstance instance: this.getUnfinishedInstances()) {
			//System.out.println("instanceID:" + instance.getId());
			lastTasksByInstance.add(this.getTaskNamesByUnfinishedInstance(instance));
		}
		
		return lastTasksByInstance;
	}
	
	/**Get all task names from the ongoing instances, Except for the instances in buffer.
	  *Set<String> instancesID: It stores the instance ID in the buffer. */
	public List<List<String>> getAllTaskNamesByUnfinishedInstance(Set<String> instancesID) {
		
		List<List<String>> lastTasksByInstance = new ArrayList<>();
		for(HistoricProcessInstance instance: this.getUnfinishedInstances()) {
			if(!instancesID.contains(instance.getId())) {
				//System.out.println(instance.getId());
				lastTasksByInstance.add(this.getTaskNamesByUnfinishedInstance(instance));
			}
		}
		
		return lastTasksByInstance;
	}
	
	/**Get all task names from a ongoing instance*/
	public List<String> getTaskNamesByUnfinishedInstance(HistoricProcessInstance instance) {
		
		String processKey = this.bpmnProcess.getProcessId();
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		HistoryService hs = engine.getHistoryService();
		
		HistoricTaskInstanceQuery tasksQuery = hs.createHistoricTaskInstanceQuery().processDefinitionKey(processKey).orderByTaskCreateTime().asc();
		
		String instanceId = instance.getId();
		//System.out.println("Instance ID:" + instance.getId());
		List<String> tasksName = new ArrayList<>();
		
		List<HistoricTaskInstance> tasks = tasksQuery.processInstanceId(instanceId).list();
		for(HistoricTaskInstance task : tasks) {
			tasksName.add(task.getName());
			//System.out.println(task.getProcessInstanceId());
		}
		
		return tasksName;
	}
	
	/**Get all task names from instance Id set*/
	public List<List<String>> getAllTaskNamesByInstanceId(Set<String> instanceIds) {
		
		List<List<String>> lastTasksByInstance = new ArrayList<>();
		for(String instance: instanceIds) {
			lastTasksByInstance.add(this.getTaskNamesByInstanceId(instance));
			}
		return lastTasksByInstance;
	}
	
	/**Get all task names from instanceId*/
	public List<String> getTaskNamesByInstanceId(String instanceId) {
		
		String processKey = this.bpmnProcess.getProcessId();
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		HistoryService hs = engine.getHistoryService();
		
		HistoricTaskInstanceQuery tasksQuery = hs.createHistoricTaskInstanceQuery().processDefinitionKey(processKey).orderByTaskCreateTime().asc();
		
		//String instanceId = instance.getId();
		//System.out.println("Instance ID:" + instance.getId());
		List<String> tasksName = new ArrayList<>();
		
		List<HistoricTaskInstance> tasks = tasksQuery.processInstanceId(instanceId).list();
		for(HistoricTaskInstance task : tasks) {
			tasksName.add(task.getName());
			//System.out.println(task.getProcessInstanceId());
		}
		
		return tasksName;
	}
	
	
	
}
