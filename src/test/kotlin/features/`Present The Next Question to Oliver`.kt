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

        val questions = listOf(questionOne, questionTwo)

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
                quizStorage = quizStorage)

        // When I choose to move to the next question
        given(commandLineUser.readCommand())
                .willReturn(
                        "next",
                        "quit")

        application.run()

        // Then I see the next question
        verify(commandLinePrinter)
                .println("Current question: How about Question One?")
        verify(commandLinePrinter)
                .println("Current question: How about Question Two?")
    }
}

