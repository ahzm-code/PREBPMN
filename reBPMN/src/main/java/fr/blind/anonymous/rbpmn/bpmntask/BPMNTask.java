package fr.blind.anonymous.rbpmn.bpmntask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.task.Task;

public class BPMNTask {
	
	private Task task;
	private String taskId;
	private String taskName;
	private int duration;
	private String instanceId;
	
	
	public BPMNTask(Task task) {
		
		this.task = task;
		this.getTaskInfos();
		
		//this.duration = 0;
		this.taskName = task.getName();
		this.taskId = task.getId();
		
		this.instanceId = task.getProcessInstanceId();
		
	}
	
	public BPMNTask(String taskName) {
		
		this.taskName = taskName;
	}
	
	public BPMNTask(String taskId, String taskName) {
		
		this.taskId = taskId;
		this.taskName = taskName;
	}
	
	public BPMNTask(String taskId, String taskName, int duration) {
		
		this.taskId = taskId;
		this.duration = duration;
		this.taskName = taskName;
	}
	
	public BPMNTask(String taskName, int duration) {
		
		this.duration = duration;
		this.taskName = taskName;
	}
	
	//Resources
//	private Map<String, Integer> taskResources;
//	
//	//Resources
//	public void setTaskResources(Map<String, Integer> taskResources) {
//		this.taskResources = taskResources;
//	}
//
//	//Resources
//	public Map<String, Integer> getTaskResources() {
//		return taskResources;
//	}
//	
	
	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}
	
	public String getId() {
		return task.getId();
		
	}
	
	private void getTaskInfos() {
		
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		FormService formService = engine.getFormService();
		
		if(this.task.getId() != null) {
			TaskFormData taskformdate = formService.getTaskFormData(this.task.getId());
			List<FormProperty> listforms = taskformdate.getFormProperties();
			String durationkey = "duration";
			for (FormProperty form : listforms) {
				if (durationkey.contains(form.getName())) {
					this.duration = Integer.parseInt(form.getValue());
				} 
//				else { //Resources
//					this.taskResources.put(form.getId(), Integer.parseInt(form.getValue()));
//				}

			}
		}
		
	}

	public String getName() {
		return this.taskName;
	}

	@Override
	public String toString() {
		return "BPMNTask ["+ taskId +", " + taskName + ", duration=" + duration + "]";
	}

	public String getInstanceId() {
		return instanceId;
	}
}
