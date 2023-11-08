package com.marzec.cheatday.core

import app.cash.paparazzi.Paparazzi
import com.google.common.truth.Truth.assertThat
import com.marzec.mvi.StoreViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest

fun <T> Flow<T>.test(scope: TestScope): TestCollector<T> {
    return TestCollector(scope, this).test()
}

fun <State : Any, SideEffect> StoreViewModel<State, SideEffect>.test(scope: TestScope) =
    merge(state, sideEffects).test(scope)

class TestCollector<T>(
    private val scope: TestScope,
    private val flow: Flow<T>
) {
    private val values = mutableListOf<T>()
    private lateinit var job: Job

    fun test(): TestCollector<T> {
        job = scope.async {
            flow.collect { values.add(it) }
        }
        return this
    }

    fun values(): List<T> {
        cancelIfActive()
        return values
    }

    fun isEqualTo(expected: List<T>) {
        cancelIfActive()
        assertThat(values).isEqualTo(expected)
    }

    fun isEqualTo(vararg expected: T) {
        cancelIfActive()
        assertThat(values).isEqualTo(expected.toList())
    }

    private fun cancelIfActive() {
        scope.advanceUntilIdle()
        if (job.isActive) {
            job.cancel()
        }
    }
}

fun test(testBody: suspend TestScope.() -> Unit) {
    return runTest(UnconfinedTestDispatcher()) {
        testBody.invoke(this)
    }
}

fun getPaparazziRule() = Paparazzi(theme = "AppTheme")

