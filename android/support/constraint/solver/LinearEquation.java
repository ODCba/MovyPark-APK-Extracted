package android.support.constraint.solver;

import android.support.constraint.solver.SolverVariable.Strength;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

class LinearEquation {
    private static int artificialIndex = 0;
    private static int errorIndex = 0;
    private static int slackIndex = 0;
    private ArrayList<EquationVariable> mCurrentSide;
    private ArrayList<EquationVariable> mLeftSide;
    private ArrayList<EquationVariable> mRightSide;
    private LinearSystem mSystem;
    private Type mType;

    private enum Type {
        EQUALS,
        LOWER_THAN,
        GREATER_THAN
    }

    public boolean isNull() {
        if (this.mLeftSide.size() == 0 && this.mRightSide.size() == 0) {
            return true;
        }
        if (this.mLeftSide.size() == 1 && this.mRightSide.size() == 1) {
            EquationVariable v1 = (EquationVariable) this.mLeftSide.get(0);
            EquationVariable v2 = (EquationVariable) this.mRightSide.get(0);
            if (v1.isConstant() && v2.isConstant() && v1.getAmount().isNull() && v2.getAmount().isNull()) {
                return true;
            }
        }
        return false;
    }

    static String getNextArtificialVariableName() {
        StringBuilder append = new StringBuilder().append("a");
        int i = artificialIndex + 1;
        artificialIndex = i;
        return append.append(i).toString();
    }

    static String getNextSlackVariableName() {
        StringBuilder append = new StringBuilder().append("s");
        int i = slackIndex + 1;
        slackIndex = i;
        return append.append(i).toString();
    }

    static String getNextErrorVariableName() {
        StringBuilder append = new StringBuilder().append("e");
        int i = errorIndex + 1;
        errorIndex = i;
        return append.append(i).toString();
    }

    public static void resetNaming() {
        artificialIndex = 0;
        slackIndex = 0;
        errorIndex = 0;
    }

    public LinearEquation(LinearEquation equation) {
        int i;
        this.mLeftSide = new ArrayList();
        this.mRightSide = new ArrayList();
        this.mCurrentSide = null;
        this.mType = Type.EQUALS;
        this.mSystem = null;
        ArrayList<EquationVariable> mLeftSide1 = equation.mLeftSide;
        int mLeftSide1Size = mLeftSide1.size();
        for (i = 0; i < mLeftSide1Size; i++) {
            this.mLeftSide.add(new EquationVariable((EquationVariable) mLeftSide1.get(i)));
        }
        ArrayList<EquationVariable> mRightSide1 = equation.mRightSide;
        int mRightSide1Size = mRightSide1.size();
        for (i = 0; i < mRightSide1Size; i++) {
            this.mRightSide.add(new EquationVariable((EquationVariable) mRightSide1.get(i)));
        }
        this.mCurrentSide = this.mRightSide;
    }

    public void m0i() {
        if (this.mSystem != null) {
            this.mSystem.addConstraint(this);
        }
    }

    public void setLeftSide() {
        this.mCurrentSide = this.mLeftSide;
    }

    public void clearLeftSide() {
        this.mLeftSide.clear();
    }

    public void remove(SolverVariable v) {
        EquationVariable ev = find(v, this.mLeftSide);
        if (ev != null) {
            this.mLeftSide.remove(ev);
        }
        ev = find(v, this.mRightSide);
        if (ev != null) {
            this.mRightSide.remove(ev);
        }
    }

    public LinearEquation() {
        this.mLeftSide = new ArrayList();
        this.mRightSide = new ArrayList();
        this.mCurrentSide = null;
        this.mType = Type.EQUALS;
        this.mSystem = null;
        this.mCurrentSide = this.mLeftSide;
    }

    public LinearEquation(LinearSystem system) {
        this.mLeftSide = new ArrayList();
        this.mRightSide = new ArrayList();
        this.mCurrentSide = null;
        this.mType = Type.EQUALS;
        this.mSystem = null;
        this.mCurrentSide = this.mLeftSide;
        this.mSystem = system;
    }

    public void setSystem(LinearSystem system) {
        this.mSystem = system;
    }

    public LinearEquation equalsTo() {
        this.mCurrentSide = this.mRightSide;
        return this;
    }

    public LinearEquation greaterThan() {
        this.mCurrentSide = this.mRightSide;
        this.mType = Type.GREATER_THAN;
        return this;
    }

    public LinearEquation lowerThan() {
        this.mCurrentSide = this.mRightSide;
        this.mType = Type.LOWER_THAN;
        return this;
    }

    public void normalize() {
        if (this.mType != Type.EQUALS) {
            this.mCurrentSide = this.mLeftSide;
            if (this.mType == Type.LOWER_THAN) {
                withSlack(1);
            } else if (this.mType == Type.GREATER_THAN) {
                withSlack(-1);
            }
            this.mType = Type.EQUALS;
            this.mCurrentSide = this.mRightSide;
        }
    }

