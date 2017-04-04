package com.rd.animation.controller;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.rd.animation.type.*;

public class ValueAnimation {

    private ColorAnimation colorAnimation;
    private ScaleAnimation scaleAnimation;
    private WormAnimation wormAnimation;
    private SlideAnimation slideAnimation;
    private FillAnimation fillAnimation;
    private ThinWormAnimation thinWormAnimation;
    private DropAnimation dropAnimation;
    private SwapAnimation swapAnimation;

    private UpdateListener updateListener;

    public interface UpdateListener {
        //Prefix "fr" stands for "frame", that mean that value will frequently update, for each frame

        void onColorAnimationUpdated(int frColor, int frColorReverse);

        void onScaleAnimationUpdated(int frColor, int frColorReverse, int frRadius, int frRadiusReverse);

        void onSlideAnimationUpdated(int frX);

        void onWormAnimationUpdated(int frLeftX, int frRightX);

        void onFillAnimationUpdated(int frColor, int frColorReverse, int frRadius, int frRadiusReverse, int frStroke, int frStrokeReverse);

        void onThinWormAnimationUpdated(int frLeftX, int frRightX, int frHeight);

        void onDropAnimationUpdated(int frX, int frY, int frRadius);

        void onSwapAnimationUpdated(int frX);
    }

    public ValueAnimation(@Nullable UpdateListener listener) {
        updateListener = listener;
    }

    @NonNull
    public ColorAnimation color() {
        if (colorAnimation == null) {
            colorAnimation = new ColorAnimation(updateListener);
        }

        return colorAnimation;
    }

    @NonNull
    public ScaleAnimation scale() {
        if (scaleAnimation == null) {
            scaleAnimation = new ScaleAnimation(updateListener);
        }

        return scaleAnimation;
    }

    @NonNull
    public WormAnimation worm() {
        if (wormAnimation == null) {
            wormAnimation = new WormAnimation(updateListener);
        }

        return wormAnimation;
    }

    @NonNull
    public SlideAnimation slide() {
        if (slideAnimation == null) {
            slideAnimation = new SlideAnimation(updateListener);
        }

        return slideAnimation;
    }

    @NonNull
    public FillAnimation fill() {
        if (fillAnimation == null) {
            fillAnimation = new FillAnimation(updateListener);
        }

        return fillAnimation;
    }

    @NonNull
    public ThinWormAnimation thinWorm() {
        if (thinWormAnimation == null) {
            thinWormAnimation = new ThinWormAnimation(updateListener);
        }

        return thinWormAnimation;
    }

    @NonNull
    public DropAnimation drop() {
        if (dropAnimation == null) {
            dropAnimation = new DropAnimation(updateListener);
        }

        return dropAnimation;
    }

    @NonNull
    public SwapAnimation swap() {
        if (swapAnimation == null) {
            swapAnimation = new SwapAnimation(updateListener);
        }

        return swapAnimation;
    }

}
