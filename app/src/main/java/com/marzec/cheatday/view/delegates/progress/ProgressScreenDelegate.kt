package com.marzec.cheatday.view.delegates.progress

import android.view.LayoutInflater
import android.view.ViewGroup
import com.marzec.adapterdelegate.adapter.AdapterDelegate
import com.marzec.adapterdelegate.viewholder.BaseViewHolder
import com.marzec.cheatday.R
import com.marzec.cheatday.view.ErrorView
import com.marzec.cheatday.view.ProgressView

class ProgressScreenDelegate : AdapterDelegate<ProgressScreen> {

    override val viewType: Int = VIEW_TYPE

    override fun onCreateViewHolder(parent: ViewGroup): BaseViewHolder<out ProgressScreen> {
        return LayoutInflater.from(parent.context)
            .inflate(R.layout.screen_progress, parent, false)
            .let {
                ProgressScreenViewHolder(it as ProgressView)
            }
    }

    companion object {
        val VIEW_TYPE = R.layout.screen_progress
    }
}

class ProgressScreenViewHolder(view: ProgressView) : BaseViewHolder<ProgressScreen>(view) {

    override fun onBind(item: ProgressScreen) = Unit
    override fun onPayload(item: ProgressScreen, payloads: List<Any>) = Unit
}