package android.support.constraint;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build.VERSION;
import android.support.constraint.ConstraintLayout.LayoutParams;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class ConstraintSet {
    public static final int BASELINE = 5;
    private static final int BASELINE_TO_BASELINE = 1;
    public static final int BOTTOM = 4;
    private static final int BOTTOM_MARGIN = 2;
    private static final int BOTTOM_TO_BOTTOM = 3;
    private static final int BOTTOM_TO_TOP = 4;
    private static final boolean DEBUG = false;
    private static final int DIMENSION_RATIO = 5;
    private static final int EDITOR_ABSOLUTE_X = 6;
    private static final int EDITOR_ABSOLUTE_Y = 7;
    public static final int END = 7;
    private static final int END_MARGIN = 8;
    private static final int END_TO_END = 9;
    private static final int END_TO_START = 10;
    public static final int GONE = 8;
    private static final int GONE_BOTTOM_MARGIN = 11;
    private static final int GONE_END_MARGIN = 12;
    private static final int GONE_LEFT_MARGIN = 13;
    private static final int GONE_RIGHT_MARGIN = 14;
    private static final int GONE_START_MARGIN = 15;
    private static final int GONE_TOP_MARGIN = 16;
    private static final int GUIDE_BEGIN = 17;
    private static final int GUIDE_END = 18;
    private static final int GUIDE_PERCENT = 19;
    public static final int HORIZONTAL = 0;
    private static final int HORIZONTAL_BIAS = 20;
    public static final int HORIZONTAL_GUIDELINE = 0;
    private static final int HORIZONTAL_PACK = 41;
    private static final int HORIZONTAL_WEIGHT = 39;
    public static final int INVISIBLE = 4;
    private static final int LAYOUT_HEIGHT = 21;
    private static final int LAYOUT_VISIBILITY = 22;
    private static final int LAYOUT_WIDTH = 23;
    public static final int LEFT = 1;
    private static final int LEFT_MARGIN = 24;
    private static final int LEFT_TO_LEFT = 25;
    private static final int LEFT_TO_RIGHT = 26;
    public static final int MATCH_CONSTRAINT = 0;
    private static final int ORIENTATION = 27;
    public static final int PARENT_ID = 0;
    public static final int RIGHT = 2;
    private static final int RIGHT_MARGIN = 28;
    private static final int RIGHT_TO_LEFT = 29;
    private static final int RIGHT_TO_RIGHT = 30;
    public static final int START = 6;
    private static final int START_MARGIN = 31;
    private static final int START_TO_END = 32;
    private static final int START_TO_START = 33;
    private static final String TAG = "ConstraintSet";
    public static final int TOP = 3;
    private static final int TOP_MARGIN = 34;
    private static final int TOP_TO_BOTTOM = 35;
    private static final int TOP_TO_TOP = 36;
    private static final int UNUSED = 43;
    public static final int VERTICAL = 1;
    private static final int VERTICAL_BIAS = 37;
    public static final int VERTICAL_GUIDELINE = 1;
    private static final int VERTICAL_PACK = 42;
    private static final int VERTICAL_WEIGHT = 40;
    private static final int VIEW_ID = 38;
    private static final int[] VISIBILITY_FLAGS = new int[]{0, 4, 8};
    public static final int VISIBLE = 0;
    public static final int WRAP_CONTENT = -2;
    private static SparseIntArray mapToConstant = new SparseIntArray();
    private HashMap<Integer, Constraint> mConstraints = new HashMap();

    private static class Constraint {
        static final int UNSET = -1;
        public int baselineToBaseline;
        public int bottomMargin;
        public int bottomToBottom;
        public int bottomToTop;
        public float dimensionRatio;
        public int dimensionRatioSide;
        public int editorAbsoluteX;
        public int editorAbsoluteY;
        public int endMargin;
        public int endToEnd;
        public int endToStart;
        public int goneBottomMargin;
        public int goneEndMargin;
        public int goneLeftMargin;
        public int goneRightMargin;
        public int goneStartMargin;
        public int goneTopMargin;
        public int guideBegin;
        public int guideEnd;
        public float guidePercent;
        public float horizontalBias;
        public boolean horizontalChainPacked;
        public float horizontalWeight;
        public int leftMargin;
        public int leftToLeft;
        public int leftToRight;
        public int mHeight;
        boolean mIsGuideline;
        int mViewId;
        public int mWidth;
        public int orientation;
        public int rightMargin;
        public int rightToLeft;
        public int rightToRight;
        public int startMargin;
        public int startToEnd;
        public int startToStart;
        public int topMargin;
        public int topToBottom;
        public int topToTop;
        public float verticalBias;
        public boolean verticalChainPacked;
        public float verticalWeight;
        public int visibility;

        private Constraint() {
            this.mIsGuideline = false;
            this.guideBegin = -1;
            this.guideEnd = -1;
            this.guidePercent = GroundOverlayOptions.NO_DIMENSION;
            this.leftToLeft = -1;
            this.leftToRight = -1;
            this.rightToLeft = -1;
            this.rightToRight = -1;
            this.topToTop = -1;
            this.topToBottom = -1;
            this.bottomToTop = -1;
            this.bottomToBottom = -1;
            this.baselineToBaseline = -1;
            this.startToEnd = -1;
            this.startToStart = -1;
            this.endToStart = -1;
            this.endToEnd = -1;
            this.horizontalBias = 0.5f;
            this.verticalBias = 0.5f;
            this.dimensionRatio = 0.0f;
            this.editorAbsoluteX = -1;
            this.editorAbsoluteY = -1;
            this.orientation = -1;
        }

        private void fillFrom(int viewId, LayoutParams param) {
            this.mViewId = viewId;
            this.leftToLeft = param.leftToLeft;
            this.leftToRight = param.leftToRight;
            this.rightToLeft = param.rightToLeft;
            this.rightToRight = param.rightToRight;
            this.topToTop = param.topToTop;
            this.topToBottom = param.topToBottom;
            this.bottomToTop = param.bottomToTop;
            this.bottomToBottom = param.bottomToBottom;
            this.baselineToBaseline = param.baselineToBaseline;
            this.startToEnd = param.startToEnd;
            this.startToStart = param.startToStart;
            this.endToStart = param.endToStart;
            this.endToEnd = param.endToEnd;
            this.horizontalBias = param.horizontalBias;
            this.verticalBias = param.verticalBias;
            this.dimensionRatio = param.dimensionRatio;
            this.dimensionRatioSide = param.dimensionRatioSide;
            this.editorAbsoluteX = param.editorAbsoluteX;
            this.editorAbsoluteY = param.editorAbsoluteY;
            this.orientation = param.orientation;
            this.guidePercent = param.guidePercent;
            this.guideBegin = param.guideBegin;
            this.guideEnd = param.guideEnd;
            this.mWidth = param.width;
            this.mHeight = param.height;
            this.leftMargin = param.leftMargin;
            this.rightMargin = param.rightMargin;
            this.topMargin = param.topMargin;
            this.bottomMargin = param.bottomMargin;
            this.verticalWeight = param.verticalWeight;
            this.horizontalWeight = param.horizontalWeight;
            this.verticalChainPacked = param.verticalChainPacked;
            this.horizontalChainPacked = param.horizontalChainPacked;
            this.endMargin = param.getMarginEnd();
            this.startMargin = param.getMarginStart();
        }

        public void applyTo(LayoutParams param) {
            param.leftToLeft = this.leftToLeft;
            param.leftToRight = this.leftToRight;
            param.rightToLeft = this.rightToLeft;
            param.rightToRight = this.rightToRight;
            param.topToTop = this.topToTop;
            param.topToBottom = this.topToBottom;
            param.bottomToTop = this.bottomToTop;
            param.bottomToBottom = this.bottomToBottom;
            param.baselineToBaseline = this.baselineToBaseline;
            param.startToEnd = this.startToEnd;
            param.startToStart = this.startToStart;
            param.endToStart = this.endToStart;
            param.endToEnd = this.endToEnd;
            param.leftMargin = this.leftMargin;
            param.rightMargin = this.rightMargin;
            param.topMargin = this.topMargin;
            param.bottomMargin = this.bottomMargin;
            param.horizontalBias = this.horizontalBias;
            param.verticalBias = this.verticalBias;
            param.dimensionRatio = this.dimensionRatio;
            param.dimensionRatioSide = this.dimensionRatioSide;
            param.editorAbsoluteX = this.editorAbsoluteX;
            param.editorAbsoluteY = this.editorAbsoluteY;
            param.verticalWeight = this.verticalWeight;
            param.horizontalWeight = this.horizontalWeight;
            param.verticalChainPacked = this.verticalChainPacked;
            param.horizontalChainPacked = this.horizontalChainPacked;
            param.orientation = this.orientation;
            param.guidePercent = this.guidePercent;
            param.guideBegin = this.guideBegin;
            param.guideEnd = this.guideEnd;
            param.width = this.mWidth;
            param.height = this.mHeight;
            if (VERSION.SDK_INT >= 17) {
                param.setMarginStart(this.startMargin);
                param.setMarginEnd(this.endMargin);
            }
            param.validate();
        }
    }

    static {
        mapToConstant.append(C0002R.styleable.ConstraintSet_layout_constraintLeft_toLeftOf, 25);
        mapToConstant.append(C0002R.styleable.ConstraintSet_layout_constraintLeft_toRightOf, 26);
        mapToConstant.append(C0002R.styleable.ConstraintSet_layout_constraintRight_toLeftOf, 29);
        mapToConstant.append(C0002R.styleable.ConstraintSet_layout_constraintRight_toRightOf, 30);
        mapToConstant.append(C0002R.styleable.ConstraintSet_layout_constraintTop_toTopOf, 36);
        mapToConstant.append(C0002R.styleable.ConstraintSet_layout_constraintTop_toBottomOf, 35);
        mapToConstant.append(C0002R.styleable.ConstraintSet_layout_constraintBottom_toTopOf, 4);
        mapToConstant.append(C0002R.styleable.ConstraintSet_layout_constraintBottom_toBottomOf, 3);
        mapToConstant.append(C0002R.styleable.ConstraintSet_layout_constraintBaseline_toBaselineOf, 1);
        mapToConstant.append(C0002R.styleable.ConstraintSet_layout_editor_absoluteX, 6);
        mapToConstant.append(C0002R.styleable.ConstraintSet_layout_editor_absoluteY, 7);
        mapToConstant.append(C0002R.styleable.ConstraintSet_layout_constraintGuide_begin, 17);
        mapToConstant.append(C0002R.styleable.ConstraintSet_layout_constraintGuide_end, 18);
        mapToConstant.append(C0002R.styleable.ConstraintSet_layout_constraintGuide_percent, 19);
        mapToConstant.append(C0002R.styleable.ConstraintSet_android_orientation, 27);
        mapToConstant.append(C0002R.styleable.ConstraintSet_layout_constraintStart_toEndOf, 32);
        mapToConstant.append(C0002R.styleable.ConstraintSet_layout_constraintStart_toStartOf, 33);
        mapToConstant.append(C0002R.styleable.ConstraintSet_layout_constraintEnd_toStartOf, 10);
        mapToConstant.append(C0002R.styleable.ConstraintSet_layout_constraintEnd_toEndOf, 9);
        mapToConstant.append(C0002R.styleable.ConstraintSet_layout_goneMarginLeft, 13);
        mapToConstant.append(C0002R.styleable.ConstraintSet_layout_goneMarginTop, 16);
        mapToConstant.append(C0002R.styleable.ConstraintSet_layout_goneMarginRight, 14);
        mapToConstant.append(C0002R.styleable.ConstraintSet_layout_goneMarginBottom, 11);
        mapToConstant.append(C0002R.styleable.ConstraintSet_layout_goneMarginStart, 15);
        mapToConstant.append(C0002R.styleable.ConstraintSet_layout_goneMarginEnd, 12);
        mapToConstant.append(C0002R.styleable.ConstraintSet_layout_constraintVertical_weight, 40);
        mapToConstant.append(C0002R.styleable.ConstraintSet_layout_constraintHorizontal_weight, 39);
        mapToConstant.append(C0002R.styleable.ConstraintSet_layout_constraintHorizontal_chainPacked, 41);
        mapToConstant.append(C0002R.styleable.ConstraintSet_layout_constraintVertical_chainPacked, 42);
        mapToConstant.append(C0002R.styleable.ConstraintSet_layout_constraintHorizontal_bias, 20);
        mapToConstant.append(C0002R.styleable.ConstraintSet_layout_constraintVertical_bias, 37);
        mapToConstant.append(C0002R.styleable.ConstraintSet_layout_constraintDimensionRatio, 5);
        mapToConstant.append(C0002R.styleable.ConstraintSet_layout_constraintLeft_creator, 43);
        mapToConstant.append(C0002R.styleable.ConstraintSet_layout_constraintTop_creator, 43);
        mapToConstant.append(C0002R.styleable.ConstraintSet_layout_constraintRight_creator, 43);
        mapToConstant.append(C0002R.styleable.ConstraintSet_layout_constraintBottom_creator, 43);
        mapToConstant.append(C0002R.styleable.ConstraintSet_layout_constraintBaseline_creator, 43);
        mapToConstant.append(C0002R.styleable.ConstraintSet_android_layout_marginLeft, 24);
        mapToConstant.append(C0002R.styleable.ConstraintSet_android_layout_marginRight, 28);
        mapToConstant.append(C0002R.styleable.ConstraintSet_android_layout_marginStart, 31);
        mapToConstant.append(C0002R.styleable.ConstraintSet_android_layout_marginEnd, 8);
        mapToConstant.append(C0002R.styleable.ConstraintSet_android_layout_marginTop, 34);
        mapToConstant.append(C0002R.styleable.ConstraintSet_android_layout_marginBottom, 2);
        mapToConstant.append(C0002R.styleable.ConstraintSet_android_layout_width, 23);
        mapToConstant.append(C0002R.styleable.ConstraintSet_android_layout_height, 21);
        mapToConstant.append(C0002R.styleable.ConstraintSet_android_visibility, 22);
        mapToConstant.append(C0002R.styleable.ConstraintSet_android_id, 38);
    }

    public void clone(Context context, int constraintLayoutId) {
        clone((ConstraintLayout) LayoutInflater.from(context).inflate(constraintLayoutId, null));
    }

    public void clone(ConstraintLayout constraintLayout) {
        int count = constraintLayout.getChildCount();
        this.mConstraints.clear();
        for (int i = 0; i < count; i++) {
            View view = constraintLayout.getChildAt(i);
            LayoutParams param = (LayoutParams) view.getLayoutParams();
            int id = view.getId();
            if (!this.mConstraints.containsKey(Integer.valueOf(id))) {
                this.mConstraints.put(Integer.valueOf(id), new Constraint());
            }
            Constraint constraint = (Constraint) this.mConstraints.get(Integer.valueOf(id));
            constraint.fillFrom(id, param);
            constraint.visibility = view.getVisibility();
        }
    }

    public void applyTo(ConstraintLayout constraintLayout) {
        int count = constraintLayout.getChildCount();
        HashSet<Integer> used = new HashSet(this.mConstraints.keySet());
        for (int i = 0; i < count; i++) {
            View view = constraintLayout.getChildAt(i);
            int id = view.getId();
            if (this.mConstraints.containsKey(Integer.valueOf(id))) {
                used.remove(Integer.valueOf(id));
                Constraint constraint = (Constraint) this.mConstraints.get(Integer.valueOf(id));
                LayoutParams param = (LayoutParams) view.getLayoutParams();
                constraint.applyTo(param);
                view.setLayoutParams(param);
                view.setVisibility(constraint.visibility);
            }
        }
        Iterator it = used.iterator();
        while (it.hasNext()) {
            Integer id2 = (Integer) it.next();
            constraint = (Constraint) this.mConstraints.get(id2);
            if (constraint.mIsGuideline) {
                Guideline g = new Guideline(constraintLayout.getContext());
                g.setId(id2.intValue());
                param = constraintLayout.generateDefaultLayoutParams();
                constraint.applyTo(param);
                constraintLayout.addView(g, param);
            }
        }
    }

    public void center(int centerID, int firstID, int firstSide, int firstMargin, int secondId, int secondSide, int secondMargin, float bias) {
        if (firstMargin < 0) {
            throw new IllegalArgumentException("margin must be > 0");
        } else if (secondMargin < 0) {
            throw new IllegalArgumentException("margin must be > 0");
        } else if (bias <= 0.0f || bias > 1.0f) {
            throw new IllegalArgumentException("bias must be between 0 and 1 inclusive");
        } else if (firstSide == 1 || firstSide == 2) {
            connect(centerID, 1, firstID, firstSide, firstMargin);
            connect(centerID, 2, secondId, secondSide, secondMargin);
            ((Constraint) this.mConstraints.get(Integer.valueOf(centerID))).horizontalBias = bias;
        } else {
            connect(centerID, 3, firstID, firstSide, firstMargin);
            connect(centerID, 4, secondId, secondSide, secondMargin);
            ((Constraint) this.mConstraints.get(Integer.valueOf(centerID))).verticalBias = bias;
        }
    }

    private void centerHorizontally(int centerID, int leftId, int leftSide, int leftMargin, int rightId, int rightSide, int rightMargin, float bias) {
        connect(centerID, 1, leftId, leftSide, leftMargin);
        connect(centerID, 2, rightId, rightSide, rightMargin);
        ((Constraint) this.mConstraints.get(Integer.valueOf(centerID))).horizontalBias = bias;
    }

    private void centerVertically(int centerID, int topId, int topSide, int topMargin, int bottomId, int bottomSide, int bottomMargin, float bias) {
        connect(centerID, 3, topId, topSide, topMargin);
        connect(centerID, 4, bottomId, bottomSide, bottomMargin);
        ((Constraint) this.mConstraints.get(Integer.valueOf(centerID))).verticalBias = bias;
    }

    public void createVerticalChain(int topId, int bottomId, int[] chainIds, float[] weights, boolean packed) {
        if (chainIds.length < 2) {
            throw new IllegalArgumentException("must have 2 or more widgets in a chain");
        } else if (weights == null || weights.length == chainIds.length) {
            if (weights != null) {
                get(chainIds[0]).verticalWeight = weights[0];
            }
            get(chainIds[0]).verticalChainPacked = packed;
            connect(chainIds[0], 3, topId, 3, 0);
            for (int i = 1; i < chainIds.length - 1; i++) {
                int chainId = chainIds[i];
                connect(chainIds[i], 3, chainIds[i - 1], 4, 0);
                connect(chainIds[i - 1], 4, chainIds[i], 3, 0);
                if (weights != null) {
                    get(chainIds[i]).verticalWeight = weights[i];
                }
            }
            connect(chainIds[chainIds.length - 1], 4, bottomId, 3, 0);
            if (weights != null) {
                get(chainIds[chainIds.length - 1]).verticalWeight = weights[chainIds.length - 1];
            }
        } else {
            throw new IllegalArgumentException("must have 2 or more widgets in a chain");
        }
    }

    public void createHorizontalChain(int leftId, int rightId, int[] chainIds, float[] weights, boolean packed) {
        if (chainIds.length < 2) {
            throw new IllegalArgumentException("must have 2 or more widgets in a chain");
        } else if (weights == null || weights.length == chainIds.length) {
            if (weights != null) {
                get(chainIds[0]).verticalWeight = weights[0];
            }
            get(chainIds[0]).horizontalChainPacked = packed;
            connect(chainIds[0], 3, leftId, 3, 0);
            for (int i = 1; i < chainIds.length - 1; i++) {
                int chainId = chainIds[i];
                connect(chainIds[i], 3, chainIds[i - 1], 4, 0);
                connect(chainIds[i - 1], 4, chainIds[i], 3, 0);
                if (weights != null) {
                    get(chainIds[i]).verticalWeight = weights[i];
                }
            }
            connect(chainIds[chainIds.length - 1], 4, rightId, 3, 0);
            if (weights != null) {
                get(chainIds[chainIds.length - 1]).verticalWeight = weights[chainIds.length - 1];
            }
        } else {
            throw new IllegalArgumentException("must have 2 or more widgets in a chain");
        }
    }

    public void connect(int startID, int startSide, int endID, int endSide, int margin) {
        if (!this.mConstraints.containsKey(Integer.valueOf(startID))) {
            this.mConstraints.put(Integer.valueOf(startID), new Constraint());
        }
        Constraint constraint = (Constraint) this.mConstraints.get(Integer.valueOf(startID));
        switch (startSide) {
            case 1:
                if (endSide == 1) {
                    constraint.leftToLeft = endID;
                    constraint.leftToRight = -1;
                } else if (endSide == 2) {
                    constraint.leftToRight = endID;
                    constraint.leftToLeft = -1;
                } else {
                    throw new IllegalArgumentException("Left to " + sideToString(endSide) + " undefined");
                }
                constraint.leftMargin = margin;
                return;
            case 2:
                if (endSide == 1) {
                    constraint.rightToLeft = endID;
                    constraint.rightToRight = -1;
                } else if (endSide == 2) {
                    constraint.rightToRight = endID;
                    constraint.rightToLeft = -1;
                } else {
                    throw new IllegalArgumentException("right to " + sideToString(endSide) + " undefined");
                }
                constraint.rightMargin = margin;
                return;
            case 3:
                if (endSide == 3) {
                    constraint.topToTop = endID;
                    constraint.topToBottom = -1;
                    constraint.baselineToBaseline = -1;
                } else if (endSide == 4) {
                    constraint.topToBottom = endID;
                    constraint.topToTop = -1;
                    constraint.baselineToBaseline = -1;
                } else {
                    throw new IllegalArgumentException("right to " + sideToString(endSide) + " undefined");
                }
                constraint.topMargin = margin;
                return;
            case 4:
                if (endSide == 4) {
                    constraint.bottomToBottom = endID;
                    constraint.bottomToTop = -1;
                    constraint.baselineToBaseline = -1;
                } else if (endSide == 3) {
                    constraint.bottomToTop = endID;
                    constraint.bottomToBottom = -1;
                    constraint.baselineToBaseline = -1;
                } else {
                    throw new IllegalArgumentException("right to " + sideToString(endSide) + " undefined");
                }
                constraint.bottomMargin = margin;
                return;
            case 5:
                if (endSide == 5) {
                    constraint.baselineToBaseline = endID;
                    constraint.bottomToBottom = -1;
                    constraint.bottomToTop = -1;
                    constraint.topToTop = -1;
                    constraint.topToBottom = -1;
                    return;
                }
                throw new IllegalArgumentException("right to " + sideToString(endSide) + " undefined");
            case 6:
                if (endSide == 6) {
                    constraint.startToStart = endID;
                    constraint.startToEnd = -1;
                } else if (endSide == 7) {
                    constraint.startToEnd = endID;
                    constraint.startToStart = -1;
                } else {
                    throw new IllegalArgumentException("right to " + sideToString(endSide) + " undefined");
                }
                constraint.startMargin = margin;
                return;
            case 7:
                if (endSide == 7) {
                    constraint.endToEnd = endID;
                    constraint.endToStart = -1;
                } else if (endSide == 6) {
                    constraint.endToStart = endID;
                    constraint.endToEnd = -1;
                } else {
                    throw new IllegalArgumentException("right to " + sideToString(endSide) + " undefined");
                }
                constraint.endMargin = margin;
                return;
            default:
                throw new IllegalArgumentException(sideToString(startSide) + " to " + sideToString(endSide) + " unknown");
        }
    }

    public void centerHorizontally(int viewId, int toView) {
        center(viewId, toView, 1, 0, toView, 2, 0, 0.5f);
    }

    public void centerVertically(int viewId, int toView) {
        center(viewId, toView, 3, 0, toView, 4, 0, 0.5f);
    }

    public void clear(int viewId) {
        this.mConstraints.remove(Integer.valueOf(viewId));
    }

    public void clear(int viewId, int anchor) {
        if (this.mConstraints.containsKey(Integer.valueOf(viewId))) {
            Constraint constraint = (Constraint) this.mConstraints.get(Integer.valueOf(viewId));
            switch (anchor) {
                case 1:
                    constraint.leftToRight = -1;
                    constraint.leftToLeft = -1;
                    constraint.leftMargin = 0;
                    return;
                case 2:
                    constraint.leftToRight = -1;
                    constraint.leftToLeft = -1;
                    constraint.rightMargin = 0;
                    return;
                case 3:
                    constraint.topToBottom = -1;
                    constraint.topToTop = -1;
                    constraint.topMargin = 0;
                    return;
                case 4:
                    constraint.bottomToTop = -1;
                    constraint.bottomToBottom = -1;
                    constraint.bottomMargin = 0;
                    return;
                case 5:
                    constraint.baselineToBaseline = -1;
                    return;
                case 6:
                    constraint.startToEnd = -1;
                    constraint.startToStart = -1;
                    constraint.startMargin = 0;
                    return;
                case 7:
                    constraint.endToStart = -1;
                    constraint.endToEnd = -1;
                    constraint.endMargin = 0;
                    return;
                default:
                    throw new IllegalArgumentException("unknown constraint");
            }
        }
    }

    public void setMargin(int viewId, int anchor, int value) {
        Constraint constraint = get(viewId);
        switch (anchor) {
            case 1:
                constraint.leftMargin = value;
                return;
            case 2:
                constraint.rightMargin = value;
                return;
            case 3:
                constraint.topMargin = value;
                return;
            case 4:
                constraint.bottomMargin = value;
                return;
            case 5:
                throw new IllegalArgumentException("baseline does not support margins");
            case 6:
                constraint.startMargin = value;
                return;
            case 7:
                constraint.endMargin = value;
                return;
            default:
                throw new IllegalArgumentException("unknown constraint");
        }
    }

    public void setHorizontalBias(int viewId, float bias) {
        get(viewId).horizontalBias = bias;
    }

    public void setVerticalBias(int viewId, float bias) {
        get(viewId).verticalBias = bias;
    }

    public void setDimensionRatio(int viewId, int side, float ratio) {
        get(viewId).dimensionRatio = ratio;
        get(viewId).dimensionRatioSide = side;
    }

    public void setVisibility(int viewId, int visibility) {
        get(viewId).visibility = visibility;
    }

    public void constrainHeight(int viewId, int height) {
        get(viewId).mHeight = height;
    }

    public void constrainWidth(int viewId, int width) {
        get(viewId).mWidth = width;
    }

    public void create(int guidelineID, int orientation) {
        Constraint constraint = get(guidelineID);
        constraint.mIsGuideline = true;
        constraint.orientation = orientation;
    }

    public void setGuidelineBegin(int guidelineID, int margin) {
        get(guidelineID).guideBegin = margin;
        get(guidelineID).guideEnd = -1;
        get(guidelineID).guidePercent = 0.5f;
    }

    public void setGuidelineEnd(int guidelineID, int margin) {
        get(guidelineID).guideEnd = margin;
        get(guidelineID).guideBegin = -1;
        get(guidelineID).guidePercent = 0.5f;
    }

    public void setGuidelinePercent(int guidelineID, float ratio) {
        get(guidelineID).guidePercent = ratio;
        get(guidelineID).guideEnd = -1;
        get(guidelineID).guideBegin = -1;
    }

    private Constraint get(int id) {
        if (!this.mConstraints.containsKey(Integer.valueOf(id))) {
            this.mConstraints.put(Integer.valueOf(id), new Constraint());
        }
        return (Constraint) this.mConstraints.get(Integer.valueOf(id));
    }

    private String sideToString(int side) {
        switch (side) {
            case 1:
                return "left";
            case 2:
                return "right";
            case 3:
                return "top";
            case 4:
                return "bottom";
            case 5:
                return "baseline";
            case 6:
                return "start";
            case 7:
                return "end";
            default:
                return "undefined";
        }
    }

    public void load(Context context, int resourceId) {
        XmlPullParser parser = context.getResources().getXml(resourceId);
        try {
            for (int eventType = parser.getEventType(); eventType != 1; eventType = parser.next()) {
                switch (eventType) {
                    case 0:
                        String document = parser.getName();
                        break;
                    case 2:
                        String tagName = parser.getName();
                        Constraint constraint = fillFromAttributeList(context, Xml.asAttributeSet(parser));
                        if (tagName.equalsIgnoreCase("Guideline")) {
                            constraint.mIsGuideline = true;
                        }
                        this.mConstraints.put(Integer.valueOf(constraint.mViewId), constraint);
                        break;
                    case 3:
                        break;
                    default:
                        break;
                }
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    private static int lookupID(TypedArray a, int index, int def) {
        int ret = a.getResourceId(index, def);
        if (ret == -1) {
            return a.getInt(index, -1);
        }
        return ret;
    }

    private Constraint fillFromAttributeList(Context context, AttributeSet attrs) {
        Constraint c = new Constraint();
        TypedArray a = context.obtainStyledAttributes(attrs, C0002R.styleable.ConstraintSet);
        populateConstraint(c, a);
        a.recycle();
        return c;
    }

    private void populateConstraint(Constraint c, TypedArray a) {
        int N = a.getIndexCount();
        for (int i = 0; i < N; i++) {
            int attr = a.getIndex(i);
            switch (mapToConstant.get(attr)) {
                case 1:
                    c.baselineToBaseline = lookupID(a, attr, c.baselineToBaseline);
                    break;
                case 2:
                    c.bottomMargin = a.getDimensionPixelSize(attr, c.bottomMargin);
                    break;
                case 3:
                    c.bottomToBottom = lookupID(a, attr, c.bottomToBottom);
                    break;
                case 4:
                    c.bottomToTop = lookupID(a, attr, c.bottomToTop);
                    break;
                case 5:
                    String ratio = a.getString(attr);
                    c.dimensionRatio = 0.0f;
                    c.dimensionRatioSide = -1;
                    if (ratio == null) {
                        break;
                    }
                    int len = ratio.length();
                    int commaIndex = ratio.indexOf(44);
                    if (commaIndex <= 0 || commaIndex >= len - 1) {
                        commaIndex = 0;
                    } else {
                        String dimension = ratio.substring(0, commaIndex);
                        if (dimension.equalsIgnoreCase("W")) {
                            c.dimensionRatioSide = 0;
                        } else if (dimension.equalsIgnoreCase("H")) {
                            c.dimensionRatioSide = 1;
                        }
                        commaIndex++;
                    }
                    int colonIndex = ratio.indexOf(58);
                    if (colonIndex >= 0 && colonIndex < len - 1) {
                        String nominator = ratio.substring(commaIndex, colonIndex);
                        String denominator = ratio.substring(colonIndex + 1);
                        if (nominator.length() > 0 && denominator.length() > 0) {
                            try {
                                float nominatorValue = Float.parseFloat(nominator);
                                float denominatorValue = Float.parseFloat(denominator);
                                if (nominatorValue > 0.0f && denominatorValue > 0.0f) {
                                    if (c.dimensionRatioSide != 1) {
                                        c.dimensionRatio = Math.abs(nominatorValue / denominatorValue);
                                        break;
                                    } else {
                                        c.dimensionRatio = Math.abs(denominatorValue / nominatorValue);
                                        break;
                                    }
                                }
                            } catch (NumberFormatException e) {
                                break;
                            }
                        }
                    }
                    String r = ratio.substring(commaIndex);
                    if (r.length() <= 0) {
                        break;
                    }
                    try {
                        c.dimensionRatio = Float.parseFloat(r);
                        break;
                    } catch (NumberFormatException e2) {
                        break;
                    }
                    break;
                case 6:
                    c.editorAbsoluteX = a.getDimensionPixelOffset(attr, c.editorAbsoluteX);
                    break;
                case 7:
                    c.editorAbsoluteY = a.getDimensionPixelOffset(attr, c.editorAbsoluteY);
                    break;
                case 8:
                    c.endMargin = a.getDimensionPixelSize(attr, c.endMargin);
                    break;
                case 9:
                    c.bottomToTop = lookupID(a, attr, c.endToEnd);
                    break;
                case 10:
                    c.endToStart = lookupID(a, attr, c.endToStart);
                    break;
                case 11:
                    c.goneBottomMargin = a.getDimensionPixelSize(attr, c.goneBottomMargin);
                    break;
                case 12:
                    c.goneEndMargin = a.getDimensionPixelSize(attr, c.goneEndMargin);
                    break;
                case 13:
                    c.goneLeftMargin = a.getDimensionPixelSize(attr, c.goneLeftMargin);
                    break;
                case 14:
                    c.goneRightMargin = a.getDimensionPixelSize(attr, c.goneRightMargin);
                    break;
                case 15:
                    c.goneStartMargin = a.getDimensionPixelSize(attr, c.goneStartMargin);
                    break;
                case 16:
                    c.goneTopMargin = a.getDimensionPixelSize(attr, c.goneTopMargin);
                    break;
                case 17:
                    c.guideBegin = a.getDimensionPixelOffset(attr, c.guideBegin);
                    break;
                case 18:
                    c.guideEnd = a.getDimensionPixelOffset(attr, c.guideEnd);
                    break;
                case 19:
                    c.guidePercent = a.getFloat(attr, c.guidePercent);
                    break;
                case 20:
                    c.horizontalBias = a.getFloat(attr, c.horizontalBias);
                    break;
                case 21:
                    c.mHeight = a.getLayoutDimension(attr, c.mHeight);
                    break;
                case 22:
                    c.visibility = a.getInt(attr, c.visibility);
                    c.visibility = VISIBILITY_FLAGS[c.visibility];
                    break;
                case 23:
                    c.mWidth = a.getLayoutDimension(attr, c.mWidth);
                    break;
                case 24:
                    c.leftMargin = a.getDimensionPixelSize(attr, c.leftMargin);
                    break;
                case 25:
                    c.leftToLeft = lookupID(a, attr, c.leftToLeft);
                    break;
                case 26:
                    c.leftToRight = lookupID(a, attr, c.leftToRight);
                    break;
                case 27:
                    c.orientation = a.getInt(attr, c.orientation);
                    break;
                case 28:
                    c.rightMargin = a.getDimensionPixelSize(attr, c.rightMargin);
                    break;
                case 29:
                    c.rightToLeft = lookupID(a, attr, c.rightToLeft);
                    break;
                case 30:
                    c.rightToRight = lookupID(a, attr, c.rightToRight);
                    break;
                case 31:
                    c.startMargin = a.getDimensionPixelSize(attr, c.startMargin);
                    break;
                case 32:
                    c.startToEnd = lookupID(a, attr, c.startToEnd);
                    break;
                case 33:
                    c.startToStart = lookupID(a, attr, c.startToStart);
                    break;
                case 34:
                    c.topMargin = a.getDimensionPixelSize(attr, c.topMargin);
                    break;
                case 35:
                    c.topToBottom = lookupID(a, attr, c.topToBottom);
                    break;
                case 36:
                    c.topToTop = lookupID(a, attr, c.topToTop);
                    break;
                case 37:
                    c.verticalBias = a.getFloat(attr, c.verticalBias);
                    break;
                case 38:
                    c.mViewId = a.getResourceId(attr, c.mViewId);
                    break;
                case 39:
                    c.horizontalWeight = a.getFloat(attr, c.horizontalWeight);
                    break;
                case 40:
                    c.verticalWeight = a.getFloat(attr, c.verticalWeight);
                    break;
                case 41:
                    c.horizontalChainPacked = a.getBoolean(attr, c.horizontalChainPacked);
                    break;
                case 42:
                    c.verticalChainPacked = a.getBoolean(attr, c.verticalChainPacked);
                    break;
                default:
                    Log.w(TAG, "Unknown attribute 0x" + Integer.toHexString(attr) + "   " + mapToConstant.get(attr));
                    break;
            }
        }
    }
}
