package com.rd.animation;

import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.view.animation.AccelerateDecelerateInterpolator;

public class ThinWormAnimation extends WormAnimation {

    private static final float PERCENTAGE_SIZE_DURATION_DELAY = 0.7f;
    private static final float PERCENTAGE_REVERSE_HEIGHT_DELAY = 0.65f;
    private static final float PERCENTAGE_HEIGHT_DURATION = 0.25f;
    private int height;

    public ThinWormAnimation(@NonNull ValueAnimation.UpdateListener listener) {
        super(listener);
    }

    @Override
    public ThinWormAnimation duration(long duration) {
        super.duration(duration);
        return this;
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

            long straightSizeDuration = (long) (animationDuration * PERCENTAGE_SIZE_DURATION_DELAY);
            long reverseSizeDuration = animationDuration;

            long straightHeightDelay = 0;
            long reverseHeightDelay = (long) (animationDuration * PERCENTAGE_REVERSE_HEIGHT_DELAY);

            AnimationValues values = createAnimationValues(isRightSide);
            ValueAnimator straightAnimator = createWormAnimator(values.fromX, values.toX, straightSizeDuration, false);
            ValueAnimator straightHeightAnimator = createHeightAnimator(height, height / 2, straightHeightDelay);

            ValueAnimator reverseAnimator = createWormAnimator(values.reverseFromX, values.reverseToX, reverseSizeDuration, true);
            ValueAnimator reverseHeightAnimator = createHeightAnimator(height / 2, height, reverseHeightDelay);

            animator.playTogether(straightAnimator, reverseAnimator, straightHeightAnimator, reverseHeightAnimator);
        }
        return this;
    }

    private ValueAnimator createHeightAnimator(int fromHeight, int toHeight, long startDelay) {
        ValueAnimator anim = ValueAnimator.ofInt(fromHeight, toHeight);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration((long) (animationDuration * PERCENTAGE_HEIGHT_DURATION));
        anim.setStartDelay(startDelay);
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
            long minus = (long) (animationDuration * PERCENTAGE_REVERSE_HEIGHT_DELAY);

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