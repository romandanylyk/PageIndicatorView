package com.rd.animation.data.type;

import com.rd.animation.data.Value;

public class WormAnimationValue implements Value {

    private int rectStart;
    private int rectRight;

    public int getRectStart() {
        return rectStart;
    }

    public void setRectStart(int rectStartEdge) {
        this.rectStart = rectStartEdge;
    }

    public int getRectEnd() {
        return rectRight;
    }

    public void setRectRight(int rectRight) {
        this.rectRight = rectRight;
    }
}
