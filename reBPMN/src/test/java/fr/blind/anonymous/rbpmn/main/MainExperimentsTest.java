package fr.blind.anonymous.rbpmn.main;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import fr.blind.anonymous.rbpmn.automata.GuardVariable;
import fr.blind.anonymous.rbpmn.automata.Inequality;
import fr.blind.anonymous.rbpmn.automata.PTSTask;
import fr.blind.anonymous.rbpmn.deploy.BPMNController;
import fr.blind.anonymous.rbpmn.enforcement.ProbabilisticEnforcer;
import fr.blind.anonymous.rbpmn.executor.ExecuteRuntimeTasks;
import fr.blind.anonymous.rbpmn.executor.ProbabilisticModelChecking;
import fr.blind.anonymous.rbpmn.file.Aut2Auto;
import fr.blind.anonymous.rbpmn.mysql.DataBaseOp;
import fr.blind.anonymous.rbpmn.property.RegularFormula;
import fr.blind.anonymous.rbpmn.visualisation.CreateChart;

class MainExperimentsTest {

	public static void main(String args[]) {
		
		String name = "livraison2";
		String key = "livraison2";

		DataBaseOp db = new DataBaseOp();
		db.dropDatabase();
		db.createDatabase();


		BPMNController bpmn = new BPMNController(name, key);
		bpmn.bpmnDeployment();
		
		Map<String, Object> variables = new HashMap<>(); //Variables in BPMN models
		variables.put("modenum", 10);
		variables.put("inclunum", 10);
		
		bpmn.generateInstances(5, variables);
		
		
		
		Aut2Auto aut = new Aut2Auto();
		aut.readFile("livraison.aut");
		PTSTask pts = aut.getInitPTS();
		System.out.print(pts);
		
		RegularFormula rf = new RegularFormula("r1");
		rf.concatRegular("USERTASK1", "USERTASK3");
		//rf.concatRegular("USERTASK4");
		
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
		executor.executorWithEnforcer(pmc, enforcerProperty, probEnfocer, variables);
	}
}



