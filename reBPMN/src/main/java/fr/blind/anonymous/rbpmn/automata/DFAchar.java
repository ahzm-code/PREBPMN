package fr.blind.anonymous.rbpmn.automata;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.blind.anonymous.rbpmn.automata.Event;

/**
 * DFA类中使用了:
   * 一个Map来存储转移关系，
   * 一个Set来存储所有状态，
   * 一个State来表示初始状态，
   * 一个Set来存储终止状态，
   * 一个Set来存储所有事件。 
 * accepts方法接受一个输入字符串并返回true/false表示输入
 * */

public class DFAchar {
	
		private Map<State, Map<Character, State>> transitions;
		private Set<State> states;
		private State initialState;
		private Set<State> finalStates;
		private Set<Character> alphabets;
		private State currentState;

		public DFAchar() {
			transitions = new HashMap<>();
			states = new HashSet<>();
			finalStates = new HashSet<>();
			alphabets = new HashSet<>(); 
		}

		public void addTransition(State from, Character on, State to) {
			if (!states.contains(from)) {
				states.add(from);
			}
			if (!states.contains(to)) {
				states.add(to);
			}
			
			if(!alphabets.contains(on)) {
				alphabets.add(on);
			}

			Map<Character, State> map = transitions.get(from);
			if (map == null) {
				map = new HashMap<>();
				transitions.put(from, map);
			}
			map.put(on, to);
		}

		public void setInitialState(State initialState) {
			this.initialState = initialState;
			this.currentState = initialState;
		}

		public void addFinalState(State state) {
			finalStates.add(state);
		}
		
		public boolean checkEvent(Character on){
			return alphabets.contains(on);
		}
		
		public boolean accepts(String input) {
			
			State currentState = initialState;
			
			for (int i = 0; i < input.length(); i++) {
				
				if(!alphabets.contains(input.charAt(i))) {
					continue;
				}
				
				Map<Character, State> map = transitions.get(currentState);
				
				if (map == null) {
					//System.out.println("i:" + i);
					return false;
				}
				
				currentState = map.get(input.charAt(i));
				
				if (currentState == null) {
					//System.out.println("i:" + i);
					//continue;
					return false;
				}
			}
			return finalStates.contains(currentState);
		}
		
//		public boolean processEvent(Event event) {
//			
//			Map<Character, State> eventMap = transitions.get(currentState);
//			
//	        if (eventMap == null) {
//	            return false;
//	        }
//	        State nextState = eventMap.get(event.getName());
//	        if (nextState == null) {
//	            return false;
//	        }
//	        currentState = nextState;
//	        
//	        return true;
//	    }				
	}

