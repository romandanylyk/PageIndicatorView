package com.rd.draw.drawer.type

import android.graphics.Canvas
import android.graphics.Paint
import com.rd.animation.data.Value
import com.rd.animation.data.type.DropAnimationValue
import com.rd.draw.data.Indicator
import com.rd.draw.data.Orientation

class DropDrawer(paint: Paint, indicator: Indicator) : BaseDrawer(paint, indicator) {

	fun draw(
			canvas: Canvas,
			value: Value,
			coordinateX: Int,
			coordinateY: Int) {

		if (value !is DropAnimationValue) {
			return
		}

		val unselectedColor = indicator.unselectedColor
		val selectedColor = indicator.selectedColor
		val radius = indicator.radius.toFloat()

		paint.color = unselectedColor
		canvas.drawCircle(coordinateX.toFloat(), coordinateY.toFloat(), radius, paint)

		paint.color = selectedColor
		if (indicator.orientation === Orientation.HORIZONTAL) {
			canvas.drawCircle(value.width.toFloat(), value.height.toFloat(), value.radius.toFloat(), paint)
		} else {
			canvas.drawCircle(value.height.toFloat(), value.width.toFloat(), value.radius.toFloat(), paint)
		}
	}
}
