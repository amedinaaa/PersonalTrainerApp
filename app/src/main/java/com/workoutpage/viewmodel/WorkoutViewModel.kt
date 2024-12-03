package com.workoutpage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.database.dao.TileDataDao
import com.workoutpage.model.ExerciseModel
import com.workoutpage.model.TileData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WorkoutViewModel(private val dao: TileDataDao) : ViewModel() {

    // Mutable LiveData to hold workout data
    private val _tiles = MutableLiveData<List<ExerciseModel>>()
    val tiles: LiveData<List<ExerciseModel>> get() = _tiles

    private val _tileDataState = MutableStateFlow<List< TileData?>>(emptyList())
    val tileDataState: StateFlow<List< TileData?>> = _tileDataState

    init {
        // Load sample data or fetch from a repository
        loadWorkoutTiles()

    }

    private fun loadWorkoutTiles() {
        _tiles.value = listOf(
            ExerciseModel(title = "Leg Press", weight = 120.0, sets = 2, reps = 10,3 ,50),
            ExerciseModel(title = "Leg Extension", weight = 30.0, sets = 4, reps = 12,3 ,50),
            ExerciseModel(title = "Chest Press", weight = 40.5, sets = 5, reps = 11,3 ,50),
            ExerciseModel(title = "Bicep Curl", weight = 50.0, sets = 3, reps = 10,3 ,50),
            ExerciseModel(title = "Squat", weight = 150.0, sets = 3, reps = 10,3 ,50),
            ExerciseModel(title = "Lat Pull", weight = 50.0, sets = 3, reps = 10,3 ,50),
        )
    }
    // Function to load TileData by title
    fun loadTileDataByTitle(title: String):TileData? {
        var tileDataTitle:TileData? = null
        viewModelScope.launch {
            val tileData = dao.getTileDataByTitle(title)
            tileDataTitle = tileData
        }
        return tileDataTitle
    }

    // Function to insert or update TileData
    fun insertOrUpdateTileData(tileData: TileData) {
        viewModelScope.launch {
            dao.insertTileData(tileData)
        }
    }

    // Function to delete TileData
    fun deleteTileData(tileData: TileData) {
        viewModelScope.launch {
            dao.deleteTileData(tileData)
        }
    }

    // Function to delete TileData by title
    fun deleteTileDataByTitle(title: String) {
        viewModelScope.launch {
            dao.deleteTileDataByTitle(title)
        }
    }
     suspend fun getAllTitle():List<TileData>{
        _tileDataState.value=dao.getAllTile()
        return dao.getAllTile()
    }
}