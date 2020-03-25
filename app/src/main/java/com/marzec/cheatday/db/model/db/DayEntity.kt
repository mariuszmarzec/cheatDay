package com.marzec.cheatday.db.model.db

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import com.marzec.cheatday.domain.Day

@Entity(
    tableName = DayEntity.NAME,
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = [UserEntity.COLUMN_UUID],
            childColumns = [DayEntity.COLUMN_USER_ID],
            onDelete = CASCADE
        )
    ],
    indices = [Index(value = [DayEntity.COLUMN_TYPE, DayEntity.COLUMN_USER_ID], unique = true)]
)
data class DayEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = COLUMN_TYPE) val type: String,
    @ColumnInfo(name = COLUMN_COUNT) val count: Long,
    @ColumnInfo(name = COLUMN_USER_ID) val userId: String
) {
    companion object {
        const val NAME = "days"
        const val COLUMN_TYPE = "type"
        const val COLUMN_COUNT = "count"
        const val COLUMN_USER_ID = "user_id"
    }
}

fun DayEntity.toDomain(): Day {
    return Day(this.id, enumValueOf(this.type), this.count)
}