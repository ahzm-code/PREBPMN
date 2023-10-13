package fr.blind.anonymous.rbpmn.mysql;

import static org.junit.Assert.assertTrue;
import org.junit.jupiter.api.Test;

import fr.blind.anonymous.rbpmn.mysql.DataBaseOp;


public class DataBaseOpTest {
	
	@Test
	public void dropDatabase() {
		DataBaseOp db = new DataBaseOp();
		assertTrue(db.dropDatabase());
		
	}
	
	@Test
	public void createDatabase() {
		DataBaseOp db = new DataBaseOp();
		assertTrue(db.createDatabase());
	}
}
