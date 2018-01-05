package features

import application.CommandLineApplication
import business.Question
import business.Quiz
import business.TestOnlyQuizStorage
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import ui.CommandLinePrinter
import ui.CommandLineUser
import java.util.*

class `Handling unknown input` {

    private val commandLineUser =
            mock(CommandLineUser::class.java)

    private val commandLinePrinter =
            mock(CommandLinePrinter::class.java)

    @Test
    fun `tell user that input is invalid and provide valid options`() {
        // Given I have started the quiz
        val questionOne = Question(
                id = UUID.randomUUID().toString(),
                title = "What is raw string literal for?",
                answerOptions = emptyList())

        val questionTwo = Question(
                id = UUID.randomUUID().toString(),
                title = "How about Question Two?",
                answerOptions = emptyList())

        val quiz = Quiz(
                id = UUID.randomUUID().toString(),
                questions = listOf(
                        questionOne,
                        questionTwo))

        val quizStorage = TestOnlyQuizStorage(
                quizes = listOf(quiz))

        val args = arrayOf("start-quiz", quiz.id)
        val application = CommandLineApplication(
                args = args,
                commandLineUser = commandLineUser,
                commandLinePrinter = commandLinePrinter,
                underlyingQuizStorage = quizStorage)

        // When I input an invalid command “hello”
        given(commandLineUser.readCommand())
                .willReturn(
                        "hello",
                        "quit")

        application.run()

        // Then I see that my input was invalid
        // And I see what commands are available to me
        verify(commandLinePrinter)
                .println("""
                    |Unknown user input "hello"
                    |
                    |Please use one of the following commands:
                    |
                    |    type "next" to advance to the next question
                    |    type "quit" to abort the quiz and exit
                    |
                """.trimMargin())

    }

}