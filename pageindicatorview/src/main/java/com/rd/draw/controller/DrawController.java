package com.rd.draw.controller;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import com.rd.animation.data.Value;
import com.rd.animation.type.AnimationType;
import com.rd.draw.data.Indicator;
import com.rd.draw.drawer.Drawer;
import com.rd.utils.CoordinatesUtils;

public class DrawController {

    private Value value;
    private Drawer drawer;
    private Indicator indicator;

    public DrawController(@NonNull Indicator indicator) {
        this.indicator = indicator;
        this.drawer = new Drawer(indicator);
    }

    public void updateValue(@NonNull Value value) {
        this.value = value;
    }

    public void draw(@NonNull Canvas canvas) {
        int count = indicator.getCount();

        for (int position = 0; position < count; position++) {
            int coordinateX = CoordinatesUtils.getXCoordinate(indicator, position);
            int coordinateY = CoordinatesUtils.getYCoordinate(indicator, position);
            drawIndicator(canvas, position, coordinateX, coordinateY);
        }
    }

    private void drawIndicator(
            @NonNull Canvas canvas,
            int position,
            int coordinateX,
            int coordinateY) {

        boolean interactiveAnimation = indicator.isInteractiveAnimation();
        int selectedPosition = indicator.getSelectedPosition();
        int selectingPosition = indicator.getSelectingPosition();
        int lastSelectedPosition = indicator.getLastSelectedPosition();

        boolean selectedItem = !interactiveAnimation && (position == selectedPosition || position == lastSelectedPosition);
        boolean selectingItem = interactiveAnimation && (position == selectedPosition || position == selectingPosition);
        boolean isSelectedItem = selectedItem | selectingItem;
        drawer.setup(position, coordinateX, coordinateY);

        if (value != null && isSelectedItem) {
            drawWithAnimation(canvas);
        } else {
            drawer.drawBasic(canvas, isSelectedItem);
        }
    }

    private void drawWithAnimation(@NonNull Canvas canvas) {
        AnimationType animationType = indicator.getAnimationType();
        switch (animationType) {
            case NONE:
                drawer.drawBasic(canvas, true);
                break;

            case COLOR:
                drawer.drawColor(canvas, value);
                break;

            case SCALE:
                drawer.drawScale(canvas, value);
                break;

            case WORM:
                drawer.drawWorm(canvas, value);
                break;

            case FILL:
                drawer.drawFill(canvas, value);
                break;

//            case SLIDE:
//                drawWithSlideAnimation(canvas, position, coordinateX, coordinateY);
//                break;
//
//            case THIN_WORM:
//                drawWithThinWormAnimation(canvas, coordinateX, coordinateY);
//                break;
//
//            case DROP:
//                drawWithDropAnimation(canvas, coordinateX, coordinateY);
//                break;
//
//            case SWAP:
//                if (orientation == Orientation.HORIZONTAL)
//                    drawWithSwapAnimation(canvas, position, coordinateX, coordinateY);
//                else
//                    drawWithSwapAnimationVertically(canvas, position, coordinateX, coordinateY);
//                break;
        }
    }
}
