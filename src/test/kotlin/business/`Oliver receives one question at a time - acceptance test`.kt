package business

import org.junit.Assert
import org.junit.Test
import java.util.*

class `Oliver receives one question at a time - acceptance test` {

    @Test
    fun `there should be one current question when quiz starts`() {

        // Given there are questions in the quiz
        val first = Question(
                id = UUID.randomUUID().toString(),
                title = "How to create a method with spaces in Kotlin?")
        val second = Question(
                id = UUID.randomUUID().toString(),
                title = "How to create JUnit4 test function in Kotlin?")
        val questions = listOf(first, second)
        val quiz = Quiz(questions = questions)

        // When I start the quiz
        quiz.start()

        // Then I see only one question from that quiz
        val currentQuestion = quiz.currentQuestion
        Assert.assertTrue(questions.contains(currentQuestion))

    }
}