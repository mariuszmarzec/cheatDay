package com.marzec.cheatday.repository

import com.marzec.cheatday.TestSchedulersRule
import com.marzec.cheatday.common.Constants
import com.marzec.cheatday.db.dao.DayDao
import com.marzec.cheatday.db.model.db.DayEntity
import com.marzec.cheatday.domain.Day
import com.marzec.cheatday.domain.DaysGroup
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(TestSchedulersRule::class)
internal class DayRepositoryImplTest {

    val dayDao: DayDao = mock()

    lateinit var repository: DayRepositoryImpl

    @BeforeEach
    fun setUp() {
        whenever(dayDao.createOrUpdateCompletable(any())).thenReturn(Completable.complete())
        whenever(dayDao.getDay(any(), any())).thenReturn(null)
        whenever(dayDao.getDayObservable(any(), any())).thenReturn(Observable.empty())
        repository = DayRepositoryImpl(dayDao)
    }

    @Test
    fun getDaysByUser_int() {
        whenever(dayDao.getDay(any(), any())).thenReturn(null)
        repository.getDaysByUser("userId")
            .test().assertComplete()
        val captor = argumentCaptor<DayEntity>()
        verify(dayDao, times(3)).createOrUpdate(captor.capture())

        assertEquals(
            listOf(
                DayEntity(0, "CHEAT", 0, Int.MAX_VALUE.toLong(), "userId"),
                DayEntity(0, "WORKOUT", 0, Constants.MAX_WORKOUT_DAYS.toLong(), "userId"),
                DayEntity(0, "DIET", 0, Constants.MAX_DIET_DAYS.toLong(), "userId")
            ),
            captor.allValues
        )
    }

    @Test
    fun getDaysByUser() {
        val subjectCheat = BehaviorSubject.create<DayEntity>()
        val subjectWorkout = BehaviorSubject.create<DayEntity>()
        val subjectDiet = BehaviorSubject.create<DayEntity>()
        whenever(dayDao.getDay(any(), any())).thenReturn(DayEntity(0, "CHEAT", 0, 0, "userId"))
        whenever(dayDao.getDayObservable(any(), eq(Day.Type.CHEAT.name))).thenReturn(subjectCheat)
        whenever(dayDao.getDayObservable(any(), eq(Day.Type.WORKOUT.name))).thenReturn(subjectWorkout)
        whenever(dayDao.getDayObservable(any(), eq(Day.Type.DIET.name))).thenReturn(subjectDiet)

        val testObserver = repository.getDaysByUser("userId")
            .test()

        subjectCheat.onNext(DayEntity(0, "CHEAT", 0, 0, "userId"))
        subjectWorkout.onNext(DayEntity(0, "WORKOUT", 0, 0, "userId"))
        subjectDiet.onNext(DayEntity(0, "DIET", 0, 0, "userId"))

        testObserver.assertValueAt(
            0,
            DaysGroup(
                Day(0, Day.Type.CHEAT, 0, 0),
                Day(0, Day.Type.WORKOUT, 0, 0),
                Day(0, Day.Type.DIET, 0, 0)
            )
        )

        subjectWorkout.onNext(DayEntity(0, "WORKOUT", 1, 0, "userId"))

        testObserver.assertValueAt(
            1,
            DaysGroup(
                Day(0, Day.Type.CHEAT, 0, 0),
                Day(0, Day.Type.WORKOUT, 1, 0),
                Day(0, Day.Type.DIET, 0, 0)
            )
        )
    }

    @Test
    fun update() {
        repository.update("userId", Day(0, Day.Type.CHEAT, 1, 0)).test().assertComplete()
        verify(dayDao).createOrUpdateCompletable(DayEntity(0, "CHEAT", 1, 0, "userId"))
    }
}