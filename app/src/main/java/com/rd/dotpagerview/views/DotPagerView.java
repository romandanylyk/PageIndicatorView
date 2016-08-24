package com.rd.dotpagerview.views;

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
import android.util.Log;
import android.view.View;

import com.rd.dotpagerview.utils.DensityUtils;

public class DotPagerView extends View {

    private static final String DOT_DEFAULT_UNSELECTED_COLOR = "#33ffffff";
    private static final String DOT_DEFAULT_SELECTED_COLOR = "#ffffff";

    private static final int DOT_DEFAULT_RADIUS_DP = 8;
    private static final int DOT_DEFAULT_PADDING_DP = 16;
    private static final int DOT_SIDES_PADDING_DP = 4;

    private static final int ANIMATION_ALPHA_DURATION = 500;
    private static final int ANIMATION_ALPHA_INCREASE = 10;
    private static final int ANIMATION_ALPHA_MAX = 255;
    private static final int ANIMATION_ALPHA_MIN = 0;
    private int alpha;

    private int dotCount;
    private int dotRadiusPx;
    private int dotPaddingPx;
    private int dotPaddingSidesPx;

    private int dotsUnselectedColor;
    private int dotsSelectedColor;

    private int selectedPosition;

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

        int dotDiameterPx = dotRadiusPx * 2;
        int widthPx = (dotDiameterPx * dotCount) + (dotPaddingPx * (dotCount - 1));
        int heightPx = dotPaddingSidesPx + dotDiameterPx;

        setMeasuredDimension(widthPx, heightPx);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawDotView(canvas);
    }

    public void setDotsCount(int count) {
        dotCount = count;
        invalidate();
    }

    public void setDotsRadius(int radiusDp) {
        dotRadiusPx = DensityUtils.dpToPx(getContext(), radiusDp);
        invalidate();
    }

    public void setDotPadding(int paddingDp) {
        dotPaddingPx = DensityUtils.dpToPx(getContext(), paddingDp);
        invalidate();
    }

    public void setDotsUnselectedColor(int color) {
        dotsUnselectedColor = color;
        invalidate();
    }

    public void setDotsSelectedColor(int color) {
        dotsSelectedColor = color;
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
        selectedPosition = position;

        switch (animationType) {
            case ALPHA:
                alphaAnimation();
                break;

            case NONE:
                break;
        }
    }

    private void alphaAnimation() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(ANIMATION_ALPHA_MIN, ANIMATION_ALPHA_MAX);
        valueAnimator.setDuration(ANIMATION_ALPHA_DURATION);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                alpha = (int) animation.getAnimatedValue();
                invalidate();
            }
        });

        valueAnimator.start();
    }

    private void drawDotView(@NonNull Canvas canvas) {
        int x = 0;
        int y = getHeight() / 2;

        for (int i = 0; i < dotCount; i++) {
            x += dotRadiusPx;
            drawDot(canvas, i, x, y);
            x += dotRadiusPx + dotPaddingPx;
        }
    }

    private void drawDot(@NonNull Canvas canvas, int position, int x, int y) {
        if (position == selectedPosition) {
            paint.setColor(dotsSelectedColor);

            switch (animationType) {
                case ALPHA:
                    paint.setAlpha(alpha);
                    break;
            }

        } else {
            paint.setAlpha(ANIMATION_ALPHA_MAX);
            paint.setColor(dotsUnselectedColor);
        }

        canvas.drawCircle(x, y, dotRadiusPx, paint);
    }

    private void init() {
        initDefaultValues();
        initPaint();
    }

    private void initDefaultValues() {
        dotsUnselectedColor = Color.parseColor(DOT_DEFAULT_UNSELECTED_COLOR);
        dotsSelectedColor = Color.parseColor(DOT_DEFAULT_SELECTED_COLOR);

        dotRadiusPx = DensityUtils.dpToPx(getContext(), DOT_DEFAULT_RADIUS_DP);
        dotPaddingPx = DensityUtils.dpToPx(getContext(), DOT_DEFAULT_PADDING_DP);
        dotPaddingSidesPx = DensityUtils.dpToPx(getContext(), DOT_SIDES_PADDING_DP);
    }

    private void initPaint() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
    }
}
