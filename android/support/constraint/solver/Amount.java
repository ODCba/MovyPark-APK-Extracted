package android.support.constraint.solver;

class Amount {
    private int mDenominator = 1;
    private int mNumerator = 0;

    public Amount(int numerator, int denominator) {
        this.mNumerator = numerator;
        this.mDenominator = denominator;
        simplify();
    }

    public Amount(int numerator) {
        this.mNumerator = numerator;
        this.mDenominator = 1;
    }

    public Amount(Amount amount) {
        this.mNumerator = amount.mNumerator;
        this.mDenominator = amount.mDenominator;
        simplify();
    }

    public void set(int numerator, int denominator) {
        this.mNumerator = numerator;
        this.mDenominator = denominator;
        simplify();
    }

    public Amount add(Amount amount) {
        if (this.mDenominator == amount.mDenominator) {
            this.mNumerator += amount.mNumerator;
        } else {
            this.mNumerator = (this.mNumerator * amount.mDenominator) + (amount.mNumerator * this.mDenominator);
            this.mDenominator *= amount.mDenominator;
        }
        simplify();
        return this;
    }

    public Amount add(int amount) {
        this.mNumerator += this.mDenominator * amount;
        return this;
    }

    public Amount substract(Amount amount) {
        if (this.mDenominator == amount.mDenominator) {
            this.mNumerator -= amount.mNumerator;
        } else {
            this.mNumerator = (this.mNumerator * amount.mDenominator) - (amount.mNumerator * this.mDenominator);
            this.mDenominator *= amount.mDenominator;
        }
        simplify();
        return this;
    }

    public Amount multiply(Amount amount) {
        this.mNumerator *= amount.mNumerator;
        this.mDenominator *= amount.mDenominator;
        simplify();
        return this;
    }

    public Amount divide(Amount amount) {
        int preN = this.mNumerator;
        int preD = this.mDenominator;
        this.mNumerator *= amount.mDenominator;
        this.mDenominator *= amount.mNumerator;
        simplify();
        return this;
    }

    public Amount inverseFraction() {
        int n = this.mNumerator;
        this.mNumerator = this.mDenominator;
        this.mDenominator = n;
        simplify();
        return this;
    }

    public Amount inverse() {
        this.mNumerator *= -1;
        simplify();
        return this;
    }

    public int getNumerator() {
        return this.mNumerator;
    }

    public int getDenominator() {
        return this.mDenominator;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Amount)) {
            return false;
        }
        Amount a = (Amount) o;
        if (this.mNumerator == a.mNumerator && this.mDenominator == a.mDenominator) {
            return true;
        }
        return false;
    }

    private void simplify() {
        if (this.mNumerator < 0 && this.mDenominator < 0) {
            this.mNumerator *= -1;
            this.mDenominator *= -1;
        } else if (this.mNumerator >= 0 && this.mDenominator < 0) {
            this.mNumerator *= -1;
            this.mDenominator *= -1;
        }
        if (this.mDenominator > 1) {
            int commonDenominator;
            if (this.mDenominator == 2 && this.mNumerator % 2 == 0) {
                commonDenominator = 2;
            } else {
                commonDenominator = gcd(this.mNumerator, this.mDenominator);
            }
            this.mNumerator /= commonDenominator;
            this.mDenominator /= commonDenominator;
        }
    }

    private static int gcd(int u, int v) {
        if (u < 0) {
            u *= -1;
        }
        if (v < 0) {
            v *= -1;
        }
        if (u == 0) {
            return v;
        }
        if (v == 0) {
            return u;
        }
        int shift = 0;
        while (((u | v) & 1) == 0) {
            u >>= 1;
            v >>= 1;
            shift++;
        }
        while ((u & 1) == 0) {
            u >>= 1;
        }
        while (true) {
            if ((v & 1) == 0) {
                v >>= 1;
            } else {
                if (u > v) {
                    int t = v;
                    v = u;
                    u = t;
                }
                v -= u;
                if (v == 0) {
                    return u << shift;
                }
            }
        }
    }

    public boolean isOne() {
        return this.mNumerator == 1 && this.mDenominator == 1;
    }

    public boolean isMinusOne() {
        return this.mNumerator == -1 && this.mDenominator == 1;
    }

    public boolean isPositive() {
        return this.mNumerator >= 0 && this.mDenominator >= 0;
    }

    public boolean isNegative() {
        return this.mNumerator < 0;
    }

    public boolean isNull() {
        return this.mNumerator == 0;
    }

    public void setToZero() {
        this.mNumerator = 0;
        this.mDenominator = 1;
    }

    public float toFloat() {
        if (this.mDenominator >= 1) {
            return ((float) this.mNumerator) / ((float) this.mDenominator);
        }
        return 0.0f;
    }

    public String toString() {
        if (this.mDenominator == 1) {
            if (this.mNumerator == 1 || this.mNumerator == -1) {
                return "";
            }
            if (this.mNumerator < 0) {
                return "" + (this.mNumerator * -1);
            }
            return "" + this.mNumerator;
        } else if (this.mNumerator < 0) {
            return "" + (this.mNumerator * -1) + "/" + this.mDenominator;
        } else {
            return "" + this.mNumerator + "/" + this.mDenominator;
        }
    }

    public String valueString() {
        if (this.mDenominator == 1) {
            return "" + this.mNumerator;
        }
        return "" + this.mNumerator + "/" + this.mDenominator;
    }
}
