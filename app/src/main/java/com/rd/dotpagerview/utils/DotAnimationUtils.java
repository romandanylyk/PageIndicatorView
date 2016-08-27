package com.rd.dotpagerview.utils;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.view.animation.DecelerateInterpolator;

public class DotAnimationUtils {

    public static final String ANIMATION_COLOR = "ANIMATION_COLOR";
    public static final String ANIMATION_COLOR_REVERSE = "ANIMATION_COLOR_REVERSE";

    public static final String ANIMATION_SCALE = "ANIMATION_SCALE";
    public static final String ANIMATION_SCALE_REVERSE = "ANIMATION_SCALE_REVERSE";

    public static final float SCALE_FACTOR = 2;
    private static final int ANIMATION_DURATION = 500;

    public interface Listener {
        void onAnimationUpdate(@NonNull ValueAnimator animation);
    }

    @SuppressWarnings("AccessStaticViaInstance")
    public static void startColorAnimation(int selectedColor, int unSelectedColor, @NonNull final Listener listener) {
        PropertyValuesHolder colorHolder = createColorPropertyHolder(selectedColor, unSelectedColor);
        PropertyValuesHolder reverseColorHolder = createReverseColorPropertyHolder(selectedColor, unSelectedColor);

        ValueAnimator animator = ObjectAnimator.ofPropertyValuesHolder(colorHolder, reverseColorHolder);
        animator.setDuration(ANIMATION_DURATION);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                listener.onAnimationUpdate(animation);
            }
        });

        animator.start();
    }

    @SuppressWarnings("AccessStaticViaInstance")
    public static void startScaleAnimation(int radiusPx, @NonNull final Listener listener) {
        PropertyValuesHolder scaleHolder = createScalePropertyHolder(radiusPx);
        PropertyValuesHolder scaleReverseHolder = createReverseScalePropertyHolder(radiusPx);

        ValueAnimator animator = ObjectAnimator.ofPropertyValuesHolder(scaleHolder, scaleReverseHolder);
        animator.setDuration(ANIMATION_DURATION);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                listener.onAnimationUpdate(animation);
            }
        });

        animator.start();
    }

    @SuppressWarnings("AccessStaticViaInstance")
    public static void startColorAndScaleAnimation(int selectedColor, int unSelectedColor, int radiusPx, @NonNull final Listener listener) {
        PropertyValuesHolder colorHolder = createColorPropertyHolder(selectedColor, unSelectedColor);
        PropertyValuesHolder reverseColorHolder = createReverseColorPropertyHolder(selectedColor, unSelectedColor);

        PropertyValuesHolder scaleHolder = createScalePropertyHolder(radiusPx);
        PropertyValuesHolder scaleReverseHolder = createReverseScalePropertyHolder(radiusPx);

        ValueAnimator animator = ObjectAnimator.ofPropertyValuesHolder(colorHolder, reverseColorHolder, scaleHolder, scaleReverseHolder);
        animator.setDuration(ANIMATION_DURATION);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                listener.onAnimationUpdate(animation);
            }
        });

        animator.start();
    }

    private static PropertyValuesHolder createColorPropertyHolder(int selectedColor, int unSelectedColor) {
        PropertyValuesHolder holder = PropertyValuesHolder.ofInt(ANIMATION_COLOR, unSelectedColor, selectedColor);
        holder.setEvaluator(new ArgbEvaluator());

        return holder;
    }

    private static PropertyValuesHolder createReverseColorPropertyHolder(int selectedColor, int unSelectedColor) {
        PropertyValuesHolder holder = PropertyValuesHolder.ofInt(ANIMATION_COLOR_REVERSE, selectedColor, unSelectedColor);
        holder.setEvaluator(new ArgbEvaluator());

        return holder;
    }

    private static PropertyValuesHolder createScalePropertyHolder(int radiusPx) {
        PropertyValuesHolder holder = PropertyValuesHolder.ofInt(ANIMATION_SCALE, (int) (radiusPx / SCALE_FACTOR), radiusPx);
        holder.setEvaluator(new ArgbEvaluator());

        return holder;
    }

    private static PropertyValuesHolder createReverseScalePropertyHolder(int radiusPx) {
        PropertyValuesHolder holder = PropertyValuesHolder.ofInt(ANIMATION_SCALE_REVERSE, radiusPx, (int) (radiusPx / SCALE_FACTOR));
        holder.setEvaluator(new ArgbEvaluator());

        return holder;
    }
}
