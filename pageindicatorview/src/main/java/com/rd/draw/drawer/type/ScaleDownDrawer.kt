package com.rd.draw.drawer.type

import android.graphics.Canvas
import android.graphics.Paint
import com.rd.animation.data.Value
import com.rd.animation.data.type.ScaleAnimationValue
import com.rd.draw.data.Indicator

class ScaleDownDrawer(paint: Paint, indicator: Indicator) : BaseDrawer(paint, indicator) {

	fun draw(
			canvas: Canvas,
			value: Value,
			position: Int,
			coordinateX: Int,
			coordinateY: Int) {

		if (value !is ScaleAnimationValue) {
			return
		}

		var radius = indicator.radius.toFloat()
		var color = indicator.selectedColor

		val selectedPosition = indicator.selectedPosition
		val selectingPosition = indicator.selectingPosition
		val lastSelectedPosition = indicator.lastSelectedPosition

		if (indicator.isInteractiveAnimation) {
			if (position == selectingPosition) {
				radius = value.radius.toFloat()
				color = value.color

			} else if (position == selectedPosition) {
				radius = value.radiusReverse.toFloat()
				color = value.colorReverse
			}

		} else {
			if (position == selectedPosition) {
				radius = value.radius.toFloat()
				color = value.color

			} else if (position == lastSelectedPosition) {
				radius = value.radiusReverse.toFloat()
				color = value.colorReverse
			}
		}

		paint.color = color
		canvas.drawCircle(coordinateX.toFloat(), coordinateY.toFloat(), radius, paint)
	}
}
