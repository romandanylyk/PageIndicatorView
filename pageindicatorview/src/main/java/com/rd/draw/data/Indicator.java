package com.rd.draw.data;

import android.support.annotation.NonNull;
import com.rd.animation.type.AnimationType;

public class Indicator {

    private int height;
    private int width;

    private int radiusPx;
    private int paddingPx;

    private int strokePx; //For "Fill" animation only
    private int scaleFactor; //For "Scale" animation only

    private int unselectedColor;
    private int selectedColor;

    private boolean interactiveAnimation;
    private long animationDuration;
    private int count;

    private int selectedPosition;
    private int selectingPosition;
    private int lastSelectedPosition;

    private Orientation orientation;
    private AnimationType animationType;

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

    public int getRadiusPx() {
        return radiusPx;
    }

    public void setRadiusPx(int radiusPx) {
        this.radiusPx = radiusPx;
    }

    public int getPaddingPx() {
        return paddingPx;
    }

    public void setPaddingPx(int paddingPx) {
        this.paddingPx = paddingPx;
    }

    public int getStrokePx() {
        return strokePx;
    }

    public void setStrokePx(int strokePx) {
        this.strokePx = strokePx;
    }

    public int getScaleFactor() {
        return scaleFactor;
    }

    public void setScaleFactor(int scaleFactor) {
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
}
