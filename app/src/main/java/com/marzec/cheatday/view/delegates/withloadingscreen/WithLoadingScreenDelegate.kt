package com.marzec.cheatday.view.delegates.withloadingscreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.marzec.adapterdelegate.adapter.AdapterDelegate
import com.marzec.adapterdelegate.adapter.DelegateManager
import com.marzec.adapterdelegate.model.ListItem
import com.marzec.adapterdelegate.viewholder.BaseViewHolder
import com.marzec.adapterdelegate.viewholder.PayloadViewHolder
import com.marzec.cheatday.R
import com.marzec.cheatday.databinding.ScreenWithLoadingBinding

class WithLoadingScreenDelegate(
    private val recycledViewPool: RecycledViewPool,
    private val delegateManager: DelegateManager<ListItem>
) : AdapterDelegate<WithLoadingScreen> {

    override val viewType: Int = VIEW_TYPE
    override fun onCreateViewHolder(parent: ViewGroup): BaseViewHolder<WithLoadingScreen> =
        WithLoadingScreenViewHolder(
            ScreenWithLoadingBinding.inflate(LayoutInflater.from(parent.context)).withLoading,
            recycledViewPool,
            delegateManager
        )

    companion object {
        val VIEW_TYPE = R.layout.screen_with_loading
    }
}

class WithLoadingScreenViewHolder(
    private val view: WithLoadingView,
    recycledViewPool: RecycledViewPool,
    delegateManager: DelegateManager<ListItem>
) : PayloadViewHolder<WithLoadingScreen, WithLoadingScreen.Payload>(view) {

    init {
        view.setCommonRecycledViewPool(recycledViewPool)
        view.delegateManager = delegateManager
    }

    override fun onBind(item: WithLoadingScreen) {
        setItems(item.items)
        setShowOverflowLoading(item.showOverflowLoading)
    }

    override fun onPayload(item: WithLoadingScreen, payload: WithLoadingScreen.Payload) {
        with(payload) {
            items?.let(::setItems)
            showOverflowLoading?.let(::setShowOverflowLoading)
        }
    }

    private fun setItems(items: List<ListItem>) {
        view.items = items
    }

    private fun setShowOverflowLoading(showOverflowLoading: Boolean) {
        view.showOverflowLoading = showOverflowLoading
    }
}