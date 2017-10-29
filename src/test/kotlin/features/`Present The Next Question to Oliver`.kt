package features

import application.CommandLineApplication
import business.Question
import business.Quiz
import business.QuizStorage
import business.TestOnlyQuizStorage
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.mockito.Mockito.verify
import ui.CommandLinePrinter
import ui.CommandLineUser
import java.util.*

class `Present The Next Question to Oliver` {

    private val commandLineUser =
            Mockito.mock(CommandLineUser::class.java)

    private val commandLinePrinter =
            Mockito.mock(CommandLinePrinter::class.java)

    @Test
    fun `move to the next question after starting`() {
        // Given there are multiple questions in the quiz
        val questionOne = Question(
                id = UUID.randomUUID().toString(),
                title = "How about Question One?")

        val questionTwo = Question(
                id = UUID.randomUUID().toString(),
                title = "How about Question Two?")

        val questionThree = Question(
                id = UUID.randomUUID().toString(),
                title = "How about Question Three?")

        val questions = listOf(
                questionOne,
                questionTwo,
                questionThree)

        val quiz = Quiz(
                id = UUID.randomUUID().toString(),
                questions = questions)

        val quizStorage: QuizStorage = TestOnlyQuizStorage(
                quizes = listOf(quiz))

        // And I have already started the quiz
        val args = arrayOf("start-quiz", quiz.id)
        val application = CommandLineApplication(
                args = args,
                commandLineUser = commandLineUser,
                commandLinePrinter = commandLinePrinter,
                underlyingQuizStorage = quizStorage)

        // When I choose to move to the next question
        given(commandLineUser.readCommand())
                .willReturn(
                        "next",
                        "next",
                        "quit")

        application.run()

        // Then I see the next question
        verify(commandLinePrinter)
                .println("""
                    |Current question: How about Question One?
                    |
                    |    type "next" to advance to the next question
                    |    type "quit" to abort the quiz and exit
                    |
                """.trimMargin())
        verify(commandLinePrinter)
                .println("""
                    |Current question: How about Question Two?
                    |
                    |    type "next" to advance to the next question
                    |    type "quit" to abort the quiz and exit
                    |
                """.trimMargin())
        verify(commandLinePrinter)
                .println("""
                    |Current question: How about Question Three?
                    |
                    |    type "next" to advance to the next question
                    |    type "quit" to abort the quiz and exit
                    |
                """.trimMargin())
    }

    @Test
    fun `reaching the end of the quiz when moving to the next question`() {
        // Given I am on the last question of the quiz
        val question = Question(
                id = UUID.randomUUID().toString(),
                title = "How about Question One?")

        val quiz = Quiz(
                id = UUID.randomUUID().toString(),
                questions = listOf(question))

        val quizStorage: QuizStorage = TestOnlyQuizStorage(
                quizes = listOf(quiz))

        val args = arrayOf("start-quiz", quiz.id)
        val application = CommandLineApplication(
                args = args,
                commandLineUser = commandLineUser,
                commandLinePrinter = commandLinePrinter,
                underlyingQuizStorage = quizStorage)

        // When I choose to move to the next question
        given(commandLineUser.readCommand())
                .willReturn("next")

        application.run()

        // Then the quiz is over
        verify(commandLinePrinter)
                .println("""
                    |Current question: How about Question One?
                    |
                    |    type "next" to advance to the next question
                    |    type "quit" to abort the quiz and exit
                    |
                """.trimMargin())
        verify(commandLinePrinter)
                .println("Congratulations! You have completed the quiz!")
    }

}

