package com.rd;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.text.TextUtilsCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import com.rd.animation.type.*;
import com.rd.draw.controller.DrawController;
import com.rd.draw.data.Indicator;
import com.rd.draw.data.Orientation;
import com.rd.draw.data.PositionSavedState;
import com.rd.draw.data.RtlMode;
import com.rd.utils.CoordinatesUtils;
import com.rd.utils.DensityUtils;
import com.rd.utils.IdUtils;

public class PageIndicatorView extends View implements ViewPager.OnPageChangeListener, IndicatorManager.Listener, ViewPager.OnAdapterChangeListener, View.OnTouchListener {

    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    private IndicatorManager manager;
    private DataSetObserver setObserver;
    private ViewPager viewPager;
    private boolean isInteractionEnabled;

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
        findViewPager(getParent());
    }

    @Override
    protected void onDetachedFromWindow() {
        unRegisterSetObserver();
        super.onDetachedFromWindow();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Indicator indicator = manager.indicator();
        PositionSavedState positionSavedState = new PositionSavedState(super.onSaveInstanceState());
        positionSavedState.setSelectedPosition(indicator.getSelectedPosition());
        positionSavedState.setSelectingPosition(indicator.getSelectingPosition());
        positionSavedState.setLastSelectedPosition(indicator.getLastSelectedPosition());

        return positionSavedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof PositionSavedState) {
            Indicator indicator = manager.indicator();
            PositionSavedState positionSavedState = (PositionSavedState) state;
            indicator.setSelectedPosition(positionSavedState.getSelectedPosition());
            indicator.setSelectingPosition(positionSavedState.getSelectingPosition());
            indicator.setLastSelectedPosition(positionSavedState.getLastSelectedPosition());
            super.onRestoreInstanceState(positionSavedState.getSuperState());

        } else {
            super.onRestoreInstanceState(state);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Pair<Integer, Integer> pair = manager.drawer().measureViewSize(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(pair.first, pair.second);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        manager.drawer().draw(canvas);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        manager.drawer().touch(event);
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (!manager.indicator().isFadeOnIdle()) return false;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                stopIdleRunnable();
                break;

            case MotionEvent.ACTION_UP:
                startIdleRunnable();
                break;
        }
        return false;
    }

    @Override
    public void onIndicatorUpdated() {
        invalidate();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        onPageScroll(position, positionOffset);
    }

    @Override
    public void onPageSelected(int position) {
        onPageSelect(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            manager.indicator().setInteractiveAnimation(isInteractionEnabled);
        }
    }

    @Override
    public void onAdapterChanged(@NonNull ViewPager viewPager, @Nullable PagerAdapter oldAdapter, @Nullable PagerAdapter newAdapter) {
        if (manager.indicator().isDynamicCount()) {
            if (oldAdapter != null && setObserver != null) {
                oldAdapter.unregisterDataSetObserver(setObserver);
                setObserver = null;
            }
            registerSetObserver();
        }
        updateState();
    }

    /**
     * Set static number of circle indicators to be displayed.
     *
     * @param count total count of indicators.
     */
    public void setCount(int count) {
        if (count >= 0 && manager.indicator().getCount() != count) {
            manager.indicator().setCount(count);
            updateVisibility();
            requestLayout();
        }
    }

    /**
     * Return number of circle indicators
     */
    public int getCount() {
        return manager.indicator().getCount();
    }

    /**
     * Dynamic count will automatically update number of circle indicators
     * if {@link ViewPager} page count updates on run-time. If new count will be bigger than current count,
     * selected circle will stay as it is, otherwise it will be set to last one.
     * Note: works if {@link ViewPager} set and already have it's adapter. See {@link #setViewPager(ViewPager)}.
     *
     * @param dynamicCount boolean value to add/remove indicators dynamically.
     */
    public void setDynamicCount(boolean dynamicCount) {
        manager.indicator().setDynamicCount(dynamicCount);

        if (dynamicCount) {
            registerSetObserver();
        } else {
            unRegisterSetObserver();
        }
    }

    /**
     * Fade on idle will make {@link PageIndicatorView} {@link View#INVISIBLE} if {@link ViewPager} is not interacted
     * in time equal to {@link Indicator#idleDuration}. Take care when setting {@link PageIndicatorView} alpha
     * manually if this is true. Alpha is used to manage fading and appearance of {@link PageIndicatorView} and value you provide
     * will be overridden when {@link PageIndicatorView} enters or leaves idle state.
     *
     * @param fadeOnIdle boolean value to hide {@link PageIndicatorView} when {@link ViewPager} is idle
     */
    public void setFadeOnIdle(boolean fadeOnIdle) {
        manager.indicator().setFadeOnIdle(fadeOnIdle);
        if (fadeOnIdle) {
            startIdleRunnable();
        } else {
            stopIdleRunnable();
        }
    }

    /**
     * Set radius in dp of each circle indicator. Default value is {@link Indicator#DEFAULT_RADIUS_DP}.
     * Note: make sure you set circle Radius, not a Diameter.
     *
     * @param radiusDp radius of circle in dp.
     */
    public void setRadius(int radiusDp) {
        if (radiusDp < 0) {
            radiusDp = 0;
        }

        int radiusPx = DensityUtils.dpToPx(radiusDp);
        manager.indicator().setRadius(radiusPx);
        invalidate();
    }

    /**
     * Set radius in px of each circle indicator. Default value is {@link Indicator#DEFAULT_RADIUS_DP}.
     * Note: make sure you set circle Radius, not a Diameter.
     *
     * @param radiusPx radius of circle in px.
     */
    public void setRadius(float radiusPx) {
        if (radiusPx < 0) {
            radiusPx = 0;
        }

        manager.indicator().setRadius((int) radiusPx);
        invalidate();
    }

    /**
     * Return radius of each circle indicators in px. If custom radius is not set, return
     * default value {@link Indicator#DEFAULT_RADIUS_DP}.
     */
    public int getRadius() {
        return manager.indicator().getRadius();
    }

    /**
     * Set padding in dp between each circle indicator. Default value is {@link Indicator#DEFAULT_PADDING_DP}.
     *
     * @param paddingDp padding between circles in dp.
     */
    public void setPadding(int paddingDp) {
        if (paddingDp < 0) {
            paddingDp = 0;
        }

        int paddingPx = DensityUtils.dpToPx(paddingDp);
        manager.indicator().setPadding(paddingPx);
        invalidate();
    }

    /**
     * Set padding in px between each circle indicator. Default value is {@link Indicator#DEFAULT_PADDING_DP}.
     *
     * @param paddingPx padding between circles in px.
     */
    public void setPadding(float paddingPx) {
        if (paddingPx < 0) {
            paddingPx = 0;
        }

        manager.indicator().setPadding((int) paddingPx);
        invalidate();
    }

    /**
     * Return padding in px between each circle indicator. If custom padding is not set,
     * return default value {@link Indicator#DEFAULT_PADDING_DP}.
     */
    public int getPadding() {
        return manager.indicator().getPadding();
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

        manager.indicator().setScaleFactor(factor);
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
        return manager.indicator().getScaleFactor();
    }

    /**
     * Set stroke width in px to set while {@link AnimationType#FILL} is selected.
     * Default value is {@link FillAnimation#DEFAULT_STROKE_DP}
     *
     * @param strokePx stroke width in px.
     */
    public void setStrokeWidth(float strokePx) {
        int radiusPx = manager.indicator().getRadius();

        if (strokePx < 0) {
            strokePx = 0;

        } else if (strokePx > radiusPx) {
            strokePx = radiusPx;
        }

        manager.indicator().setStroke((int) strokePx);
        invalidate();
    }

    /**
     * Set stroke width in dp to set while {@link AnimationType#FILL} is selected.
     * Default value is {@link FillAnimation#DEFAULT_STROKE_DP}
     *
     * @param strokeDp stroke width in dp.
     */

    public void setStrokeWidth(int strokeDp) {
        int strokePx = DensityUtils.dpToPx(strokeDp);
        int radiusPx = manager.indicator().getRadius();

        if (strokePx < 0) {
            strokePx = 0;

        } else if (strokePx > radiusPx) {
            strokePx = radiusPx;
        }

        manager.indicator().setStroke(strokePx);
        invalidate();
    }

    /**
     * Return stroke width in px if {@link AnimationType#FILL} is selected, 0 otherwise.
     */
    public int getStrokeWidth() {
        return manager.indicator().getStroke();
    }

    /**
     * Set color of selected state to circle indicator. Default color is {@link ColorAnimation#DEFAULT_SELECTED_COLOR}.
     *
     * @param color color selected circle.
     */
    public void setSelectedColor(int color) {
        manager.indicator().setSelectedColor(color);
        invalidate();
    }

    /**
     * Return color of selected circle indicator. If custom unselected color
     * is not set, return default color {@link ColorAnimation#DEFAULT_SELECTED_COLOR}.
     */
    public int getSelectedColor() {
        return manager.indicator().getSelectedColor();
    }

    /**
     * Set color of unselected state to each circle indicator. Default color {@link ColorAnimation#DEFAULT_UNSELECTED_COLOR}.
     *
     * @param color color of each unselected circle.
     */
    public void setUnselectedColor(int color) {
        manager.indicator().setUnselectedColor(color);
        invalidate();
    }

    /**
     * Return color of unselected state of each circle indicator. If custom unselected color
     * is not set, return default color {@link ColorAnimation#DEFAULT_UNSELECTED_COLOR}.
     */
    public int getUnselectedColor() {
        return manager.indicator().getUnselectedColor();
    }

    /**
     * Automatically hide (View.INVISIBLE) PageIndicatorView while indicator count is <= 1.
     * Default is true.
     *
     * @param autoVisibility auto hide indicators.
     */
    public void setAutoVisibility(boolean autoVisibility) {
        if (!autoVisibility) {
            setVisibility(VISIBLE);
        }

        manager.indicator().setAutoVisibility(autoVisibility);
        updateVisibility();
    }

    /**
     * Set orientation for indicator, one of HORIZONTAL or VERTICAL.
     * Default is HORIZONTAL.
     *
     * @param orientation an orientation to display page indicators.
     */
    public void setOrientation(@Nullable Orientation orientation) {
        if (orientation != null) {
            manager.indicator().setOrientation(orientation);
            requestLayout();
        }
    }

    /**
     * Set animation duration time in millisecond. Default animation duration time is {@link BaseAnimation#DEFAULT_ANIMATION_TIME}.
     * (Won't affect on anything unless {@link #setAnimationType(AnimationType type)} is specified
     * and {@link #setInteractiveAnimation(boolean isInteractive)} is false).
     *
     * @param duration animation duration time.
     */
    public void setAnimationDuration(long duration) {
        manager.indicator().setAnimationDuration(duration);
    }

    /**
     * Sets time in millis after which {@link ViewPager} is considered idle.
     * If {@link Indicator#fadeOnIdle} is true, {@link PageIndicatorView} will
     * fade away after entering idle state and appear when it is left.
     *
     * @param duration time in millis after which {@link ViewPager} is considered idle
     */
    public void setIdleDuration(long duration) {
        manager.indicator().setIdleDuration(duration);
        if (manager.indicator().isFadeOnIdle()) {
            startIdleRunnable();
        } else {
            stopIdleRunnable();
        }
    }

    /**
     * Return animation duration time in milliseconds. If custom duration is not set,
     * return default duration time {@link BaseAnimation#DEFAULT_ANIMATION_TIME}.
     */
    public long getAnimationDuration() {
        return manager.indicator().getAnimationDuration();
    }

    /**
     * Set animation type to perform while selecting new circle indicator.
     * Default animation type is {@link AnimationType#NONE}.
     *
     * @param type type of animation, one of {@link AnimationType}
     */
    public void setAnimationType(@Nullable AnimationType type) {
        manager.onValueUpdated(null);

        if (type != null) {
            manager.indicator().setAnimationType(type);
        } else {
            manager.indicator().setAnimationType(AnimationType.NONE);
        }
        invalidate();
    }

    /**
     * Interactive animation will animate indicator smoothly
     * from position to position based on user's current swipe progress.
     * (Won't affect on anything unless {@link #setViewPager(ViewPager)} is specified).
     *
     * @param isInteractive value of animation to be interactive or not.
     */
    public void setInteractiveAnimation(boolean isInteractive) {
        manager.indicator().setInteractiveAnimation(isInteractive);
        this.isInteractionEnabled = isInteractive;
    }

    /**
     * Set {@link ViewPager} to add {@link ViewPager.OnPageChangeListener} and automatically
     * handle selecting new indicators (and interactive animation effect if it is enabled).
     *
     * @param pager instance of {@link ViewPager} to work with
     */
    @SuppressLint("ClickableViewAccessibility")
    public void setViewPager(@Nullable ViewPager pager) {
        releaseViewPager();
        if (pager == null) {
            return;
        }

        viewPager = pager;
        viewPager.addOnPageChangeListener(this);
        viewPager.addOnAdapterChangeListener(this);
        viewPager.setOnTouchListener(this);
        manager.indicator().setViewPagerId(viewPager.getId());

        setDynamicCount(manager.indicator().isDynamicCount());
        updateState();
    }

    /**
     * Release {@link ViewPager} and stop handling events of {@link ViewPager.OnPageChangeListener}.
     */
    public void releaseViewPager() {
        if (viewPager != null) {
            viewPager.removeOnPageChangeListener(this);
            viewPager.removeOnAdapterChangeListener(this);
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
        Indicator indicator = manager.indicator();
        if (mode == null) {
            indicator.setRtlMode(RtlMode.Off);
        } else {
            indicator.setRtlMode(mode);
        }

        if (viewPager == null) {
            return;
        }

        int selectedPosition = indicator.getSelectedPosition();
        int position = selectedPosition;

        if (isRtl()) {
            position = (indicator.getCount() - 1) - selectedPosition;

        } else if (viewPager != null) {
            position = viewPager.getCurrentItem();
        }

        indicator.setLastSelectedPosition(position);
        indicator.setSelectingPosition(position);
        indicator.setSelectedPosition(position);
        invalidate();
    }

    /**
     * Return position of currently selected circle indicator.
     */
    public int getSelection() {
        return manager.indicator().getSelectedPosition();
    }

    /**
     * Set specific circle indicator position to be selected. If position < or > total count,
     * accordingly first or last circle indicator will be selected.
     *
     * @param position position of indicator to select.
     */
    public void setSelection(int position) {
        Indicator indicator = manager.indicator();
        position = adjustPosition(position);

        if (position == indicator.getSelectedPosition() || position == indicator.getSelectingPosition()) {
            return;
        }

        indicator.setInteractiveAnimation(false);
        indicator.setLastSelectedPosition(indicator.getSelectedPosition());
        indicator.setSelectingPosition(position);
        indicator.setSelectedPosition(position);
        manager.animate().basic();
    }

    /**
     * Set specific circle indicator position to be selected without any kind of animation. If position < or > total count,
     * accordingly first or last circle indicator will be selected.
     *
     * @param position position of indicator to select.
     */
    public void setSelected(int position) {
        Indicator indicator = manager.indicator();
        AnimationType animationType = indicator.getAnimationType();
        indicator.setAnimationType(AnimationType.NONE);

        setSelection(position);
        indicator.setAnimationType(animationType);
    }

    /**
     * Clears selection of all indicators
     */
    public void clearSelection() {
        //TODO check
        Indicator indicator = manager.indicator();
        indicator.setInteractiveAnimation(false);
        indicator.setLastSelectedPosition(Indicator.COUNT_NONE);
        indicator.setSelectingPosition(Indicator.COUNT_NONE);
        indicator.setSelectedPosition(Indicator.COUNT_NONE);
        manager.animate().basic();
    }

    /**
     * Set progress value in range [0 - 1] to specify state of animation while selecting new circle indicator.
     *
     * @param selectingPosition selecting position with specific progress value.
     * @param progress          float value of progress.
     */
    public void setProgress(int selectingPosition, float progress) {
        Indicator indicator = manager.indicator();
        if (!indicator.isInteractiveAnimation()) {
            return;
        }

        int count = indicator.getCount();
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

        if (progress == 1) {
            indicator.setLastSelectedPosition(indicator.getSelectedPosition());
            indicator.setSelectedPosition(selectingPosition);
        }

        indicator.setSelectingPosition(selectingPosition);
        manager.animate().interactive(progress);
    }

    public void setClickListener(@Nullable DrawController.ClickListener listener) {
        manager.drawer().setClickListener(listener);
    }

    private void init(@Nullable AttributeSet attrs) {
        setupId();
        initIndicatorManager(attrs);

        if (manager.indicator().isFadeOnIdle()) {
            startIdleRunnable();
        }
    }

    private void setupId() {
        if (getId() == NO_ID) {
            setId(IdUtils.generateViewId());
        }
    }

    private void initIndicatorManager(@Nullable AttributeSet attrs) {
        manager = new IndicatorManager(this);
        manager.drawer().initAttributes(getContext(), attrs);

        Indicator indicator = manager.indicator();
        indicator.setPaddingLeft(getPaddingLeft());
        indicator.setPaddingTop(getPaddingTop());
        indicator.setPaddingRight(getPaddingRight());
        indicator.setPaddingBottom(getPaddingBottom());
        isInteractionEnabled = indicator.isInteractiveAnimation();
    }

    private void registerSetObserver() {
        if (setObserver != null || viewPager == null || viewPager.getAdapter() == null) {
            return;
        }

        setObserver = new DataSetObserver() {
            @Override
            public void onChanged() {
                updateState();
            }
        };

        try {
            viewPager.getAdapter().registerDataSetObserver(setObserver);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private void unRegisterSetObserver() {
        if (setObserver == null || viewPager == null || viewPager.getAdapter() == null) {
            return;
        }

        try {
            viewPager.getAdapter().unregisterDataSetObserver(setObserver);
            setObserver = null;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private void updateState() {
        if (viewPager == null || viewPager.getAdapter() == null) {
            return;
        }

        int count = viewPager.getAdapter().getCount();
        int selectedPos = isRtl() ? (count - 1) - viewPager.getCurrentItem() : viewPager.getCurrentItem();

        manager.indicator().setSelectedPosition(selectedPos);
        manager.indicator().setSelectingPosition(selectedPos);
        manager.indicator().setLastSelectedPosition(selectedPos);
        manager.indicator().setCount(count);
        manager.animate().end();

        updateVisibility();
        requestLayout();
    }

    private void updateVisibility() {
        if (!manager.indicator().isAutoVisibility()) {
            return;
        }

        int count = manager.indicator().getCount();
        int visibility = getVisibility();

        if (visibility != VISIBLE && count > Indicator.MIN_COUNT) {
            setVisibility(VISIBLE);

        } else if (visibility != INVISIBLE && count <= Indicator.MIN_COUNT) {
            setVisibility(View.INVISIBLE);
        }
    }

    private void onPageSelect(int position) {
        Indicator indicator = manager.indicator();
        boolean canSelectIndicator = isViewMeasured();
        int count = indicator.getCount();

        if (canSelectIndicator) {
            if (isRtl()) {
                position = (count - 1) - position;
            }

            setSelection(position);
        }
    }

    private void onPageScroll(int position, float positionOffset) {
        Indicator indicator = manager.indicator();
        AnimationType animationType = indicator.getAnimationType();
        boolean interactiveAnimation = indicator.isInteractiveAnimation();
        boolean canSelectIndicator = isViewMeasured() && interactiveAnimation && animationType != AnimationType.NONE;

        if (!canSelectIndicator) {
            return;
        }

        Pair<Integer, Float> progressPair = CoordinatesUtils.getProgress(indicator, position, positionOffset, isRtl());
        int selectingPosition = progressPair.first;
        float selectingProgress = progressPair.second;
        setProgress(selectingPosition, selectingProgress);
    }

    private boolean isRtl() {
        switch (manager.indicator().getRtlMode()) {
            case On:
                return true;

            case Off:
                return false;

            case Auto:
                return TextUtilsCompat.getLayoutDirectionFromLocale(getContext().getResources().getConfiguration().locale) == ViewCompat.LAYOUT_DIRECTION_RTL;
        }

        return false;
    }

    private boolean isViewMeasured() {
        return getMeasuredHeight() != 0 || getMeasuredWidth() != 0;
    }

    private void findViewPager(@Nullable ViewParent viewParent) {
        boolean isValidParent = viewParent != null &&
                viewParent instanceof ViewGroup &&
                ((ViewGroup) viewParent).getChildCount() > 0;

        if (!isValidParent) {
            return;
        }

        int viewPagerId = manager.indicator().getViewPagerId();
        ViewPager viewPager = findViewPager((ViewGroup) viewParent, viewPagerId);

        if (viewPager != null) {
            setViewPager(viewPager);
        } else {
            findViewPager(viewParent.getParent());
        }
    }

    @Nullable
    private ViewPager findViewPager(@NonNull ViewGroup viewGroup, int id) {
        if (viewGroup.getChildCount() <= 0) {
            return null;
        }

        View view = viewGroup.findViewById(id);
        if (view != null && view instanceof ViewPager) {
            return (ViewPager) view;
        } else {
            return null;
        }
    }

    private int adjustPosition(int position) {
        Indicator indicator = manager.indicator();
        int count = indicator.getCount();
        int lastPosition = count - 1;

        if (position < 0) {
            position = 0;

        } else if (position > lastPosition) {
            position = lastPosition;
        }

        return position;
    }

    private void displayWithAnimation() {
        animate().cancel();
        animate().alpha(1.0f).setDuration(Indicator.IDLE_ANIMATION_DURATION);
    }

    private void hideWithAnimation() {
        animate().cancel();
        animate().alpha(0f).setDuration(Indicator.IDLE_ANIMATION_DURATION);
    }

    private void startIdleRunnable() {
        HANDLER.removeCallbacks(idleRunnable);
        HANDLER.postDelayed(idleRunnable, manager.indicator().getIdleDuration());
    }

    private void stopIdleRunnable() {
        HANDLER.removeCallbacks(idleRunnable);
        displayWithAnimation();
    }

    private Runnable idleRunnable = new Runnable() {
        @Override
        public void run() {
            manager.indicator().setIdle(true);
            hideWithAnimation();
        }
    };
}
