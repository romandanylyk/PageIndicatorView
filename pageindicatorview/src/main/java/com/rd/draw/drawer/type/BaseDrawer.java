package com.rd.draw.drawer.type;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import com.rd.draw.IndicatorShape;
import com.rd.draw.data.Indicator;

class BaseDrawer {

    Paint paint;
    Indicator indicator;

    BaseDrawer(@NonNull Paint paint, @NonNull Indicator indicator) {
        this.paint = paint;
        this.indicator = indicator;
    }

    private void drawRectWithCenter(Canvas canvas, Paint paint,
                                      int centerX, int centerY, int width, int height) {
        canvas.drawRect(centerX-(width/2),
                centerY-(height/2),
                centerX+(width/2),
                centerY+(height/2),
                paint);
    }

    private void drawRectWithCenter(Canvas canvas, Paint paint,
                                    int centerX, int centerY, int width, int height, int cornerRadius) {
        canvas.drawRoundRect( new RectF(centerX-(width/2),
                centerY-(height/2),
                centerX+(width/2),
                centerY+(height/2)),
                cornerRadius,cornerRadius,paint);
    }

    void drawIndicator(Canvas canvas, Paint paint, int centerX, int centerY ) {
        drawIndicator(canvas,paint,centerX,centerY,
                indicator.getRadius(),indicator.getRectWidth(),indicator.getRectHeight());
    }

    void drawIndicator(Canvas canvas, Paint paint, int centerX, int centerY,
                       float radius, int rectWidth, int rectHeight ) {
        if ( indicator.getShape() == IndicatorShape.CIRCLE ) {
            canvas.drawCircle(centerX, centerY, radius, paint);
        } else if ( indicator.getShape() == IndicatorShape.RECTANGLE ) {
            if ( indicator.getCornerRadius() > 0 ) {
                drawRectWithCenter(
                        canvas,
                        paint,
                        centerX,
                        centerY,
                        rectWidth,
                        rectHeight,
                        indicator.getCornerRadius());
            } else {
                drawRectWithCenter(
                        canvas,
                        paint,
                        centerX,
                        centerY,
                        rectWidth,
                        rectHeight);
            }
        }
    }


}
