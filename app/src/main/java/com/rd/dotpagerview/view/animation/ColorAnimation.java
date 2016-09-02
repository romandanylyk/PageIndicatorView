package com.rd.dotpagerview.view.animation;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.animation.DecelerateInterpolator;

public class ColorAnimation {

    private static final String ANIMATION_COLOR_REVERSE = "ANIMATION_COLOR_REVERSE";
    private static final String ANIMATION_COLOR = "ANIMATION_COLOR";
    private static final int ANIMATION_DURATION = 500;

    private ValueAnimation.UpdateListener listener;
    private ValueAnimator animator;

    public ColorAnimation(@Nullable ValueAnimation.UpdateListener listener) {
        this.listener = listener;
        animator = createValueAnimator();
    }

    @NonNull
    public ColorAnimation with(int colorStart, int colorEnd) {
        if (animator != null) {
            PropertyValuesHolder colorHolder = createColorPropertyHolder(ANIMATION_COLOR, colorStart, colorEnd);
            PropertyValuesHolder reverseColorHolder = createColorPropertyHolder(ANIMATION_COLOR_REVERSE, colorEnd, colorStart);

            animator.setValues(colorHolder, reverseColorHolder);
        }

        return this;
    }

    public void start() {
        if (animator != null) {
            animator.start();
        }
    }

    public void end() {
        if (animator != null) {
            animator.end();
        }
    }

    public void progress(float progress) {
        if (animator != null) {
            long playTime = (long) (progress * ANIMATION_DURATION);
            animator.setCurrentPlayTime(playTime);
        }
    }

    private ValueAnimator createValueAnimator() {
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

    private void onAnimateUpdated(@NonNull ValueAnimator animation) {
        int color = (int) animation.getAnimatedValue(ANIMATION_COLOR);
        int colorReverse = (int) animation.getAnimatedValue(ANIMATION_COLOR_REVERSE);

        if (listener != null) {
            listener.onColorAnimationUpdated(color, colorReverse);
        }
    }

    protected static PropertyValuesHolder createColorPropertyHolder(@NonNull String propertyName, int selectedColor, int unSelectedColor) {
        PropertyValuesHolder holder = PropertyValuesHolder.ofInt(propertyName, unSelectedColor, selectedColor);
        holder.setEvaluator(new ArgbEvaluator());

        return holder;
    }
}
