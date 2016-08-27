package com.rd.dotpagerview.views;

import android.animation.ArgbEvaluator;
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
import android.view.animation.DecelerateInterpolator;

import com.rd.dotpagerview.utils.DensityUtils;

public class DotPagerView extends View {

    private static final String DEFAULT_UNSELECTED_COLOR = "#33ffffff";
    private static final String DEFAULT_SELECTED_COLOR = "#ffffff";

    private static final int DEFAULT_RADIUS_DP = 8;
    private static final int DEFAULT_PADDING_DP = 16;
    private static final int SIDES_PADDING_DP = 4;

    private static final int ANIMATION_DURATION = 300;
    private static final float SCALE_FACTOR = 1.5f;

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
        }
    }

    private void startColorAnimation() {
        ValueAnimator animator = ValueAnimator.ofInt(unselectedColor, selectedColor);
        animator.setDuration(ANIMATION_DURATION);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setEvaluator(new ArgbEvaluator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currColor = (int) animation.getAnimatedValue();
            }
        });

        ValueAnimator reverseAnimator = ValueAnimator.ofInt(selectedColor, unselectedColor);
        reverseAnimator.setDuration(ANIMATION_DURATION);
        reverseAnimator.setInterpolator(new DecelerateInterpolator());
        reverseAnimator.setEvaluator(new ArgbEvaluator());
        reverseAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                reverseColor = (int) animation.getAnimatedValue();
                invalidate();
            }
        });

        animator.start();
        reverseAnimator.start();
    }

    private void startScaleAnimation() {
        ValueAnimator animator = ValueAnimator.ofInt((int) (radiusPx / SCALE_FACTOR), radiusPx);
        animator.setDuration(ANIMATION_DURATION);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currRadiusPx = (int) animation.getAnimatedValue();
                invalidate();
            }
        });

        animator.start();
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
            color = selectedColor;
            radius = currRadiusPx;
        } else if (position == lastSelectedPosition) {
            color = unselectedColor;

            int scale = (int) (radiusPx / SCALE_FACTOR);
            radius = scale + (radiusPx - currRadiusPx);
        }

        paint.setColor(color);
        canvas.drawCircle(x, y, radius, paint);
    }

    private void drawWithNoEffect(@NonNull Canvas canvas, int x, int y) {
        int radius = radiusPx;

        if (animationType == AnimationType.SCALE) {
            radius /= 2;
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
        currRadiusPx = radiusPx;
    }

    private void initPaint() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
    }
}
