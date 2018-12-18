package android.support.constraint.solver;

import java.util.Arrays;

class ArrayBackedVariables {
    private static final boolean DEBUG = false;
    private SolverVariable candidate;
    private int currentSize;
    private int currentWriteSize;
    private int[] indexes;
    private int maxSize;
    private final int threshold;
    private float[] values;
    private SolverVariable[] variables;

    public ArrayBackedVariables(ArrayRow arrayRow, Cache cache) {
        this.variables = null;
        this.values = null;
        this.indexes = null;
        this.threshold = 16;
        this.maxSize = 4;
        this.currentSize = 0;
        this.currentWriteSize = 0;
        this.candidate = null;
        this.variables = new SolverVariable[this.maxSize];
        this.values = new float[this.maxSize];
        this.indexes = new int[this.maxSize];
    }

    public SolverVariable getPivotCandidate() {
        if (this.candidate == null) {
            for (int i = 0; i < this.currentSize; i++) {
                int idx = this.indexes[i];
                if (this.values[idx] < 0.0f) {
                    this.candidate = this.variables[idx];
                    break;
                }
            }
        }
        return this.candidate;
    }

    void increaseSize() {
        this.maxSize *= 2;
        this.variables = (SolverVariable[]) Arrays.copyOf(this.variables, this.maxSize);
        this.values = Arrays.copyOf(this.values, this.maxSize);
        this.indexes = Arrays.copyOf(this.indexes, this.maxSize);
    }

    public final int size() {
        return this.currentSize;
    }

    public final SolverVariable getVariable(int index) {
        return this.variables[this.indexes[index]];
    }

    public final float getVariableValue(int index) {
        return this.values[this.indexes[index]];
    }

    public final void updateArray(ArrayBackedVariables target, float amount) {
        if (amount != 0.0f) {
            for (int i = 0; i < this.currentSize; i++) {
                int idx = this.indexes[i];
                target.add(this.variables[idx], this.values[idx] * amount);
            }
        }
    }

    public void setVariable(int index, float value) {
        int idx = this.indexes[index];
        this.values[idx] = value;
        if (value < 0.0f) {
            this.candidate = this.variables[idx];
        }
    }

