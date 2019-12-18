package com.marzec.cheatday.db.model

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(
    tableName = WeightResultEntity.NAME,
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = [UserEntity.COLUMN_UUID],
            childColumns = [DayEntity.COLUMN_USER_ID],
            onDelete = CASCADE
        )
    ]
)
data class WeightResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = COLUMN_VALUE) val value: Float,
    @ColumnInfo(name = COLUMN_DATE) val date: Long,
    @ColumnInfo(name = COLUMN_USER_ID) val userId: String
) {
    companion object {
        const val NAME = "weights"
        const val COLUMN_VALUE = "value"
        const val COLUMN_DATE = "date"
        const val COLUMN_USER_ID = "user_id"
    }
}