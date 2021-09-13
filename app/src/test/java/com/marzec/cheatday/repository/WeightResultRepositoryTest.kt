package com.marzec.cheatday.repository

import com.google.common.truth.Truth.assertThat
import com.marzec.cheatday.api.Content
import com.marzec.cheatday.api.WeightApi
import com.marzec.cheatday.api.request.PutWeightRequest
import com.marzec.cheatday.core.test
import com.marzec.cheatday.extensions.toDateTime
import com.marzec.cheatday.stubs.stubWeightDto
import com.marzec.cheatday.stubs.stubWeightResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


internal class WeightResultRepositoryTest {

    val weightApi: WeightApi = mockk()
    val dispatcher: CoroutineDispatcher = TestCoroutineDispatcher()


    lateinit var repository: WeightResultRepository

    @BeforeEach
    fun setUp() {
        repository = WeightResultRepository(weightApi, dispatcher)
    }

    @Test
    fun observeWeights() = runBlockingTest {
        coEvery { weightApi.getAll() } returns listOf(stubWeightDto())

        assertThat(repository.observeWeights()).isEqualTo(
            Content.Data(listOf(stubWeightResult()))
        )
    }

    @Test
    fun observeMinWeight() = runBlockingTest {
        coEvery { weightApi.getAll() } returns listOf(
            stubWeightDto(value = 10f),
            stubWeightDto(value = 5f)
        )

        assertThat(repository.observeMinWeight().test(this).values()).isEqualTo(
            listOf(
                Content.Loading(),
                Content.Data(stubWeightResult(value = 5f))
            )
        )
    }

    @Test
    fun observeLastWeight() = runBlockingTest {
        coEvery { weightApi.getAll() } returns listOf(
            stubWeightDto(value = 10f),
            stubWeightDto(value = 5f, date = "2021-06-07T00:00:00")
        )

        repository.observeLastWeight().test(this).isEqualTo(
            listOf(
                Content.Loading(),
                Content.Data(
                    stubWeightResult(
                        value = 5f,
                        date = "2021-06-07T00:00:00".toDateTime()
                    )
                )
            )
        )
    }


    @Test
    fun putWeight() = runBlockingTest {
        coEvery {
            weightApi.put(
                PutWeightRequest(
                    value = 5f,
                    date = "2021-06-07T00:00:00"
                )
            )
        } returns Unit

        val result = repository.putWeight(
            stubWeightResult(
                value = 5f,
                date = "2021-06-07T00:00:00".toDateTime()
            )
        ).test(this).values()

        assertThat(result).isEqualTo(
            listOf(
                Content.Loading(),
                Content.Data(Unit)
            )
        )
    }

    @Test
    fun updateWeight() = runBlockingTest {
        coEvery {
            weightApi.update(
                0,
                stubWeightDto(value = 5f, date = "2021-06-07T00:00:00")
            )
        } returns Unit

        val result = repository.updateWeight(
            stubWeightResult(
                value = 5f,
                date = "2021-06-07T00:00:00".toDateTime()
            )
        ).test(this).values()

        assertThat(result).isEqualTo(
            listOf(
                Content.Loading(),
                Content.Data(Unit)
            )
        )
    }

    @Test
    fun getWeight() = runBlockingTest {
        coEvery { weightApi.getAll() } returns listOf(
            stubWeightDto(id = 1, value = 10f),
            stubWeightDto(id = 2, value = 5f)
        )

        assertThat(repository.getWeight(1).test(this).values()).isEqualTo(
            listOf(
                Content.Loading(),
                Content.Data(stubWeightResult(id = 1, value = 10f))
            )
        )
    }

    @Test
    fun removeWeight() = runBlockingTest {
        coEvery { weightApi.remove(1) } returns Unit

        repository.removeWeight(1).test(this).isEqualTo(
            Content.Loading(),
            Content.Data(Unit)
        )
    }
}
