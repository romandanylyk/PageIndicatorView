package com.rd.draw.drawer.type

import android.graphics.Canvas
import android.graphics.Paint
import com.rd.animation.data.Value
import com.rd.animation.data.type.FillAnimationValue
import com.rd.draw.data.Indicator

class FillDrawer(paint: Paint, indicator: Indicator) : BaseDrawer(paint, indicator) {

	private val strokePaint: Paint = Paint()

	init {
		strokePaint.style = Paint.Style.STROKE
		strokePaint.isAntiAlias = true
	}

	fun draw(
			canvas: Canvas,
			value: Value,
			position: Int,
			coordinateX: Int,
			coordinateY: Int) {

		if (value !is FillAnimationValue) {
			return
		}

		var color = indicator.unselectedColor
		var radius = indicator.radius.toFloat()
		var stroke = indicator.stroke

		val selectedPosition = indicator.selectedPosition
		val selectingPosition = indicator.selectingPosition
		val lastSelectedPosition = indicator.lastSelectedPosition

		if (indicator.isInteractiveAnimation) {
			if (position == selectingPosition) {
				color = value.color
				radius = value.radius.toFloat()
				stroke = value.stroke

			} else if (position == selectedPosition) {
				color = value.colorReverse
				radius = value.radiusReverse.toFloat()
				stroke = value.strokeReverse
			}

		} else {
			if (position == selectedPosition) {
				color = value.color
				radius = value.radius.toFloat()
				stroke = value.stroke

			} else if (position == lastSelectedPosition) {
				color = value.colorReverse
				radius = value.radiusReverse.toFloat()
				stroke = value.strokeReverse
			}
		}

		strokePaint.color = color
		strokePaint.strokeWidth = indicator.stroke.toFloat()
		canvas.drawCircle(coordinateX.toFloat(), coordinateY.toFloat(), indicator.radius.toFloat(), strokePaint)

		strokePaint.strokeWidth = stroke.toFloat()
		canvas.drawCircle(coordinateX.toFloat(), coordinateY.toFloat(), radius, strokePaint)
	}
}
