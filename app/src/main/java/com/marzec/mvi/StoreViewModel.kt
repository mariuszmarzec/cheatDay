package com.marzec.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

open class StoreViewModel<State : Any, SideEffect>(defaultState: State) : ViewModel() {

    protected val sideEffectsInternal = MutableSharedFlow<SideEffect>()
    val sideEffects: Flow<SideEffect>
        get() = sideEffectsInternal

    private val store = Store4Impl(viewModelScope, defaultState)

    val state
        get() = store.state

    init {
        viewModelScope.launch { store.init() }
    }

    fun <Result : Any> intent(id: String? = null, buildFun: IntentBuilder<State, Result>.() -> Unit) {
        store.intent(id, buildFun)
    }

    fun <Result : Any> IntentBuilder<State, Result>.emitSideEffect(
        func: suspend IntentContext<State, Result>.() -> SideEffect?
    ): IntentBuilder<State, Result> {
        this.sideEffect {
            func()?.let {
                sideEffectsInternal.emit(it)
            }
        }
        return this
    }
}
