package com.database

import androidx.room.Database
import androidx.room.RoomDatabase

import com.database.dao.ExerciseDao
import com.database.dao.FitnessPlanDao
import com.database.dao.FitnessPlanExerciseDao
import com.database.dao.UserDao
import com.database.dao.ProfileDao
import com.database.dao.TileDataDao
import com.database.dao.WorkoutDao

import com.database.entities.User
import com.database.entities.Profile
import com.database.entities.FitnessPlan
import com.database.entities.Exercise
import com.database.entities.FitnessPlanExercise
import com.database.entities.Workout
import com.database.entities.WorkoutExercise
import com.database.entities.WorkoutSet
import com.workoutpage.model.TileData

@Database(
    entities = [
        User::class, Profile::class, FitnessPlan::class,
        Exercise::class, FitnessPlanExercise::class, Workout::class,
        WorkoutExercise::class, WorkoutSet::class,TileData::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun profileDao(): ProfileDao
    abstract fun workoutDao(): WorkoutDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun fitnessPlanDao(): FitnessPlanDao
    abstract fun fitnessPlanExerciseDao(): FitnessPlanExerciseDao
    abstract fun tileDao():TileDataDao
}
