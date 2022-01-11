package com.marzec.cheatday.repository

import com.google.common.truth.Truth.assertThat
import com.marzec.cheatday.api.Content
import com.marzec.cheatday.api.WeightApi
import com.marzec.cheatday.api.request.PutWeightRequest
import com.marzec.cheatday.core.test
import com.marzec.cheatday.db.dao.WeightDao
import com.marzec.cheatday.extensions.toDateTime
import com.marzec.cheatday.model.domain.User
import com.marzec.cheatday.stubs.stubWeightDto
import com.marzec.cheatday.stubs.stubWeightResult
import com.marzec.cheatday.stubs.stubWeightResultEntity
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.joda.time.DateTime
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


internal class WeightResultRepositoryTest {

    val weightApi: WeightApi = mockk()
    val dispatcher: CoroutineDispatcher = UnconfinedTestDispatcher()
    val weightDao: WeightDao = mockk(relaxed = true)
    val userRepository: UserRepository = mockk()

    var repository = WeightResultRepository(
        weightApi = weightApi,
        weightDao = weightDao,
        userRepository = userRepository,
        dispatcher = dispatcher
    )

    @BeforeEach
    fun setUp() {
        coEvery { userRepository.getCurrentUser() } returns User(0, "user@email.com")
    }

    @Test
    fun observeWeights() = test {
        coEvery { weightApi.getAll() } returns listOf(stubWeightDto())
        coEvery { weightDao.observeWeights(0) }.returnsMany(
            emptyFlow(),
            flowOf(listOf(stubWeightResultEntity()))
        )

        val test = repository.observeWeights().test(this)
        test.isEqualTo(
            Content.Loading(),
            Content.Data(listOf(stubWeightResult()))
        )
    }

    @Test
    fun observeMinWeight() = test {
        coEvery { weightApi.getAll() } returns listOf(
            stubWeightDto(value = 10f),
            stubWeightDto(value = 5f)
        )
        coEvery { weightDao.observeWeights(0) }.returnsMany(
            emptyFlow(),
            flowOf(listOf(stubWeightResultEntity(value = 5f)))
        )

        assertThat(repository.observeMinWeight().test(this).values()).isEqualTo(
            listOf(
                Content.Loading(),
                Content.Data(stubWeightResult(value = 5f))
            )
        )
    }

    @Test
    fun observeLastWeight() {
        val date: Long = DateTime()
            .withDate(2021, 6, 7)
            .withTimeAtStartOfDay().millis
        test {
            coEvery { weightApi.getAll() } returns listOf(
                stubWeightDto(value = 10f),
                stubWeightDto(value = 5f, date = "2021-06-07T00:00:00")
            )

            coEvery { weightDao.observeWeights(0) }.returnsMany(
                emptyFlow(),
                flowOf(
                    listOf(
                        stubWeightResultEntity(value = 10f),
                        stubWeightResultEntity(value = 5f, date = date)
                    )
                )
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
    }


    @Test
    fun putWeight() = test {
        coEvery {
            weightApi.put(
                PutWeightRequest(
                    value = 5f,
                    date = "2021-06-07T00:00:00"
                )
            )
        } returns stubWeightDto(
            value = 5f,
            date = "2021-06-07T00:00:00"
        )


        val result = repository.putWeight(
            stubWeightResult(
                value = 5f,
                date = "2021-06-07T00:00:00".toDateTime()
            )
        ).test(this).values()

        assertThat(result).isEqualTo(
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
    fun updateWeight() = test {
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
    fun getWeight() = test {
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
    fun removeWeight() = test {
        coEvery { weightApi.remove(1) } returns Unit

        repository.removeWeight(1).test(this).isEqualTo(
            Content.Loading(),
            Content.Data(Unit)
        )
    }
}
