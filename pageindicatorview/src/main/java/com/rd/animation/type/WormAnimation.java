package com.rd.animation.type;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.rd.animation.controller.ValueController;
import com.rd.animation.data.type.WormAnimationValue;

public class WormAnimation extends BaseAnimation<AnimatorSet> {

    int widthStart;
    int widthEnd;
    int radius;
    boolean isRightSide;

    int rectLeftEdge;
    int rectRightEdge;

    private WormAnimationValue value;

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

            RectValues values = createRectValues(isRightSide);
            long duration = animationDuration / 2;

            ValueAnimator straightAnimator = createWormAnimator(values.fromX, values.toX, duration, false);
            ValueAnimator reverseAnimator = createWormAnimator(values.reverseFromX, values.reverseToX, duration, true);
            animator.playSequentially(straightAnimator, reverseAnimator);
        }
        return this;
    }

    @Override
    public WormAnimation progress(float progress) {
        if (animator == null) {
            return this;
        }

        long progressDuration = (long) (progress * animationDuration);
        for (Animator anim : animator.getChildAnimations()) {
            ValueAnimator animator = (ValueAnimator) anim;
            long duration = animator.getDuration();
            long setDuration = progressDuration;

            if (setDuration > duration) {
                setDuration = duration;
            }

            animator.setCurrentPlayTime(setDuration);
            progressDuration -= setDuration;
        }

        return this;
    }

    private ValueAnimator createWormAnimator(
            int fromX,
            int toX,
            long duration,
            final boolean isReverse) {

        ValueAnimator anim = ValueAnimator.ofInt(fromX, toX);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(duration);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                onAnimateUpdated(animation, isReverse);
            }
        });

        return anim;
    }

    private void onAnimateUpdated(@NonNull ValueAnimator animation, final boolean isReverse) {
        int rectEdge = (int) animation.getAnimatedValue();

        if (isRightSide) {
            if (!isReverse) {
                value.setRectRightEdge(rectEdge);
            } else {
                value.setRectLeftEdge(rectEdge);
            }

        } else {
            if (!isReverse) {
                value.setRectLeftEdge(rectEdge);
            } else {
                value.setRectRightEdge(rectEdge);
            }
        }

        if (listener != null) {
            listener.onValueUpdated(value);
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
    private RectValues createRectValues(boolean isRightSide) {
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

        return new RectValues(fromX, toX, reverseFromX, reverseToX);
    }

    private class RectValues {

        final int fromX;
        final int toX;

        final int reverseFromX;
        final int reverseToX;

        RectValues(int fromX, int toX, int reverseFromX, int reverseToX) {
            this.fromX = fromX;
            this.toX = toX;

            this.reverseFromX = reverseFromX;
            this.reverseToX = reverseToX;
        }
    }
}
