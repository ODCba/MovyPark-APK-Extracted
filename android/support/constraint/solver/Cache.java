package android.support.constraint.solver;

public class Cache {
    private static final int POOL_SIZE = 4096;
    public Pool<ArrayRow> arrayRowPool = new SimplePool(256);
    public Pool<Link> linkedSolverVariablePool = new SimplePool(256);
    public Pool<Link> linkedVariablesPool = new SimplePool(256);
    public SolverVariable[] mIndexedVariables = new SolverVariable[32];
    public Pool<SolverVariable> solverVariablePool = new SimplePool(256);
}
