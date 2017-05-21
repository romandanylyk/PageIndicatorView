package com.rd.animation.data.type;

import com.rd.animation.data.Value;

public class FillAnimationValue extends ColorAnimationValue implements Value {

    private int radius;
    private int radiusReverse;

    private int stroke;
    private int strokeReverse;

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getRadiusReverse() {
        return radiusReverse;
    }

    public void setRadiusReverse(int radiusReverse) {
        this.radiusReverse = radiusReverse;
    }

    public int getStroke() {
        return stroke;
    }

    public void setStroke(int stroke) {
        this.stroke = stroke;
    }

    public int getStrokeReverse() {
        return strokeReverse;
    }

    public void setStrokeReverse(int strokeReverse) {
        this.strokeReverse = strokeReverse;
    }
}
