package application

import business.Question
import business.Quiz
import business.QuizStorage
import infrastructure.QuestionsRepository
import infrastructure.QuizEntity
import infrastructure.QuizesRepository
import java.io.FileNotFoundException

class FileSystemQuizStorage : QuizStorage {

    private val quizesRepository = QuizesRepository()
    private val questionsRepository = QuestionsRepository()

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
            Question(id = it.id,
                    title = it.title)
        }

        return Quiz(id = quizEntity.id,
                questions = questions)
    }

}