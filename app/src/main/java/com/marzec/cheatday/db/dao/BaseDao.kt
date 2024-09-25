package com.marzec.cheatday.db.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Upsert

interface BaseDao<T> {

    @Insert
    fun insert(ob: T): Long

    @Delete
    fun remove(ob: T)

    @Update
    fun update(ob: T)

    @Upsert
    fun upsert(ob: T)

    @Insert
    suspend fun insertSuspend(ob: T): Long

    @Delete
    suspend fun removeSuspend(ob: T)

    @Update
    suspend fun updateSuspend(ob: T)
}
