package com.rd.dotpagerview.view.animation;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.view.animation.DecelerateInterpolator;

public class ColorAndScaleAnimation {

    private static final String ANIMATION_COLOR_REVERSE = "ANIMATION_COLOR_REVERSE";
    private static final String ANIMATION_COLOR = "ANIMATION_COLOR";

    private static final String ANIMATION_SCALE_REVERSE = "ANIMATION_SCALE_REVERSE";
    private static final String ANIMATION_SCALE = "ANIMATION_SCALE";

    private static final int ANIMATION_DURATION = 500;
    public static final float SCALE_FACTOR = 2;

    public interface Listener {
        void onScaleAnimationUpdated(int color, int colorReverse, int radius, int radiusReverse);
    }

    @SuppressWarnings("AccessStaticViaInstance")
    public static void start(int selectedColor, int unSelectedColor, int radiusPx, @NonNull final Listener listener) {
        PropertyValuesHolder colorHolder = ColorAnimation.createColorPropertyHolder(ANIMATION_COLOR, selectedColor, unSelectedColor);
        PropertyValuesHolder reverseColorHolder = ColorAnimation.createColorPropertyHolder(ANIMATION_COLOR_REVERSE, unSelectedColor, selectedColor);

        PropertyValuesHolder scaleHolder = ScaleAnimation.createScalePropertyHolder(ANIMATION_SCALE, (int) (radiusPx / SCALE_FACTOR), radiusPx);
        PropertyValuesHolder scaleReverseHolder = ScaleAnimation.createScalePropertyHolder(ANIMATION_SCALE_REVERSE, radiusPx, (int) (radiusPx / SCALE_FACTOR));

        ValueAnimator animator = ObjectAnimator.ofPropertyValuesHolder(colorHolder, reverseColorHolder, scaleHolder, scaleReverseHolder);
        animator.setDuration(ANIMATION_DURATION);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int color = (int) animation.getAnimatedValue(ANIMATION_COLOR);
                int colorReverse = (int) animation.getAnimatedValue(ANIMATION_COLOR_REVERSE);

                int radius = (int) animation.getAnimatedValue(ANIMATION_SCALE);
                int radiusReverse = (int) animation.getAnimatedValue(ANIMATION_SCALE_REVERSE);

                listener.onScaleAnimationUpdated(color, colorReverse, radius, radiusReverse);
            }
        });

        animator.start();
    }
}
