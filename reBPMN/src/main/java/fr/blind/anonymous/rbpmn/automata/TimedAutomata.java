package fr.blind.anonymous.rbpmn.automata;

import java.util.Map;
import java.util.Set;

/**
 * DFA类中使用了:
   * 一个Map来存储转移关系，
   * 一个Set来存储所有状态，
   * 一个State来表示初始状态，
   * 一个Set来存储终止状态，
   * 一个Set来存储所有事件。 
 * accepts方法接受一个输入字符串并返回true/false表示输入
 * */

public class TimedAutomata {
	
	//TODO
	
	private Map<State, Map<String, State>> transitions;
	private Set<State> states;
	private State initialState;
	private Set<State> finalStates;
	private Set<String> alphabets;
	private State currentState;
	
	
}
