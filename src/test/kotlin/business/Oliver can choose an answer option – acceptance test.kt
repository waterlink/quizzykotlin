package business

import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import java.util.*

class `Oliver can choose an answer option – acceptance test` {

    @get:Rule
    val thrown = ExpectedException.none()

    // Given there is a quiz with a question
    //      with a few answer options
    private val answerOne = AnswerOption(
            id = UUID.randomUUID().toString(),
            title = "Multiline string")
    private val answerTwo = AnswerOption(
            id = UUID.randomUUID().toString(),
            title = "Shorthand for list of strings")

    private val question = Question(
            id = UUID.randomUUID().toString(),
            title = "What is raw string literal for?",
            answerOptions = listOf(
                    answerOne,
                    answerTwo))

    private val quiz = Quiz(
            id = UUID.randomUUID().toString(),
            questions = listOf(question))

    @Test
    fun `there is no selected answer option initially`() {
        // When I start the quiz
        quiz.start()

        // And navigate to that question
        val currentQuestion = quiz.currentQuestion!!

        // Then I see that no answer option is chosen
        assertEquals(false, currentQuestion.answerOptions[0].isChosen)
        assertEquals(false, currentQuestion.answerOptions[1].isChosen)
    }

    @Test
    fun `choosing an answer option by index`() {
        // And I have already started the quiz
        quiz.start()

        // And I have already navigated to that question
        val currentQuestion = quiz.currentQuestion!!

        // When I choose an answer option
        currentQuestion.chooseAnswerOption(0)

        // Then I see it is chosen
        assertEquals(true, currentQuestion.answerOptions[0].isChosen)

        // And I see no other option is chosen
        assertEquals(false, currentQuestion.answerOptions[1].isChosen)
    }

    @Test
    fun `choosing different answer option after one was already chosen`() {
        // And I have already started the quiz
        quiz.start()

        // And I have already navigated to that question
        val currentQuestion = quiz.currentQuestion!!

        // And I have already chosen the answer option
        currentQuestion.chooseAnswerOption(0)

        // When I choose another answer option
        currentQuestion.chooseAnswerOption(1)

        // Then I see that the new answer option is chosen
        assertEquals(true, currentQuestion.answerOptions[1].isChosen)

        // And I see that the previous answer option is not chosen anymore
        assertEquals(false, currentQuestion.answerOptions[0].isChosen)
    }

    @Test
    fun `choosing non-existing answer option – index is less than 0`() {
        // And I have already started the quiz
        quiz.start()

        // And I have already navigated to that question
        val currentQuestion = quiz.currentQuestion!!

        // Then I see that there is no such answer option
        thrown.expect(NoSuchAnswerOptionException::class.java)
        thrown.expectMessage("Answer option index can’t be < 0")

        // When I choose the answer option with index < 0
        currentQuestion.chooseAnswerOption(-1)
    }

    @Test
    fun `choosing non-existing answer option – index is more or equal count`() {
        // And I have already started the quiz
        quiz.start()

        // And I have already navigated to that question
        val currentQuestion = quiz.currentQuestion!!

        // Then I see that there is no such answer option
        thrown.expect(NoSuchAnswerOptionException::class.java)
        thrown.expectMessage("Answer option index is too big")

        // When I choose the answer option with index >= count
        currentQuestion.chooseAnswerOption(2)
    }

    @Test
    fun `can choose the provided answer option by index from quiz storage`() {
        // And I have already started the quiz
        val quizStorage = TestOnlyQuizStorage(quizes = listOf(quiz))
        val startQuizService = StartQuizService(quizStorage)
        startQuizService.startQuiz(quiz.id)

        // When I choose an answer option for that question
        val service = ChooseAnswerOptionService(quizStorage)
        val currentQuestion = service.chooseAnswerOption(
                quizId = quiz.id,
                index = 1)

        // Then I see it is chosen
        assertEquals(true, currentQuestion.answerOptions[1].isChosen)

        // And I see no other option is chosen
        assertEquals(false, currentQuestion.answerOptions[0].isChosen)
    }
}

