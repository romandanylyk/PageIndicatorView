package com.rd.animation.type;

import android.animation.IntEvaluator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import androidx.annotation.NonNull;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.rd.animation.controller.ValueController;
import com.rd.animation.data.type.SlideAnimationValue;

public class SlideAnimation extends BaseAnimation<ValueAnimator> {

    private static final String ANIMATION_COORDINATE = "ANIMATION_COORDINATE";
    private static final int COORDINATE_NONE = -1;

    private SlideAnimationValue value;
    private int coordinateStart = COORDINATE_NONE;
    private int coordinateEnd = COORDINATE_NONE;

    public SlideAnimation(@NonNull ValueController.UpdateListener listener) {
        super(listener);
        value = new SlideAnimationValue();
    }

    @NonNull
    @Override
    public ValueAnimator createAnimator() {
        ValueAnimator animator = new ValueAnimator();
        animator.setDuration(BaseAnimation.DEFAULT_ANIMATION_TIME);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
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
    public SlideAnimation with(int coordinateStart, int coordinateEnd) {
        if (animator != null && hasChanges(coordinateStart, coordinateEnd)) {

            this.coordinateStart = coordinateStart;
            this.coordinateEnd = coordinateEnd;

            PropertyValuesHolder holder = createSlidePropertyHolder();
            animator.setValues(holder);
        }

        return this;
    }

    private PropertyValuesHolder createSlidePropertyHolder() {
        PropertyValuesHolder holder = PropertyValuesHolder.ofInt(ANIMATION_COORDINATE, coordinateStart, coordinateEnd);
        holder.setEvaluator(new IntEvaluator());

        return holder;
    }

    private void onAnimateUpdated(@NonNull ValueAnimator animation) {
        int coordinate = (int) animation.getAnimatedValue(ANIMATION_COORDINATE);
        value.setCoordinate(coordinate);

        if (listener != null) {
            listener.onValueUpdated(value);
        }
    }

    @SuppressWarnings("RedundantIfStatement")
    private boolean hasChanges(int coordinateStart, int coordinateEnd) {
        if (this.coordinateStart != coordinateStart) {
            return true;
        }

        if (this.coordinateEnd != coordinateEnd) {
            return true;
        }

        return false;
    }
}
