package com.rd.animation;

import android.animation.ValueAnimator;
import android.support.annotation.NonNull;

public class ThinWormAnimation extends WormAnimation {

    private int height;

    public ThinWormAnimation(@NonNull ValueAnimation.UpdateListener listener) {
        super(listener);
    }

    @Override
    public WormAnimation with(int fromValue, int toValue, int radius, boolean isRightSide) {
        if (hasChanges(fromValue, toValue, radius, isRightSide)) {
            animator = createAnimator();

            this.fromValue = fromValue;
            this.toValue = toValue;
            this.radius = radius;
            this.height = radius * 2;
            this.isRightSide = isRightSide;

            rectLeftX = fromValue - radius;
            rectRightX = fromValue + radius;

            long straightSizeDuration = animationDuration - (animationDuration / 3);
            long reverseSizeDuration = animationDuration;

            AnimationValues values = createAnimationValues(isRightSide);
            ValueAnimator straightAnimator = createWormAnimator(values.fromX, values.toX, straightSizeDuration, false);
            ValueAnimator straightHeightAnimator = createHeightAnimator(height, height / 2);

            ValueAnimator reverseAnimator = createWormAnimator(values.reverseFromX, values.reverseToX, reverseSizeDuration, true);
            ValueAnimator reverseHeightAnimator = createHeightAnimator(height / 2, height);

            animator.playTogether(straightAnimator, straightHeightAnimator, reverseAnimator, reverseHeightAnimator);
        }
        return this;
    }

    private ValueAnimator createWormAnimator(int fromX, int toX, long duration, final boolean isReverse) {
        ValueAnimator anim = ValueAnimator.ofInt(fromX, toX);
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

    private ValueAnimator createHeightAnimator(int fromHeight, int toHeight) {
        ValueAnimator anim = ValueAnimator.ofInt(fromHeight, toHeight);
        anim.setDuration(animationDuration / 3);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                height = (int) animation.getAnimatedValue();
                listener.onThinWormAnimationUpdated(rectLeftX, rectRightX, height);
            }
        });

        return anim;
    }

    @Override
    public ThinWormAnimation progress(float progress) {
        if (animator != null) {
            long duration = (long) (progress * animationDuration);
            int size = animator.getChildAnimations().size();
            long minus = animationDuration / 2;

            for (int i = 0; i < size; i++) {
                ValueAnimator anim = (ValueAnimator) animator.getChildAnimations().get(i);

                if (i == 3) {
                    if (duration < minus) {
                        break;
                    } else {
                        duration -= minus;
                    }
                }

                long currPlayTime = duration;
                if (currPlayTime >= anim.getDuration()) {
                    currPlayTime = anim.getDuration();
                }

                anim.setCurrentPlayTime(currPlayTime);
            }
        }

        return this;
    }
}