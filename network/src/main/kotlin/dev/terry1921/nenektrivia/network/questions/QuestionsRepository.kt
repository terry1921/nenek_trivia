package dev.terry1921.nenektrivia.network.questions

import dev.terry1921.nenektrivia.model.question.RemoteQuestion

interface QuestionsRepository {
    suspend fun fetchQuestions(): List<RemoteQuestion>
}
