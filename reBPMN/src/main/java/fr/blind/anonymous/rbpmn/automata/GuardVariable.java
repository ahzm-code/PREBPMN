package fr.blind.anonymous.rbpmn.automata;

public class GuardVariable {
	
	private String name;
    private double value;

    public GuardVariable(String name) {
        this.name = name;
        this.value = 0;
    }
    
    public GuardVariable(String name, double value) {
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

    public void setValue(double value) {
        this.value = value;
    }

	@Override
	public String toString() {
		return "[" + name + "]";
	}
}
