package com.marzec.cheatday.repository

import com.marzec.cheatday.db.dao.WeightDao
import com.marzec.cheatday.db.model.db.WeightResultEntity
import com.marzec.cheatday.model.domain.WeightResult
import com.marzec.cheatday.model.domain.toDomain
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class WeightResultRepositoryImplTest {

    val weightDao: WeightDao = mock()

    lateinit var repository: WeightResultRepository

    @BeforeEach
    fun setUp() {
        whenever(weightDao.observeMinWeight(any())).thenReturn(flowOf())
        whenever(weightDao.observeWeights(any())).thenReturn(flowOf(listOf(stubWeightResultEntity())))
        repository = WeightResultRepositoryImpl(weightDao)
    }

    @Test
    fun observeWeight() = runBlockingTest {
        assertEquals(
            listOf(stubWeightResultEntity().toDomain()),
            repository.observeWeights("user_id").first()
        )
        verify(weightDao).observeWeights("user_id")
    }

    @Test
    fun observeMinWeight() = runBlockingTest {
        whenever(weightDao.observeMinWeight(any())).thenReturn(flowOf(stubWeightResultEntity()))
        assertEquals(
            stubWeightResultEntity().toDomain(),
            repository.observeMinWeight("user_id").first()
        )
        verify(weightDao).observeMinWeight("user_id")
    }

    @Test
    fun observeLastWeight() = runBlockingTest {
        whenever(weightDao.observeLastWeight(any())).thenReturn(flowOf(stubWeightResultEntity()))
        assertEquals(
            stubWeightResultEntity().toDomain(),
            repository.observeLastWeight("user_id").first()
        )
        verify(weightDao).observeLastWeight("user_id")
    }

    @Test
    fun editWeight() = runBlockingTest {
        repository.updateWeight("user_id", stubWeightResult(userId = "user_id"))
        verify(weightDao).updateSuspend(stubWeightResultEntity(userId = "user_id"))
    }

    @Test
    fun putWeight() = runBlockingTest {
        repository.putWeight("user_id", stubWeightResult(userId = "user_id"))
        verify(weightDao).insertSuspend(stubWeightResultEntity(userId = "user_id"))
    }

    private fun stubWeightResultEntity(
        id: Long = 0,
        value: Float = 0f,
        date: Long = 0,
        userId: String = ""
    ) = WeightResultEntity(
        id,
        value,
        date,
        userId
    )

    private fun stubWeightResult(
        id: Long = 0,
        value: Float = 0f,
        date: DateTime = DateTime(0),
        userId: String = ""
    ) = WeightResult(
        id,
        value,
        date
    )
}