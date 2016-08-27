package com.rd.dotpagerview.views;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;

import com.rd.dotpagerview.utils.DotAnimationUtils;
import com.rd.dotpagerview.utils.DensityUtils;

public class DotPagerView extends View {

    private static final String DEFAULT_UNSELECTED_COLOR = "#33ffffff";
    private static final String DEFAULT_SELECTED_COLOR = "#ffffff";

    private static final int DEFAULT_RADIUS_DP = 8;
    private static final int DEFAULT_PADDING_DP = 16;
    private static final int SIDES_PADDING_DP = 4;

    private int count;
    private int radiusPx;
    private int paddingPx;
    private int paddingSidesPx;

    private int unselectedColor;
    private int selectedColor;

    //Color
    private int currColor;
    private int reverseColor;

    //Scale
    private int currRadiusPx;
    private int reverseRadiusPx;

    private int selectedPosition;
    private int lastSelectedPosition;

    private Paint paint;
    private AnimationType animationType = AnimationType.NONE;

    public DotPagerView(Context context) {
        super(context);
        init();
    }

    public DotPagerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DotPagerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DotPagerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int dotDiameterPx = radiusPx * 2;
        int widthPx = (dotDiameterPx * count) + (paddingPx * (count - 1));
        int heightPx = paddingSidesPx + dotDiameterPx;

        setMeasuredDimension(widthPx, heightPx);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawDotView(canvas);
    }

    public void setCount(int count) {
        this.count = count;
        invalidate();
    }

    public void setRadius(int radiusDp) {
        radiusPx = DensityUtils.dpToPx(getContext(), radiusDp);
        invalidate();
    }

    public void setPadding(int paddingDp) {
        paddingPx = DensityUtils.dpToPx(getContext(), paddingDp);
        invalidate();
    }

    public void setUnselectedColor(int color) {
        unselectedColor = color;
        invalidate();
    }

    public void setSelectedColor(int color) {
        selectedColor = color;
        invalidate();
    }

    public void setAnimationType(@Nullable AnimationType type) {
        if (type != null) {
            animationType = type;
        } else {
            animationType = AnimationType.NONE;
        }
    }

    public void setSelection(int position) {
        lastSelectedPosition = selectedPosition;
        selectedPosition = position;

        switch (animationType) {
            case COLOR:
                startColorAnimation();
                break;

            case SCALE:
                startScaleAnimation();
                break;

            case COLOR_AND_SCALE:
                startColorAndScaleAnimation();
                break;
        }
    }

    private void drawDotView(@NonNull Canvas canvas) {
        int x = 0;
        int y = getHeight() / 2;

        for (int i = 0; i < count; i++) {
            x += radiusPx;
            drawDot(canvas, i, x, y);
            x += radiusPx + paddingPx;
        }
    }

    private void drawDot(@NonNull Canvas canvas, int position, int x, int y) {
        if (position == selectedPosition || position == lastSelectedPosition) {
            drawWithAnimationEffect(canvas, position, x, y);
        } else {
            drawWithNoEffect(canvas, x, y);
        }
    }

    private void drawWithAnimationEffect(@NonNull Canvas canvas, int position, int x, int y) {
        switch (animationType) {
            case COLOR:
                drawWithColorAnimation(canvas, position, x, y);
                break;

            case SCALE:
            case COLOR_AND_SCALE:
                drawWithScaleAnimation(canvas, position, x, y);
                break;

            case NONE:
                drawWithNoEffect(canvas, x, y);
                break;
        }
    }

    private void drawWithColorAnimation(@NonNull Canvas canvas, int position, int x, int y) {
        int color = selectedColor;

        if (position == selectedPosition) {
            color = currColor;
        } else if (position == lastSelectedPosition) {
            color = reverseColor;
        }

        paint.setColor(color);
        canvas.drawCircle(x, y, radiusPx, paint);
    }

    private void drawWithScaleAnimation(@NonNull Canvas canvas, int position, int x, int y) {
        int color = selectedColor;
        int radius = radiusPx;

        if (position == selectedPosition) {
            color = currColor;
            radius = currRadiusPx;
        } else if (position == lastSelectedPosition) {
            color = reverseColor;
            radius = reverseRadiusPx;
        }

        paint.setColor(color);
        canvas.drawCircle(x, y, radius, paint);
    }

    private void drawWithNoEffect(@NonNull Canvas canvas, int x, int y) {
        boolean isScaleAnimation = animationType == AnimationType.SCALE || animationType == AnimationType.COLOR_AND_SCALE;
        int radius = radiusPx;

        if (isScaleAnimation) {
            radius /= DotAnimationUtils.SCALE_FACTOR;
        }

        paint.setColor(unselectedColor);
        canvas.drawCircle(x, y, radius, paint);
    }

    private void init() {
        initDefaultValues();
        initPaint();
    }

    private void initDefaultValues() {
        unselectedColor = Color.parseColor(DEFAULT_UNSELECTED_COLOR);
        selectedColor = Color.parseColor(DEFAULT_SELECTED_COLOR);

        radiusPx = DensityUtils.dpToPx(getContext(), DEFAULT_RADIUS_DP);
        paddingPx = DensityUtils.dpToPx(getContext(), DEFAULT_PADDING_DP);
        paddingSidesPx = DensityUtils.dpToPx(getContext(), SIDES_PADDING_DP);

        currColor = selectedColor;
        reverseColor = unselectedColor;

        currRadiusPx = radiusPx;
        reverseRadiusPx = radiusPx;
    }

    private void initPaint() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
    }

    @SuppressWarnings("AccessStaticViaInstance")
    private void startColorAnimation() {
        DotAnimationUtils.startColorAnimation(selectedColor, unselectedColor, new DotAnimationUtils.Listener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                currColor = (int) animation.getAnimatedValue(DotAnimationUtils.ANIMATION_COLOR);
                reverseColor = (int) animation.getAnimatedValue(DotAnimationUtils.ANIMATION_COLOR_REVERSE);
                invalidate();
            }
        });
    }

    private void startScaleAnimation() {
        DotAnimationUtils.startScaleAnimation(radiusPx, new DotAnimationUtils.Listener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                currRadiusPx = (Integer) animation.getAnimatedValue(DotAnimationUtils.ANIMATION_SCALE);
                reverseRadiusPx = (Integer) animation.getAnimatedValue(DotAnimationUtils.ANIMATION_SCALE_REVERSE);
                invalidate();
            }
        });
    }

    private void startColorAndScaleAnimation() {
        DotAnimationUtils.startColorAndScaleAnimation(selectedColor, unselectedColor, radiusPx, new DotAnimationUtils.Listener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                currColor = (Integer) animation.getAnimatedValue(DotAnimationUtils.ANIMATION_COLOR);
                reverseColor = (Integer) animation.getAnimatedValue(DotAnimationUtils.ANIMATION_COLOR_REVERSE);

                currRadiusPx = (Integer) animation.getAnimatedValue(DotAnimationUtils.ANIMATION_SCALE);
                reverseRadiusPx = (Integer) animation.getAnimatedValue(DotAnimationUtils.ANIMATION_SCALE_REVERSE);
                invalidate();
            }
        });
    }
}
