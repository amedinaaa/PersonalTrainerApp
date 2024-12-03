package com.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.workoutpage.model.TileData

@Dao
interface TileDataDao {

    // Insert or Replace (Upsert)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTileData(tileData: TileData)

    // Update
    @Update
    suspend fun updateTileData(tileData: TileData)

    // Delete
    @Delete
    suspend fun deleteTileData(tileData: TileData)

    // Delete by title
    @Query("DELETE FROM tile_data WHERE title = :title")
    suspend fun deleteTileDataByTitle(title: String)

    // Get a TileData object by title
    @Query("SELECT * FROM tile_data WHERE title = :title LIMIT 1")
    suspend fun getTileDataByTitle(title: String): TileData?

    @Query("SELECT * FROM tile_data")
    suspend fun getAllTile():List<TileData>
}