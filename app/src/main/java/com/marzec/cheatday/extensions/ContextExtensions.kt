package com.marzec.cheatday.extensions

import android.content.Context
import android.content.res.Resources
import android.text.InputType
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import androidx.annotation.AttrRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.marzec.cheatday.R

fun Context.showInputDialog(dialogInputOptions: DialogInputOptions): Unit = with(dialogInputOptions) {
    val editText = EditText(this@showInputDialog).apply {
        inputType = inputType
    }
    val dialog: AlertDialog = AlertDialog.Builder(this@showInputDialog)
        .setTitle(title)
        .setMessage(message)
        .setView(editText)
        .setPositiveButton(
            positiveButton
        ) { dialog, _ ->
            val text = editText.text.toString()
            dialog.dismiss()
            onInputText(text)
        }
        .setNegativeButton(negativeButton) { dialog, _ ->
            dialog.dismiss()
        }
        .create()
    dialog.show()
}

fun Context.showAnswerDialog(dialogOptions: DialogOptions): Unit = with(dialogOptions) {
    val dialog: AlertDialog = AlertDialog.Builder(this@showAnswerDialog)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(
            positiveButton
        ) { dialog, _ ->
            dialog.dismiss()
            onPositiveButtonClicked()
        }
        .setNegativeButton(negativeButton) { dialog, _ ->
            dialog.dismiss()
        }
        .create()
    dialog.show()
}

fun Context.showErrorDialog(
    @StringRes titleResId: Int = R.string.dialog_error_title_common,
    @StringRes messageResId: Int = R.string.dialog_error_message_try_later
) {
    AlertDialog.Builder(this).apply {
        setTitle(titleResId)
        setMessage(messageResId)
        setCancelable(true)
        setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
        show()
    }
}

data class DialogInputOptions(
    val title: String,
    val message: String,
    val positiveButton: String,
    val negativeButton: String,
    val inputType: Int = InputType.TYPE_CLASS_TEXT,
    val onInputText: (String) -> Unit = {}
)

data class DialogOptions(
    val title: String,
    val message: String,
    val positiveButton: String,
    val negativeButton: String,
    val onPositiveButtonClicked: () -> Unit = {}
)

fun Resources.Theme.attr(@AttrRes attribute: Int): TypedValue {
    val typedValue = TypedValue()
    if (!resolveAttribute(attribute, typedValue, true)) {
        throw IllegalArgumentException("Failed to resolve attribute: $attribute")
    }

    return typedValue
}

fun Context.attr(@AttrRes attribute: Int): TypedValue = theme.attr(attribute)
