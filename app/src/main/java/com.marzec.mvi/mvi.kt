package com.marzec.mvi

import androidx.lifecycle.*
import com.marzec.cheatday.common.SingleLiveEvent
import java.sql.Statement
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.runningReduce
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
open class StoreViewModel<State : Any, SideEffect>(defaultState: State) : ViewModel() {

    private val sideEffectsInternal = SingleLiveEvent<SideEffect>()
    val sideEffects: LiveData<SideEffect>
        get() = sideEffectsInternal

    private val store = Store(defaultState)

    val state = store.state.asLiveData()

    init {
        viewModelScope.launch { store.init(viewModelScope) }
    }

    fun <Result : Any> intent(buildFun: IntentBuilder<State, Result>.() -> Unit) {
        viewModelScope.launch {
            store.intent(buildFun)
        }
    }

    fun <Result : Any> IntentBuilder<State, Result>.emitSideEffect(
        func: suspend IntentBuilder.IntentContext<State, Result>.() -> SideEffect?
    ): IntentBuilder<State, Result> {
        this.sideEffect {
            func()?.let {
                sideEffectsInternal.postValue(it)
            }
        }
        return this
    }
}

open class Store<State : Any>(defaultState: State) {

    private val actions =
        MutableStateFlow<Intent<State, Any>>(Intent(state = defaultState, result = null))

    private val _intentContextFlow =
        MutableStateFlow<Intent<State, Any>>(Intent(state = defaultState, result = null))

    private var _state = MutableStateFlow(defaultState)

    val state: StateFlow<State>
        get() = _state

    suspend fun init(scope: CoroutineScope) {
        scope.launch {
            actions.flatMapMerge { intent ->
                debug("actions flatMapMerge intent: $intent")
                intent.onTrigger(_state.value).map {
                    debug("actions onTrigger state: ${intent.state} result: $it")
                    intent.copy(result = it, sideEffect = intent.sideEffect)
                }
            }.collect {
                debug("actions collect intent: $it")
                _intentContextFlow.emit(it)
            }
        }
        scope.launch {
            _intentContextFlow
                .runningReduce { old, new ->
                    val reducedState = new.reducer(new.result, old.state!!)
                    debug("_intentContextFlow runningReduce state: $reducedState")
                    old.copy(state = reducedState, result = new.result, sideEffect = new.sideEffect)
                }.onEach {
                    debug("_intentContextFlow onEach intent: $it")
                    onNewState(it.state!!)
                    it.sideEffect?.invoke(it.result, it.state)
                }.collect {
                    debug("_intentContextFlow collect state: ${it.state}")
                    _state.emit(it.state!!)
                }
        }
    }

    suspend fun <Result : Any> intent(buildFun: IntentBuilder<State, Result>.() -> Unit) {
        actions.emit(IntentBuilder<State, Result>().apply { buildFun() }.build())
    }

    open suspend fun onNewState(newState: State) = Unit

    fun debug(string: String) {
        if (false) {
            println(string)
        }
    }
}

data class Intent<State, out Result : Any>(
    val onTrigger: suspend (stateParam: State) -> Flow<Result?> = { _ -> flowOf(null) },
    val reducer: suspend (result: Any?, stateParam: State) -> State = { _, stateParam -> stateParam },
    val sideEffect: (suspend (result: Any?, state: State) -> Unit)? = null,
    val state: State?,
    val result: Result?
) {
    fun resultNonNull(): Result = result!!
}

@Suppress("UNCHECKED_CAST")
class IntentBuilder<State : Any, Result : Any> {

    private var onTrigger: suspend (stateParam: State) -> Flow<Result?> = { _ -> flowOf(null) }
    private var reducer: suspend (result: Any?, stateParam: State) -> State =
        { _, stateParam -> stateParam }
    private var sideEffect: (suspend (result: Any?, state: State) -> Unit)? = null

    fun onTrigger(func: suspend IntentContext<State, Result>.() -> Flow<Result?>): IntentBuilder<State, Result> {
        onTrigger = { state ->
            IntentContext<State, Result>(state, null).func()
        }
        return this
    }

    fun reducer(func: suspend IntentContext<State, Result>.() -> State): IntentBuilder<State, Result> {
        reducer = { result: Any?, state ->
            val res = result as? Result
            IntentContext(state, res).func()
        }
        return this
    }

    fun sideEffect(func: suspend IntentContext<State, Result>.() -> Unit): IntentBuilder<State, Result> {
        sideEffect = { result: Any?, state ->
            val res = result as? Result
            IntentContext(state, res).func()
        }
        return this
    }

    fun build(): Intent<State, Result> = Intent(onTrigger, reducer, sideEffect, null, null)

    data class IntentContext<State, Result>(
        val state: State,
        val result: Result?
    ) {
        fun resultNonNull(): Result = result!!
    }
}