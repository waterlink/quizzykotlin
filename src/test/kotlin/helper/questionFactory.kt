package helper

import business.AnswerOption
import business.Question
import business.Quiz
import java.util.*

fun quiz(
        id: String = UUID.randomUUID().toString(),
        questions: List<Question> = emptyList()
) = Quiz(
        id = id,
        questions = questions
)

fun question(
        title: String = "Example title",
        id: String = UUID.randomUUID().toString(),
        answers: List<AnswerOption> = emptyList()
) = Question(
        id = id,
        title = title,
        answerOptions = answers
)

fun answer(
        title: String = "Example answer",
        id: String = UUID.randomUUID().toString(),
        isChosen: Boolean = false,
        isCorrect: Boolean = false
) = AnswerOption(
        id = id,
        title = title,
        isChosen = isChosen,
        isCorrect = isCorrect
)

val AnswerOption.correct get() = copy(isCorrect = true)
val AnswerOption.chosen get() = copy(isChosen = true)