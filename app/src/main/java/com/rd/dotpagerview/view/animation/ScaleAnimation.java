package com.rd.dotpagerview.view.animation;

import android.animation.*;
import android.support.annotation.NonNull;
import android.view.animation.DecelerateInterpolator;

public class ScaleAnimation extends ColorAnimation {

    private static final String ANIMATION_COLOR_REVERSE = "ANIMATION_COLOR_REVERSE";
    private static final String ANIMATION_COLOR = "ANIMATION_COLOR";

    private static final String ANIMATION_SCALE_REVERSE = "ANIMATION_SCALE_REVERSE";
    private static final String ANIMATION_SCALE = "ANIMATION_SCALE";

    private static final int ANIMATION_DURATION = 500;
    public static final float SCALE_FACTOR = 2;

    private int radiusPx;

    public ScaleAnimation(@NonNull ValueAnimation.UpdateListener listener) {
        super(listener);
    }

    @NonNull
    @Override
    public ValueAnimator createValueAnimator() {
        ValueAnimator animator = new ValueAnimator();
        animator.setDuration(ANIMATION_DURATION);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                onAnimateUpdated(animation);
            }
        });

        return animator;
    }

    @NonNull
    public ScaleAnimation with(int colorStartValue, int colorEndValue, int radiusValue) {
        if (animator != null && hasChanges(colorStartValue, colorEndValue, radiusValue)) {

            startColor = colorStartValue;
            endColor = colorEndValue;
            radiusPx = radiusValue;

            PropertyValuesHolder colorHolder = createColorPropertyHolder(false);
            PropertyValuesHolder reverseColorHolder = createColorPropertyHolder(true);

            PropertyValuesHolder scaleHolder = createScalePropertyHolder(false);
            PropertyValuesHolder scaleReverseHolder = createScalePropertyHolder(true);

            animator.setValues(colorHolder, reverseColorHolder, scaleHolder, scaleReverseHolder);
        }

        return this;
    }

    private void onAnimateUpdated(@NonNull ValueAnimator animation) {
        int color = (int) animation.getAnimatedValue(ANIMATION_COLOR);
        int colorReverse = (int) animation.getAnimatedValue(ANIMATION_COLOR_REVERSE);

        int radius = (int) animation.getAnimatedValue(ANIMATION_SCALE);
        int radiusReverse = (int) animation.getAnimatedValue(ANIMATION_SCALE_REVERSE);

        if (listener != null) {
            listener.onScaleAnimationUpdated(color, colorReverse, radius, radiusReverse);
        }
    }

    @NonNull
    private PropertyValuesHolder createScalePropertyHolder(boolean isReverse) {
        String propertyName;
        int startRadiusValue;
        int endRadiusValue;

        if (isReverse) {
            propertyName = ANIMATION_SCALE_REVERSE;
            startRadiusValue = radiusPx;
            endRadiusValue = (int) (radiusPx / SCALE_FACTOR);
        } else {
            propertyName = ANIMATION_SCALE;
            startRadiusValue = (int) (radiusPx / SCALE_FACTOR);
            endRadiusValue = radiusPx;
        }

        PropertyValuesHolder holder = PropertyValuesHolder.ofInt(propertyName, startRadiusValue, endRadiusValue);
        holder.setEvaluator(new IntEvaluator());

        return holder;
    }

    @SuppressWarnings("RedundantIfStatement")
    private boolean hasChanges(int colorStartValue, int colorEndValue, int radiusValue) {
        if (startColor != colorStartValue) {
            return true;
        }

        if (endColor != colorEndValue) {
            return true;
        }

        if (radiusPx != radiusValue) {
            return true;
        }

        return false;
    }
}

