package android.support.constraint.solver.widgets;

import android.support.constraint.solver.ArrayRow;
import android.support.constraint.solver.LinearSystem;
import android.support.constraint.solver.SolverVariable;
import android.support.constraint.solver.widgets.ConstraintAnchor.Type;
import android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour;
import java.util.ArrayList;
import java.util.Arrays;

public class ConstraintWidgetContainer extends WidgetContainer {
    static boolean ALLOW_ROOT_GROUP = true;
    private static final boolean DEBUG = false;
    private static final boolean USE_DIRECT_CHAIN_RESOLUTION = true;
    private static final boolean USE_SNAPSHOT = true;
    private static final boolean USE_THREAD = false;
    protected LinearSystem mBackgroundSystem;
    private ConstraintWidget[] mHorizontalChainsArray;
    private int mHorizontalChainsSize;
    private ConstraintWidget[] mMatchConstraintsChainedWidgets;
    int mPaddingBottom;
    int mPaddingLeft;
    int mPaddingRight;
    int mPaddingTop;
    private Snapshot mSnapshot;
    protected LinearSystem mSystem;
    private ConstraintWidget[] mVerticalChainsArray;
    private int mVerticalChainsSize;
    int mWrapHeight;
    int mWrapWidth;

    public ConstraintWidgetContainer() {
        this.mSystem = new LinearSystem();
        this.mBackgroundSystem = null;
        this.mHorizontalChainsSize = 0;
        this.mVerticalChainsSize = 0;
        this.mMatchConstraintsChainedWidgets = new ConstraintWidget[4];
        this.mVerticalChainsArray = new ConstraintWidget[4];
        this.mHorizontalChainsArray = new ConstraintWidget[4];
    }

    public ConstraintWidgetContainer(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.mSystem = new LinearSystem();
        this.mBackgroundSystem = null;
        this.mHorizontalChainsSize = 0;
        this.mVerticalChainsSize = 0;
        this.mMatchConstraintsChainedWidgets = new ConstraintWidget[4];
        this.mVerticalChainsArray = new ConstraintWidget[4];
        this.mHorizontalChainsArray = new ConstraintWidget[4];
    }

    public ConstraintWidgetContainer(int width, int height) {
        super(width, height);
        this.mSystem = new LinearSystem();
        this.mBackgroundSystem = null;
        this.mHorizontalChainsSize = 0;
        this.mVerticalChainsSize = 0;
        this.mMatchConstraintsChainedWidgets = new ConstraintWidget[4];
        this.mVerticalChainsArray = new ConstraintWidget[4];
        this.mHorizontalChainsArray = new ConstraintWidget[4];
    }

    public String getType() {
        return "ConstraintLayout";
    }

    public void reset() {
        this.mSystem.reset();
        this.mPaddingLeft = 0;
        this.mPaddingRight = 0;
        this.mPaddingTop = 0;
        this.mPaddingBottom = 0;
        super.reset();
    }

    public static ConstraintWidgetContainer createContainer(ConstraintWidgetContainer container, String name, ArrayList<ConstraintWidget> widgets, int padding) {
        Rectangle bounds = WidgetContainer.getBounds(widgets);
        if (bounds.width == 0 || bounds.height == 0) {
            return null;
        }
        if (padding > 0) {
            int maxPadding = Math.min(bounds.f4x, bounds.f5y);
            if (padding > maxPadding) {
                padding = maxPadding;
            }
            bounds.grow(padding, padding);
        }
        container.setOrigin(bounds.f4x, bounds.f5y);
        container.setDimension(bounds.width, bounds.height);
        container.setDebugName(name);
        ConstraintWidget parent = ((ConstraintWidget) widgets.get(0)).getParent();
        int widgetsSize = widgets.size();
        for (int i = 0; i < widgetsSize; i++) {
            ConstraintWidget widget = (ConstraintWidget) widgets.get(i);
            if (widget.getParent() == parent) {
                container.add(widget);
                widget.setX(widget.getX() - bounds.f4x);
                widget.setY(widget.getY() - bounds.f5y);
            }
        }
        return container;
    }

    public void addChildrenToSolver(LinearSystem system, int group) {
        addToSolver(system, group);
        int count = this.mChildren.size();
        for (int i = 0; i < count; i++) {
            ConstraintWidget widget = (ConstraintWidget) this.mChildren.get(i);
            if (widget instanceof ConstraintWidgetContainer) {
                DimensionBehaviour horizontalBehaviour = widget.mHorizontalDimensionBehaviour;
                DimensionBehaviour verticalBehaviour = widget.mVerticalDimensionBehaviour;
                if (horizontalBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                    widget.setHorizontalDimensionBehaviour(DimensionBehaviour.FIXED);
                }
                if (verticalBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                    widget.setVerticalDimensionBehaviour(DimensionBehaviour.FIXED);
                }
                widget.addToSolver(system, group);
                if (horizontalBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                    widget.setHorizontalDimensionBehaviour(horizontalBehaviour);
                }
                if (verticalBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                    widget.setVerticalDimensionBehaviour(verticalBehaviour);
                }
            } else {
                widget.addToSolver(system, group);
            }
        }
        if (this.mHorizontalChainsSize > 0) {
            applyHorizontalChain(system);
        }
        if (this.mVerticalChainsSize > 0) {
            applyVerticalChain(system);
        }
    }

