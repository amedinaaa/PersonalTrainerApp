package com.workoutpage

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.Graph
import com.workoutpage.model.ExerciseModel
import com.workoutpage.model.TileData

/** ExerciseCard - create the body of the the card
 *      Inputs: navController & TileData
 *      set up the structure of the card
 *      set up scrolling
 *      fill in the values
 */
@Composable
fun ExerciseCard(navController: NavController, data: ExerciseModel) {
    // outer container - the padding makes the card fit well
    Surface(
        modifier = Modifier.padding(4.dp)
    ) {
        // inner container - for proper placement of the contents
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.hsv(200f, 0.3f, 0.99f))
                .border(
                    border = BorderStroke(
                        4.dp,
                        color = Color.Gray
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            // set up the scroll state
            val scrollState = rememberScrollState()

            // make larger lists scrollable
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {
                // generate a summary
                if (data.reps > 1) {
                    if (data.weight > 0) {

                        ExerciseTextResistance(data)

                        // create the individual input fields & handle data
                        ExerciseInputsResistance(data)
                    }
                    else {
                        ExerciseTextBody(data)

                        // create the individual input fields & handle data
                        ExerciseInputsBody(data)
                    }
                }
                else {
                    if (data.time > 0) {
                        ExerciseTextTime(data)

                        // create the individual input fields & handle data
                        ExerciseInputsTime(data)
                    }
                    else {
                        ExerciseTextDistance(data)

                        // create the individual input fields & handle data
                        ExerciseInputsDistance(data)
                    }
                }

                // navigate back to the Workout page
                CloseButton(navController)
            }
        }
    }
}

// **************************************************************************

/** ExerciseTextBody - offer encouragement and summarize the exercise
 *      identify the exercise
 *      outline the exercise goal
 */
@Composable
fun ExerciseTextBody(data: ExerciseModel) {
    Text(
        text = "Get Yourself Ready!",
        modifier = Modifier.padding(20.dp, 8.dp),
        fontSize = 36.sp
    )
    Text(
        text = data.title,
        modifier = Modifier.padding(8.dp, 4.dp),
        fontSize = 28.sp
    )
    Text(
        text = "Let's do ${data.sets} sets of ${data.reps}",
        modifier = Modifier.padding(8.dp, 4.dp),
        fontSize = 28.sp
    )
}

/** ExerciseTextResistance - offer encouragement and summarize the exercise
 *      identify the exercise
 *      outline the exercise goal
 */
@Composable
fun ExerciseTextResistance(data: ExerciseModel) {
    Text(
        text = "Let's Do This!",
        modifier = Modifier.padding(20.dp, 8.dp),
        fontSize = 36.sp
    )
    Text(
        text = data.title,
        modifier = Modifier.padding(8.dp, 4.dp),
        fontSize = 28.sp
    )
    Text(
        text = " ${data.sets} sets of ${data.reps} at ${data.weight} lbs",
        modifier = Modifier.padding(8.dp, 4.dp),
        fontSize = 28.sp
    )
}

/** ExerciseTextTime - offer encouragement and summarize the exercise
 *      identify the exercise
 *      outline the exercise goal
 */
@Composable
fun ExerciseTextTime(data: ExerciseModel) {
    val minutes: Int =  data.time / 60
    val seconds: Int = data.time % 60

    Text(
        text = "Time to Work Out!",
        modifier = Modifier.padding(20.dp, 8.dp),
        fontSize = 36.sp
    )
    Text(
        text = data.title,
        modifier = Modifier.padding(8.dp, 4.dp),
        fontSize = 28.sp
    )
    if (minutes > 0) {
        Text(
            text = " for $minutes minutes, $seconds seconds",
            modifier = Modifier.padding(8.dp, 4.dp),
            fontSize = 28.sp
        )
    }
    else {
        Text(
            text = " for $seconds seconds",
            modifier = Modifier.padding(8.dp, 4.dp),
            fontSize = 28.sp
        )
    }
}

/** ExerciseTextDistance - offer encouragement and summarize the exercise
 *      identify the exercise
 *      outline the exercise goal
 */
