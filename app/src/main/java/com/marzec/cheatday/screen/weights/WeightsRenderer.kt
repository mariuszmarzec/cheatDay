package com.marzec.cheatday.screen.weights

import android.view.View
import com.airbnb.epoxy.EpoxyRecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.marzec.cheatday.R
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

    fun init(view: View) {
        recyclerView = view.findViewById(R.id.recycler_view)
        floatingButton = view.findViewById(R.id.floating_button)

        floatingButton.setOnClickListener {
            onFloatingButtonClick()
        }
    }

    fun render(state: State<WeightsData>) {
        when (state) {
            is State.Data -> {
                val (min, maxPossible, target, weights) = state.data
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
            is State.Error -> {
                recyclerView.withModels {
                    errorView {
                        id("ERROR")
                        errorMessage(state.message)
                        buttonLabel(R.string.button_try_again)
                        onButtonClickListener { onTryAgainButtonClickListener() }
                    }
                }

            }
            is State.Loading -> {
                recyclerView.withModels {
                    progressView { id("PROGRESS") }
                }
            }
        }
    }
}