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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class DayRepository @Inject constructor(
    private val dayDao: DayDao,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun observeDaysByUser(userId: String): Flow<DaysGroup> = withContext(dispatcher) {
        combine(
            observeDay(userId, Day.Type.CHEAT, Constants.MAX_CHEAT_DAYS),
            observeDay(userId, Day.Type.WORKOUT, Constants.MAX_WORKOUT_DAYS),
            observeDay(userId, Day.Type.DIET, Constants.MAX_DIET_DAYS)
        ) { cheat, workout, diet ->
            DaysGroup(cheat, workout, diet)
        }.flowOn(dispatcher)
    }

    private fun observeDay(userId: String, day: Day.Type, max: Int) =
        dayDao.observeDay(userId, day.name)
            .map {
                (it ?: DayEntity(0, day.name, 0, max.toLong(), userId)).toDomain()
            }
            .filterNotNull()

    suspend fun update(userId: String, day: Day) = withContext(dispatcher) {
        dayDao.createOrUpdate(day.toDb(userId))
    }
}
