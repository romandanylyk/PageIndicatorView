package com.rd.animation.type;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import androidx.annotation.NonNull;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.rd.animation.controller.ValueController;
import com.rd.animation.data.type.WormAnimationValue;

public class WormAnimation extends BaseAnimation<AnimatorSet> {

    int coordinateStart;
    int coordinateEnd;

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

    public WormAnimation with(int coordinateStart, int coordinateEnd, int radius, boolean isRightSide) {
        if (hasChanges(coordinateStart, coordinateEnd, radius, isRightSide)) {
            animator = createAnimator();

            this.coordinateStart = coordinateStart;
            this.coordinateEnd = coordinateEnd;

            this.radius = radius;
            this.isRightSide = isRightSide;

            rectLeftEdge = coordinateStart - radius;
            rectRightEdge = coordinateStart + radius;

            value.setRectStart(rectLeftEdge);
            value.setRectEnd(rectRightEdge);

            RectValues rect = createRectValues(isRightSide);
            long duration = animationDuration / 2;

            ValueAnimator straightAnimator = createWormAnimator(rect.fromX, rect.toX, duration, false, value);
            ValueAnimator reverseAnimator = createWormAnimator(rect.reverseFromX, rect.reverseToX, duration, true, value);
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

    ValueAnimator createWormAnimator(
            int fromValue,
            int toValue,
            long duration,
            final boolean isReverse,
            final WormAnimationValue value) {

        ValueAnimator anim = ValueAnimator.ofInt(fromValue, toValue);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(duration);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                onAnimateUpdated(value, animation, isReverse);
            }
        });

        return anim;
    }

    private void onAnimateUpdated(@NonNull WormAnimationValue value, @NonNull ValueAnimator animation, final boolean isReverse) {
        int rectEdge = (int) animation.getAnimatedValue();

        if (isRightSide) {
            if (!isReverse) {
                value.setRectEnd(rectEdge);
            } else {
                value.setRectStart(rectEdge);
            }

        } else {
            if (!isReverse) {
                value.setRectStart(rectEdge);
            } else {
                value.setRectEnd(rectEdge);
            }
        }

        if (listener != null) {
            listener.onValueUpdated(value);
        }
    }

    @SuppressWarnings("RedundantIfStatement")
    boolean hasChanges(int coordinateStart, int coordinateEnd, int radius, boolean isRightSide) {
        if (this.coordinateStart != coordinateStart) {
            return true;
        }

        if (this.coordinateEnd != coordinateEnd) {
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
    RectValues createRectValues(boolean isRightSide) {
        int fromX;
        int toX;

        int reverseFromX;
        int reverseToX;

        if (isRightSide) {
            fromX = coordinateStart + radius;
            toX = coordinateEnd + radius;

            reverseFromX = coordinateStart - radius;
            reverseToX = coordinateEnd - radius;

        } else {
            fromX = coordinateStart - radius;
            toX = coordinateEnd - radius;

            reverseFromX = coordinateStart + radius;
            reverseToX = coordinateEnd + radius;
        }

        return new RectValues(fromX, toX, reverseFromX, reverseToX);
    }

    class RectValues {

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
