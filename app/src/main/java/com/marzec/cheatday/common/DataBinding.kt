package com.marzec.cheatday.common

import android.text.Editable
import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import androidx.databinding.InverseBindingAdapter
import com.marzec.cheatday.R
import com.marzec.cheatday.view.CounterView

@BindingMethods(
    value = [
        BindingMethod(
            type = CounterView::class,
            attribute = "cv_day",
            method = "setDayOrHide"
        )
    ]
)
object CounterViewBinding {

    @BindingAdapter("cv_clicked")
    @JvmStatic
    fun setClickedState(counterView: CounterView, isClicked: Boolean) {
        val context = counterView.context
        counterView.setButtonColor(context.getColor(
            if (isClicked) R.color.colorPrimary else R.color.colorAccent)
        )
    }
}

object EditTextBinding {

    @BindingAdapter("android:text")
    @JvmStatic
    fun setText(editText: EditText, text: Editable?) {
        val newValue = text ?: Editable.Factory.getInstance().newEditable("")
        if (editText.text != newValue) {
            editText.text = newValue
        }
    }

    @InverseBindingAdapter(attribute = "android:text", event = "OnTextChanged")
    @JvmStatic
    fun getText(editText: EditText) = editText.text
}