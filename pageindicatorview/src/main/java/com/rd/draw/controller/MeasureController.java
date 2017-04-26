package com.rd.draw.controller;

import android.support.annotation.NonNull;
import android.view.View;
import com.rd.animation.type.AnimationType;
import com.rd.draw.data.Indicator;
import com.rd.draw.data.Orientation;

public class MeasureController {

    public int measureViewWidth(@NonNull Indicator indicator, int widthMeasureSpec) {
        int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);

        int count = indicator.getCount();
        int radius = indicator.getRadius();
        int stroke = indicator.getStroke();
        int padding = indicator.getPadding();

        int circleDiameterPx = radius * 2;
        int desiredWidth = 0;

        if (count > 0) {
            int diameterSum = circleDiameterPx * count;
            int strokeSum = (stroke * 2) * count;
            int paddingSum = padding * (count - 1);
            desiredWidth = diameterSum + strokeSum + paddingSum;
        }

        int width;
        if (widthMode == View.MeasureSpec.EXACTLY) {
            width = widthSize;

        } else if (widthMode == View.MeasureSpec.AT_MOST) {
            width = Math.min(desiredWidth, widthSize);

        } else {
            width = desiredWidth;
        }

        if (indicator.getAnimationType() == AnimationType.DROP) {
            if (indicator.getOrientation() == Orientation.VERTICAL) {
                width *= 2;
            }
        }

        if (width < 0) {
            width = 0;
        }

        return width;
    }

    public int measureViewHeight(@NonNull Indicator indicator, int heightMeasureSpec) {
        int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = View.MeasureSpec.getSize(heightMeasureSpec);

        int count = indicator.getCount();
        int radius = indicator.getRadius();
        int stroke = indicator.getStroke();

        int circleDiameter = radius * 2;
        int desiredHeight = 0;

        if (count > 0) {
            desiredHeight = circleDiameter + stroke;
        }

        int height;
        if (heightMode == View.MeasureSpec.EXACTLY) {
            height = heightSize;

        } else if (heightMode == View.MeasureSpec.AT_MOST) {
            height = Math.min(desiredHeight, heightSize);

        } else {
            height = desiredHeight;
        }

        if (indicator.getAnimationType() == AnimationType.DROP) {
            if (indicator.getOrientation() == Orientation.HORIZONTAL) {
                height *= 2;
            }
        }

        if (height < 0) {
            height = 0;
        }

        return height;
    }
}
