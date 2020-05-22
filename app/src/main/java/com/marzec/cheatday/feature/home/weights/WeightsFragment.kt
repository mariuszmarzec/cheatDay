package com.marzec.cheatday.feature.home.weights

import android.os.Bundle
import com.marzec.cheatday.R
import com.marzec.cheatday.common.BaseFragment
import com.marzec.cheatday.feature.home.addnewresult.AddNewWeightResultFragment
import com.marzec.cheatday.view.labeledRowView
import com.marzec.cheatday.view.model.LabeledRowItem
import kotlinx.android.synthetic.main.fragment_weights.*
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

class WeightsFragment : BaseFragment(R.layout.fragment_weights) {

    @Inject
    lateinit var viewModel: WeightsViewModel

    @InternalCoroutinesApi
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.list.observeNonNull { items ->
            recyclerView.withModels {
                items.forEach { item ->
                    when (item) {
                        is LabeledRowItem -> {
                            labeledRowView {
                                id(item.id)
                                label(item.label)
                                value(item.value)
                                onClickListener { _, _, _, _ ->
                                    viewModel.onClick(item.id)
                                }
                            }
                        }
                    }
                }
            }
        }

        viewModel.goToAddResultScreen.observe() {
            replaceFragment<AddNewWeightResultFragment>()
        }

        floatingButton.setOnClickListener {
            viewModel.onFloatingButtonClick()
        }
    }
}