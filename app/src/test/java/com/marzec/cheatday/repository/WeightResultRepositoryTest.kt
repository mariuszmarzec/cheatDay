package com.marzec.cheatday.repository

import com.google.common.truth.Truth.assertThat
import com.marzec.cheatday.api.Api
import com.marzec.cheatday.api.Content
import com.marzec.cheatday.api.WeightApi
import com.marzec.cheatday.api.request.PutWeightRequest
import com.marzec.cheatday.core.values
import com.marzec.cheatday.extensions.toDateTime
import com.marzec.cheatday.stubs.stubWeightDto
import com.marzec.cheatday.stubs.stubWeightResult
import com.marzec.cheatday.stubs.stubWeightResultEntity
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


internal class WeightResultRepositoryTest {

    val weightApi: WeightApi = mock()
    val dispatcher: CoroutineDispatcher = TestCoroutineDispatcher()


    lateinit var repository: WeightResultRepository

    @BeforeEach
    fun setUp() {
        repository = WeightResultRepository(weightApi, dispatcher)
    }

    @Test
    fun observeWeights() = runBlockingTest {
        whenever(weightApi.getAll()).thenReturn(listOf(stubWeightDto()))

        assertThat(repository.observeWeights()).isEqualTo(
            Content.Data(listOf(stubWeightResult()))
        )
    }

    @Test
    fun observeMinWeight() = runBlockingTest {
        whenever(weightApi.getAll()).thenReturn(
            listOf(
                stubWeightDto(value = 10f),
                stubWeightDto(value = 5f)
            )
        )

        assertThat(repository.observeMinWeight().values(this)).isEqualTo(
            listOf(stubWeightResult(value = 5f))
        )
    }

    @Test
    fun observeLastWeight() = runBlockingTest {
        whenever(weightApi.getAll()).thenReturn(
            listOf(
                stubWeightDto(value = 10f),
                stubWeightDto(value = 5f, date = "2021-06-07T00:00:00")
            )
        )

        assertThat(repository.observeLastWeight().values(this)).isEqualTo(
            listOf(stubWeightResult(value = 5f, date = "2021-06-07T00:00:00".toDateTime()))
        )
    }


    @Test
    fun putWeight() = runBlockingTest {
        whenever(
            weightApi.put(PutWeightRequest(value = 5f, date = "2021-06-07T00:00:00"))
        ).thenReturn(Unit)

        val result = repository.putWeight(
            stubWeightResult(
                value = 5f,
                date = "2021-06-07T00:00:00".toDateTime()
            )
        )

        assertThat(result).isEqualTo(Content.Data(Unit))
    }

    @Test
    fun updateWeight() = runBlockingTest {
        whenever(
            weightApi.update(
                0,
                stubWeightDto(value = 5f, date = "2021-06-07T00:00:00")
            )
        ).thenReturn(Unit)

        val result = repository.updateWeight(
            stubWeightResult(
                value = 5f,
                date = "2021-06-07T00:00:00".toDateTime()
            )
        )

        assertThat(result).isEqualTo(Content.Data(Unit))
    }

    @Test
    fun getWeight() = runBlockingTest {
        whenever(weightApi.getAll()).thenReturn(
            listOf(
                stubWeightDto(id = 1, value = 10f),
                stubWeightDto(id = 2, value = 5f)
            )
        )

        assertThat(repository.getWeight(1)).isEqualTo(
            stubWeightResult(id = 1, value = 10f)
        )
    }

    @Test
    fun removeWeight() = runBlockingTest {
        whenever(weightApi.remove(1)).thenReturn(Unit)

        assertThat(repository.removeWeight(1)).isEqualTo(Content.Data(Unit))
    }
}
