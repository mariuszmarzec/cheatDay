package com.marzec.cheatday.repository

import com.marzec.cheatday.common.Constants
import com.marzec.cheatday.db.dao.DayDao
import com.marzec.cheatday.db.model.db.DayEntity
import com.marzec.cheatday.db.model.db.toDomain
import com.marzec.cheatday.model.domain.Day
import com.marzec.cheatday.model.domain.DaysGroup
import com.marzec.cheatday.model.domain.toDb
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class DayRepository @Inject constructor(
    private val dayDao: DayDao,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun observeDaysByUser(userId: String): Flow<DaysGroup> = withContext(dispatcher) {
        createDaysIfNeeded(userId)
        combine(
            dayDao.observeDay(userId, Day.Type.CHEAT.name).map { it.toDomain() },
            dayDao.observeDay(userId, Day.Type.WORKOUT.name).map { it.toDomain() },
            dayDao.observeDay(userId, Day.Type.DIET.name).map { it.toDomain() }
        ) { cheat, workout, diet ->
            DaysGroup(cheat, workout, diet)
        }.flowOn(dispatcher)
    }

    private suspend fun createDaysIfNeeded(userId: String) = withContext(dispatcher) {
        createDayIfNeeded(userId, Constants.MAX_CHEAT_DAYS.toLong(), Day.Type.CHEAT.name)
        createDayIfNeeded(userId, Constants.MAX_WORKOUT_DAYS.toLong(), Day.Type.WORKOUT.name)
        createDayIfNeeded(userId, Constants.MAX_DIET_DAYS.toLong(), Day.Type.DIET.name)
    }

    private suspend fun createDayIfNeeded(userId: String, max: Long, type: String) = withContext(dispatcher) {
        if (dayDao.getDay(userId, type) == null) {
            dayDao.createOrUpdate(DayEntity(0, type, 0, max, userId))
        }
    }

    suspend fun update(userId: String, day: Day) = withContext(dispatcher) {
        dayDao.createOrUpdate(day.toDb(userId))
    }
}
