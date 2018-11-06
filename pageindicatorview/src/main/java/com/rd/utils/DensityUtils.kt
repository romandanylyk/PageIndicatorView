package com.rd.utils

import android.content.res.Resources
import android.util.TypedValue

object DensityUtils {

	fun dpToPx(dp: Int): Int {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), Resources.getSystem().displayMetrics).toInt()
	}

	fun pxToDp(px: Float): Int {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, Resources.getSystem().displayMetrics).toInt()
	}
}
