package infrastructure

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File

class QuestionsRepository {

    private val objectMapper = jacksonObjectMapper()

    fun findAllByQuizId(quizId: String): List<QuestionEntity> {
        val directory = File("./quizzykotlinStorage/questions/")
        val fileTreeWalk = directory.walk().maxDepth(1)

        val filesOnly = fileTreeWalk.filter { it.isFile }

        val questionEntities = filesOnly.map {
            objectMapper.readValue<QuestionEntity>(it)
        }

        val filteredQuestionEntities = questionEntities.filter {
            it.quizId == quizId
        }

        return filteredQuestionEntities.toList()
    }

}