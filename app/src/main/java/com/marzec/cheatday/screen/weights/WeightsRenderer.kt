package com.marzec.cheatday.screen.weights

import android.view.View
import com.airbnb.epoxy.EpoxyRecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.marzec.cheatday.R
import com.marzec.cheatday.extensions.gone
import com.marzec.cheatday.extensions.visible
import com.marzec.cheatday.screen.weights.model.WeightsData
import com.marzec.cheatday.screen.weights.model.WeightsMapper
import com.marzec.cheatday.view.errorView
import com.marzec.cheatday.view.labeledRowView
import com.marzec.cheatday.view.model.LabeledRowItem
import com.marzec.cheatday.view.progressView
import com.marzec.mvi.State
import javax.inject.Inject

class WeightsRenderer @Inject constructor(
    private val mapper: WeightsMapper,
) {
    var onClickListener: (String) -> Unit = { }
    var onLongClickListener: (String) -> Unit = { }
    var onFloatingButtonClick: () -> Unit = { }
    var onTryAgainButtonClickListener: () -> Unit = { }

    private lateinit var recyclerView: EpoxyRecyclerView
    private lateinit var floatingButton: FloatingActionButton
    private lateinit var progressBar: View

    fun init(view: View) {
        recyclerView = view.findViewById(R.id.recycler_view)
        floatingButton = view.findViewById(R.id.floating_button)
        progressBar = view.findViewById(R.id.progress_bar)

        floatingButton.setOnClickListener {
            onFloatingButtonClick()
        }
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
        recyclerView.withModels {
            errorView {
                id("ERROR")
                errorMessage(state.message)
                buttonLabel(R.string.button_try_again)
                onButtonClickListener { onTryAgainButtonClickListener() }
            }
        }
    }

    private fun render(data: WeightsData) {
        val (min, maxPossible, target, weights) = data
        val list = mapper.mapWeights(min, maxPossible, target, weights)
        recyclerView.withModels {
            list.forEach { item ->
                when (item) {
                    is LabeledRowItem -> {
                        labeledRowView {
                            id(item.id)
                            label(item.label)
                            value(item.value)
                            onClickListener { _, _, _, _ ->
                                onClickListener(item.id)
                            }
                            onLongClickListener { _, _, _, _ ->
                                onLongClickListener(item.id)
                                true
                            }
                        }
                    }
                }
            }
        }
    }

    private fun renderLoading(state: State.Loading<WeightsData>) {
        val data = state.data
        if (data != null) {
            progressBar.visible()
            render(data)
        } else {
            recyclerView.withModels {
                progressView { id("PROGRESS") }
            }
        }
    }
}
