package com.rd.animation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class ValueAnimation {

    private ColorAnimation colorAnimation;
    private ScaleAnimation scaleAnimation;
    private WormAnimation wormAnimation;
    private SlideAnimation slideAnimation;
    private FillAnimation fillAnimation;
    private ThinWormAnimation thinWormAnimation;
    private DropAnimation dropAnimation;
    private SwapAnimation swapAnimation;

    private UpdateListener updateListener;

    public interface UpdateListener {

        void onColorAnimationUpdated(int color, int colorReverse);

        void onScaleAnimationUpdated(int color, int colorReverse, int radius, int radiusReverse);

        void onSlideAnimationUpdated(int value);

        void onWormAnimationUpdated(int leftX, int rightX);

        void onFillAnimationUpdated(int color, int colorReverse, int radius, int radiusReverse, int strokeWidth, int strokeWidthReverse);

        void onThinWormAnimationUpdated(int leftX, int rightX, int height);

        void onDropAnimationUpdated(int x, int y, int selectedRadius);

        void onSwapAnimationUpdated(int xCoordinate);
    }

    public ValueAnimation(@Nullable UpdateListener listener) {
        updateListener = listener;
    }

    @NonNull
    public ColorAnimation color() {
        if (colorAnimation == null) {
            colorAnimation = new ColorAnimation(updateListener);
        }

        return colorAnimation;
    }

    @NonNull
    public ScaleAnimation scale() {
        if (scaleAnimation == null) {
            scaleAnimation = new ScaleAnimation(updateListener);
        }

        return scaleAnimation;
    }

    @NonNull
    public WormAnimation worm() {
        if (wormAnimation == null) {
            wormAnimation = new WormAnimation(updateListener);
        }

        return wormAnimation;
    }

    @NonNull
    public SlideAnimation slide() {
        if (slideAnimation == null) {
            slideAnimation = new SlideAnimation(updateListener);
        }

        return slideAnimation;
    }

    @NonNull
    public FillAnimation fill() {
        if (fillAnimation == null) {
            fillAnimation = new FillAnimation(updateListener);
        }

        return fillAnimation;
    }

    @NonNull
    public ThinWormAnimation thinWorm() {
        if (thinWormAnimation == null) {
            thinWormAnimation = new ThinWormAnimation(updateListener);
        }

        return thinWormAnimation;
    }

    @NonNull
    public DropAnimation drop() {
        if (dropAnimation == null) {
            dropAnimation = new DropAnimation(updateListener);
        }

        return dropAnimation;
    }

    @NonNull
    public SwapAnimation swap() {
        if (swapAnimation == null) {
            swapAnimation = new SwapAnimation(updateListener);
        }

        return swapAnimation;
    }

}