    private void applyHorizontalChain(LinearSystem system) {
        for (int i = 0; i < this.mHorizontalChainsSize; i++) {
            ConstraintWidget first = this.mHorizontalChainsArray[i];
            int numMatchConstraints = countMatchConstraintsChainedWidgets(this.mHorizontalChainsArray[i], 0);
            boolean chainPacked = first.mHorizontalChainPacked && numMatchConstraints == 0;
            ConstraintWidget widget = first;
            if (widget.mHorizontalChainFixedPosition && !chainPacked) {
                applyDirectResolutionHorizontalChain(system, numMatchConstraints, widget);
            } else if (numMatchConstraints == 0) {
                int leftMargin;
                int rightMargin;
                previous = null;
                while (true) {
                    if (previous != null && (widget.mLeft.mTarget == null || widget.mLeft.mTarget.mOwner != previous)) {
                        break;
                    }
                    leftMargin = widget.mLeft.getMargin();
                    rightMargin = widget.mRight.getMargin();
                    left = widget.mLeft.mSolverVariable;
                    SolverVariable leftTarget = widget.mLeft.mTarget != null ? widget.mLeft.mTarget.mSolverVariable : null;
                    right = widget.mRight.mSolverVariable;
                    SolverVariable rightTarget = widget.mRight.mTarget != null ? widget.mRight.mTarget.mSolverVariable : null;
                    margin = leftMargin;
                    if (previous != null) {
                        margin += previous.mRight.getMargin();
                    }
                    if (leftTarget != null) {
                        if (!chainPacked || widget == first) {
                            system.addGreaterThan(left, leftTarget, margin);
                        } else {
                            system.addEquality(left, leftTarget, margin);
                        }
                    }
                    if (rightTarget != null) {
                        margin = rightMargin;
                        ConstraintAnchor nextLeft = widget.mRight.mTarget.mOwner.mLeft;
                        ConstraintWidget nextLeftTarget = nextLeft.mTarget != null ? nextLeft.mTarget.mOwner : null;
                        if (nextLeftTarget == widget) {
                            margin += nextLeft.getMargin();
                        }
                        if (chainPacked && nextLeftTarget == widget) {
                            system.addEquality(right, rightTarget, -margin);
                        } else {
                            system.addLowerThan(right, rightTarget, -margin);
                        }
                        if (!(chainPacked || leftTarget == null)) {
                            system.addCentering(left, leftTarget, leftMargin, 0.5f, rightTarget, right, rightMargin);
                        }
                    }
                    previous = widget;
                    if (rightTarget == null) {
                        break;
                    }
                    widget = widget.mRight.mTarget.mOwner;
                }
                if (chainPacked) {
                    leftMargin = first.mLeft.getMargin();
                    rightMargin = previous.mRight.getMargin();
                    system.addCentering(first.mLeft.mSolverVariable, first.mLeft.mTarget != null ? first.mLeft.mTarget.mSolverVariable : null, leftMargin, first.mHorizontalBiasPercent, previous.mRight.mTarget != null ? previous.mRight.mTarget.mSolverVariable : null, previous.mRight.mSolverVariable, rightMargin);
                }
            } else {
                previous = null;
                float totalWeights = 0.0f;
                while (true) {
                    if (previous == null || (widget.mLeft.mTarget != null && widget.mLeft.mTarget.mOwner == previous)) {
                        if (widget.mHorizontalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT) {
                            margin = widget.mLeft.getMargin();
                            if (previous != null) {
                                margin += previous.mRight.getMargin();
                            }
                            system.addGreaterThan(widget.mLeft.mSolverVariable, widget.mLeft.mTarget.mSolverVariable, margin);
                            margin = widget.mRight.getMargin();
                            if (widget.mRight.mTarget.mOwner.mLeft.mTarget != null && widget.mRight.mTarget.mOwner.mLeft.mTarget.mOwner == widget) {
                                margin += widget.mRight.mTarget.mOwner.mLeft.getMargin();
                            }
                            system.addLowerThan(widget.mRight.mSolverVariable, widget.mRight.mTarget.mSolverVariable, -margin);
                        } else {
                            totalWeights += widget.mHorizontalWeight;
                        }
                        previous = widget;
                        widget = widget.mRight.mTarget.mOwner;
                    }
                }
                if (numMatchConstraints == 1) {
                    ConstraintWidget w = this.mMatchConstraintsChainedWidgets[0];
                    system.addEquality(w.mLeft.mSolverVariable, w.mLeft.mTarget.mSolverVariable, w.mLeft.getMargin());
                    system.addEquality(w.mRight.mSolverVariable, w.mRight.mTarget.mSolverVariable, w.mRight.getMargin() * -1);
                    system.addLowerThan(w.mRight.mSolverVariable, w.mRight.mTarget.mSolverVariable, 0);
                } else {
                    for (int j = 0; j < numMatchConstraints - 1; j++) {
                        ConstraintWidget current = this.mMatchConstraintsChainedWidgets[j];
                        ConstraintWidget nextWidget = this.mMatchConstraintsChainedWidgets[j + 1];
                        left = current.mLeft.mSolverVariable;
                        right = current.mRight.mSolverVariable;
                        SolverVariable nextLeft2 = nextWidget.mLeft.mSolverVariable;
                        SolverVariable nextRight = nextWidget.mRight.mSolverVariable;
                        margin = current.mLeft.getMargin();
                        if (!(current.mLeft.mTarget == null || current.mLeft.mTarget.mOwner.mRight.mTarget == null || current.mLeft.mTarget.mOwner.mRight.mTarget.mOwner != current)) {
                            margin += current.mLeft.mTarget.mOwner.mRight.getMargin();
                        }
                        system.addGreaterThan(left, current.mLeft.mTarget.mSolverVariable, margin);
                        margin = current.mRight.getMargin();
                        if (!(current.mRight.mTarget == null || current.mRight.mTarget.mOwner.mLeft.mTarget == null || current.mRight.mTarget.mOwner.mLeft.mTarget.mOwner != current)) {
                            margin += current.mRight.mTarget.mOwner.mLeft.getMargin();
                        }
                        system.addLowerThan(right, current.mRight.mTarget.mSolverVariable, -margin);
                        if (j + 1 == numMatchConstraints - 1) {
                            margin = nextWidget.mLeft.getMargin();
                            if (!(nextWidget.mLeft.mTarget == null || nextWidget.mLeft.mTarget.mOwner.mRight.mTarget == null || nextWidget.mLeft.mTarget.mOwner.mRight.mTarget.mOwner != nextWidget)) {
                                margin += nextWidget.mLeft.mTarget.mOwner.mRight.getMargin();
                            }
                            system.addGreaterThan(nextLeft2, nextWidget.mLeft.mTarget.mSolverVariable, margin);
                            margin = nextWidget.mRight.getMargin();
                            if (!(nextWidget.mRight.mTarget == null || nextWidget.mRight.mTarget.mOwner.mLeft.mTarget == null || nextWidget.mRight.mTarget.mOwner.mLeft.mTarget.mOwner != nextWidget)) {
                                margin += nextWidget.mRight.mTarget.mOwner.mLeft.getMargin();
                            }
                            system.addLowerThan(nextRight, nextWidget.mRight.mTarget.mSolverVariable, -margin);
                        }
                        ArrayRow row = system.createRow();
                        row.createRowEqualDimension(current.mHorizontalWeight, totalWeights, nextWidget.mHorizontalWeight, left, current.mLeft.getMargin(), right, current.mRight.getMargin(), nextLeft2, nextWidget.mLeft.getMargin(), nextRight, nextWidget.mRight.getMargin());
                        system.addConstraint(row);
                    }
                }
            }
        }
    }

