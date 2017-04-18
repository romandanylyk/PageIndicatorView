package com.rd.draw;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import com.rd.animation.data.Value;
import com.rd.draw.controller.DrawController;
import com.rd.draw.controller.MeasureController;
import com.rd.draw.data.Indicator;

public class DrawManager {

    private Indicator indicator;
    private DrawController drawController;
    private MeasureController measureController;

    public DrawManager() {
        this.indicator = new Indicator();
        this.drawController = new DrawController(indicator);
        this.measureController = new MeasureController();
    }

    @NonNull
    public Indicator indicator() {
        if (indicator == null) {
            indicator = new Indicator();
        }

        return indicator;
    }

    public void updateValue(@NonNull Value value) {
        drawController.updateValue(value);
    }

    public void draw(@NonNull Canvas canvas) {
        drawController.draw(canvas);
    }

    public int measureViewWidth(int widthMeasureSpec) {
        return measureController.measureViewWidth(indicator, widthMeasureSpec);
    }

    public int measureViewHeight(int heightMeasureSpec) {
        return measureController.measureViewHeight(indicator, heightMeasureSpec);
    }
}
