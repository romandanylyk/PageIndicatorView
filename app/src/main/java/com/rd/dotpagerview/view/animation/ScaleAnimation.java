package com.rd.dotpagerview.view.animation;

import android.animation.*;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.animation.DecelerateInterpolator;

public class ScaleAnimation {

    private static final String ANIMATION_SCALE_REVERSE = "ANIMATION_SCALE_REVERSE";
    private static final String ANIMATION_SCALE = "ANIMATION_SCALE";
    private static final int ANIMATION_DURATION = 500;
    public static final float SCALE_FACTOR = 2;

    private ValueAnimation.UpdateListener listener;
    private ValueAnimator animator;

    public ScaleAnimation(@Nullable ValueAnimation.UpdateListener listener) {
        this.listener = listener;
        animator = createValueAnimator();
    }

    @NonNull
    public ScaleAnimation with(int radiusPx) {
        if (animator != null) {
            PropertyValuesHolder scaleHolder = createScalePropertyHolder(ANIMATION_SCALE, (int) (radiusPx / SCALE_FACTOR), radiusPx);
            PropertyValuesHolder scaleReverseHolder = createScalePropertyHolder(ANIMATION_SCALE_REVERSE, radiusPx, (int) (radiusPx / SCALE_FACTOR));

            animator.setValues(scaleHolder, scaleReverseHolder);
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
        int radius = (int) animation.getAnimatedValue(ANIMATION_SCALE);
        int radiusReverse = (int) animation.getAnimatedValue(ANIMATION_SCALE_REVERSE);

        if (listener != null) {
            listener.onScaleAnimationUpdated(radius, radiusReverse);
        }
    }

    @NonNull
    protected static PropertyValuesHolder createScalePropertyHolder(@NonNull String propertyName, int fromRadiusPx, int toRadiuxPx) {
        PropertyValuesHolder holder = PropertyValuesHolder.ofInt(propertyName, fromRadiusPx, toRadiuxPx);
        holder.setEvaluator(new IntEvaluator());

        return holder;
    }
}