    private void applyDirectResolutionHorizontalChain(LinearSystem system, int numMatchConstraints, ConstraintWidget widget) {
        ConstraintWidget firstWidget = widget;
        int widgetSize = 0;
        ConstraintWidget previous = null;
        int count = 0;
        float totalWeights = 0.0f;
        while (widget != null) {
            count++;
            if (widget.mHorizontalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT) {
                widgetSize = ((widgetSize + widget.getWidth()) + (widget.mLeft.mTarget != null ? widget.mLeft.getMargin() : 0)) + (widget.mRight.mTarget != null ? widget.mRight.getMargin() : 0);
            } else {
                totalWeights += widget.mHorizontalWeight;
            }
            previous = widget;
            widget = widget.mRight.mTarget != null ? widget.mRight.mTarget.mOwner : null;
            if (widget != null && (widget.mLeft.mTarget == null || !(widget.mLeft.mTarget == null || widget.mLeft.mTarget.mOwner == previous))) {
                widget = null;
            }
        }
        int lastPosition = 0;
        if (previous != null) {
            lastPosition = previous.mRight.mTarget != null ? previous.mRight.mTarget.mOwner.getX() : 0;
            if (previous.mRight.mTarget != null && previous.mRight.mTarget.mOwner == this) {
                lastPosition = getRight();
            }
        }
        float spreadSpace = ((float) (lastPosition - 0)) - ((float) widgetSize);
        float split = spreadSpace / ((float) (count + 1));
        widget = firstWidget;
        float currentPosition = 0.0f;
        if (numMatchConstraints == 0) {
            currentPosition = split;
        } else {
            split = spreadSpace / ((float) numMatchConstraints);
        }
        while (widget != null) {
            int left = widget.mLeft.mTarget != null ? widget.mLeft.getMargin() : 0;
            int right = widget.mRight.mTarget != null ? widget.mRight.getMargin() : 0;
            currentPosition += (float) left;
            system.addEquality(widget.mLeft.mSolverVariable, (int) currentPosition);
            if (widget.mHorizontalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT) {
                currentPosition += (float) widget.getWidth();
            } else if (totalWeights == 0.0f) {
                currentPosition += (split - ((float) left)) - ((float) right);
            } else {
                currentPosition += (((widget.mHorizontalWeight * spreadSpace) / totalWeights) - ((float) left)) - ((float) right);
            }
            system.addEquality(widget.mRight.mSolverVariable, (int) currentPosition);
            if (numMatchConstraints == 0) {
                currentPosition += split;
            }
            currentPosition += (float) right;
            previous = widget;
            widget = widget.mRight.mTarget != null ? widget.mRight.mTarget.mOwner : null;
            if (!(widget == null || widget.mLeft.mTarget == null || widget.mLeft.mTarget.mOwner == previous)) {
                widget = null;
            }
            if (widget == this) {
                widget = null;
            }
        }
    }

