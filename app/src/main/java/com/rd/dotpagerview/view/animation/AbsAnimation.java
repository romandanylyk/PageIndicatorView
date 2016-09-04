package com.rd.dotpagerview.view.animation;

import android.animation.ValueAnimator;
import android.support.annotation.NonNull;

public abstract class AbsAnimation {

    protected ValueAnimation.UpdateListener listener;
    protected ValueAnimator animator;

    public AbsAnimation(@NonNull ValueAnimation.UpdateListener listener) {
        this.listener = listener;
        animator = createValueAnimator();
    }

    @NonNull
    public abstract ValueAnimator createValueAnimator();

    public abstract long getAnimationDuration();

    public AbsAnimation progress(float progress) {
        if (animator != null) {
            long playTime = (long) (progress * getAnimationDuration());
            animator.setCurrentPlayTime(playTime);
        }

        return this;
    }

    public void start() {
        if (animator != null) {
            animator.start();
        }
    }

    public void end() {
        if (animator != null) {
            animator.end();
        }
    }
}
