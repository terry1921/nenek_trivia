package dev.terry1921.nenektrivia.network.api

import dev.terry1921.nenektrivia.model.question.QuestionResponse
import retrofit2.http.GET

fun interface ApiService {
    @GET("questions")
    suspend fun getQuestions(): List<QuestionResponse>
}
