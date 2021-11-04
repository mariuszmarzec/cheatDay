package com.marzec.cheatday.view

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatImageView
import com.airbnb.paris.annotations.Attr
import com.airbnb.paris.annotations.Styleable
import com.airbnb.paris.extensions.style
import com.marzec.cheatday.R
import com.marzec.cheatday.extensions.enumValueOfOrDefault
import com.marzec.cheatday.extensions.gone
import com.marzec.cheatday.extensions.setVisible
import com.marzec.cheatday.extensions.visible
import com.marzec.cheatday.model.domain.Day

@Styleable("CounterView")
class CounterView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    enum class CountMode {
        DECREASE, INCREASE
    }

    private var value = 0
    private var max = 1
    private var titleTextSize = context.resources.getDimensionPixelSize(R.dimen.counter_title_text_size)
    private var mode = CountMode.DECREASE

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
        style(attrs)

        increaseButton.setOnClickListener {
            onIncreaseButtonClickListener?.invoke()
        }
        decreaseButton.setOnClickListener {
            onDecreaseButtonClickListener?.invoke()
        }
    }

    @Attr(R.styleable.CounterView_cv_mode)
    fun setMode(newMode: String) {
        mode = enumValueOfOrDefault(newMode, CountMode.DECREASE)
        drawValue()
        increaseButton.setVisible(mode == CountMode.INCREASE)
        decreaseButton.setVisible(mode == CountMode.DECREASE)
    }

    @Attr(R.styleable.CounterView_cv_title)
    fun setTitle(@StringRes resId: Int) {
        titleTextView.setText(resId)
    }

    @Attr(R.styleable.CounterView_cv_value)
    fun setValue(value: Int) {
        this.value = value
        drawValue()
    }

    @Attr(R.styleable.CounterView_cv_max)
    fun setMax(max: Int) {
        this.max = max
        drawValue()
    }

    @Attr(R.styleable.CounterView_cv_title_text_size)
    fun setTextSize(@Px textSize: Int) {
        this.titleTextSize = textSize
        titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
    }

    @Attr(R.styleable.CounterView_cv_button_color)
    fun setButtonColor(@ColorInt color: Int) {
        increaseButton.setColorFilter(color)
        decreaseButton.setColorFilter(color)
    }

    fun setDay(day: Day) {
        setValue(day.count.toInt())
        setMax(day.max.toInt())
        visible()
    }

    private fun drawValue() {
        valueTextView.text = when (mode) {
            CountMode.DECREASE -> "${this.value}"
            CountMode.INCREASE -> {
                val valueToDraw = this.value%max
                "$valueToDraw/${max}"
            }
        }
    }
}
