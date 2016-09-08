package com.rd.dotpagerview.view.animation;

import android.animation.Animator;
import android.support.annotation.NonNull;

public abstract class AbsAnimation<T extends Animator> {

    protected ValueAnimation.UpdateListener listener;
    protected T animator;

    public AbsAnimation(@NonNull ValueAnimation.UpdateListener listener) {
        this.listener = listener;
        animator = createAnimator();
    }

    @NonNull
    public abstract T createAnimator();

    public abstract long getAnimationDuration();

    public abstract void progress(float progress);

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
