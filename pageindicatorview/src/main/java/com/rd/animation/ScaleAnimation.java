package com.rd.animation;

import android.animation.IntEvaluator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.view.animation.DecelerateInterpolator;

public class ScaleAnimation extends ColorAnimation {

    public static final float DEFAULT_SCALE_FACTOR = 1.7f;
    public static final float MIN_SCALE_FACTOR = 1;
    public static final float MAX_SCALE_FACTOR = 3;

    private static final String ANIMATION_COLOR_REVERSE = "ANIMATION_COLOR_REVERSE";
    private static final String ANIMATION_COLOR = "ANIMATION_COLOR";

    private static final String ANIMATION_SCALE_REVERSE = "ANIMATION_SCALE_REVERSE";
    private static final String ANIMATION_SCALE = "ANIMATION_SCALE";

    private static final int ANIMATION_DURATION = 350;

    private int radiusPx;
    private float scaleFactor;

    public ScaleAnimation(@NonNull ValueAnimation.UpdateListener listener) {
        super(listener);
    }

    @NonNull
    @Override
    public ValueAnimator createAnimator() {
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
    public ScaleAnimation with(int colorStartValue, int colorEndValue, int radiusValue, float scaleFactorValue) {
        if (animator != null && hasChanges(colorStartValue, colorEndValue, radiusValue, scaleFactorValue)) {

            startColor = colorStartValue;
            endColor = colorEndValue;
            radiusPx = radiusValue;
            scaleFactor = scaleFactorValue;

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
            endRadiusValue = (int) (radiusPx / scaleFactor);
        } else {
            propertyName = ANIMATION_SCALE;
            startRadiusValue = (int) (radiusPx / scaleFactor);
            endRadiusValue = radiusPx;
        }

        PropertyValuesHolder holder = PropertyValuesHolder.ofInt(propertyName, startRadiusValue, endRadiusValue);
        holder.setEvaluator(new IntEvaluator());

        return holder;
    }

    @SuppressWarnings("RedundantIfStatement")
    private boolean hasChanges(int colorStartValue, int colorEndValue, int radiusValue, float scaleFactorValue) {
        if (startColor != colorStartValue) {
            return true;
        }

        if (endColor != colorEndValue) {
            return true;
        }

        if (radiusPx != radiusValue) {
            return true;
        }

        if (scaleFactor != scaleFactorValue) {
            return true;
        }

        return false;
    }
}

