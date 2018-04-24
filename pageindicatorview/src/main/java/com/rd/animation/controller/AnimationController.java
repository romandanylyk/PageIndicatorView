package com.rd.animation.controller;

import android.support.annotation.NonNull;
import com.rd.animation.type.AnimationType;
import com.rd.animation.type.BaseAnimation;
import com.rd.draw.data.Indicator;
import com.rd.draw.data.Orientation;
import com.rd.utils.CoordinatesUtils;

public class AnimationController {

    private ValueController valueController;
    private ValueController.UpdateListener listener;

    private BaseAnimation runningAnimation;
    private Indicator indicator;

    private float progress;
    private boolean isInteractive;

    public AnimationController(@NonNull Indicator indicator, @NonNull ValueController.UpdateListener listener) {
        this.valueController = new ValueController(listener);
        this.listener = listener;
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

            case SCALE_DOWN:
                scaleDownAnimation();
                break;
        }
    }

    private void colorAnimation() {
        int selectedColor = indicator.getSelectedColor();
        int unselectedColor = indicator.getUnselectedColor();
        int foregroundSelectedColor = indicator.getSelectedForegroundColor();
        int foregroundUnselectedColor = indicator.getUnselectedForegroundColor();

        long animationDuration = indicator.getAnimationDuration();

        BaseAnimation animation = valueController
                .color()
                .with(unselectedColor, selectedColor, foregroundUnselectedColor, foregroundSelectedColor)
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
        int foregroundSelectedColor = indicator.getSelectedForegroundColor();
        int foregroundUnselectedColor = indicator.getUnselectedForegroundColor();
        int radiusPx = indicator.getRadius();
        float scaleFactor = indicator.getScaleFactor();
        long animationDuration = indicator.getAnimationDuration();

        BaseAnimation animation = valueController
                .scale()
                .with(unselectedColor, selectedColor, foregroundUnselectedColor,
                        foregroundSelectedColor, radiusPx, scaleFactor)
                .duration(animationDuration);

        if (isInteractive) {
            animation.progress(progress);
        } else {
            animation.start();
        }

        runningAnimation = animation;
    }

    private void wormAnimation() {
        int fromPosition = indicator.isInteractiveAnimation() ? indicator.getSelectedPosition() : indicator.getLastSelectedPosition();
        int toPosition = indicator.isInteractiveAnimation() ? indicator.getSelectingPosition() : indicator.getSelectedPosition();

        int from = CoordinatesUtils.getCoordinate(indicator, fromPosition);
        int to = CoordinatesUtils.getCoordinate(indicator, toPosition);
        boolean isRightSide = toPosition > fromPosition;

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

    private void slideAnimation() {
        int fromPosition = indicator.isInteractiveAnimation() ? indicator.getSelectedPosition() : indicator.getLastSelectedPosition();
        int toPosition = indicator.isInteractiveAnimation() ? indicator.getSelectingPosition() : indicator.getSelectedPosition();

        int from = CoordinatesUtils.getCoordinate(indicator, fromPosition);
        int to = CoordinatesUtils.getCoordinate(indicator, toPosition);
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
        int fromPosition = indicator.isInteractiveAnimation() ? indicator.getSelectedPosition() : indicator.getLastSelectedPosition();
        int toPosition = indicator.isInteractiveAnimation() ? indicator.getSelectingPosition() : indicator.getSelectedPosition();

        int from = CoordinatesUtils.getCoordinate(indicator, fromPosition);
        int to = CoordinatesUtils.getCoordinate(indicator, toPosition);
        boolean isRightSide = toPosition > fromPosition;

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
        int fromPosition = indicator.isInteractiveAnimation() ? indicator.getSelectedPosition() : indicator.getLastSelectedPosition();
        int toPosition = indicator.isInteractiveAnimation() ? indicator.getSelectingPosition() : indicator.getSelectedPosition();

        int widthFrom = CoordinatesUtils.getCoordinate(indicator, fromPosition);
        int widthTo = CoordinatesUtils.getCoordinate(indicator, toPosition);

        int paddingTop = indicator.getPaddingTop();
        int paddingLeft = indicator.getPaddingLeft();
        int padding = indicator.getOrientation() == Orientation.HORIZONTAL ? paddingTop : paddingLeft;

        int radius = indicator.getRadius();
        int heightFrom = radius * 3 + padding;
        int heightTo = radius + padding;

        long animationDuration = indicator.getAnimationDuration();

        BaseAnimation animation = valueController
                .drop()
                .duration(animationDuration)
                .with(widthFrom, widthTo, heightFrom, heightTo, radius);

        if (isInteractive) {
            animation.progress(progress);
        } else {
            animation.start();
        }

        runningAnimation = animation;
    }

    private void swapAnimation() {
        int fromPosition = indicator.isInteractiveAnimation() ? indicator.getSelectedPosition() : indicator.getLastSelectedPosition();
        int toPosition = indicator.isInteractiveAnimation() ? indicator.getSelectingPosition() : indicator.getSelectedPosition();

        int from = CoordinatesUtils.getCoordinate(indicator, fromPosition);
        int to = CoordinatesUtils.getCoordinate(indicator, toPosition);
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

    private void scaleDownAnimation() {
        int selectedColor = indicator.getSelectedColor();
        int unselectedColor = indicator.getUnselectedColor();
        int foregroundSelectedColor = indicator.getSelectedForegroundColor();
        int foregroundUnselectedColor = indicator.getUnselectedForegroundColor();
        int radiusPx = indicator.getRadius();
        float scaleFactor = indicator.getScaleFactor();
        long animationDuration = indicator.getAnimationDuration();

        BaseAnimation animation = valueController
                .scaleDown()
                .with(unselectedColor, selectedColor,
                        foregroundUnselectedColor, foregroundSelectedColor, radiusPx, scaleFactor)
                .duration(animationDuration);

        if (isInteractive) {
            animation.progress(progress);
        } else {
            animation.start();
        }

        runningAnimation = animation;
    }
}

