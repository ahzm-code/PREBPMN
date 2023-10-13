package fr.blind.anonymous.rbpmn.deploy;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.junit.jupiter.api.Test;

import fr.blind.anonymous.rbpmn.deploy.BPMNController;

class BPMNProcessTest {

	Random random = new Random(100);

	@Test
	void generatInstancetest() {
		
		
		String name = "SimpleExclusive";
		String key = "SimpleExclusive";

		BPMNController bpmn = new BPMNController(name, key);
		bpmn.bpmnDeployment();
		
		Map<String, Object> variables = new HashMap<>(); //Variables in BPMN models
		variables.put("num", 10);

		bpmn.generateInstances(10, variables);
		System.out.println("\u001B[31m" + variables +"\u001B[0m");
		bpmn.generateInstances(10, variables);

	}
}
