package com.rd.animation.type;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.rd.animation.controller.ValueController;
import com.rd.animation.data.type.WormAnimationValue;

public class WormAnimation extends BaseAnimation<AnimatorSet> {

    private WormAnimationValue value;

    int widthStart;
    int widthEnd;
    int radius;
    boolean isRightSide;

    int rectLeftEdge;
    int rectRightEdge;

    public WormAnimation(@NonNull ValueController.UpdateListener listener) {
        super(listener);
        value = new WormAnimationValue();
    }

    @NonNull
    @Override
    public AnimatorSet createAnimator() {
        AnimatorSet animator = new AnimatorSet();
        animator.setInterpolator(new AccelerateDecelerateInterpolator());

        return animator;
    }

    @Override
    public WormAnimation duration(long duration) {
        super.duration(duration);
        return this;
    }

    public WormAnimation with(int widthStart, int widthEnd, int radius, boolean isRightSide) {
        if (hasChanges(widthStart, widthEnd, radius, isRightSide)) {
            animator = createAnimator();

            this.widthStart = widthStart;
            this.widthEnd = widthEnd;

            this.radius = radius;
            this.isRightSide = isRightSide;

            rectLeftEdge = widthStart - radius;
            rectRightEdge = widthStart + radius;

            AnimationValues values = createAnimationValues(isRightSide);
            long duration = animationDuration / 2;

//            ValueAnimator straightAnimator = createWormAnimator(values.fromX, values.toX, duration, false);
//            ValueAnimator reverseAnimator = createWormAnimator(values.reverseFromX, values.reverseToX, duration, true);
//
//            animator.playSequentially(straightAnimator, reverseAnimator);
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

                if (animator.getValues() != null && animator.getValues().length > 0) {
                    animator.setCurrentPlayTime(currPlayTime);
                }
                playTimeLeft -= currPlayTime;
            }
        }

        return this;
    }

    ValueAnimator createWormAnimator(
            int fromX,
            int toX,
            long duration,
            final boolean isReverse,
            @Nullable ValueAnimator.AnimatorUpdateListener listener) {

        ValueAnimator anim = ValueAnimator.ofInt(fromX, toX);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(duration);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                onAnimateUpdated(animation, isReverse);

//                if (listener != null) {
//                    listener.onWormAnimationUpdated(value);
//                }
            }
        });

        return anim;
    }

    private void onAnimateUpdated(@NonNull ValueAnimator animation, final boolean isReverse) {
        int rectEdge = (int) animation.getAnimatedValue();

        if (!isReverse) {
            if (isRightSide) {
                value.setRectRightEdge(rectEdge);
            } else {
                value.setRectLeftEdge(rectEdge);
            }

        } else {
            if (isRightSide) {
                value.setRectLeftEdge(rectEdge);
            } else {
                value.setRectRightEdge(rectEdge);
            }
        }
    }

    @SuppressWarnings("RedundantIfStatement")
    boolean hasChanges(int fromValue, int toValue, int radius, boolean isRightSide) {
        if (this.widthStart != fromValue) {
            return true;
        }

        if (this.widthEnd != toValue) {
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
            fromX = widthStart + radius;
            toX = widthEnd + radius;

            reverseFromX = widthStart - radius;
            reverseToX = widthEnd - radius;

        } else {
            fromX = widthStart - radius;
            toX = widthEnd - radius;

            reverseFromX = widthStart + radius;
            reverseToX = widthEnd + radius;
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
