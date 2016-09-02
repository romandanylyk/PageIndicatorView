package com.rd.dotpagerview.view.animation;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;

public class SlideAnimation {

    private static final int ANIMATION_DURATION = 250;
    private int leftX;
    private int rightX;
    private boolean isRightSide;

    private ValueAnimation.UpdateListener listener;
    private AnimatorSet animatorSet;

    public SlideAnimation(@Nullable ValueAnimation.UpdateListener listener) {
        this.listener = listener;
        animatorSet = createAnimatorSet();
    }

    public SlideAnimation with(int fromX, int toX, final int reverseFromX, int reverseToX, final boolean isRight) {
        if (animatorSet != null) {
            leftX = reverseFromX;
            isRightSide = isRight;

            ValueAnimator animator = createValueAnimator(fromX, toX, false);
            ValueAnimator reverseAnimator = createValueAnimator(reverseFromX, reverseToX, true);

            animatorSet.play(animator).before(reverseAnimator);
        }
        return this;
    }

    public void start() {
        if (animatorSet != null) {
            animatorSet.start();
        }
    }

    public void end() {
        if (animatorSet != null) {
            animatorSet.end();
        }
    }

    private AnimatorSet createAnimatorSet() {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(ANIMATION_DURATION);
        animatorSet.setInterpolator(new DecelerateInterpolator());

        return animatorSet;
    }

    private ValueAnimator createValueAnimator(int fromX, int toX, final boolean isReverseAnimator) {
        ValueAnimator animator = ValueAnimator.ofInt(fromX, toX);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();

                if (!isReverseAnimator) {
                    if (isRightSide) {
                        rightX = value;
                    } else {
                        leftX = value;
                    }

                } else {
                    if (isRightSide) {
                        leftX = value;
                    } else {
                        rightX = value;
                    }
                }

                listener.onSlideAnimationUpdated(leftX, rightX);
            }
        });

        return animator;
    }
}
