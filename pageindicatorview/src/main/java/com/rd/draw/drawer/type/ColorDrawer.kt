package com.rd.draw.drawer.type

import android.graphics.Canvas
import android.graphics.Paint
import com.rd.animation.data.Value
import com.rd.animation.data.type.ColorAnimationValue
import com.rd.draw.data.Indicator

class ColorDrawer(paint: Paint, indicator: Indicator) : BaseDrawer(paint, indicator) {

	fun draw(canvas: Canvas,
	         value: Value,
	         position: Int,
	         coordinateX: Int,
	         coordinateY: Int) {

		if (value !is ColorAnimationValue) {
			return
		}

		val radius = indicator.radius.toFloat()
		var color = indicator.selectedColor

		val selectedPosition = indicator.selectedPosition
		val selectingPosition = indicator.selectingPosition
		val lastSelectedPosition = indicator.lastSelectedPosition

		if (indicator.isInteractiveAnimation) {
			if (position == selectingPosition) {
				color = value.color

			} else if (position == selectedPosition) {
				color = value.colorReverse
			}

		} else {
			if (position == selectedPosition) {
				color = value.color

			} else if (position == lastSelectedPosition) {
				color = value.colorReverse
			}
		}

		paint.color = color
		canvas.drawCircle(coordinateX.toFloat(), coordinateY.toFloat(), radius, paint)
	}
}
