package features

import application.CommandLineApplication
import business.QuizStorage
import business.TestOnlyQuizStorage
import helper.answer
import helper.correct
import helper.question
import helper.quiz
import org.junit.Before
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

    private val quiz = quiz(questions = listOf(
            question("What is raw string literal for?", answers = listOf(
                    answer("Multiline string").correct,
                    answer("Shorthand for list of strings"),
                    answer("There is no such feature in Kotlin")
            )),
            question("How about Question Two?", answers = listOf(
                    answer("Yes. Give me more questions!").correct,
                    answer("No! Please, no more questions!")
            )),
            question("How about Question Three? Last one, I promise!",
                    answers = listOf(
                            answer("No! I want more questions!"),
                            answer("Finally…").correct
                    ))
    ))
    private val quizStorage: QuizStorage = TestOnlyQuizStorage(
            quizes = listOf(quiz))

    private val args = arrayOf("start-quiz", quiz.id)
    private val application = CommandLineApplication(
            args = args,
            commandLineUser = commandLineUser,
            commandLinePrinter = commandLinePrinter,
            underlyingQuizStorage = quizStorage
    )

    private val commands = mutableListOf<String>()

    @Before
    fun beforeEach() {
        given(commandLineUser.readCommand()).will {
            commands.removeAt(0)
        }
    }

    @Test
    fun `getting all questions right`() {
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

    @Test
    fun `getting all questions wrong`() {
        // And I have answered all questions
        // And I have gotten all questions wrong
        commands.addAll(listOf(
                "B", "next",
                "B", "next",
                "A", "next"
        ))

        // When I type "results" command
        commands.addAll(listOf("results", "quit"))

        application.run()

        // Then I see that my score is "0 out of 3"
        verify(commandLinePrinter).println("""
            |Your score is: 0 out of 3
        """.trimMargin())

        // And I see all questions
        // And for each question I see my answer and correct answer
        verify(commandLinePrinter).println("""
            |You got 3 wrong questions:
            |
            |   Question: What is raw string literal for?
            |   Incorrect: Shorthand for list of strings
            |   Correct: Multiline string
            |
            |   Question: How about Question Two?
            |   Incorrect: No! Please, no more questions!
            |   Correct: Yes. Give me more questions!
            |
            |   Question: How about Question Three? Last one, I promise!
            |   Incorrect: No! I want more questions!
            |   Correct: Finally…
            |
            |   type "quit" to exit
            |
        """.trimMargin())
    }

    @Test
    fun `getting some questions wrong and some right`() {
        // And I have answered all questions
        // And I have gotten two questions wrong
        commands.addAll(listOf(
                "B", "next",
                "A", "next",
                "A", "next"
        ))

        // When I type "results" command
        commands.addAll(listOf("results", "quit"))

        application.run()

        // Then I see that my score is "1 out of 3"
        verify(commandLinePrinter).println("""
            |Your score is: 1 out of 3
        """.trimMargin())

        // And I see the 2 questions I got wrong
        // And for each question I see my answer and correct answer
        verify(commandLinePrinter).println("""
            |You got 2 wrong questions:
            |
            |   Question: What is raw string literal for?
            |   Incorrect: Shorthand for list of strings
            |   Correct: Multiline string
            |
            |   Question: How about Question Three? Last one, I promise!
            |   Incorrect: No! I want more questions!
            |   Correct: Finally…
            |
            |   type "quit" to exit
            |
        """.trimMargin())
    }
}