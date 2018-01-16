package business

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class `Oliver can choose an answer option – acceptance test` {

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

        // Then I see that no answer option is selected
        assertEquals(false, currentQuestion.answerOptions[0].isChosen)
        assertEquals(false, currentQuestion.answerOptions[1].isChosen)
    }

    @Test
    fun `choosing an answer option by index`() {
        // And I have already started the quiz
        quiz.start()

        // And I have already navigated to that question
        val currentQuestion = quiz.currentQuestion!!

        // When I select an answer option
        currentQuestion.chooseAnswerOption(0)

        // Then I see it is selected
        assertEquals(true, currentQuestion.answerOptions[0].isChosen)

        // And I see no other option is selected
        assertEquals(false, currentQuestion.answerOptions[1].isChosen)
    }
}