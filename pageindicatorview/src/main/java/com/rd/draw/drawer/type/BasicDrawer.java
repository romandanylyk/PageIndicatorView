package com.rd.draw.drawer.type;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import com.rd.animation.type.AnimationType;
import com.rd.draw.data.Indicator;

public class BasicDrawer extends BaseDrawer {

    private Paint strokePaint;

    public BasicDrawer(@NonNull Paint paint, @NonNull Indicator indicator) {
        super(paint, indicator);

        strokePaint = new Paint();
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setAntiAlias(true);
        strokePaint.setStrokeWidth(indicator.getStroke());
    }

    public void draw(
            @NonNull Canvas canvas,
            int position,
            boolean isSelectedItem,
            int coordinateX,
            int coordinateY) {

        float radius = indicator.getRadius();
        int strokePx = indicator.getStroke();
        float scaleFactor = indicator.getScaleFactor();

        int selectedColor = indicator.getSelectedColor();
        int unselectedColor = indicator.getUnselectedColor();

        int selectedForegroundColor = indicator.getSelectedForegroundColor();
        int unselectedForegroundColor = indicator.getUnselectedForegroundColor();

        int selectedPosition = indicator.getSelectedPosition();
        AnimationType animationType = indicator.getAnimationType();
        boolean hasForeground = indicator.isHasForeground();

		if (animationType == AnimationType.SCALE && !isSelectedItem) {
			radius *= scaleFactor;

		} else if (animationType == AnimationType.SCALE_DOWN && isSelectedItem) {
			radius *= scaleFactor;
		}

        int color = unselectedColor;
		int foregroundColor = unselectedForegroundColor;
        if (position == selectedPosition) {
            color = selectedColor;
            foregroundColor = selectedForegroundColor;
        }

        Paint paint;
        if (animationType == AnimationType.FILL && position != selectedPosition) {
            paint = strokePaint;
            paint.setStrokeWidth(strokePx);
        } else {
            paint = this.paint;
        }

        paint.setColor(color);
        canvas.drawCircle(coordinateX, coordinateY, radius, paint);

        if (hasForeground && (
                animationType == AnimationType.COLOR ||
                animationType == AnimationType.NONE ||
                animationType == AnimationType.SCALE ||
                animationType == AnimationType.WORM)) {
            paint.setColor(foregroundColor);
            canvas.drawCircle(coordinateX, coordinateY, radius - indicator.getForegroundPadding(), paint);
        }
    }
}
