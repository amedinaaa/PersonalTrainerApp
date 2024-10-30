package com.example.fitnesspage

import android.net.Uri
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import com.example.fitnesspage.model.FitnessAppModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
//import com.example.fitnesspage.ui.theme.SurveyScreen
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import com.example.fitnesspage.ui.theme.UiState
import androidx.compose.runtime.getValue
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.fitnesspage.ui.theme.FitnessPlanPage
import com.example.fitnesspage.ui.theme.SurveyScreen
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
//import com.example.fitnesspage.ui.theme.SurveyView

// general knowledge:
// In Jetpack Compose, state refers to any piece of data that can change over time and needs to be reflected in the UI
//  When the state changes, the UI is automatically recomposed to reflect the updated state

// Will handle navigation and state handling for our app
@Composable
fun FitnessPlanApp(
    // gets an instance of the view model
    viewModel: FitnessAppModel = viewModel(),
    // This manages navigation between the different screens in our app
    // rememberNavController ensures navigation controller is created once
    navController: NavHostController = rememberNavController()
)
{
    // retrieves the current state from the view model
    // collectAsState() collects the latest value from the state flow and converts it into a State object
    // the returned value (i.e. uiState.value) from this is then assigned to uiState with "by"
    val uiState by viewModel.uiState.collectAsState()
    // NavHost defines the navigation path for the app
    // sets startDestination to survey page -- needs to be changed
    NavHost(navController = navController, startDestination = "survey" ){
        composable(route = "survey") {
            SurveyScreen(viewModel = viewModel, uiState = uiState) { answers ->
                val answersJson = Uri.encode(Json.encodeToString(answers))
                navController.navigate("fitnessPlan/$answersJson")

            }

//            SurveyView()
        }
        composable(
            "fitnessPlan/{answersJson}",
            arguments = listOf(navArgument("answersJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val answersJson = backStackEntry.arguments?.getString("answersJson") ?: "{}"
            val answers = Json.decodeFromString<Map<String, Set<String>>>(answersJson)
            FitnessPlanPage(answers = answers, navController = navController)
        }
    }
}