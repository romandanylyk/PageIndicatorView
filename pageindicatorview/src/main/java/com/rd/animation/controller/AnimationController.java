package com.rd.animation.controller;

import android.support.annotation.NonNull;
import com.rd.animation.type.AnimationType;
import com.rd.animation.type.BaseAnimation;
import com.rd.animation.type.DropAnimation;
import com.rd.animation.type.WormAnimation;
import com.rd.draw.data.Indicator;
import com.rd.utils.CoordinatesUtils;

public class AnimationController {

    private ValueController valueController;
    private Indicator indicator;

    private float progress;
    private boolean isInteractive;

    public AnimationController(@NonNull ValueController valueController, @NonNull Indicator indicator) {
        this.valueController = valueController;
        this.indicator = indicator;
    }

    public void interactive(float progress) {
        this.isInteractive = true;
        this.progress = progress;
        animate();
    }

    public void basic() {
        this.isInteractive = false;
        this.progress = 0;
        animate();
    }

    private void animate() {
        AnimationType animationType = indicator.getAnimationType();
        switch (animationType) {
            case NONE:
                break;

            case COLOR:
                colorAnimation();
                break;

            case SCALE:
                scaleAnimation();
                break;

            case WORM:
                wormAnimation();
                break;

            case FILL:
                fillAnimation();
                break;

            case SLIDE:
                slideAnimation();
                break;

            case THIN_WORM:
                thinWormAnimation();
                break;

            case DROP:
                dropAnimation();
                break;

            case SWAP:
                swapAnimation();
                break;
        }
    }

    private void colorAnimation() {
        int selectedColor = indicator.getSelectedColor();
        int unselectedColor = indicator.getUnselectedColor();
        long animationDuration = indicator.getAnimationDuration();

        BaseAnimation animation = valueController
                .color()
                .with(unselectedColor, selectedColor)
                .duration(animationDuration);

        if (isInteractive) {
            animation.progress(progress);
        } else {
            animation.start();
        }
    }

    private void scaleAnimation() {
        int selectedColor = indicator.getSelectedColor();
        int unselectedColor = indicator.getUnselectedColor();
        int radiusPx = indicator.getRadiusPx();
        int scaleFactor = indicator.getScaleFactor();
        long animationDuration = indicator.getAnimationDuration();

        BaseAnimation animation = valueController
                .scale()
                .with(unselectedColor, selectedColor, radiusPx, scaleFactor)
                .duration(animationDuration);

        if (isInteractive) {
            animation.progress(progress);
        } else {
            animation.start();
        }
    }

    private void slideAnimation() {
        int lastSelectedPosition = indicator.getLastSelectedPosition();
        int selectedPosition = indicator.getSelectedPosition();

        int from = CoordinatesUtils.getCoordinate(indicator, lastSelectedPosition);
        int to = CoordinatesUtils.getCoordinate(indicator, selectedPosition);
        long animationDuration = indicator.getAnimationDuration();

        BaseAnimation animation = valueController
                .slide()
                .with(from, to)
                .duration(animationDuration);

        if (isInteractive) {
            animation.progress(progress);
        } else {
            animation.start();
        }
    }

    private void wormAnimation() {
        int lastSelectedPosition = indicator.getLastSelectedPosition();
        int selectedPosition = indicator.getSelectedPosition();

        int from = CoordinatesUtils.getCoordinate(indicator, lastSelectedPosition);
        int to = CoordinatesUtils.getCoordinate(indicator, selectedPosition);
        boolean isRightSide = selectedPosition > lastSelectedPosition;

        int radiusPx = indicator.getRadiusPx();
        long animationDuration = indicator.getAnimationDuration();

        WormAnimation animation = valueController
                .worm()
                .with(from, to, radiusPx, isRightSide)
                .duration(animationDuration);

        if (isInteractive) {
            animation.progress(progress);
        } else {
            animation.start();
        }
    }

    private void fillAnimation() {
        int selectedColor = indicator.getSelectedColor();
        int unselectedColor = indicator.getUnselectedColor();
        int radiusPx = indicator.getRadiusPx();
        int strokePx = indicator.getStrokePx();
        long animationDuration = indicator.getAnimationDuration();

        BaseAnimation animation = valueController
                .fill()
                .with(unselectedColor, selectedColor, radiusPx, strokePx)
                .duration(animationDuration);

        if (isInteractive) {
            animation.progress(progress);
        } else {
            animation.start();
        }
    }

    private void thinWormAnimation() {
        int lastSelectedPosition = indicator.getLastSelectedPosition();
        int selectedPosition = indicator.getSelectedPosition();

        int from = CoordinatesUtils.getCoordinate(indicator, lastSelectedPosition);
        int to = CoordinatesUtils.getCoordinate(indicator, selectedPosition);
        boolean isRightSide = selectedPosition > lastSelectedPosition;

        int radiusPx = indicator.getRadiusPx();
        long animationDuration = indicator.getAnimationDuration();

        WormAnimation animation = valueController
                .thinWorm()
                .with(from, to, radiusPx, isRightSide)
                .duration(animationDuration);

        if (isInteractive) {
            animation.progress(progress);
        } else {
            animation.start();
        }
    }

    private void dropAnimation() {
        int lastSelectedPosition = indicator.getLastSelectedPosition();
        int selectedPosition = indicator.getSelectedPosition();

        int from = CoordinatesUtils.getCoordinate(indicator, lastSelectedPosition);
        int to = CoordinatesUtils.getCoordinate(indicator, selectedPosition);

        long animationDuration = indicator.getAnimationDuration();
        int radiusPx = indicator.getRadiusPx();

        DropAnimation animation = valueController
                .drop()
                .duration(animationDuration)
                .with(from, to, to, radiusPx);

        if (isInteractive) {
            animation.progress(progress);
        } else {
            animation.start();
        }
    }

    private void swapAnimation() {
        int lastSelectedPosition = indicator.getLastSelectedPosition();
        int selectedPosition = indicator.getSelectedPosition();

        int from = CoordinatesUtils.getCoordinate(indicator, lastSelectedPosition);
        int to = CoordinatesUtils.getCoordinate(indicator, selectedPosition);
        long animationDuration = indicator.getAnimationDuration();

        BaseAnimation animation = valueController
                .swap()
                .with(from, to)
                .duration(animationDuration);

        if (isInteractive) {
            animation.progress(progress);
        } else {
            animation.start();
        }
    }
}

