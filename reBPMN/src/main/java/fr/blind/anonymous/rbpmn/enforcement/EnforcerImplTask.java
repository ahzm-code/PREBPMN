package fr.blind.anonymous.rbpmn.enforcement;

import java.util.LinkedList;
import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;

import fr.blind.anonymous.rbpmn.automata.DFATask;
import fr.blind.anonymous.rbpmn.bpmntask.BPMNTask;

public class EnforcerImplTask implements EnforcerBPMN{
	
	
	private Buffer<BPMNTask> buffer;
	private List<BPMNTask> inputTrace;
	private List<BPMNTask> outputTrace;
	private DFATask property;
	
	// Constructor for testing
	public EnforcerImplTask(List<BPMNTask> inputTrace, DFATask property, String s) {

		this.buffer = new Buffer<BPMNTask>();
		this.property = property;
		this.inputTrace = new LinkedList<>(inputTrace);
		this.outputTrace = new LinkedList<>();
		
	}
	
	// Constructor for deployment
	public EnforcerImplTask(List<Task> inputTrace, DFATask property) {

		this.buffer = new Buffer<BPMNTask>();
		this.property = property;
		this.inputTrace = new LinkedList<>();
		this.initInput(inputTrace);
		
		this.outputTrace = new LinkedList<>();
		
	}
	
	
	private void initInput(List<Task> inputTrace) {
		
		for(Task task: inputTrace) {
			this.inputTrace.add(new BPMNTask(task));
		}
	}
	
	@Override
	public List<BPMNTask> enforce() {
		//FIXME optimser
		
		this.outputTrace.clear();
		
		System.out.println("currentState: " + property.getCurrentState().getId());
		
		if(property.accepts(this.inputTrace)) {
			
			this.outputTrace.addAll(this.inputTrace);
			return this.outputTrace;
			
		}else {
			
			List<BPMNTask> initTrace = new LinkedList<>(this.inputTrace);
			
			System.out.println("----- Enforcing Start -----");
			for(int i=0; i< initTrace.size(); i++) {
				
				if(this.property.checkTask(initTrace.get(i))) {
					this.outputTrace.add(initTrace.get(i));
				}else {
					
					if(!this.buffer.contains(initTrace.get(i))) {
						this.buffer.add(initTrace.get(i));
						initTrace.remove(i);
						i--;
					}
					
				}
				
			}
			
		}
		System.out.println("----- Enforcing End -----");
		System.out.println("Input:" + this.inputTrace  + ";\n Buffer:" + buffer.toString() + ";\n Output:" + this.outputTrace);
		
		this.reorder();
		
		return this.outputTrace;
	}
	
//	ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
//	TaskService taskService = engine.getTaskService();
//	List<Task> tasks = taskService.createTaskQuery().processDefinitionKey(this.process.getProcessId()).orderByTaskCreateTime().asc().list();
	
	
	private List<BPMNTask> reorder(){
		
		System.out.println("----- Reordering Start -----");
		System.out.println("-Current Output:-" + this.outputTrace);
		System.out.println("-Current Buffer:-" + this.buffer);
		
		int initBufferSize = buffer.size();
		int initOutputSize = this.outputTrace.size();
		
		int sizeOfBuffer = buffer.size();
		
		while(true) {
			for(int i =0; i< buffer.size(); i++) {
				if(this.property.checkTask(buffer.get(i))) {
					this.outputTrace.add(buffer.get(i));
					buffer.remove(i);
					i--;
				}
			}
			
			if(sizeOfBuffer != buffer.size()) {
				sizeOfBuffer = buffer.size();
			}else {
				break;
			}
		}
		
		System.out.println("----- Reordering End -----");
		System.out.println("-Current Output:-" + outputTrace);
		System.out.println("-Current Buffer:-" + buffer);
		
		System.out.println("Increased length is (OutputTrace): " + (outputTrace.size() - initOutputSize));
		System.out.println("Number of releases is (Buffer):" + (initBufferSize - buffer.size()));
		
		
		return this.outputTrace;
	}
	
	public boolean enforceTask(BPMNTask bpmnTask) {
		return property.checkTask(bpmnTask);
	}
	
	public boolean enforceFinshedTask(BPMNTask bpmnTask) {
		return property.checkTaskandToNext(bpmnTask);
	}

	public List<BPMNTask> getInputTrace() {
		return inputTrace;
	}

	public void setInputTrace(List<BPMNTask> inputTrace) {
		this.inputTrace.clear();
		this.inputTrace.addAll(inputTrace);
	}

	public List<BPMNTask> getOutputTrace() {
		return outputTrace;
	}

	public void setOutputTrace(List<BPMNTask> outputTrace) {
		this.outputTrace = outputTrace;
	}
	
}
