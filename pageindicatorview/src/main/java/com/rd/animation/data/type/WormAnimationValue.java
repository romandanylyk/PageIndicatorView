package com.rd.animation.data.type;

import com.rd.animation.data.Value;

public class WormAnimationValue implements Value {

    private int rectLeftEdge;
    private int rectRightEdge;

    public int getRectLeftEdge() {
        return rectLeftEdge;
    }

    public void setRectLeftEdge(int rectLeftEdge) {
        this.rectLeftEdge = rectLeftEdge;
    }

    public int getRectRightEdge() {
        return rectRightEdge;
    }

    public void setRectRightEdge(int rectRightEdge) {
        this.rectRightEdge = rectRightEdge;
    }
}