    public void simplify() {
        simplifySide(this.mLeftSide);
        simplifySide(this.mRightSide);
    }

    private void simplifySide(ArrayList<EquationVariable> side) {
        int i;
        EquationVariable constant = null;
        HashMap<String, EquationVariable> variables = new HashMap();
        ArrayList<String> variablesNames = new ArrayList();
        int sideSize = side.size();
        for (i = 0; i < sideSize; i++) {
            EquationVariable v = (EquationVariable) side.get(i);
            if (v.isConstant()) {
                if (constant == null) {
                    constant = v;
                } else {
                    constant.add(v);
                }
            } else if (variables.containsKey(v.getName())) {
                ((EquationVariable) variables.get(v.getName())).add(v);
            } else {
                variables.put(v.getName(), v);
                variablesNames.add(v.getName());
            }
        }
        side.clear();
        if (constant != null) {
            side.add(constant);
        }
        Collections.sort(variablesNames);
        int variablesNamesSize = variablesNames.size();
        for (i = 0; i < variablesNamesSize; i++) {
            side.add((EquationVariable) variables.get((String) variablesNames.get(i)));
        }
        removeNullTerms(side);
    }

    public void moveAllToTheRight() {
        int mLeftSideSize = this.mLeftSide.size();
        for (int i = 0; i < mLeftSideSize; i++) {
            this.mRightSide.add(((EquationVariable) this.mLeftSide.get(i)).inverse());
        }
        this.mLeftSide.clear();
    }

    public void balance() {
        if (this.mLeftSide.size() != 0 || this.mRightSide.size() != 0) {
            int i;
            EquationVariable v;
            this.mCurrentSide = this.mLeftSide;
            int mLeftSideSize = this.mLeftSide.size();
            for (i = 0; i < mLeftSideSize; i++) {
                this.mRightSide.add(((EquationVariable) this.mLeftSide.get(i)).inverse());
            }
            this.mLeftSide.clear();
            simplifySide(this.mRightSide);
            EquationVariable found = null;
            int mRightSideSize = this.mRightSide.size();
            for (i = 0; i < mRightSideSize; i++) {
                v = (EquationVariable) this.mRightSide.get(i);
                if (v.getType() == android.support.constraint.solver.SolverVariable.Type.UNRESTRICTED) {
                    found = v;
                    break;
                }
            }
            if (found == null) {
                mRightSideSize = this.mRightSide.size();
                for (i = 0; i < mRightSideSize; i++) {
                    v = (EquationVariable) this.mRightSide.get(i);
                    if (v.getType() == android.support.constraint.solver.SolverVariable.Type.SLACK) {
                        found = v;
                        break;
                    }
                }
            }
            if (found == null) {
                mRightSideSize = this.mRightSide.size();
                for (i = 0; i < mRightSideSize; i++) {
                    v = (EquationVariable) this.mRightSide.get(i);
                    if (v.getType() == android.support.constraint.solver.SolverVariable.Type.ERROR) {
                        found = v;
                        break;
                    }
                }
            }
            if (found != null) {
                this.mRightSide.remove(found);
                found.inverse();
                if (!found.getAmount().isOne()) {
                    Amount foundAmount = found.getAmount();
                    mRightSideSize = this.mRightSide.size();
                    for (i = 0; i < mRightSideSize; i++) {
                        ((EquationVariable) this.mRightSide.get(i)).getAmount().divide(foundAmount);
                    }
                    found.setAmount(new Amount(1));
                }
                simplifySide(this.mRightSide);
                this.mLeftSide.add(found);
            }
        }
    }

    private void removeNullTerms(ArrayList<EquationVariable> list) {
        int i;
        boolean hasNullTerm = false;
        int listSize = list.size();
        for (i = 0; i < listSize; i++) {
            if (((EquationVariable) list.get(i)).getAmount().isNull()) {
                hasNullTerm = true;
                break;
            }
        }
        if (hasNullTerm) {
            ArrayList<EquationVariable> newSide = new ArrayList();
            listSize = list.size();
            for (i = 0; i < listSize; i++) {
                EquationVariable v = (EquationVariable) list.get(i);
                if (!v.getAmount().isNull()) {
                    newSide.add(v);
                }
            }
            list.clear();
            list.addAll(newSide);
        }
    }

