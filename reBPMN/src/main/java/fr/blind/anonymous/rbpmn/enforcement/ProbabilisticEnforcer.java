package fr.blind.anonymous.rbpmn.enforcement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import fr.blind.anonymous.rbpmn.automata.Inequality;
import fr.blind.anonymous.rbpmn.bpmntask.BPMNTask;
import fr.blind.anonymous.rbpmn.executor.ProbabilisticModelChecking;

public class ProbabilisticEnforcer {

	/** Buffer is used to store tasks that may violate the property */
	private Buffer<BPMNTask> buffer;

	/** Execution traces of input */
	private List<BPMNTask> inputTrace;

	/** Execution traces of output */
	private List<BPMNTask> outputTrace;

	/** Key Tasks are influence the property value */
	private Set<String> keyTasks;

	/** Probabilistic Property automaton */
	private Inequality probabilisticProperty;

	/** Probabilistic Model Checker */
	private ProbabilisticModelChecking pmc;

	/** instance ids in buffer */
	private Set<String> instanceId;
	
	/**Verdict true result probability*/
	private double pmcProbability;

	public ProbabilisticEnforcer(ProbabilisticModelChecking pmc, Inequality probabilisticProperty) {

		this.buffer = new Buffer<BPMNTask>();
		//this.inputTrace = new LinkedList<>(inputTrace);
		this.inputTrace = new ArrayList<>();
		this.outputTrace = new ArrayList<>();
		this.keyTasks = new HashSet<>();
		this.keyTasks.addAll(pmc.getKeyTasks());
		System.out.println("Enforcer KeyTasks:" + this.keyTasks);
		this.pmc = pmc;
		this.probabilisticProperty = probabilisticProperty;
		this.instanceId = new HashSet<>();
		
		this.pmcProbability = 0.0;

	}
	
	public List<BPMNTask> enforcerMain(List<BPMNTask> inputTrace){
		
		List<BPMNTask> outputList = new ArrayList<>();
		
		outputList.addAll(this.enforcer(inputTrace));
		
		if(this.buffer.size() == 0) {
			return outputList;
		}
		
		System.out.println("enforcerMain: (outputlist)" + outputList);
		
		return this.filtrage(outputList, this.buffer.size());
	}

	public List<BPMNTask> enforcer(List<BPMNTask> inputTrace) {
		
		System.out.println("enforcer: (inpuTrace)" + inputTrace);
		/** init output trace */
		this.outputTrace.clear();

		/** Get PMC result */
		// double pmcProbResult = pmc.getProbability(); // PMC only with completed
		// instance prob
		//double pmcProbResult = pmc.getOngingProbability(); // PMC with completed & ongoing instances prob
		// FIXME Calculate the probability without considering the list in the buffer
		Set<String> instanceIds = new HashSet<>();
		for(BPMNTask task: inputTrace) {
			instanceIds.add(task.getInstanceId());
		}
		
		double pmcProbResult = pmc.getOngingProbabilityByInstanceIds(instanceIds);
		
		/** Set current Probability */
		probabilisticProperty.getLeftOperand().setValue(pmcProbResult);

		System.out.println("* PMC result: " + probabilisticProperty.evaluate() + "("
				+ probabilisticProperty.getLeftOperand().getValue() + ")");

		if (probabilisticProperty.evaluate()) {
			/** Satisfy the property */
			this.outputTrace.addAll(inputTrace);
			System.out.println("enforcer:(outputTrace)" + this.outputTrace);
			
			/**The value of the probability when the condition is satisfied*/
			this.pmcProbability = pmcProbResult;
			
			return this.outputTrace;

		} else {

			/** Violate the property */
			// Set<String> instIdset = new HashSet<>(); // Instance ids in buffer
			System.out.println("** Violate the property **");
			System.out.println("inputTrace:" + inputTrace);
			
			List<BPMNTask> outputTrace = new ArrayList<>();
			
			for (BPMNTask task : inputTrace) {
				System.out.println("task: " + task.getName() + ", Result:" + keyTasks.contains(task.getName()));
				if (this.keyTasks.contains(task.getName())) {
					// FIXME
					/** Put the task to buffer */
					if (!this.buffer.contains(task)) {
						this.addtoBuffer(task);
						this.instanceId.add(task.getInstanceId());
					}

				} else {

					//this.outputTrace.add(task);
					outputTrace.add(task);
				}

			}
			return outputTrace;
			//return this.enforcer(outputTrace);
		}

		// return this.outputTrace;
		// TODO Verify that the elements in the buffer could come out
		// FIXME
	}
	
