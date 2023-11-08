package com.marzec.cheatday.screen.dayscounter

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import com.marzec.cheatday.R
import com.marzec.cheatday.common.BaseFragment
import com.marzec.cheatday.common.StateObserver
import com.marzec.cheatday.screen.dayscounter.model.DaysCounterState
import com.marzec.cheatday.screen.dayscounter.model.DaysCounterViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

@AndroidEntryPoint
class DaysCounterFragment : BaseFragment(R.layout.fragment_days_counter), StateObserver<DaysCounterState> {

    private val viewModel: DaysCounterViewModel by viewModels()

    @Inject
    lateinit var renderer: DaysCounterRenderer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        renderer.onDecreaseCheatButtonClickListener = {
            viewModel.onCheatDecreaseClick()
        }
        renderer.onIncreaseDietButtonClickListener = {
            viewModel.onDietIncreaseClick()
        }
        renderer.onIncreaseDaysButtonClickListener = {
            viewModel.onWorkoutIncreaseClick()
        }


        observeState(viewModel.state) { state ->
            renderer.render(state)
        }

        StateObserver.testState ?: viewModel.loading()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.days, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> viewModel.onLogoutClick().run { true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun bindStateObserver(
        stateFlow: Flow<DaysCounterState>,
        action: (DaysCounterState) -> Unit
    ) {
        stateFlow.observe(action)
    }
}
