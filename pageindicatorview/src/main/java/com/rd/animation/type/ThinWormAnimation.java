package com.rd.animation.type;

import android.animation.ValueAnimator;
import android.support.annotation.NonNull;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.rd.animation.controller.ValueController;
import com.rd.animation.data.type.ThinWormAnimationValue;

public class ThinWormAnimation extends WormAnimation {

    private static final float PERCENTAGE_SIZE_DURATION_DELAY = 0.7f;
    private static final float PERCENTAGE_REVERSE_HEIGHT_DELAY = 0.65f;
    private static final float PERCENTAGE_HEIGHT_DURATION = 0.25f;

    private ThinWormAnimationValue value;
    private int height;

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
    public WormAnimation with(int widthStart, int widthEnd, int radius, boolean isRightSide) {
        if (hasChanges(widthStart, widthEnd, radius, isRightSide)) {
            animator = createAnimator();

            this.widthStart = widthStart;
            this.widthEnd = widthEnd;

            this.radius = radius;
            this.height = radius * 2;
            this.isRightSide = isRightSide;

            rectLeftEdge = widthStart - radius;
            rectRightEdge = widthStart + radius;

            long straightSizeDuration = (long) (animationDuration * PERCENTAGE_SIZE_DURATION_DELAY);
            long reverseSizeDuration = animationDuration;

            long straightHeightDelay = 0;
            long reverseHeightDelay = (long) (animationDuration * PERCENTAGE_REVERSE_HEIGHT_DELAY);

//            AnimationValues values = createAnimationValues(isRightSide);
//            ValueAnimator straightAnimator = createWormAnimator(values.fromX, values.toX, straightSizeDuration, false);
//            ValueAnimator straightHeightAnimator = createHeightAnimator(height, height / 2, straightHeightDelay);
//
//            ValueAnimator reverseAnimator = createWormAnimator(values.reverseFromX, values.reverseToX, reverseSizeDuration, true);
//            ValueAnimator reverseHeightAnimator = createHeightAnimator(height / 2, height, reverseHeightDelay);
//
//            animator.playTogether(straightAnimator, reverseAnimator, straightHeightAnimator, reverseHeightAnimator);
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
                onAnimateUpdated(animation);

                if (listener != null) {
                    listener.onValueUpdated(value);
                }
            }
        });

        return anim;
    }

    private void onAnimateUpdated(@NonNull ValueAnimator animation) {
        height = (int) animation.getAnimatedValue();
        value.setHeight(height);
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

                if (anim.getValues() != null && anim.getValues().length > 0) {
                    anim.setCurrentPlayTime(currPlayTime);
                }
            }
        }

        return this;
    }
}