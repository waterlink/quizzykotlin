package business

import helper.quiz
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class `Oliver is informed about empty quiz – acceptance test` {

    @get:Rule
    val thrown = ExpectedException.none()

    @Test
    fun `fails with QuizHasNoQuestionsException when quiz is empty`() {
        // Given there is a quiz without any questions
        val quiz = quiz()
        val quizStorage = TestOnlyQuizStorage(listOf(quiz))
        val startQuizService = StartQuizService(quizStorage)

        // Then I see Quiz Is Empty error
        thrown.expect(QuizHasNoQuestionsException::class.java)
        thrown.expectMessage("Quiz with id = ${quiz.id} has no questions")

        // When I start that quiz
        startQuizService.startQuiz(quiz.id)
    }

}

