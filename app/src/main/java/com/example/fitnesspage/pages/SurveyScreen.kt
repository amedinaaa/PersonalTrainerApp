package com.example.fitnesspage.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text

import androidx.compose.ui.Modifier

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

import com.example.fitnesspage.model.QuestionType
import com.example.fitnesspage.model.SingleOptionUI
import com.example.fitnesspage.model.SurveyModel
import com.example.fitnesspage.model.SurveyText


@Composable
fun SurveyView(onFinishSurvey: (Map<String,Set<String>>) -> Unit) {
    val sampleSurvey = listOf(
        SurveyModel(
            questionType = QuestionType.SINGLE_CHOICE,
            questionId = "id1",
            questionTitle = "What is your primary fitness goal?",
            answers = listOf("Build strength and muscle mass", "Lose weight", "Improve flexibility"),
        ),
        SurveyModel(
            questionType = QuestionType.SINGLE_CHOICE,
            questionId = "id2",
            questionTitle = "What is your fitness level?",
            answers = listOf("Novice/Beginner", "Intermediate", "Advance"),
        ),
        SurveyModel(
            questionType = QuestionType.SINGLE_CHOICE,
            questionId = "id3",
            questionTitle = "What is your targeted muscle group?",
            answers = listOf("Upper-body", "Lower-body", "Full-body"),
        )
    )
    // Define answer mappings for backend values
    val answerMappings = mapOf(
        "id1" to mapOf(
            "Build strength and muscle mass" to "STRENGTH",
            "Lose weight" to "CARDIO",
            "Improve flexibility" to "FLEXIBILITY"
        ),
        "id2" to mapOf(
            "Novice/Beginner" to "BEGINNER",
            "Intermediate" to "INTERMEDIATE",
            "Advance" to "ADVANCE"
        ),
        "id3" to mapOf(
            "Upper-body" to "UPPER",
            "Lower-body" to "LOWER",
            "Full-body" to "FULL"
        )
    )

    SurveyScreen2(
        survey = sampleSurvey,
        backgroundColor = Color.White,
        singleOptionUI = SingleOptionUI(
            questionTitle = SurveyText(
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.SansSerif,

            ),
            answer = SurveyText(
                color = Color.White,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.SansSerif,
            ),
            selectedColor = Color.White,
            unSelectedColor = Color.Gray,
            borderColor = Color.Gray
        ),
        onFinishButtonClicked = { answers ->
            // Convert answers to backend values
            val backendAnswers = answers.mapValues { (questionId, selectedAnswers) ->
                selectedAnswers.mapNotNull { answer ->
                    answerMappings[questionId]?.get(answer)
                }.toSet()
            }
            onFinishSurvey(backendAnswers) // Pass converted answers to callback
        }
    )
}

@Composable
fun SurveyScreen2(
    survey: List<SurveyModel>,
    backgroundColor: Color = Color.White,
    singleOptionUI: SingleOptionUI = SingleOptionUI(),
    onFinishButtonClicked: (Map<String, Set<String>>) -> Unit // Pass answers when button clicked
) {
    // Track selected answers
    var answers by remember { mutableStateOf<Map<String, Set<String>>>(emptyMap()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
//            .background(backgroundColor)
            .background(Color(color = 0xFFF5F5F5))

            .clip(RoundedCornerShape(12.dp)) // Add rounded corners
            .padding(16.dp)
    ) {
        items(survey.size) { index ->
            val question = survey[index]
            when (question.questionType) {
                QuestionType.SINGLE_CHOICE -> {
                    SingleChoiceQuestion(
                        question = question,
                        singleOptionUI = singleOptionUI,
                        selectedAnswer = answers[question.questionId]?.firstOrNull(),
                        onAnswerSelected = { selectedAnswer ->
                            // Update answers map with the selected answer
                            answers = answers + (question.questionId to setOf(selectedAnswer))
                        }
                    )
                    Spacer(modifier = Modifier.padding(vertical = 4.dp))
                }
            }
        }

        // Show finish button when all questions are answered
        if (answers.size == survey.size) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { onFinishButtonClicked(answers) }, // Pass answers to callback
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(text = "Finish Survey")
                }
            }
        }
    }
}



@Composable
fun SingleChoiceQuestion(
    question: SurveyModel,
    selectedAnswer: String?,
    singleOptionUI: SingleOptionUI = SingleOptionUI(),
    onAnswerSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()

            .shadow(1.dp)
            .padding(2.dp),

    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)
            .padding(16.dp)) {
            Text(
                text = question.questionTitle,
                fontWeight = FontWeight.Bold

            )
        }

        Spacer(modifier = Modifier.padding(vertical = 1.dp)
            )

        question.answers.forEach { answer ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onAnswerSelected(answer)
                    }
                    .background(
                        color = if (answer == selectedAnswer) Color.Gray else Color.White
                    )
                    .padding(4.dp),

                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    colors = RadioButtonDefaults.colors(
                        selectedColor = singleOptionUI.selectedColor,
                        unselectedColor = singleOptionUI.unSelectedColor),
                    selected = answer == selectedAnswer,
                    onClick = null,
                    modifier = Modifier.padding(8.dp),

                )
                Text(
                    text = answer,
                )
            }
            Spacer(modifier = Modifier.padding(vertical = 1.dp))
        }
    }
}


@Composable
fun SurveyScreen(
    modifier: Modifier = Modifier,
    onSurveyFinished: (Map<String, Set<String>>) -> Unit // Add a parameter to handle survey answers
) {
    Card(modifier = modifier
        .fillMaxSize()



    ) {
        Column(
            modifier = Modifier
                .fillMaxSize() // Column takes up full size within the Card
                .background(Color(color = 0xFFF5F5F5)) // Set cream background color
                .padding(12.dp) // Add padding inside the Column


        ) {
            Text(
                text = "Let's start with a quick survey!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.SansSerif,


            )
            // Pass the callback to SurveyView
            SurveyView(onFinishSurvey = onSurveyFinished)
        }
    }
}
