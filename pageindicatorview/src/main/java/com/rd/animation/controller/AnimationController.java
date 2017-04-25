package com.rd.animation.controller;

import android.support.annotation.NonNull;
import com.rd.animation.type.AnimationType;
import com.rd.animation.type.BaseAnimation;
import com.rd.draw.data.Indicator;
import com.rd.utils.CoordinatesUtils;

public class AnimationController {

    private ValueController valueController;
    private ValueController.UpdateListener listener;

    private BaseAnimation runningAnimation;
    private Indicator indicator;

    private float progress;
    private boolean isInteractive;
    private int lastSelectingPosition;

    public AnimationController(@NonNull Indicator indicator, @NonNull ValueController.UpdateListener listener) {
        this.valueController = new ValueController(listener);
        this.listener = listener;
        this.indicator = indicator;
    }

    public void interactive(float progress) {
        this.isInteractive = true;
        this.progress = progress;
        int selectingPosition = indicator.getSelectingPosition();

//        if (lastSelectingPosition != selectingPosition && progress > 0 && progress < 1) {
//            end();
//        }
//
//        lastSelectingPosition = selectingPosition;
        animate();
    }

    public void basic() {
        this.isInteractive = false;
        this.progress = 0;
        animate();
    }

    public void end() {
        if (runningAnimation != null) {
            runningAnimation.end();
        }
    }

    private void animate() {
        AnimationType animationType = indicator.getAnimationType();
        switch (animationType) {
            case NONE:
                listener.onValueUpdated(null);
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

        runningAnimation = animation;
    }

    private void scaleAnimation() {
        int selectedColor = indicator.getSelectedColor();
        int unselectedColor = indicator.getUnselectedColor();
        int radiusPx = indicator.getRadius();
        float scaleFactor = indicator.getScaleFactor();
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

        runningAnimation = animation;
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

        runningAnimation = animation;
    }

    private void wormAnimation() {
        int lastSelectedPosition = indicator.getLastSelectedPosition();
        int selectedPosition = indicator.getSelectedPosition();

        int from = CoordinatesUtils.getCoordinate(indicator, lastSelectedPosition);
        int to = CoordinatesUtils.getCoordinate(indicator, selectedPosition);
        boolean isRightSide = selectedPosition > lastSelectedPosition;

        int radiusPx = indicator.getRadius();
        long animationDuration = indicator.getAnimationDuration();

        BaseAnimation animation = valueController
                .worm()
                .with(from, to, radiusPx, isRightSide)
                .duration(animationDuration);

        if (isInteractive) {
            animation.progress(progress);
        } else {
            animation.start();
        }

        runningAnimation = animation;
    }

    private void fillAnimation() {
        int selectedColor = indicator.getSelectedColor();
        int unselectedColor = indicator.getUnselectedColor();
        int radiusPx = indicator.getRadius();
        int strokePx = indicator.getStroke();
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

        runningAnimation = animation;
    }

    private void thinWormAnimation() {
        int lastSelectedPosition = indicator.getLastSelectedPosition();
        int selectedPosition = indicator.getSelectedPosition();

        int from = CoordinatesUtils.getCoordinate(indicator, lastSelectedPosition);
        int to = CoordinatesUtils.getCoordinate(indicator, selectedPosition);
        boolean isRightSide = selectedPosition > lastSelectedPosition;

        int radiusPx = indicator.getRadius();
        long animationDuration = indicator.getAnimationDuration();

        BaseAnimation animation = valueController
                .thinWorm()
                .with(from, to, radiusPx, isRightSide)
                .duration(animationDuration);

        if (isInteractive) {
            animation.progress(progress);
        } else {
            animation.start();
        }

        runningAnimation = animation;
    }

    private void dropAnimation() {
        int lastSelectedPosition = indicator.getLastSelectedPosition();
        int selectedPosition = indicator.getSelectedPosition();

        int from = CoordinatesUtils.getCoordinate(indicator, lastSelectedPosition);
        int to = CoordinatesUtils.getCoordinate(indicator, selectedPosition);

        long animationDuration = indicator.getAnimationDuration();
        int radiusPx = indicator.getRadius();

        BaseAnimation animation = valueController
                .drop()
                .duration(animationDuration)
                .with(from, to, to, radiusPx);

        if (isInteractive) {
            animation.progress(progress);
        } else {
            animation.start();
        }

        runningAnimation = animation;
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

        runningAnimation = animation;
    }
}

