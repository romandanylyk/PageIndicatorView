package com.rd.draw

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.Pair
import android.view.MotionEvent
import com.rd.animation.data.Value
import com.rd.draw.controller.AttributeController
import com.rd.draw.controller.DrawController
import com.rd.draw.controller.MeasureController
import com.rd.draw.data.Indicator

class DrawManager {

	private val attributeController: AttributeController
	private val measureController: MeasureController
	private val drawController: DrawController
	private val indicator = Indicator()

	init {
		this.attributeController = AttributeController(indicator)
		this.drawController = DrawController(indicator)
		this.measureController = MeasureController()
	}

	fun indicator(): Indicator {
		return indicator
	}

	fun setClickListener(listener: DrawController.ClickListener?) {
		drawController.setClickListener(listener)
	}

	fun touch(event: MotionEvent?) {
		drawController.touch(event)
	}

	fun updateValue(value: Value?) {
		drawController.updateValue(value)
	}

	fun draw(canvas: Canvas) {
		drawController.draw(canvas)
	}

	fun measureViewSize(widthMeasureSpec: Int, heightMeasureSpec: Int): Pair<Int, Int> {
		return measureController.measureViewSize(indicator, widthMeasureSpec, heightMeasureSpec)
	}

	fun initAttributes(context: Context, attrs: AttributeSet?) {
		attributeController.init(context, attrs)
	}
}
