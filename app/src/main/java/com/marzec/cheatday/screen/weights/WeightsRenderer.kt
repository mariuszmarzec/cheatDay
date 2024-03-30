package com.marzec.cheatday.screen.weights

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.marzec.adapterdelegate.adapter.DelegateAdapter
import com.marzec.adapterdelegate.adapter.DelegateManager
import com.marzec.adapterdelegate.model.ListItem
import com.marzec.cheatday.R
import com.marzec.cheatday.screen.weights.model.WeightsData
import com.marzec.cheatday.screen.weights.model.WeightsMapper
import com.marzec.cheatday.view.delegates.errorscreen.ErrorScreen
import com.marzec.cheatday.view.delegates.errorscreen.ErrorScreenDelegate
import com.marzec.cheatday.view.delegates.labeledrowitem.LabeledRowAdapterDelegate
import com.marzec.cheatday.view.delegates.progress.ProgressScreen
import com.marzec.cheatday.view.delegates.progress.ProgressScreenDelegate
import com.marzec.mvi.State
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class WeightsRenderer @Inject constructor(
    private val mapper: WeightsMapper,
    private val dispatcher: CoroutineDispatcher
) {
    var onClickListener: (String) -> Unit = { }
    var onLongClickListener: (String) -> Unit = { }
    var onFloatingButtonClick: () -> Unit = { }
    var onTryAgainButtonClickListener: () -> Unit = { }

    private val delegateManager by lazy {
        DelegateManager<ListItem>()
            .add(ErrorScreenDelegate())
            .add(ProgressScreenDelegate())
            .add(LabeledRowAdapterDelegate())
    }

    private val adapter by lazy { DelegateAdapter(delegateManager) }

    private lateinit var recyclerView: RecyclerView
    private lateinit var floatingButton: FloatingActionButton

    fun init(view: View) {
        recyclerView = view.findViewById(R.id.recycler_view)
        floatingButton = view.findViewById(R.id.floating_button)

        floatingButton.setOnClickListener {
            onFloatingButtonClick()
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(view.context)
    }

    fun Flow<State<WeightsData>>.mapToUi() = map { state ->
        when (state) {
            is State.Data -> renderData(state)
            is State.Error -> renderError(state)
            is State.Loading -> renderLoading()
        }
    }.flowOn(dispatcher)

    fun render(uiItems: List<ListItem>) {
        adapter.items = uiItems
    }

    private suspend fun renderData(state: State.Data<WeightsData>): List<ListItem> =
        render(state.data)

    private fun renderError(state: State.Error<WeightsData>): List<ListItem> {
        return listOf(errorScreen(state))
    }

    private fun errorScreen(state: State.Error<WeightsData>): ListItem = ErrorScreen(
        id = "ERROR",
        message = state.message,
        buttonLabel = recyclerView.context.getString(R.string.button_try_again)
    ).apply {
        onButtonClickListener = { onTryAgainButtonClickListener() }
    }

    private suspend fun render(data: WeightsData): List<ListItem> = data.mapData()

    private fun renderLoading(): List<ListItem> = listOf(ProgressScreen("LOADING"))

    private suspend fun WeightsData.mapData() = mapper.mapWeights(
        minWeight,
        weekAverage,
        maxPossibleWeight,
        targetWeight,
        weights,
        onClickListener,
        onLongClickListener
    )
}
