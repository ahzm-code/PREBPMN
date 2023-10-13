package fr.blind.anonymous.rbpmn.property;

import org.junit.jupiter.api.Test;

import fr.blind.anonymous.rbpmn.automata.DFAchar;
import fr.blind.anonymous.rbpmn.enforcement.Enforcer;
import fr.blind.anonymous.rbpmn.enforcement.EnforcerImpl;
import fr.blind.anonymous.rbpmn.property.FollowedBy;

public class FollowedByTest {
	
	
	@Test
	public void testSameInputOutput() {
		FollowedBy p1 = new FollowedBy();
		DFAchar pfb = p1.getProperty();
		
		String input1 = "ababab";
		
		System.out.println("Input(" + input1.length() + "): " + input1 + ".");
        Enforcer enforcer = new EnforcerImpl(input1, pfb);
        
        System.out.println(enforcer.enforce());
        System.out.println(enforcer.toString());
        
	}
	
	
	@Test
	public void testBuffer() {
		FollowedBy p1 = new FollowedBy();
		DFAchar pfb = p1.getProperty();
		
		String input2 = "ababbb";
		
		System.out.println("Input(" + input2.length() + "): " + input2 + ".");
        Enforcer enforcer = new EnforcerImpl(input2, pfb);
        
        System.out.println(enforcer.enforce());
        System.out.println(enforcer.toString());
        
	}
	
	
	@Test
	public void testReorder() {
		
		FollowedBy p1 = new FollowedBy();
		DFAchar pfb = p1.getProperty();
		
		String input3 = "ababba";
		
		System.out.println("Input(" + input3.length() + "): " + input3 + ".");
        Enforcer enforcer = new EnforcerImpl(input3, pfb);
        
        System.out.println(enforcer.enforce());
        System.out.println(enforcer.toString());
        
	}
	
	@Test
	public void testEventNotInAlphabet() {
		FollowedBy p1 = new FollowedBy();
		DFAchar pfb = p1.getProperty();
		
		String input4 = "ababcbba";
		
		System.out.println("Input(" + input4.length() + "):" + input4 + ".");
        Enforcer enforcer = new EnforcerImpl(input4, pfb);
        
        System.out.println(enforcer.enforce());
        System.out.println(enforcer.toString());
        
	}

}
