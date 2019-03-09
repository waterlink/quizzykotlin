package features

import application.CommandLineApplication
import business.TestOnlyQuizStorage
import helper.question
import helper.quiz
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.*
import ui.CommandLinePrinter
import ui.CommandLineUser

class `Handling unknown input` {

    private val commandLineUser =
            mock(CommandLineUser::class.java)

    private val commandLinePrinter =
            mock(CommandLinePrinter::class.java)

    @Test
    fun `tell user that input is invalid and provide valid options`() {
        // Given I have started the quiz
        val questionOne = question("What is raw string literal for?")
        val questionTwo = question("How about Question Two?")
        val quiz = quiz(questions = listOf(questionOne, questionTwo))
        val quizStorage = TestOnlyQuizStorage(quizes = listOf(quiz))

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