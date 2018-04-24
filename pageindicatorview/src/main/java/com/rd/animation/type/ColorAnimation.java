package com.rd.animation.type;

import android.animation.ArgbEvaluator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.rd.animation.controller.ValueController;
import com.rd.animation.data.type.ColorAnimationValue;

public class ColorAnimation extends BaseAnimation<ValueAnimator> {

    public static final String DEFAULT_UNSELECTED_COLOR = "#33ffffff";
    public static final String DEFAULT_SELECTED_COLOR = "#ffffff";
    public static final String DEFAULT_FOREGROUND_UNSELECTED_COLOR = "#33ffffff";
    public static final String DEFAULT_FOREGROUND_SELECTED_COLOR = "#ffffff";

    static final String ANIMATION_COLOR_REVERSE = "ANIMATION_COLOR_REVERSE";
    static final String ANIMATION_COLOR = "ANIMATION_COLOR";
    static final String ANIMATION_FOREGROUND_COLOR_REVERSE = "ANIMATION_COLOR_FOREGROUND_REVERSE";
    static final String ANIMATION_FOREGROUND_COLOR = "ANIMATION_FOREGROUND_COLOR";

    private ColorAnimationValue value;

    int colorStart;
    int colorEnd;
    int foregroundColorStart;
    int foregroundColorEnd;

    public ColorAnimation(@Nullable ValueController.UpdateListener listener) {
        super(listener);
        value = new ColorAnimationValue();
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
    public ColorAnimation progress(float progress) {
        if (animator != null) {
            long playTime = (long) (progress * animationDuration);

            if (animator.getValues() != null && animator.getValues().length > 0) {
                animator.setCurrentPlayTime(playTime);
            }
        }

        return this;
    }

    @NonNull
    public ColorAnimation with(int colorStart, int colorEnd, int foregroundColorStart, int foregroundColorEnd) {
        if (animator != null && hasChanges(colorStart, colorEnd, foregroundColorStart, foregroundColorEnd)) {

            this.colorStart = colorStart;
            this.colorEnd = colorEnd;
            this.foregroundColorStart = foregroundColorStart;
            this.foregroundColorEnd = foregroundColorEnd;

            PropertyValuesHolder colorHolder = createColorPropertyHolder(false, false);
            PropertyValuesHolder reverseColorHolder = createColorPropertyHolder(true, false);
            PropertyValuesHolder colorForegroundHolder = createColorPropertyHolder(false, true);
            PropertyValuesHolder reverseForegroundColorHolder = createColorPropertyHolder(true, true);

            animator.setValues(colorHolder, reverseColorHolder, colorForegroundHolder, reverseForegroundColorHolder);
        }

        return this;
    }

    PropertyValuesHolder createColorPropertyHolder(boolean isReverse, boolean isForeground) {
        String propertyName;
        int colorStart;
        int colorEnd;

        if (isReverse) {
            propertyName = isForeground ? ANIMATION_FOREGROUND_COLOR_REVERSE : ANIMATION_COLOR_REVERSE;
            colorStart = isForeground ? this.foregroundColorEnd : this.colorEnd;
            colorEnd = isForeground ? this.foregroundColorStart : this.colorStart;

        } else {
            propertyName = isForeground ? ANIMATION_FOREGROUND_COLOR : ANIMATION_COLOR;
            colorStart = isForeground ? this.foregroundColorStart : this.colorStart;
            colorEnd = isForeground ? this.foregroundColorEnd : this.colorEnd;
        }

        PropertyValuesHolder holder = PropertyValuesHolder.ofInt(propertyName, colorStart, colorEnd);
        holder.setEvaluator(new ArgbEvaluator());

        return holder;
    }

    @SuppressWarnings("RedundantIfStatement")
    private boolean hasChanges(int colorStart, int colorEnd, int foregroundColorStart, int foregroundColorEnd) {
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

        return false;
    }

    private void onAnimateUpdated(@NonNull ValueAnimator animation) {
        int color = (int) animation.getAnimatedValue(ANIMATION_COLOR);
        int colorReverse = (int) animation.getAnimatedValue(ANIMATION_COLOR_REVERSE);
        int foregroundColor = (int) animation.getAnimatedValue(ANIMATION_FOREGROUND_COLOR);
        int foregroundColorReverse = (int) animation.getAnimatedValue(ANIMATION_FOREGROUND_COLOR_REVERSE);

        value.setColor(color);
        value.setColorReverse(colorReverse);
        value.setForegroundColor(foregroundColor);
        value.setForegroundColorReverse(foregroundColorReverse);

        if (listener != null) {
            listener.onValueUpdated(value);
        }
    }
}
