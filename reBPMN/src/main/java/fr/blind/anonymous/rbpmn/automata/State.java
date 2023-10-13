package fr.blind.anonymous.rbpmn.automata;

import java.util.Objects;

public class State {
	
	private int id;

	public State(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof State) {
			State other = (State) obj;
			return id == other.id;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "State(id):" + id + "";
	}
}
