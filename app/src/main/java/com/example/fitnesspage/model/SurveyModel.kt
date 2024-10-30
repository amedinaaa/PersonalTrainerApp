package com.example.fitnesspage.model

data class SurveyModel(
    val questionType: QuestionType,
    val questionId: String,
    val questionTitle: String,
//    val questionDescription: String,
    val answers: List<String> = listOf()
)
