package com.rd;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.text.TextUtilsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;
import com.rd.animation.*;
import com.rd.pageindicatorview.R;
import com.rd.utils.DensityUtils;
import com.rd.utils.IdUtils;

public class PageIndicatorView extends View implements ViewPager.OnPageChangeListener {

    private static final int DEFAULT_CIRCLES_COUNT = 3;
    private static final int COUNT_NOT_SET = -1;

    private static final int DEFAULT_RADIUS_DP = 6;
    private static final int DEFAULT_PADDING_DP = 8;

    private int radiusPx;
    private int paddingPx;
    private int strokePx;

    private int count;
    private boolean isCountSet;

    //Color
    private int unselectedColor;
    private int selectedColor;

    private int frameColor;
    private int frameColorReverse;

    // Orientation
    private Orientation orientation = Orientation.HORIZONTAL;

    //Scale
    private int frameRadiusPx;
    private int frameRadiusReversePx;
    private float scaleFactor;

    //Fill
    private int frameStrokePx;
    private int frameStrokeReversePx;

    //Worm
    private int frameFrom;
    private int frameTo;

    //Slide & Drop
    private int frameSlideFrom;
    private int frameY;

    //Thin Worm
    private int frameHeight;
    private boolean autoVisibility;

    private int selectedPosition;
    private int selectingPosition;
    private int lastSelectedPosition;

    private boolean isFrameValuesSet;
    private boolean interactiveAnimation;
    private long animationDuration;

    private DataSetObserver setObserver;
    private boolean dynamicCount;

    private Paint fillPaint = new Paint();
    private Paint strokePaint = new Paint();
    private RectF rect = new RectF();

    private AnimationType animationType = AnimationType.NONE;
    private ValueAnimation animation;

    private ViewPager viewPager;
    private int viewPagerId;
    private RtlMode rtlMode = RtlMode.Off;

    public PageIndicatorView(Context context) {
        super(context);
        init(null);
    }

