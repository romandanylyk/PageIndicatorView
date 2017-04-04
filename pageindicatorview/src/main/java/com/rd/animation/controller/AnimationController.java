package com.rd.animation.controller;

import com.rd.DrawManager;
import com.rd.animation.data.AnimationType;
import com.rd.data.Indicator;

public class AnimationController implements ValueAnimation.UpdateListener {

    private DrawManager drawManager;
    private AnimationType animationType;
    private ValueAnimation animation;

    private int frColor;
    private int frColorReverse;

    private int frHeight;
    private int frRadius;
    private int frRadiusReverse;

    private int frStroke;
    private int frStrokeReverse;

    private int frX;
    private int frY;

    private int frLeftX;
    private int frRightX;

    AnimationController() {
        drawManager = new DrawManager();
        animationType = AnimationType.NONE;
        animation = new ValueAnimation(this);
    }

    @Override
    public void onColorAnimationUpdated(int frColor, int frColorReverse) {
        this.frColor = frColor;
        this.frColorReverse = frColorReverse;
    }

    @Override
    public void onScaleAnimationUpdated(int frColor, int frColorReverse, int frRadius, int frRadiusReverse) {
        this.frColor = frColor;
        this.frColorReverse = frColorReverse;

        this.frRadius = frRadius;
        this.frColorReverse = frRadiusReverse;
    }

    @Override
    public void onSlideAnimationUpdated(int frX) {
        this.frX = frX;
    }

    @Override
    public void onWormAnimationUpdated(int frLeftX, int frRightX) {
        this.frLeftX = frLeftX;
        this.frRightX = frRightX;
    }

    @Override
    public void onFillAnimationUpdated(int frColor, int frColorReverse, int frRadius, int frRadiusReverse, int frStroke, int frStrokeReverse) {
        this.frColor = frColor;
        this.frColorReverse = frColorReverse;

        this.frRadius = frRadius;
        this.frRadiusReverse = frRadiusReverse;

        this.frStroke = frStroke;
        this.frStrokeReverse = frStrokeReverse;
    }

    @Override
    public void onThinWormAnimationUpdated(int frLeftX, int frRightX, int frHeight) {
        this.frLeftX = frLeftX;
        this.frRightX = frRightX;
        this.frHeight = frHeight;
    }

    @Override
    public void onDropAnimationUpdated(int frX, int frY, int frRadius) {
        this.frX = frX;
        this.frY = frY;
        this.frRadius = frRadius;
    }

    @Override
    public void onSwapAnimationUpdated(int frX) {
        this.frX = frX;
    }

    void start() {
        if (animationType == null) {
            startCurrentAnimation();
        }
    }

    void end() {

    }

    private void startCurrentAnimation() {
        switch (animationType) {
            case NONE:
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

    private void startColorAnimation() {
        Indicator indicator = drawManager.getIndicator();
        int selectedColor = indicator.getSelectedColor();
        int unselectedColor = indicator.getUnselectedColor();
        long animationDuration = drawManager.getAnimationDuration();

        animation.color()
                .with(unselectedColor, selectedColor)
                .duration(animationDuration)
                .start();
    }

    private void startScaleAnimation() {
        Indicator indicator = drawManager.getIndicator();
        int selectedColor = indicator.getSelectedColor();
        int unselectedColor = indicator.getUnselectedColor();
        int radiusPx = indicator.getRadiusPx();
        int scaleFactor = indicator.getScaleFactor();
        long animationDuration = drawManager.getAnimationDuration();

        animation.scale()
                .with(unselectedColor, selectedColor, radiusPx, scaleFactor)
                .duration(animationDuration)
                .start();
    }

    private void startSlideAnimation() {
//        int fromX = getCoordinate(lastSelectedPosition);
//        int toX = getCoordinate(selectedPosition);
//
//        animation.slide().end();
//        animation.slide().with(fromX, toX).duration(animationDuration).start();
    }

    private void startWormAnimation() {
//        int from = getCoordinate(lastSelectedPosition);
//        int to = getCoordinate(selectedPosition);
//        boolean isRightSide = selectedPosition > lastSelectedPosition;
//
//        animation.worm().end();
//        animation.worm().duration(animationDuration).with(from, to, radiusPx, isRightSide).start();
    }

    private void startFillAnimation() {
//        animation.fill().end();
//        animation.fill().with(unselectedColor, selectedColor, radiusPx, strokePx).duration(animationDuration).start();
    }

    private void startThinWormAnimation() {
//        int from = getCoordinate(lastSelectedPosition);
//        int to = getCoordinate(selectedPosition);
//        boolean isRightSide = selectedPosition > lastSelectedPosition;
//
//        animation.thinWorm().end();
//        animation.thinWorm().duration(animationDuration).with(from, to, radiusPx, isRightSide).start();
    }

    private void startDropAnimation() {
//        int from = getCoordinate(lastSelectedPosition);
//        int to = getCoordinate(selectedPosition);
//
//        int center = (orientation == Orientation.HORIZONTAL)
//                ? getYCoordinate(selectedPosition)
//                : getXCoordinate(selectedPosition);
//
//        animation.drop().end();
//        animation.drop().duration(animationDuration).with(from, to, center, radiusPx).start();
    }

    private void startSwapAnimation() {
//        int from = getCoordinate(lastSelectedPosition);
//        int to = getCoordinate(selectedPosition);
//
//        animation.swap().end();
//        animation.swap().with(from, to).duration(animationDuration).start();
    }
}
