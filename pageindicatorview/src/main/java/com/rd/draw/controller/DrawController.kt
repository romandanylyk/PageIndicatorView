package com.rd.draw.controller

import android.graphics.Canvas
import android.view.MotionEvent
import com.rd.animation.data.Value
import com.rd.animation.type.AnimationType
import com.rd.draw.data.Indicator
import com.rd.draw.drawer.Drawer
import com.rd.utils.CoordinatesUtils

class DrawController(private val indicator: Indicator) {

	private var value: Value? = null
	private val drawer = Drawer(indicator)
	private var listener: ClickListener? = null

	interface ClickListener {

		fun onIndicatorClicked(position: Int)
	}

	fun updateValue(value: Value?) {
		this.value = value
	}

	fun setClickListener(listener: ClickListener?) {
		this.listener = listener
	}

	fun touch(event: MotionEvent?) {
		if (event == null) {
			return
		}

		when (event.action) {
			MotionEvent.ACTION_UP -> onIndicatorTouched(event.x, event.y)
		}
	}

	private fun onIndicatorTouched(x: Float, y: Float) {
		if (listener != null) {
			val position = CoordinatesUtils.getPosition(indicator, x, y)
			if (position >= 0) {
				listener!!.onIndicatorClicked(position)
			}
		}
	}

	fun draw(canvas: Canvas) {
		val count = indicator.count

		for (position in 0 until count) {
			val coordinateX = CoordinatesUtils.getXCoordinate(indicator, position)
			val coordinateY = CoordinatesUtils.getYCoordinate(indicator, position)
			drawIndicator(canvas, position, coordinateX, coordinateY)
		}
	}

	private fun drawIndicator(
			canvas: Canvas,
			position: Int,
			coordinateX: Int,
			coordinateY: Int) {

		val interactiveAnimation = indicator.isInteractiveAnimation
		val selectedPosition = indicator.selectedPosition
		val selectingPosition = indicator.selectingPosition
		val lastSelectedPosition = indicator.lastSelectedPosition

		val selectedItem = !interactiveAnimation && (position == selectedPosition || position == lastSelectedPosition)
		val selectingItem = interactiveAnimation && (position == selectedPosition || position == selectingPosition)
		val isSelectedItem = selectedItem or selectingItem
		drawer.setup(position, coordinateX, coordinateY)

		if (value != null && isSelectedItem) {
			drawWithAnimation(canvas)
		} else {
			drawer.drawBasic(canvas, isSelectedItem)
		}
	}

	private fun drawWithAnimation(canvas: Canvas) {
		val animationType = indicator.animationType
		when (animationType) {
			AnimationType.NONE -> drawer.drawBasic(canvas, true)

			AnimationType.COLOR -> drawer.drawColor(canvas, value!!)

			AnimationType.SCALE -> drawer.drawScale(canvas, value!!)

			AnimationType.WORM -> drawer.drawWorm(canvas, value!!)

			AnimationType.SLIDE -> drawer.drawSlide(canvas, value!!)

			AnimationType.FILL -> drawer.drawFill(canvas, value!!)

			AnimationType.THIN_WORM -> drawer.drawThinWorm(canvas, value!!)

			AnimationType.DROP -> drawer.drawDrop(canvas, value!!)

			AnimationType.SWAP -> drawer.drawSwap(canvas, value!!)

			AnimationType.SCALE_DOWN -> drawer.drawScaleDown(canvas, value!!)
		}
	}
}
