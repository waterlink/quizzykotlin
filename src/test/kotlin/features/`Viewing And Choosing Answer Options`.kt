package features

import application.CommandLineApplication
import business.AnswerOption
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

class `Viewing And Choosing Answer Options` {

    private val commandLineUser =
            mock(CommandLineUser::class.java)

    private val commandLinePrinter =
            mock(CommandLinePrinter::class.java)

    @Test
    fun `current question renders its answer options`() {

        // Given there are few answer options for
        //   any question in the quiz
        val answerOne = AnswerOption(
                id = UUID.randomUUID().toString(),
                title = "Multiline string")
        val answerTwo = AnswerOption(
                id = UUID.randomUUID().toString(),
                title = "Shorthand for list of strings")
        val answerThree = AnswerOption(
                id = UUID.randomUUID().toString(),
                title = "There is no such feature in Kotlin")
        val questionOne = Question(
                id = UUID.randomUUID().toString(),
                title = "What is raw string literal for?",
                answerOptions = listOf(
                        answerOne,
                        answerTwo,
                        answerThree))

        val answerFour = AnswerOption(
                id = UUID.randomUUID().toString(),
                title = "Yes. Give me more questions!")
        val answerFive = AnswerOption(
                id = UUID.randomUUID().toString(),
                title = "No! Please, no more questions!")
        val questionTwo = Question(
                id = UUID.randomUUID().toString(),
                title = "How about Question Two?",
                answerOptions = listOf(
                        answerFour,
                        answerFive))

        val quiz = Quiz(
                id = UUID.randomUUID().toString(),
                questions = listOf(
                        questionOne,
                        questionTwo))

        val quizStorage = TestOnlyQuizStorage(
                quizes = listOf(quiz))

        // When I start that quiz
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
                        "quit")

        application.run()

        // Then I see these answer options
        verify(commandLinePrinter)
                .println("""
                    |Current question: What is raw string literal for?
                    |
                    |A. Multiline string
                    |B. Shorthand for list of strings
                    |C. There is no such feature in Kotlin
                    |
                    |    type "next" to advance to the next question
                    |    type "quit" to abort the quiz and exit
                    |
                """.trimMargin())

        // Then I see the answer options of that next question
        verify(commandLinePrinter)
                .println("""
                    |Current question: How about Question Two?
                    |
                    |A. Yes. Give me more questions!
                    |B. No! Please, no more questions!
                    |
                    |    type "next" to advance to the next question
                    |    type "quit" to abort the quiz and exit
                    |
                """.trimMargin())


    }
}

