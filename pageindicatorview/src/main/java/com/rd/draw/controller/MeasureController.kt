package com.rd.draw.controller

import android.util.Pair
import android.view.View
import com.rd.animation.type.AnimationType
import com.rd.draw.data.Indicator
import com.rd.draw.data.Orientation

class MeasureController {

	fun measureViewSize(indicator: Indicator, widthMeasureSpec: Int, heightMeasureSpec: Int): Pair<Int, Int> {
		val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
		val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)

		val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
		val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

		val count = indicator.count
		val radius = indicator.radius
		val stroke = indicator.stroke

		val padding = indicator.padding
		val paddingLeft = indicator.paddingLeft
		val paddingTop = indicator.paddingTop
		val paddingRight = indicator.paddingRight
		val paddingBottom = indicator.paddingBottom

		val circleDiameterPx = radius * 2
		var desiredWidth = 0
		var desiredHeight = 0

		var width = 0
		var height = 0

		val orientation = indicator.orientation
		if (count != 0) {
			val diameterSum = circleDiameterPx * count
			val strokeSum = stroke * 2 * count

			val paddingSum = padding * (count - 1)
			val w = diameterSum + strokeSum + paddingSum
			val h = circleDiameterPx + stroke

			if (orientation === Orientation.HORIZONTAL) {
				desiredWidth = w
				desiredHeight = h

			} else {
				desiredWidth = h
				desiredHeight = w
			}
		}

		if (indicator.animationType == AnimationType.DROP) {
			if (orientation === Orientation.HORIZONTAL) {
				desiredHeight *= 2
			} else {
				desiredWidth *= 2
			}
		}

		val horizontalPadding = paddingLeft + paddingRight
		val verticalPadding = paddingTop + paddingBottom

		if (orientation === Orientation.HORIZONTAL) {
			desiredWidth += horizontalPadding
			desiredHeight += verticalPadding

		} else {
			desiredWidth += horizontalPadding
			desiredHeight += verticalPadding
		}

		when (widthMode) {
			View.MeasureSpec.EXACTLY -> width = widthSize
			View.MeasureSpec.AT_MOST -> width = Math.min(desiredWidth, widthSize)
			View.MeasureSpec.UNSPECIFIED -> width = desiredWidth
		}

		when (heightMode) {
			View.MeasureSpec.EXACTLY -> height = heightSize
			View.MeasureSpec.AT_MOST -> height = Math.min(desiredHeight, heightSize)
			View.MeasureSpec.UNSPECIFIED -> height = desiredHeight
		}

		if (width < 0) width = 0
		if (height < 0) height = 0

		indicator.width = width
		indicator.height = height

		return Pair(width, height)
	}
}
