package com.marzec.cheatday.screen.addnewresult

import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import com.marzec.cheatday.R
import com.marzec.cheatday.common.Constants
import com.marzec.cheatday.extensions.gone
import com.marzec.cheatday.extensions.visible
import com.marzec.cheatday.screen.addnewresult.model.AddWeightData
import com.marzec.mvi.State
import javax.inject.Inject

class AddNewWeightResultRenderer @Inject constructor() {

    lateinit var onDatePickerClick: () -> Unit
    lateinit var onNewWeightChanged: (String) -> Unit
    lateinit var onButtonClick: () -> Unit

    private lateinit var weightEditText: EditText
    private lateinit var dateEditText: EditText
    private lateinit var button: Button
    private lateinit var progressBar: View
    private lateinit var error: TextView

    fun init(weightId: String?, view: View) {
        weightEditText = view.findViewById(R.id.weight_edit_text)
        dateEditText = view.findViewById(R.id.date_edit_text)
        progressBar = view.findViewById(R.id.progress_bar)
        button = view.findViewById(R.id.button)
        error = view.findViewById(R.id.error)

        dateEditText.setOnClickListener { onDatePickerClick() }

        button.setText(weightId?.let { R.string.common_update } ?: R.string.common_add)

        weightEditText.doOnTextChanged { text, _, _, _ ->
            onNewWeightChanged(text.toString())
        }

        button.setOnClickListener { onButtonClick() }
    }

    fun render(state: State<AddWeightData>) = when (state) {
        is State.Data -> renderData(state)
        is State.Error -> renderError(state)
        is State.Loading -> renderLoading(state)
    }

    private fun renderLoading(state: State<AddWeightData>) {
        state.data?.let {
            rendererData(it)
        }
        error.gone()
        progressBar.visible()
    }

    private fun renderError(state: State.Error<AddWeightData>) {
        state.data?.let {
            rendererData(it)
        }
        progressBar.gone()
        error.visible()
        error.text = state.message
    }

    private fun renderData(state: State.Data<AddWeightData>) {
        rendererData(state.data)
        progressBar.gone()
    }

    private fun rendererData(data: AddWeightData) {
        val newText = data.weight
        val newDate = data.date.toString(Constants.DATE_PICKER_PATTERN)
        if (weightEditText.text.toString() != newText) {
            weightEditText.setText(newText)
        }
        if (dateEditText.text.toString() != newDate) {
            dateEditText.setText(newDate)
        }
    }
}
