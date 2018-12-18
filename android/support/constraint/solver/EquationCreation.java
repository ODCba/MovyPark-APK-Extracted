package android.support.constraint.solver;

import com.google.android.gms.maps.model.GroundOverlayOptions;
import java.util.ArrayList;

public class EquationCreation {
    private static final boolean DEBUG = false;

    public static ArrayRow createRowEquals(LinearSystem linearSystem, SolverVariable variable, int value) {
        ArrayRow row = linearSystem.createRow();
        row.createRowEquals(variable, value);
        return row;
    }

    public static ArrayRow createRowEquals(LinearSystem linearSystem, SolverVariable variableA, SolverVariable variableB, int margin, boolean withError) {
        ArrayRow row = linearSystem.createRow();
        row.createRowEquals(variableA, variableB, margin);
        if (withError) {
            linearSystem.addSingleError(row, 1);
        }
        return row;
    }

    public static ArrayRow createRowDimensionPercent(LinearSystem linearSystem, SolverVariable variableA, SolverVariable variableB, SolverVariable variableC, float percent, boolean withError) {
        ArrayRow row = linearSystem.createRow();
        if (withError) {
            linearSystem.addError(row);
        }
        return row.createRowDimensionPercent(variableA, variableB, variableC, percent);
    }

    public static ArrayRow createRowGreaterThan(LinearSystem linearSystem, SolverVariable variableA, SolverVariable variableB, int margin, boolean withError) {
        SolverVariable slack = linearSystem.createSlackVariable();
        ArrayRow row = linearSystem.createRow();
        row.createRowGreaterThan(variableA, variableB, slack, margin);
        if (withError) {
            linearSystem.addSingleError(row, (int) (GroundOverlayOptions.NO_DIMENSION * row.variables.get(slack)));
        }
        return row;
    }

    public static ArrayRow createRowLowerThan(LinearSystem linearSystem, SolverVariable variableA, SolverVariable variableB, int margin, boolean withError) {
        SolverVariable slack = linearSystem.createSlackVariable();
        ArrayRow row = linearSystem.createRow();
        row.createRowLowerThan(variableA, variableB, slack, margin);
        if (withError) {
            linearSystem.addSingleError(row, (int) (GroundOverlayOptions.NO_DIMENSION * row.variables.get(slack)));
        }
        return row;
    }

    public static ArrayRow createRowCentering(LinearSystem linearSystem, SolverVariable variableA, SolverVariable variableB, int marginA, float bias, SolverVariable variableC, SolverVariable variableD, int marginB, boolean withError) {
        ArrayRow row = linearSystem.createRow();
        row.createRowCentering(variableA, variableB, marginA, bias, variableC, variableD, marginB, withError);
        if (withError) {
            linearSystem.addError(row);
        }
        return row;
    }

    static ArrayRow createRowFromEquation(LinearSystem linearSystem, LinearEquation e) {
        e.normalize();
        e.moveAllToTheRight();
        ArrayRow row = linearSystem.createRow();
        ArrayList<EquationVariable> eq = e.getRightSide();
        int count = eq.size();
        for (int i = 0; i < count; i++) {
            EquationVariable v = (EquationVariable) eq.get(i);
            SolverVariable sv = v.getSolverVariable();
            if (sv != null) {
                row.variables.put(sv, v.getAmount().toFloat());
            } else {
                row.constantValue = v.getAmount().toFloat();
            }
        }
        return row;
    }
}
