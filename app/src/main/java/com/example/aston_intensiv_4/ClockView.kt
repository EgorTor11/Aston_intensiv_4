package com.example.aston_intensiv_4

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import java.lang.Math.cos
import java.lang.Math.sin
import java.util.Calendar

class ClockView @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyle: Int = 0
): View(context, attr, defStyle){

    private var viewHeight: Int = 0
    private var viewWidth: Int = 0
    private var padding: Int = 0
    private var fontSize: Int = 0
    private var handTruncation: Int = 0
    private var hourHandTruncation: Int = 0
    private var numeralSpacing: Int = 0
    private var radius: Int = 0
    private var clockIsInit = false
    private val numbers = intArrayOf(1,2,3,4,5,6,7,8,9,10,11,12)
    private val rect = Rect()
    private val paint = Paint()

    fun initClock() {
        viewHeight = height
        viewWidth = width
        padding = numeralSpacing + 50
        fontSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 13F, resources.displayMetrics).toInt()
        val min = Math.min(viewHeight,viewWidth)
        radius = min / 2 - padding
        handTruncation = min / 20
        hourHandTruncation = min / 7
        clockIsInit = true

    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if(!clockIsInit){
            initClock()
        }
        canvas.drawColor(Color.WHITE)
        drawCircle(canvas)
        drawCenter(canvas)
        drawNumeral(canvas)
        drawHands(canvas)
        postInvalidateDelayed(500)
        invalidate()
    }

    private fun drawNumeral(canvas: Canvas){
        paint.textSize = fontSize.toFloat()
        for(number in numbers) {
            var tmp: String = number.toString()
            paint.getTextBounds(tmp, 0, tmp.length, rect)
            var angle = Math.PI / 6 * (number - 3)
            var x = (viewWidth / 2 + Math.cos(angle) * radius - rect.width())
            var y = (viewHeight / 2 + Math.sin(angle) * radius + rect.height())
            canvas.drawText(tmp, x.toFloat(), y.toFloat(), paint)
        }
    }

    private fun drawCenter(canvas: Canvas){
        paint.style = Paint.Style.FILL
        canvas.drawCircle(viewWidth/ 2F, viewHeight / 2F, 12F, paint)
    }

    private fun drawCircle(canvas: Canvas) {
        paint.reset()
        paint.color = Color.DKGRAY
        paint.strokeWidth = 12F
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
        canvas.drawCircle(viewWidth / 2F,viewHeight / 2F,radius+padding.toFloat() - 10F, paint)
    }

    private fun drawHour(canvas: Canvas, loc: Float){
        paint.reset()
        paint.color = Color.GREEN
        paint.strokeWidth = 26F
        var angle = Math.PI * loc / 30 - Math.PI / 2
        var handRadius = radius - handTruncation * 2
        canvas.drawLine(
            (viewWidth / 2 - cos(angle) * handRadius / 4).toFloat(),
            (viewHeight / 2 - sin(angle) * handRadius / 4).toFloat(),
            (viewWidth / 2 + cos(angle) * handRadius / 2).toFloat(),
            (viewHeight / 2 + sin(angle) * handRadius / 2).toFloat(),
            paint)
    }


    private fun drawMinute(canvas: Canvas, loc: Float){
        paint.reset()
        paint.color = Color.BLUE
        paint.strokeWidth = 16F
        var angle = Math.PI * loc / 30 - Math.PI / 2
        var handRadius = radius - handTruncation
        canvas.drawLine(
            (viewWidth / 2 - cos(angle) * handRadius / 4).toFloat(),
            (viewHeight / 2 - sin(angle) * handRadius / 4).toFloat(),
            (viewWidth / 2 + cos(angle) * handRadius / 1.5).toFloat(),
            (viewHeight / 2 + sin(angle)* handRadius / 1.5).toFloat(),
            paint)
    }

    private fun drawSecond(canvas: Canvas,loc: Float){
        paint.reset()
        paint.color = Color.RED
        paint.strokeWidth = 12F
        var angle = Math.PI * loc / 30 - Math.PI / 2
        var handRadius = radius - handTruncation
        canvas.drawLine(
            (viewWidth / 2 - cos(angle) * handRadius / 4).toFloat(),
            (viewHeight / 2 - sin(angle) * handRadius / 4).toFloat(),
            (viewWidth / 2 + cos(angle) * handRadius  ).toFloat(),
            (viewHeight / 2 + sin(angle) * handRadius ).toFloat(),
            paint)
    }

    private fun drawHands(canvas: Canvas){
        var calendar: Calendar = Calendar.getInstance()
        var hour: Int = calendar.get(Calendar.HOUR_OF_DAY)
        hour = if(hour > 12) hour - 12 else hour
        drawHour(canvas,(hour + calendar.get(Calendar.MINUTE) / 60) * 5F)
        drawMinute(canvas, calendar.get(Calendar.MINUTE).toFloat())
        drawSecond(canvas, calendar.get(Calendar.SECOND).toFloat())
    }
}