package com.marzec.cheatday.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.marzec.cheatday.db.model.db.WeightResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeightDao : BaseDao<WeightResultEntity> {

    @Query("SELECT * FROM ${WeightResultEntity.NAME} WHERE user_id = :userId ORDER BY ${WeightResultEntity.COLUMN_DATE} DESC")
    fun getWeights(userId: String): Flow<List<WeightResultEntity>>
}