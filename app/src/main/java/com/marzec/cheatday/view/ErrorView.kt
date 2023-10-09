package com.marzec.cheatday.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.airbnb.epoxy.ModelProp
import com.airbnb.epoxy.ModelView
import com.airbnb.epoxy.TextProp
import com.airbnb.paris.annotations.Attr
import com.airbnb.paris.annotations.Styleable
import com.airbnb.paris.extensions.style
import com.marzec.cheatday.R
import com.marzec.cheatday.R2

@ModelView(autoLayout = ModelView.Size.MATCH_WIDTH_MATCH_HEIGHT)
@Styleable("ErrorView")
class ErrorView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val errorMessageTextView: TextView
    private val button: Button

    private var onButtonClickListener: () -> Unit = { }

    init {
        LayoutInflater.from(context).inflate(R.layout.view_error, this)

        errorMessageTextView = findViewById(R.id.error_message)
        button = findViewById(R.id.button)

        button.setOnClickListener { onButtonClickListener() }

        style(attrs)
    }

    @Attr(R2.styleable.ErrorView_ev_message)
    @TextProp
    fun setErrorMessage(message: CharSequence) {
        errorMessageTextView.text = message
    }

    @Attr(R2.styleable.ErrorView_ev_button_label)
    @TextProp
    fun setButtonLabel(buttonLabel: CharSequence) {
        button.text = buttonLabel
    }

    @ModelProp(options = [ModelProp.Option.DoNotHash])
    fun setOnButtonClickListener(onButtonClickListener: () -> Unit) {
        this.onButtonClickListener = onButtonClickListener
    }
}
