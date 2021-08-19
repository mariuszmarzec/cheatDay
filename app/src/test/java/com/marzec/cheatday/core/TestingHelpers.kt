package com.marzec.cheatday.core

import com.marzec.mvi.StoreViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch

fun <T> Flow<T>.test(scope: CoroutineScope): TestCollector<T> {
    return TestCollector(scope, this).test()
}

fun <State : Any, SideEffect> StoreViewModel<State, SideEffect>.test(scope: CoroutineScope) =
    merge(state, sideEffects).test(scope)

class TestCollector<T>(
    private val scope: CoroutineScope,
    private val flow: Flow<T>
) {
    private val values = mutableListOf<T>()
    private lateinit var job: Job

    fun test(): TestCollector<T> {
        job = scope.launch {
            flow.collect { values.add(it) }
        }
        return this
    }

    fun values(): List<T> {
        job.cancel()
        return values
    }
}