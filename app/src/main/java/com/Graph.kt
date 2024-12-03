package com

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.database.AppDatabase
import com.database.predefinedExercises
import com.database.repositories.ExerciseRepository
import com.database.repositories.FitnessPlanExerciseRepository
import com.database.repositories.FitnessPlanRepository
import com.database.repositories.ProfileRepository
import com.database.repositories.UserRepository
import com.database.repositories.WorkoutRepository
import com.workoutpage.viewmodel.WorkoutViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// singleton database class container to be used throughout the entire application
object Graph {
    lateinit var database: AppDatabase

    // lazy initialization of the repositories DAO wrappers
    // to be used in the view models throughout the application
    val profileRepository by lazy{
        ProfileRepository(profileDao = database.profileDao())
    }
    val tiltDataViewModel by lazy{
        WorkoutViewModel( database.tileDao() )
    }
    val userRepository by lazy{
        UserRepository(userDao = database.userDao())
    }

    val workoutRepository by lazy{
        WorkoutRepository(workoutDao = database.workoutDao())
    }

    val exerciseRepository by lazy{
        ExerciseRepository(exerciseDao = database.exerciseDao())
    }

    val fitnessPlanRepository by lazy{
        FitnessPlanRepository(fitnessPlanDao = database.fitnessPlanDao())
    }

    val fitnessPlanExerciseRepository by lazy{
        FitnessPlanExerciseRepository(fitnessPlanExerciseDao = database.fitnessPlanExerciseDao())
    }

    // Coroutine scope for background operations
    // Optimized for I/O operations including database writes
    private val ioScope = CoroutineScope(Dispatchers.IO)

    // create an instance of the sqlite room app database
    // should be called from the application class on startup
    fun provide(context: Context){
        Log.d("Graph", "initializing database")
        database = Room.databaseBuilder(context, AppDatabase::class.java, "personal-trainer.db")
            .fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    Log.d("Graph", "Database created")
                    // Coroutine inserts the exercises in the background thread, without blocking the main thread
                    ioScope.launch {
                        Log.d("Graph", "inserting exercises")
                        database.exerciseDao().insertAll(predefinedExercises)
                    }
                }
            })
            .build()
        Log.d("Graph", "database initialized")
        // dummy query
        // Room does not call callbacks until the database is accessed
        database.query("select 1", null)
    }


}