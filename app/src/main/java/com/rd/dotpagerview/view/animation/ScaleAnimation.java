package com.rd.dotpagerview.view.animation;

import android.animation.*;
import android.support.annotation.NonNull;
import android.view.animation.DecelerateInterpolator;

public class ScaleAnimation {

    private static final String ANIMATION_SCALE_REVERSE = "ANIMATION_SCALE_REVERSE";
    private static final String ANIMATION_SCALE = "ANIMATION_SCALE";
    private static final int ANIMATION_DURATION = 500;
    public static final float SCALE_FACTOR = 2;

    public interface Listener {
        void onScaleAnimationUpdated(int radius, int radiusReverse);
    }

    @SuppressWarnings("AccessStaticViaInstance")
    public static void start(int radiusPx, @NonNull final Listener listener) {
        PropertyValuesHolder scaleHolder = createScalePropertyHolder(ANIMATION_SCALE, (int) (radiusPx / SCALE_FACTOR), radiusPx);
        PropertyValuesHolder scaleReverseHolder = createScalePropertyHolder(ANIMATION_SCALE_REVERSE, radiusPx, (int) (radiusPx / SCALE_FACTOR));

        ValueAnimator animator = ObjectAnimator.ofPropertyValuesHolder(scaleHolder, scaleReverseHolder);
        animator.setDuration(ANIMATION_DURATION);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int radius = (int) animation.getAnimatedValue(ANIMATION_SCALE);
                int radiusReverse = (int) animation.getAnimatedValue(ANIMATION_SCALE_REVERSE);
                listener.onScaleAnimationUpdated(radius, radiusReverse);
            }
        });

        animator.start();
    }

    protected static PropertyValuesHolder createScalePropertyHolder(@NonNull String propertyName, int fromRadiusPx, int toRadiuxPx) {
        PropertyValuesHolder holder = PropertyValuesHolder.ofInt(propertyName, fromRadiusPx, toRadiuxPx);
        holder.setEvaluator(new IntEvaluator());

        return holder;
    }
}
