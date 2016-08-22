package com.rd.dotpagerview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;

import com.rd.dotpagerview.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

public class DotPagerView extends View {

    private static final String DOT_UNSELECTED_COLOR = "#80ffffff";
    private static final int DOT_RADIUS_DP = 8;
    private static final int DOTS_PADDING_DP = 16;
    private static final int SIDES_PADDING = 4;

    private Paint paint;
    private List<Pair> dotList;

    public DotPagerView(Context context) {
        super(context);
        init();
    }

    public DotPagerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DotPagerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DotPagerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int dotDiameterDp = DOT_RADIUS_DP * 2;

        int widthDp = (dotDiameterDp * 4) + (DOTS_PADDING_DP * 3);
        int widthPx = DensityUtils.dpToPx(getContext(), widthDp);

        int heightDp = SIDES_PADDING + dotDiameterDp;
        int heightPx = DensityUtils.dpToPx(getContext(), heightDp);

        setMeasuredDimension(widthPx, heightPx);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        dotList = createDotList();
        int radius = DensityUtils.dpToPx(getContext(), DOT_RADIUS_DP);

        for (Pair pair : dotList) {
            int x = (int) pair.first;
            int y = (int) pair.second;
            canvas.drawCircle(x, y, radius, paint);
        }
    }

    private void init() {
        initPaint();
    }

    private void initPaint() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor(DOT_UNSELECTED_COLOR));
    }

    @NonNull
    private List<Pair> createDotList() {
        List<Pair> dotList = new ArrayList<>();

        int width = 0;
        int heightCenter = getHeight() / 2;

        int dotRadiusPx = DensityUtils.dpToPx(getContext(), DOT_RADIUS_DP);
        int dotPaddingPx = DensityUtils.dpToPx(getContext(), DOTS_PADDING_DP);

        for (int i = 0; i < 4; i++) {
            width += dotRadiusPx;

            Pair pair = new Pair<>(width, heightCenter);
            dotList.add(pair);

            width += dotRadiusPx + dotPaddingPx;
        }

        return dotList;
    }
}
