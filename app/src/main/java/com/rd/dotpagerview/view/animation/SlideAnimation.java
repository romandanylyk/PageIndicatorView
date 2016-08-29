package com.rd.dotpagerview.view.animation;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;

public class SlideAnimation {

    private static final int ANIMATION_DURATION = 350;

    private static int leftX;
    private static int rightX;

    public interface Listener {
        void onSlideAnimationUpdated(int leftX, int rightX);
    }

    public static void startSlideAnimation(
            int fromX, int toX,
            final int reverseFromX, int reverseToX,
            @NonNull final Listener listener) {

        leftX = fromX;

        ValueAnimator animator = ValueAnimator.ofInt(fromX, toX);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                rightX = (int) animation.getAnimatedValue();
                Log.e("TEST", "rightX: " + rightX);
                listener.onSlideAnimationUpdated(leftX, rightX);
            }
        });

        ValueAnimator reverseAnimator = ValueAnimator.ofInt(reverseFromX, reverseToX);
        reverseAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                leftX = (int) animation.getAnimatedValue();
                Log.e("TEST", "leftX: " + leftX);
                listener.onSlideAnimationUpdated(leftX, rightX);
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(ANIMATION_DURATION);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.play(animator).before(reverseAnimator);
        animatorSet.start();
    }
}
