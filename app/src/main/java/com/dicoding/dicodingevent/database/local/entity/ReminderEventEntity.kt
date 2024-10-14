package com.dicoding.dicodingevent.database.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminder")
data class ReminderEventEntity (
    @field:PrimaryKey
    @field:ColumnInfo(name = "id")
    val id: Int,

    @field:ColumnInfo(name = "name")
    val name: String? = null,

    @field:ColumnInfo(name = "date")
    val date: String? = null
)