@Composable
fun ExerciseTextDistance(data: ExerciseModel) {
    Text(
        text = "Ready to go",
        modifier = Modifier.padding(20.dp, 8.dp),
        fontSize = 36.sp
    )
    Text(
        text = "the Distance?",
        modifier = Modifier.padding(20.dp, 8.dp),
        fontSize = 36.sp
    )
    Text(
        text = "${data.title} for ${data.speed} miles.",
        modifier = Modifier.padding(8.dp, 4.dp),
        fontSize = 28.sp
    )
}

// **************************************************************************

/** ExerciseInputsBody - show text fields, gather input data, and store it
 *      create lists for data storage
 *      display input fields for each rep
 *      save the data to the list
 *      temp - display the data to the screen
 *      TODO: send back to the database
 */
@Composable
fun ExerciseInputsBody(data: ExerciseModel) {
    // Generated logic using chatGPT
    // List of MutableState for each TextField
    val repsFieldStates = remember { MutableList(data.sets) { mutableStateOf("") } }

    // List to store saved inputs
    val savedInputReps = remember { mutableStateListOf<Int>() }

    // list for each input field
    for (set in 1 .. data.sets) {
        Column {
            Row(
                modifier = Modifier.align(Alignment.Start)
            )
            {
                // state the set number
                Text(
                    text = "Set $set:",
                    modifier = Modifier.padding(4.dp, 4.dp),
                    fontSize = 24.sp,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(
                    modifier = Modifier.width(8.dp)
                )
                // gather the number of reps done
                // use number pad to gather integers only
                TextField(

                    value = repsFieldStates[set - 1].value,
                    onValueChange = {
                        repsFieldStates[set - 1].value = it
                    },
                    modifier = Modifier
                        .width(48.dp)
                        .height(28.dp)
                        .padding(12.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    )
                )
                Text(
                    text = " reps  ",
                    modifier = Modifier.padding(0.dp, 8.dp),
                    fontSize = 20.sp,
                )
                // create a save button for this input
                FilledIconButton(
                    modifier = Modifier
                        .padding(8.dp, 0.dp)
                        .size(32.dp),
                    onClick = {
                        // Save current inputs to the savedInputs list
                        savedInputReps.clear()
                        savedInputReps.addAll(repsFieldStates.map { it.value.toIntOrNull() ?: 0 })
                    },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "checkmark"
                    )
                }
                Spacer(
                    modifier = Modifier.width(20.dp)
                )
                // create an edit button for this input
                FilledIconButton(
                    modifier = Modifier
                        .padding(8.dp, 0.dp)
                        .size(32.dp),
                    onClick = {
                        // Set the saved value to zero
                        savedInputReps[set - 1] = 0
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "edit"
                    )
                }
            }
        }
    }
    // testing only
    savedInputReps.forEachIndexed { index, input ->
        Text(" Reps Entered #${index + 1}: $input")
    }
}

/** ExerciseInputsResistance - show text fields, gather input data, and store it
 *      create lists for data storage
 *      display input fields for each rep
 *      save the data to the list
 *      temp - display the data to the screen
 *      TODO: send back to the database
 */
