package com.marzec.cheatday.db.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import io.reactivex.Completable

interface BaseDao<T> {

    @Insert
    fun insert(ob: T): Completable

    @Delete
    fun remove(ob: T): Completable

    @Update
    fun update(ob: T): Completable
}