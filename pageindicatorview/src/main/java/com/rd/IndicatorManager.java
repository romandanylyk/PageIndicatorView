package com.rd;

import com.rd.animation.AnimationManager;
import com.rd.draw.DrawManager;
import com.rd.draw.data.Indicator;

public class IndicatorManager {

    private DrawManager drawManager;
    private AnimationManager animationManager;

    IndicatorManager() {
        drawManager = new DrawManager();
        animationManager = new AnimationManager(drawManager);
    }

    public AnimationManager animate() {
        return animationManager;
    }

    public Indicator indicator() {
        return drawManager.indicator();
    }
}
