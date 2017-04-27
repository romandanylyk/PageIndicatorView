package com.rd.animation.type;

import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.rd.animation.controller.ValueController;
import com.rd.animation.data.type.ThinWormAnimationValue;

public class ThinWormAnimation extends WormAnimation {

    private static final float SIZE_FACTOR = 0.7f;
    private static final float HEIGHT_FACTOR = 0.5f;

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

            RectValues values = createRectValues(isRightSide);
            long straightSizeDuration = (long) (animationDuration * SIZE_FACTOR);
            long reverseSizeDuration = animationDuration;

            long straightHeightDelay = 0;
            long reverseHeightDelay = (long) (animationDuration * HEIGHT_FACTOR);

            ValueAnimator straightAnimator = createWormAnimator(values.fromX, values.toX, straightSizeDuration, false, value);
            ValueAnimator straightHeightAnimator = createHeightAnimator(height, radius, straightHeightDelay);

            ValueAnimator reverseAnimator = createWormAnimator(values.reverseFromX, values.reverseToX, reverseSizeDuration, true, value);
            ValueAnimator reverseHeightAnimator = createHeightAnimator(radius, height, reverseHeightDelay);

            animator.playTogether(straightAnimator, reverseAnimator, straightHeightAnimator, reverseHeightAnimator);
        }
        return this;
    }

    private ValueAnimator createHeightAnimator(int fromHeight, int toHeight, long startDelay) {
        ValueAnimator anim = ValueAnimator.ofInt(fromHeight, toHeight);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.setDuration((long) (animationDuration * HEIGHT_FACTOR));
        anim.setStartDelay(startDelay);
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
            long duration = (long) (progress * animationDuration);
            int size = animator.getChildAnimations().size();
            long minus = (long) (animationDuration * HEIGHT_FACTOR);

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

                if (anim.getValues() != null && anim.getValues().length > 0) {
                    anim.setCurrentPlayTime(currPlayTime);
                }
            }
        }

        return this;
    }
}