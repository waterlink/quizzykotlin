package business

import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import java.util.*

class `Oliver receives one question at a time - acceptance test` {

    @get:Rule
    val thrown = ExpectedException.none()

    // Given there are questions in the quiz
    private val first = Question(
            id = UUID.randomUUID().toString(),
            title = "How to create a method with spaces in Kotlin?")
    private val second = Question(
            id = UUID.randomUUID().toString(),
            title = "How to create JUnit4 test function in Kotlin?")
    private val questions = listOf(first, second)
    private val quiz = Quiz(
            id = UUID.randomUUID().toString(),
            questions = questions)

    @Test
    fun `there should be one current question when quiz starts`() {

        // When I start the quiz
        quiz.start()

        // Then I see only one question from that quiz
        val currentQuestion = quiz.currentQuestion
        Assert.assertTrue(questions.contains(currentQuestion))

    }

    @Test
    fun `can move to the next question`() {

        // Given I have started the quiz
        quiz.start()

        // When I move to the next question
        quiz.moveToNextQuestion()

        // Then I see that next question
        Assert.assertEquals(second, quiz.currentQuestion)

    }

    @Test
    fun `can not move beyond the last question`() {

        // Given I am on the last question
        quiz.start()
        quiz.moveToNextQuestion()

        // Then I see that I have completed the quiz
        thrown.expect(QuizCompletedException::class.java)
        thrown.expectMessage("Quiz has been completed")

        // When I move to the next question
        quiz.moveToNextQuestion()
    }

    @Test
    fun `can load started quiz from storage and move to the next question`() {

        // Given I have started the quiz
        quiz.start()
        val quizStorage = TestOnlyQuizStorage(quizes = listOf(quiz))
        val nextQuestionService = NextQuestionService(quizStorage)

        // When I move to the next question
        val result = nextQuestionService.moveToNextQuestion(quiz.id)

        // Then I see the next Question from that Quiz
        Assert.assertEquals(second, result)

    }

    @Test
    fun `can load quiz from storage, start it and respond with current question`() {

        // Given there is a Quiz with a few Questions in the Quiz Storage
        val quizStorage = TestOnlyQuizStorage(quizes = listOf(quiz))
        val startQuizService = StartQuizService(quizStorage)

        // When I start the Quiz
        val result = startQuizService.startQuiz(quiz.id)

        // Then I see a Question from that Quiz
        Assert.assertTrue(questions.contains(result))

    }
}

