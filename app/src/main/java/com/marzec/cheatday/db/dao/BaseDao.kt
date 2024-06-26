package com.marzec.cheatday.db.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Upsert
import com.marzec.cheatday.db.model.db.WeightResultEntity

interface BaseDao<T> {

    @Insert
    fun insert(ob: T)

    @Delete
    fun remove(ob: T)

    @Update
    fun update(ob: T)

    @Upsert
    fun upsert(ob: T)

    @Insert
    suspend fun insertSuspend(ob: T)

    @Delete
    suspend fun removeSuspend(ob: T)

    @Update
    suspend fun updateSuspend(ob: T)

    @Insert
    suspend fun insertAll(weights: List<WeightResultEntity>)

    @Update
    suspend fun updateAll(weights: List<WeightResultEntity>)
}
