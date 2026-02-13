package dev.terry1921.nenektrivia.model.question

data class RemoteQuestion(
    val id: String,
    val question: String,
    val category: String,
    val answerGood: String,
    val answerBad01: String,
    val answerBad02: String,
    val answerBad03: String,
    val tip: String? = null
)
