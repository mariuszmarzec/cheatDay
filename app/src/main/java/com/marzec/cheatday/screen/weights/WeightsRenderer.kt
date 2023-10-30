package com.marzec.cheatday.screen.weights

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.marzec.adapterdelegate.adapter.DelegateAdapter
import com.marzec.adapterdelegate.adapter.DelegateManager
import com.marzec.adapterdelegate.model.ListItem
import com.marzec.cheatday.R
import com.marzec.cheatday.extensions.gone
import com.marzec.cheatday.extensions.visible
import com.marzec.cheatday.screen.weights.model.WeightsData
import com.marzec.cheatday.screen.weights.model.WeightsMapper
import com.marzec.cheatday.view.delegates.errorscreen.ErrorScreen
import com.marzec.cheatday.view.delegates.errorscreen.ErrorScreenDelegate
import com.marzec.cheatday.view.delegates.labeledrowitem.LabeledRowAdapterDelegate
import com.marzec.cheatday.view.delegates.progress.ProgressScreen
import com.marzec.cheatday.view.delegates.progress.ProgressScreenDelegate
import com.marzec.mvi.State
import javax.inject.Inject

class WeightsRenderer @Inject constructor(
    private val mapper: WeightsMapper,
) {
    var onClickListener: (String) -> Unit = { }
    var onLongClickListener: (String) -> Unit = { }
    var onFloatingButtonClick: () -> Unit = { }
    var onTryAgainButtonClickListener: () -> Unit = { }

    private val delegateManager = DelegateManager<ListItem>()
        .add(ErrorScreenDelegate())
        .add(LabeledRowAdapterDelegate())
        .add(ProgressScreenDelegate())

    private val adapter = DelegateAdapter(delegateManager)

    private lateinit var recyclerView: RecyclerView
    private lateinit var floatingButton: FloatingActionButton
    private lateinit var progressBar: View

    fun init(view: View) {
        recyclerView = view.findViewById(R.id.recycler_view)
        floatingButton = view.findViewById(R.id.floating_button)
        progressBar = view.findViewById(R.id.progress_bar)

        floatingButton.setOnClickListener {
            onFloatingButtonClick()
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(view.context)
    }

    fun render(state: State<WeightsData>) = when (state) {
        is State.Data -> renderData(state)
        is State.Error -> renderError(state)
        is State.Loading -> renderLoading(state)
    }

    private fun renderData(state: State.Data<WeightsData>) {
        progressBar.gone()
        render(state.data)
    }

    private fun renderError(state: State.Error<WeightsData>) {
        progressBar.gone()
        adapter.items = listOf(errorScreen(state))
    }

    private fun errorScreen(state: State.Error<WeightsData>): ListItem = ErrorScreen(
        id = "ERROR",
        message = state.message,
        buttonLabel = recyclerView.context.getString(R.string.button_try_again)
    ).apply {
        onButtonClickListener = { onTryAgainButtonClickListener() }
    }

    private fun render(data: WeightsData) = with(data) {
        adapter.items = mapper.mapWeights(
            minWeight,
            weekAverage,
            maxPossibleWeight,
            targetWeight,
            weights,
            onClickListener,
            onLongClickListener
        )
    }

    private fun renderLoading(state: State.Loading<WeightsData>) {
        val data = state.data
        if (data != null) {
            progressBar.visible()
            render(data)
        } else {
            adapter.items = listOf(ProgressScreen("PROGRESS"))
        }
    }
}
