package android.support.constraint.solver.widgets;

import android.support.constraint.solver.LinearSystem;

public interface Solvable {
    void addToSolver(LinearSystem linearSystem, int i);

    void setDebugSolverName(LinearSystem linearSystem, String str);

    void updateFromSolver(LinearSystem linearSystem, int i);
}
