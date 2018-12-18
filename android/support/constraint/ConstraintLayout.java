package android.support.constraint;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.support.constraint.solver.widgets.Animator;
import android.support.constraint.solver.widgets.ConstraintAnchor.Strength;
import android.support.constraint.solver.widgets.ConstraintAnchor.Type;
import android.support.constraint.solver.widgets.ConstraintWidget;
import android.support.constraint.solver.widgets.ConstraintWidget.DimensionBehaviour;
import android.support.constraint.solver.widgets.ConstraintWidgetContainer;
import android.support.constraint.solver.widgets.Guideline;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import java.util.ArrayList;

public class ConstraintLayout extends ViewGroup {
    static final boolean ALLOWS_EMBEDDED = false;
    private static final boolean SIMPLE_LAYOUT = true;
    private static final String TAG = "ConstraintLayout";
    SparseArray<View> mChildrenByIds = new SparseArray();
    private boolean mDirtyHierarchy = SIMPLE_LAYOUT;
    ConstraintWidgetContainer mLayoutWidget = new ConstraintWidgetContainer();
    private final ArrayList<ConstraintWidget> mVariableDimensionsWidgets = new ArrayList(100);
    int previousHeightMeasureSpec = -1;
    int previousPaddingLeft = -1;
    int previousPaddingTop = -1;
    int previousWidthMeasureSpec = -1;

