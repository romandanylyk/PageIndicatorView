package com.rd.animation.data.type;

import com.rd.animation.data.Value;

public class ColorAnimationValue implements Value {

    private int color;
    private int colorReverse;
    private int foregroundColor;
    private int foregroundColorReverse;

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getColorReverse() {
        return colorReverse;
    }

    public void setColorReverse(int colorReverse) {
        this.colorReverse = colorReverse;
    }

    public int getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(int foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    public int getForegroundColorReverse() {
        return foregroundColorReverse;
    }

    public void setForegroundColorReverse(int foregroundColorReverse) {
        this.foregroundColorReverse = foregroundColorReverse;
    }
}
