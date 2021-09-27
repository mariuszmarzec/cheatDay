package com.marzec.cheatday.screen.addnewresult

import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import com.marzec.cheatday.R
import com.marzec.cheatday.common.Constants
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

    fun init(weightId: String?, view: View) {
        weightEditText = view.findViewById(R.id.weight_edit_text)
        dateEditText = view.findViewById(R.id.date_edit_text)
        button = view.findViewById(R.id.button)

        dateEditText.setOnClickListener { onDatePickerClick() }

        button.setText(weightId?.let { R.string.common_update } ?: R.string.common_add)

        weightEditText.doOnTextChanged { text, _, _, _ ->
            onNewWeightChanged(text.toString())
        }

        button.setOnClickListener { onButtonClick() }
    }

    fun render(state: State<AddWeightData>) {
        when (state) {
            is State.Data -> {
                val newText = state.data.weight
                val newDate = state.data.date.toString(Constants.DATE_PICKER_PATTERN)
                if (weightEditText.text.toString() != newText) {
                    weightEditText.setText(newText)
                }
                if (dateEditText.text.toString() != newDate) {
                    dateEditText.setText(newDate)
                }
            }
            is State.Error -> {
                // TODO RENDER ERROR
            }
            is State.Loading -> {
                // TODO RENDER LOADING
            }
        }
    }
}
