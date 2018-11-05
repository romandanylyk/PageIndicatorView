package com.rd.draw.drawer.type;

import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.annotation.NonNull;
import com.rd.animation.data.Value;
import com.rd.animation.data.type.ThinWormAnimationValue;
import com.rd.draw.data.Indicator;
import com.rd.draw.data.Orientation;

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
        int rectStart = v.getRectStart();
        int rectEnd = v.getRectEnd();
        int height = v.getHeight() / 2;

        int radius = indicator.getRadius();
        int unselectedColor = indicator.getUnselectedColor();
        int selectedColor = indicator.getSelectedColor();

        if (indicator.getOrientation() == Orientation.HORIZONTAL) {
            rect.left = rectStart;
            rect.right = rectEnd;
            rect.top = coordinateY - height;
            rect.bottom = coordinateY + height;

        } else {
            rect.left = coordinateX - height;
            rect.right = coordinateX + height;
            rect.top = rectStart;
            rect.bottom = rectEnd;
        }

        paint.setColor(unselectedColor);
        canvas.drawCircle(coordinateX, coordinateY, radius, paint);

        paint.setColor(selectedColor);
        canvas.drawRoundRect(rect, radius, radius, paint);
    }
}
