package com.marzec.cheatday.db.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

interface BaseDao<T> {

    @Insert
    fun insert(ob: T)

    @Delete
    fun remove(ob: T)

    @Update
    fun update(ob: T)
    
    @Insert
    suspend fun insertSuspend(ob: T)

    @Delete
    suspend fun removeSuspend(ob: T)

    @Update
    suspend fun updateSuspend(ob: T)
}
