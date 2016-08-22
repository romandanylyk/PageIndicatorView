package com.rd.dotpagerview.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;

public class DensityUtils {

    @SuppressWarnings("UnnecessaryLocalVariable")
    public static int dpToPx(@NonNull Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));

        return px;
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    public static int pxToDp(@NonNull Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));

        return dp;
    }
}
