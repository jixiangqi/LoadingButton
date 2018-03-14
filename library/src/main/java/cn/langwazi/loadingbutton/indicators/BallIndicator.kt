package cn.langwazi.loadingbutton.indicators

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Paint
import cn.langwazi.loadingbutton.Indicator

/**
 * Created by langwa on 2018/3/14.
 * 跳动的小球.
 */
class BallIndicator : Indicator() {
    private val SCALE = 1.0f
    private val scaleFloats = floatArrayOf(SCALE, SCALE, SCALE)

    private lateinit var paint: Paint

    override fun setIndicatorColor(color: Int) {
        paint = Paint()
        paint.isAntiAlias = true
        paint.color = color
    }

    override fun onDraw(canvas: Canvas, width: Int, height: Int, paddingTop: Int, paddingBottom: Int, paddingLeft: Int, paddingRight: Int) {
        val circleSpacing = 6f
        val radius = (Math.min(width - paddingLeft - paddingRight, height - paddingTop - paddingBottom) - circleSpacing * 2) / 6
        val x = width / 2 - (radius * 2 + circleSpacing)
        val y = (height / 2).toFloat()
        for (i in 0..2) {
            canvas.save()
            val translateX = x + radius * 2 * i + circleSpacing * i
            canvas.translate(translateX, y)
            canvas.scale(scaleFloats[i], scaleFloats[i])
            canvas.drawCircle(0f, 0f, radius, paint)
            canvas.restore()
        }
    }

    override fun onCreateAnimators(): ArrayList<ValueAnimator> {
        for (i in 0..2) {
            scaleFloats[i] = SCALE
        }

        val animators = ArrayList<ValueAnimator>()
        val delays = intArrayOf(120, 240, 360)
        for (i in 0..2) {

            val scaleAnimator = ValueAnimator.ofFloat(1f, 0.3f, 1f)

            scaleAnimator.duration = 750
            scaleAnimator.repeatCount = -1
            scaleAnimator.startDelay = delays[i].toLong()

            scaleAnimator.addUpdateListener({ animation ->
                scaleFloats[i] = animation.animatedValue as Float
                invalidate()
            })

            animators.add(scaleAnimator)
        }
        return animators
    }
}