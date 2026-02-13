package dev.terry1921.nenektrivia.domain.questions

import dev.terry1921.nenektrivia.database.questions.QuestionRepository
import dev.terry1921.nenektrivia.model.question.QuestionModel
import dev.terry1921.nenektrivia.network.questions.QuestionsRepository
import javax.inject.Inject

class GetQuestionsUseCase @Inject constructor(
    private val localRepository: QuestionRepository,
    private val remoteRepository: QuestionsRepository
) {
    suspend operator fun invoke(forceRefresh: Boolean = false): List<QuestionModel> {
        if (!forceRefresh) {
            val local = localRepository.getAll()
            if (local.isNotEmpty()) {
                return local.map { it.toModel() }
            }
        }

        val remote = remoteRepository.fetchQuestions()
        localRepository.replaceAll(remote.map { it.toEntity() })
        return localRepository.getAll().map { it.toModel() }
    }
}
