package com.rd.dotpagerview.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.*;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.rd.dotpagerview.view.animation.*;
import com.rd.dotpagerview.utils.DensityUtils;

public class DotPagerView extends View {

    private static final String DEFAULT_UNSELECTED_COLOR = "#33ffffff";
    private static final String DEFAULT_SELECTED_COLOR = "#ffffff";

    private static final int DEFAULT_RADIUS_DP = 8;
    private static final int DEFAULT_PADDING_DP = 16;

    private int radiusPx = DensityUtils.dpToPx(getContext(), DEFAULT_RADIUS_DP);
    private int paddingPx = DensityUtils.dpToPx(getContext(), DEFAULT_PADDING_DP);
    private int count;

    //Color
    private int unselectedColor = Color.parseColor(DEFAULT_UNSELECTED_COLOR);
    private int selectedColor = Color.parseColor(DEFAULT_SELECTED_COLOR);

    private int frameColor;
    private int frameColorReverse;

    //Scale
    private int frameRadiusPx;
    private int frameRadiusReversePx;

    //Slide
    private int frameLeftX;
    private int frameToX;

    private int frameRightX;
    private int frameReverseToX;

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

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int dotDiameterPx = radiusPx * 2;
        int desiredHeight = dotDiameterPx;
        int desiredWidth = 0;

        if (count != 0) {
            desiredWidth = (dotDiameterPx * count) + (paddingPx * (count - 1));
        }

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(desiredWidth, widthSize);
        } else {
            width = desiredWidth;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(desiredHeight, heightSize);
        } else {
            height = desiredHeight;
        }

        setMeasuredDimension(width, height);
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

            case SLIDE:
                startSlideAnimation();
                break;
        }
    }

    private void drawDotView(@NonNull Canvas canvas) {
        int y = getHeight() / 2;

        for (int i = 0; i < count; i++) {
            int x = getDotXCoordinate(i);
            drawDot(canvas, i, x, y);
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

            case SLIDE:
                drawWithSlideAnimation(canvas, position, x, y);
                break;

            case NONE:
                drawWithNoEffect(canvas, x, y);
                break;
        }
    }

    private void drawWithColorAnimation(@NonNull Canvas canvas, int position, int x, int y) {
        int color = selectedColor;

        if (position == selectedPosition) {
            color = frameColor;
        } else if (position == lastSelectedPosition) {
            color = frameColorReverse;
        }

        paint.setColor(color);
        canvas.drawCircle(x, y, radiusPx, paint);
    }

    private void drawWithScaleAnimation(@NonNull Canvas canvas, int position, int x, int y) {
        int color = selectedColor;
        int radius = radiusPx;

        if (position == selectedPosition) {
            color = frameColor;
            radius = frameRadiusPx;
        } else if (position == lastSelectedPosition) {
            color = frameColorReverse;
            radius = frameRadiusReversePx;
        }

        paint.setColor(color);
        canvas.drawCircle(x, y, radius, paint);
    }

    private void drawWithSlideAnimation(@NonNull Canvas canvas, int position, int x, int y) {
        int radius = radiusPx;
        RectF rect = null;

        if (position == selectedPosition) {
            int left ;
            int right;
            int top = y - radius;
            int bot = y + radius;

            if (lastSelectedPosition > selectedPosition) {
                left = frameRightX;
                right = frameLeftX;
            } else {
                left = frameLeftX;
                right = frameRightX;
            }

            rect = new RectF(left, top, right, bot);

        }

        paint.setColor(unselectedColor);
        canvas.drawCircle(x, y, radius, paint);

        if (rect != null) {
            paint.setColor(selectedColor);
            canvas.drawRoundRect(rect, radiusPx, radiusPx, paint);
        }
    }

    private void drawWithNoEffect(@NonNull Canvas canvas, int x, int y) {
        boolean isScaleAnimation = animationType == AnimationType.SCALE || animationType == AnimationType.COLOR_AND_SCALE;
        int radius = radiusPx;

        if (isScaleAnimation) {
            radius /= ScaleAnimation.SCALE_FACTOR;
        }

        paint.setColor(unselectedColor);
        canvas.drawCircle(x, y, radius, paint);
    }

    private void init() {
        initDefaultValues();
        initPaint();
    }

    private void initDefaultValues() {
        frameColor = selectedColor;
        frameColorReverse = unselectedColor;

        frameRadiusPx = radiusPx;
        frameRadiusReversePx = radiusPx;

        frameLeftX = 0;
        frameRightX = 0;
    }

    private void initPaint() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
    }

    private void startColorAnimation() {
        ColorAnimation.start(selectedColor, unselectedColor, new ColorAnimation.Listener() {
            @Override
            public void onColorAnimationUpdated(int color, int colorReverse) {
                frameColor = color;
                frameColorReverse = colorReverse;
                invalidate();
            }
        });
    }

    private void startScaleAnimation() {
        ScaleAnimation.start(radiusPx, new ScaleAnimation.Listener() {
            @Override
            public void onScaleAnimationUpdated(int radius, int radiusReverse) {
                frameRadiusPx = radius;
                frameRadiusReversePx = radiusReverse;
                invalidate();
            }
        });
    }

    private void startColorAndScaleAnimation() {
        ColorAndScaleAnimation.start(selectedColor, unselectedColor, radiusPx, new ColorAndScaleAnimation.Listener() {
            @Override
            public void onScaleAnimationUpdated(int color, int colorReverse, int radius, int radiusReverse) {
                frameColor = color;
                frameColorReverse = colorReverse;

                frameRadiusPx = radius;
                frameRadiusReversePx = radiusReverse;

                invalidate();
            }
        });
    }

    private void startSlideAnimation() {
        int fromX;
        int toX;

        int reverseFromX;
        int reverseToX;

        int xSelected = getDotXCoordinate(selectedPosition);
        int xLastSelected = getDotXCoordinate(lastSelectedPosition);

        frameLeftX = 0;
        frameRightX = 0;

        if (selectedPosition > lastSelectedPosition) {
            fromX = xLastSelected - radiusPx;
            toX = xSelected + radiusPx;

            reverseFromX = fromX;
            reverseToX = xSelected - radiusPx;

        } else if (selectedPosition < lastSelectedPosition) {
            fromX = xLastSelected + radiusPx;
            toX = xSelected - radiusPx;

            reverseFromX = fromX;
            reverseToX = xSelected + radiusPx;

        } else {
            return;
        }

        SlideAnimation.startSlideAnimation(fromX, toX, reverseFromX, reverseToX, new SlideAnimation.Listener() {
            @Override
            public void onSlideAnimationUpdated(int leftX, int rightX) {
                frameLeftX = leftX;
                frameRightX = rightX;
                invalidate();
            }
        });
    }

    private int getDotXCoordinate(int position) {
        int actualViewWidth = calculateActualViewWidth();
        int x = (getWidth() - actualViewWidth) / 2;

        for (int i = 0; i < count; i++) {
            x += radiusPx;
            if (position == i) {
                return x;
            }

            x += radiusPx + paddingPx;
        }

        return x;
    }

    private int calculateActualViewWidth() {
        int width = 0;
        int diameter = radiusPx * 2;

        for (int i = 0; i < count; i++) {
            width += diameter;

            if (i < count - 1) {
                width += paddingPx;
            }
        }

        return width;
    }
}
