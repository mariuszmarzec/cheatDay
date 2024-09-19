package com.marzec.cheatday.extensions

import com.google.common.truth.Truth.assertThat
import com.marzec.cheatday.core.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

import org.joda.time.DateTime
import org.junit.Assert.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class CommonExtensionsKtTest {

    val dispatcher = UnconfinedTestDispatcher()
    val scope = TestScope(dispatcher)

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @Test
    fun emptyStringTest() {
        assertThat(EMPTY_STRING).isEqualTo("")
    }

    @Test
    fun toDateTime() {
        assertThat(10L.toDateTime()).isEqualTo(DateTime(10))
    }

    @Test
    fun toDateTimeNull() {
        assertThat(null.toDateTime()).isEqualTo(null)
    }

    @Test
    fun `when condition is true, then increase`() {
        assertThat(1.incIf { true }).isEqualTo(2)
    }

    @Test
    fun `when condition is false, then decrease`() {
        assertThat(1.incIf { false }).isEqualTo(1)
    }

    @Test
    fun combinePair()  = scope.runTest {
        val combine = combine(flowOf(1), flowOf("a"))
        assertThat(combine.test(this).values()).isEqualTo(listOf(Pair(1, "a")))
    }

    @Test
    fun combineTriple()  = scope.runTest {
        val combine = combine(flowOf(1), flowOf("a"), flowOf(1L))
        assertThat(combine.test(this).values()).isEqualTo(listOf(Triple(1, "a", 1L)))
    }

    @Test
    fun combineQuadruple()  = scope.runTest {
        val combine = combine(
            flowOf(1),
            flowOf("a"),
            flowOf(1L),
            flowOf(1f)
        )
        assertThat(combine.test(this).values())
            .isEqualTo(listOf(Quadruple(1, "a", 1L, 1f)))
    }

    @Test
    fun `when string doesn't contain date, then toDateTime throws exception`() {
        val thrown = assertThrows(IllegalArgumentException::class.java) { "".toDateTime() }
        assertThat(thrown).isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `when string contains date, then toDateTime throws DateTime`() {
        assertThat("2021-07-08T09:31:12".toDateTime()).isEqualTo(
            DateTime(2021, 7, 8, 9, 31, 12)
        )
    }
}
