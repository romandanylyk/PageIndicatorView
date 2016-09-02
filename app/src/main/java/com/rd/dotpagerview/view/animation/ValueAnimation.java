package com.rd.dotpagerview.view.animation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class ValueAnimation {

    private ColorAnimation colorAnimation;
    private ScaleAnimation scaleAnimation;
    private ColorAndScaleAnimation colorAndScaleAnimation;
    private SlideAnimation slideAnimation;

    private UpdateListener updateListener;

    public interface UpdateListener {

        void onColorAnimationUpdated(int color, int colorReverse);

        void onScaleAnimationUpdated(int radiusPx, int radiusReverse);
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
    public ColorAndScaleAnimation colorAndScale() {
        if (colorAndScaleAnimation == null) {
            colorAndScaleAnimation = new ColorAndScaleAnimation();
        }

        return colorAndScaleAnimation;
    }

    @NonNull
    public SlideAnimation slide() {
        if (slideAnimation == null) {
            slideAnimation = new SlideAnimation();
        }

        return slideAnimation;
    }
}
