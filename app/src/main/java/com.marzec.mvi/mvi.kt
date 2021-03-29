package com.marzec.mvi

import androidx.lifecycle.*
import kotlin.reflect.KClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.consume
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
open class StoreViewModel<State, Action : Any, SideEffect>(defaultState: State) : ViewModel() {

    protected var intents = mapOf<KClass<out Action>, Intent<State, SideEffect>>()

    private val sideEffectsInternal = MutableLiveData<SideEffect>()

    val sideEffects: LiveData<SideEffect>
        get() = sideEffectsInternal

    private val actions = BroadcastChannel<Action>(-1)

    private val _state = MutableStateFlow(defaultState)

    private val scope: CoroutineScope = viewModelScope

    val state: LiveData<State>
        get() = _state.asLiveData(viewModelScope.coroutineContext)

    @ObsoleteCoroutinesApi
    fun sendAction(action: Action) {
        scope.launch {
            actions.consume {
                val intent = intents[action::class]
                requireNotNull(intent)
                val result = intent.onTrigger?.invoke(_state.value)

                val newState = intent.reducer(action, result, _state.value)

                _state.value = newState

                intent.sideEffect?.invoke(action, result, _state.value)?.let { sideEffect ->
                    sideEffectsInternal.postValue(sideEffect)
                }
            }
            actions.send(action)
        }
    }

}

data class Intent<State, SideEffect>(
    val onTrigger: (suspend (State) -> Any?)? = null,
    val reducer: suspend (Any, Any?, State) -> State = {_, _, state -> state},
    val sideEffect: ((Any, Any?, State) -> SideEffect?)? = null
)