package com.rd.animation.type;

import android.animation.IntEvaluator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.rd.animation.controller.ValueController;
import com.rd.animation.data.type.FillAnimationValue;

public class FillAnimation extends ColorAnimation {

    private static final String ANIMATION_RADIUS_REVERSE = "ANIMATION_RADIUS_REVERSE";
    private static final String ANIMATION_RADIUS = "ANIMATION_RADIUS";

    private static final String ANIMATION_STROKE_REVERSE = "ANIMATION_STROKE_REVERSE";
    private static final String ANIMATION_STROKE = "ANIMATION_STROKE";

    public static final int DEFAULT_STROKE_DP = 1;
    private FillAnimationValue value;

    private int radius;
    private int stroke;

    public FillAnimation(@NonNull ValueController.UpdateListener listener) {
        super(listener);
        value = new FillAnimationValue();
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

    @NonNull
    public FillAnimation with(int colorStart, int colorEnd, int radius, int stroke) {
        if (animator != null && hasChanges(colorStart, colorEnd, radius, stroke)) {

            this.colorStart = colorStart;
            this.colorEnd = colorEnd;

            this.radius = radius;
            this.stroke = stroke;

            PropertyValuesHolder colorHolder = createColorPropertyHolder(false, false);
            PropertyValuesHolder reverseColorHolder = createColorPropertyHolder(true, false);

            PropertyValuesHolder radiusHolder = createRadiusPropertyHolder(false);
            PropertyValuesHolder radiusReverseHolder = createRadiusPropertyHolder(true);

            PropertyValuesHolder strokeHolder = createStrokePropertyHolder(false);
            PropertyValuesHolder strokeReverseHolder = createStrokePropertyHolder(true);

            animator.setValues(
                    colorHolder,
                    reverseColorHolder,

                    radiusHolder,
                    radiusReverseHolder,

                    strokeHolder,
                    strokeReverseHolder);
        }

        return this;
    }

    @NonNull
    private PropertyValuesHolder createRadiusPropertyHolder(boolean isReverse) {
        String propertyName;
        int startRadiusValue;
        int endRadiusValue;

        if (isReverse) {
            propertyName = ANIMATION_RADIUS_REVERSE;
            startRadiusValue = radius / 2;
            endRadiusValue = radius;
        } else {
            propertyName = ANIMATION_RADIUS;
            startRadiusValue = radius;
            endRadiusValue = radius / 2;
        }

        PropertyValuesHolder holder = PropertyValuesHolder.ofInt(propertyName, startRadiusValue, endRadiusValue);
        holder.setEvaluator(new IntEvaluator());

        return holder;
    }

    @NonNull
    private PropertyValuesHolder createStrokePropertyHolder(boolean isReverse) {
        String propertyName;
        int startStrokeValue;
        int endStrokeValue;

        if (isReverse) {
            propertyName = ANIMATION_STROKE_REVERSE;
            startStrokeValue = radius;
            endStrokeValue = 0;
        } else {
            propertyName = ANIMATION_STROKE;
            startStrokeValue = 0;
            endStrokeValue = radius;
        }

        PropertyValuesHolder holder = PropertyValuesHolder.ofInt(propertyName, startStrokeValue, endStrokeValue);
        holder.setEvaluator(new IntEvaluator());

        return holder;
    }

    private void onAnimateUpdated(@NonNull ValueAnimator animation) {
        int color = (int) animation.getAnimatedValue(ANIMATION_COLOR);
        int colorReverse = (int) animation.getAnimatedValue(ANIMATION_COLOR_REVERSE);

        int radius = (int) animation.getAnimatedValue(ANIMATION_RADIUS);
        int radiusReverse = (int) animation.getAnimatedValue(ANIMATION_RADIUS_REVERSE);

        int stroke = (int) animation.getAnimatedValue(ANIMATION_STROKE);
        int strokeReverse = (int) animation.getAnimatedValue(ANIMATION_STROKE_REVERSE);

        value.setColor(color);
        value.setColorReverse(colorReverse);

        value.setRadius(radius);
        value.setRadiusReverse(radiusReverse);

        value.setStroke(stroke);
        value.setStrokeReverse(strokeReverse);

        if (listener != null) {
            listener.onValueUpdated(value);
        }
    }

    @SuppressWarnings("RedundantIfStatement")
    private boolean hasChanges(int colorStart, int colorEnd, int radiusValue, int strokeValue) {
        if (this.colorStart != colorStart) {
            return true;
        }

        if (this.colorEnd != colorEnd) {
            return true;
        }

        if (radius != radiusValue) {
            return true;
        }

        if (stroke != strokeValue) {
            return true;
        }

        return false;
    }
}
