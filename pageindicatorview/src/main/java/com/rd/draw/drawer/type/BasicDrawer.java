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

        float radius = getIndicator().getRadius();
        int strokePx = getIndicator().getStroke();
        float scaleFactor = getIndicator().getScaleFactor();

        int selectedColor = getIndicator().getSelectedColor();
        int unselectedColor = getIndicator().getUnselectedColor();
        int selectedPosition = getIndicator().getSelectedPosition();
        AnimationType animationType = getIndicator().getAnimationType();

		if (animationType == AnimationType.SCALE && !isSelectedItem) {
			radius *= scaleFactor;

		} else if (animationType == AnimationType.SCALE_DOWN && isSelectedItem) {
			radius *= scaleFactor;
		}

        int color = unselectedColor;
        if (position == selectedPosition) {
            color = selectedColor;
        }

        Paint paint;
        if (animationType == AnimationType.FILL && position != selectedPosition) {
            paint = strokePaint;
            paint.setStrokeWidth(strokePx);
        } else {
            paint = this.getPaint();
        }

        paint.setColor(color);
        canvas.drawCircle(coordinateX, coordinateY, radius, paint);
    }
}
