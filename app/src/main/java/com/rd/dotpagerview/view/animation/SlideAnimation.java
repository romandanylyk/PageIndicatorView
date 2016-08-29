package com.rd.dotpagerview.view.animation;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.view.animation.DecelerateInterpolator;

public class SlideAnimation {

    private static final int ANIMATION_DURATION = 250;

    private static AnimatorSet animatorSet;
    private static int leftX;
    private static int rightX;

    public interface Listener {
        void onSlideAnimationUpdated(int leftX, int rightX);
    }

    public static void start(
            int fromX, int toX,
            final int reverseFromX, int reverseToX,
            @NonNull final Listener listener) {

        leftX = reverseFromX;

        ValueAnimator animator = ValueAnimator.ofInt(fromX, toX);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                rightX = (int) animation.getAnimatedValue();
                listener.onSlideAnimationUpdated(leftX, rightX);
            }
        });

        ValueAnimator reverseAnimator = ValueAnimator.ofInt(reverseFromX, reverseToX);
        reverseAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                leftX = (int) animation.getAnimatedValue();
                listener.onSlideAnimationUpdated(leftX, rightX);
            }
        });

        animatorSet = new AnimatorSet();
        animatorSet.setDuration(ANIMATION_DURATION);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.play(animator).before(reverseAnimator);
        animatorSet.start();
    }

    public static void end() {
        if (animatorSet != null) {
            animatorSet.end();
        }
    }
}