    public void pivot(SolverVariable variable) {
        if (this.mLeftSide.size() != 1 || ((EquationVariable) this.mLeftSide.get(0)).getSolverVariable() != variable) {
            int i;
            int mLeftSideSize = this.mLeftSide.size();
            for (i = 0; i < mLeftSideSize; i++) {
                this.mRightSide.add(((EquationVariable) this.mLeftSide.get(i)).inverse());
            }
            this.mLeftSide.clear();
            simplifySide(this.mRightSide);
            EquationVariable found = null;
            int mRightSideSize = this.mRightSide.size();
            for (i = 0; i < mRightSideSize; i++) {
                EquationVariable v = (EquationVariable) this.mRightSide.get(i);
                if (v.getSolverVariable() == variable) {
                    found = v;
                    break;
                }
            }
            if (found != null) {
                this.mRightSide.remove(found);
                found.inverse();
                if (!found.getAmount().isOne()) {
                    Amount foundAmount = found.getAmount();
                    mRightSideSize = this.mRightSide.size();
                    for (i = 0; i < mRightSideSize; i++) {
                        ((EquationVariable) this.mRightSide.get(i)).getAmount().divide(foundAmount);
                    }
                    found.setAmount(new Amount(1));
                }
                this.mLeftSide.add(found);
            }
        }
    }

    public boolean hasNegativeConstant() {
        int mRightSideSize = this.mRightSide.size();
        for (int i = 0; i < mRightSideSize; i++) {
            EquationVariable v = (EquationVariable) this.mRightSide.get(i);
            if (v.isConstant() && v.getAmount().isNegative()) {
                return true;
            }
        }
        return false;
    }

    public Amount getConstant() {
        int mRightSideSize = this.mRightSide.size();
        for (int i = 0; i < mRightSideSize; i++) {
            EquationVariable v = (EquationVariable) this.mRightSide.get(i);
            if (v.isConstant()) {
                return v.getAmount();
            }
        }
        return null;
    }

    public void inverse() {
        int i;
        Amount amount = new Amount(-1);
        int mLeftSideSize = this.mLeftSide.size();
        for (i = 0; i < mLeftSideSize; i++) {
            ((EquationVariable) this.mLeftSide.get(i)).multiply(amount);
        }
        int mRightSideSize = this.mRightSide.size();
        for (i = 0; i < mRightSideSize; i++) {
            ((EquationVariable) this.mRightSide.get(i)).multiply(amount);
        }
    }

    public EquationVariable getFirstUnconstrainedVariable() {
        int i;
        int mLeftSideSize = this.mLeftSide.size();
        for (i = 0; i < mLeftSideSize; i++) {
            EquationVariable v = (EquationVariable) this.mLeftSide.get(i);
            if (v.getType() == android.support.constraint.solver.SolverVariable.Type.UNRESTRICTED) {
                return v;
            }
        }
        int mRightSideSize = this.mRightSide.size();
        for (i = 0; i < mRightSideSize; i++) {
            v = (EquationVariable) this.mRightSide.get(i);
            if (v.getType() == android.support.constraint.solver.SolverVariable.Type.UNRESTRICTED) {
                return v;
            }
        }
        return null;
    }

    public EquationVariable getLeftVariable() {
        if (this.mLeftSide.size() == 1) {
            return (EquationVariable) this.mLeftSide.get(0);
        }
        return null;
    }

    public void replace(SolverVariable v, LinearEquation l) {
        replace(v, l, this.mLeftSide);
        replace(v, l, this.mRightSide);
    }

    private void replace(SolverVariable v, LinearEquation l, ArrayList<EquationVariable> list) {
        EquationVariable toReplace = find(v, list);
        if (toReplace != null) {
            list.remove(toReplace);
            Amount amount = toReplace.getAmount();
            ArrayList<EquationVariable> mRightSide1 = l.mRightSide;
            int mRightSide1Size = mRightSide1.size();
            for (int i = 0; i < mRightSide1Size; i++) {
                list.add(new EquationVariable(amount, (EquationVariable) mRightSide1.get(i)));
            }
        }
    }

    private EquationVariable find(SolverVariable v, ArrayList<EquationVariable> list) {
        int listSize = list.size();
        for (int i = 0; i < listSize; i++) {
            EquationVariable ev = (EquationVariable) list.get(i);
            if (ev.getSolverVariable() == v) {
                return ev;
            }
        }
        return null;
    }

    public ArrayList<EquationVariable> getRightSide() {
        return this.mRightSide;
    }

    public boolean contains(SolverVariable solverVariable) {
        if (find(solverVariable, this.mLeftSide) == null && find(solverVariable, this.mRightSide) == null) {
            return false;
        }
        return true;
    }

    public EquationVariable getVariable(SolverVariable solverVariable) {
        EquationVariable variable = find(solverVariable, this.mRightSide);
        return variable != null ? variable : find(solverVariable, this.mLeftSide);
    }

    public LinearEquation var(int amount) {
        this.mCurrentSide.add(new EquationVariable(this.mSystem, amount));
        return this;
    }

    public LinearEquation var(int numerator, int denominator) {
        this.mCurrentSide.add(new EquationVariable(new Amount(numerator, denominator)));
        return this;
    }

