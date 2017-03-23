package com.rd.animation;

import android.animation.IntEvaluator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.view.animation.DecelerateInterpolator;

public class SlideAnimation extends AbsAnimation<ValueAnimator> {

    private static final String ANIMATION_X_COORDINATE = "ANIMATION_COORDINATE";
    private static final int COORDINATE_NONE = -1;

    private int fromCoordinate = COORDINATE_NONE;
    private int toCoordinate = COORDINATE_NONE;

    public SlideAnimation(@NonNull ValueAnimation.UpdateListener listener) {
        super(listener);
    }

    @NonNull
    @Override
    public ValueAnimator createAnimator() {
        ValueAnimator animator = new ValueAnimator();
        animator.setDuration(AbsAnimation.DEFAULT_ANIMATION_TIME);
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
    public SlideAnimation progress(float progress) {
        if (animator != null) {
            long playTime = (long) (progress * animationDuration);

            if (animator.getValues() != null && animator.getValues().length > 0) {
                animator.setCurrentPlayTime(playTime);
            }
        }

        return this;
    }

    @NonNull
    public SlideAnimation with(int startValue, int endValue) {
        if (animator != null && hasChanges(startValue, endValue)) {

            fromCoordinate = startValue;
            toCoordinate = endValue;

            PropertyValuesHolder holder = createSlidePropertyHolder();
            animator.setValues(holder);
        }

        return this;
    }

    private PropertyValuesHolder createSlidePropertyHolder() {
        PropertyValuesHolder holder = PropertyValuesHolder.ofInt(ANIMATION_X_COORDINATE, fromCoordinate, toCoordinate);
        holder.setEvaluator(new IntEvaluator());

        return holder;
    }

    private void onAnimateUpdated(@NonNull ValueAnimator animation) {
        int value = (int) animation.getAnimatedValue(ANIMATION_X_COORDINATE);

        if (listener != null) {
            listener.onSlideAnimationUpdated(value);
        }
    }

    @SuppressWarnings("RedundantIfStatement")
    private boolean hasChanges(int startValue, int endValue) {
        if (fromCoordinate != startValue) {
            return true;
        }

        if (toCoordinate != endValue) {
            return true;
        }

        return false;
    }
}
