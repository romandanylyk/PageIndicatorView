package com.rd.draw.drawer.type

import android.graphics.Canvas
import android.graphics.Paint
import com.rd.animation.data.Value
import com.rd.animation.data.type.SwapAnimationValue
import com.rd.draw.data.Indicator
import com.rd.draw.data.Orientation

class SwapDrawer(paint: Paint, indicator: Indicator) : BaseDrawer(paint, indicator) {

	fun draw(
			canvas: Canvas,
			value: Value,
			position: Int,
			coordinateX: Int,
			coordinateY: Int) {

		if (value !is SwapAnimationValue) {
			return
		}

		val selectedColor = indicator.selectedColor
		val unselectedColor = indicator.unselectedColor
		val radius = indicator.radius

		val selectedPosition = indicator.selectedPosition
		val selectingPosition = indicator.selectingPosition
		val lastSelectedPosition = indicator.lastSelectedPosition

		var coordinate = value.coordinate
		var color = unselectedColor

		if (indicator.isInteractiveAnimation) {
			if (position == selectingPosition) {
				coordinate = value.coordinate
				color = selectedColor

			} else if (position == selectedPosition) {
				coordinate = value.coordinateReverse
				color = unselectedColor
			}

		} else {
			if (position == lastSelectedPosition) {
				coordinate = value.coordinate
				color = selectedColor

			} else if (position == selectedPosition) {
				coordinate = value.coordinateReverse
				color = unselectedColor
			}
		}

		paint.color = color
		if (indicator.orientation === Orientation.HORIZONTAL) {
			canvas.drawCircle(coordinate.toFloat(), coordinateY.toFloat(), radius.toFloat(), paint)
		} else {
			canvas.drawCircle(coordinateX.toFloat(), coordinate.toFloat(), radius.toFloat(), paint)
		}
	}
}
