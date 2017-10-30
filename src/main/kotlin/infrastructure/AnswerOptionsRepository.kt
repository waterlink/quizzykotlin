package infrastructure

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File

class AnswerOptionsRepository {

    private val objectMapper = jacksonObjectMapper()

    fun findAllByQuestionId(questionId: String)
            : List<AnswerOptionEntity> {

        val directory = File("./quizzykotlinStorage/answerOptions/")
        val fileTreeWalk = directory.walk().maxDepth(1)

        val filesOnly = fileTreeWalk.filter { it.isFile }

        val answerOptionEntities = filesOnly.map {
            objectMapper.readValue<AnswerOptionEntity>(it)
        }

        val filteredAnswerOptionEntities = answerOptionEntities.filter {
            it.questionId == questionId
        }

        return filteredAnswerOptionEntities.toList()

    }

}