package fr.blind.anonymous.rbpmn.automata;
import java.util.ArrayList;

public class Inequalities {
	
    private ArrayList<Inequality> inequalities;
    private String operator;

	public Inequalities() {
        inequalities = new ArrayList<Inequality>();
	}
	
	public void addInequality(Inequality inequality) {
		inequalities.add(inequality);
	}
	
	public void addOperator(String operator) {
        this.operator = operator;
    }
	
	
	public boolean evaluateAll() {
       
		if(inequalities.size() != 2) {
			 throw new IllegalArgumentException("Invalid size: " + inequalities.size());
		 }else {
			 boolean leftResult = this.evaluate(inequalities.get(0));
	         boolean rightResult = this.evaluate(inequalities.get(1));
	         
	         if (operator.equals("&")) {
	                return leftResult & rightResult;
	            } else if (operator.equals("|")) {
	                return leftResult | rightResult;
	            } else {
	                throw new IllegalArgumentException("Invalid operator: " + operator);
	            }
		 }	 
    }

	 public boolean evaluate(Inequality inequality) {
		 return inequality.evaluate(); 
	}
	    
}
