package com.rd.draw.drawer.type;

import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.annotation.NonNull;
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

        if (indicator.isInteractiveAnimation()) {
            if (position == selectingPosition) {
                color = v.getColor();
                radius = v.getRadius();
                stroke = v.getStroke();

            } else if (position == selectedPosition) {
                color = v.getColorReverse();
                radius = v.getRadiusReverse();
                stroke = v.getStrokeReverse();
            }

        } else {
            if (position == selectedPosition) {
                color = v.getColor();
                radius = v.getRadius();
                stroke = v.getStroke();

            } else if (position == lastSelectedPosition) {
                color = v.getColorReverse();
                radius = v.getRadiusReverse();
                stroke = v.getStrokeReverse();
            }
        }

        strokePaint.setColor(color);
        strokePaint.setStrokeWidth(indicator.getStroke());
        canvas.drawCircle(coordinateX, coordinateY, indicator.getRadius(), strokePaint);

        strokePaint.setStrokeWidth(stroke);
        canvas.drawCircle(coordinateX, coordinateY, radius, strokePaint);
    }
}
