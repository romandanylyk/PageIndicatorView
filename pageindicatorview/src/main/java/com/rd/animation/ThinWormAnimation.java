package com.rd.animation;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;

public class ThinWormAnimation extends WormAnimation {

    private int height;

    public ThinWormAnimation(@NonNull ValueAnimation.UpdateListener listener) {
        super(listener);
    }

    @NonNull
    @Override
    public AnimatorSet createAnimator() {
        AnimatorSet animator = new AnimatorSet();
        animator.setInterpolator(new DecelerateInterpolator());

        return animator;
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

            AnimationValues values = createAnimationValues(isRightSide);
            ValueAnimator straightAnimator = createWormAnimator(values.fromX, values.toX, false);
            ValueAnimator straightHeightAnimator = createHeightAnimator(height, height / 2, false);

            ValueAnimator reverseAnimator = createWormAnimator(values.reverseFromX, values.reverseToX, true);
            ValueAnimator reverseHeightAnimator = createHeightAnimator(height / 2, height, true);

            animator.play(straightAnimator).with(straightHeightAnimator).before(reverseAnimator).before(reverseHeightAnimator);
//            animator.playSequentially(straightAnimator, reverseAnimator);
        }
        return this;
    }

    private ValueAnimator createHeightAnimator(int fromHeight, int toHeight, final boolean isReverse) {
        ValueAnimator anim = ValueAnimator.ofInt(fromHeight, toHeight);
        anim.setDuration(animationDuration / 6);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                height = (int) animation.getAnimatedValue();
                Log.e("TEST", "HEIGHT: " + isReverse);
                listener.onThinWormAnimationUpdated(rectLeftX, rectRightX, height);
            }
        });

        return anim;
    }

    @Override
    public ThinWormAnimation progress(float progress) {
        if (animator != null) {
            long playTimeLeft = (long) (progress * animationDuration);
            long durationToMinus = 0;

            for (int i = 0; i < animator.getChildAnimations().size(); i++) {
                ValueAnimator anim = (ValueAnimator) animator.getChildAnimations().get(i);

                if (playTimeLeft <= 0) {
                    return this;
                }

                long currPlayTime = playTimeLeft;
                if (currPlayTime >= anim.getDuration()) {
                    currPlayTime = anim.getDuration();
                }

                if (i == 0) {
                    durationToMinus = currPlayTime;
                } else if (i == 1) {
                    playTimeLeft -= durationToMinus;
                }

                anim.setCurrentPlayTime(currPlayTime);
            }
        }

        return this;
    }
}