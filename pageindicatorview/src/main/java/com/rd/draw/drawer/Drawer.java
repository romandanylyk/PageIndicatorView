package com.rd.draw.drawer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import com.rd.animation.data.Value;
import com.rd.draw.data.Indicator;

public class Drawer {

    private BasicDrawer basicDrawer;
    private ColorDrawer colorDrawer;
    private ScaleDrawer scaleDrawer;

    private int position;
    private int coordinateX;
    private int coordinateY;

    public Drawer(@NonNull Indicator indicator) {
        Paint paint = new Paint();

        basicDrawer = new BasicDrawer(paint, indicator);
        colorDrawer = new ColorDrawer(paint, indicator);
        scaleDrawer = new ScaleDrawer(paint, indicator);
    }

    public void setup(int position, int coordinateX, int coordinateY) {
        this.position = position;
        this.coordinateX = coordinateX;
        this.coordinateY = coordinateY;
    }

    public void drawBasic(@NonNull Canvas canvas) {
        if (colorDrawer != null) {
            basicDrawer.draw(canvas, position, coordinateX, coordinateY);
        }
    }

    public void drawColor(@NonNull Canvas canvas, @NonNull Value value) {
        if (colorDrawer != null) {
            colorDrawer.draw(canvas, value, position, coordinateX, coordinateY);
        }
    }

    public void drawScale(@NonNull Canvas canvas, @NonNull Value value) {
        if (scaleDrawer != null) {
            scaleDrawer.draw(canvas, value, position, coordinateX, coordinateY);
        }
    }
}
