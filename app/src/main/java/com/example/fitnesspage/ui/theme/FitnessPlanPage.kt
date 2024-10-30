package com.example.fitnesspage.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

// This composable displays information pulled from backend

// Need to do:
// Pull information from dedicated backend
// Be able to add/delete the fetched exercises
// Make UI more modern

@Composable
fun FitnessPlanPage(
    // uses the navController passed in to navigate from and to screens
    // uses passed in answers from survey screen to display data -- needs to be changed
    answers: Map<String, Set<String>>,
    navController: NavController
) {
    // Variable to control the visibility of the warning dialog
    var showWarningDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text("Fitness Plan Recommendations", style = MaterialTheme.typography.headlineMedium)

            // Placeholder data -- needs to display exercises pulled from backend
            answers.forEach { (questionId, selectedAnswers) ->
                Text("Question Title: $questionId", fontWeight = FontWeight.Bold)
                selectedAnswers.forEach { answer ->
                    Text("- $answer")
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // Row for navigation buttons at the bottom of the screen
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            // shows the warning dialog once clicked
            Button(onClick = { showWarningDialog = true }) {
                Text("Back to Survey")
            }
            // Once this button is clicked, data from this screen should be collected
            // and pushed to the Workout page screen
            Button(onClick = { /* This should route to Workout page screen */ }) {
                Text("Start Workout")
            }
        }

        // This shows the warning dialog if the user presses the "Back to Survey" button
        if (showWarningDialog) {
            AlertDialog(
                // If clicked out of the warning pop up
                onDismissRequest = { showWarningDialog = false },
                title = { Text("Confirm") },
                text = { Text("Are you sure you want to go back to the survey page? Your responses will not be saved.") },
                // Confirm button
                confirmButton = {
                    Button(
                        onClick = {
                            // set the show warning variable back to false
                            showWarningDialog = false
                            // navigate back to survey screen
                            navController.navigate("survey") {
                                // but remove all screens from the back stack
                                // this allows for a true "refresh" of the survey screen
                                popUpTo("survey") { inclusive = true }
                            }
                        }
                    ) {
                        Text("Yes")
                    }
                },
                // dismiss button
                dismissButton = {
                    Button(
                        // set warning variable to false
                        onClick = { showWarningDialog = false }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}


