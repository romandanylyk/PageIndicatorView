package com.rd.dotpagerview.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.rd.dotpagerview.R;
import com.rd.dotpagerview.view.animation.*;
import com.rd.dotpagerview.utils.DensityUtils;

public class DotPagerView extends View {

    private static final String DEFAULT_UNSELECTED_COLOR = "#33ffffff";
    private static final String DEFAULT_SELECTED_COLOR = "#ffffff";

    private static final int DEFAULT_RADIUS_DP = 6;
    private static final int DEFAULT_PADDING_DP = 8;

    private static final float DEFAULT_SCALE_FACTOR = 1.7f;
    private static final float MIN_SCALE_FACTOR = 1;
    private static final float MAX_SCALE_FACTOR = 3;

    private int radiusPx = DensityUtils.dpToPx(DEFAULT_RADIUS_DP);
    private int paddingPx = DensityUtils.dpToPx(DEFAULT_PADDING_DP);
    private int count;

    //Color
    private int unselectedColor = Color.parseColor(DEFAULT_UNSELECTED_COLOR);
    private int selectedColor = Color.parseColor(DEFAULT_SELECTED_COLOR);

    private int frameColor;
    private int frameColorReverse;

    //Scale
    private int frameRadiusPx;
    private int frameRadiusReversePx;
    private float scaleFactor;

    //Slide
    private int frameLeftX;
    private int frameRightX;

    private int selectedPosition;
    private int selectingPosition;
    private int lastSelectedPosition;

    private boolean interactiveAnimation;

    private Paint paint;
    private AnimationType animationType = AnimationType.NONE;
    private ValueAnimation animation;

    public DotPagerView(Context context) {
        super(context);
        init(null);
    }

