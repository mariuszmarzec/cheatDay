package com.marzec.cheatday.repository

import com.marzec.cheatday.TestSchedulersRule
import com.marzec.cheatday.db.dao.WeightDao
import com.marzec.cheatday.db.model.db.WeightResultEntity
import com.marzec.cheatday.domain.WeightResult
import com.marzec.cheatday.domain.toDomain
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Completable
import io.reactivex.Observable
import org.joda.time.DateTime
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(TestSchedulersRule::class)
internal class WeightResultRepositoryImplTest {

    val weightDao: WeightDao = mock()

    lateinit var repository: WeightResultRepository

    @BeforeEach
    fun setUp() {
        whenever(weightDao.getWeights(any())).thenReturn(Observable.just(listOf(stubWeightResultEntity())))
        whenever(weightDao.insertCompletable(any())).thenReturn(Completable.complete())
        whenever(weightDao.updateCompletable(any())).thenReturn(Completable.complete())
        repository = WeightResultRepositoryImpl(weightDao)
    }

    @Test
    fun getWeights() {
        repository.getWeights("user_id")
            .test()
            .assertValue(listOf(stubWeightResultEntity().toDomain()))
        verify(weightDao).getWeights("user_id")
    }

    @Test
    fun editWeight() {
        repository.editWeight("user_id", stubWeightResult(userId = "user_id"))
            .test()
            .assertComplete()
        verify(weightDao).updateCompletable(stubWeightResultEntity(userId = "user_id"))
    }

    @Test
    fun putWeight() {
        repository.putWeight("user_id", stubWeightResult(userId = "user_id"))
            .test()
            .assertComplete()
        verify(weightDao).insertCompletable(stubWeightResultEntity(userId = "user_id"))
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