package com.rd.draw;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import com.rd.animation.data.Value;
import com.rd.draw.controller.DrawController;
import com.rd.draw.data.Indicator;

public class DrawManager {

    private Indicator indicator;
    private DrawController controller;

    public DrawManager() {
        this.indicator = new Indicator();
        this.controller = new DrawController(indicator);
    }

    @NonNull
    public Indicator indicator() {
        if (indicator == null) {
            indicator = new Indicator();
        }

        return indicator;
    }

    public void updateValue(@NonNull Value value) {
        controller.updateValue(value);
    }

    public void draw(@NonNull Canvas canvas) {
        controller.draw(canvas);
    }
}
