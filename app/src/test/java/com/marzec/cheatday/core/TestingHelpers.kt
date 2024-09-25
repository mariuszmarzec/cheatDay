package com.marzec.cheatday.core

import app.cash.paparazzi.Paparazzi
import com.google.common.truth.Truth.assertThat
import com.marzec.mvi.StoreViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle

fun <State : Any, SideEffect> StoreViewModel<State, SideEffect>.test(scope: TestScope) =
    merge(state, sideEffects).test(scope)

fun <T> Flow<T>.test(scope: TestScope): TestCollector<T> {
    return TestCollector(scope, this).test()
}

class TestCollector<T>(
    private val scope: TestScope,
    private val flow: Flow<T>
) {
    private val values = mutableListOf<T>()
    private lateinit var job: Job

    fun test(): TestCollector<T> {
        job = scope.launch {
            flow.toCollection(values)
        }
        return this
    }

    fun values(): List<T> {
        scope.advanceUntilIdle()
        cancelIfActive()
        return values
    }

    fun isEqualTo(expected: List<T>) {
        scope.advanceUntilIdle()
        cancelIfActive()
        assertThat(values).isEqualTo(expected)
    }

    fun isEqualTo(vararg expected: T) {
        scope.advanceUntilIdle()
        cancelIfActive()
        assertThat(values).isEqualTo(expected.toList())
    }

    private fun cancelIfActive() {
        if (job.isActive) {
            job.cancel()
        }
    }
}

fun getPaparazziRule() = Paparazzi(theme = "AppTheme")