@Composable
fun ExerciseInputsResistance(data: ExerciseModel) {
    // Generated logic using chatGPT
    val repsFieldStates = remember { MutableList(data.sets) { mutableStateOf("") } }
    val weightFieldState = remember { MutableList(data.sets) { mutableStateOf("") } }

    // List to store saved inputs
    val savedInputReps = remember { mutableStateListOf<Int>() }
    val savedInputWeights = remember { mutableStateListOf<Double>() }
    var totalReps by remember { mutableIntStateOf(0) }
    var totalWeight by remember { mutableDoubleStateOf(0.0) }
    // list for each input field
    for (set in 1 .. data.sets) {
        Column {
            Row(
                modifier = Modifier.align(Alignment.Start)
            )
            {
                // state the set number
                Text(
                    text = "Set $set:",
                    modifier = Modifier.padding(4.dp, 4.dp),
                    fontSize = 24.sp,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(
                    modifier = Modifier.width(8.dp)
                )
                // gather the number of reps done
                // use number pad to gather integers only
                TextField(

                    value = repsFieldStates[set - 1].value,
                    onValueChange = {
                        repsFieldStates[set - 1].value = it
                    },
                    modifier = Modifier.width(48.dp)
                        .height(28.dp)
                        .padding(12.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    )
                )
                Text(
                    text = " reps  ",
                    modifier = Modifier.padding(0.dp, 8.dp),
                    fontSize = 20.sp,
                )
                // gather the weight used
                // use number pad to gather integers/doubles only
                TextField(

                    value = weightFieldState[set - 1].value,
                    onValueChange = {
                        weightFieldState[set - 1].value = it
                    },
                    modifier = Modifier.width(52.dp)
                        .height(32.dp)
                        .padding(4.dp),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    )
                )
                Text(
                    text = " lbs ",
                    modifier = Modifier.padding(0.dp, 8.dp),
                    fontSize = 20.sp,
                )
                // create a save button for this input
                FilledIconButton(
                    modifier = Modifier.padding(8.dp, 0.dp)
                        .size(32.dp),
                    onClick = {
                        // Save current inputs to the savedInputs list
                        savedInputReps.clear()
                        savedInputReps.addAll(repsFieldStates.map { it.value.toIntOrNull() ?: 0 })
                        savedInputWeights.clear()
                        savedInputWeights.addAll(weightFieldState.map { it.value.toDoubleOrNull() ?: 0.0 })
                        // Update totals
                        totalReps = savedInputReps.sum()
                        totalWeight = savedInputWeights.sum()
                        Graph.tiltDataViewModel.insertOrUpdateTileData(TileData(data.title,totalWeight,data.sets,totalReps))
                        Log.d("savedWeights",data.sets.toString() +" "+totalReps.toString() +" | "+totalWeight.toString())
                    },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "checkmark"
                    )
                }
                Spacer(
                    modifier = Modifier.width(20.dp)
                )
                // create an edit button for this input
                FilledIconButton(
                    modifier = Modifier.padding(4.dp, 0.dp)
                        .size(28.dp),
                    onClick = {
                        /*
                        // savedInputReps.clear()
                        if (repsFieldStates.isNotEmpty())
                        {
                            repsFieldStates.removeLast()
                        //repsFieldStates.map { it.value.toIntOrNull() ?: 0 })
                        }
                        else {
                            // do nothing
                        }
                        // savedInputWeights.clear()
                        if (weightFieldState.isNotEmpty())
                        {
                            weightFieldState.removeLast()
                            // savedInputWeights.addAll(weightFieldState.map { it.value.toDoubleOrNull() ?: 0.0 })
                        }
                        else {
                            // do nothing
                        }*/
                    },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit,
                        contentDescription = "edit"
                    )
                }
            }
        }
    }
    // testing only
    savedInputReps.forEachIndexed { index, input ->
        Text(" Reps Entered #${index + 1}: $input")
    }
    savedInputWeights.forEachIndexed { index, input ->
        Text(" Weights Entered #${index + 1}: $input")
    }
}

/** ExerciseInputsTime - show text fields, gather input data, and store it
 *      create lists for data storage
 *      display input fields for each rep
 *      save the data to the list
 *      temp - display the data to the screen
 *      TODO: send back to the database
 */
