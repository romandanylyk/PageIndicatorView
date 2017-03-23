package com.rd.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.view.animation.AccelerateDecelerateInterpolator;

public class DropAnimation extends AbsAnimation<AnimatorSet> {

    private int fromValue;
    private int toValue;

    private int center;
    private int radius;

    private int frameX;
    private int frameY;
    private int frameRadius;

    public enum AnimationType {Width, Height, Radius}

    public DropAnimation(@NonNull ValueAnimation.UpdateListener listener) {
        super(listener);
    }

    @NonNull
    @Override
    public AnimatorSet createAnimator() {
        AnimatorSet animator = new AnimatorSet();
        animator.setInterpolator(new AccelerateDecelerateInterpolator());

        return animator;
    }

    @Override
    public DropAnimation progress(float progress) {
        if (animator != null) {
            long playTimeLeft = (long) (progress * animationDuration);
            boolean isReverse = false;

            for (Animator anim : animator.getChildAnimations()) {
                ValueAnimator animator = (ValueAnimator) anim;
                long animDuration = animator.getDuration();
                long currPlayTime = playTimeLeft;

                if (isReverse) {
                    currPlayTime -= animDuration;
                }

                if (currPlayTime < 0) {
                    continue;

                } else if (currPlayTime >= animDuration) {
                    currPlayTime = animDuration;
                }

                if (animator.getValues() != null && animator.getValues().length > 0) {
                    animator.setCurrentPlayTime(currPlayTime);
                }

                if (!isReverse && animDuration >= animationDuration) {
                    isReverse = true;
                }
            }
        }

        return this;
    }

    @Override
    public DropAnimation duration(long duration) {
        super.duration(duration);
        return this;
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    public DropAnimation with(int fromValue, int toValue, int center, int radius) {
        if (hasChanges(fromValue, toValue, center, radius)) {
            animator = createAnimator();

            this.fromValue = fromValue;
            this.toValue = toValue;

            this.center = center;
            this.radius = radius;

            int yFromValue = center;
            int yToValue = center / 3;

            int fromSelectedRadius = radius;
            int toSelectedRadius = (int) (radius / 1.5);
            long halfDuration = animationDuration / 2;

            frameX = fromValue;
            frameY = yFromValue;
            frameRadius = radius;

            ValueAnimator widthAnimator = createValueAnimation(fromValue, toValue, animationDuration, AnimationType.Width);
            ValueAnimator heightForwardAnimator = createValueAnimation(yFromValue, yToValue, halfDuration, AnimationType.Height);
            ValueAnimator heightBackwardAnimator = createValueAnimation(yToValue, yFromValue, halfDuration, AnimationType.Height);

            ValueAnimator radiusForwardAnimator = createValueAnimation(fromSelectedRadius, toSelectedRadius, halfDuration, AnimationType.Radius);
            ValueAnimator radiusBackwardAnimator = createValueAnimation(toSelectedRadius, fromSelectedRadius, halfDuration, AnimationType.Radius);

            animator.play(heightForwardAnimator).with(radiusForwardAnimator).with(widthAnimator).before(heightBackwardAnimator).before(radiusBackwardAnimator);
        }

        return this;
    }

    private ValueAnimator createValueAnimation(int fromValue, int toValue, long duration, final AnimationType type) {
        ValueAnimator anim = ValueAnimator.ofInt(fromValue, toValue);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(duration);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                onAnimatorUpdate(value, type);
            }
        });

        return anim;
    }

    private void onAnimatorUpdate(int value, AnimationType type) {
        switch (type) {
            case Width:
                frameX = value;
                break;

            case Height:
                frameY = value;
                break;

            case Radius:
                frameRadius = value;
                break;
        }

        if (listener != null) {
            listener.onDropAnimationUpdated(frameX, frameY, frameRadius);
        }
    }

    @SuppressWarnings("RedundantIfStatement")
    private boolean hasChanges(int fromValue, int toValue, int center, int radius) {
        if (this.fromValue != fromValue) {
            return true;
        }

        if (this.toValue != toValue) {
            return true;
        }

        if (this.center != center) {
            return true;
        }

        if (this.radius != radius) {
            return true;
        }

        return false;
    }

}
