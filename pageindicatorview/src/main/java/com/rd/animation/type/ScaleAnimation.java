package com.rd.animation.type;

import android.animation.IntEvaluator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.rd.animation.controller.ValueController;
import com.rd.animation.data.type.ScaleAnimationValue;

public class ScaleAnimation extends ColorAnimation {

    public static final float DEFAULT_SCALE_FACTOR = 0.7f;
    public static final float MIN_SCALE_FACTOR = 0.3f;
    public static final float MAX_SCALE_FACTOR = 1;

    static final String ANIMATION_SCALE_REVERSE = "ANIMATION_SCALE_REVERSE";
    static final String ANIMATION_SCALE = "ANIMATION_SCALE";

    int radius;
    float scaleFactor;

    private ScaleAnimationValue value;

    public ScaleAnimation(@NonNull ValueController.UpdateListener listener) {
        super(listener);
        value = new ScaleAnimationValue();
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
    public ScaleAnimation with(int colorStart, int colorEnd,
                               int foregroundColorStart, int foregroundColorEnd, int radius, float scaleFactor) {
        if (animator != null && hasChanges(colorStart, colorEnd, foregroundColorStart, foregroundColorEnd, radius, scaleFactor)) {

            this.colorStart = colorStart;
            this.colorEnd = colorEnd;
            this.foregroundColorStart = foregroundColorStart;
            this.foregroundColorEnd = foregroundColorEnd;

            this.radius = radius;
            this.scaleFactor = scaleFactor;

            PropertyValuesHolder colorHolder = createColorPropertyHolder(false, false);
            PropertyValuesHolder reverseColorHolder = createColorPropertyHolder(true, false);
            PropertyValuesHolder foregroundColorHolder = createColorPropertyHolder(false, true);
            PropertyValuesHolder foregroundReverseColorHolder = createColorPropertyHolder(true, true);


            PropertyValuesHolder scaleHolder = createScalePropertyHolder(false);
            PropertyValuesHolder scaleReverseHolder = createScalePropertyHolder(true);

            animator.setValues(colorHolder, reverseColorHolder, foregroundColorHolder, foregroundReverseColorHolder, scaleHolder, scaleReverseHolder);
        }

        return this;
    }

    private void onAnimateUpdated(@NonNull ValueAnimator animation) {
        int color = (int) animation.getAnimatedValue(ANIMATION_COLOR);
        int colorReverse = (int) animation.getAnimatedValue(ANIMATION_COLOR_REVERSE);

        int foregroundColor = (int) animation.getAnimatedValue(ANIMATION_FOREGROUND_COLOR);
        int foregroundColorReverse = (int) animation.getAnimatedValue(ANIMATION_FOREGROUND_COLOR_REVERSE);

        int radius = (int) animation.getAnimatedValue(ANIMATION_SCALE);
        int radiusReverse = (int) animation.getAnimatedValue(ANIMATION_SCALE_REVERSE);

        value.setColor(color);
        value.setColorReverse(colorReverse);

        value.setForegroundColor(foregroundColor);
        value.setForegroundColorReverse(foregroundColorReverse);

        value.setRadius(radius);
        value.setRadiusReverse(radiusReverse);

        if (listener != null) {
            listener.onValueUpdated(value);
        }
    }

    @NonNull
    protected PropertyValuesHolder createScalePropertyHolder(boolean isReverse) {
        String propertyName;
        int startRadiusValue;
        int endRadiusValue;

        if (isReverse) {
            propertyName = ANIMATION_SCALE_REVERSE;
            startRadiusValue = radius;
            endRadiusValue = (int) (radius * scaleFactor);
        } else {
            propertyName = ANIMATION_SCALE;
            startRadiusValue = (int) (radius * scaleFactor);
            endRadiusValue = radius;
        }

        PropertyValuesHolder holder = PropertyValuesHolder.ofInt(propertyName, startRadiusValue, endRadiusValue);
        holder.setEvaluator(new IntEvaluator());

        return holder;
    }

    @SuppressWarnings("RedundantIfStatement")
    private boolean hasChanges(int colorStart, int colorEnd, int foregroundColorStart, int foregroundColorEnd, int radiusValue, float scaleFactorValue) {
        if (this.colorStart != colorStart) {
            return true;
        }

        if (this.colorEnd != colorEnd) {
            return true;
        }

        if (this.foregroundColorStart != foregroundColorStart) {
            return true;
        }

        if (this.foregroundColorEnd != foregroundColorEnd) {
            return true;
        }

        if (radius != radiusValue) {
            return true;
        }

        if (scaleFactor != scaleFactorValue) {
            return true;
        }

        return false;
    }
}

