package com.rd;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import com.rd.animation.AnimationManager;
import com.rd.draw.DrawManager;

public class IndicatorManager {

    private DrawManager drawManager;
    private AnimationManager animationManager;

    IndicatorManager(@NonNull Canvas canvas) {
        drawManager = new DrawManager(canvas);
        animationManager = new AnimationManager(drawManager);
    }

    public AnimationManager animate() {
        return animationManager;
    }

    public DrawManager set() {
        return drawManager;
    }

}
