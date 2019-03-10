package application

import business.AnswerOption
import business.Question
import business.Quiz
import business.QuizStorage
import infrastructure.AnswerOptionsRepository
import infrastructure.QuestionsRepository
import infrastructure.QuizEntity
import infrastructure.QuizesRepository
import java.io.FileNotFoundException

class FileSystemQuizStorage : QuizStorage {

    private val quizesRepository = QuizesRepository()
    private val questionsRepository = QuestionsRepository()
    private val answerOptionsRepository = AnswerOptionsRepository()

    override fun load(id: String): Quiz? {
        val quizEntity: QuizEntity

        try {
            quizEntity = quizesRepository.load(id)
        } catch (e: FileNotFoundException) {
            return null
        }

        val questionEntities = questionsRepository
                .findAllByQuizId(quizId = id)

        val questions = questionEntities.map {
            val answerOptionEntities = answerOptionsRepository
                    .findAllByQuestionId(questionId = it.id)

            val answerOptions = answerOptionEntities.map {
                AnswerOption(
                        id = it.id,
                        title = it.title,
                        isCorrect = it.isCorrect
                )
            }

            Question(
                    id = it.id,
                    title = it.title,
                    answerOptions = answerOptions
            )
        }

        return Quiz(
                id = quizEntity.id,
                questions = questions
        )
    }

}