package business

import org.junit.Assert
import org.junit.Test
import java.util.*

class `Oliver is informed about quiz not found – acceptance test` {

    @Test
    fun `fails with QuizNotFoundException when there are no quizes`() {
        // Given there are no quizes
        val quizStorage = TestOnlyQuizStorage(quizes = emptyList())
        val startQuizService = StartQuizService(quizStorage)

        // When I start quiz with some id
        try {
            val someId = UUID.randomUUID().toString()
            val result = startQuizService.startQuiz(someId)
            // Then I see Quiz Not Found error
        } catch (e: QuizNotFoundException) {
            // everything is good, we will stop the test
            return
        }
        // or fail if we did not get the exception
        Assert.assertEquals(true, false)

    }
}