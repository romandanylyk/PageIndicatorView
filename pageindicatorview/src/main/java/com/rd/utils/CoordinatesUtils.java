package com.rd.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;
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

        if (indicator.getOrientation() == Orientation.HORIZONTAL) {
            int x = 0;
            for (int i = 0; i < count; i++) {
                x += radiusPx + (strokePx / 2);

                if (position == i) {
                    return x;
                }

                x += radiusPx + paddingPx + (strokePx / 2);
            }

            return x;

        } else {
            int x = width / 2;

            if (indicator.getAnimationType() == AnimationType.DROP) {
                x += radiusPx;
            }

            return x;
        }
    }

    public static int getYCoordinate(@Nullable Indicator indicator, int position) {
        if (indicator == null) {
            return 0;
        }

        int height = indicator.getHeight();
        int radiusPx = indicator.getRadius();
        int strokePx = indicator.getStroke();
        int paddingPx = indicator.getPadding();
        int count = indicator.getCount();

        if (indicator.getOrientation() == Orientation.HORIZONTAL) {
            int y = height / 2;

            if (indicator.getAnimationType() == AnimationType.DROP) {
                y += radiusPx;
            }

            return y;

        } else {
            int y = 0;
            for (int i = 0; i < count; i++) {
                y += radiusPx + strokePx;

                if (position == i) {
                    return y;
                }

                y += radiusPx + paddingPx;
            }

            return y;
        }
    }

    public static Pair<Integer, Float> getProgress(@NonNull Indicator indicator, int position, float positionOffset, boolean isRtl) {
        int count = indicator.getCount();
        int selectedPosition = indicator.getSelectedPosition();

        if (isRtl) {
            position = (count - 1) - position;
        }


        if (position < 0) {
            position = 0;

        } else if (position > count - 1) {
            position = count - 1;
        }

        boolean isRightOverScrolled = position > selectedPosition;
        boolean isLeftOverScrolled;

        if (isRtl) {
            isLeftOverScrolled = position - 1 < selectedPosition;
        } else {
            isLeftOverScrolled = position + 1 < selectedPosition;
        }

        if (isRightOverScrolled || isLeftOverScrolled) {
            selectedPosition = position;
            indicator.setSelectedPosition(selectedPosition);
        }

        boolean slideToRightSide = selectedPosition == position && positionOffset != 0;
        int selectingPosition;
        float selectingProgress;

        if (slideToRightSide) {
            selectingPosition = isRtl ? position - 1 : position + 1;
            selectingProgress = positionOffset;

        } else {
            selectingPosition = position;
            selectingProgress = 1 - positionOffset;
        }

        if (selectingProgress > 1) {
            selectingProgress = 1;

        } else if (selectingProgress < 0) {
            selectingProgress = 0;
        }

        return new Pair<>(selectingPosition, selectingProgress);
    }
}
