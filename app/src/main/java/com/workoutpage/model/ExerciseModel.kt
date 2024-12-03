package com.workoutpage.model

data class ExerciseModel(
    val title: String,
    val weight: Double,
    val sets: Int,
    val reps: Int,
    val time:Int,
    val speed: Int
)
