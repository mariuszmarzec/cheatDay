package com.marzec.cheatday.db.dao

import androidx.room.Dao
import androidx.room.Ignore
import androidx.room.Query
import androidx.room.Transaction
import com.marzec.cheatday.db.model.DayEntity
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
abstract class DayDao : BaseDao<DayEntity> {

    @Query("SELECT * FROM ${DayEntity.NAME} WHERE type = :type AND user_id = :userId")
    abstract fun getDayObservable(userId: String, type: String): Observable<DayEntity>

    @Query("SELECT * FROM ${DayEntity.NAME} WHERE type = :type AND user_id = :userId")
    abstract fun getDay(userId: String, type: String): DayEntity?

    @Transaction
    open fun createOrUpdate(day: DayEntity) {
        val old = getDay(day.userId, day.type)
        if (old != null) {
            update(day.copy(id = old.id))
        } else {
            insert(day)
        }
    }

    @Ignore
    fun createOrUpdateCompletable(day: DayEntity) =
        Completable.fromAction {
            createOrUpdate(day)
        }
}