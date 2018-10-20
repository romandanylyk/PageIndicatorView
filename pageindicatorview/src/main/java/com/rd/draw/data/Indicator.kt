package com.rd.draw.data

import android.view.View
import com.rd.animation.type.AnimationType

class Indicator {

	companion object {
		const val DEFAULT_COUNT = 3
		const val MIN_COUNT = 1
		const val COUNT_NONE = -1

		const val DEFAULT_RADIUS_DP = 6
		const val DEFAULT_PADDING_DP = 8
		const val IDLE_ANIMATION_DURATION = 250
	}

	var height: Int = 0
	var width: Int = 0
	var radius: Int = 0

	var padding: Int = 0
	var paddingLeft: Int = 0
	var paddingTop: Int = 0
	var paddingRight: Int = 0
	var paddingBottom: Int = 0

	var stroke: Int = 0 //For "Fill" animation only
	var scaleFactor: Float = 0.toFloat() //For "Scale" animation only

	var unselectedColor: Int = 0
	var selectedColor: Int = 0

	var isInteractiveAnimation: Boolean = false
	var isAutoVisibility: Boolean = false
	var isDynamicCount: Boolean = false

	var isFadeOnIdle: Boolean = false
	var isIdle: Boolean = false
	var idleDuration: Long = 0

	var animationDuration: Long = 0
	var count = DEFAULT_COUNT

	var selectedPosition: Int = 0
	var selectingPosition: Int = 0
	var lastSelectedPosition: Int = 0

	var viewPagerId = View.NO_ID

	var orientation: Orientation? = null
		get() {
			if (field == null) {
				this.orientation = Orientation.HORIZONTAL
			}
			return field
		}

	var animationType: AnimationType? = null
		get() {
			if (field == null) {
				this.animationType = AnimationType.NONE
			}
			return field
		}

	var rtlMode: RtlMode? = null
		get() {
			if (field == null) {
				this.rtlMode = RtlMode.Off
			}
			return field
		}
}
