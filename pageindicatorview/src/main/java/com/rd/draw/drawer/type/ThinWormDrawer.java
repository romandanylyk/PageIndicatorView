package com.rd.draw.drawer.type;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import com.rd.animation.data.Value;
import com.rd.animation.data.type.ThinWormAnimationValue;
import com.rd.draw.data.Indicator;

public class ThinWormDrawer extends WormDrawer {

    public ThinWormDrawer(@NonNull Paint paint, @NonNull Indicator indicator) {
        super(paint, indicator);
    }

    public void draw(
            @NonNull Canvas canvas,
            @NonNull Value value,
            int coordinateX,
            int coordinateY) {

        if (!(value instanceof ThinWormAnimationValue)) {
            return;
        }

        ThinWormAnimationValue v = (ThinWormAnimationValue) value;
        int rectLeftEdge = v.getRectLeftEdge();
        int rectRightEdge = v.getRectRightEdge();
        int height = v.getHeight() / 2;

        int radius = indicator.getRadius();
        int unselectedColor = indicator.getUnselectedColor();
        int selectedColor = indicator.getSelectedColor();

        rect.left = rectLeftEdge;
        rect.right = rectRightEdge;
        rect.top = coordinateY - height;
        rect.bottom = coordinateY + height;

        paint.setColor(unselectedColor);
        canvas.drawCircle(coordinateX, coordinateY, radius, paint);

        paint.setColor(selectedColor);
        canvas.drawRoundRect(rect, radius, radius, paint);
    }
}
