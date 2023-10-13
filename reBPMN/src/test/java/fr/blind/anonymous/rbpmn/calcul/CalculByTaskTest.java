package fr.blind.anonymous.rbpmn.calcul;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;

import fr.blind.anonymous.rbpmn.calcul.CalculByTask;
import fr.blind.anonymous.rbpmn.deploy.BPMNController;
import fr.blind.anonymous.rbpmn.deploy.BPMNProcess;
import fr.blind.anonymous.rbpmn.mysql.DataBaseOp;

class CalculByTaskTest {
	
	@Test
	void testGetAllTasks() {
		
		 CalculByTask calculByInstance = new CalculByTask(new BPMNProcess("ParallelProcess", "ParallelProcess"));
		 List<HistoricActivityInstance> listAllTasks = calculByInstance.getAllTasks();
		 for(HistoricActivityInstance task: listAllTasks) {
			 if(task.getTaskId() != null) {
				 System.out.println(task.getProcessInstanceId() + ", taskname: " + task.getActivityName());
			 }
		 }
		 
	}

	@Test
	void testGetFinishedTasks() {
		CalculByTask calculByInstance = new CalculByTask(new BPMNProcess("ParallelProcess", "ParallelProcess"));
		 List<HistoricActivityInstance> listAllTasks = calculByInstance.getFinishedTasks();
		 for(HistoricActivityInstance task: listAllTasks) {
			 if(task.getTaskId() != null) {
				 System.out.println(task.getProcessInstanceId() + ", taskname: " + task.getActivityName());
			 }
		 }
		 
	}

	@Test
	void testGetUnfinishedTasks() {
		CalculByTask calculByInstance = new CalculByTask(new BPMNProcess("ParallelProcess", "ParallelProcess"));
		 List<HistoricActivityInstance> listAllTasks = calculByInstance.getUnfinishedTasks();
		 for(HistoricActivityInstance task: listAllTasks) {
			 if(task.getTaskId() != null) {
				 System.out.println(task.getProcessInstanceId() + ", taskID: " + task.getId() + ", taskName: " + task.getActivityName());
			 }
		 }
	}
	
	@Test
	void testTasksExtraction() {
		//TODO
		//HistoricActivityInstance, task.getTaskId() != taskService task.getID();
		//The task IDs in the two tables do not match.
		
		DataBaseOp db = new DataBaseOp();
		
		db.dropDatabase();
		db.createDatabase();
		
		String name = "ParallelProcess";
		String key = "ParallelProcess";

		BPMNController bpmn = new BPMNController(name, key);
		bpmn.bpmnDeployment();
		
		bpmn.generateInstances(1);
		
		CalculByTask calculByInstance = new CalculByTask(bpmn.getBpmnProcess());

		List<HistoricActivityInstance> listAllTasks = calculByInstance.getUnfinishedTasks();
		 for(HistoricActivityInstance task: listAllTasks) {
			 if(task.getTaskId() != null) {
				 System.out.println(task.getProcessInstanceId() + ", taskID: " + task.getId() + ", taskName: " + task.getActivityName());
			 }
		 }
		 ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		 TaskService taskService = engine.getTaskService();
		 
		 
		 // listAllTasks = calculByInstance.getUnfinishedTasks();
		 
		 List<Task> tasks = taskService.createTaskQuery().processDefinitionKey(key)
					.orderByTaskCreateTime().asc().list();
		 
		 
		 System.out.println("random start");
		 Random random = new Random(50);
		 for(Task task: tasks) {
			 if(task.getId() != null) {
				 System.out.println(task.getProcessInstanceId() + ", taskID: " + task.getId() + ", taskName: " + task.getName());
				 
//				 if(random.nextDouble() > 0.5) {
					 taskService.complete(task.getId());
//				 }
				 
			 }
		 }
		 System.out.println("random end");
		 
		 listAllTasks = calculByInstance.getUnfinishedTasks();
		 for(HistoricActivityInstance task: listAllTasks) {
			 if(task.getTaskId() != null) {
				 System.out.println(task.getProcessInstanceId() + ", taskID: " + task.getId() + ", taskName: " + task.getActivityName());
			 }
		 }
	}

}
