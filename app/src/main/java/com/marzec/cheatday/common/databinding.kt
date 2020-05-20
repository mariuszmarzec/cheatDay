package com.marzec.cheatday.common

import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
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
object CounterViewBinding