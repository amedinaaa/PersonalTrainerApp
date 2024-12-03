package com.example.fitnesspage

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.Graph
import com.database.entities.Exercise
import com.example.fitnesspage.navigation.NavigationItem
import com.example.fitnesspage.pages.FitnessPlanPage
import com.example.fitnesspage.pages.SurveyScreen
import com.example.fitnesspage.ui.theme.FitnessPageTheme
import com.workoutpage.SummaryLayout
import com.workoutpage.WorkoutPage
import com.workoutpage.viewmodel.WorkoutViewModel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class MainActivity : ComponentActivity() {
//private val workoutViewModel:WorkoutViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FitnessPageTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FitnessPlanApp(Graph.tiltDataViewModel)
                }
            }
        }
    }
}
@Composable
fun FitnessPlanApp(
    // gets an instance of the view model
//    viewModel: FitnessAppModel = viewModel(),
    // This manages navigation between the different screens in our app
    // rememberNavController ensures navigation controller is created once
    workoutViewModel: WorkoutViewModel,
    navController: NavHostController = rememberNavController()
)
{
    // retrieves the current state from the view model
    // collectAsState() collects the latest value from the state flow and converts it into a State object
    // the returned value (i.e. uiState.value) from this is then assigned to uiState with "by"
    //    val uiState by viewModel.uiState.collectAsState()
    // NavHost defines the navigation path for the app
    // sets startDestination to survey page -- needs to be changed
    NavHost(navController = navController, startDestination = NavigationItem.SurveyScreen.route ){
        composable(route = NavigationItem.SurveyScreen.route) {
            SurveyScreen{ answers ->
                val answersJson = Uri.encode(Json.encodeToString(answers))
                navController.navigate("${NavigationItem.FitnessPlanScreen.route}/$answersJson")
            }
        }
        composable(
            "${NavigationItem.FitnessPlanScreen.route}/{answersJson}",
            arguments = listOf(navArgument("answersJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val answersJson = backStackEntry.arguments?.getString("answersJson") ?: "{}"
            val answers = Json.decodeFromString<Map<String, Set<String>>>(answersJson)
            FitnessPlanPage(answers = answers, navController = navController)
        }
        // Workout Page with displayedExercises
        composable(
            route = "${NavigationItem.WorkoutScreen.route}/{displayedExercisesJson}",
            arguments = listOf(navArgument("displayedExercisesJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val displayedExercisesJson = backStackEntry.arguments?.getString("displayedExercisesJson") ?: "[]"
            val displayedExercises = Json.decodeFromString<List<Exercise>>(displayedExercisesJson)
            WorkoutPage(navController = navController, displayedExercises = displayedExercises,workoutViewModel)
        }
        composable(route = NavigationItem.SummaryScreen.route) { SummaryLayout(navController,workoutViewModel) }
    }
}
