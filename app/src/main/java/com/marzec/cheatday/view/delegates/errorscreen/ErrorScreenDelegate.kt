package com.marzec.cheatday.view.delegates.errorscreen

import android.view.LayoutInflater
import android.view.ViewGroup
import com.marzec.adapterdelegate.adapter.AdapterDelegate
import com.marzec.adapterdelegate.viewholder.BaseViewHolder
import com.marzec.adapterdelegate.viewholder.PayloadViewHolder
import com.marzec.cheatday.R
import com.marzec.cheatday.view.ErrorView

class ErrorScreenDelegate : AdapterDelegate<Error> {

    override val viewType: Int
        get() {
            return VIEW_TYPE
        }

    override fun onCreateViewHolder(parent: ViewGroup): BaseViewHolder<out Error> {
        return LayoutInflater.from(parent.context)
            .inflate(R.layout.screen_error, parent, false)
            .let {
                ErrorScreenViewHolder(it as ErrorView)
            }
    }

    companion object {
        val VIEW_TYPE = R.layout.screen_error
    }
}

class ErrorScreenViewHolder(
    private val view: ErrorView
) : PayloadViewHolder<Error, Error.Payload>(view) {

    override fun onBind(item: Error) {
        setMessage(item.message)
        setButtonLabel(item.buttonLabel)
        item.onButtonClickListener?.let {
            view.setOnClickListener { it() }
        }
    }

    override fun onPayload(item: Error, payload: Error.Payload) {
        with(payload) {
            message?.let(::setMessage)
            buttonLabel?.let(::setButtonLabel)
        }
    }

    private fun setMessage(message: String) {
        view.setErrorMessage(message)
    }

    fun setButtonLabel(buttonLabel: String) {
        view.setButtonLabel(buttonLabel)
    }
}