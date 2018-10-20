package com.rd.draw.drawer.type

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.rd.animation.data.Value
import com.rd.animation.data.type.WormAnimationValue
import com.rd.draw.data.Indicator
import com.rd.draw.data.Orientation

open class WormDrawer(paint: Paint, indicator: Indicator) : BaseDrawer(paint, indicator) {

	var rect: RectF = RectF()

	open fun draw(
			canvas: Canvas,
			value: Value,
			coordinateX: Int,
			coordinateY: Int) {

		if (value !is WormAnimationValue) {
			return
		}

		val rectStart = value.rectStart
		val rectEnd = value.rectEnd

		val radius = indicator.radius
		val unselectedColor = indicator.unselectedColor
		val selectedColor = indicator.selectedColor

		if (indicator.orientation === Orientation.HORIZONTAL) {
			rect.left = rectStart.toFloat()
			rect.right = rectEnd.toFloat()
			rect.top = (coordinateY - radius).toFloat()
			rect.bottom = (coordinateY + radius).toFloat()

		} else {
			rect.left = (coordinateX - radius).toFloat()
			rect.right = (coordinateX + radius).toFloat()
			rect.top = rectStart.toFloat()
			rect.bottom = rectEnd.toFloat()
		}

		paint.color = unselectedColor
		canvas.drawCircle(coordinateX.toFloat(), coordinateY.toFloat(), radius.toFloat(), paint)

		paint.color = selectedColor
		canvas.drawRoundRect(rect, radius.toFloat(), radius.toFloat(), paint)
	}
}
