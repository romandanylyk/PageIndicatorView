package com.rd.animation.type;

import android.animation.Animator;
import android.animation.ValueAnimator;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.rd.animation.controller.ValueController;

public abstract class BaseAnimation<T extends Animator> {

    public static final int DEFAULT_ANIMATION_TIME = 350;
    protected long animationDuration = DEFAULT_ANIMATION_TIME;

    protected ValueController.UpdateListener listener;
    protected T animator;

    public BaseAnimation(@Nullable ValueController.UpdateListener listener) {
        this.listener = listener;
        animator = createAnimator();
    }

    @NonNull
    public abstract T createAnimator();

    public abstract BaseAnimation progress(float progress);

    public BaseAnimation duration(long duration) {
        animationDuration = duration;

        if (animator instanceof ValueAnimator) {
            animator.setDuration(animationDuration);
        }

        return this;
    }

    public void start() {
        if (animator != null && !animator.isRunning()) {
            animator.start();
        }
    }

    public void end() {
        if (animator != null && animator.isStarted()) {
            animator.end();
        }
    }
}
