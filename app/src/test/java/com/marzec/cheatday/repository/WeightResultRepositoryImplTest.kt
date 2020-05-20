package com.marzec.cheatday.repository

import com.marzec.cheatday.db.dao.WeightDao
import com.marzec.cheatday.db.model.db.WeightResultEntity
import com.marzec.cheatday.domain.WeightResult
import com.marzec.cheatday.domain.toDomain
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
        whenever(weightDao.getWeights(any())).thenReturn(flowOf(listOf(stubWeightResultEntity())))
        repository = WeightResultRepositoryImpl(weightDao)
    }

    @Test
    fun getWeights() = runBlockingTest {
        assertEquals(
            listOf(stubWeightResultEntity().toDomain()),
            repository.getWeights("user_id").first()
        )
        verify(weightDao).getWeights("user_id")
    }

    @Test
    fun editWeight() = runBlockingTest {
        repository.editWeight("user_id", stubWeightResult(userId = "user_id"))
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