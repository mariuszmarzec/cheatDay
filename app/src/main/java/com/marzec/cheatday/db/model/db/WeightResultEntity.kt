package com.marzec.cheatday.db.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.marzec.cheatday.model.domain.WeightResult
import org.joda.time.DateTime

@Entity(
    tableName = WeightResultEntity.NAME,
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = [UserEntity.COLUMN_ID],
            childColumns = [DayEntity.COLUMN_USER_ID],
            onDelete = CASCADE
        )
    ]
)
data class WeightResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = COLUMN_VALUE) val value: Float,
    @ColumnInfo(name = COLUMN_DATE) val date: Long,
    @ColumnInfo(name = COLUMN_USER_ID) val userId: Long
) {
    companion object {
        const val NAME = "weights"
        const val COLUMN_VALUE = "value"
        const val COLUMN_DATE = "date"
        const val COLUMN_USER_ID = "user_id"
    }
}

fun WeightResultEntity.toDomain() = WeightResult(
    id = id,
    value = value,
    date = DateTime(date)
)
