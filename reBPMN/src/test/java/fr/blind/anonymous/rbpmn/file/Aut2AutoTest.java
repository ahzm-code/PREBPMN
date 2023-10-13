package fr.blind.anonymous.rbpmn.file;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import fr.blind.anonymous.rbpmn.file.Aut2Auto;

class Aut2AutoTest {

	@Test
	void testReadFile() {
	
			Aut2Auto a = new Aut2Auto();
			a.readFile("./livraison.aut");
			System.out.println(a.toString());
			
		}
	
	@Test
	void testFinalStates() {
	
			Aut2Auto a = new Aut2Auto();
			a.readFile("./livraison.aut");
			//System.out.println(a.toString());
			System.out.println("finalStates:"+ a.getFinalStates());
			
		}
	
	@Test
	void testPTS() {
	
			Aut2Auto a = new Aut2Auto();
			a.readFile("./livraison.aut");
			//System.out.println(a.toString());
			System.out.println("PTS:"+ a.getInitPTS());
			
		}

}
