package com.rd.draw.drawer.type;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;

import com.rd.animation.data.Value;
import com.rd.animation.data.type.FillAnimationValue;
import com.rd.draw.data.Indicator;

public class FillDrawer extends BaseDrawer {

    private Paint strokePaint;

    public FillDrawer(@NonNull Paint paint, @NonNull Indicator indicator) {
        super(paint, indicator);

        strokePaint = new Paint();
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setAntiAlias(true);
    }

    public void draw(
            @NonNull Canvas canvas,
            @NonNull Value value,
            int position,
            int coordinateX,
            int coordinateY) {

        if (!(value instanceof FillAnimationValue)) {
            return;
        }

        FillAnimationValue v = (FillAnimationValue) value;
        int color = indicator.getUnselectedColor();
        float radius = indicator.getRadius();
        int stroke = indicator.getStroke();

        int selectedPosition = indicator.getSelectedPosition();
        int selectingPosition = indicator.getSelectingPosition();
        int lastSelectedPosition = indicator.getLastSelectedPosition();

        float scale = 0.0f;

        if (indicator.isInteractiveAnimation()) {
            if (position == selectingPosition) {
                color = v.getColor();
                radius = v.getRadius();
                stroke = v.getStroke();
                scale = radius/v.getRadius();
            } else if (position == selectedPosition) {
                color = v.getColorReverse();
                radius = v.getRadiusReverse();
                stroke = v.getStrokeReverse();
                scale = radius/v.getRadiusReverse();
            }

        } else {
            if (position == selectedPosition) {
                color = v.getColor();
                radius = v.getRadius();
                stroke = v.getStroke();
                scale = radius/v.getRadius();
            } else if (position == lastSelectedPosition) {
                color = v.getColorReverse();
                radius = v.getRadiusReverse();
                stroke = v.getStrokeReverse();
                scale = radius/v.getRadiusReverse();
            }
        }

        strokePaint.setColor(color);
        strokePaint.setStrokeWidth(indicator.getStroke());
        drawIndicator(
                canvas,
                strokePaint,
                coordinateX,
                coordinateY,
                indicator.getRadius(),
                indicator.getRectWidth(),
                indicator.getRectHeight());

        strokePaint.setStrokeWidth(stroke);
        drawIndicator(
                canvas,
                strokePaint,
                coordinateX,
                coordinateY,
                radius,
                Float.valueOf(indicator.getRectWidth()-(scale*stroke)).intValue(),
                Float.valueOf(indicator.getRectHeight()-(scale*stroke)).intValue());
    }
}