    private void applyVerticalChain(LinearSystem system) {
        for (int i = 0; i < this.mVerticalChainsSize; i++) {
            ConstraintWidget first = this.mVerticalChainsArray[i];
            int numMatchConstraints = countMatchConstraintsChainedWidgets(this.mVerticalChainsArray[i], 1);
            boolean chainPacked = first.mVerticalChainPacked && numMatchConstraints == 0;
            ConstraintWidget widget = first;
            if (widget.mVerticalChainFixedPosition && !chainPacked) {
                applyDirectResolutionVerticalChain(system, numMatchConstraints, widget);
            } else if (numMatchConstraints == 0) {
                int topMargin;
                int bottomMargin;
                previous = null;
                while (true) {
                    if (previous != null && (widget.mTop.mTarget == null || widget.mTop.mTarget.mOwner != previous)) {
                        break;
                    }
                    topMargin = widget.mTop.getMargin();
                    bottomMargin = widget.mBottom.getMargin();
                    top = widget.mTop.mSolverVariable;
                    SolverVariable topTarget = widget.mTop.mTarget != null ? widget.mTop.mTarget.mSolverVariable : null;
                    bottom = widget.mBottom.mSolverVariable;
                    SolverVariable bottomTarget = widget.mBottom.mTarget != null ? widget.mBottom.mTarget.mSolverVariable : null;
                    margin = topMargin;
                    if (previous != null) {
                        margin += previous.mBottom.getMargin();
                    }
                    if (topTarget != null) {
                        if (!chainPacked || widget == first) {
                            system.addGreaterThan(top, topTarget, margin);
                        } else {
                            system.addEquality(top, topTarget, margin);
                        }
                    }
                    if (bottomTarget != null) {
                        margin = bottomMargin;
                        ConstraintAnchor nextTop = widget.mBottom.mTarget.mOwner.mTop;
                        ConstraintWidget nextTopTarget = nextTop.mTarget != null ? nextTop.mTarget.mOwner : null;
                        if (nextTopTarget == widget) {
                            margin += nextTop.getMargin();
                        }
                        if (chainPacked && nextTopTarget == widget) {
                            system.addEquality(bottom, bottomTarget, -margin);
                        } else {
                            system.addLowerThan(bottom, bottomTarget, -margin);
                        }
                        if (!(chainPacked || topTarget == null)) {
                            system.addCentering(top, topTarget, topMargin, 0.5f, bottomTarget, bottom, bottomMargin);
                        }
                    }
                    previous = widget;
                    if (bottomTarget == null) {
                        break;
                    }
                    widget = widget.mBottom.mTarget.mOwner;
                }
                if (chainPacked) {
                    topMargin = first.mTop.getMargin();
                    bottomMargin = previous.mBottom.getMargin();
                    system.addCentering(first.mTop.mSolverVariable, first.mTop.mTarget != null ? first.mTop.mTarget.mSolverVariable : null, topMargin, first.mVerticalBiasPercent, previous.mBottom.mTarget != null ? previous.mBottom.mTarget.mSolverVariable : null, previous.mBottom.mSolverVariable, bottomMargin);
                }
            } else {
                previous = null;
                float totalWeights = 0.0f;
                while (true) {
                    if (previous == null || (widget.mTop.mTarget != null && widget.mTop.mTarget.mOwner == previous)) {
                        if (widget.mVerticalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT) {
                            margin = widget.mTop.getMargin();
                            if (previous != null) {
                                margin += previous.mBottom.getMargin();
                            }
                            system.addGreaterThan(widget.mTop.mSolverVariable, widget.mTop.mTarget.mSolverVariable, margin);
                            margin = widget.mBottom.getMargin();
                            if (widget.mBottom.mTarget.mOwner.mTop.mTarget != null && widget.mBottom.mTarget.mOwner.mTop.mTarget.mOwner == widget) {
                                margin += widget.mBottom.mTarget.mOwner.mTop.getMargin();
                            }
                            system.addLowerThan(widget.mBottom.mSolverVariable, widget.mBottom.mTarget.mSolverVariable, -margin);
                        } else {
                            totalWeights += widget.mVerticalWeight;
                        }
                        previous = widget;
                        widget = widget.mBottom.mTarget.mOwner;
                    }
                }
                if (numMatchConstraints == 1) {
                    ConstraintWidget w = this.mMatchConstraintsChainedWidgets[0];
                    system.addEquality(w.mTop.mSolverVariable, w.mTop.mTarget.mSolverVariable, w.mTop.getMargin());
                    system.addEquality(w.mBottom.mSolverVariable, w.mBottom.mTarget.mSolverVariable, w.mBottom.getMargin() * -1);
                    system.addLowerThan(w.mBottom.mSolverVariable, w.mBottom.mTarget.mSolverVariable, 0);
                } else {
                    for (int j = 0; j < numMatchConstraints - 1; j++) {
                        ConstraintWidget current = this.mMatchConstraintsChainedWidgets[j];
                        ConstraintWidget nextWidget = this.mMatchConstraintsChainedWidgets[j + 1];
                        top = current.mTop.mSolverVariable;
                        bottom = current.mBottom.mSolverVariable;
                        SolverVariable nextLeft = nextWidget.mTop.mSolverVariable;
                        SolverVariable nextRight = nextWidget.mBottom.mSolverVariable;
                        margin = current.mTop.getMargin();
                        if (!(current.mTop.mTarget == null || current.mTop.mTarget.mOwner.mBottom.mTarget == null || current.mTop.mTarget.mOwner.mBottom.mTarget.mOwner != current)) {
                            margin += current.mTop.mTarget.mOwner.mBottom.getMargin();
                        }
                        system.addGreaterThan(top, current.mTop.mTarget.mSolverVariable, margin);
                        margin = current.mBottom.getMargin();
                        if (!(current.mBottom.mTarget == null || current.mBottom.mTarget.mOwner.mTop.mTarget == null || current.mBottom.mTarget.mOwner.mTop.mTarget.mOwner != current)) {
                            margin += current.mBottom.mTarget.mOwner.mTop.getMargin();
                        }
                        system.addLowerThan(bottom, current.mBottom.mTarget.mSolverVariable, -margin);
                        if (j + 1 == numMatchConstraints - 1) {
                            margin = nextWidget.mTop.getMargin();
                            if (!(nextWidget.mTop.mTarget == null || nextWidget.mTop.mTarget.mOwner.mBottom.mTarget == null || nextWidget.mTop.mTarget.mOwner.mBottom.mTarget.mOwner != nextWidget)) {
                                margin += nextWidget.mTop.mTarget.mOwner.mBottom.getMargin();
                            }
                            system.addGreaterThan(nextLeft, nextWidget.mTop.mTarget.mSolverVariable, margin);
                            margin = nextWidget.mBottom.getMargin();
                            if (!(nextWidget.mBottom.mTarget == null || nextWidget.mBottom.mTarget.mOwner.mTop.mTarget == null || nextWidget.mBottom.mTarget.mOwner.mTop.mTarget.mOwner != nextWidget)) {
                                margin += nextWidget.mBottom.mTarget.mOwner.mTop.getMargin();
                            }
                            system.addLowerThan(nextRight, nextWidget.mBottom.mTarget.mSolverVariable, -margin);
                        }
                        ArrayRow row = system.createRow();
                        row.createRowEqualDimension(current.mVerticalWeight, totalWeights, nextWidget.mVerticalWeight, top, current.mTop.getMargin(), bottom, current.mBottom.getMargin(), nextLeft, nextWidget.mTop.getMargin(), nextRight, nextWidget.mBottom.getMargin());
                        system.addConstraint(row);
                    }
                }
            }
        }
    }

