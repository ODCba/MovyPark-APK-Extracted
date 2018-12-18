package android.support.constraint.solver;

import android.support.constraint.solver.SolverVariable.Type;

class EquationVariable {
    private Amount mAmount;
    private SolverVariable mVariable;

    public EquationVariable(LinearSystem system, Amount amount, String name, Type type) {
        this.mAmount = null;
        this.mVariable = null;
        this.mAmount = amount;
        this.mVariable = system.getVariable(name, type);
    }

    public EquationVariable(Amount amount) {
        this.mAmount = null;
        this.mVariable = null;
        this.mAmount = amount;
    }

    public EquationVariable(LinearSystem system, int amount, String name, Type type) {
        this.mAmount = null;
        this.mVariable = null;
        this.mAmount = new Amount(amount);
        this.mVariable = system.getVariable(name, type);
    }

    public EquationVariable(LinearSystem system, int amount) {
        this.mAmount = null;
        this.mVariable = null;
        this.mAmount = new Amount(amount);
    }

    public EquationVariable(LinearSystem system, String name, Type type) {
        this.mAmount = null;
        this.mVariable = null;
        this.mAmount = new Amount(1);
        this.mVariable = system.getVariable(name, type);
    }

    public EquationVariable(Amount amount, EquationVariable variable) {
        this.mAmount = null;
        this.mVariable = null;
        this.mAmount = new Amount(amount);
        this.mAmount.multiply(variable.mAmount);
        this.mVariable = variable.getSolverVariable();
    }

    public EquationVariable(EquationVariable v) {
        this.mAmount = null;
        this.mVariable = null;
        this.mAmount = new Amount(v.mAmount);
        this.mVariable = v.getSolverVariable();
    }

    public String getName() {
        if (this.mVariable == null) {
            return null;
        }
        return this.mVariable.getName();
    }

    public Type getType() {
        if (this.mVariable == null) {
            return Type.CONSTANT;
        }
        return this.mVariable.mType;
    }

    public SolverVariable getSolverVariable() {
        return this.mVariable;
    }

    public boolean isConstant() {
        return this.mVariable == null;
    }

    public Amount getAmount() {
        return this.mAmount;
    }

    public void setAmount(Amount amount) {
        this.mAmount = amount;
    }

    public EquationVariable inverse() {
        this.mAmount.inverse();
        return this;
    }

    public boolean isCompatible(EquationVariable variable) {
        if (isConstant()) {
            return variable.isConstant();
        }
        if (variable.isConstant() || variable.getSolverVariable() != getSolverVariable()) {
            return false;
        }
        return true;
    }

    public void add(EquationVariable variable) {
        if (variable.isCompatible(this)) {
            this.mAmount.add(variable.mAmount);
        }
    }

    public void substract(EquationVariable variable) {
        if (variable.isCompatible(this)) {
            this.mAmount.substract(variable.mAmount);
        }
    }

    public void multiply(EquationVariable variable) {
        multiply(variable.getAmount());
    }

    public void multiply(Amount amount) {
        this.mAmount.multiply(amount);
    }

    public void divide(EquationVariable variable) {
        this.mAmount.divide(variable.mAmount);
    }

    public String toString() {
        if (isConstant()) {
            return "" + this.mAmount;
        }
        if (this.mAmount.isOne() || this.mAmount.isMinusOne()) {
            return "" + this.mVariable;
        }
        return "" + this.mAmount + " " + this.mVariable;
    }

    public String signString() {
        if (this.mAmount.isPositive()) {
            return "+";
        }
        return "-";
    }
}
