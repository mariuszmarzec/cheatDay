package com.marzec.cheatday.repository

import com.marzec.cheatday.db.dao.DayDao
import com.marzec.cheatday.db.model.db.DayEntity
import com.marzec.cheatday.db.model.db.toDomain
import com.marzec.cheatday.db.model.domain.DaysGroup
import com.marzec.cheatday.db.model.domain.Day
import com.marzec.cheatday.db.model.domain.toDb
import com.marzec.cheatday.extensions.onIo
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.functions.Function3
import javax.inject.Inject

class DayRepositoryImpl @Inject constructor(
    private val dayDao: DayDao
) : DayRepository {

    override fun getDaysByUser(userId: String): Observable<DaysGroup> {
        return createDaysIfNeeded(userId).andThen(Observable.combineLatest(
            dayDao.getDayObservable(userId, Day.Type.CHEAT.name).map { it.toDomain() },
            dayDao.getDayObservable(userId, Day.Type.WORKOUT.name).map { it.toDomain() },
            dayDao.getDayObservable(userId, Day.Type.DIET.name).map { it.toDomain() },
            Function3<Day, Day, Day, DaysGroup> { cheat, workout, diet ->
                DaysGroup(cheat, workout, diet)
            }
        )).onIo()
    }

    private fun createDaysIfNeeded(userId: String): Completable {
        return Completable.fromAction {
            createDayIfNeeded(userId, Day.Type.CHEAT.name)
            createDayIfNeeded(userId, Day.Type.WORKOUT.name)
            createDayIfNeeded(userId, Day.Type.DIET.name)
        }
    }

    private fun createDayIfNeeded(userId: String, type: String) {
        if (dayDao.getDay(userId, type) == null) {
            dayDao.createOrUpdate(DayEntity(0, type, 0, userId))
        }
    }

    override fun update(userId: String, day: Day): Completable {
        return dayDao.createOrUpdateCompletable(day.toDb(userId)).onIo()
    }
}

interface DayRepository {

    fun getDaysByUser(userId: String): Observable<DaysGroup>

    fun update(userId: String, day: Day): Completable
}