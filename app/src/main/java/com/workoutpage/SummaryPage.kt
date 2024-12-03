package com.workoutpage

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.Graph
import com.workoutpage.model.TileData
import com.workoutpage.viewmodel.WorkoutViewModel
import java.text.SimpleDateFormat
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryLayout(navController: NavController, workoutViewModel: WorkoutViewModel) {
    val date = Calendar.getInstance().time
    val formatter = SimpleDateFormat.getDateInstance()
    val formattedDate = formatter.format(date)

    val defaultExercises = listOf(
        TileData("Leg Press", 0.0, 0, 0),
        TileData("Leg Extension", 0.0, 0, 0),
        TileData("Chest Press", 0.0, 0, 0),
        TileData("Bicep Curl", 0.0, 0, 0),
        TileData("Squat", 0.0, 0, 0),
        TileData("Lat Pull", 0.0, 0, 0)
    )

    LaunchedEffect(true) {
        Graph.tiltDataViewModel.getAllTitle()
    }
    val fetchedExercises = Graph.tiltDataViewModel.tileDataState.collectAsState().value

    // Merge default exercises with fetched data
    val exercises = defaultExercises.map { default ->
        fetchedExercises.find { it?.title == default.title } ?: default
    }

    Log.d("savedWeights", exercises.toString())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Summary Page", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            // Display date
            Text(
                text = "Date: $formattedDate",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            ExerciseGraphAndTable(exercises)

            // Enable horizontal scrolling for the table layout
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
            ) {
                // Left header column for "Weight", "Sets", and "Reps"
                ColumnWithDivider(
                    content = {
                        Text(" ", fontWeight = FontWeight.Bold, modifier = Modifier.padding(8.dp))
                        HorizontalDivider(color = Color.Black, thickness = 1.dp)
                        Text("Weight", fontWeight = FontWeight.Bold, modifier = Modifier.padding(8.dp))
                        HorizontalDivider(color = Color.Black, thickness = 1.dp)
                        Text("Sets", fontWeight = FontWeight.Bold, modifier = Modifier.padding(8.dp))
                        HorizontalDivider(color = Color.Black, thickness = 1.dp)
                        Text("Reps", fontWeight = FontWeight.Bold, modifier = Modifier.padding(8.dp))
                    }
                )

                // Exercise columns for each exercise
                exercises.forEach { exercise ->
                    ColumnWithDivider(
                        content = {
                            Text(
                                text = exercise.title,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(8.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            HorizontalDivider(color = Color.Black, thickness = 1.dp)
                            Text(exercise.weight.toString(), modifier = Modifier.padding(8.dp))
                            HorizontalDivider(color = Color.Black, thickness = 1.dp)
                            Text(exercise.sets.toString(), modifier = Modifier.padding(8.dp))
                            HorizontalDivider(color = Color.Black, thickness = 1.dp)
                            Text(exercise.reps.toString(), modifier = Modifier.padding(8.dp))
                        }
                    )
                }
            }

            // Draw a bottom border for the table
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = Color.Black
            )
        }
    }
}


@Composable
fun ExerciseGraphAndTable(exercises: List<TileData?>) {
    val aggregatedData = aggregateExerciseData(exercises)
    val nonNullExercises = exercises.filterNotNull()
    val maxWeight = nonNullExercises.maxOfOrNull { it.weight } ?: 1f

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Graph Section
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            val barWidth = (maxWidth - 16.dp * 3) / (aggregatedData.size * 1.5f)

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Canvas for the bars
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    aggregatedData.forEachIndexed { index, exercise ->
                        val weight = exercise.weight ?: 0f
                        val barHeight = (weight.toDouble() / maxWeight.toDouble()) * size.height

                        drawRect(
                            color = Color.Blue,
                            topLeft = Offset(
                                x = index * (barWidth.toPx() + barWidth.toPx()),
                                y = size.height - barHeight.toFloat()
                            ),
                            size = Size(barWidth.toPx(), barHeight.toFloat())
                        )
                    }
                }

                // Labels aligned below the bars
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(barWidth)
                ) {
                    aggregatedData.forEach { exercise ->
                        Box(
                            modifier = Modifier
                                .width(barWidth)
                                .padding(top = 8.dp)
                        ) {
                            Text(
                                text = exercise.title,
                                textAlign = TextAlign.Center,
                                fontSize = 12.sp,
                                modifier = Modifier.align(Alignment.Center),
                                maxLines = 2
                            )
                        }
                    }
                }
            }
        }
    }
}






fun aggregateExerciseData(exercises: List<TileData?>): List<TileData> {
    return exercises
        .filterNotNull()
        .groupBy { it.title }
        .map { (title, items) ->
            TileData(
                title = title,
                weight = items.sumOf { it.weight.toDouble() ?: 0.0 },
                sets = items.sumOf { it.sets ?: 0 },
                reps = items.sumOf { it.reps ?: 0 }
            )
        }
}




// Helper composable to create a column with a right vertical divider
@Composable
fun ColumnWithDivider(content: @Composable ColumnScope.() -> Unit) {
    Row {
        Column(modifier = Modifier.width(80.dp)) {
            content()
        }
        Divider(
            color = Color.Black,
            modifier = Modifier
                .height(200.dp)
                .width(1.dp)
        )
    }
}


