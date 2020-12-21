package com.engalshikh.draganddraw


import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View


private const val TAG = "BoxDrawingView"


private const val boxes = "BOXES"
private const val state = "STATE"
class BoxDrawingView(context: Context, attrs: AttributeSet? = null) :
    View(context, attrs) {

    private val boxPaint = Paint().apply {
        color = 0x22ff0000.toInt()
    }
    private val backgroundPaint = Paint().apply {
        color = 0xfff8efe0.toInt()
    }
    private var currentBox: Box? = null
    private var boxen = mutableListOf<Box>()

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val current = PointF(event.x, event.y)
        var action = ""
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                action = "ACTION_DOWN"
                // Reset drawing state
                currentBox = Box(current).also {
                    boxen.add(it)
                }

            }
            MotionEvent.ACTION_MOVE -> {
                action = "ACTION_MOVE"
                updateCurrentBox(current)
            }
            MotionEvent.ACTION_UP -> {
                action = "ACTION_UP"
                updateCurrentBox(current)
                currentBox = null

            }
            MotionEvent.ACTION_CANCEL -> {
                action = "ACTION_CANCEL"
                currentBox = null

            }
        }
        Log.i(TAG, "$action at x=${current.x}, y=${current.y}")
        return true
    }

    private fun updateCurrentBox(current: PointF) {
        currentBox?.let {
            it.end = current
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
// Fill the background
        canvas.drawPaint(backgroundPaint)
        boxen.forEach { box ->
            canvas.drawRect(box.left, box.top, box.right, box.bottom, boxPaint)

        }
    }


    override fun onSaveInstanceState(): Parcelable {
        val state = super.onSaveInstanceState()
        val bundle:Bundle = Bundle()
        bundle.putParcelableArrayList(boxes, ArrayList<Parcelable>(boxen))
        bundle.putParcelable(com.engalshikh.draganddraw.state, state)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state is Bundle){
            boxen = state.getParcelableArrayList<Box>(boxes)?.toMutableList() ?: mutableListOf()
            super.onRestoreInstanceState(state.getParcelable(com.engalshikh.draganddraw.state))
        }
    }

}
