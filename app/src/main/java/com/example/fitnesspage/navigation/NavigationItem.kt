package com.example.fitnesspage.navigation

sealed class NavigationItem(val route:String) {
    data object SurveyScreen:NavigationItem(Screens.survey.name)
    data object FitnessPlanScreen:NavigationItem(Screens.fitnessPlan.name)
    data object WorkoutScreen:NavigationItem(Screens.workout.name)
    data object SummaryScreen:NavigationItem(Screens.summary.name)
}