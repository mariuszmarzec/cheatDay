package com.marzec.cheatday.view.delegates.withloadingscreen

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marzec.adapterdelegate.adapter.DelegateAdapter
import com.marzec.adapterdelegate.adapter.DelegateManager
import com.marzec.adapterdelegate.model.ListItem
import com.marzec.cheatday.databinding.ViewWithLoadingBinding
import com.marzec.cheatday.extensions.findParentInstance

class WithLoadingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding: ViewWithLoadingBinding =
        ViewWithLoadingBinding.inflate(LayoutInflater.from(context), this)

    private var delegateAdapter: DelegateAdapter<ListItem> = DelegateAdapter(DelegateManager())

    var delegateManager: DelegateManager<ListItem> = DelegateManager()
        set(value) {
            field = value
            delegateManager(value)
        }

    var items: List<ListItem> = listOf()
        set(value) {
            field = value
            items(items)
        }

    var showOverflowLoading: Boolean = false
        set(value) {
            field = value
            showOverflowLoading(value)
        }

    init {
        with(binding.nestedRecyclerView) {
            layoutManager = LinearLayoutManager(context)
            setOnTouchListener { view, _ -> onTouch(view) }
        }
    }

    private fun onTouch(view: View): Boolean {
        view.findParentInstance<RecyclerView>()
            .requestDisallowInterceptTouchEvent(true)
        return false
    }

    private fun showOverflowLoading(showOverflowLoading: Boolean) {
        binding.loading.progressBar.isVisible = showOverflowLoading
    }

    private fun delegateManager(delegateManager: DelegateManager<ListItem>) {
        this.delegateAdapter = DelegateAdapter(delegateManager)
        binding.nestedRecyclerView.adapter = delegateAdapter
    }

    private fun items(items: List<ListItem>) {
        delegateAdapter.items = items
    }

    fun setCommonRecycledViewPool(recycledViewPool: RecyclerView.RecycledViewPool) {
        binding.nestedRecyclerView.setRecycledViewPool(recycledViewPool)
    }
}