    public static class LayoutParams extends MarginLayoutParams {
        public static final int BASELINE = 5;
        public static final int BOTTOM = 4;
        public static final int END = 7;
        public static final int HORIZONTAL = 0;
        public static final int LEFT = 1;
        public static final int MATCH_CONSTRAINT = 0;
        public static final int PARENT_ID = 0;
        public static final int RIGHT = 2;
        public static final int START = 6;
        public static final int TOP = 3;
        public static final int UNSET = -1;
        public static final int VERTICAL = 1;
        public int baselineToBaseline = -1;
        public int bottomToBottom = -1;
        public int bottomToTop = -1;
        public float dimensionRatio = 0.0f;
        public int dimensionRatioSide = 1;
        public int editorAbsoluteX = -1;
        public int editorAbsoluteY = -1;
        public int endToEnd = -1;
        public int endToStart = -1;
        public int goneBottomMargin = -1;
        public int goneEndMargin = -1;
        public int goneLeftMargin = -1;
        public int goneRightMargin = -1;
        public int goneStartMargin = -1;
        public int goneTopMargin = -1;
        public int guideBegin = -1;
        public int guideEnd = -1;
        public float guidePercent = GroundOverlayOptions.NO_DIMENSION;
        public float horizontalBias = 0.5f;
        public boolean horizontalChainPacked = false;
        boolean horizontalDimensionFixed = ConstraintLayout.SIMPLE_LAYOUT;
        public float horizontalWeight = 0.0f;
        boolean isGuideline = false;
        public int leftToLeft = -1;
        public int leftToRight = -1;
        boolean needsBaseline = false;
        public int orientation = -1;
        int resolveGoneLeftMargin = -1;
        int resolveGoneRightMargin = -1;
        float resolvedHorizontalBias = 0.5f;
        int resolvedLeftToLeft = -1;
        int resolvedLeftToRight = -1;
        int resolvedRightToLeft = -1;
        int resolvedRightToRight = -1;
        public int rightToLeft = -1;
        public int rightToRight = -1;
        public int startToEnd = -1;
        public int startToStart = -1;
        public int topToBottom = -1;
        public int topToTop = -1;
        public float verticalBias = 0.5f;
        public boolean verticalChainPacked = false;
        boolean verticalDimensionFixed = ConstraintLayout.SIMPLE_LAYOUT;
        public float verticalWeight = 0.0f;
        ConstraintWidget widget = new ConstraintWidget();

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, C0002R.styleable.ConstraintLayout_Layout);
            int N = a.getIndexCount();
            for (int i = 0; i < N; i++) {
                int attr = a.getIndex(i);
                if (attr == C0002R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toLeftOf) {
                    this.leftToLeft = a.getResourceId(attr, this.leftToLeft);
                    if (this.leftToLeft == -1) {
                        this.leftToLeft = a.getInt(attr, -1);
                    }
                } else if (attr == C0002R.styleable.ConstraintLayout_Layout_layout_constraintLeft_toRightOf) {
                    this.leftToRight = a.getResourceId(attr, this.leftToRight);
                    if (this.leftToRight == -1) {
                        this.leftToRight = a.getInt(attr, -1);
                    }
                } else if (attr == C0002R.styleable.ConstraintLayout_Layout_layout_constraintRight_toLeftOf) {
                    this.rightToLeft = a.getResourceId(attr, this.rightToLeft);
                    if (this.rightToLeft == -1) {
                        this.rightToLeft = a.getInt(attr, -1);
                    }
                } else if (attr == C0002R.styleable.ConstraintLayout_Layout_layout_constraintRight_toRightOf) {
                    this.rightToRight = a.getResourceId(attr, this.rightToRight);
                    if (this.rightToRight == -1) {
                        this.rightToRight = a.getInt(attr, -1);
                    }
                } else if (attr == C0002R.styleable.ConstraintLayout_Layout_layout_constraintTop_toTopOf) {
                    this.topToTop = a.getResourceId(attr, this.topToTop);
                    if (this.topToTop == -1) {
                        this.topToTop = a.getInt(attr, -1);
                    }
                } else if (attr == C0002R.styleable.ConstraintLayout_Layout_layout_constraintTop_toBottomOf) {
                    this.topToBottom = a.getResourceId(attr, this.topToBottom);
                    if (this.topToBottom == -1) {
                        this.topToBottom = a.getInt(attr, -1);
                    }
                } else if (attr == C0002R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toTopOf) {
                    this.bottomToTop = a.getResourceId(attr, this.bottomToTop);
                    if (this.bottomToTop == -1) {
                        this.bottomToTop = a.getInt(attr, -1);
                    }
                } else if (attr == C0002R.styleable.ConstraintLayout_Layout_layout_constraintBottom_toBottomOf) {
                    this.bottomToBottom = a.getResourceId(attr, this.bottomToBottom);
                    if (this.bottomToBottom == -1) {
                        this.bottomToBottom = a.getInt(attr, -1);
                    }
                } else if (attr == C0002R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_toBaselineOf) {
                    this.baselineToBaseline = a.getResourceId(attr, this.baselineToBaseline);
                    if (this.baselineToBaseline == -1) {
                        this.baselineToBaseline = a.getInt(attr, -1);
                    }
                } else if (attr == C0002R.styleable.ConstraintLayout_Layout_layout_editor_absoluteX) {
                    this.editorAbsoluteX = a.getDimensionPixelOffset(attr, this.editorAbsoluteX);
                } else if (attr == C0002R.styleable.ConstraintLayout_Layout_layout_editor_absoluteY) {
                    this.editorAbsoluteY = a.getDimensionPixelOffset(attr, this.editorAbsoluteY);
                } else if (attr == C0002R.styleable.ConstraintLayout_Layout_layout_constraintGuide_begin) {
                    this.guideBegin = a.getDimensionPixelOffset(attr, this.guideBegin);
                } else if (attr == C0002R.styleable.ConstraintLayout_Layout_layout_constraintGuide_end) {
                    this.guideEnd = a.getDimensionPixelOffset(attr, this.guideEnd);
                } else if (attr == C0002R.styleable.ConstraintLayout_Layout_layout_constraintGuide_percent) {
                    this.guidePercent = a.getFloat(attr, this.guidePercent);
                } else if (attr == C0002R.styleable.ConstraintLayout_Layout_android_orientation) {
                    this.orientation = a.getInt(attr, this.orientation);
                } else if (attr == C0002R.styleable.ConstraintLayout_Layout_layout_constraintStart_toEndOf) {
                    this.startToEnd = a.getResourceId(attr, this.startToEnd);
                    if (this.startToEnd == -1) {
                        this.startToEnd = a.getInt(attr, -1);
                    }
                } else if (attr == C0002R.styleable.ConstraintLayout_Layout_layout_constraintStart_toStartOf) {
                    this.startToStart = a.getResourceId(attr, this.startToStart);
                    if (this.startToStart == -1) {
                        this.startToStart = a.getInt(attr, -1);
                    }
                } else if (attr == C0002R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toStartOf) {
                    this.endToStart = a.getResourceId(attr, this.endToStart);
                    if (this.endToStart == -1) {
                        this.endToStart = a.getInt(attr, -1);
                    }
                } else if (attr == C0002R.styleable.ConstraintLayout_Layout_layout_constraintEnd_toEndOf) {
                    this.endToEnd = a.getResourceId(attr, this.endToEnd);
                    if (this.endToEnd == -1) {
                        this.endToEnd = a.getInt(attr, -1);
                    }
                } else if (attr == C0002R.styleable.ConstraintLayout_Layout_layout_goneMarginLeft) {
                    this.goneLeftMargin = a.getDimensionPixelSize(attr, this.goneLeftMargin);
                } else if (attr == C0002R.styleable.ConstraintLayout_Layout_layout_goneMarginTop) {
                    this.goneTopMargin = a.getDimensionPixelSize(attr, this.goneTopMargin);
                } else if (attr == C0002R.styleable.ConstraintLayout_Layout_layout_goneMarginRight) {
                    this.goneRightMargin = a.getDimensionPixelSize(attr, this.goneRightMargin);
                } else if (attr == C0002R.styleable.ConstraintLayout_Layout_layout_goneMarginBottom) {
                    this.goneBottomMargin = a.getDimensionPixelSize(attr, this.goneBottomMargin);
                } else if (attr == C0002R.styleable.ConstraintLayout_Layout_layout_goneMarginStart) {
                    this.goneStartMargin = a.getDimensionPixelSize(attr, this.goneStartMargin);
                } else if (attr == C0002R.styleable.ConstraintLayout_Layout_layout_goneMarginEnd) {
                    this.goneEndMargin = a.getDimensionPixelSize(attr, this.goneEndMargin);
                } else if (attr == C0002R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_bias) {
                    this.horizontalBias = a.getFloat(attr, this.horizontalBias);
                } else if (attr == C0002R.styleable.ConstraintLayout_Layout_layout_constraintVertical_bias) {
                    this.verticalBias = a.getFloat(attr, this.verticalBias);
                } else if (attr == C0002R.styleable.ConstraintLayout_Layout_layout_constraintDimensionRatio) {
                    String ratio = a.getString(attr);
                    this.dimensionRatio = 0.0f;
                    this.dimensionRatioSide = -1;
                    if (ratio != null) {
                        int len = ratio.length();
                        int commaIndex = ratio.indexOf(44);
                        if (commaIndex <= 0 || commaIndex >= len - 1) {
                            commaIndex = 0;
                        } else {
                            String dimension = ratio.substring(0, commaIndex);
                            if (dimension.equalsIgnoreCase("W")) {
                                this.dimensionRatioSide = 0;
                            } else if (dimension.equalsIgnoreCase("H")) {
                                this.dimensionRatioSide = 1;
                            }
                            commaIndex++;
                        }
                        int colonIndex = ratio.indexOf(58);
                        if (colonIndex < 0 || colonIndex >= len - 1) {
                            String r = ratio.substring(commaIndex);
                            if (r.length() > 0) {
                                try {
                                    this.dimensionRatio = Float.parseFloat(r);
                                } catch (NumberFormatException e) {
                                }
                            }
                        } else {
                            String nominator = ratio.substring(commaIndex, colonIndex);
                            String denominator = ratio.substring(colonIndex + 1);
                            if (nominator.length() > 0 && denominator.length() > 0) {
                                try {
                                    float nominatorValue = Float.parseFloat(nominator);
                                    float denominatorValue = Float.parseFloat(denominator);
                                    if (nominatorValue > 0.0f && denominatorValue > 0.0f) {
                                        if (this.dimensionRatioSide == 1) {
                                            this.dimensionRatio = Math.abs(denominatorValue / nominatorValue);
                                        } else {
                                            this.dimensionRatio = Math.abs(nominatorValue / denominatorValue);
                                        }
                                    }
                                } catch (NumberFormatException e2) {
                                }
                            }
                        }
                    }
                } else if (attr == C0002R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_weight) {
                    this.horizontalWeight = a.getFloat(attr, 0.0f);
                } else if (attr == C0002R.styleable.ConstraintLayout_Layout_layout_constraintVertical_weight) {
                    this.verticalWeight = a.getFloat(attr, 0.0f);
                } else if (attr == C0002R.styleable.ConstraintLayout_Layout_layout_constraintHorizontal_chainPacked) {
                    this.horizontalChainPacked = a.getBoolean(attr, false);
                } else if (attr == C0002R.styleable.ConstraintLayout_Layout_layout_constraintVertical_chainPacked) {
                    this.verticalChainPacked = a.getBoolean(attr, false);
                } else if (!(attr == C0002R.styleable.ConstraintLayout_Layout_layout_constraintLeft_creator || attr == C0002R.styleable.ConstraintLayout_Layout_layout_constraintTop_creator || attr == C0002R.styleable.ConstraintLayout_Layout_layout_constraintRight_creator || attr == C0002R.styleable.ConstraintLayout_Layout_layout_constraintBottom_creator || attr == C0002R.styleable.ConstraintLayout_Layout_layout_constraintBaseline_creator)) {
                    Log.w(ConstraintLayout.TAG, "Unknown attribute 0x" + Integer.toHexString(attr));
                }
            }
            validate();
        }

        public void validate() {
            this.isGuideline = false;
            this.horizontalDimensionFixed = ConstraintLayout.SIMPLE_LAYOUT;
            this.verticalDimensionFixed = ConstraintLayout.SIMPLE_LAYOUT;
            if (this.width == 0 || this.width == -1) {
                this.horizontalDimensionFixed = false;
            }
            if (this.height == 0 || this.height == -1) {
                this.verticalDimensionFixed = false;
            }
            if (this.guidePercent != GroundOverlayOptions.NO_DIMENSION || this.guideBegin != -1 || this.guideEnd != -1) {
                this.isGuideline = ConstraintLayout.SIMPLE_LAYOUT;
                this.horizontalDimensionFixed = ConstraintLayout.SIMPLE_LAYOUT;
                this.verticalDimensionFixed = ConstraintLayout.SIMPLE_LAYOUT;
                if (!(this.widget instanceof Guideline)) {
                    this.widget = new Guideline();
                }
                ((Guideline) this.widget).setOrientation(this.orientation);
            }
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
        }

        protected void setBaseAttributes(TypedArray a, int widthAttr, int heightAttr) {
            try {
                this.width = a.getLayoutDimension(widthAttr, "layout_width");
                this.height = a.getLayoutDimension(heightAttr, "layout_height");
            } catch (RuntimeException e) {
            }
        }

        @TargetApi(17)
        public void resolveLayoutDirection(int layoutDirection) {
            boolean isRtl = ConstraintLayout.SIMPLE_LAYOUT;
            super.resolveLayoutDirection(layoutDirection);
            this.resolvedRightToLeft = -1;
            this.resolvedRightToRight = -1;
            this.resolvedLeftToLeft = -1;
            this.resolvedLeftToRight = -1;
            this.resolveGoneLeftMargin = -1;
            this.resolveGoneRightMargin = -1;
            this.resolveGoneLeftMargin = this.goneLeftMargin;
            this.resolveGoneRightMargin = this.goneRightMargin;
            this.resolvedHorizontalBias = this.horizontalBias;
            if (1 != getLayoutDirection()) {
                isRtl = false;
            }
            if (isRtl) {
                if (this.startToEnd != -1) {
                    this.resolvedRightToLeft = this.startToEnd;
                } else if (this.startToStart != -1) {
                    this.resolvedRightToRight = this.startToStart;
                }
                if (this.endToStart != -1) {
                    this.resolvedLeftToRight = this.endToStart;
                }
                if (this.endToEnd != -1) {
                    this.resolvedLeftToLeft = this.endToEnd;
                }
                if (this.goneStartMargin != -1) {
                    this.resolveGoneRightMargin = this.goneStartMargin;
                }
                if (this.goneEndMargin != -1) {
                    this.resolveGoneLeftMargin = this.goneEndMargin;
                }
                this.resolvedHorizontalBias = 1.0f - this.horizontalBias;
            } else {
                if (this.startToEnd != -1) {
                    this.resolvedLeftToRight = this.startToEnd;
                }
                if (this.startToStart != -1) {
                    this.resolvedLeftToLeft = this.startToStart;
                }
                if (this.endToStart != -1) {
                    this.resolvedRightToLeft = this.endToStart;
                }
                if (this.endToEnd != -1) {
                    this.resolvedRightToRight = this.endToEnd;
                }
                if (this.goneStartMargin != -1) {
                    this.resolveGoneLeftMargin = this.goneStartMargin;
                }
                if (this.goneEndMargin != -1) {
                    this.resolveGoneRightMargin = this.goneEndMargin;
                }
            }
            if (this.endToStart == -1 && this.endToEnd == -1) {
                if (this.rightToLeft != -1) {
                    this.resolvedRightToLeft = this.rightToLeft;
                } else if (this.rightToRight != -1) {
                    this.resolvedRightToRight = this.rightToRight;
                }
            }
            if (this.startToStart != -1 || this.startToEnd != -1) {
                return;
            }
            if (this.leftToLeft != -1) {
                this.resolvedLeftToLeft = this.leftToLeft;
            } else if (this.leftToRight != -1) {
                this.resolvedLeftToRight = this.leftToRight;
            }
        }
    }

    public ConstraintLayout(Context context) {
        super(context);
        init();
    }

    public ConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.mLayoutWidget.setCompanionWidget(this);
        this.mChildrenByIds.put(getId(), this);
    }

    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        if (VERSION.SDK_INT < 14) {
            onViewAdded(child);
        }
    }

    public void removeView(View view) {
        super.removeView(view);
        if (VERSION.SDK_INT < 14) {
            onViewRemoved(view);
        }
    }

    public void onViewAdded(View view) {
        if (VERSION.SDK_INT >= 14) {
            super.onViewAdded(view);
        }
        ConstraintWidget widget = getViewWidget(view);
        if ((view instanceof Guideline) && !(widget instanceof Guideline)) {
            LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
            layoutParams.widget = new Guideline();
            layoutParams.isGuideline = SIMPLE_LAYOUT;
            ((Guideline) layoutParams.widget).setOrientation(layoutParams.orientation);
            widget = layoutParams.widget;
        }
        ConstraintWidgetContainer container = this.mLayoutWidget;
        widget.setCompanionWidget(view);
        this.mChildrenByIds.put(view.getId(), view);
        container.add(widget);
        widget.setParent(container);
        this.mDirtyHierarchy = SIMPLE_LAYOUT;
    }

    public void onViewRemoved(View view) {
        if (VERSION.SDK_INT >= 14) {
            super.onViewRemoved(view);
        }
        this.mChildrenByIds.remove(view.getId());
        this.mLayoutWidget.remove(getViewWidget(view));
        this.mDirtyHierarchy = SIMPLE_LAYOUT;
    }

    private void updateHierarchy() {
        int count = getChildCount();
        boolean recompute = false;
        for (int i = 0; i < count; i++) {
            if (getChildAt(i).isLayoutRequested()) {
                recompute = SIMPLE_LAYOUT;
                break;
            }
        }
        if (recompute) {
            this.mVariableDimensionsWidgets.clear();
            setChildrenConstraints();
        }
    }

    private void setChildrenConstraints() {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            ConstraintWidget widget = getViewWidget(child);
            if (widget != null) {
                LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
                widget.reset();
                widget.setParent(this.mLayoutWidget);
                widget.setVisibility(child.getVisibility());
                widget.setCompanionWidget(child);
                if (!(layoutParams.verticalDimensionFixed && layoutParams.horizontalDimensionFixed)) {
                    this.mVariableDimensionsWidgets.add(widget);
                }
                if (layoutParams.isGuideline) {
                    Guideline guideline = (Guideline) widget;
                    if (layoutParams.guideBegin != -1) {
                        guideline.setGuideBegin(layoutParams.guideBegin);
                    }
                    if (layoutParams.guideEnd != -1) {
                        guideline.setGuideEnd(layoutParams.guideEnd);
                    }
                    if (layoutParams.guidePercent != GroundOverlayOptions.NO_DIMENSION) {
                        guideline.setGuidePercent(layoutParams.guidePercent);
                    }
                } else if (layoutParams.resolvedLeftToLeft != -1 || layoutParams.resolvedLeftToRight != -1 || layoutParams.resolvedRightToLeft != -1 || layoutParams.resolvedRightToRight != -1 || layoutParams.topToTop != -1 || layoutParams.topToBottom != -1 || layoutParams.bottomToTop != -1 || layoutParams.bottomToBottom != -1 || layoutParams.baselineToBaseline != -1 || layoutParams.editorAbsoluteX != -1 || layoutParams.editorAbsoluteY != -1) {
                    ConstraintWidget target;
                    int resolvedLeftToLeft = layoutParams.resolvedLeftToLeft;
                    int resolvedLeftToRight = layoutParams.resolvedLeftToRight;
                    int resolvedRightToLeft = layoutParams.resolvedRightToLeft;
                    int resolvedRightToRight = layoutParams.resolvedRightToRight;
                    int resolveGoneLeftMargin = layoutParams.resolveGoneLeftMargin;
                    int resolveGoneRightMargin = layoutParams.resolveGoneRightMargin;
                    float resolvedHorizontalBias = layoutParams.resolvedHorizontalBias;
                    if (VERSION.SDK_INT < 17) {
                        resolvedLeftToLeft = layoutParams.leftToLeft;
                        resolvedLeftToRight = layoutParams.leftToRight;
                        resolvedRightToLeft = layoutParams.rightToLeft;
                        resolvedRightToRight = layoutParams.rightToRight;
                        resolveGoneLeftMargin = layoutParams.goneLeftMargin;
                        resolveGoneRightMargin = layoutParams.goneRightMargin;
                        resolvedHorizontalBias = layoutParams.horizontalBias;
                        if (resolvedLeftToLeft == -1 && resolvedLeftToRight == -1) {
                            if (layoutParams.startToStart != -1) {
                                resolvedLeftToLeft = layoutParams.startToStart;
                            } else if (layoutParams.startToEnd != -1) {
                                resolvedLeftToRight = layoutParams.startToEnd;
                            }
                        }
                        if (resolvedRightToLeft == -1 && resolvedRightToRight == -1) {
                            if (layoutParams.endToStart != -1) {
                                resolvedRightToLeft = layoutParams.endToStart;
                            } else if (layoutParams.endToEnd != -1) {
                                resolvedRightToRight = layoutParams.endToEnd;
                            }
                        }
                    }
                    if (resolvedLeftToLeft != -1) {
                        target = getTargetWidget(resolvedLeftToLeft);
                        if (target != null) {
                            widget.immediateConnect(Type.LEFT, target, Type.LEFT, layoutParams.leftMargin, resolveGoneLeftMargin);
                        }
                    } else if (resolvedLeftToRight != -1) {
                        target = getTargetWidget(resolvedLeftToRight);
                        if (target != null) {
                            widget.immediateConnect(Type.LEFT, target, Type.RIGHT, layoutParams.leftMargin, resolveGoneLeftMargin);
                        }
                    }
                    if (resolvedRightToLeft != -1) {
                        target = getTargetWidget(resolvedRightToLeft);
                        if (target != null) {
                            widget.immediateConnect(Type.RIGHT, target, Type.LEFT, layoutParams.rightMargin, resolveGoneRightMargin);
                        }
                    } else if (resolvedRightToRight != -1) {
                        target = getTargetWidget(resolvedRightToRight);
                        if (target != null) {
                            widget.immediateConnect(Type.RIGHT, target, Type.RIGHT, layoutParams.rightMargin, resolveGoneRightMargin);
                        }
                    }
                    if (layoutParams.topToTop != -1) {
                        target = getTargetWidget(layoutParams.topToTop);
                        if (target != null) {
                            widget.immediateConnect(Type.TOP, target, Type.TOP, layoutParams.topMargin, layoutParams.goneTopMargin);
                        }
                    } else if (layoutParams.topToBottom != -1) {
                        target = getTargetWidget(layoutParams.topToBottom);
                        if (target != null) {
                            widget.immediateConnect(Type.TOP, target, Type.BOTTOM, layoutParams.topMargin, layoutParams.goneTopMargin);
                        }
                    }
                    if (layoutParams.bottomToTop != -1) {
                        target = getTargetWidget(layoutParams.bottomToTop);
                        if (target != null) {
                            widget.immediateConnect(Type.BOTTOM, target, Type.TOP, layoutParams.bottomMargin, layoutParams.goneBottomMargin);
                        }
                    } else if (layoutParams.bottomToBottom != -1) {
                        target = getTargetWidget(layoutParams.bottomToBottom);
                        if (target != null) {
                            widget.immediateConnect(Type.BOTTOM, target, Type.BOTTOM, layoutParams.bottomMargin, layoutParams.goneBottomMargin);
                        }
                    }
                    if (layoutParams.baselineToBaseline != -1) {
                        View view = (View) this.mChildrenByIds.get(layoutParams.baselineToBaseline);
                        target = getTargetWidget(layoutParams.baselineToBaseline);
                        if (!(target == null || view == null || !(view.getLayoutParams() instanceof LayoutParams))) {
                            LayoutParams targetParams = (LayoutParams) view.getLayoutParams();
                            layoutParams.needsBaseline = SIMPLE_LAYOUT;
                            targetParams.needsBaseline = SIMPLE_LAYOUT;
                            widget.getAnchor(Type.BASELINE).connect(target.getAnchor(Type.BASELINE), 0, -1, Strength.STRONG, 0, SIMPLE_LAYOUT);
                            widget.getAnchor(Type.TOP).reset();
                            widget.getAnchor(Type.BOTTOM).reset();
                        }
                    }
                    if (resolvedHorizontalBias >= 0.0f && resolvedHorizontalBias != 0.5f) {
                        widget.setHorizontalBiasPercent(resolvedHorizontalBias);
                    }
                    if (layoutParams.verticalBias >= 0.0f && layoutParams.verticalBias != 0.5f) {
                        widget.setVerticalBiasPercent(layoutParams.verticalBias);
                    }
                    if (layoutParams.horizontalDimensionFixed) {
                        widget.setHorizontalDimensionBehaviour(DimensionBehaviour.FIXED);
                        widget.setWidth(layoutParams.width);
                    } else {
                        widget.setHorizontalDimensionBehaviour(DimensionBehaviour.MATCH_CONSTRAINT);
                        widget.setWidth(0);
                        if (layoutParams.width == -1) {
                            widget.setWidth(this.mLayoutWidget.getWidth());
                        }
                    }
                    if (layoutParams.verticalDimensionFixed) {
                        widget.setVerticalDimensionBehaviour(DimensionBehaviour.FIXED);
                        widget.setHeight(layoutParams.height);
                    } else {
                        widget.setVerticalDimensionBehaviour(DimensionBehaviour.MATCH_CONSTRAINT);
                        widget.setHeight(0);
                        if (layoutParams.height == -1) {
                            widget.setWidth(this.mLayoutWidget.getHeight());
                        }
                    }
                    if (isInEditMode() && !(layoutParams.editorAbsoluteX == -1 && layoutParams.editorAbsoluteY == -1)) {
                        widget.setOrigin(layoutParams.editorAbsoluteX, layoutParams.editorAbsoluteY);
                    }
                    if (layoutParams.dimensionRatio > 0.0f) {
                        widget.setDimensionRatio(layoutParams.dimensionRatio, layoutParams.dimensionRatioSide);
                    }
                    widget.setHorizontalWeight(layoutParams.horizontalWeight);
                    widget.setVerticalWeight(layoutParams.verticalWeight);
                    widget.setHorizontalChainPacked(layoutParams.horizontalChainPacked);
                    widget.setVerticalChainPacked(layoutParams.verticalChainPacked);
                }
            }
        }
    }

    private final ConstraintWidget getTargetWidget(int id) {
        if (id == 0) {
            return this.mLayoutWidget;
        }
        View view = (View) this.mChildrenByIds.get(id);
        if (view == this) {
            return this.mLayoutWidget;
        }
        return view == null ? null : ((LayoutParams) view.getLayoutParams()).widget;
    }

    private final ConstraintWidget getViewWidget(View view) {
        if (view == this) {
            return this.mLayoutWidget;
        }
        return view == null ? null : ((LayoutParams) view.getLayoutParams()).widget;
    }

    private void internalMeasureChildren(int parentWidthSpec, int parentHeightSpec) {
        int heightPadding = getPaddingTop() + getPaddingBottom();
        int widthPadding = getPaddingLeft() + getPaddingRight();
        int widgetsCount = getChildCount();
        for (int i = 0; i < widgetsCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                LayoutParams params = (LayoutParams) child.getLayoutParams();
                ConstraintWidget widget = params.widget;
                if (!params.isGuideline) {
                    int width = params.width;
                    int height = params.height;
                    if (params.horizontalDimensionFixed || params.verticalDimensionFixed) {
                        int childWidthMeasureSpec;
                        int childHeightMeasureSpec;
                        if (width == 0) {
                            childWidthMeasureSpec = getChildMeasureSpec(parentWidthSpec, widthPadding, -2);
                        } else {
                            childWidthMeasureSpec = getChildMeasureSpec(parentWidthSpec, widthPadding, width);
                        }
                        if (height == 0) {
                            childHeightMeasureSpec = getChildMeasureSpec(parentHeightSpec, heightPadding, -2);
                        } else {
                            childHeightMeasureSpec = getChildMeasureSpec(parentHeightSpec, heightPadding, height);
                        }
                        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
                        width = child.getMeasuredWidth();
                        height = child.getMeasuredHeight();
                    }
                    widget.setWidth(width);
                    widget.setHeight(height);
                    if (params.needsBaseline) {
                        int baseline = child.getBaseline();
                        if (baseline != -1) {
                            widget.setBaselineDistance(baseline);
                        }
                    }
                }
            }
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mDirtyHierarchy) {
            this.mDirtyHierarchy = false;
            updateHierarchy();
        }
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        if (this.previousPaddingLeft == -1 || this.previousPaddingTop == -1 || this.previousHeightMeasureSpec == -1 || this.previousWidthMeasureSpec == -1 || this.previousPaddingLeft != paddingLeft || this.previousPaddingTop != paddingTop || this.previousWidthMeasureSpec != widthMeasureSpec || this.previousHeightMeasureSpec != heightMeasureSpec) {
            this.mLayoutWidget.setX(paddingLeft);
            this.mLayoutWidget.setY(paddingTop);
            setSelfDimensionBehaviour(widthMeasureSpec, heightMeasureSpec);
        }
        this.previousPaddingLeft = paddingLeft;
        this.previousPaddingTop = paddingTop;
        this.previousWidthMeasureSpec = widthMeasureSpec;
        this.previousHeightMeasureSpec = heightMeasureSpec;
        internalMeasureChildren(widthMeasureSpec, heightMeasureSpec);
        solveLinearSystem();
        int childState = 0;
        int sizeDependentWidgetsCount = this.mVariableDimensionsWidgets.size();
        int heightPadding = paddingTop + getPaddingBottom();
        int widthPadding = paddingLeft + getPaddingRight();
        if (sizeDependentWidgetsCount > 0) {
            boolean needSolverPass = false;
            for (int i = 0; i < sizeDependentWidgetsCount; i++) {
                ConstraintWidget widget = (ConstraintWidget) this.mVariableDimensionsWidgets.get(i);
                if (!(widget instanceof Guideline)) {
                    View child = (View) widget.getCompanionWidget();
                    if (!(child == null || child.getVisibility() == 8)) {
                        int widthSpec = MeasureSpec.makeMeasureSpec(widget.getWidth(), 1073741824);
                        int heightSpec = MeasureSpec.makeMeasureSpec(widget.getHeight(), 1073741824);
                        LayoutParams params = (LayoutParams) child.getLayoutParams();
                        if (params.width == -2) {
                            widthSpec = MeasureSpec.makeMeasureSpec(widget.getWidth(), 0);
                        } else if (params.height == -2) {
                            heightSpec = MeasureSpec.makeMeasureSpec(widget.getHeight(), 0);
                        }
                        child.measure(widthSpec, heightSpec);
                        int measuredWidth = child.getMeasuredWidth();
                        int measureHeight = child.getMeasuredHeight();
                        if (measuredWidth != widget.getWidth()) {
                            widget.setWidth(measuredWidth);
                            needSolverPass = SIMPLE_LAYOUT;
                        } else if (measureHeight != widget.getHeight()) {
                            widget.setHeight(measureHeight);
                            needSolverPass = SIMPLE_LAYOUT;
                        }
                        if (VERSION.SDK_INT >= 11) {
                            childState = combineMeasuredStates(childState, child.getMeasuredState());
                        }
                    }
                }
            }
            if (needSolverPass) {
                solveLinearSystem();
            }
        }
        int androidLayoutWidth = this.mLayoutWidget.getWidth() + widthPadding;
        int androidLayoutHeight = this.mLayoutWidget.getHeight() + heightPadding;
        if (VERSION.SDK_INT >= 11) {
            setMeasuredDimension(ViewCompat.MEASURED_SIZE_MASK & resolveSizeAndState(androidLayoutWidth, widthMeasureSpec, childState), ViewCompat.MEASURED_SIZE_MASK & resolveSizeAndState(androidLayoutHeight, heightMeasureSpec, childState << 16));
            return;
        }
        setMeasuredDimension(androidLayoutWidth, androidLayoutHeight);
    }

    private void setSelfDimensionBehaviour(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightPadding = getPaddingTop() + getPaddingBottom();
        int widthPadding = getPaddingLeft() + getPaddingRight();
        DimensionBehaviour widthBehaviour = DimensionBehaviour.FIXED;
        DimensionBehaviour heightBehaviour = DimensionBehaviour.FIXED;
        int desiredWidth = 0;
        int desiredHeight = 0;
        android.view.ViewGroup.LayoutParams params = getLayoutParams();
        switch (widthMode) {
            case Integer.MIN_VALUE:
                widthBehaviour = DimensionBehaviour.WRAP_CONTENT;
                break;
            case 0:
                if (params.width <= 0) {
                    widthBehaviour = DimensionBehaviour.WRAP_CONTENT;
                    break;
                } else {
                    desiredWidth = params.width;
                    break;
                }
            case 1073741824:
                desiredWidth = widthSize - widthPadding;
                break;
        }
        switch (heightMode) {
            case Integer.MIN_VALUE:
                heightBehaviour = DimensionBehaviour.WRAP_CONTENT;
                break;
            case 0:
                if (params.height <= 0) {
                    heightBehaviour = DimensionBehaviour.WRAP_CONTENT;
                    break;
                } else {
                    desiredHeight = params.height;
                    break;
                }
            case 1073741824:
                desiredHeight = heightSize - heightPadding;
                break;
        }
        this.mLayoutWidget.setHorizontalDimensionBehaviour(widthBehaviour);
        this.mLayoutWidget.setWidth(desiredWidth);
        this.mLayoutWidget.setVerticalDimensionBehaviour(heightBehaviour);
        this.mLayoutWidget.setHeight(desiredHeight);
    }

    private void solveLinearSystem() {
        Animator.setAnimationEnabled(false);
        this.mLayoutWidget.layout();
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int widgetsCount = getChildCount();
        for (int i = 0; i < widgetsCount; i++) {
            View child = getChildAt(i);
            LayoutParams params = (LayoutParams) child.getLayoutParams();
            if (child.getVisibility() != 8 || params.isGuideline) {
                ConstraintWidget widget = params.widget;
                int l = widget.getDrawX();
                int t = widget.getDrawY();
                child.layout(l, t, l + widget.getWidth(), t + widget.getHeight());
            }
        }
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    public void requestLayout() {
        super.requestLayout();
        this.mDirtyHierarchy = SIMPLE_LAYOUT;
    }
}
