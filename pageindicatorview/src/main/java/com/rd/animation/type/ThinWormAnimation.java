package com.rd.animation.type;

import android.animation.ValueAnimator;
import androidx.annotation.NonNull;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.rd.animation.controller.ValueController;
import com.rd.animation.data.type.ThinWormAnimationValue;

public class ThinWormAnimation extends WormAnimation {

    private ThinWormAnimationValue value;

    public ThinWormAnimation(@NonNull ValueController.UpdateListener listener) {
        super(listener);
        value = new ThinWormAnimationValue();
    }

    @Override
    public ThinWormAnimation duration(long duration) {
        super.duration(duration);
        return this;
    }

    @Override
    public WormAnimation with(int coordinateStart, int coordinateEnd, int radius, boolean isRightSide) {
        if (hasChanges(coordinateStart, coordinateEnd, radius, isRightSide)) {
            animator = createAnimator();

            this.coordinateStart = coordinateStart;
            this.coordinateEnd = coordinateEnd;

            this.radius = radius;
            this.isRightSide = isRightSide;

            int height = radius * 2;
            rectLeftEdge = coordinateStart - radius;
            rectRightEdge = coordinateStart + radius;

            value.setRectStart(rectLeftEdge);
            value.setRectEnd(rectRightEdge);
            value.setHeight(height);

            RectValues rec = createRectValues(isRightSide);
            long sizeDuration = (long) (animationDuration * 0.8);
            long reverseDelay = (long) (animationDuration * 0.2);

            long heightDuration = (long) (animationDuration * 0.5);
            long reverseHeightDelay = (long) (animationDuration * 0.5);

            ValueAnimator straightAnimator = createWormAnimator(rec.fromX, rec.toX, sizeDuration, false, value);
            ValueAnimator reverseAnimator = createWormAnimator(rec.reverseFromX, rec.reverseToX, sizeDuration, true, value);
            reverseAnimator.setStartDelay(reverseDelay);

            ValueAnimator straightHeightAnimator = createHeightAnimator(height, radius, heightDuration);
            ValueAnimator reverseHeightAnimator = createHeightAnimator(radius, height, heightDuration);
            reverseHeightAnimator.setStartDelay(reverseHeightDelay);

            animator.playTogether(straightAnimator, reverseAnimator, straightHeightAnimator, reverseHeightAnimator);
        }
        return this;
    }

    private ValueAnimator createHeightAnimator(int fromHeight, int toHeight, long duration) {
        ValueAnimator anim = ValueAnimator.ofInt(fromHeight, toHeight);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration(duration);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                onAnimateUpdated(animation);
            }
        });

        return anim;
    }

    private void onAnimateUpdated(@NonNull ValueAnimator animation) {
        value.setHeight((int) animation.getAnimatedValue());

        if (listener != null) {
            listener.onValueUpdated(value);
        }
    }

    @Override
    public ThinWormAnimation progress(float progress) {
        if (animator != null) {
            long progressDuration = (long) (progress * animationDuration);
            int size = animator.getChildAnimations().size();

            for (int i = 0; i < size; i++) {
                ValueAnimator anim = (ValueAnimator) animator.getChildAnimations().get(i);

                long setDuration = progressDuration - anim.getStartDelay();
                long duration = anim.getDuration();

                if (setDuration > duration) {
                    setDuration = duration;

                } else if (setDuration < 0) {
                    setDuration = 0;
                }

                if (i == size - 1 && setDuration <= 0) {
                    continue;
                }

                if (anim.getValues() != null && anim.getValues().length > 0) {
                    anim.setCurrentPlayTime(setDuration);
                }
            }
        }

        return this;
    }
}