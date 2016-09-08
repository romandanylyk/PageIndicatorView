package com.rd.dotpagerview.view.animation;

import android.animation.ArgbEvaluator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.animation.DecelerateInterpolator;

public class ColorAnimation extends AbsAnimation<ValueAnimator> {

    private static final String ANIMATION_COLOR_REVERSE = "ANIMATION_COLOR_REVERSE";
    private static final String ANIMATION_COLOR = "ANIMATION_COLOR";
    private static final int ANIMATION_DURATION = 500;

    protected int startColor;
    protected int endColor;

    public ColorAnimation(@NonNull ValueAnimation.UpdateListener listener) {
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

    @Override
    public long getAnimationDuration() {
        return ANIMATION_DURATION;
    }

    @Override
    public void progress(float progress) {
        if (animator != null) {
            long playTime = (long) (progress * getAnimationDuration());
            animator.setCurrentPlayTime(playTime);
        }
    }

    @NonNull
    public ColorAnimation with(int colorStartValue, int colorEndValue) {
        if (animator != null && hasChanges(colorStartValue, colorEndValue)) {

            startColor = colorStartValue;
            endColor = colorEndValue;

            PropertyValuesHolder colorHolder = createColorPropertyHolder(false);
            PropertyValuesHolder reverseColorHolder = createColorPropertyHolder(true);

            animator.setValues(colorHolder, reverseColorHolder);
        }

        return this;
    }

    protected PropertyValuesHolder createColorPropertyHolder(boolean isReverse) {
        String propertyName;
        int startColorValue;
        int endColorValue;

        if (isReverse) {
            propertyName = ANIMATION_COLOR_REVERSE;
            startColorValue = endColor;
            endColorValue = startColor;

        } else {
            propertyName = ANIMATION_COLOR;
            startColorValue = startColor;
            endColorValue = endColor;
        }

        PropertyValuesHolder holder = PropertyValuesHolder.ofInt(propertyName, startColorValue, endColorValue);
        holder.setEvaluator(new ArgbEvaluator());

        return holder;
    }

    @SuppressWarnings("RedundantIfStatement")
    private boolean hasChanges(int colorStartValue, int colorEndValue) {
        if (startColor != colorStartValue) {
            return true;
        }

        if (endColor != colorEndValue) {
            return true;
        }

        return false;
    }

    private void onAnimateUpdated(@NonNull ValueAnimator animation) {
        int color = (int) animation.getAnimatedValue(ANIMATION_COLOR);
        int colorReverse = (int) animation.getAnimatedValue(ANIMATION_COLOR_REVERSE);

        if (listener != null) {
            listener.onColorAnimationUpdated(color, colorReverse);
        }
    }
}