@Composable
fun ExerciseInputsTime(data: ExerciseModel) {
    // save as seconds an convert to minutes/seconds for display
    val savedInputMinutes = remember { mutableStateOf("") }
    val savedInputSeconds = remember { mutableStateOf("") }
    var savedMinutesValue by remember { mutableIntStateOf(0) }
    var savedSecondsValue by remember { mutableIntStateOf(0) }
    var savedTime by remember { mutableIntStateOf(0) }

    Column {
        Row(
            modifier = Modifier.align(Alignment.Start)
        )
        {
            // state the set number
            Text(
                text = "This session:",
                modifier = Modifier.padding(4.dp, 4.dp),
                fontSize = 24.sp,
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(
                modifier = Modifier.width(8.dp)
            )
            // gather the number of reps done
            // use number pad to gather integers only
            TextField(
                value = savedInputMinutes.value,
                onValueChange = {
                    savedInputMinutes.value = it
                },
                modifier = Modifier
                    .width(48.dp)
                    .height(28.dp)
                    .padding(12.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
            )
            Text(
                text = " minutes  ",
                modifier = Modifier.padding(0.dp, 8.dp),
                fontSize = 20.sp,
            )
            // use number pad to gather integers/doubles only
            TextField(

                value = savedInputSeconds.value,
                onValueChange = {
                    savedInputSeconds.value = it
                },
                modifier = Modifier
                    .width(52.dp)
                    .height(32.dp)
                    .padding(4.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
            )
            Text(
                text = " seconds ",
                modifier = Modifier.padding(0.dp, 8.dp),
                fontSize = 20.sp,
            )
            // create a save button for this input
            FilledIconButton(
                modifier = Modifier
                    .padding(8.dp, 0.dp)
                    .size(32.dp),
                onClick = {
                    // Save current inputs to the savedInputs list
                    savedTime = (savedInputMinutes.value.toIntOrNull() ?: 1) * 60 + (savedInputSeconds.value.toIntOrNull() ?: 1)
                },
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "checkmark"
                )
            }
            Spacer(
                modifier = Modifier.width(20.dp)
            )
            // create an edit button for this input
            FilledIconButton(
                modifier = Modifier
                    .padding(8.dp, 0.dp)
                    .size(32.dp),
                onClick = {
                    // Set the saved value to zero
                    savedSecondsValue = 0
                    savedMinutesValue = 0
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "edit"
                )
            }
        }
        // testing only
        Text(" Time Entered: $savedTime seconds")
    }
}

/** ExerciseInputsDistance - show text fields, gather input data, and store it
 *      create lists for data storage
 *      display input fields for each rep
 *      save the data to the list
 *      temp - display the data to the screen
 *      TODO: send back to the database
 */
@Composable
fun ExerciseInputsDistance(data: ExerciseModel) {
    // List to store saved inputs
    // save as seconds an convert to minutes/seconds for display
    val savedInputDistance = remember { mutableStateOf("") }
    var savedDistanceValue by remember { mutableStateOf(0) }
    // list for each input field
    Column {
        Row(
            modifier = Modifier.align(Alignment.Start)
        )
        {
            // state the set number
            Text(
                text = "This session:",
                modifier = Modifier.padding(4.dp, 4.dp),
                fontSize = 24.sp,
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(
                modifier = Modifier.width(8.dp)
            )
            // gather the number of reps done
            // use number pad to gather integers only
            TextField(

                value = savedInputDistance.value,
                onValueChange = {
                    savedInputDistance.value = it
                },
                modifier = Modifier
                    .width(48.dp)
                    .height(28.dp)
                    .padding(12.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
            )
            Text(
                text = " miles  ",
                modifier = Modifier.padding(0.dp, 8.dp),
                fontSize = 20.sp,
            )
            // create a save button for this input
            FilledIconButton(
                modifier = Modifier
                    .padding(8.dp, 0.dp)
                    .size(32.dp),
                onClick = {
                    // Save current inputs to the savedInputs list
                    savedDistanceValue = savedInputDistance.value.toIntOrNull() ?: 1
                },
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "checkmark"
                )
            }
            Spacer(
                modifier = Modifier.width(20.dp)
            )
            // create an edit button for this input
            FilledIconButton(
                modifier = Modifier
                    .padding(8.dp, 0.dp)
                    .size(32.dp),
                onClick = {
                    // Set the saved value to zero
                    savedDistanceValue = 0
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "edit"
                )
            }
        }
        // testing only
        Text(" Time Entered: $savedDistanceValue seconds")
    }
}

// **************************************************************************

@Composable
fun CloseButton (navController: NavController) {
    //
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp, 8.dp)
    ) {
        Spacer(
            Modifier.width(40.dp)
        )
        Button(
            modifier = Modifier.padding(4.dp),
            elevation = ButtonDefaults.buttonElevation(8.dp),
            onClick = {  }
        ) {
            Text(text = "Save All")
        }
        Spacer(
            Modifier.width(40.dp)
        )
        Button(
            modifier = Modifier.padding(4.dp),
            elevation = ButtonDefaults.buttonElevation(8.dp),
            onClick = {
                // Pop the back stack to return to the previous screen (Home screen)
                navController.popBackStack()

            }) {
            Text(text = "Close")
        }
        Spacer(
            Modifier.width(40.dp)
        )
    }
}

