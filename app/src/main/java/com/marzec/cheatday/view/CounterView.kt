package com.marzec.cheatday.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.appcompat.widget.AppCompatImageView
import com.marzec.cheatday.R
import com.marzec.cheatday.extensions.enumValueOfOrDefault
import com.marzec.cheatday.extensions.setVisible
import com.marzec.cheatday.extensions.visible
import com.marzec.cheatday.model.domain.Day

class CounterView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val defaultFontSize = context.resources.getDimensionPixelSize(R.dimen.counter_title_text_size)

    enum class CountMode {
        DECREASE, INCREASE
    }

    var title = ""
        set(newValue) {
            title(newValue)
            field = newValue
        }

    var value = 0
        set(newValue) {
            value()
            field = newValue
        }

    var max = 1
        set(newValue) {
            max()
            field = newValue
        }

    @Px
    var titleTextSize = defaultFontSize
        set(newValue) {
            textSize(newValue)
            field = newValue
        }

    var mode = CountMode.DECREASE
        set(newValue) {
            mode()
            field = newValue
        }

    @ColorInt
    var buttonColor: Int = Color.BLACK
        set(newValue) {
            buttonColor(newValue)
            field = newValue
        }

    private val decreaseButton: AppCompatImageView
    private val increaseButton: AppCompatImageView
    private val titleTextView: TextView
    private val valueTextView: TextView

    var onDecreaseButtonClickListener: (() -> Unit)? = null
    var onIncreaseButtonClickListener: (() -> Unit)? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_counter, this)

        decreaseButton = findViewById(R.id.decrease_button)
        increaseButton = findViewById(R.id.increase_button)
        titleTextView = findViewById(R.id.title_text_view)
        valueTextView = findViewById(R.id.value_text_view)

        orientation = VERTICAL
        gravity = Gravity.CENTER_HORIZONTAL

        increaseButton.setOnClickListener {
            onIncreaseButtonClickListener?.invoke()
        }
        decreaseButton.setOnClickListener {
            onDecreaseButtonClickListener?.invoke()
        }
        style(context, attrs, defStyleAttr)
    }

    private fun style(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CounterView,
            defStyleAttr,
            0
        ).apply {
            try {
                mode = enumValueOfOrDefault(getString(R.styleable.CounterView_cv_mode).orEmpty(), CountMode.DECREASE)
                title = getString(R.styleable.CounterView_cv_title).orEmpty()
                value = getInt(R.styleable.CounterView_cv_value, 0)
                max = getInt(R.styleable.CounterView_cv_max, 0)
                buttonColor = getColor(R.styleable.CounterView_cv_button_color, Color.BLACK)
                titleTextSize = getDimensionPixelSize(
                    R.styleable.CounterView_cv_title_text_size,
                    defaultFontSize
                )
            } finally {
                recycle()
            }
        }
    }

    private fun mode() {
        drawValue()
    }

    private fun title(title: String) {
        titleTextView.text = title
    }

    private fun value() {
        drawValue()
    }

    private fun max() {
        drawValue()
    }

    private fun textSize(@Px textSize: Int) {
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
    }

    private fun buttonColor(@ColorInt color: Int) {
        increaseButton.setColorFilter(color)
        decreaseButton.setColorFilter(color)
    }

    fun setDay(day: Day) {
        value = day.count.toInt()
        max = day.max.toInt()
        visible()
    }

    private fun drawValue() {
        increaseButton.setVisible(mode == CountMode.INCREASE)
        decreaseButton.setVisible(mode == CountMode.DECREASE)

        valueTextView.text = when (mode) {
            CountMode.DECREASE -> "${this.value}"
            CountMode.INCREASE -> {
                val valueToDraw = this.value % max
                "$valueToDraw/$max"
            }
        }
    }
}
