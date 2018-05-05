package com.rd.draw.drawer.type;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import com.rd.animation.data.Value;
import com.rd.animation.data.type.WormAnimationValue;
import com.rd.draw.data.Indicator;
import com.rd.draw.data.Orientation;

public class WormDrawer extends BaseDrawer {

    public RectF rect;

    public WormDrawer(@NonNull Paint paint, @NonNull Indicator indicator) {
        super(paint, indicator);
        rect = new RectF();
    }

    public void draw(
            @NonNull Canvas canvas,
            @NonNull Value value,
            int coordinateX,
            int coordinateY,
            int position) {

        if (!(value instanceof WormAnimationValue)) {
            return;
        }

        WormAnimationValue v = (WormAnimationValue) value;
        int rectStart = v.getRectStart();
        int rectEnd = v.getRectEnd();

        int radius = indicator.getRadius();
        int unselectedColor = indicator.getUnselectedColor();
        int selectedColor = indicator.getSelectedColor();
        int color =  selectedColor;
        int unselectedForegroundColor = indicator.getUnselectedForegroundColor();
        int selectedForegroundColor = indicator.getSelectedForegroundColor();

        int selectedPosition = indicator.getSelectedPosition();
        int selectingPosition = indicator.getSelectingPosition();
        int lastSelectedPosition = indicator.getLastSelectedPosition();

        int finalRadius;

        if (indicator.isHasForeground()) {
             finalRadius = radius - indicator.getForegroundPadding();
             rectStart += indicator.getForegroundPadding();
             rectEnd -= indicator.getForegroundPadding();
        } else {
            finalRadius = radius;
        }

        if (indicator.getOrientation() == Orientation.HORIZONTAL) {
            rect.left = rectStart;
            rect.right = rectEnd;
            rect.top = coordinateY - finalRadius;
            rect.bottom = coordinateY + finalRadius;

        } else {
            rect.left = coordinateX - finalRadius;
            rect.right = coordinateX + finalRadius;
            rect.top = rectStart;
            rect.bottom = rectEnd;
        }

        if (indicator.isInteractiveAnimation()) {
            if (position == selectingPosition) {
                color = indicator.getSelectedColor();

            } else if (position == selectedPosition) {
                color = indicator.getUnselectedColor();
            }

        } else {
            if (position == selectedPosition) {
                color = indicator.getSelectedColor();

            } else if (position == lastSelectedPosition) {
                color = indicator.getUnselectedColor();
            }
        }



        if (indicator.isHasForeground()) {
            paint.setColor(color);
            canvas.drawCircle(coordinateX, coordinateY, radius, paint);

            paint.setColor(unselectedForegroundColor);
            canvas.drawCircle(coordinateX, coordinateY, finalRadius, paint);

            paint.setColor(selectedForegroundColor);
            canvas.drawRoundRect(rect, finalRadius, finalRadius, paint);
        } else {
            paint.setColor(unselectedColor);
            canvas.drawCircle(coordinateX, coordinateY, radius, paint);

            paint.setColor(selectedColor);
            canvas.drawRoundRect(rect, radius, radius, paint);
        }
    }
}
