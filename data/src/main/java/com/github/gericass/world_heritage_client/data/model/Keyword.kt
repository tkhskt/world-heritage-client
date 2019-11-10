package com.github.gericass.world_heritage_client.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity
data class Keyword(
    @PrimaryKey
    val keyword: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Date
) : Serializable