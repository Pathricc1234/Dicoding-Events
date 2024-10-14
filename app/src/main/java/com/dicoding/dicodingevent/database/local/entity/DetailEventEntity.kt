package com.dicoding.dicodingevent.database.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "detailEvent")
data class DetailEventEntity (
    @field:PrimaryKey
    @field:ColumnInfo(name = "id")
    val id: Int,

    @field:ColumnInfo(name = "logo")
    val logo: String? = null,

    @field:ColumnInfo(name = "banner")
    val banner: String? = null,

    @field:ColumnInfo(name = "name")
    val name: String? = null,

    @field:ColumnInfo(name = "summary")
    val summary: String? = null,

    @field:ColumnInfo(name = "owner")
    val ownerName: String? = null,

    @field:ColumnInfo(name = "time")
    val beginTime: String? = null,

    @field:ColumnInfo(name = "quota")
    val quota: Int,

    @field:ColumnInfo(name = "registrant")
    val registrant : Int,

    @field:ColumnInfo(name = "description")
    val description: String? = null,

    @field:ColumnInfo(name = "link")
    val link: String? = null,

    @field:ColumnInfo(name = "favorite")
    var favorite: Boolean
)