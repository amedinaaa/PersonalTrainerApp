package com.workoutpage.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "tile_data")
data class TileData(
    @PrimaryKey val title: String,
    val weight: Double,
    val sets: Int,
    val reps: Int
)