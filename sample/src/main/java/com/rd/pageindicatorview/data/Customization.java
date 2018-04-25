package com.rd.pageindicatorview.data;

import android.os.Parcel;
import android.os.Parcelable;
import com.rd.animation.type.AnimationType;
import com.rd.draw.data.Orientation;
import com.rd.draw.data.RtlMode;

public class Customization implements Parcelable {

    private AnimationType animationType = AnimationType.NONE;
    private Orientation orientation = Orientation.HORIZONTAL;
    private RtlMode rtlMode = RtlMode.Off;

    private boolean interactiveAnimation = false;
    private boolean autoVisibility = true;
    private boolean foreground = false;

    public AnimationType getAnimationType() {
        return animationType;
    }

    public void setAnimationType(AnimationType animationType) {
        this.animationType = animationType;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public RtlMode getRtlMode() {
        return rtlMode;
    }

    public void setRtlMode(RtlMode rtlMode) {
        this.rtlMode = rtlMode;
    }

    public boolean isInteractiveAnimation() {
        return interactiveAnimation;
    }

    public void setInteractiveAnimation(boolean interactiveAnimation) {
        this.interactiveAnimation = interactiveAnimation;
    }

    public boolean isAutoVisibility() {
        return autoVisibility;
    }

    public void setAutoVisibility(boolean autoVisibility) {
        this.autoVisibility = autoVisibility;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customization that = (Customization) o;

        if (interactiveAnimation != that.interactiveAnimation) return false;
        if (autoVisibility != that.autoVisibility) return false;
        if (foreground != that.foreground) return false;
        if (animationType != that.animationType) return false;
        if (orientation != that.orientation) return false;
        return rtlMode == that.rtlMode;

    }

    @Override
    public int hashCode() {
        int result = animationType != null ? animationType.hashCode() : 0;
        result = 31 * result + (orientation != null ? orientation.hashCode() : 0);
        result = 31 * result + (rtlMode != null ? rtlMode.hashCode() : 0);
        result = 31 * result + (interactiveAnimation ? 1 : 0);
        result = 31 * result + (autoVisibility ? 1 : 0);
        result = 31 * result + (foreground ? 1 : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.animationType == null ? -1 : this.animationType.ordinal());
        dest.writeInt(this.orientation == null ? -1 : this.orientation.ordinal());
        dest.writeInt(this.rtlMode == null ? -1 : this.rtlMode.ordinal());
        dest.writeByte(this.interactiveAnimation ? (byte) 1 : (byte) 0);
        dest.writeByte(this.autoVisibility ? (byte) 1 : (byte) 0);
        dest.writeByte(this.foreground ? (byte) 1 : (byte) 0);
    }

    public Customization() {
    }

    protected Customization(Parcel in) {
        int tmpAnimationType = in.readInt();
        this.animationType = tmpAnimationType == -1 ? null : AnimationType.values()[tmpAnimationType];
        int tmpOrientation = in.readInt();
        this.orientation = tmpOrientation == -1 ? null : Orientation.values()[tmpOrientation];
        int tmpRtlMode = in.readInt();
        this.rtlMode = tmpRtlMode == -1 ? null : RtlMode.values()[tmpRtlMode];
        this.interactiveAnimation = in.readByte() != 0;
        this.autoVisibility = in.readByte() != 0;
        this.foreground = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Customization> CREATOR = new Parcelable.Creator<Customization>() {
        @Override
        public Customization createFromParcel(Parcel source) {
            return new Customization(source);
        }

        @Override
        public Customization[] newArray(int size) {
            return new Customization[size];
        }
    };

    public boolean isForeground() {
        return foreground;
    }

    public void setForeground(boolean foreground) {
        this.foreground = foreground;
    }
}
