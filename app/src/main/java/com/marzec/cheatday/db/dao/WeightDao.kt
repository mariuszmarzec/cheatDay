package com.marzec.cheatday.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.marzec.cheatday.db.model.db.WeightResultEntity
import com.marzec.cheatday.model.domain.WeightResult
import kotlinx.coroutines.flow.Flow

@Dao
interface WeightDao : BaseDao<WeightResultEntity> {

    @Query("SELECT * FROM ${WeightResultEntity.NAME} " +
            "WHERE user_id = :userId " +
            "ORDER BY ${WeightResultEntity.COLUMN_DATE} DESC")
    fun observeWeights(userId: Long): Flow<List<WeightResultEntity>>

    @Query("SELECT * FROM ${WeightResultEntity.NAME} " +
            "WHERE user_id = :userId AND value == (SELECT MIN(value) FROM ${WeightResultEntity.NAME} " +
            "WHERE user_id = :userId) ORDER BY ${WeightResultEntity.COLUMN_DATE} DESC")
    fun observeMinWeight(userId: Long): Flow<WeightResultEntity?>

    @Query("SELECT * FROM ${WeightResultEntity.NAME} " +
            "WHERE user_id = :userId ORDER BY ${WeightResultEntity.COLUMN_DATE} DESC")
    fun observeLastWeight(userId: Long): Flow<WeightResultEntity?>

    @Query("SELECT * FROM ${WeightResultEntity.NAME} WHERE id = :id")
    suspend fun getWeight(id: Long): WeightResultEntity?

    @Query("SELECT * FROM ${WeightResultEntity.NAME} WHERE id = :id")
    fun observeWeight(id: Long): Flow<WeightResultEntity>

    @Query("DELETE FROM ${WeightResultEntity.NAME}")
    suspend fun removeAll()

    @Query("DELETE FROM ${WeightResultEntity.NAME} WHERE id = :id")
    suspend fun removeById(id: Long)

    @Insert
    suspend fun insertAll(weights: List<WeightResultEntity>)

    @Transaction
    suspend fun replaceAll(weights: List<WeightResultEntity>) {
        removeAll()
        insertAll(weights)
    }
}
