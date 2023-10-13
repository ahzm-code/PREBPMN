package fr.blind.anonymous.rbpmn.automata;

public class Event {
	private String name;
    private int value;

    public Event(String name) {
        this.name = name;
    }
    
    public Event(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
