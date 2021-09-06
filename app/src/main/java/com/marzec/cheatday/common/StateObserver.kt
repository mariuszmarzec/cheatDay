package com.marzec.cheatday.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf

@Suppress("unchecked_cast")
interface StateObserver<STATE> {

    fun observeState(state: StateFlow<STATE>, action: (STATE) -> Unit) {
        (testState as? STATE)?.let {
            bindStateObserver(flowOf(it), action)
        } ?: bindStateObserver(state, action)
    }

    fun bindStateObserver(stateFlow: Flow<STATE>, action: (STATE) -> Unit)

    companion object {
        var testState: Any? = null
    }
}