    public DotPagerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public DotPagerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DotPagerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
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
        radiusPx = DensityUtils.dpToPx(radiusDp);
        invalidate();
    }

    public void setPadding(int paddingDp) {
        paddingPx = DensityUtils.dpToPx(paddingDp);
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
        if (interactiveAnimation) {
            return;
        }

        lastSelectedPosition = selectedPosition;
        selectedPosition = position;

        switch (animationType) {
            case NONE:
                invalidate();
                break;

            case COLOR:
                startColorAnimation();
                break;

            case SCALE:
                startScaleAnimation();
                break;

            case WORM:
                startSlideAnimation();
                break;
        }
    }

    public int getSelection() {
        return selectedPosition;
    }

    public void setInteractiveAnimation(boolean isInteractive) {
        interactiveAnimation = isInteractive;
    }

    public void setProgress(int position, float offset) {
        if (!interactiveAnimation) {
            return;
        }

        if (position == selectingPosition) {
            lastSelectedPosition = selectedPosition;
            selectedPosition = position;
        }

        boolean isRightSlide = selectedPosition == position;
        float progress;

        if (isRightSlide) {
            progress = offset;
            selectingPosition = position + 1;
        } else {
            progress = 1 - offset;
            selectingPosition = position;
        }

        if (selectingPosition >= count) {
            selectingPosition = count - 1;
            return;
        }

        switch (animationType) {
            case COLOR:
                animation.color().with(unselectedColor, selectedColor).progress(progress);
                break;

            case SCALE:
                animation.scale().with(unselectedColor, selectedColor, radiusPx, scaleFactor).progress(progress);
                break;

            case WORM:
                int fromX = getXCoordinate(selectedPosition);
                int toX = getXCoordinate(selectingPosition);
                boolean isRightSide = selectingPosition > selectedPosition;

                animation.worm().with(fromX, toX, radiusPx, isRightSide).progress(progress);
                break;
        }
    }

    public void setViewPager(@Nullable ViewPager viewPager) {
        if (viewPager == null) {
            return;
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                setProgress(position, positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                setSelection(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void drawDotView(@NonNull Canvas canvas) {
        int y = getHeight() / 2;

        for (int i = 0; i < count; i++) {
            int x = getXCoordinate(i);
            drawDot(canvas, i, x, y);
        }
    }

    private void drawDot(@NonNull Canvas canvas, int position, int x, int y) {
        boolean selectedItem = !interactiveAnimation && (position == selectedPosition || position == lastSelectedPosition);
        boolean selectingItem = interactiveAnimation && (position == selectingPosition || position == selectedPosition);
        boolean isSelectedItem = selectedItem | selectingItem;

        if (isSelectedItem) {
            drawWithAnimationEffect(canvas, position, x, y);
        } else {
            drawWithNoEffect(canvas, position, x, y);
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

            case WORM:
                drawWithSlideAnimation(canvas, position, x, y);
                break;

            case NONE:
                drawWithNoEffect(canvas, position, x, y);
                break;
        }
    }

    private void drawWithColorAnimation(@NonNull Canvas canvas, int position, int x, int y) {
        int color = unselectedColor;

        if (interactiveAnimation) {
            if (position == selectingPosition) {
                color = frameColor;
            } else if (position == selectedPosition) {
                color = frameColorReverse;
            }

        } else {
            if (position == selectedPosition) {
                color = frameColor;
            } else if (position == lastSelectedPosition) {
                color = frameColorReverse;
            }
        }

        paint.setColor(color);
        canvas.drawCircle(x, y, radiusPx, paint);
    }

    private void drawWithScaleAnimation(@NonNull Canvas canvas, int position, int x, int y) {
        int color = unselectedColor;
        int radius = radiusPx;

        if (interactiveAnimation) {
            if (position == selectingPosition) {
                radius = frameRadiusPx;
                color = frameColor;

            } else if (position == selectedPosition) {
                radius = frameRadiusReversePx;
                color = frameColorReverse;
            }

        } else {
            if (position == selectedPosition) {
                radius = frameRadiusPx;
                color = frameColor;

            } else if (position == lastSelectedPosition) {
                radius = frameRadiusReversePx;
                color = frameColorReverse;
            }
        }

        paint.setColor(color);
        canvas.drawCircle(x, y, radius, paint);
    }

    private void drawWithSlideAnimation(@NonNull Canvas canvas, int position, int x, int y) {
        int radius = radiusPx;
        RectF rect = null;

        int left = frameLeftX;
        int right = frameRightX;
        int top = y - radius;
        int bot = y + radius;

        if (interactiveAnimation && position == selectingPosition) {
            rect = new RectF(left, top, right, bot);

        } else if (!interactiveAnimation && position == selectedPosition) {
            rect = new RectF(left, top, right, bot);
        }

        paint.setColor(unselectedColor);
        canvas.drawCircle(x, y, radius, paint);

        if (rect != null) {
            paint.setColor(selectedColor);
            canvas.drawRoundRect(rect, radiusPx, radiusPx, paint);
        }
    }

    private void drawWithNoEffect(@NonNull Canvas canvas, int position, int x, int y) {
        int radius = radiusPx;
        int color = unselectedColor;

        if (animationType == AnimationType.SCALE) {
            radius /= scaleFactor;
        }

        if (position == selectedPosition) {
            color = selectedColor;
        }

        paint.setColor(color);
        canvas.drawCircle(x, y, radius, paint);
    }

    private void init(@Nullable AttributeSet attrs) {
        initAttributes(attrs);
        initFrameValues();

        initAnimation();
        initPaint();
    }

    private void initFrameValues() {
        frameColor = selectedColor;
        frameColorReverse = unselectedColor;

        frameRadiusPx = radiusPx;
        frameRadiusReversePx = radiusPx;

        int xCoordinate = getXCoordinate(selectedPosition);
        if (xCoordinate - radiusPx >= 0) {
            frameLeftX = xCoordinate - radiusPx;
            frameRightX = xCoordinate + radiusPx;

        } else {
            frameLeftX = xCoordinate;
            frameRightX = xCoordinate + (radiusPx * 2);
        }
    }

    private void initAnimation() {
        animation = new ValueAnimation(new ValueAnimation.UpdateListener() {
            @Override
            public void onColorAnimationUpdated(int color, int colorReverse) {
                frameColor = color;
                frameColorReverse = colorReverse;
                invalidate();
            }

            @Override
            public void onScaleAnimationUpdated(int color, int colorReverse, int radius, int radiusReverse) {
                frameColor = color;
                frameColorReverse = colorReverse;

                frameRadiusPx = radius;
                frameRadiusReversePx = radiusReverse;
                invalidate();
            }

            @Override
            public void onSlideAnimationUpdated(int leftX, int rightX) {
                frameLeftX = leftX;
                frameRightX = rightX;
                invalidate();
            }
        });
    }

    private void initPaint() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
    }

    private void initAttributes(@Nullable AttributeSet attrs) {
        if (attrs == null) {
            return;
        }

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.DotPagerView, 0, 0);

        count = typedArray.getInt(R.styleable.DotPagerView_count, 0);
        int position = typedArray.getInt(R.styleable.DotPagerView_select, 0);

        if (position < 0) {
            position = 0;
        } else if (position > count - 1) {
            position = count - 1;
        }

        selectedPosition = position;
        selectingPosition = position;

        radiusPx = (int) typedArray.getDimension(R.styleable.DotPagerView_radius, radiusPx);
        paddingPx = (int) typedArray.getDimension(R.styleable.DotPagerView_padding, paddingPx);
        scaleFactor = typedArray.getFloat(R.styleable.DotPagerView_scaleFactor, DEFAULT_SCALE_FACTOR);

        if (scaleFactor < MIN_SCALE_FACTOR) {
            scaleFactor = MIN_SCALE_FACTOR;
        } else if (scaleFactor > MAX_SCALE_FACTOR) {
            scaleFactor = MAX_SCALE_FACTOR;
        }

        unselectedColor = typedArray.getColor(R.styleable.DotPagerView_unselectedColor, unselectedColor);
        selectedColor = typedArray.getColor(R.styleable.DotPagerView_selectedColor, selectedColor);

        interactiveAnimation = typedArray.getBoolean(R.styleable.DotPagerView_animationInteractiveEffect, false);
        int index = typedArray.getInt(R.styleable.DotPagerView_animationType, AnimationType.NONE.ordinal());
        animationType = getAnimationType(index);

        typedArray.recycle();
    }

    private AnimationType getAnimationType(int index) {
        switch (index) {
            case 0:
                return AnimationType.NONE;
            case 1:
                return AnimationType.COLOR;
            case 2:
                return AnimationType.SCALE;
            case 3:
                return AnimationType.WORM;
        }

        return AnimationType.NONE;
    }

    private void startColorAnimation() {
        animation.color().with(unselectedColor, selectedColor).start();
    }

    private void startScaleAnimation() {
        animation.scale().with(unselectedColor, selectedColor, radiusPx, scaleFactor).start();
    }

    private void startSlideAnimation() {
        int fromX = getXCoordinate(lastSelectedPosition);
        int toX = getXCoordinate(selectedPosition);
        boolean isRightSide = selectedPosition > lastSelectedPosition;

        animation.worm().end();
        animation.worm().with(fromX, toX, radiusPx, isRightSide).start();
    }

    private int getXCoordinate(int position) {
        int actualViewWidth = calculateActualViewWidth();
        int x = (getWidth() - actualViewWidth) / 2;

        if (x < 0) {
            x = 0;
        }

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
