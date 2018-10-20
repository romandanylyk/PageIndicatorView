package com.rd.draw.data

import android.os.Parcel
import android.os.Parcelable
import android.view.View

class PositionSavedState : View.BaseSavedState {

	var selectedPosition: Int = 0
	var selectingPosition: Int = 0
	var lastSelectedPosition: Int = 0

	constructor(superState: Parcelable) : super(superState) {}

	private constructor(`in`: Parcel) : super(`in`) {
		this.selectedPosition = `in`.readInt()
		this.selectingPosition = `in`.readInt()
		this.lastSelectedPosition = `in`.readInt()
	}

	override fun writeToParcel(out: Parcel, flags: Int) {
		super.writeToParcel(out, flags)
		out.writeInt(this.selectedPosition)
		out.writeInt(this.selectingPosition)
		out.writeInt(this.lastSelectedPosition)
	}

	companion object {

		@JvmField
		val CREATOR: Parcelable.Creator<PositionSavedState> = object : Parcelable.Creator<PositionSavedState> {
			override fun createFromParcel(`in`: Parcel): PositionSavedState {
				return PositionSavedState(`in`)
			}

			override fun newArray(size: Int): Array<PositionSavedState?> {
				return arrayOfNulls(size)
			}
		}
	}
}
