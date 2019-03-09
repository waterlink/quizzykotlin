package features

import application.CommandLineApplication
import business.TestOnlyQuizStorage
import helper.answer
import helper.question
import helper.quiz
import org.junit.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.*
import ui.CommandLinePrinter
import ui.CommandLineUser

class `Viewing And Choosing Answer Options` {

    private val commandLineUser =
            mock(CommandLineUser::class.java)

    private val commandLinePrinter =
            mock(CommandLinePrinter::class.java)

    @Test
    fun `current question renders its answer options`() {
        // Given there are few answer options for
        //   any question in the quiz
        val questionOne = question(
                title = "What is raw string literal for?",
                answers = listOf(
                        answer("Multiline string"),
                        answer("Shorthand for list of strings"),
                        answer("There is no such feature in Kotlin")
                ))
        val questionTwo = question(
                title = "How about Question Two?",
                answers = listOf(
                        answer("Yes. Give me more questions!"),
                        answer("No! Please, no more questions!")
                ))
        val quiz = quiz(questions = listOf(questionOne, questionTwo))
        val quizStorage = TestOnlyQuizStorage(quizes = listOf(quiz))

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
                    | A. Multiline string
                    | B. Shorthand for list of strings
                    | C. There is no such feature in Kotlin
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
                    | A. Yes. Give me more questions!
                    | B. No! Please, no more questions!
                    |
                    |    type "next" to advance to the next question
                    |    type "quit" to abort the quiz and exit
                    |
                """.trimMargin())
    }

    @Test
    fun `user can select an answer option`() {
        // Given there are answer options
        //      “A”, “B” and “C” for a question in a quiz
        val questionOne = question(
                title = "What is raw string literal for?",
                answers = listOf(
                        answer("Multiline string"),
                        answer("Shorthand for list of strings"),
                        answer("There is no such feature in Kotlin")
                ))
        val questionTwo = question("How about Question Two?")
        val quiz = quiz(questions = listOf(questionOne, questionTwo))
        val quizStorage = TestOnlyQuizStorage(quizes = listOf(quiz))

        // And the quiz has been started
        // And that question is current
        val args = arrayOf("start-quiz", quiz.id)
        val application = CommandLineApplication(
                args = args,
                commandLineUser = commandLineUser,
                commandLinePrinter = commandLinePrinter,
                underlyingQuizStorage = quizStorage)

        // When I choose answer option “B”
        given(commandLineUser.readCommand())
                .willReturn(
                        "B",
                        "next",
                        "quit")

        application.run()

        // Then I see that answer option “B” is chosen
        // And I see that answer options “A” and “C” are not chosen
        verify(commandLinePrinter)
                .println("""
                    |Current question: What is raw string literal for?
                    |
                    | A. Multiline string
                    |[B] Shorthand for list of strings
                    | C. There is no such feature in Kotlin
                    |
                    |    type "next" to advance to the next question
                    |    type "quit" to abort the quiz and exit
                    |
                """.trimMargin())
    }
}

