package com.rd.draw.data;

import android.support.annotation.NonNull;
import android.view.View;
import com.rd.animation.type.AnimationType;

public class Indicator {

    public static final int DEFAULT_COUNT = 3;
    public static final int MIN_COUNT = 1;
    public static final int COUNT_NONE = -1;

    public static final int DEFAULT_RADIUS_DP = 6;
    public static final int DEFAULT_PADDING_DP = 8;
    public static final int DEFAULT_FOREGROUND_PADDING_DP = 0;

    private int height;
    private int width;
    private int radius;

    private int padding;
    private int paddingLeft;
    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;

    private int foregroundPadding;
    private int stroke; //For "Fill" animation only
    private float scaleFactor; //For "Scale" animation only

    private int unselectedColor;
    private int selectedColor;

    private int unselectedForegroundColor;
    private int selectedForegroundColor;

    private boolean interactiveAnimation;
    private boolean autoVisibility;
    private boolean dynamicCount;
    private boolean hasForeground;

    private long animationDuration;
    private int count = DEFAULT_COUNT;

    private int selectedPosition;
    private int selectingPosition;
    private int lastSelectedPosition;

    private int viewPagerId = View.NO_ID;

    private Orientation orientation;
    private AnimationType animationType;
    private RtlMode rtlMode;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public int getPaddingLeft() {
        return paddingLeft;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public int getPaddingTop() {
        return paddingTop;
    }

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }

    public int getPaddingRight() {
        return paddingRight;
    }

    public void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
    }

    public int getPaddingBottom() {
        return paddingBottom;
    }

    public void setPaddingBottom(int paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    public int getStroke() {
        return stroke;
    }

    public void setStroke(int stroke) {
        this.stroke = stroke;
    }

    public float getScaleFactor() {
        return scaleFactor;
    }

    public void setScaleFactor(float scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    public int getUnselectedColor() {
        return unselectedColor;
    }

    public void setUnselectedColor(int unselectedColor) {
        this.unselectedColor = unselectedColor;
    }

    public int getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(int selectedColor) {
        this.selectedColor = selectedColor;
    }

    public boolean isInteractiveAnimation() {
        return interactiveAnimation;
    }

    public void setInteractiveAnimation(boolean interactiveAnimation) {
        this.interactiveAnimation = interactiveAnimation;
    }

    public boolean isAutoVisibility() {
        return autoVisibility;
    }

    public void setAutoVisibility(boolean autoVisibility) {
        this.autoVisibility = autoVisibility;
    }

    public boolean isDynamicCount() {
        return dynamicCount;
    }

    public void setDynamicCount(boolean dynamicCount) {
        this.dynamicCount = dynamicCount;
    }

    public long getAnimationDuration() {
        return animationDuration;
    }

    public void setAnimationDuration(long animationDuration) {
        this.animationDuration = animationDuration;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public int getSelectingPosition() {
        return selectingPosition;
    }

    public void setSelectingPosition(int selectingPosition) {
        this.selectingPosition = selectingPosition;
    }

    public int getLastSelectedPosition() {
        return lastSelectedPosition;
    }

    public void setLastSelectedPosition(int lastSelectedPosition) {
        this.lastSelectedPosition = lastSelectedPosition;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @NonNull
    public Orientation getOrientation() {
        if (orientation == null) {
            orientation = Orientation.HORIZONTAL;
        }
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    @NonNull
    public AnimationType getAnimationType() {
        if (animationType == null) {
            animationType = AnimationType.NONE;
        }
        return animationType;
    }

    public void setAnimationType(AnimationType animationType) {
        this.animationType = animationType;
    }

    @NonNull
    public RtlMode getRtlMode() {
        if (rtlMode == null) {
            rtlMode = RtlMode.Off;
        }
        return rtlMode;
    }

    public void setRtlMode(RtlMode rtlMode) {
        this.rtlMode = rtlMode;
    }

    public int getViewPagerId() {
        return viewPagerId;
    }

    public void setViewPagerId(int viewPagerId) {
        this.viewPagerId = viewPagerId;
    }

    public boolean isHasForeground() {
        return hasForeground;
    }

    public void setHasForeground(boolean hasForeground) {
        this.hasForeground = hasForeground;
    }

    public int getUnselectedForegroundColor() {
        return unselectedForegroundColor;
    }

    public void setUnselectedForegroundColor(int unselectedForegroundColor) {
        this.unselectedForegroundColor = unselectedForegroundColor;
    }

    public int getSelectedForegroundColor() {
        return selectedForegroundColor;
    }

    public void setSelectedForegroundColor(int selectedForegroundColor) {
        this.selectedForegroundColor = selectedForegroundColor;
    }

    public int getForegroundPadding() {
        return foregroundPadding;
    }

    public void setForegroundPadding(int foregroundPadding) {
        this.foregroundPadding = foregroundPadding;
    }
}
