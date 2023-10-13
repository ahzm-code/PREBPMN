package fr.blind.anonymous.rbpmn.automata;


public class Inequality {
	
    private GuardVariable leftOperand;
    private double rightOperand;
    private String operator;

    public Inequality(GuardVariable leftOperand, double rightOperand, String operator) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
        this.operator = operator;
    }

    public GuardVariable getLeftOperand() {
        return leftOperand;
    }

    public double getRightOperand() {
        return rightOperand;
    }

    public String getOperator() {
        return operator;
    }

    public boolean evaluate() {
        switch (operator) {
            case ">":
                return leftOperand.getValue() > rightOperand;
            case "<":
                return leftOperand.getValue() < rightOperand;
            case ">=":
                return leftOperand.getValue() >= rightOperand;
            case "<=":
                return leftOperand.getValue() <= rightOperand;
            case "==":
                return leftOperand.getValue() == rightOperand;
            case "!=":
                return leftOperand.getValue() != rightOperand;
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }

	@Override
	public String toString() {
		return "("+ leftOperand + "" + operator + "" + rightOperand  + ")";
	}
}
