package com.marzec.cheatday.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.marzec.cheatday.db.model.db.DayEntity
import kotlinx.coroutines.flow.Flow

@Dao
abstract class DayDao : BaseDao<DayEntity> {

    @Query("SELECT * FROM ${DayEntity.NAME} WHERE type = :type AND user_id = :userId")
    abstract fun observeDay(userId: Long, type: String): Flow<DayEntity?>

    @Query("SELECT * FROM ${DayEntity.NAME} WHERE type = :type AND user_id = :userId")
    abstract fun getDay(userId: Long, type: String): DayEntity?

    @Transaction
    open suspend fun createOrUpdate(day: DayEntity) {
        val old = getDay(day.userId, day.type)
        if (old != null) {
            update(day.copy(id = old.id))
        } else {
            insert(day)
        }
    }
}