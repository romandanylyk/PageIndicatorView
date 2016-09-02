package com.rd.dotpagerview.view.animation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class ValueAnimation {

    private ColorAnimation colorAnimation;
    private ScaleAnimation scaleAnimation;
    private SlideAnimation slideAnimation;

    private UpdateListener updateListener;

    public interface UpdateListener {

        void onColorAnimationUpdated(int color, int colorReverse);

        void onScaleAnimationUpdated(int color, int colorReverse, int radius, int radiusReverse);

        void onSlideAnimationUpdated(int leftX, int rightX);
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
    public SlideAnimation slide() {
        if (slideAnimation == null) {
            slideAnimation = new SlideAnimation(updateListener);
        }

        return slideAnimation;
    }
}
