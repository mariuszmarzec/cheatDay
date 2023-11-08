package com.marzec.cheatday.view.delegates.errorscreen

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.marzec.cheatday.R

class ErrorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var errorMessage = ""
        set(newValue) {
            errorMessage(newValue)
            field = newValue
        }

    var butttonLabel = ""
        set(newValue) {
            butttonLabel(newValue)
            field = newValue
        }


    private val errorMessageTextView: TextView
    private val button: Button

    private var onButtonClickListener: () -> Unit = { }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_error, this)

        errorMessageTextView = findViewById(R.id.error_message)
        button = findViewById(R.id.button)

        button.setOnClickListener { onButtonClickListener() }

        style(context, attrs, defStyleAttr)
    }

    private fun style(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ErrorView,
            defStyleAttr,
            0
        ).apply {
            try {
                errorMessage = getString(R.styleable.ErrorView_ev_message).orEmpty()
                butttonLabel = getString(R.styleable.ErrorView_ev_button_label).orEmpty()
            } finally {
                recycle()
            }
        }
    }

    private fun errorMessage(message: String) {
        errorMessageTextView.text = message
    }

    private fun butttonLabel(buttonLabel: String) {
        button.text = buttonLabel
    }

    fun setOnButtonClickListener(onButtonClickListener: () -> Unit) {
        this.onButtonClickListener = onButtonClickListener
    }
}
