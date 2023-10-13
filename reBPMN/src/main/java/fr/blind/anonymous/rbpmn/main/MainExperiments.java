package fr.blind.anonymous.rbpmn.main;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import fr.blind.anonymous.rbpmn.automata.GuardVariable;
import fr.blind.anonymous.rbpmn.automata.Inequality;
import fr.blind.anonymous.rbpmn.automata.PTSTask;
import fr.blind.anonymous.rbpmn.deploy.BPMNController;
import fr.blind.anonymous.rbpmn.enforcement.ProbabilisticEnforcer;
import fr.blind.anonymous.rbpmn.executor.ExecuteRuntimeTasks;
import fr.blind.anonymous.rbpmn.executor.ProbabilisticModelChecking;
import fr.blind.anonymous.rbpmn.mysql.DataBaseOp;
import fr.blind.anonymous.rbpmn.property.RegularFormula;
import fr.blind.anonymous.rbpmn.visualisation.CreateChart;

public class MainExperiments {
	
		public static void main(String agrs[]) {
			
			
			String name = "SimpleInclusive";
			String key = "SimpleInclusive";

			DataBaseOp db = new DataBaseOp();
			db.dropDatabase();
			db.createDatabase();


			BPMNController bpmn = new BPMNController(name, key);
			bpmn.bpmnDeployment();
			
			Map<String, Object> variables = new HashMap<>(); //Variables in BPMN models
			variables.put("num", 10);
			
			bpmn.generateInstances(5, variables);
			
			
			PTSTask pts = new PTSTask();
			pts.addTransition("0", "taskA", "1");
			pts.addTransition("1", "taskB", "2");
			pts.addTransition("1", "taskC", "2");
			
			pts.addTransition("1", "taskB", "3");
			pts.addTransition("1", "taskC", "4");
			pts.addTransition("3", "taskC", "2");
			pts.addTransition("4", "taskB", "2");
			
			pts.addTransition("2", "taskD", "5");
			pts.setInitState("0");
			pts.addFinalState("5");
			
			RegularFormula rf = new RegularFormula("r1");
			rf.concatRegular("taskA", "taskB");
			rf.concatRegular("taskC");
			
			ProbabilisticModelChecking pmc = new ProbabilisticModelChecking(bpmn.getBpmnProcess(), pts, rf, 10);
			
			String epName = rf.getPropertyName();
			GuardVariable enforcerProb = new GuardVariable(epName);
			//with Enfocer
			//Inequality enforcerProperty = new Inequality(enforcerProb, 0.7, "<=");
			// without Enfocer
			Inequality enforcerProperty = new Inequality(enforcerProb, 1.0, "<=");
			
			ProbabilisticEnforcer probEnfocer = new ProbabilisticEnforcer(pmc, enforcerProperty);
			System.out.println(probEnfocer.getVerdictProb());
			
			int period = 10;
			CreateChart createChart;
			try {
				createChart = new CreateChart(bpmn.getBpmnProcess(), probEnfocer, period);
				createChart.showProbChart();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			ExecuteRuntimeTasks executor = new ExecuteRuntimeTasks(bpmn);
			//executor.executorWithoutEnforcer();
			executor.executorWithEnforcer(pmc, enforcerProperty, probEnfocer);
		}
}
