package fr.blind.anonymous.rbpmn.executor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;

import fr.blind.anonymous.rbpmn.automata.DFATask;
import fr.blind.anonymous.rbpmn.automata.Inequality;
import fr.blind.anonymous.rbpmn.automata.State;
import fr.blind.anonymous.rbpmn.bpmntask.BPMNTask;
import fr.blind.anonymous.rbpmn.deploy.BPMNController;
import fr.blind.anonymous.rbpmn.deploy.BPMNProcess;
import fr.blind.anonymous.rbpmn.enforcement.EnforcerBPMN;
import fr.blind.anonymous.rbpmn.enforcement.EnforcerImplTask;
import fr.blind.anonymous.rbpmn.enforcement.ProbabilisticEnforcer;
import fr.blind.anonymous.rbpmn.property.PropertyFBY;

public class ExecuteRuntimeTasks {

	private BPMNProcess process;
//	private BPMNResources resources;

	public ExecuteRuntimeTasks(BPMNController bpmnProcess) {

		this.process = bpmnProcess.getBpmnProcess();
	}

//	public ExecuteRuntimeTasks(BPMNController bpmnProcess, BPMNResources resources) {
//		
//		this.process = bpmnProcess.getBpmnProcess();
//		this.resources = resources;
//	}

	// FIXME When there is no execution time for a task, it is run directly.
	public void executor() {

		// TODO
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		TaskService taskService = engine.getTaskService();

		List<Task> tasks = taskService.createTaskQuery().processDefinitionKey(this.process.getProcessId())
				.orderByTaskCreateTime().asc().list();

		System.out.println(tasks);

		List<String> tasksQueue = new ArrayList<>();
		List<BPMNTask> bpmnTasksQueue = new ArrayList<>(); // Awaiting tasks
		ConcurrentMap<BPMNTask, Integer> runningTasks = new ConcurrentHashMap<>(); // Running tasks

		// init enforcer
		PropertyFBY pfby = new PropertyFBY();
		EnforcerImplTask enforcer = new EnforcerImplTask(bpmnTasksQueue, pfby.getDFATask(), "");

		Random num = new Random(50);
		do {

			// System.out.println("Tasks:" + tasks);
			if (tasks.isEmpty()) {

				System.out.println("Tasks is empty:" + tasks);

				this.process.generateInstances(1 + num.nextInt(3));
				tasks = taskService.createTaskQuery().processDefinitionKey(this.process.getProcessId())
						.orderByTaskCreateTime().asc().list();

				System.out.println("random number:" + num.nextDouble());
				if (num.nextDouble() < 0.7) {
					Collections.shuffle(tasks);

					// Collections.shuffle(tasks, new Random(50));
				}

			}
			System.out.println("Tasks:" + tasks);

			for (Task task : tasks) {

				BPMNTask bpmnTask = new BPMNTask(task);

				if (!tasksQueue.contains(bpmnTask.getId())) {

					bpmnTasksQueue.add(bpmnTask);

					tasksQueue.add(bpmnTask.getId());

				}
			}

			// Enforcer
			enforcer.setInputTrace(bpmnTasksQueue);
			System.out.println("enforcerInput:" + enforcer.getInputTrace());

			enforcer.enforce();

			List<BPMNTask> enforceOutput = new ArrayList<>();
			enforceOutput.addAll(enforcer.getOutputTrace());
			System.out.println("enforcerOut:" + enforceOutput);

			for (BPMNTask bpmnTask : enforceOutput) {
				if (enforcer.enforceTask(bpmnTask)) {

					runningTasks.put(bpmnTask, bpmnTask.getDuration());

					bpmnTasksQueue.remove(bpmnTask);

					tasksQueue.remove(bpmnTask.getId());

					break;
				}
			}
			enforcer.setInputTrace(bpmnTasksQueue);

			boolean flag = true;
			do {
				try {
					Thread.sleep(1000);

				} catch (Exception e) {
					System.exit(0); // exit
				}

				for (BPMNTask runingtaskid : runningTasks.keySet()) {
					if (runningTasks.get(runingtaskid) - 1 == 0) {
						tasksQueue.remove(runingtaskid.getId());
						runningTasks.remove(runingtaskid);

						enforcer.enforceFinshedTask(runingtaskid);
						// 1. Complete tasks
						// taskController.executeCourrentTask(runingtaskid);
						taskService.complete(runingtaskid.getId());
						// TODO Add resource attributes
						// 2. release resources
						// taskController.putResource(runingtaskid);
						flag = false;

					} else {
						int old_value = runningTasks.get(runingtaskid);
						runningTasks.put(runingtaskid, old_value - 1);
					}
				}

			} while (flag);

			tasks = taskService.createTaskQuery().processDefinitionKey(this.process.getProcessId())
					.orderByTaskCreateTime().asc().list();

		} while (true);
	}

	// Method for testing, direct execution of tasks without any Enforcer
	public void executorWithoutEnforcer() {
		// TODO Optimizer
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		TaskService taskService = engine.getTaskService();

		List<Task> tasks = taskService.createTaskQuery().processDefinitionKey(this.process.getProcessId())
				.orderByTaskCreateTime().asc().list();

		System.out.println(tasks);
		do {

			// System.out.println("Tasks:" + tasks);
			if (tasks.isEmpty()) {

				System.out.println("Tasks is empty:" + tasks);
				break;
			}
			System.out.println("Tasks:" + tasks);

			for (Task task : tasks) {
				taskService.complete(task.getId());
			}

			tasks = taskService.createTaskQuery().processDefinitionKey(this.process.getProcessId())
					.orderByTaskCreateTime().asc().list();

		} while (true);
	}

