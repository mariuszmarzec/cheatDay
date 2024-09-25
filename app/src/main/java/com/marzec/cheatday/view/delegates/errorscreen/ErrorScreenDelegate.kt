package com.marzec.cheatday.view.delegates.errorscreen

import android.view.LayoutInflater
import android.view.ViewGroup
import com.marzec.adapterdelegate.adapter.AdapterDelegate
import com.marzec.adapterdelegate.viewholder.BaseViewHolder
import com.marzec.adapterdelegate.viewholder.PayloadViewHolder
import com.marzec.cheatday.R

class ErrorScreenDelegate : AdapterDelegate<ErrorScreen> {

    override val viewType: Int
        get() {
            return VIEW_TYPE
        }

    override fun onCreateViewHolder(parent: ViewGroup): BaseViewHolder<out ErrorScreen> {
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
) : PayloadViewHolder<ErrorScreen, ErrorScreen.Payload>(view) {

    override fun onBind(item: ErrorScreen) {
        setMessage(item.message)
        setButtonLabel(item.buttonLabel)
        item.onButtonClickListener?.let {
            view.setOnClickListener { it() }
        }
    }

    override fun onPayload(item: ErrorScreen, payload: ErrorScreen.Payload) {
        with(payload) {
            message?.let(::setMessage)
            buttonLabel?.let(::setButtonLabel)
        }
    }

    private fun setMessage(message: String) {
        view.errorMessage = message
    }

    private fun setButtonLabel(buttonLabel: String) {
        view.butttonLabel = buttonLabel
    }
}
