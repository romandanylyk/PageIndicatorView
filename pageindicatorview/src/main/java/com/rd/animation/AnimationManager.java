package com.rd.animation;

import android.support.annotation.NonNull;
import com.rd.animation.controller.AnimationController;
import com.rd.animation.controller.ValueController;
import com.rd.animation.data.Value;
import com.rd.draw.DrawManager;

public class AnimationManager implements ValueController.UpdateListener {

    private AnimationController animationController;
    private DrawManager drawManager;

    public AnimationManager(@NonNull DrawManager drawManager) {
        this.drawManager = drawManager;
        this.animationController = new AnimationController(new ValueController(this), drawManager.indicator());
    }

    public void basic() {
        if (animationController != null) {
            animationController.basic();
        }
    }

    public void interactive(float progress) {
        if (animationController != null) {
            animationController.interactive(progress);
        }
    }

    public void end() {
        if (animationController != null) {
            animationController.end();
        }
    }

    @Override
    public void onValueUpdated(@NonNull Value value) {
//        drawManager.draw(value);
    }
}
