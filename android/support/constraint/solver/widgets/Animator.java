package android.support.constraint.solver.widgets;

public class Animator {
    private static final boolean DEBUG = false;
    private static final boolean USE_EASE_IN_OUT = true;
    private static boolean sAllowsAnimation = false;
    private Frame animCurrent = new Frame();
    private long animDuration = 350;
    private Frame animStart = new Frame();
    private long animStartTime = 0;
    private Frame animTarget = new Frame();
    private boolean mAnimating = false;
    private final ConstraintWidget mWidget;

    static class Frame {
        int bottom;
        int left;
        int right;
        int top;

        Frame() {
        }

        void set(int l, int t, int r, int b) {
            this.left = l;
            this.top = t;
            this.right = r;
            this.bottom = b;
        }

        public int getLeft() {
            return this.left;
        }

        public int getTop() {
            return this.top;
        }

        public int getRight() {
            return this.right;
        }

        public int getBottom() {
            return this.bottom;
        }
    }

    public static void setAnimationEnabled(boolean value) {
        sAllowsAnimation = value;
    }

    public static boolean doAnimation() {
        return sAllowsAnimation;
    }

    public static boolean isAnimationEnabled() {
        return sAllowsAnimation;
    }

    public Animator(ConstraintWidget widget) {
        this.mWidget = widget;
    }

    public boolean isAnimating() {
        return this.mAnimating;
    }

    public void start() {
        this.animStartTime = System.currentTimeMillis();
        this.mAnimating = USE_EASE_IN_OUT;
    }

    public static double EaseInOutinterpolator(double progress, double begin, double end) {
        double change = (end - begin) / 2.0d;
        progress *= 2.0d;
        if (progress < 1.0d) {
            return ((change * progress) * progress) + begin;
        }
        progress -= 1.0d;
        return ((-change) * (((progress - 2.0d) * progress) - 1.0d)) + begin;
    }

    private static float linearInterpolator(float progress, float begin, float end) {
        return (end * progress) + ((1.0f - progress) * begin);
    }

    private static int interpolator(float progress, float begin, float end) {
        return (int) EaseInOutinterpolator((double) progress, (double) begin, (double) end);
    }

    public void step() {
        long currentTime = System.currentTimeMillis();
        if (currentTime > this.animStartTime + this.animDuration || currentTime < this.animStartTime) {
            this.animCurrent.left = this.animTarget.left;
            this.animCurrent.top = this.animTarget.top;
            this.animCurrent.right = this.animTarget.right;
            this.animCurrent.bottom = this.animTarget.bottom;
            this.mAnimating = false;
            return;
        }
        float progress = ((float) (currentTime - this.animStartTime)) / ((float) this.animDuration);
        this.animCurrent.left = interpolator(progress, (float) this.animStart.left, (float) this.animTarget.left);
        this.animCurrent.right = interpolator(progress, (float) this.animStart.right, (float) this.animTarget.right);
        this.animCurrent.top = interpolator(progress, (float) this.animStart.top, (float) this.animTarget.top);
        this.animCurrent.bottom = interpolator(progress, (float) this.animStart.bottom, (float) this.animTarget.bottom);
    }

    public void animate(int left, int top, int right, int bottom) {
        this.animCurrent.set(left, top, right, bottom);
        if (!(isAnimating() || (left == this.mWidget.getInternalDrawX() && top == this.mWidget.getInternalDrawY() && right == this.mWidget.getInternalDrawRight() && bottom == this.mWidget.getInternalDrawBottom()))) {
            this.animStart.set(this.mWidget.getInternalDrawX(), this.mWidget.getInternalDrawY(), this.mWidget.getInternalDrawRight(), this.mWidget.getInternalDrawBottom());
            start();
        }
        if (isAnimating()) {
            this.animTarget.set(left, top, right, bottom);
            step();
        }
    }

    public int getCurrentLeft() {
        return this.animCurrent.left;
    }

    public int getCurrentTop() {
        return this.animCurrent.top;
    }

    public int getCurrentRight() {
        return this.animCurrent.right;
    }

    public int getCurrentBottom() {
        return this.animCurrent.bottom;
    }
}
