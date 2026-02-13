package dev.terry1921.nenektrivia.domain.questions

import dev.terry1921.nenektrivia.database.entity.Question
import dev.terry1921.nenektrivia.model.question.QuestionModel
import dev.terry1921.nenektrivia.model.question.RemoteQuestion

internal fun RemoteQuestion.toEntity(): Question = Question(
    id = id,
    question = question,
    category = category,
    answerGood = answerGood,
    answerBad01 = answerBad01,
    answerBad02 = answerBad02,
    answerBad03 = answerBad03,
    tip = tip
)

internal fun Question.toModel(): QuestionModel = QuestionModel(
    id = id,
    question = question,
    category = category,
    answerGood = answerGood,
    answerBad01 = answerBad01,
    answerBad02 = answerBad02,
    answerBad03 = answerBad03,
    tip = tip
)
