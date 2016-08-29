package com.rd.dotpagerview.view.animation;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.view.animation.DecelerateInterpolator;

public class ColorAnimation {

    private static final String ANIMATION_COLOR_REVERSE = "ANIMATION_COLOR_REVERSE";
    private static final String ANIMATION_COLOR = "ANIMATION_COLOR";
    private static final int ANIMATION_DURATION = 2000;

    public interface Listener {
        void onColorAnimationUpdated(int color, int colorReverse);
    }

    public static void start(int selectedColor, int unSelectedColor, @NonNull final Listener listener) {
        PropertyValuesHolder colorHolder = createColorPropertyHolder(ANIMATION_COLOR, selectedColor, unSelectedColor);
        PropertyValuesHolder reverseColorHolder = createColorPropertyHolder(ANIMATION_COLOR_REVERSE, unSelectedColor, selectedColor);

        ValueAnimator animator = ObjectAnimator.ofPropertyValuesHolder(colorHolder, reverseColorHolder);
        animator.setDuration(ANIMATION_DURATION);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int color = (int) animation.getAnimatedValue(ANIMATION_COLOR);
                int colorReverse = (int) animation.getAnimatedValue(ANIMATION_COLOR_REVERSE);
                listener.onColorAnimationUpdated(color, colorReverse);
            }
        });

        animator.start();
    }

    protected static PropertyValuesHolder createColorPropertyHolder(@NonNull String propertyName, int selectedColor, int unSelectedColor) {
        PropertyValuesHolder holder = PropertyValuesHolder.ofInt(propertyName, unSelectedColor, selectedColor);
        holder.setEvaluator(new ArgbEvaluator());

        return holder;
    }
}
