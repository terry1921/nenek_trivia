package dev.terry1921.nenektrivia.ui.questions

data class QuestionUiState(
    val questionIndex: Int = 1,
    val totalQuestions: Int = 10,
    val points: Int = 0,
    val question: QuestionItem? = null,
    val selectedOptionId: String? = null,
    val revealAnswer: Boolean = false,
    val timeRemainingSeconds: Float = 0f,
    val tip: String? = null,
    val showWinnerDialog: Boolean = false
)

data class QuestionItem(
    val id: String,
    val category: String,
    val text: String,
    val options: List<QuestionOption>
)

data class QuestionOption(val id: String, val text: String, val isCorrect: Boolean)
