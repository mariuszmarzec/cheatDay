package com.marzec.cheatday.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.paris.annotations.Attr
import com.airbnb.paris.extensions.style
import com.marzec.cheatday.R

class ErrorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val errorMessageTextView: TextView
    private val button: Button

    var onButtonClickListener: () -> Unit = { }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_error, this)

        errorMessageTextView = findViewById(R.id.error_message)
        button = findViewById(R.id.button)

        button.setOnClickListener { onButtonClickListener() }

        style(attrs)
    }

    @Attr(R.styleable.ErrorView_ev_message)
    fun setErrorMessage(message: String) {
        errorMessageTextView.text = message
    }
}