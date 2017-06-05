package com.rd.draw.drawer.type;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;

import com.rd.animation.data.Value;
import com.rd.animation.data.type.DropAnimationValue;
import com.rd.draw.data.Indicator;
import com.rd.draw.data.Orientation;

public class DropDrawer extends BaseDrawer {

    public DropDrawer(@NonNull Paint paint, @NonNull Indicator indicator) {
        super(paint, indicator);
    }

    public void draw(
            @NonNull Canvas canvas,
            @NonNull Value value,
            int coordinateX,
            int coordinateY) {

        if (!(value instanceof DropAnimationValue)) {
            return;
        }

        DropAnimationValue v = (DropAnimationValue) value;
        int unselectedColor = indicator.getUnselectedColor();
        int selectedColor = indicator.getSelectedColor();
        float radius = indicator.getRadius();

        paint.setColor(unselectedColor);
        drawIndicator(canvas,paint,coordinateX,coordinateY);

        paint.setColor(selectedColor);
        int dropX = v.getWidth();
        int dropY = v.getHeight();
        if (indicator.getOrientation() == Orientation.VERTICAL) {
            dropX = v.getHeight();
            dropY = v.getWidth();
        }

        drawIndicator(canvas,paint,dropX,dropY,
                v.getRadius(),
                indicator.getRectWidth(),
                indicator.getRectHeight());
    }
}
