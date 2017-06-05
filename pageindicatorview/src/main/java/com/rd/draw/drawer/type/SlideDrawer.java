package com.rd.draw.drawer.type;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;

import com.rd.animation.data.Value;
import com.rd.animation.data.type.SlideAnimationValue;
import com.rd.draw.data.Indicator;
import com.rd.draw.data.Orientation;

public class SlideDrawer extends BaseDrawer {

    public SlideDrawer(@NonNull Paint paint, @NonNull Indicator indicator) {
        super(paint, indicator);
    }

    public void draw(
            @NonNull Canvas canvas,
            @NonNull Value value,
            int coordinateX,
            int coordinateY) {

        if (!(value instanceof SlideAnimationValue)) {
            return;
        }

        SlideAnimationValue v = (SlideAnimationValue) value;
        int coordinate = v.getCoordinate();
        int unselectedColor = indicator.getUnselectedColor();
        int selectedColor = indicator.getSelectedColor();

        paint.setColor(unselectedColor);
        drawIndicator(canvas,paint,coordinateX,coordinateY);


        paint.setColor(selectedColor);
        int slideX = coordinate;
        int slideY = coordinateY;
        if (indicator.getOrientation() == Orientation.VERTICAL) {
            slideX = coordinateX;
            slideY = coordinate;
        }

        drawIndicator(canvas,paint,slideX,slideY);
    }
}