    private void applyDirectResolutionVerticalChain(LinearSystem system, int numMatchConstraints, ConstraintWidget widget) {
        ConstraintWidget firstWidget = widget;
        int widgetSize = 0;
        ConstraintWidget previous = null;
        int count = 0;
        float totalWeights = 0.0f;
        while (widget != null) {
            count++;
            if (widget.mVerticalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT) {
                widgetSize = ((widgetSize + widget.getHeight()) + (widget.mTop.mTarget != null ? widget.mTop.getMargin() : 0)) + (widget.mBottom.mTarget != null ? widget.mBottom.getMargin() : 0);
            } else {
                totalWeights += widget.mVerticalWeight;
            }
            previous = widget;
            widget = widget.mBottom.mTarget != null ? widget.mBottom.mTarget.mOwner : null;
            if (widget != null && (widget.mTop.mTarget == null || !(widget.mTop.mTarget == null || widget.mTop.mTarget.mOwner == previous))) {
                widget = null;
            }
        }
        int lastPosition = 0;
        if (previous != null) {
            lastPosition = previous.mBottom.mTarget != null ? previous.mBottom.mTarget.mOwner.getX() : 0;
            if (previous.mBottom.mTarget != null && previous.mBottom.mTarget.mOwner == this) {
                lastPosition = getBottom();
            }
        }
        float spreadSpace = ((float) (lastPosition - 0)) - ((float) widgetSize);
        float split = spreadSpace / ((float) (count + 1));
        widget = firstWidget;
        float currentPosition = 0.0f;
        if (numMatchConstraints == 0) {
            currentPosition = split;
        } else {
            split = spreadSpace / ((float) numMatchConstraints);
        }
        while (widget != null) {
            int top = widget.mTop.mTarget != null ? widget.mTop.getMargin() : 0;
            int bottom = widget.mBottom.mTarget != null ? widget.mBottom.getMargin() : 0;
            currentPosition += (float) top;
            system.addEquality(widget.mTop.mSolverVariable, (int) currentPosition);
            if (widget.mVerticalDimensionBehaviour != DimensionBehaviour.MATCH_CONSTRAINT) {
                currentPosition += (float) widget.getHeight();
            } else if (totalWeights == 0.0f) {
                currentPosition += (split - ((float) top)) - ((float) bottom);
            } else {
                currentPosition += (((widget.mVerticalWeight * spreadSpace) / totalWeights) - ((float) top)) - ((float) bottom);
            }
            system.addEquality(widget.mBottom.mSolverVariable, (int) currentPosition);
            if (numMatchConstraints == 0) {
                currentPosition += split;
            }
            currentPosition += (float) bottom;
            previous = widget;
            widget = widget.mBottom.mTarget != null ? widget.mBottom.mTarget.mOwner : null;
            if (!(widget == null || widget.mTop.mTarget == null || widget.mTop.mTarget.mOwner == previous)) {
                widget = null;
            }
            if (widget == this) {
                widget = null;
            }
        }
    }

    public void updateChildrenFromSolver(LinearSystem system, int group) {
        updateFromSolver(system, group);
        int count = this.mChildren.size();
        for (int i = 0; i < count; i++) {
            ((ConstraintWidget) this.mChildren.get(i)).updateFromSolver(system, group);
        }
    }

    public void setPadding(int left, int top, int right, int bottom) {
        this.mPaddingLeft = left;
        this.mPaddingTop = top;
        this.mPaddingRight = right;
        this.mPaddingBottom = bottom;
    }

