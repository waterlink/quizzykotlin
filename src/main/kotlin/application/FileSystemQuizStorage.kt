package application

import business.Question
import business.Quiz
import business.QuizStorage
import infrastructure.QuestionsRepository
import infrastructure.QuizesRepository

class FileSystemQuizStorage : QuizStorage {

    private val quizesRepository = QuizesRepository()
    private val questionsRepository = QuestionsRepository()

    override fun load(id: String): Quiz {
        val quizEntity = quizesRepository.load(id)
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