	private List<BPMNTask> filtrage(List<BPMNTask> inputTrace, int size){
		
		List<BPMNTask> allInputTrace = new ArrayList<>();
		allInputTrace.addAll(inputTrace);
		allInputTrace.addAll(this.buffer.subList(0, size));
		System.out.println("filtrage (sublist):" + this.buffer.subList(0, size));
		Set<String> instanceIds = new HashSet<>();
		for(BPMNTask task: allInputTrace) {
			instanceIds.add(task.getInstanceId());
		}
		
		double pmcProbResult = pmc.getOngingProbabilityByInstanceIds(instanceIds);
		
		probabilisticProperty.getLeftOperand().setValue(pmcProbResult);

		System.out.println("* PMC result: " + probabilisticProperty.evaluate() + "("
				+ probabilisticProperty.getLeftOperand().getValue() + ")");
		
		if(probabilisticProperty.evaluate()) {
			
			for(int i=0; i< allInputTrace.size(); i++) {
				if(this.buffer.contains(allInputTrace.get(i))) {
					this.removeFromBuffer(allInputTrace.get(i));
				}
			}
			
			/**The value of the probability when the condition is satisfied*/
			this.pmcProbability = pmcProbResult;
//			this.outputTrace.clear();
//			this.outputTrace.addAll(allInputTrace);
//			return this.outputTrace;
			//FIXME
			return this.removeDuplication(allInputTrace);
			//return allInputTrace;
			
		}else {
			
			if(size==0) {
				return inputTrace;
			}
			size = size / 2;
			System.out.println("size: " + size);
			return this.filtrage(inputTrace, size);
		}
	}

	/** Add bpmnTask element to buffer */
	private void addtoBuffer(BPMNTask task) {
		buffer.add(task);
	}

	private void addAlltoBuffer(List<BPMNTask> tasks) {
		buffer.addAll(tasks);
	}

	/** Remove bpmnTask from buffer */
	private void removeFromBuffer(BPMNTask task) {
		buffer.remove(task);
	}

	/** Clear all bpmnTasks in buffer */
	private void purgeBuffer() {
		buffer.clear();
	}

	/** Make it the last to execute the key tasks */
	private List<BPMNTask> reorder(List<BPMNTask> inputTrace) {

		List<BPMNTask> reoderResult = new ArrayList<>();
		List<BPMNTask> keyTaskResult = new ArrayList<>();

		for (BPMNTask task : inputTrace) {
			if (this.keyTasks.contains(task.getName())) {
				keyTaskResult.add(task);
			} else {
				reoderResult.add(task);
			}
		}

		reoderResult.addAll(keyTaskResult);
		return reoderResult;
	}
	
	public List<BPMNTask> getBuffer(){
		return this.buffer.subList(0, this.buffer.size());
	}
	
	public double getVerdictProb() {
		return this.pmcProbability;
	}
	
	//FIXME
	private List<BPMNTask> removeDuplication(List<BPMNTask> list){
		 List<BPMNTask> newList =new ArrayList<>();
		    for (int i=0;i<list.size();i++)
		    {
		        boolean isContains =newList.contains(list.get(i));
		        if(!isContains){
		            newList.add(list.get(i));
		        }
		    }
		    list.clear();
		    list.addAll(newList);
		    return list;
	}
}