	// executor with enforcer
	public void executorWithEnforcer(ProbabilisticModelChecking pmc, Inequality probabilisticProperty,
			ProbabilisticEnforcer probEnfocer) {
		// TODO Optimizer
		ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
		TaskService taskService = engine.getTaskService();

		List<Task> tasks = taskService.createTaskQuery().processDefinitionKey(this.process.getProcessId())
				.orderByTaskCreateTime().asc().list();

		System.out.println(tasks);

		List<String> tasksQueue = new ArrayList<>();
		List<BPMNTask> bpmnTasksQueue = new ArrayList<>(); // Awaiting tasks

		// ProbabilisticEnforcer probEnfocer = new ProbabilisticEnforcer(pmc,
		// probabilisticProperty);

		// FIXME
		Map<String, Object> variables = new HashMap<>(); // Variables in BPMN models
		variables.put("num", 10);
		// FIXME end

		do {

			// System.out.println("Tasks:" + tasks);
			if (tasks.isEmpty()) {
				// FIXME see TODO line 229
				System.out.println("Tasks is empty:" + tasks);
				break;
			}

			System.out.println("Tasks:" + tasks);

			for (Task task : tasks) {
				if (!tasksQueue.contains(task.getId())) {
					tasksQueue.add(task.getId());
					bpmnTasksQueue.add(new BPMNTask(task));
				}

			}

			System.out.println("bpmnTasksQueue:" + bpmnTasksQueue);

			// TODO The pending task exists in the buffer.
			// It can be used as a condition for generating new instances
			System.out.println("buffer, task:" + probEnfocer.getBuffer().equals(bpmnTasksQueue));

			List<BPMNTask> enforcerResult = new ArrayList<>();

			enforcerResult.addAll(probEnfocer.enforcerMain(bpmnTasksQueue));

			// enforcerResult.addAll(probEnfocer.enforcer(bpmnTasksQueue));

//			for (Task task : tasks) {
//				taskService.complete(task.getId());
//			}
			System.out.println("********************************");
			System.out.println("enforcerResult:" + enforcerResult);
			for (BPMNTask task : enforcerResult) {
				System.out.println("taksId:" + task.getId());
				taskService.complete(task.getId());
				tasksQueue.remove(task.getId());
				bpmnTasksQueue.remove(task);

				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}

			System.out.println("buffer, task:" + probEnfocer.getBuffer().equals(bpmnTasksQueue));
			boolean bufferflag = probEnfocer.getBuffer().equals(bpmnTasksQueue);
			if (bufferflag) {
				this.process.generateInstances(10, variables);
			}

			tasks = taskService.createTaskQuery().processDefinitionKey(this.process.getProcessId())
					.orderByTaskCreateTime().asc().list();

		} while (true);
	}
	
	
	// executor with enforcer
		public void executorWithEnforcer(ProbabilisticModelChecking pmc, Inequality probabilisticProperty,
				ProbabilisticEnforcer probEnfocer, Map<String, Object> variablesProcess) {
			// TODO Optimizer
			ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
			TaskService taskService = engine.getTaskService();

			List<Task> tasks = taskService.createTaskQuery().processDefinitionKey(this.process.getProcessId())
					.orderByTaskCreateTime().asc().list();

			System.out.println(tasks);

			List<String> tasksQueue = new ArrayList<>();
			List<BPMNTask> bpmnTasksQueue = new ArrayList<>(); // Awaiting tasks

			// ProbabilisticEnforcer probEnfocer = new ProbabilisticEnforcer(pmc,
			// probabilisticProperty);

			// FIXME
			Map<String, Object> variables = new HashMap<>(); // Variables in BPMN models
			//variables.put("num", 10);
			variables.putAll(variablesProcess);
			// FIXME end

			do {

				// System.out.println("Tasks:" + tasks);
				if (tasks.isEmpty()) {
					// FIXME see TODO line 229
					System.out.println("Tasks is empty:" + tasks);
					break;
				}

				System.out.println("Tasks:" + tasks);

				for (Task task : tasks) {
					if (!tasksQueue.contains(task.getId())) {
						tasksQueue.add(task.getId());
						bpmnTasksQueue.add(new BPMNTask(task));
					}

				}

				System.out.println("bpmnTasksQueue:" + bpmnTasksQueue);

				// TODO The pending task exists in the buffer.
				// It can be used as a condition for generating new instances
				System.out.println("buffer, task:" + probEnfocer.getBuffer().equals(bpmnTasksQueue));

				List<BPMNTask> enforcerResult = new ArrayList<>();

				enforcerResult.addAll(probEnfocer.enforcerMain(bpmnTasksQueue));

				// enforcerResult.addAll(probEnfocer.enforcer(bpmnTasksQueue));

//				for (Task task : tasks) {
//					taskService.complete(task.getId());
//				}
				System.out.println("********************************");
				System.out.println("enforcerResult:" + enforcerResult);
				for (BPMNTask task : enforcerResult) {
					System.out.println("taksId:" + task.getId());
					taskService.complete(task.getId());
					tasksQueue.remove(task.getId());
					bpmnTasksQueue.remove(task);

					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}

				System.out.println("buffer, task:" + probEnfocer.getBuffer().equals(bpmnTasksQueue));
				boolean bufferflag = probEnfocer.getBuffer().equals(bpmnTasksQueue);
				if (bufferflag) {
					this.process.generateInstances(10, variables);
				}

				tasks = taskService.createTaskQuery().processDefinitionKey(this.process.getProcessId())
						.orderByTaskCreateTime().asc().list();

			} while (true);
		}
}
