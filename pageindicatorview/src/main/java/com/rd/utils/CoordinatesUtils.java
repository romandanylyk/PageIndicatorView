package com.rd.utils;

import android.support.annotation.Nullable;
import com.rd.animation.type.AnimationType;
import com.rd.draw.data.Indicator;
import com.rd.draw.data.Orientation;

public class CoordinatesUtils {

    @SuppressWarnings("UnnecessaryLocalVariable")
    public static int getCoordinate(@Nullable Indicator indicator, int position) {
        if (indicator == null) {
            return 0;
        }

        if (indicator.getOrientation() == Orientation.HORIZONTAL) {
            return getXCoordinate(indicator, position);
        } else {
            return getYCoordinate(indicator, position);
        }
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    public static int getXCoordinate(@Nullable Indicator indicator, int position) {
        if (indicator == null) {
            return 0;
        }

        int width = indicator.getWidth();
        int radiusPx = indicator.getRadius();
        int strokePx = indicator.getStroke();
        int paddingPx = indicator.getPadding();
        int count = indicator.getCount();

        Orientation orientation = indicator.getOrientation();
        AnimationType animationType = indicator.getAnimationType();

        if (orientation == Orientation.HORIZONTAL) {
            int x = 0;
            for (int i = 0; i < count; i++) {
                x += radiusPx;

                if (position == i) {
                    return x;
                }

                x += radiusPx + paddingPx;
            }

            return x;

        } else {
            int x = width / 2;

            if (animationType == AnimationType.DROP) {
                x += radiusPx + strokePx;
            }

            return x;
        }
    }

    public static int getYCoordinate(@Nullable Indicator indicator, int position) {
        if (indicator == null) {
            return 0;
        }

        int width = indicator.getWidth();
        int radiusPx = indicator.getRadius();
        int strokePx = indicator.getStroke();
        int paddingPx = indicator.getPadding();
        int count = indicator.getCount();

        Orientation orientation = indicator.getOrientation();
        AnimationType animationType = indicator.getAnimationType();

        int y = 0;
        y += radiusPx;
        return y;

//        if (orientation == Orientation.HORIZONTAL) {
//            int y = 0;
//
//            for (int i = 0; i < count; i++) {
//                y += radiusPx;
//
//                if (position == i) {
//                    return y;
//                }
//
//                y += radiusPx + paddingPx;
//            }
//
//            return y;
//
//        } else {
//            int y = width / 2;
//
//            if (animationType == AnimationType.DROP) {
//                y += radiusPx;
//            }
//
//            return y;
//        }
    }
}
