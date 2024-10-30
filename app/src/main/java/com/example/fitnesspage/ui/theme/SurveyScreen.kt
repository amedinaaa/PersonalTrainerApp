package com.example.fitnesspage.ui.theme

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
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Snackbar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import com.example.fitnesspage.model.Category
import com.example.fitnesspage.model.FitnessAppModel
import com.example.fitnesspage.model.QuestionType
import com.example.fitnesspage.model.SingleOptionUI
import com.example.fitnesspage.model.SurveyModel
import com.example.fitnesspage.model.SurveyText
//
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.res.stringResource
import com.example.fitnesspage.R

@Composable
fun SurveyView(onFinishSurvey: (Map<String,Set<String>>) -> Unit) {
    val sampleSurvey = listOf(
        SurveyModel(
            questionType = QuestionType.SINGLE_CHOICE,
            questionId = "id1",
            questionTitle = "1) What is your primary fitness goal:",
            answers = listOf("Build strength and muscle mass", "Lose weight", "Improve flexibility"),
        ),
        SurveyModel(
            questionType = QuestionType.SINGLE_CHOICE,
            questionId = "id2",
            questionTitle = "2) What is your fitness level?:",
            answers = listOf("Novice/Beginner", "Intermediate", "Advance"),
        ),
//        SurveyModel(
//            questionType = QuestionType.SINGLE_CHOICE,
//            questionId = "id3",
//            questionTitle = "3) What is your age:",
//            answers = listOf("18-30", "30-39", "40-49", "50+"),
//        )
    )
    SurveyScreen2(
        survey = sampleSurvey,
        backgroundColor = Color.White,
        singleOptionUI = SingleOptionUI(
            questionTitle = SurveyText(
                color = Color.DarkGray,
                fontWeight = FontWeight.ExtraBold
            ),
            answer = SurveyText(
                color = Color.DarkGray,
                fontWeight = FontWeight.Medium
            ),
            selectedColor = Color.White,
            unSelectedColor = Color.Gray,
            borderColor = Color.Gray
        ),
        onFinishButtonClicked = { answers -> onFinishSurvey(answers) } // Pass callback to save answers
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
            .background(backgroundColor)
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
            .padding(8.dp)
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
            Text(
                text = question.questionTitle,
            )
        }

        Spacer(modifier = Modifier.padding(vertical = 4.dp))

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
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    colors = RadioButtonDefaults.colors(
                        selectedColor = singleOptionUI.selectedColor,
                        unselectedColor = singleOptionUI.unSelectedColor),
                    selected = answer == selectedAnswer,
                    onClick = null,
                    modifier = Modifier.padding(8.dp)
                )
                Text(
                    text = answer,
                )
            }
            Spacer(modifier = Modifier.padding(vertical = 2.dp))
        }
    }
}


@Composable
fun SurveyScreen(
    viewModel: FitnessAppModel,
    uiState: UiState,
    modifier: Modifier = Modifier,
    onSurveyFinished: (Map<String, Set<String>>) -> Unit // Add a parameter to handle survey answers
) {
    Card(modifier = modifier) {
        Column {
            Text(text = "Fitness Survey", style = MaterialTheme.typography.headlineMedium)
//            Text(text = "Tell us your goals for exercising?", style = MaterialTheme.typography.bodyLarge)
            // Pass the callback to SurveyView
            SurveyView(onFinishSurvey = onSurveyFinished)
        }
    }
}


//@Composable
//fun SurveyScreen(
//    viewModel: FitnessAppModel,
//    uiState: UiState,
//    modifier: Modifier= Modifier,
//    ){
//    Card(modifier = modifier) {
//        Column {
//            Text(
//                text = "Fitness Plan Survey",
//                style = MaterialTheme.typography.headlineMedium
//            )
            // SurveyView()

//            Text(text = "What are your goals for exercising?", style = MaterialTheme.typography.bodyLarge)
//            LazyColumn(modifier = modifier.padding(top = dimensionResource(id = R.dimen.padding_medium))) {
//                items(uiState.categories) {
//                    CategoryCard(category = it, modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(
//                            bottom = dimensionResource(
//                                id = R.dimen.padding_small
//                            ),
//                            start = dimensionResource(id = R.dimen.padding_medium),
//                            end = dimensionResource(id = R.dimen.padding_medium)
//                        ),
//                        onClick = {
//                            visible = false
//                            viewModel.updateCurrentCategory(it)
//                            navigateFunction()
//                        })
//                }
//        }
//
//    }}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CategoryCard(
//    category: Category,
//    modifier: Modifier = Modifier,
//    onClick: () -> Unit = {},
//    colors: CardColors = CardDefaults.cardColors()
//) {
//    Card(
//        colors = colors,
//        onClick = onClick,
//        shape = RoundedCornerShape(
//            topEnd = 20.dp, topStart = 40.dp,
//            bottomEnd = 20.dp, bottomStart = 40.dp
//        ),
//        modifier = modifier
//    ) {
//        Row(
//            modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Column {
//                Text(
//                    text = stringResource(id = category.name),
//                    style = MaterialTheme.typography.titleMedium
//                )
//
//            }
//
//        }
//
//    }
//
//}

// preview
//fun getSampleUiState(): UiState {
//    return UiState(
//        categories = listOf(
//            Category(name = R.string.category_cardio, list = listOf(
//                Recommendation(name = R.string.recommendation_run),
//                Recommendation(name = R.string.recommendation_jump_rope)
//            )),
//            Category(name = R.string.category_strength, list = listOf(
//                Recommendation(name = R.string.recommendation_bench_press),
//                Recommendation(name = R.string.recommendation_squat)
//            )),
//            Category(name = R.string.category_flexibility, list = listOf(
//                Recommendation(name = R.string.recommendation_yoga),
//                Recommendation(name = R.string.recommendation_stretching)
//            ))
//        )
//    )
//}

//@Composable
//@Preview(showBackground = true)
//fun PreviewSurveyScreen() {
//    // Create a sample ViewModel.
//    val viewModel = FitnessAppModel()
//    val sampleUiState = getSampleUiState()
//
//    MaterialTheme {
//        SurveyScreen(viewModel = viewModel, uiState = sampleUiState)
//    }
//}