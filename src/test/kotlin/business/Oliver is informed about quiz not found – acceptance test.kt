package business

import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import java.util.*

class `Oliver is informed about quiz not found – acceptance test` {

    @get:Rule
    val thrown = ExpectedException.none()

    @Test
    fun `fails with QuizNotFoundException when there are no quizes`() {
        // Given there are no quizes
        val quizStorage = TestOnlyQuizStorage(quizes = emptyList())
        val startQuizService = StartQuizService(quizStorage)
        val someId = UUID.randomUUID().toString()

        // Then I see Quiz Not Found error
        thrown.expect(QuizNotFoundException::class.java)
        thrown.expectMessage("Unable to find Quiz with id = " + someId)

        // When I start quiz with some id
        startQuizService.startQuiz(someId)
    }
}

