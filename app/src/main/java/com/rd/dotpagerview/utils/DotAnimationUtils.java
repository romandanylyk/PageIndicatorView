package com.rd.dotpagerview.utils;

import android.animation.ArgbEvaluator;
import android.animation.IntEvaluator;
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

    public static final String ANIMATION_SLIDE = "ANIMATION_SLIDE";
    public static final String ANIMATION_SLIDE_REVERSE = "ANIMATION_SLIDE_REVERSE";

    public static final float SCALE_FACTOR = 2;
    private static final int ANIMATION_DURATION = 500;

    public interface Listener {
        void onAnimationUpdate(@NonNull ValueAnimator animation);
    }

    @SuppressWarnings("AccessStaticViaInstance")
    public static void startColorAnimation(int selectedColor, int unSelectedColor, @NonNull final Listener listener) {
        PropertyValuesHolder colorHolder = createColorPropertyHolder(ANIMATION_COLOR, selectedColor, unSelectedColor);
        PropertyValuesHolder reverseColorHolder = createColorPropertyHolder(ANIMATION_COLOR_REVERSE, unSelectedColor, selectedColor);

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
        PropertyValuesHolder scaleHolder = createScalePropertyHolder(ANIMATION_SCALE, (int) (radiusPx / SCALE_FACTOR), radiusPx);
        PropertyValuesHolder scaleReverseHolder = createScalePropertyHolder(ANIMATION_SCALE_REVERSE, radiusPx, (int) (radiusPx / SCALE_FACTOR));

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
        PropertyValuesHolder colorHolder = createColorPropertyHolder(ANIMATION_COLOR, selectedColor, unSelectedColor);
        PropertyValuesHolder reverseColorHolder = createColorPropertyHolder(ANIMATION_COLOR_REVERSE, unSelectedColor, selectedColor);

        PropertyValuesHolder scaleHolder = createScalePropertyHolder(ANIMATION_SCALE, (int) (radiusPx / SCALE_FACTOR), radiusPx);
        PropertyValuesHolder scaleReverseHolder = createScalePropertyHolder(ANIMATION_SCALE_REVERSE, radiusPx, (int) (radiusPx / SCALE_FACTOR));

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

    public static void startSlideAnimation(int fromX, int toX, @NonNull final Listener listener) {
        PropertyValuesHolder xHolder = crateSlidePropertyHolder(ANIMATION_SLIDE, fromX, toX);
        PropertyValuesHolder xReverseHolder = crateSlidePropertyHolder(ANIMATION_SLIDE_REVERSE, toX, fromX);

        ValueAnimator animator = ObjectAnimator.ofPropertyValuesHolder(xHolder);
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

    private static PropertyValuesHolder createColorPropertyHolder(@NonNull String propertyName, int selectedColor, int unSelectedColor) {
        PropertyValuesHolder holder = PropertyValuesHolder.ofInt(propertyName, unSelectedColor, selectedColor);
        holder.setEvaluator(new ArgbEvaluator());

        return holder;
    }

    private static PropertyValuesHolder createScalePropertyHolder(@NonNull String propertyName, int fromRadiusPx, int toRadiuxPx) {
        PropertyValuesHolder holder = PropertyValuesHolder.ofInt(propertyName, fromRadiusPx, toRadiuxPx);
        holder.setEvaluator(new ArgbEvaluator());

        return holder;
    }

    private static PropertyValuesHolder crateSlidePropertyHolder(@NonNull String propertyName, int fromX, int toX) {
        PropertyValuesHolder holder = PropertyValuesHolder.ofInt(propertyName, fromX, toX);
        holder.setEvaluator(new IntEvaluator());

        return holder;
    }
}
