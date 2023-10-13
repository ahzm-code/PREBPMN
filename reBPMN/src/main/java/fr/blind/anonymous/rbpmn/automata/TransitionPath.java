package fr.blind.anonymous.rbpmn.automata;

public class TransitionPath {
	
	private String sourceState;
	private String eventName;
	private String targetState;
	
	public TransitionPath(String sourceState, String eventName, String targetState) {
		this.sourceState = sourceState;
		this.eventName = eventName;
		this.targetState = targetState;
	}

	public String getSourceState() {
		return sourceState;
	}

	public void setSourceState(String sourceState) {
		this.sourceState = sourceState;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getTargetState() {
		return targetState;
	}

	public void setTargetState(String targetState) {
		this.targetState = targetState;
	}
	
	
	public boolean equals(TransitionPath tPath) {
		// sourceState == tPath.sourceState And (eventName == tPath.eventName) And (targetState == tPath.targetName)
		return (this.sourceState.equals(tPath.getSourceState()) && this.eventName.equals(tPath.eventName) && this.targetState.equals(tPath.targetState));
	}

	@Override
	public String toString() {
		return "Transition ["+ sourceState + "->" + eventName + "->"
				+ targetState + "]";
	}
	
}
