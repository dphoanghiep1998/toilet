package com.neko.hiepdph.toiletseries.common

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.neko.hiepdph.toiletseries.R


open class GradientText(context: Context, attributeSet: AttributeSet?) :
    AppCompatTextView(context, attributeSet) {
    private val mContext: Context
    private val textPaint: Paint
    private var firstColor: Int = 0xFFFFFFFF.toInt()
    private var secondColor: Int = 0xFFFFFFFF.toInt()

    init {
        mContext = context
        textPaint = this.paint

        attributeSet?.let {
            val attributes = context.obtainStyledAttributes(it, R.styleable.GradientText)
            try {
                firstColor =
                    attributes.getColor(R.styleable.GradientText_colorFirst, 0xFFFFFFFF.toInt())
                secondColor =
                    attributes.getColor(R.styleable.GradientText_colorSecond, 0xFFFFFFFF.toInt())
                setShader(firstColor, secondColor)
                this.setTextColor(firstColor)
            } finally {
                attributes.recycle()
            }


        }
    }
    fun reset(){
        firstColor = 0xFFFFFFFF.toInt()
        secondColor= 0xFFFFFFFF.toInt()
        this.setTextColor(firstColor)
        this.setShader(firstColor,secondColor)
    }

    fun setShader(colorFirst: Int, colorSecond: Int) {
        val shader: Shader = LinearGradient(
            0f,
            0f,
            0f,
            this.lineHeight.toFloat(),
            colorFirst,
            colorSecond,
            Shader.TileMode.CLAMP
        )

        textPaint.shader = shader
    }

    fun setShader(colorList: IntArray) {
        val shader: Shader = LinearGradient(
            0f,
            0f,
            0f,
            this.lineHeight.toFloat(),
            colorList,
            null,
            Shader.TileMode.CLAMP
        )
        textPaint.shader = shader
    }

    fun disableGradient() {
        this.setTextColor(Color.WHITE)
        textPaint.shader = null
    }

    fun enabledGradient() {
        val shader: Shader = LinearGradient(
            0f,
            0f,
            0f,
            this.lineHeight.toFloat(),
            firstColor,
            secondColor,
            Shader.TileMode.CLAMP
        )
        textPaint.shader = shader
        this.setTextColor(firstColor)
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }

}