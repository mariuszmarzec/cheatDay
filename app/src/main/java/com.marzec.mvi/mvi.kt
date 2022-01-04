@file:OptIn(ExperimentalTypeInference::class)

package com.marzec.mvi

import kotlin.experimental.ExperimentalTypeInference
import kotlin.random.Random
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.runningReduce
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
open class Store3<State : Any>(
    private val scope: CoroutineScope,
    private val defaultState: State
) {

    private val _intentContextFlow =
        MutableSharedFlow<Intent3<State, Any>>(
            extraBufferCapacity = 30,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )

    private var _state = MutableStateFlow(defaultState)

    val state: StateFlow<State>
        get() = _state

    open val identifier: Any = Unit

    private val jobs = hashMapOf<String, IntentJob<State, Any>>()

    suspend fun init(initialAction: suspend () -> Unit = {}) {
        scope.launch {
            _intentContextFlow
                .onSubscription {
                    _intentContextFlow.emit(Intent3(state = defaultState, result = null))
                    initialAction()
                }
                .runningReduce { old, new ->
                    val reducedState = new.reducer(new.result, old.state!!)
                    old.copy(
                        state = reducedState,
                        result = new.result,
                        sideEffect = new.sideEffect
                    )
                }.onEach {
                    onNewState(it.state!!)
                    it.sideEffect?.invoke(it.result, it.state)
                }.collect {
                    _state.emit(it.state!!)
                }
        }
    }

    fun <T> cancelFlowsIf(
        flow: Flow<T>,
        function: (T) -> Boolean
    ): Flow<T> = flow.cancelFlowsIf(function)

    @JvmName("cancelFlowsIfExt")
    protected fun <T> Flow<T>.cancelFlowsIf(function: (T) -> Boolean): Flow<T> =
        onEach {
            if (function.invoke(it)) {
                cancelAll()
            }
        }

    protected fun cancelAll() {
        jobs.forEach { it.value.cancelJob() }
    }

    open suspend fun onNewState(newState: State) = Unit

    fun <Result : Any> intent(id: String = "", @BuilderInference buildFun: IntentBuilder<State, Result>.() -> Unit) {
        intentInternal(id, buildFun)
    }

    fun <Result : Any> intent(@BuilderInference buildFun: IntentBuilder<State, Result>.() -> Unit) {
        intentInternal("", buildFun)
    }

    fun <Result : Any> intent(id: String = "", builder: IntentBuilder<State, Result>) {
        intentInternal(id, builder)
    }

    fun <Result : Any> intent(builder: IntentBuilder<State, Result>) {
        intentInternal("", builder)
    }

    fun <Result : Any> triggerIntent(func: suspend IntentBuilder.IntentContext<State, Result>.() -> Flow<Result>?) {
        intentInternal<Result> { onTrigger(func) }
    }

    fun <Result : Any> onTrigger(
        @BuilderInference func: suspend IntentBuilder.IntentContext<State, Result>.() -> Flow<Result>? = { null }
    ): IntentBuilder<State, Result> {
        return IntentBuilder<State, Result>().apply { onTrigger(func) }
    }

    fun reducerIntent(func: suspend IntentBuilder.IntentContext<State, Unit>.() -> State) {
        intentInternal<Unit> { reducer(func) }
    }

    fun reduce(func: suspend IntentBuilder.IntentContext<State, Unit>.() -> State): IntentBuilder<State, Unit> {
        return IntentBuilder<State, Unit>().apply { reducer(func) }
    }

    fun sideEffect(func: suspend IntentBuilder.IntentContext<State, Unit>.() -> Unit) {
        intentInternal<Unit> { sideEffect(func) }
    }

    private fun <Result : Any> intentInternal(id: String = "", buildFun: IntentBuilder<State, Result>.() -> Unit) {
        val builder = IntentBuilder<State, Result>().apply { buildFun() }
        intentInternal(id, builder)
    }

    private fun <Result : Any> intentInternal(id: String = "", builder: IntentBuilder<State, Result>) {
        jobs[id]?.cancelJob()

        val identifier = Random.nextLong()
        val intent = builder.build()
        val job = launchNewJob(intent)

        if (id.isNotEmpty()) {
            job.invokeOnCompletion {
                if (jobs[id]?.identifier == identifier) {
                    jobs.remove(id)
                }
            }
            if (job.isActive) {
                jobs[id] = IntentJob(identifier, intent, job)
            }
        }
    }

    private fun <Result : Any> launchNewJob(intent: Intent3<State, Result>): Job = scope.launch {
        val flow = intent.onTrigger(_state.value) ?: flowOf(null)
        flow.collect {
            _intentContextFlow.emit(
                intent.copy(
                    state = _state.value,
                    result = it,
                    sideEffect = intent.sideEffect
                )
            )
        }
    }

    protected fun cancel(vararg ids: String) {
        ids.forEach { jobs[it]?.cancelJob() }
    }

    private fun IntentJob<State, Any>.cancelJob() {
        job.cancel()
    }
}

private data class IntentJob<State : Any, Result : Any>(
    val identifier: Long,
    val intent: Intent3<State, Result>,
    val job: Job
)

data class Intent3<State, out Result : Any>(
    val onTrigger: suspend (stateParam: State) -> Flow<Result>? = { _ -> null },
    val reducer: suspend (result: Any?, stateParam: State) -> State = { _, stateParam -> stateParam },
    val sideEffect: (suspend (result: Any?, state: State) -> Unit)? = null,
    val state: State?,
    val result: Result?
)

@Suppress("UNCHECKED_CAST")
class IntentBuilder<State : Any, Result : Any>(
    private var onTrigger: suspend (stateParam: State) -> Flow<Result>? = { _ -> null },
    private var reducer: suspend (result: Any?, stateParam: State) -> State = { _, stateParam -> stateParam },
    private var sideEffect: (suspend (result: Any?, state: State) -> Unit)? = null,
) {

    fun onTrigger(
        func: suspend IntentContext<State, Result>.() -> Flow<Result>? = { null }
    ): IntentBuilder<State, Result> {
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

    fun build(): Intent3<State, Result> = Intent3(
        onTrigger = onTrigger,
        reducer = reducer,
        sideEffect = sideEffect,
        state = null,
        result = null
    )

    data class IntentContext<State, Result>(
        val state: State,
        val result: Result?
    ) {
        fun resultNonNull(): Result = result!!
    }
}

fun <State : Any, Result : Any> Intent3<State, Result>.rebuild(
    buildFun: IntentBuilder<State, Result>.(Intent3<State, Result>) -> Unit
) =
    IntentBuilder(
        onTrigger = onTrigger,
        reducer = reducer,
        sideEffect = sideEffect
    ).apply { buildFun(this@rebuild) }.build()
