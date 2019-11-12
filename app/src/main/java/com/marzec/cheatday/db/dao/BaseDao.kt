package com.marzec.cheatday.db.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import io.reactivex.Completable

interface BaseDao<T> {

    @Insert
    fun insert(ob: T)

    @Delete
    fun remove(ob: T)

    @Update
    fun update(ob: T)

    @Insert
    fun insertCompletable(ob: T): Completable

    @Delete
    fun removeCompletable(ob: T): Completable

    @Update
    fun updateCompletable(ob: T): Completable
}