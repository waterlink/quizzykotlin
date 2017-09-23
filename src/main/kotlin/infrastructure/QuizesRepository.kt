package infrastructure

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File

class QuizesRepository {

    private val objectMapper = jacksonObjectMapper()

    fun load(id: String): QuizEntity {
        val file = File("./quizzykotlinStorage/quizes/$id.json")
        return objectMapper.readValue(file)
    }
}