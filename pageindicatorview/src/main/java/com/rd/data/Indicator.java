package com.rd.data;

public class Indicator {

    private int radiusPx;
    private int paddingPx;

    private int strokePx; //For "Fill" animation only
    private int scaleFactor; //For "Scale" animation only

    private int unselectedColor;
    private int selectedColor;

    private Orientation orientation;

    public int getRadiusPx() {
        return radiusPx;
    }

    public void setRadiusPx(int radiusPx) {
        this.radiusPx = radiusPx;
    }

    public int getPaddingPx() {
        return paddingPx;
    }

    public void setPaddingPx(int paddingPx) {
        this.paddingPx = paddingPx;
    }

    public int getStrokePx() {
        return strokePx;
    }

    public void setStrokePx(int strokePx) {
        this.strokePx = strokePx;
    }

    public int getScaleFactor() {
        return scaleFactor;
    }

    public void setScaleFactor(int scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    public int getUnselectedColor() {
        return unselectedColor;
    }

    public void setUnselectedColor(int unselectedColor) {
        this.unselectedColor = unselectedColor;
    }

    public int getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(int selectedColor) {
        this.selectedColor = selectedColor;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }
}
