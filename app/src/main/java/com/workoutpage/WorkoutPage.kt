package com.workoutpage

import com.database.entities.Exercise
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.workoutpage.model.ExerciseModel
import com.workoutpage.model.TileData
import com.workoutpage.viewmodel.WorkoutViewModel
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Calendar

// **********************************************************************************

// represents tile data
// @Serializable
//data class TileData(
//    val name: String,
//    val weight: Double,
//    val sets: Int,
//    val reps: Int,
//    val time: Int,
//    val speed: Double
//)
/*      this needs to be muted temporarily
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                WorkoutPage(navController = rememberNavController()
                )
            }
        }
    }
}*/

// **************************************************************************

@Composable
fun WorkoutPage(navController: NavController, displayedExercises: List<Exercise>
                ,workoutViewModel: WorkoutViewModel) {
    // basic structure for the page - pass in navController for buttons
    Scaffold(
        topBar = {
            WorkoutTopBar(navController)
        },
        bottomBar = {
            WorkoutBottomBar(navController)
        },
        // area to display to exercise tile content and relevant messages
        // internal navigation is for tile to cards only
        content =     { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(innerPadding)
            ) {
                SharedContent()
                WorkoutTiles(workoutViewModel)
            }
        }

    )
}

/**  WorkoutTopBar - structured container for personal information
 *      Avatar and personal message
 *      inputs: navController - for back button
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutTopBar(navButtonController: NavController) {
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = { },
        colors = TopAppBarDefaults.topAppBarColors(
            Color.hsv(200f, 0.4f, 0.95f))
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween

    ) {
        IconButton(
            // handle back button navigation
            onClick = { handleBackButtonClick(navButtonController) }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Localized description"
            )
        }
        IconButton(onClick = {  }) {
            // display a generic avatar
            // TODO: replace this with your image resource or URL
            Image(
                imageVector = Icons.Filled.Face,
                contentDescription = "Avatar",
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Bottom)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            )
        }
        Text(
            "Today's Workout",
            Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
            fontSize = 24.sp
        )
    }

}

/** handleBackButtonClick - on click, navigates back to the calling page
 *      referenced from multiple locations
 */
fun handleBackButtonClick(navButtonController: NavController) {
    // handle the back button press
    if (navButtonController.currentBackStackEntry != null) {
        // navigate back if there's a previous screen in the stack
        navButtonController.popBackStack()
    } else {
        // do nothing
    }
}

/** WorkoutBottomBar - structured container for page navigation
 *      go to statistics page
 *      go to fitness page
 */
@Composable
fun WorkoutBottomBar(navController: NavController) {

    // Setting up the NavHost with the destinations
    BottomAppBar(
        containerColor = BottomAppBarDefaults.bottomAppBarFabColor
    ) {
        Row(
            modifier = Modifier.padding(12.dp, 4.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // navigate to the summary page
            StatsButton(navController)
            // navigate to the fitness page
            EditButton(navController)
        }
    }
}

/** SharedContent - basic greeting
 *      display the date and time
 */
@Composable
fun SharedContent() {
    // gather date/time information and format it
    // https://developer.android.com/reference/java/text/DateFormat
    val date = Calendar.getInstance().time
    val formatter = SimpleDateFormat.getDateInstance()
    val formatedDate = formatter.format(date)

    Spacer(modifier = Modifier.height(8.dp))
    // display the date and time
    Text(
        text = "Here is your workout routine for \n $formatedDate",
        fontSize = 20.sp,
        lineHeight = 28.sp,
        modifier = Modifier.padding()
            .height(60.dp)
    )
}

/** WorkoutTiles - set up the temporary list and handle navigation
 *          temporarily declare a list of exercises with values
 *          pass these values to the individual tiles
 *          handle navigation to an Exercise card
 */
@Composable
fun WorkoutTiles(viewModel: WorkoutViewModel) {
    // create the local navController
    val navTileController = rememberNavController()

    // sample data
    // TODO: hook up the real data and delete this section

    val tiles = viewModel.tiles.observeAsState(initial = emptyList())
    // Setting up the local NavHost with the Exercise card as the destination
    // NO OUTSIDE NAVIGATION - primary button navigation is handed on the fitness page
    NavHost(
        navController = navTileController,
        startDestination = "workout",
        modifier = Modifier.fillMaxSize()
            .padding(4.dp)
    ) {
        // local home screen route
        composable("workout") { TileList(tiles.value, navTileController) }

        // route to the Exercise card and pass in the arguments
        // TODO: update this for more complex data
        composable("exercise/{title}/{weight}/{sets}/{reps}") { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title")
            val weight = backStackEntry.arguments?.getString("weight")?.toDoubleOrNull()
            val sets = backStackEntry.arguments?.getString("sets")?.toIntOrNull()
            val reps = backStackEntry.arguments?.getString("reps")?.toIntOrNull()
            val time = backStackEntry.arguments?.getString("time")?.toIntOrNull()
            val speed = backStackEntry.arguments?.getString("speed")?.toIntOrNull()
            val exerciseModel = ExerciseModel(
                title = title ?: "Exercise",
                weight = weight ?: 0.0,
                sets = sets ?: 1,
                reps = reps ?: 0,
                time = time ?: 0,
                speed = speed ?: 0
            )
            ExerciseCard(
                navTileController, exerciseModel
            )
        }
    }
}

/** TileList - display of tiles in a lazy column
 *      populate the contents of one object for each tile
 */
@Composable
fun TileList(tiles: List<ExerciseModel>, navTileController: NavController) {
    LazyColumn {
        items(tiles) { tile ->
            // each individual tile
            TileItem(tile, navTileController)
        }
    }
}

/** TileItem - handle each tile and call the Exercise card
 *      generate the individual tile
 *      set the button click navigation
 */
@Composable
fun TileItem(tile: ExerciseModel, navTileController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                navTileController.navigate(
                    "exercise/${tile.title}/${tile.weight}/${tile.sets}/${tile.reps}"
                )
            },
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.Blue)
    ) {
        // tile for standard exercise (reps/sets/weight)
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "${tile.title}       ${tile.weight} lbs",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(
                modifier = Modifier.height(4.dp)
            )
            Text(
                text = "${tile.sets} sets of ${tile.reps} repetitions",
                fontSize = 16.sp,
                color = Color.DarkGray
            )
        }
    }
}
/** StatsButton - handle navigation to Summary page
 *      handle button click and navigation
 */
@Composable
fun StatsButton(navController: NavController) {
    OutlinedButton(
        modifier = Modifier.padding(12.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(Color.Blue),
        elevation = ButtonDefaults.buttonElevation(4.dp),
        onClick = { navController.navigate("summary") }
    ) {
        Text(
            fontSize = 16.sp,
            text = "View Progress"
        )
    }
}

/** EditButton - handle navigation back to Fitness page
 *      handle button click and navigation
 */
@Composable
fun EditButton(navController: NavController) {
    OutlinedButton(
        modifier = Modifier.padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(Color.Blue),
        elevation = ButtonDefaults.buttonElevation(4.dp),
        onClick = { handleBackButtonClick(navController) }
    ) {
        Text(
            text = "Change It Up",
            fontSize = 16.sp
        )
    }
}

// **************************************************************************
