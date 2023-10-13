package fr.blind.anonymous.rbpmn.property;

import fr.blind.anonymous.rbpmn.automata.DFAchar;
import fr.blind.anonymous.rbpmn.automata.State;
import fr.blind.anonymous.rbpmn.enforcement.Enforcer;
import fr.blind.anonymous.rbpmn.enforcement.EnforcerImpl;

public class FollowedBy {
	
	private DFAchar pfb;
	
	public FollowedBy() {
		
		this.pfb = new DFAchar();
		
		State q1 = new State(1);
        State q2 = new State(2);
        
        pfb.setInitialState(q1);
        pfb.addFinalState(q1);
        pfb.addFinalState(q2);
        
        pfb.addTransition(q1, 'a', q2);
        pfb.addTransition(q2, 'b', q1);
	}
	
	public DFAchar getProperty() {
		return pfb;
	}
	
	public static void main(String[] args) {
        
		FollowedBy p1 = new FollowedBy();
		
		DFAchar pfb = p1.getProperty();
		
        String input1 = "ababab";
        String input2 = "ababbb";
        String input3 = "ababba";
        
        
        String input4 = "ababcbba";
//      System.out.println("Input: " + input1 + " , Accepted: " + pfb.accepts(input1));

        System.out.println("Input: (" + input4.length() + ")" + input4 + ".");
        Enforcer enforcer = new EnforcerImpl(input4, pfb);
//         
        System.out.println(enforcer.enforce());
        System.out.println(enforcer.toString());

	}

}
