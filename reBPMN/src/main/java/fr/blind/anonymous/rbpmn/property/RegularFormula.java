package fr.blind.anonymous.rbpmn.property;

import java.util.ArrayList;
import java.util.List;


// TODO: action formula
// TODO: regularFormila operator (*, +, | ...) 
public class RegularFormula {
	
	private String propertyName;
	private List<String> taskList;
	
	public RegularFormula(String propertyName) {
		this.propertyName = propertyName;
		this.taskList = new ArrayList<>();
	}
	
	public void concatRegular(String leftRegular, String rightRegular) {
		taskList.add(leftRegular);
		taskList.add(rightRegular);
	}
	
	public void concatRegular(String singleRegular) {
		taskList.add(singleRegular);
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public List<String> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<String> taskList) {
		this.taskList = taskList;
	}
	
	
}
