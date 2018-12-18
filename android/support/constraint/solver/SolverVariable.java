package android.support.constraint.solver;

import java.util.Arrays;

public class SolverVariable {
    private static final boolean INTERNAL_DEBUG = false;
    public static final boolean USE_LIST = false;
    static int uniqueId = 1;
    public float computedValue;
    public int definitionId;
    public int id;
    private final Cache mCache;
    ArrayRow[] mClientEquations;
    int mClientEquationsCount;
    private String mName;
    Strength mStrength;
    Type mType;

    final class Link {
        Link next;
        ArrayRow row;

        Link() {
        }
    }

    public enum Strength {
        STRONG,
        WEAK,
        UNKNOWN
    }

    public enum Type {
        UNRESTRICTED,
        CONSTANT,
        SLACK,
        ERROR,
        UNKNOWN
    }

    public static String getUniqueName() {
        uniqueId++;
        return "V" + uniqueId;
    }

    public static String getUniqueName(Type type, Strength strength) {
        uniqueId++;
        switch (type) {
            case UNRESTRICTED:
                return "U" + uniqueId;
            case CONSTANT:
                return "C" + uniqueId;
            case SLACK:
                return "S" + uniqueId;
            case ERROR:
                if (strength == Strength.STRONG) {
                    return "E" + uniqueId;
                }
                return "e" + uniqueId;
            default:
                return "V" + uniqueId;
        }
    }

    public SolverVariable(Cache cache, String name, Type type) {
        this.id = -1;
        this.definitionId = -1;
        this.mStrength = Strength.WEAK;
        this.mClientEquations = new ArrayRow[8];
        this.mClientEquationsCount = 0;
        this.mCache = cache;
        this.mName = name;
        this.mType = type;
    }

    public SolverVariable(Cache cache, Type type) {
        this.id = -1;
        this.definitionId = -1;
        this.mStrength = Strength.WEAK;
        this.mClientEquations = new ArrayRow[8];
        this.mClientEquationsCount = 0;
        this.mCache = cache;
        this.mType = type;
    }

    public void addClientEquation(ArrayRow equation) {
        int i = 0;
        while (i < this.mClientEquationsCount) {
            if (this.mClientEquations[i] != equation) {
                i++;
            } else {
                return;
            }
        }
        if (this.mClientEquationsCount >= this.mClientEquations.length) {
            this.mClientEquations = (ArrayRow[]) Arrays.copyOf(this.mClientEquations, this.mClientEquations.length * 2);
        }
        this.mClientEquations[this.mClientEquationsCount] = equation;
        this.mClientEquationsCount++;
    }

    public void removeClientEquation(ArrayRow equation) {
        for (int i = 0; i < this.mClientEquationsCount; i++) {
            if (this.mClientEquations[i] == equation) {
                System.arraycopy(this.mClientEquations, i + 1, this.mClientEquations, i, (this.mClientEquationsCount - i) - 1);
                this.mClientEquationsCount--;
                return;
            }
        }
    }

    public void reset() {
        this.mName = null;
        this.mType = Type.UNKNOWN;
        this.mStrength = Strength.STRONG;
        this.id = -1;
        this.definitionId = -1;
        this.computedValue = 0.0f;
        this.mClientEquationsCount = 0;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void setType(Type type) {
        this.mType = type;
    }

    public void setStrength(Strength s) {
        this.mStrength = s;
    }

    public String toString() {
        return "" + this.mName;
    }
}
