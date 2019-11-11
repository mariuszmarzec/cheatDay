package com.marzec.cheatday.view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.annotation.StringRes
import com.airbnb.paris.annotations.Attr
import com.airbnb.paris.annotations.Styleable
import com.airbnb.paris.extensions.style
import com.marzec.cheatday.R
import com.marzec.cheatday.extensions.enumValueOfOrDefault
import com.marzec.cheatday.extensions.setVisible
import kotlinx.android.synthetic.main.view_counter.view.*

@Styleable("CounterView")
class CounterView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    enum class CountMode {
        DECREASE, INCREASE
    }

    private var mode = CountMode.DECREASE
    private var value = 0
    private var max = 1

    var onDecreaseButtonClickListener: ((Int) -> Unit)? = null
    var onIncreaseButtonClickListener: ((Int, Int) -> Unit)? = null

    init {
        inflate(getContext(), R.layout.view_counter, this)
        style(attrs)

        increaseButton.setOnClickListener {
            onIncreaseButtonClickListener?.invoke(value, max)
        }
        decreaseButton.setOnClickListener {
            onDecreaseButtonClickListener?.invoke(value)
        }
    }

    @Attr(R.styleable.CounterView_mode)
    fun setMode(newMode: String) {
        mode = enumValueOfOrDefault(newMode, CountMode.DECREASE)
        drawValue()
        increaseButton.setVisible(mode == CountMode.INCREASE, true)
        decreaseButton.setVisible(mode == CountMode.DECREASE, true)
    }

    @Attr(R.styleable.CounterView_cv_title)
    fun setTitle(@StringRes resId: Int) {
        titleTextView.setText(resId)
    }

    @Attr(R.styleable.CounterView_value)
    fun setValue(value: Int) {
        this.value = value
        drawValue()
    }

    @Attr(R.styleable.CounterView_max)
    fun setMax(max: Int) {
        this.max = max
        drawValue()
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