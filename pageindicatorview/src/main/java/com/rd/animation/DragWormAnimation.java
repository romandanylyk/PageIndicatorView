package com.rd.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.view.animation.AccelerateDecelerateInterpolator;


/**
 * Created by milton on 17/2/23.
 */

public class DragWormAnimation extends WormAnimation {
    private int radiusReversePx;
    private int radiusPx;

    public DragWormAnimation(@NonNull ValueAnimation.UpdateListener listener) {
        super(listener);
    }

    public DragWormAnimation with(int fromValue, int toValue, int radius, boolean isRightSide) {
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
            AnimatorSet straightAnimatorSet = new AnimatorSet();
            AnimatorSet reverseAnimatorSet = new AnimatorSet();
            ValueAnimator straightAnimator = createWormAnimator(values.fromX, values.toX, duration, false);
            ValueAnimator reverseAnimator = createWormAnimator(values.reverseFromX, values.reverseToX, duration, true);
            ValueAnimator straightHeightAnimator = createDragAnimation(0, radius, duration, false);
            ValueAnimator reverseHeightAnimator = createDragAnimation(radius, 0, duration, true);
            straightAnimatorSet.setDuration(duration);
            reverseAnimatorSet.setDuration(duration);
            straightAnimatorSet.playTogether(straightAnimator, straightHeightAnimator);
            reverseAnimatorSet.playTogether(reverseAnimator, reverseHeightAnimator);
            animator.playSequentially(straightAnimatorSet, reverseAnimatorSet);
        }
        return this;
    }

    private ValueAnimator createDragAnimation(int fromRadius, int toRadius, long duration, final boolean isReverse) {
        ValueAnimator anim = ValueAnimator.ofInt(fromRadius, toRadius);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(duration);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int val = (int) animation.getAnimatedValue();
                if (isRightSide) {
                    if (isReverse) {
                        radiusReversePx = val;
                    } else {
                        radiusPx = val;
                    }
                } else {
                    if (isReverse) {
                        radiusPx = val;
                    } else {
                        radiusReversePx = val;
                    }
                }

                listener.onDragWormAnimationUpdated(rectLeftX, rectRightX, radiusPx, radiusReversePx);
            }
        });

        return anim;
    }

    @Override
    public DragWormAnimation progress(float progress) {
        if (animator != null) {
            long playTimeLeft = (long) (progress * animationDuration);
            for (Animator anim : animator.getChildAnimations()) {
                AnimatorSet animatorSet = (AnimatorSet) anim;
                ValueAnimator anim1 = (ValueAnimator) animatorSet.getChildAnimations().get(0);
                ValueAnimator anim2 = (ValueAnimator) animatorSet.getChildAnimations().get(1);

                long animDuration = animatorSet.getDuration();

                if (playTimeLeft < 0) {
                    playTimeLeft = 0;
                }

                long currPlayTime = playTimeLeft;
                if (currPlayTime >= animDuration) {
                    currPlayTime = animDuration;
                }
                if (anim1.getValues() != null && anim1.getValues().length > 0) {
                    anim1.setCurrentPlayTime(currPlayTime);
                }
                if (anim2.getValues() != null && anim2.getValues().length > 0) {
                    anim2.setCurrentPlayTime(currPlayTime);
                }
                playTimeLeft -= currPlayTime;
            }
        }
        return this;
    }
}