    public LinearEquation var(String name) {
        this.mCurrentSide.add(new EquationVariable(this.mSystem, name, android.support.constraint.solver.SolverVariable.Type.UNRESTRICTED));
        return this;
    }

    public LinearEquation var(int amount, String name) {
        this.mCurrentSide.add(new EquationVariable(this.mSystem, amount, name, android.support.constraint.solver.SolverVariable.Type.UNRESTRICTED));
        return this;
    }

    public LinearEquation var(int numerator, int denominator, String name) {
        this.mCurrentSide.add(new EquationVariable(this.mSystem, new Amount(numerator, denominator), name, android.support.constraint.solver.SolverVariable.Type.UNRESTRICTED));
        return this;
    }

    public LinearEquation plus(String name) {
        var(name);
        return this;
    }

    public LinearEquation plus(int amount, String name) {
        var(amount, name);
        return this;
    }

    public LinearEquation minus(String name) {
        var(-1, name);
        return this;
    }

    public LinearEquation minus(int amount, String name) {
        var(amount * -1, name);
        return this;
    }

    public LinearEquation plus(int amount) {
        var(amount);
        return this;
    }

    public LinearEquation minus(int amount) {
        var(amount * -1);
        return this;
    }

    public LinearEquation plus(int numerator, int denominator) {
        var(numerator, denominator);
        return this;
    }

    public LinearEquation minus(int numerator, int denominator) {
        var(numerator * -1, denominator);
        return this;
    }

    public LinearEquation withError(String name, int strength) {
        this.mCurrentSide.add(new EquationVariable(this.mSystem, strength, name, android.support.constraint.solver.SolverVariable.Type.ERROR));
        return this;
    }

    public LinearEquation withError(Amount amount, String name) {
        this.mCurrentSide.add(new EquationVariable(this.mSystem, amount, name, android.support.constraint.solver.SolverVariable.Type.ERROR));
        return this;
    }

    public LinearEquation withError() {
        String name = getNextErrorVariableName();
        withError(name + "+", 1);
        withError(name + "-", -1);
        return this;
    }

    public LinearEquation withPositiveError() {
        withError(getNextErrorVariableName() + "+", 1);
        return this;
    }

    public LinearEquation withStrongError() {
        String name = getNextErrorVariableName();
        EquationVariable e = new EquationVariable(this.mSystem, 1, name + "+", android.support.constraint.solver.SolverVariable.Type.ERROR);
        e.getSolverVariable().setStrength(Strength.STRONG);
        this.mCurrentSide.add(e);
        e = new EquationVariable(this.mSystem, -1, name + "-", android.support.constraint.solver.SolverVariable.Type.ERROR);
        e.getSolverVariable().setStrength(Strength.STRONG);
        this.mCurrentSide.add(e);
        return this;
    }

    public EquationVariable addArtificialVar() {
        EquationVariable e = new EquationVariable(this.mSystem, 1, getNextArtificialVariableName(), android.support.constraint.solver.SolverVariable.Type.ERROR);
        this.mCurrentSide.add(e);
        return e;
    }

    public LinearEquation withError(int strength) {
        withError(getNextErrorVariableName(), strength);
        return this;
    }

    public LinearEquation withSlack(String name, int strength) {
        this.mCurrentSide.add(new EquationVariable(this.mSystem, strength, name, android.support.constraint.solver.SolverVariable.Type.SLACK));
        return this;
    }

    public LinearEquation withSlack(Amount amount, String name) {
        this.mCurrentSide.add(new EquationVariable(this.mSystem, amount, name, android.support.constraint.solver.SolverVariable.Type.SLACK));
        return this;
    }

    public LinearEquation withSlack() {
        withSlack(getNextSlackVariableName(), 1);
        return this;
    }

    public LinearEquation withSlack(int strength) {
        withSlack(getNextSlackVariableName(), strength);
        return this;
    }

    public String toString() {
        String result = "";
        result = sideToString(this.mLeftSide);
        switch (this.mType) {
            case EQUALS:
                result = result + "= ";
                break;
            case LOWER_THAN:
                result = result + "<= ";
                break;
            case GREATER_THAN:
                result = result + ">= ";
                break;
        }
        return (result + sideToString(this.mRightSide)).trim();
    }

    private String sideToString(ArrayList<EquationVariable> side) {
        String result = "";
        boolean first = true;
        int sideSize = side.size();
        for (int i = 0; i < sideSize; i++) {
            EquationVariable v = (EquationVariable) side.get(i);
            if (first) {
                if (v.getAmount().isPositive()) {
                    result = result + v + " ";
                } else {
                    result = result + v.signString() + " " + v + " ";
                }
                first = false;
            } else {
                result = result + v.signString() + " " + v + " ";
            }
        }
        if (side.size() == 0) {
            return "0";
        }
        return result;
    }
}
