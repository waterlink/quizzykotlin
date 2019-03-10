package features

import application.CommandLineApplication
import business.QuizStorage
import business.TestOnlyQuizStorage
import helper.answer
import helper.correct
import helper.question
import helper.quiz
import org.junit.Test
import org.mockito.BDDMockito.*
import org.mockito.Mockito
import ui.CommandLinePrinter
import ui.CommandLineUser

class `Show Results to Oliver` {
    private val commandLineUser =
            Mockito.mock(CommandLineUser::class.java)

    private val commandLinePrinter =
            Mockito.mock(CommandLinePrinter::class.java)

    @Test
    fun `getting all questions right`() {
        // Given I have started a quiz with three questions
        val questionOne = question(
                title = "What is raw string literal for?",
                answers = listOf(
                        answer("Multiline string").correct,
                        answer("Shorthand for list of strings"),
                        answer("There is no such feature in Kotlin")
                ))
        val questionTwo = question(
                title = "How about Question Two?",
                answers = listOf(
                        answer("Yes. Give me more questions!").correct,
                        answer("No! Please, no more questions!")
                ))
        val questionThree = question(
                title = "How about Question Three? Last one, I promise!",
                answers = listOf(
                        answer("No! I want more questions!"),
                        answer("Finally…").correct
                ))
        val quiz = quiz(questions = listOf(
                questionOne,
                questionTwo,
                questionThree
        ))
        val quizStorage: QuizStorage = TestOnlyQuizStorage(
                quizes = listOf(quiz))

        val args = arrayOf("start-quiz", quiz.id)
        val application = CommandLineApplication(
                args = args,
                commandLineUser = commandLineUser,
                commandLinePrinter = commandLinePrinter,
                underlyingQuizStorage = quizStorage
        )

        val commands = mutableListOf<String>()
        given(commandLineUser.readCommand()).will {
            commands.removeAt(0)
        }

        // And I have answered all questions
        // And I have gotten no questions wrong
        commands.addAll(listOf(
                "A", "next",
                "A", "next",
                "B", "next"
        ))

        // When I type "results" command
        commands.addAll(listOf("results", "quit"))

        application.run()

        // Then I see that my score is "3 out of 3"
        verify(commandLinePrinter).println("""
            |Your score is: 3 out of 3
        """.trimMargin())

        // And I see no questions I got wrong
        verify(commandLinePrinter, times(0)).println(contains("Incorrect:"))
        verify(commandLinePrinter, times(0)).println(contains("Correct:"))
    }
}