    public void layout() {
        int prex = this.mX;
        int prey = this.mY;
        int prew = getWidth();
        int preh = getHeight();
        if (this.mParent != null) {
            if (this.mSnapshot == null) {
                this.mSnapshot = new Snapshot(this);
            }
            this.mSnapshot.updateFrom(this);
            setX(this.mPaddingLeft);
            setY(this.mPaddingTop);
            resetAnchors();
            resetSolverVariables(this.mSystem.getCache());
        } else {
            this.mX = 0;
            this.mY = 0;
        }
        resetChains();
        int count = this.mChildren.size();
        for (int i = 0; i < count; i++) {
            ConstraintWidget widget = (ConstraintWidget) this.mChildren.get(i);
            if (widget instanceof WidgetContainer) {
                ((WidgetContainer) widget).layout();
            }
        }
        try {
            this.mSystem.reset();
            addChildrenToSolver(this.mSystem, Integer.MAX_VALUE);
            this.mSystem.minimize();
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateChildrenFromSolver(this.mSystem, Integer.MAX_VALUE);
        if (this.mParent != null) {
            int width = getWidth();
            int height = getHeight();
            this.mSnapshot.applyTo(this);
            setWidth((this.mPaddingLeft + width) + this.mPaddingRight);
            setHeight((this.mPaddingTop + height) + this.mPaddingBottom);
        } else {
            this.mX = prex;
            this.mY = prey;
        }
        resetSolverVariables(this.mSystem.getCache());
        if (this == getRootConstraintContainer()) {
            updateDrawPosition();
        }
    }

    static int setGroup(ConstraintAnchor anchor, int group) {
        int oldGroup = anchor.mGroup;
        if (anchor.mOwner.getParent() == null) {
            return group;
        }
        if (oldGroup <= group) {
            return oldGroup;
        }
        anchor.mGroup = group;
        ConstraintAnchor opposite = anchor.getOpposite();
        ConstraintAnchor target = anchor.mTarget;
        if (opposite != null) {
            group = setGroup(opposite, group);
        }
        if (target != null) {
            group = setGroup(target, group);
        }
        if (opposite != null) {
            group = setGroup(opposite, group);
        }
        anchor.mGroup = group;
        return group;
    }

    public int layoutFindGroupsSimple() {
        int size = this.mChildren.size();
        for (int j = 0; j < size; j++) {
            ConstraintWidget widget = (ConstraintWidget) this.mChildren.get(j);
            widget.mLeft.mGroup = 0;
            widget.mRight.mGroup = 0;
            widget.mTop.mGroup = 1;
            widget.mBottom.mGroup = 1;
            widget.mBaseline.mGroup = 1;
        }
        return 2;
    }

    public void findWrapRecursive(ConstraintWidget widget) {
        int w = widget.getWrapWidth();
        int distToRight = w;
        int distToLeft = w;
        ConstraintWidget leftWidget = null;
        ConstraintWidget rightWidget = null;
        widget.mVisited = true;
        if (widget.mRight.isConnected() || widget.mLeft.isConnected()) {
            if (widget.mRight.mTarget != null) {
                rightWidget = widget.mRight.mTarget.getOwner();
                distToRight += widget.mRight.getMargin();
                if (!(rightWidget.isRoot() || rightWidget.mVisited)) {
                    findWrapRecursive(rightWidget);
                }
            }
            if (widget.mLeft.isConnected()) {
                leftWidget = widget.mLeft.mTarget.getOwner();
                distToLeft += widget.mLeft.getMargin();
                if (!(leftWidget.isRoot() || leftWidget.mVisited)) {
                    findWrapRecursive(leftWidget);
                }
            }
            if (!(widget.mRight.mTarget == null || rightWidget.isRoot())) {
                if (widget.mRight.mTarget.mType == Type.RIGHT) {
                    distToRight += rightWidget.mDistToRight - rightWidget.getWrapWidth();
                } else if (widget.mRight.mTarget.getType() == Type.LEFT) {
                    distToRight += rightWidget.mDistToRight;
                }
            }
            if (!(widget.mLeft.mTarget == null || leftWidget.isRoot())) {
                if (widget.mLeft.mTarget.getType() == Type.LEFT) {
                    distToLeft += leftWidget.mDistToLeft - leftWidget.getWrapWidth();
                } else if (widget.mLeft.mTarget.getType() == Type.RIGHT) {
                    distToLeft += leftWidget.mDistToLeft;
                }
            }
        } else {
            distToLeft += widget.getX();
        }
        widget.mDistToLeft = distToLeft;
        widget.mDistToRight = distToRight;
        int h = widget.getWrapHeight();
        int distToTop = h;
        int distToBottom = h;
        ConstraintWidget topWidget = null;
        if (widget.mBaseline.mTarget == null && widget.mTop.mTarget == null && widget.mBottom.mTarget == null) {
            distToTop += widget.getY();
        } else if (widget.mBaseline.isConnected()) {
            ConstraintWidget baseLineWidget = widget.mBaseline.mTarget.getOwner();
            if (!baseLineWidget.mVisited) {
                findWrapRecursive(baseLineWidget);
            }
            if (baseLineWidget.mDistToBottom > distToBottom) {
                distToBottom = baseLineWidget.mDistToBottom;
            }
            if (baseLineWidget.mDistToTop > distToTop) {
                distToTop = baseLineWidget.mDistToTop;
            }
            widget.mDistToTop = distToTop;
            widget.mDistToBottom = distToBottom;
            return;
        } else {
            if (widget.mTop.isConnected()) {
                topWidget = widget.mTop.mTarget.getOwner();
                distToTop += widget.mTop.getMargin();
                if (!(topWidget.isRoot() || topWidget.mVisited)) {
                    findWrapRecursive(topWidget);
                }
            }
            ConstraintWidget bottomWidget = null;
            if (widget.mBottom.isConnected()) {
                bottomWidget = widget.mBottom.mTarget.getOwner();
                distToBottom += widget.mBottom.getMargin();
                if (!(bottomWidget.isRoot() || bottomWidget.mVisited)) {
                    findWrapRecursive(bottomWidget);
                }
            }
            if (!(widget.mTop.mTarget == null || topWidget.isRoot())) {
                if (widget.mTop.mTarget.getType() == Type.TOP) {
                    distToTop += topWidget.mDistToTop - topWidget.getWrapHeight();
                } else if (widget.mTop.mTarget.getType() == Type.BOTTOM) {
                    distToTop += topWidget.mDistToTop;
                }
            }
            if (!(widget.mBottom.mTarget == null || bottomWidget.isRoot())) {
                if (widget.mBottom.mTarget.getType() == Type.BOTTOM) {
                    distToBottom += bottomWidget.mDistToBottom - bottomWidget.getWrapHeight();
                } else if (widget.mBottom.mTarget.getType() == Type.TOP) {
                    distToBottom += bottomWidget.mDistToBottom;
                }
            }
        }
        widget.mDistToTop = distToTop;
        widget.mDistToBottom = distToBottom;
    }

    public void findWrapSize(ArrayList<ConstraintWidget> children) {
        int j;
        int maxTopDist = 0;
        int maxLeftDist = 0;
        int maxRightDist = 0;
        int maxBottomDist = 0;
        int maxConnectWidth = 0;
        int maxConnectHeight = 0;
        int size = children.size();
        for (j = 0; j < size; j++) {
            ConstraintWidget widget = (ConstraintWidget) children.get(j);
            if (!widget.isRoot()) {
                if (!widget.mVisited) {
                    findWrapRecursive(widget);
                }
                int connectWidth = (widget.mDistToLeft + widget.mDistToRight) - widget.getWrapWidth();
                int connectHeight = (widget.mDistToTop + widget.mDistToBottom) - widget.getWrapHeight();
                maxLeftDist = Math.max(maxLeftDist, widget.mDistToLeft);
                maxRightDist = Math.max(maxRightDist, widget.mDistToRight);
                maxBottomDist = Math.max(maxBottomDist, widget.mDistToBottom);
                maxTopDist = Math.max(maxTopDist, widget.mDistToTop);
                maxConnectWidth = Math.max(maxConnectWidth, connectWidth);
                maxConnectHeight = Math.max(maxConnectHeight, connectHeight);
            }
        }
        this.mWrapWidth = Math.max(Math.max(maxLeftDist, maxRightDist), maxConnectWidth);
        this.mWrapHeight = Math.max(Math.max(maxTopDist, maxBottomDist), maxConnectHeight);
        for (j = 0; j < size; j++) {
            ((ConstraintWidget) children.get(j)).mVisited = false;
        }
    }

    public int layoutFindGroups() {
        int j;
        Type[] dir = new Type[]{Type.LEFT, Type.RIGHT, Type.TOP, Type.BASELINE, Type.BOTTOM};
        int label = 1;
        int size = this.mChildren.size();
        for (j = 0; j < size; j++) {
            ConstraintWidget widget = (ConstraintWidget) this.mChildren.get(j);
            ConstraintAnchor anchor = widget.mLeft;
            if (anchor.mTarget == null) {
                anchor.mGroup = Integer.MAX_VALUE;
            } else if (setGroup(anchor, label) == label) {
                label++;
            }
            anchor = widget.mTop;
            if (anchor.mTarget == null) {
                anchor.mGroup = Integer.MAX_VALUE;
            } else if (setGroup(anchor, label) == label) {
                label++;
            }
            anchor = widget.mRight;
            if (anchor.mTarget == null) {
                anchor.mGroup = Integer.MAX_VALUE;
            } else if (setGroup(anchor, label) == label) {
                label++;
            }
            anchor = widget.mBottom;
            if (anchor.mTarget == null) {
                anchor.mGroup = Integer.MAX_VALUE;
            } else if (setGroup(anchor, label) == label) {
                label++;
            }
            anchor = widget.mBaseline;
            if (anchor.mTarget == null) {
                anchor.mGroup = Integer.MAX_VALUE;
            } else if (setGroup(anchor, label) == label) {
                label++;
            }
        }
        boolean notDone = true;
        int count = 0;
        int fix = 0;
        while (notDone) {
            notDone = false;
            count++;
            for (j = 0; j < size; j++) {
                widget = (ConstraintWidget) this.mChildren.get(j);
                for (Type type : dir) {
                    anchor = null;
                    switch (type) {
                        case LEFT:
                            anchor = widget.mLeft;
                            break;
                        case TOP:
                            anchor = widget.mTop;
                            break;
                        case RIGHT:
                            anchor = widget.mRight;
                            break;
                        case BOTTOM:
                            anchor = widget.mBottom;
                            break;
                        case BASELINE:
                            anchor = widget.mBaseline;
                            break;
                    }
                    ConstraintAnchor target = anchor.mTarget;
                    if (target != null) {
                        int i;
                        if (!(target.mOwner.getParent() == null || target.mGroup == anchor.mGroup)) {
                            i = anchor.mGroup > target.mGroup ? target.mGroup : anchor.mGroup;
                            anchor.mGroup = i;
                            target.mGroup = i;
                            fix++;
                            notDone = true;
                        }
                        ConstraintAnchor opposite = target.getOpposite();
                        if (!(opposite == null || opposite.mGroup == anchor.mGroup)) {
                            i = anchor.mGroup > opposite.mGroup ? opposite.mGroup : anchor.mGroup;
                            anchor.mGroup = i;
                            opposite.mGroup = i;
                            fix++;
                            notDone = true;
                        }
                    }
                }
            }
        }
        int[] table = new int[((this.mChildren.size() * dir.length) + 1)];
        Arrays.fill(table, -1);
        j = 0;
        int index = 0;
        while (j < size) {
            int i2;
            widget = (ConstraintWidget) this.mChildren.get(j);
            anchor = widget.mLeft;
            if (anchor.mGroup != Integer.MAX_VALUE) {
                int g;
                g = anchor.mGroup;
                if (table[g] == -1) {
                    i2 = index + 1;
                    table[g] = index;
                } else {
                    i2 = index;
                }
                anchor.mGroup = table[g];
            } else {
                i2 = index;
            }
            anchor = widget.mTop;
            if (anchor.mGroup != Integer.MAX_VALUE) {
                g = anchor.mGroup;
                if (table[g] == -1) {
                    index = i2 + 1;
                    table[g] = i2;
                    i2 = index;
                }
                anchor.mGroup = table[g];
            }
            anchor = widget.mRight;
            if (anchor.mGroup != Integer.MAX_VALUE) {
                g = anchor.mGroup;
                if (table[g] == -1) {
                    index = i2 + 1;
                    table[g] = i2;
                    i2 = index;
                }
                anchor.mGroup = table[g];
            }
            anchor = widget.mBottom;
            if (anchor.mGroup != Integer.MAX_VALUE) {
                g = anchor.mGroup;
                if (table[g] == -1) {
                    index = i2 + 1;
                    table[g] = i2;
                    i2 = index;
                }
                anchor.mGroup = table[g];
            }
            anchor = widget.mBaseline;
            if (anchor.mGroup != Integer.MAX_VALUE) {
                g = anchor.mGroup;
                if (table[g] == -1) {
                    index = i2 + 1;
                    table[g] = i2;
                    i2 = index;
                }
                anchor.mGroup = table[g];
            }
            j++;
            index = i2;
        }
        return index;
    }

    public void layoutWithGroup(int numOfGroups) {
        int i;
        int prex = this.mX;
        int prey = this.mY;
        if (this.mParent != null) {
            if (this.mSnapshot == null) {
                this.mSnapshot = new Snapshot(this);
            }
            this.mSnapshot.updateFrom(this);
            this.mX = 0;
            this.mY = 0;
            resetAnchors();
            resetSolverVariables(this.mSystem.getCache());
        } else {
            this.mX = 0;
            this.mY = 0;
        }
        int count = this.mChildren.size();
        for (i = 0; i < count; i++) {
            ConstraintWidget widget = (ConstraintWidget) this.mChildren.get(i);
            if (widget instanceof WidgetContainer) {
                ((WidgetContainer) widget).layout();
            }
        }
        this.mLeft.mGroup = 0;
        this.mRight.mGroup = 0;
        this.mTop.mGroup = 1;
        this.mBottom.mGroup = 1;
        this.mSystem.reset();
        for (i = 0; i < numOfGroups; i++) {
            try {
                addToSolver(this.mSystem, i);
                this.mSystem.minimize();
                updateFromSolver(this.mSystem, i);
            } catch (Exception e) {
                e.printStackTrace();
            }
            updateFromSolver(this.mSystem, -2);
        }
        if (this.mParent != null) {
            int width = getWidth();
            int height = getHeight();
            this.mSnapshot.applyTo(this);
            setWidth(width);
            setHeight(height);
        } else {
            this.mX = prex;
            this.mY = prey;
        }
        if (this == getRootConstraintContainer()) {
            updateDrawPosition();
        }
    }

    public boolean isAnimating() {
        if (super.isAnimating()) {
            return true;
        }
        int mChildrenSize = this.mChildren.size();
        for (int i = 0; i < mChildrenSize; i++) {
            if (((ConstraintWidget) this.mChildren.get(i)).isAnimating()) {
                return true;
            }
        }
        return false;
    }

    public boolean handlesInternalConstraints() {
        return false;
    }

    public ArrayList<Guideline> getVerticalGuidelines() {
        ArrayList<Guideline> guidelines = new ArrayList();
        int mChildrenSize = this.mChildren.size();
        for (int i = 0; i < mChildrenSize; i++) {
            ConstraintWidget widget = (ConstraintWidget) this.mChildren.get(i);
            if (widget instanceof Guideline) {
                Guideline guideline = (Guideline) widget;
                if (guideline.getOrientation() == 1) {
                    guidelines.add(guideline);
                }
            }
        }
        return guidelines;
    }

    public ArrayList<Guideline> getHorizontalGuidelines() {
        ArrayList<Guideline> guidelines = new ArrayList();
        int mChildrenSize = this.mChildren.size();
        for (int i = 0; i < mChildrenSize; i++) {
            ConstraintWidget widget = (ConstraintWidget) this.mChildren.get(i);
            if (widget instanceof Guideline) {
                Guideline guideline = (Guideline) widget;
                if (guideline.getOrientation() == 0) {
                    guidelines.add(guideline);
                }
            }
        }
        return guidelines;
    }

    public LinearSystem getSystem() {
        return this.mSystem;
    }

    private void resetChains() {
        this.mHorizontalChainsSize = 0;
        this.mVerticalChainsSize = 0;
    }

    void addChain(ConstraintWidget constraintWidget, int type) {
        ConstraintWidget widget = constraintWidget;
        if (type == 0) {
            while (widget.mLeft.mTarget != null && widget.mLeft.mTarget.mOwner.mRight.mTarget != null && widget.mLeft.mTarget.mOwner.mRight.mTarget.mOwner == widget) {
                widget = widget.mLeft.mTarget.mOwner;
            }
            addHorizontalChain(widget);
        } else if (type == 1) {
            while (widget.mTop.mTarget != null && widget.mTop.mTarget.mOwner.mBottom.mTarget != null && widget.mTop.mTarget.mOwner.mBottom.mTarget.mOwner == widget) {
                widget = widget.mTop.mTarget.mOwner;
            }
            addVerticalChain(widget);
        }
    }

    private void addHorizontalChain(ConstraintWidget widget) {
        int i = 0;
        while (i < this.mHorizontalChainsSize) {
            if (this.mHorizontalChainsArray[i] != widget) {
                i++;
            } else {
                return;
            }
        }
        if (this.mHorizontalChainsSize + 1 >= this.mHorizontalChainsArray.length) {
            this.mHorizontalChainsArray = (ConstraintWidget[]) Arrays.copyOf(this.mHorizontalChainsArray, this.mHorizontalChainsArray.length * 2);
        }
        this.mHorizontalChainsArray[this.mHorizontalChainsSize] = widget;
        this.mHorizontalChainsSize++;
    }

    private void addVerticalChain(ConstraintWidget widget) {
        int i = 0;
        while (i < this.mVerticalChainsSize) {
            if (this.mVerticalChainsArray[i] != widget) {
                i++;
            } else {
                return;
            }
        }
        if (this.mVerticalChainsSize + 1 >= this.mVerticalChainsArray.length) {
            this.mVerticalChainsArray = (ConstraintWidget[]) Arrays.copyOf(this.mVerticalChainsArray, this.mVerticalChainsArray.length * 2);
        }
        this.mVerticalChainsArray[this.mVerticalChainsSize] = widget;
        this.mVerticalChainsSize++;
    }

    private int countMatchConstraintsChainedWidgets(ConstraintWidget widget, int direction) {
        int i = 0;
        boolean fixedPosition;
        ConstraintWidget first;
        int count;
        if (direction == 0) {
            fixedPosition = true;
            first = widget;
            if (!(widget.mLeft.mTarget == null || widget.mLeft.mTarget.mOwner == this)) {
                fixedPosition = false;
            }
            while (widget.mRight.mTarget != null) {
                if (widget.mHorizontalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                    if (i + 1 >= this.mMatchConstraintsChainedWidgets.length) {
                        this.mMatchConstraintsChainedWidgets = (ConstraintWidget[]) Arrays.copyOf(this.mMatchConstraintsChainedWidgets, this.mMatchConstraintsChainedWidgets.length * 2);
                    }
                    count = i + 1;
                    this.mMatchConstraintsChainedWidgets[i] = widget;
                    i = count;
                }
                if (widget.mRight.mTarget.mOwner.mLeft.mTarget == null || widget.mRight.mTarget.mOwner.mLeft.mTarget.mOwner != widget) {
                    break;
                }
                widget = widget.mRight.mTarget.mOwner;
            }
            if (!(widget.mRight.mTarget == null || widget.mRight.mTarget.mOwner == this)) {
                fixedPosition = false;
            }
            first.mHorizontalChainFixedPosition = fixedPosition;
        } else {
            fixedPosition = true;
            first = widget;
            if (!(widget.mTop.mTarget == null || widget.mTop.mTarget.mOwner == this)) {
                fixedPosition = false;
            }
            while (widget.mBottom.mTarget != null) {
                if (widget.mVerticalDimensionBehaviour == DimensionBehaviour.MATCH_CONSTRAINT) {
                    if (i + 1 >= this.mMatchConstraintsChainedWidgets.length) {
                        this.mMatchConstraintsChainedWidgets = (ConstraintWidget[]) Arrays.copyOf(this.mMatchConstraintsChainedWidgets, this.mMatchConstraintsChainedWidgets.length * 2);
                    }
                    count = i + 1;
                    this.mMatchConstraintsChainedWidgets[i] = widget;
                    i = count;
                }
                if (widget.mBottom.mTarget.mOwner.mTop.mTarget == null || widget.mBottom.mTarget.mOwner.mTop.mTarget.mOwner != widget) {
                    break;
                }
                widget = widget.mBottom.mTarget.mOwner;
            }
            if (!(widget.mBottom.mTarget == null || widget.mBottom.mTarget.mOwner == this)) {
                fixedPosition = false;
            }
            first.mVerticalChainFixedPosition = fixedPosition;
        }
        return i;
    }
}
