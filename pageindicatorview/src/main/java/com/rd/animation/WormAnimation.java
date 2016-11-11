package com.rd.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

public class WormAnimation extends AbsAnimation<AnimatorSet> {

    int fromValue;
    int toValue;
    int radius;
    boolean isRightSide;

    int rectLeftX;
    int rectRightX;

    public WormAnimation(@NonNull ValueAnimation.UpdateListener listener) {
        super(listener);
    }

    @NonNull
    @Override
    public AnimatorSet createAnimator() {
        AnimatorSet animator = new AnimatorSet();
        animator.setInterpolator(new AccelerateDecelerateInterpolator());

        return animator;
    }

    public WormAnimation with(int fromValue, int toValue, int radius, boolean isRightSide) {
        if (hasChanges(fromValue, toValue, radius, isRightSide)) {
            animator = createAnimator();

            this.fromValue = fromValue;
            this.toValue = toValue;
            this.radius = radius;
            this.isRightSide = isRightSide;

            rectLeftX = fromValue - radius;
            rectRightX = fromValue + radius;

            AnimationValues values = createAnimationValues(isRightSide);
            long duration = animationDuration / 2;

            ValueAnimator straightAnimator = createWormAnimator(values.fromX, values.toX, duration, false);
            ValueAnimator reverseAnimator = createWormAnimator(values.reverseFromX, values.reverseToX, duration, true);

            animator.playSequentially(straightAnimator, reverseAnimator);
        }
        return this;
    }

    @Override
    public WormAnimation progress(float progress) {
        if (animator != null) {
            long playTimeLeft = (long) (progress * animationDuration);

            for (Animator anim : animator.getChildAnimations()) {
                ValueAnimator animator = (ValueAnimator) anim;
                long animDuration = animator.getDuration();

                if (playTimeLeft < 0) {
                    playTimeLeft = 0;
                }

                long currPlayTime = playTimeLeft;
                if (currPlayTime >= animDuration) {
                    currPlayTime = animDuration;
                }

                animator.setCurrentPlayTime(currPlayTime);
                playTimeLeft -= currPlayTime;
            }
        }

        return this;
    }

    ValueAnimator createWormAnimator(int fromX, int toX, final long duration, final boolean isReverse) {
        ValueAnimator anim = ValueAnimator.ofInt(fromX, toX);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(duration);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();

                if (!isReverse) {
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

                listener.onWormAnimationUpdated(rectLeftX, rectRightX);
            }
        });

        return anim;
    }

    @SuppressWarnings("RedundantIfStatement")
    boolean hasChanges(int fromValue, int toValue, int radius, boolean isRightSide) {
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
    AnimationValues createAnimationValues(boolean isRightSide) {
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

    class AnimationValues {

        final int fromX;
        final int toX;

        final int reverseFromX;
        final int reverseToX;

        AnimationValues(int fromX, int toX, int reverseFromX, int reverseToX) {
            this.fromX = fromX;
            this.toX = toX;

            this.reverseFromX = reverseFromX;
            this.reverseToX = reverseToX;
        }
    }
}
