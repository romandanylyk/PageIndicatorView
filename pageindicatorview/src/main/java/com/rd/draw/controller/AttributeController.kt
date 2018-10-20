package com.rd.draw.controller

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import com.rd.animation.type.*
import com.rd.draw.data.Indicator
import com.rd.draw.data.Orientation
import com.rd.draw.data.RtlMode
import com.rd.pageindicatorview.R
import com.rd.utils.DensityUtils

class AttributeController(private val indicator: Indicator) {

	companion object {
		private const val DEFAULT_IDLE_DURATION = 3000
	}

	fun init(context: Context, attrs: AttributeSet?) {
		val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PageIndicatorView, 0, 0)
		initCountAttribute(typedArray)
		initColorAttribute(typedArray)
		initAnimationAttribute(typedArray)
		initSizeAttribute(typedArray)
		typedArray.recycle()
	}

	private fun initCountAttribute(typedArray: TypedArray) {
		val viewPagerId = typedArray.getResourceId(R.styleable.PageIndicatorView_piv_viewPager, View.NO_ID)
		val autoVisibility = typedArray.getBoolean(R.styleable.PageIndicatorView_piv_autoVisibility, true)
		val dynamicCount = typedArray.getBoolean(R.styleable.PageIndicatorView_piv_dynamicCount, false)
		var count = typedArray.getInt(R.styleable.PageIndicatorView_piv_count, Indicator.COUNT_NONE)

		if (count == Indicator.COUNT_NONE) {
			count = Indicator.DEFAULT_COUNT
		}

		var position = typedArray.getInt(R.styleable.PageIndicatorView_piv_select, 0)
		if (position < 0) {
			position = 0
		} else if (count > 0 && position > count - 1) {
			position = count - 1
		}

		indicator.viewPagerId = viewPagerId
		indicator.isAutoVisibility = autoVisibility
		indicator.isDynamicCount = dynamicCount
		indicator.count = count

		indicator.selectedPosition = position
		indicator.selectingPosition = position
		indicator.lastSelectedPosition = position
	}

	private fun initColorAttribute(typedArray: TypedArray) {
		val unselectedColor = typedArray.getColor(R.styleable.PageIndicatorView_piv_unselectedColor, Color.parseColor(ColorAnimation.DEFAULT_UNSELECTED_COLOR))
		val selectedColor = typedArray.getColor(R.styleable.PageIndicatorView_piv_selectedColor, Color.parseColor(ColorAnimation.DEFAULT_SELECTED_COLOR))

		indicator.unselectedColor = unselectedColor
		indicator.selectedColor = selectedColor
	}

	private fun initAnimationAttribute(typedArray: TypedArray) {
		val interactiveAnimation = typedArray.getBoolean(R.styleable.PageIndicatorView_piv_interactiveAnimation, false)
		var animationDuration = typedArray.getInt(R.styleable.PageIndicatorView_piv_animationDuration, BaseAnimation.DEFAULT_ANIMATION_TIME).toLong()
		if (animationDuration < 0) {
			animationDuration = 0
		}

		val animIndex = typedArray.getInt(R.styleable.PageIndicatorView_piv_animationType, AnimationType.NONE.ordinal)
		val animationType = getAnimationType(animIndex)

		val rtlIndex = typedArray.getInt(R.styleable.PageIndicatorView_piv_rtl_mode, RtlMode.Off.ordinal)
		val rtlMode = getRtlMode(rtlIndex)

		val fadeOnIdle = typedArray.getBoolean(R.styleable.PageIndicatorView_piv_fadeOnIdle, false)
		val idleDuration = typedArray.getInt(R.styleable.PageIndicatorView_piv_idleDuration, DEFAULT_IDLE_DURATION).toLong()

		indicator.animationDuration = animationDuration
		indicator.isInteractiveAnimation = interactiveAnimation
		indicator.animationType = animationType
		indicator.rtlMode = rtlMode
		indicator.isFadeOnIdle = fadeOnIdle
		indicator.idleDuration = idleDuration
	}

	private fun initSizeAttribute(typedArray: TypedArray) {
		val orientationIndex = typedArray.getInt(R.styleable.PageIndicatorView_piv_orientation, Orientation.HORIZONTAL.ordinal)
		val orientation: Orientation

		if (orientationIndex == 0) {
			orientation = Orientation.HORIZONTAL
		} else {
			orientation = Orientation.VERTICAL
		}

		var radius = typedArray.getDimension(R.styleable.PageIndicatorView_piv_radius, DensityUtils.dpToPx(Indicator.DEFAULT_RADIUS_DP).toFloat()).toInt()
		if (radius < 0) radius = 0

		var padding = typedArray.getDimension(R.styleable.PageIndicatorView_piv_padding, DensityUtils.dpToPx(Indicator.DEFAULT_PADDING_DP).toFloat()).toInt()
		if (padding < 0) padding = 0

		var scaleFactor = typedArray.getFloat(R.styleable.PageIndicatorView_piv_scaleFactor, ScaleAnimation.DEFAULT_SCALE_FACTOR)
		if (scaleFactor < ScaleAnimation.MIN_SCALE_FACTOR) {
			scaleFactor = ScaleAnimation.MIN_SCALE_FACTOR

		} else if (scaleFactor > ScaleAnimation.MAX_SCALE_FACTOR) {
			scaleFactor = ScaleAnimation.MAX_SCALE_FACTOR
		}

		var stroke = typedArray.getDimension(R.styleable.PageIndicatorView_piv_strokeWidth, DensityUtils.dpToPx(FillAnimation.DEFAULT_STROKE_DP).toFloat()).toInt()
		if (stroke > radius) {
			stroke = radius
		}

		if (indicator.animationType != AnimationType.FILL) {
			stroke = 0
		}

		indicator.radius = radius
		indicator.orientation = orientation
		indicator.padding = padding
		indicator.scaleFactor = scaleFactor
		indicator.stroke = stroke
	}

	private fun getAnimationType(index: Int): AnimationType {
		return when (index) {
			AnimationType.NONE.ordinal -> AnimationType.NONE
			AnimationType.COLOR.ordinal -> AnimationType.COLOR
			AnimationType.SCALE.ordinal -> AnimationType.SCALE
			AnimationType.WORM.ordinal -> AnimationType.WORM
			AnimationType.SLIDE.ordinal -> AnimationType.SLIDE
			AnimationType.FILL.ordinal -> AnimationType.FILL
			AnimationType.THIN_WORM.ordinal -> AnimationType.THIN_WORM
			AnimationType.DROP.ordinal -> AnimationType.DROP
			AnimationType.SWAP.ordinal -> AnimationType.SWAP
			AnimationType.SCALE_DOWN.ordinal -> AnimationType.SCALE_DOWN
			else -> AnimationType.NONE
		}
	}

	private fun getRtlMode(index: Int): RtlMode {
		return when (index) {
			RtlMode.On.ordinal -> RtlMode.On
			RtlMode.Off.ordinal -> RtlMode.Off
			RtlMode.Auto.ordinal -> RtlMode.Auto
			else -> RtlMode.Auto
		}
	}
}