    public PageIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PageIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PageIndicatorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        findViewPager();
    }

    @Override
    protected void onDetachedFromWindow() {
        unRegisterSetObserver();
        super.onDetachedFromWindow();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        PositionSavedState positionSavedState = new PositionSavedState(super.onSaveInstanceState());
        positionSavedState.setSelectedPosition(selectedPosition);
        positionSavedState.setSelectingPosition(selectingPosition);
        positionSavedState.setLastSelectedPosition(lastSelectedPosition);

        return positionSavedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof PositionSavedState) {
            PositionSavedState positionSavedState = (PositionSavedState) state;
            this.selectedPosition = positionSavedState.getSelectedPosition();
            this.selectingPosition = positionSavedState.getSelectingPosition();
            this.lastSelectedPosition = positionSavedState.getLastSelectedPosition();
            super.onRestoreInstanceState(positionSavedState.getSuperState());
        } else {
            super.onRestoreInstanceState(state);
        }
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int circleDiameterPx = radiusPx * 2;
        int desiredWidth = 0;
        int desiredHeight = 0;

        if (orientation == Orientation.HORIZONTAL) {
            desiredHeight = circleDiameterPx + strokePx;
        } else {
            desiredWidth = circleDiameterPx + strokePx;
        }

        if (count != 0) {
            int diameterSum = circleDiameterPx * count;
            int strokeSum = (strokePx * 2) * count;
            int paddingSum = paddingPx * (count - 1);

            if (orientation == Orientation.HORIZONTAL) {
                desiredWidth = diameterSum + strokeSum + paddingSum;
            } else {
                desiredHeight = diameterSum + strokeSum + paddingSum;
            }
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

        if (animationType == AnimationType.DROP) {
            if (orientation == Orientation.HORIZONTAL) {
                height *= 2;
            } else {
                width *= 2;
            }
        }

        if (width < 0) {
            width = 0;
        }

        if (height < 0) {
            height = 0;
        }

        setMeasuredDimension(width, height);
    }

    private boolean isViewMeasured() {
        return getMeasuredHeight() != 0 || getMeasuredWidth() != 0;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        setupFrameValues();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawIndicatorView(canvas);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (isViewMeasured() && interactiveAnimation && animationType != AnimationType.NONE) {
            onPageScroll(position, positionOffset);
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (viewPager != null && viewPager.getAdapter() != null) {
            int pageCount = viewPager.getAdapter().getCount();

            if (pageCount < count) {
                return;
            }
        }

        if (isViewMeasured() && (!interactiveAnimation || animationType == AnimationType.NONE)) {
            if (isRtl()) {
                position = (count - 1) - position;
            }

            setSelection(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {/*empty*/}

    /**
     * Set static number of circle indicators to be displayed.
     *
     * @param count total count of indicators.
     */
    public void setCount(int count) {
        if (this.count != count) {
            this.count = count;
            this.isCountSet = true;

            resetFrameValues();
            updateVisibility();
            requestLayout();
        }
    }

    /**
     * Return number of circle indicators
     */
    public int getCount() {
        return count;
    }

    /**
     * Dynamic count will automatically update number of circle indicators
     * if {@link ViewPager} page count updated on run-time. If new count will be bigger than current count,
     * selected circle will stay as it is, otherwise it will be set to last one.
     * Note: works if {@link ViewPager} set and already have it's adapter. See {@link #setViewPager(ViewPager)}.
     *
     * @param dynamicCount boolean value to add/remove indicators dynamically.
     */
    public void setDynamicCount(boolean dynamicCount) {
        this.dynamicCount = dynamicCount;
        if (dynamicCount) {
            registerSetObserver();
        } else {
            unRegisterSetObserver();
        }
    }

    /**
     * Set radius in dp of each circle indicator. Default value is {@link PageIndicatorView#DEFAULT_RADIUS_DP}.
     * Note: make sure you set circle Radius, not a Diameter.
     *
     * @param radiusDp radius of circle in dp.
     */
    public void setRadius(int radiusDp) {
        if (radiusDp < 0) {
            radiusDp = 0;
        }

        radiusPx = DensityUtils.dpToPx(radiusDp);
        invalidate();
    }

    /**
     * Set radius in px of each circle indicator. Default value is {@link PageIndicatorView#DEFAULT_RADIUS_DP}.
     * Note: make sure you set circle Radius, not a Diameter.
     *
     * @param radiusPx radius of circle in px.
     */
    public void setRadius(float radiusPx) {
        if (radiusPx < 0) {
            radiusPx = 0;
        }

        this.radiusPx = (int) radiusPx;
        invalidate();
    }

    /**
     * Return radius of each circle indicators in px. If custom radius is not set, return
     * default value {@link PageIndicatorView#DEFAULT_RADIUS_DP}.
     */
    public int getRadius() {
        return radiusPx;
    }

    /**
     * Set padding in dp between each circle indicator. Default value is {@link PageIndicatorView#DEFAULT_PADDING_DP}.
     *
     * @param paddingDp padding between circles in dp.
     */
    public void setPadding(int paddingDp) {
        if (paddingDp < 0) {
            paddingDp = 0;
        }

        paddingPx = DensityUtils.dpToPx(paddingDp);
        invalidate();
    }

    /**
     * Set padding in px between each circle indicator. Default value is {@link PageIndicatorView#DEFAULT_PADDING_DP}.
     *
     * @param paddingPx padding between circles in px.
     */
    public void setPadding(float paddingPx) {
        if (paddingPx < 0) {
            paddingPx = 0;
        }

        this.paddingPx = (int) paddingPx;
        invalidate();
    }

    /**
     * Return padding in px between each circle indicator. If custom padding is not set,
     * return default value {@link PageIndicatorView#DEFAULT_PADDING_DP}.
     */
    public int getPadding() {
        return paddingPx;
    }

    /**
     * Set scale factor used in {@link AnimationType#SCALE} animation.
     * Defines size of unselected indicator circles in comparing to selected one.
     * Minimum and maximum values are {@link ScaleAnimation#MAX_SCALE_FACTOR} and {@link ScaleAnimation#MIN_SCALE_FACTOR}.
     * See also {@link ScaleAnimation#DEFAULT_SCALE_FACTOR}.
     *
     * @param factor float value in range between 0 and 1.
     */
    public void setScaleFactor(float factor) {
        if (factor > ScaleAnimation.MAX_SCALE_FACTOR) {
            factor = ScaleAnimation.MAX_SCALE_FACTOR;
        } else if (factor < ScaleAnimation.MIN_SCALE_FACTOR) {
            factor = ScaleAnimation.MIN_SCALE_FACTOR;
        }

        scaleFactor = factor;
    }

    /**
     * Returns scale factor values used in {@link AnimationType#SCALE} animation.
     * Defines size of unselected indicator circles in comparing to selected one.
     * Minimum and maximum values are {@link ScaleAnimation#MAX_SCALE_FACTOR} and {@link ScaleAnimation#MIN_SCALE_FACTOR}.
     * See also {@link ScaleAnimation#DEFAULT_SCALE_FACTOR}.
     *
     * @return float value that indicate scale factor.
     */
    public float getScaleFactor() {
        return scaleFactor;
    }

    /**
     * Set stroke width in px to draw while {@link AnimationType#FILL} is selected.
     * Default value is {@link FillAnimation#DEFAULT_STROKE_DP}
     *
     * @param strokePx stroke width in px.
     */
    public void setStrokeWidth(float strokePx) {
        if (strokePx < 0) {
            strokePx = 0;

        } else if (strokePx > radiusPx) {
            strokePx = radiusPx;
        }

        this.strokePx = (int) strokePx;
        invalidate();
    }

    /**
     * Set stroke width in dp to draw while {@link AnimationType#FILL} is selected.
     * Default value is {@link FillAnimation#DEFAULT_STROKE_DP}
     *
     * @param strokeDp stroke width in dp.
     */

    public void setStrokeWidth(int strokeDp) {
        int strokePx = DensityUtils.dpToPx(strokeDp);

        if (strokePx < 0) {
            strokePx = 0;

        } else if (strokePx > radiusPx) {
            strokePx = radiusPx;
        }

        this.strokePx = strokePx;
        invalidate();
    }

    /**
     * Return stroke width in px. If custom stroke width is not set and {@link AnimationType#FILL} is selected.
     */
    public int getStrokeWidth() {
        return strokePx;
    }

    /**
     * Set color of unselected state to each circle indicator. Default color {@link ColorAnimation#DEFAULT_UNSELECTED_COLOR}.
     *
     * @param color color of each unselected circle.
     */
    public void setUnselectedColor(int color) {
        unselectedColor = color;
        invalidate();
    }

    /**
     * Return color of unselected state of each circle indicator. If custom unselected color
     * is not set, return default color {@link ColorAnimation#DEFAULT_UNSELECTED_COLOR}.
     */
    public int getUnselectedColor() {
        return unselectedColor;
    }

    /**
     * Set color of selected state to circle indicator. Default color is white {@link ColorAnimation#DEFAULT_SELECTED_COLOR}.
     *
     * @param color color selected circle.
     */
    public void setSelectedColor(int color) {
        selectedColor = color;
        invalidate();
    }

    /**
     * Automatically hide (View.INVISIBLE) PageIndicatorView while indicator count is <= 1.
     * Default is true.
     *
     * @param autoVisibility auto hide indicators.
     */
    public void setAutoVisibility(boolean autoVisibility) {
        if(!autoVisibility){
            setVisibility(VISIBLE);
        }

        this.autoVisibility = autoVisibility;
        updateVisibility();
    }

    /**
     * Set orientation for indicator, one of HORIZONTAL or VERTICAL.
     * Default is HORIZONTAL.
     *
     * @param orientation an orientation to display page indicators..
     */
    public void setOrientation(@Nullable Orientation orientation) {
        if (orientation != null) {
            this.orientation = orientation;
            requestLayout();
        }
    }

    /**
     * Return color of selected circle indicator. If custom unselected color.
     * is not set, return default color {@link ColorAnimation#DEFAULT_SELECTED_COLOR}.
     */
    public int getSelectedColor() {
        return selectedColor;
    }

    /**
     * Set animation duration time in millisecond. Default animation duration time is {@link AbsAnimation#DEFAULT_ANIMATION_TIME}.
     * (Won't affect on anything unless {@link #setAnimationType(AnimationType type)} is specified
     * and {@link #setInteractiveAnimation(boolean isInteractive)} is false).
     *
     * @param duration animation duration time.
     */
    public void setAnimationDuration(long duration) {
        animationDuration = duration;
    }

    /**
     * Return animation duration time in milliseconds. If custom duration is not set,
     * return default duration time {@link AbsAnimation#DEFAULT_ANIMATION_TIME}.
     */
    public long getAnimationDuration() {
        return animationDuration;
    }

    /**
     * Set animation type to perform while selecting new circle indicator.
     * Default animation type is {@link AnimationType#NONE}.
     *
     * @param type type of animation, one of {@link AnimationType}
     */
    public void setAnimationType(@Nullable AnimationType type) {
        if (type != null) {
            animationType = type;
        } else {
            animationType = AnimationType.NONE;
        }
    }

    /**
     * Set boolean value to perform interactive animation while selecting new indicator.
     *
     * @param isInteractive value of animation to be interactive or not.
     */
    public void setInteractiveAnimation(boolean isInteractive) {
        interactiveAnimation = isInteractive;
    }

    /**
     * Set progress value in range [0 - 1] to specify state of animation while selecting new circle indicator.
     * (Won't affect on anything unless {@link #setInteractiveAnimation(boolean isInteractive)} is false).
     *
     * @param selectingPosition selecting position with specific progress value.
     * @param progress          float value of progress.
     */
    public void setProgress(int selectingPosition, float progress) {
        if (interactiveAnimation) {

            if (count <= 0 || selectingPosition < 0) {
                selectingPosition = 0;

            } else if (selectingPosition > count - 1) {
                selectingPosition = count - 1;
            }

            if (progress < 0) {
                progress = 0;

            } else if (progress > 1) {
                progress = 1;
            }

            this.selectingPosition = selectingPosition;
            setAnimationProgress(progress);
        }
    }

    /**
     * Set specific circle indicator position to be selected. If position < or > total count,
     * accordingly first or last circle indicator will be selected.
     *
     * @param position position of indicator to select.
     */
    public void setSelection(int position) {
        if (position < 0) {
            position = 0;

        } else if (position > count - 1) {
            position = count - 1;
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
                startWormAnimation();
                break;

            case FILL:
                startFillAnimation();
                break;

            case SLIDE:
                startSlideAnimation();
                break;

            case THIN_WORM:
                startThinWormAnimation();
                break;

            case DROP:
                startDropAnimation();
                break;

            case SWAP:
                startSwapAnimation();
                break;
        }
    }

    /**
     * Return position of currently selected circle indicator.
     */
    public int getSelection() {
        return selectedPosition;
    }

    /**
     * Set {@link ViewPager} to add {@link ViewPager.OnPageChangeListener} and automatically
     * handle selecting new indicators (and interactive animation effect if it is enabled).
     *
     * @param pager instance of {@link ViewPager} to work with
     */
    public void setViewPager(@Nullable ViewPager pager) {
        releaseViewPager();

        if (pager == null) {
            return;
        }

        viewPager = pager;
        viewPager.addOnPageChangeListener(this);

        setDynamicCount(dynamicCount);
        int count = getViewPagerCount();

        if (isRtl()) {
            int selected = viewPager.getCurrentItem();
            this.selectedPosition = (count - 1) - selected;
        }

        setCount(count);
    }

    /**
     * Release {@link ViewPager} and stop handling events of {@link ViewPager.OnPageChangeListener}.
     */
    public void releaseViewPager() {
        if (viewPager != null) {
            viewPager.removeOnPageChangeListener(this);
            viewPager = null;
        }
    }

    /**
     * Specify to display PageIndicatorView with Right to left layout or not.
     * One of {@link RtlMode}: Off (Left to right), On (Right to left)
     * or Auto (handle this mode automatically based on users language preferences).
     * Default is Off.
     *
     * @param mode instance of {@link RtlMode}
     */
    public void setRtlMode(@Nullable RtlMode mode) {
        if (mode == null) {
            rtlMode = RtlMode.Off;
        } else {
            rtlMode = mode;
        }
    }

    private void onPageScroll(int position, float positionOffset) {
        Pair<Integer, Float> progressPair = getProgress(position, positionOffset);
        int selectingPosition = progressPair.first;
        float selectingProgress = progressPair.second;

        if (selectingProgress == 1) {
            lastSelectedPosition = selectedPosition;
            selectedPosition = selectingPosition;
        }

        setProgress(selectingPosition, selectingProgress);
    }

    private void drawIndicatorView(@NonNull Canvas canvas) {
        for (int i = 0; i < count; i++) {
            int x = getXCoordinate(i);
            int y = getYCoordinate(i);
            drawCircle(canvas, i, x, y);
        }
    }

    private void drawCircle(@NonNull Canvas canvas, int position, int x, int y) {
        boolean selectedItem = !interactiveAnimation && (position == selectedPosition || position == lastSelectedPosition);
        boolean selectingItem = interactiveAnimation && (position == selectingPosition || position == selectedPosition);
        boolean isSelectedItem = selectedItem | selectingItem;

        if (isSelectedItem)
            drawWithAnimationEffect(canvas, position, x, y);
        else
            drawWithNoEffect(canvas, position, x, y);
    }

    private void drawWithAnimationEffect(@NonNull Canvas canvas, int position, int x, int y) {
        switch (animationType) {
            case NONE:
                drawWithNoEffect(canvas, position, x, y);
                break;

            case COLOR:
                drawWithColorAnimation(canvas, position, x, y);
                break;

            case SCALE:
                drawWithScaleAnimation(canvas, position, x, y);
                break;

            case SLIDE:
                drawWithSlideAnimation(canvas, position, x, y);
                break;

            case WORM:
                drawWithWormAnimation(canvas, x, y);
                break;

            case FILL:
                drawWithFillAnimation(canvas, position, x, y);
                break;

            case THIN_WORM:
                drawWithThinWormAnimation(canvas, x, y);
                break;

            case DROP:
                drawWithDropAnimation(canvas, x, y);
                break;

            case SWAP:
                if (orientation == Orientation.HORIZONTAL)
                    drawWithSwapAnimation(canvas, position, x, y);
                else
                    drawWithSwapAnimationVertically(canvas, position, x, y);
                break;
        }
    }

    private void drawWithNoEffect(@NonNull Canvas canvas, int position, int x, int y) {
        float radius = radiusPx;
        if (animationType == AnimationType.SCALE) {
            radius *= scaleFactor;
        }

        int color = unselectedColor;
        if (position == selectedPosition) {
            color = selectedColor;
        }

        Paint paint;
        if (animationType == AnimationType.FILL) {
            paint = strokePaint;
            paint.setStrokeWidth(strokePx);
        } else {
            paint = fillPaint;
        }

        paint.setColor(color);
        canvas.drawCircle(x, y, radius, paint);
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

        fillPaint.setColor(color);
        canvas.drawCircle(x, y, radiusPx, fillPaint);
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

        fillPaint.setColor(color);
        canvas.drawCircle(x, y, radius, fillPaint);
    }

    // TODO
    private void drawWithSlideAnimation(@NonNull Canvas canvas, int position, int x, int y) {
        fillPaint.setColor(unselectedColor);
        canvas.drawCircle(x, y, radiusPx, fillPaint);

        int from = orientation == Orientation.HORIZONTAL ? frameSlideFrom : x;
        int to = orientation == Orientation.HORIZONTAL ? y : frameSlideFrom;

        if (interactiveAnimation && (position == selectingPosition || position == selectedPosition)) {
            fillPaint.setColor(selectedColor);
            canvas.drawCircle(from, to, radiusPx, fillPaint);

        } else if (!interactiveAnimation && (position == selectedPosition || position == lastSelectedPosition)) {
            fillPaint.setColor(selectedColor);
            canvas.drawCircle(from, to, radiusPx, fillPaint);
        }
    }

    private void drawWithWormAnimation(@NonNull Canvas canvas, int x, int y) {
        int radius = radiusPx;

        if (orientation == Orientation.HORIZONTAL) {
            rect.left = frameFrom;
            rect.right = frameTo;
            rect.top = y - radius;
            rect.bottom = y + radius;

        } else {
            rect.left = x - radiusPx;
            rect.right = x + radiusPx;
            rect.top = frameFrom;
            rect.bottom = frameTo;
        }

        fillPaint.setColor(unselectedColor);
        canvas.drawCircle(x, y, radius, fillPaint);

        fillPaint.setColor(selectedColor);
        canvas.drawRoundRect(rect, radiusPx, radiusPx, fillPaint);
    }

    private void drawWithFillAnimation(@NonNull Canvas canvas, int position, int x, int y) {
        int color = unselectedColor;
        float radius = radiusPx;
        int stroke = strokePx;

        if (interactiveAnimation) {
            if (position == selectingPosition) {
                color = frameColor;
                radius = frameRadiusPx;
                stroke = frameStrokePx;

            } else if (position == selectedPosition) {
                color = frameColorReverse;
                radius = frameRadiusReversePx;
                stroke = frameStrokeReversePx;
            }

        } else {
            if (position == selectedPosition) {
                color = frameColor;
                radius = frameRadiusPx;
                stroke = frameStrokePx;

            } else if (position == lastSelectedPosition) {
                color = frameColorReverse;
                radius = frameRadiusReversePx;
                stroke = frameStrokeReversePx;
            }
        }

        strokePaint.setColor(color);
        strokePaint.setStrokeWidth(strokePx);
        canvas.drawCircle(x, y, radiusPx, strokePaint);

        strokePaint.setStrokeWidth(stroke);
        canvas.drawCircle(x, y, radius, strokePaint);
    }

    private void drawWithThinWormAnimation(@NonNull Canvas canvas, int x, int y) {
        int radius = radiusPx;

        if (orientation == Orientation.HORIZONTAL) {
            rect.left = frameFrom;
            rect.right = frameTo;
            rect.top = y - (frameHeight / 2);
            rect.bottom = y + (frameHeight / 2);

        } else {
            rect.left = x - (frameHeight / 2);
            rect.right = x + (frameHeight / 2);
            rect.top = frameFrom;
            rect.bottom = frameTo;
        }

        fillPaint.setColor(unselectedColor);
        canvas.drawCircle(x, y, radius, fillPaint);

        fillPaint.setColor(selectedColor);
        canvas.drawRoundRect(rect, radiusPx, radiusPx, fillPaint);
    }

    private void drawWithDropAnimation(@NonNull Canvas canvas, int x, int y) {
        fillPaint.setColor(unselectedColor);
        canvas.drawCircle(x, y, radiusPx, fillPaint);

        fillPaint.setColor(selectedColor);
        canvas.drawCircle(frameSlideFrom, frameY, frameRadiusPx, fillPaint);
    }

    private void drawWithSwapAnimation(@NonNull Canvas canvas, int position, int x, int y) {
        fillPaint.setColor(unselectedColor);

        if (position == selectedPosition) {
            fillPaint.setColor(selectedColor);
            canvas.drawCircle(frameSlideFrom, y, radiusPx, fillPaint);

        } else if (interactiveAnimation && position == selectingPosition) {
            canvas.drawCircle(x - (frameSlideFrom - getXCoordinate(selectedPosition)), y, radiusPx, fillPaint);

        } else if (!interactiveAnimation) {
            canvas.drawCircle(x - (frameSlideFrom - getXCoordinate(selectedPosition)), y, radiusPx, fillPaint);

        } else {
            canvas.drawCircle(x, y, radiusPx, fillPaint);
        }
    }

    private void drawWithSwapAnimationVertically(@NonNull Canvas canvas, int position, int x, int y) {
        fillPaint.setColor(unselectedColor);

        if (position == selectedPosition) {
            fillPaint.setColor(selectedColor);
            canvas.drawCircle(x, frameSlideFrom, radiusPx, fillPaint);

        } else if (interactiveAnimation && position == selectingPosition) {
            canvas.drawCircle(x, y - (frameSlideFrom - getYCoordinate(selectedPosition)), radiusPx, fillPaint);

        } else if (!interactiveAnimation) {
            canvas.drawCircle(x, y - (frameSlideFrom - getYCoordinate(selectedPosition)), radiusPx, fillPaint);

        } else {
            canvas.drawCircle(x, y, radiusPx, fillPaint);
        }
    }

    private void init(@Nullable AttributeSet attrs) {
        setupId();
        initAttributes(attrs);
        initAnimation();
        updateVisibility();

        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setAntiAlias(true);

        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setAntiAlias(true);
        strokePaint.setStrokeWidth(strokePx);
    }

    private void setupId() {
        if (getId() == NO_ID) {
            setId(IdUtils.generateViewId());
        }
    }

    private void initAttributes(@Nullable AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.PageIndicatorView, 0, 0);
        initCountAttribute(typedArray);
        initColorAttribute(typedArray);
        initAnimationAttribute(typedArray);
        initSizeAttribute(typedArray);
    }

    private void initCountAttribute(@NonNull TypedArray typedArray) {
        autoVisibility = typedArray.getBoolean(R.styleable.PageIndicatorView_piv_autoVisibility, true);
        dynamicCount = typedArray.getBoolean(R.styleable.PageIndicatorView_piv_dynamicCount, false);
        count = typedArray.getInt(R.styleable.PageIndicatorView_piv_count, COUNT_NOT_SET);

        if (!isCountSet && count == COUNT_NOT_SET) {
            isCountSet = true;
            count = DEFAULT_CIRCLES_COUNT;
        }

        int position = typedArray.getInt(R.styleable.PageIndicatorView_piv_select, 0);
        if (position < 0) {
            position = 0;

        } else if (count > 0 && position > count - 1) {
            position = count - 1;
        }

        selectedPosition = position;
        selectingPosition = position;
        viewPagerId = typedArray.getResourceId(R.styleable.PageIndicatorView_piv_viewPager, 0);
    }

    private void initColorAttribute(@NonNull TypedArray typedArray) {
        unselectedColor = typedArray.getColor(R.styleable.PageIndicatorView_piv_unselectedColor, Color.parseColor(ColorAnimation.DEFAULT_UNSELECTED_COLOR));
        selectedColor = typedArray.getColor(R.styleable.PageIndicatorView_piv_selectedColor, Color.parseColor(ColorAnimation.DEFAULT_SELECTED_COLOR));
    }

    private void initAnimationAttribute(@NonNull TypedArray typedArray) {
        animationDuration = typedArray.getInt(R.styleable.PageIndicatorView_piv_animationDuration, AbsAnimation.DEFAULT_ANIMATION_TIME);
        interactiveAnimation = typedArray.getBoolean(R.styleable.PageIndicatorView_piv_interactiveAnimation, false);

        int animIndex = typedArray.getInt(R.styleable.PageIndicatorView_piv_animationType, AnimationType.NONE.ordinal());
        animationType = getAnimationType(animIndex);

        int rtlIndex = typedArray.getInt(R.styleable.PageIndicatorView_piv_rtl_mode, RtlMode.Off.ordinal());
        rtlMode = getRtlMode(rtlIndex);
    }

    private void initSizeAttribute(@NonNull TypedArray typedArray) {
        int orientationIndex = typedArray.getInt(R.styleable.PageIndicatorView_piv_orientation, Orientation.HORIZONTAL.ordinal());
        if (orientationIndex == 0) {
            orientation = Orientation.HORIZONTAL;
        } else {
            orientation = Orientation.VERTICAL;
        }

        radiusPx = (int) typedArray.getDimension(R.styleable.PageIndicatorView_piv_radius, DensityUtils.dpToPx(DEFAULT_RADIUS_DP));
        paddingPx = (int) typedArray.getDimension(R.styleable.PageIndicatorView_piv_padding, DensityUtils.dpToPx(DEFAULT_PADDING_DP));

        scaleFactor = typedArray.getFloat(R.styleable.PageIndicatorView_piv_scaleFactor, ScaleAnimation.DEFAULT_SCALE_FACTOR);
        if (scaleFactor < ScaleAnimation.MIN_SCALE_FACTOR) {
            scaleFactor = ScaleAnimation.MIN_SCALE_FACTOR;

        } else if (scaleFactor > ScaleAnimation.MAX_SCALE_FACTOR) {
            scaleFactor = ScaleAnimation.MAX_SCALE_FACTOR;
        }

        strokePx = (int) typedArray.getDimension(R.styleable.PageIndicatorView_piv_strokeWidth, DensityUtils.dpToPx(FillAnimation.DEFAULT_STROKE_DP));
        if (strokePx > radiusPx) {
            strokePx = radiusPx;
        }

        if (animationType != AnimationType.FILL) {
            strokePx = 0;
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
            public void onSlideAnimationUpdated(int value) {
                frameSlideFrom = value;
                invalidate();
            }

            @Override
            public void onWormAnimationUpdated(int leftX, int rightX) {
                // hot poin
                frameFrom = leftX;
                frameTo = rightX;
                invalidate();
            }

            @Override
            public void onThinWormAnimationUpdated(int leftX, int rightX, int height) {
                frameFrom = leftX;
                frameTo = rightX;
                frameHeight = height;
                invalidate();
            }

            @Override
            public void onFillAnimationUpdated(int color, int colorReverse, int radius, int radiusReverse, int stroke, int strokeReverse) {
                frameColor = color;
                frameColorReverse = colorReverse;

                frameRadiusPx = radius;
                frameRadiusReversePx = radiusReverse;

                frameStrokePx = stroke;
                frameStrokeReversePx = strokeReverse;
                invalidate();
            }

            @Override
            public void onDropAnimationUpdated(int x, int y, int selectedRadius) {
                frameSlideFrom = (orientation == Orientation.HORIZONTAL) ? x : y;
                frameY = (orientation == Orientation.HORIZONTAL) ? y : x;
                frameRadiusPx = selectedRadius;
                invalidate();
            }

            @Override
            public void onSwapAnimationUpdated(int xCoordinate) {
                frameSlideFrom = xCoordinate;
                invalidate();
            }
        });
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
            case 4:
                return AnimationType.SLIDE;
            case 5:
                return AnimationType.FILL;
            case 6:
                return AnimationType.THIN_WORM;
            case 7:
                return AnimationType.DROP;
            case 8:
                return AnimationType.SWAP;
            case 9:
                return AnimationType.DRAG_WORM;
        }

        return AnimationType.NONE;
    }

    private RtlMode getRtlMode(int index) {
        switch (index) {
            case 0:
                return RtlMode.On;
            case 1:
                return RtlMode.Off;
            case 2:
                return RtlMode.Auto;
        }

        return RtlMode.Auto;
    }

    private void resetFrameValues() {
        isFrameValuesSet = false;
        setupFrameValues();
    }

    private void setupFrameValues() {
        if (!isViewMeasured() || isFrameValuesSet) {
            return;
        }

        //color
        frameColor = selectedColor;
        frameColorReverse = unselectedColor;

        //scale
        frameRadiusPx = radiusPx;
        frameRadiusReversePx = radiusPx;

        //worm
        int xCoordinate = getXCoordinate(selectedPosition);
        if (xCoordinate - radiusPx >= 0) {
            frameFrom = xCoordinate - radiusPx;
            frameTo = xCoordinate + radiusPx;

        } else {
            frameFrom = xCoordinate;
            frameTo = xCoordinate + (radiusPx * 2);
        }

        //slide & drop
        frameSlideFrom = xCoordinate;
        frameY = getYCoordinate(selectedPosition);

        //fill
        frameStrokePx = radiusPx;
        frameStrokeReversePx = radiusPx / 2;

        if (animationType == AnimationType.FILL) {
            frameRadiusPx = radiusPx / 2;
            frameRadiusReversePx = radiusPx;
        }

        //thin worm
        frameHeight = radiusPx * 2;
        isFrameValuesSet = true;
    }

    private void startColorAnimation() {
        animation.color().end();
        animation.color().with(unselectedColor, selectedColor).duration(animationDuration).start();
    }

    private void startScaleAnimation() {
        animation.scale().end();
        animation.scale().with(unselectedColor, selectedColor, radiusPx, scaleFactor).duration(animationDuration).start();
    }

    private void startSlideAnimation() {
        int fromX = getCoordinate(lastSelectedPosition);
        int toX = getCoordinate(selectedPosition);

        animation.slide().end();
        animation.slide().with(fromX, toX).duration(animationDuration).start();
    }

    private void startWormAnimation() {
        int from = getCoordinate(lastSelectedPosition);
        int to = getCoordinate(selectedPosition);
        boolean isRightSide = selectedPosition > lastSelectedPosition;

        animation.worm().end();
        animation.worm().duration(animationDuration).with(from, to, radiusPx, isRightSide).start();
    }

    private void startFillAnimation() {
        animation.fill().end();
        animation.fill().with(unselectedColor, selectedColor, radiusPx, strokePx).duration(animationDuration).start();
    }

    private void startThinWormAnimation() {
        int from = getCoordinate(lastSelectedPosition);
        int to = getCoordinate(selectedPosition);
        boolean isRightSide = selectedPosition > lastSelectedPosition;

        animation.thinWorm().end();
        animation.thinWorm().duration(animationDuration).with(from, to, radiusPx, isRightSide).start();
    }

    private void startDropAnimation() {
        int from = getCoordinate(lastSelectedPosition);
        int to = getCoordinate(selectedPosition);

        int center = (orientation == Orientation.HORIZONTAL)
                ? getYCoordinate(selectedPosition)
                : getXCoordinate(selectedPosition);

        animation.drop().end();
        animation.drop().duration(animationDuration).with(from, to, center, radiusPx).start();
    }

    private void startSwapAnimation() {
        int from = getCoordinate(lastSelectedPosition);
        int to = getCoordinate(selectedPosition);

        animation.swap().end();
        animation.swap().with(from, to).duration(animationDuration).start();
    }

    @Nullable
    private AbsAnimation setAnimationProgress(float progress) {
        switch (animationType) {
            case COLOR:
                return animation.color().with(unselectedColor, selectedColor).progress(progress);

            case SCALE:
                return animation.scale().with(unselectedColor, selectedColor, radiusPx, scaleFactor).progress(progress);

            case FILL:
                return animation.fill().with(unselectedColor, selectedColor, radiusPx, strokePx).progress(progress);
            case DRAG_WORM:
            case THIN_WORM:
            case WORM:
            case SLIDE:
            case DROP:
            case SWAP:
                int from = orientation == Orientation.HORIZONTAL
                        ? getXCoordinate(selectedPosition)
                        : getYCoordinate(selectedPosition);

                int to = orientation == Orientation.HORIZONTAL
                        ? getXCoordinate(selectingPosition)
                        : getYCoordinate(selectingPosition);

                if (animationType == AnimationType.SLIDE) {
                    return animation.slide().with(from, to).progress(progress);

                } else if (animationType == AnimationType.SWAP) {
                    return animation.swap().with(from, to).progress(progress);

                } else if (animationType == AnimationType.WORM || animationType == AnimationType.THIN_WORM || animationType == AnimationType.DRAG_WORM) {
                    boolean isRightSide = selectingPosition > selectedPosition;

                    if (animationType == AnimationType.WORM) {
                        return animation.worm().with(from, to, radiusPx, isRightSide).progress(progress);

                    } else if (animationType == AnimationType.THIN_WORM) {
                        return animation.thinWorm().with(from, to, radiusPx, isRightSide).progress(progress);
                    }

                } else {
                    int center = (orientation == Orientation.HORIZONTAL)
                            ? getYCoordinate(selectedPosition)
                            : getXCoordinate(selectedPosition);

                    return animation.drop().with(from, to, center, radiusPx).progress(progress);
                }
        }

        return null;
    }

    private void registerSetObserver() {
        if (setObserver == null && viewPager != null && viewPager.getAdapter() != null) {
            setObserver = new DataSetObserver() {
                @Override
                public void onChanged() {
                    if (viewPager != null && viewPager.getAdapter() != null) {

                        int newCount = viewPager.getAdapter().getCount();
                        int currItem = viewPager.getCurrentItem();

                        selectedPosition = currItem;
                        selectingPosition = currItem;
                        lastSelectedPosition = currItem;

                        endAnimation();
                        setCount(newCount);
                        setProgress(selectingPosition, 1.0f);
                    }
                }
            };

            try {
                viewPager.getAdapter().registerDataSetObserver(setObserver);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateVisibility() {
        if (!autoVisibility) {
            return;
        }

        if (count > 1 && getVisibility() != VISIBLE) {
            setVisibility(VISIBLE);

        } else if (count <= 1 && getVisibility() != INVISIBLE) {
            setVisibility(View.INVISIBLE);
        }
    }

    private void endAnimation() {
        AbsAnimation anim = null;

        switch (animationType) {
            case COLOR:
                anim = animation.color();
                break;

            case SLIDE:
                anim = animation.slide();
                break;

            case SCALE:
                anim = animation.scale();
                break;

            case WORM:
                anim = animation.worm();
                break;

            case THIN_WORM:
                anim = animation.thinWorm();
                break;

            case FILL:
                anim = animation.fill();
                break;

            case DROP:
                anim = animation.drop();
                break;

            case SWAP:
                anim = animation.swap();
                break;
        }

        if (anim != null) {
            anim.end();
        }
    }

    private void unRegisterSetObserver() {
        if (setObserver != null && viewPager != null && viewPager.getAdapter() != null) {
            try {
                viewPager.getAdapter().unregisterDataSetObserver(setObserver);
                setObserver = null;
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    private int getViewPagerCount() {
        if (viewPager != null && viewPager.getAdapter() != null) {
            return viewPager.getAdapter().getCount();
        } else {
            return count;
        }
    }

    private void findViewPager() {
        if (viewPagerId == 0) {
            return;
        }

        Context context = getContext();
        if (context instanceof Activity) {
            Activity activity = (Activity) getContext();
            View view = activity.findViewById(viewPagerId);

            if (view != null && view instanceof ViewPager) {
                setViewPager((ViewPager) view);
            }
        }
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    private int getXCoordinate(int position) {
        if (orientation == Orientation.HORIZONTAL) {
            int x = 0;
            for (int i = 0; i < count; i++) {
                x += radiusPx + strokePx;

                if (position == i) {
                    return x;
                }

                x += radiusPx + paddingPx;
            }

            return x;

        } else {
            int x = getWidth() / 2;

            if (animationType == AnimationType.DROP) {
                x += radiusPx + strokePx;
            }

            return x;
        }
    }

    private int getYCoordinate(int position) {
        if (orientation == Orientation.HORIZONTAL) {
            int y = getHeight() / 2;

            if (animationType == AnimationType.DROP) {
                y += radiusPx;
            }

            return y;

        } else {
            int y = 0;

            for (int i = 0; i < count; i++) {
                y += radiusPx + strokePx;

                if (position == i)
                    return y;

                y += radiusPx + paddingPx;
            }

            return y;
        }
    }

    private int getCoordinate(int position) {
        return orientation == Orientation.HORIZONTAL
                ? getXCoordinate(position)
                : getYCoordinate(position);
    }

    private Pair<Integer, Float> getProgress(int position, float positionOffset) {
        if (isRtl()) {
            position = (count - 1) - position;

            if (position < 0) {
                position = 0;
            }
        }

        boolean isRightOverScrolled = position > selectedPosition;
        boolean isLeftOverScrolled;
        if (isRtl()) {
            isLeftOverScrolled = position - 1 < selectedPosition;
        } else {
            isLeftOverScrolled = position + 1 < selectedPosition;
        }

        if (isRightOverScrolled || isLeftOverScrolled) {
            selectedPosition = position;
        }

        boolean isSlideToRightSide = selectedPosition == position && positionOffset != 0;
        int selectingPosition;
        float selectingProgress;

        if (isSlideToRightSide) {
            selectingPosition = isRtl() ? position - 1 : position + 1;
            selectingProgress = positionOffset;

        } else {
            selectingPosition = position;
            selectingProgress = 1 - positionOffset;
        }

        if (selectingProgress > 1) {
            selectingProgress = 1;

        } else if (selectingProgress < 0) {
            selectingProgress = 0;
        }

        return new Pair<>(selectingPosition, selectingProgress);
    }

    private boolean isRtl() {
        switch (rtlMode) {
            case On:
                return true;

            case Off:
                return false;

            case Auto:
                return TextUtilsCompat.getLayoutDirectionFromLocale(getContext().getResources().getConfiguration().locale) == ViewCompat.LAYOUT_DIRECTION_RTL;
        }

        return false;
    }
}
