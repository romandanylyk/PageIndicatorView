package com.rd.dotpagerview.view.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;

public class SlideAnimation {

    private static final int ANIMATION_DURATION = 250;

    private int fromValue;
    private int toValue;
    private int radius;
    private boolean isRightSide;

    private int rectLeftX;
    private int rectRightX;

    private ValueAnimation.UpdateListener listener;
    private AnimatorSet animatorSet;

    public SlideAnimation(@Nullable ValueAnimation.UpdateListener listener) {
        this.listener = listener;
        animatorSet = createAnimatorSet();
    }

    public SlideAnimation with(int fromValue, int toValue, int radius, boolean isRightSide) {
        if (animatorSet != null && hasChanges(fromValue, toValue, radius, isRightSide)) {
            animatorSet = createAnimatorSet();

            this.fromValue = fromValue;
            this.toValue = toValue;
            this.radius = radius;
            this.isRightSide = isRightSide;

            AnimationValues values = createAnimationValues(isRightSide);
            ValueAnimator animator = createValueAnimator(values.fromX, values.toX, false);
            ValueAnimator reverseAnimator = createValueAnimator(values.reverseFromX, values.reverseToX, true);

            animatorSet.playSequentially(animator, reverseAnimator);
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

    public void progress(float progress) {
        if (animatorSet != null) {
            long fullDuration = ANIMATION_DURATION * animatorSet.getChildAnimations().size();
            long playTimeLeft = (long) (progress * fullDuration);

            for (Animator animator : animatorSet.getChildAnimations()) {
                ValueAnimator valueAnimator = (ValueAnimator) animator;

                if (playTimeLeft < 0) {
                    playTimeLeft = 0;
                }

                long currPlayTime = playTimeLeft;
                if (currPlayTime >= valueAnimator.getDuration()) {
                    currPlayTime = valueAnimator.getDuration();
                }

                valueAnimator.setCurrentPlayTime(currPlayTime);
                playTimeLeft -= currPlayTime;
            }
        }
    }

    private AnimatorSet createAnimatorSet() {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new DecelerateInterpolator());

        return animatorSet;
    }

    private ValueAnimator createValueAnimator(int fromX, int toX, final boolean isReverseAnimator) {
        ValueAnimator animator = ValueAnimator.ofInt(fromX, toX);
        animator.setDuration(ANIMATION_DURATION);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();

                if (!isReverseAnimator) {
                    if (isRightSide) {
                        rectRightX = value;
                    } else {
                        rectLeftX = value;
                    }

                } else {
                    if (isRightSide) {
                        rectLeftX = value;
                    } else {
                        rectRightX = value;
                    }
                }

                listener.onSlideAnimationUpdated(rectLeftX, rectRightX);
            }
        });

        return animator;
    }

    @SuppressWarnings("RedundantIfStatement")
    private boolean hasChanges(int fromValue, int toValue, int radius, boolean isRightSide) {
        if (this.fromValue != fromValue) {
            return true;
        }

        if (this.toValue != toValue) {
            return true;
        }

        if (this.radius != radius) {
            return true;
        }

        if (this.isRightSide != isRightSide) {
            return true;
        }

        return false;
    }

    @NonNull
    private AnimationValues createAnimationValues(boolean isRightSide) {
        int fromX;
        int toX;

        int reverseFromX;
        int reverseToX;

        if (isRightSide) {
            fromX = fromValue + radius;
            toX = toValue + radius;

            reverseFromX = fromValue - radius;
            reverseToX = toValue - radius;

        } else {
            fromX = fromValue - radius;
            toX = toValue - radius;

            reverseFromX = fromValue + radius;
            reverseToX = toValue + radius;
        }

        return new AnimationValues(fromX, toX, reverseFromX, reverseToX);
    }

    private class AnimationValues {

        public final int fromX;
        private final int toX;

        private final int reverseFromX;
        private final int reverseToX;

        public AnimationValues(int fromX, int toX, int reverseFromX, int reverseToX) {
            this.fromX = fromX;
            this.toX = toX;

            this.reverseFromX = reverseFromX;
            this.reverseToX = reverseToX;
        }
    }
}
