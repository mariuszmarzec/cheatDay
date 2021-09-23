package com.marzec.cheatday.extensions

import com.google.common.truth.Truth.assertThat
import com.marzec.cheatday.core.test
import com.marzec.cheatday.view.model.ListItem
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.joda.time.DateTime
import org.junit.Assert.assertThrows
import org.junit.jupiter.api.Test

internal class CommonExtensionsKtTest {

    val listItem1 = object : ListItem {
        override val id: String = "list_item_1"
        override val data: Any = Unit
    }

    val listItem2 = object : ListItem {
        override val id: String = "list_item_2"
        override val data: Any = Unit
    }

    @Test
    fun emptyStringTest() {
        assertThat(emptyString()).isEqualTo("")
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
    fun incIfIncreaseWhenTrue() {
        assertThat(1.incIf { true }).isEqualTo(2)
    }

    @Test
    fun incIfDoNotIncreaseIfFalse() {
        assertThat(1.incIf { false }).isEqualTo(1)
    }

    @Test
    fun plusAddingItems() {
        assertThat(listItem1 + listItem2).isEqualTo(listOf(listItem1, listItem2))
    }

    @Test
    fun plusAddingItemToList() {
        assertThat(listOf(listItem1) + listItem2).isEqualTo(listOf(listItem1, listItem2))
    }

    @Test
    fun plusAddingListToItem() {
        assertThat(listItem1 + listOf(listItem2)).isEqualTo(listOf(listItem1, listItem2))
    }

    @Test
    fun combinePair() = runBlockingTest {
        val combine = combine(flowOf(1), flowOf("a"))
        assertThat(combine.test(this).values()).isEqualTo(listOf(Pair(1, "a")))
    }

    @Test
    fun combineTriple() = runBlockingTest {
        val combine = combine(flowOf(1), flowOf("a"), flowOf(1L))
        assertThat(combine.test(this).values()).isEqualTo(listOf(Triple(1, "a", 1L)))
    }

    @Test
    fun combineQuadruple() = runBlockingTest {
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
    fun testToDateTimeReturnsNullIfNotDate() {
        val thrown = assertThrows(IllegalArgumentException::class.java) { "".toDateTime() }
        assertThat(thrown).isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun testToDateTime() {
        assertThat("2021-07-08T09:31:12".toDateTime()).isEqualTo(
            DateTime(2021, 7, 8, 9, 31, 12)
        )
    }
}