    public final float get(SolverVariable v) {
        int idx;
        if (this.currentSize < 16) {
            for (int i = 0; i < this.currentSize; i++) {
                idx = this.indexes[i];
                if (this.variables[idx] == v) {
                    return this.values[idx];
                }
            }
        } else {
            int start = 0;
            int end = this.currentSize - 1;
            while (start <= end) {
                int index = start + ((end - start) / 2);
                idx = this.indexes[index];
                SolverVariable current = this.variables[idx];
                if (current == v) {
                    return this.values[idx];
                }
                if (current.id < v.id) {
                    start = index + 1;
                } else {
                    end = index - 1;
                }
            }
        }
        return 0.0f;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void put(android.support.constraint.solver.SolverVariable r11, float r12) {
        /*
        r10 = this;
        r7 = -1;
        r9 = 0;
        r5 = (r12 > r9 ? 1 : (r12 == r9 ? 0 : -1));
        if (r5 != 0) goto L_0x000d;
    L_0x0006:
        r10.remove(r11);
    L_0x0009:
        return;
    L_0x000a:
        r10.increaseSize();
    L_0x000d:
        r0 = -1;
        r1 = 0;
    L_0x000f:
        r5 = r10.currentWriteSize;
        if (r1 >= r5) goto L_0x0030;
    L_0x0013:
        r5 = r10.variables;
        r5 = r5[r1];
        if (r5 != r11) goto L_0x0024;
    L_0x0019:
        r5 = r10.values;
        r5[r1] = r12;
        r5 = (r12 > r9 ? 1 : (r12 == r9 ? 0 : -1));
        if (r5 >= 0) goto L_0x0009;
    L_0x0021:
        r10.candidate = r11;
        goto L_0x0009;
    L_0x0024:
        if (r0 != r7) goto L_0x002d;
    L_0x0026:
        r5 = r10.variables;
        r5 = r5[r1];
        if (r5 != 0) goto L_0x002d;
    L_0x002c:
        r0 = r1;
    L_0x002d:
        r1 = r1 + 1;
        goto L_0x000f;
    L_0x0030:
        if (r0 != r7) goto L_0x003a;
    L_0x0032:
        r5 = r10.currentWriteSize;
        r6 = r10.maxSize;
        if (r5 >= r6) goto L_0x003a;
    L_0x0038:
        r0 = r10.currentWriteSize;
    L_0x003a:
        if (r0 == r7) goto L_0x000a;
    L_0x003c:
        r5 = r10.variables;
        r5[r0] = r11;
        r5 = r10.values;
        r5[r0] = r12;
        r3 = 0;
        r4 = 0;
    L_0x0046:
        r5 = r10.currentSize;
        if (r4 >= r5) goto L_0x0069;
    L_0x004a:
        r5 = r10.indexes;
        r2 = r5[r4];
        r5 = r10.variables;
        r5 = r5[r2];
        r5 = r5.id;
        r6 = r11.id;
        if (r5 <= r6) goto L_0x0088;
    L_0x0058:
        r5 = r10.indexes;
        r6 = r10.indexes;
        r7 = r4 + 1;
        r8 = r10.currentSize;
        r8 = r8 - r4;
        java.lang.System.arraycopy(r5, r4, r6, r7, r8);
        r5 = r10.indexes;
        r5[r4] = r0;
        r3 = 1;
    L_0x0069:
        if (r3 != 0) goto L_0x0071;
    L_0x006b:
        r5 = r10.indexes;
        r6 = r10.currentSize;
        r5[r6] = r0;
    L_0x0071:
        r5 = r10.currentSize;
        r5 = r5 + 1;
        r10.currentSize = r5;
        r5 = r0 + 1;
        r6 = r10.currentWriteSize;
        if (r5 <= r6) goto L_0x0081;
    L_0x007d:
        r5 = r0 + 1;
        r10.currentWriteSize = r5;
    L_0x0081:
        r5 = (r12 > r9 ? 1 : (r12 == r9 ? 0 : -1));
        if (r5 >= 0) goto L_0x0009;
    L_0x0085:
        r10.candidate = r11;
        goto L_0x0009;
    L_0x0088:
        r4 = r4 + 1;
        goto L_0x0046;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.constraint.solver.ArrayBackedVariables.put(android.support.constraint.solver.SolverVariable, float):void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void add(android.support.constraint.solver.SolverVariable r11, float r12) {
        /*
        r10 = this;
        r7 = -1;
        r9 = 0;
        r5 = (r12 > r9 ? 1 : (r12 == r9 ? 0 : -1));
        if (r5 != 0) goto L_0x000a;
    L_0x0006:
        return;
    L_0x0007:
        r10.increaseSize();
    L_0x000a:
        r0 = -1;
        r1 = 0;
    L_0x000c:
        r5 = r10.currentWriteSize;
        if (r1 >= r5) goto L_0x003b;
    L_0x0010:
        r5 = r10.variables;
        r5 = r5[r1];
        if (r5 != r11) goto L_0x002f;
    L_0x0016:
        r5 = r10.values;
        r6 = r5[r1];
        r6 = r6 + r12;
        r5[r1] = r6;
        r5 = (r12 > r9 ? 1 : (r12 == r9 ? 0 : -1));
        if (r5 >= 0) goto L_0x0023;
    L_0x0021:
        r10.candidate = r11;
    L_0x0023:
        r5 = r10.values;
        r5 = r5[r1];
        r5 = (r5 > r9 ? 1 : (r5 == r9 ? 0 : -1));
        if (r5 != 0) goto L_0x0006;
    L_0x002b:
        r10.remove(r11);
        goto L_0x0006;
    L_0x002f:
        if (r0 != r7) goto L_0x0038;
    L_0x0031:
        r5 = r10.variables;
        r5 = r5[r1];
        if (r5 != 0) goto L_0x0038;
    L_0x0037:
        r0 = r1;
    L_0x0038:
        r1 = r1 + 1;
        goto L_0x000c;
    L_0x003b:
        if (r0 != r7) goto L_0x0045;
    L_0x003d:
        r5 = r10.currentWriteSize;
        r6 = r10.maxSize;
        if (r5 >= r6) goto L_0x0045;
    L_0x0043:
        r0 = r10.currentWriteSize;
    L_0x0045:
        if (r0 == r7) goto L_0x0007;
    L_0x0047:
        r5 = r10.variables;
        r5[r0] = r11;
        r5 = r10.values;
        r5[r0] = r12;
        r3 = 0;
        r4 = 0;
    L_0x0051:
        r5 = r10.currentSize;
        if (r4 >= r5) goto L_0x0074;
    L_0x0055:
        r5 = r10.indexes;
        r2 = r5[r4];
        r5 = r10.variables;
        r5 = r5[r2];
        r5 = r5.id;
        r6 = r11.id;
        if (r5 <= r6) goto L_0x0094;
    L_0x0063:
        r5 = r10.indexes;
        r6 = r10.indexes;
        r7 = r4 + 1;
        r8 = r10.currentSize;
        r8 = r8 - r4;
        java.lang.System.arraycopy(r5, r4, r6, r7, r8);
        r5 = r10.indexes;
        r5[r4] = r0;
        r3 = 1;
    L_0x0074:
        if (r3 != 0) goto L_0x007c;
    L_0x0076:
        r5 = r10.indexes;
        r6 = r10.currentSize;
        r5[r6] = r0;
    L_0x007c:
        r5 = r10.currentSize;
        r5 = r5 + 1;
        r10.currentSize = r5;
        r5 = r0 + 1;
        r6 = r10.currentWriteSize;
        if (r5 <= r6) goto L_0x008c;
    L_0x0088:
        r5 = r0 + 1;
        r10.currentWriteSize = r5;
    L_0x008c:
        r5 = (r12 > r9 ? 1 : (r12 == r9 ? 0 : -1));
        if (r5 >= 0) goto L_0x0006;
    L_0x0090:
        r10.candidate = r11;
        goto L_0x0006;
    L_0x0094:
        r4 = r4 + 1;
        goto L_0x0051;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.support.constraint.solver.ArrayBackedVariables.add(android.support.constraint.solver.SolverVariable, float):void");
    }

    public void clear() {
        int length = this.variables.length;
        for (int i = 0; i < length; i++) {
            this.variables[i] = null;
        }
        this.currentSize = 0;
        this.currentWriteSize = 0;
    }

    public boolean containsKey(SolverVariable variable) {
        if (this.currentSize < 8) {
            for (int i = 0; i < this.currentSize; i++) {
                if (this.variables[this.indexes[i]] == variable) {
                    return true;
                }
            }
        } else {
            int start = 0;
            int end = this.currentSize - 1;
            while (start <= end) {
                int index = start + ((end - start) / 2);
                SolverVariable current = this.variables[this.indexes[index]];
                if (current == variable) {
                    return true;
                }
                if (current.id < variable.id) {
                    start = index + 1;
                } else {
                    end = index - 1;
                }
            }
        }
        return false;
    }

    public float remove(SolverVariable variable) {
        if (this.candidate == variable) {
            this.candidate = null;
        }
        for (int i = 0; i < this.currentWriteSize; i++) {
            int idx = this.indexes[i];
            if (this.variables[idx] == variable) {
                float amount = this.values[idx];
                this.variables[idx] = null;
                System.arraycopy(this.indexes, i + 1, this.indexes, i, (this.currentWriteSize - i) - 1);
                this.currentSize--;
                return amount;
            }
        }
        return 0.0f;
    }

    public int sizeInBytes() {
        return (((0 + (this.maxSize * 4)) + (this.maxSize * 4)) + (this.maxSize * 4)) + 16;
    }

    public void display() {
        int count = size();
        System.out.print("{ ");
        for (int i = 0; i < count; i++) {
            System.out.print(getVariable(i) + " = " + getVariableValue(i) + " ");
        }
        System.out.println(" }");
    }

    private String getInternalArrays() {
        int i;
        int count = size();
        String str = "" + "idx { ";
        for (i = 0; i < count; i++) {
            str = str + this.indexes[i] + " ";
        }
        str = (str + "}\n") + "obj { ";
        for (i = 0; i < count; i++) {
            str = str + this.variables[i] + ":" + this.values[i] + " ";
        }
        return str + "}\n";
    }

    public void displayInternalArrays() {
        int i;
        int count = size();
        System.out.print("idx { ");
        for (i = 0; i < count; i++) {
            System.out.print(this.indexes[i] + " ");
        }
        System.out.println("}");
        System.out.print("obj { ");
        for (i = 0; i < count; i++) {
            System.out.print(this.variables[i] + ":" + this.values[i] + " ");
        }
        System.out.println("}");
    }

    public void updateFromRow(ArrayRow arrayRow, ArrayRow definition) {
    }

    public SolverVariable pickPivotCandidate() {
        return null;
    }

    public void updateFromSystem(ArrayRow goal, ArrayRow[] mRows) {
    }

    public void divideByAmount(float amount) {
    }

    public void updateClientEquations(ArrayRow arrayRow) {
    }

    public boolean hasAtLeastOnePositiveVariable() {
        return false;
    }

    public void invert() {
    }
}
