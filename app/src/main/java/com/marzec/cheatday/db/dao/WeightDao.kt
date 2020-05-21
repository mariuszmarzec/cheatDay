package com.marzec.cheatday.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.marzec.cheatday.db.model.db.WeightResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeightDao : BaseDao<WeightResultEntity> {

    @Query("SELECT * FROM ${WeightResultEntity.NAME} WHERE user_id = :userId ORDER BY ${WeightResultEntity.COLUMN_DATE} DESC")
    fun observeWeights(userId: String): Flow<List<WeightResultEntity>>

    @Query("SELECT * FROM ${WeightResultEntity.NAME} WHERE user_id = :userId AND value == (SELECT MIN(value) FROM ${WeightResultEntity.NAME} WHERE user_id = :userId) ORDER BY ${WeightResultEntity.COLUMN_DATE} DESC")
    fun observeMinWeight(userId: String): Flow<WeightResultEntity>

    @Query("SELECT * FROM ${WeightResultEntity.NAME} WHERE user_id = :userId ORDER BY ${WeightResultEntity.COLUMN_DATE} DESC")
    fun observeLastWeight(userId: String): Flow<WeightResultEntity>
}