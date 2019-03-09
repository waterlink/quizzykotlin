package ui

import org.junit.Test
import org.mockito.Mockito.*

class CurrentQuestionViewTest {

    private val commandLinePrinter =
            mock(CommandLinePrinter::class.java)

    @Test
    fun `renders question with answer options and commands`() {

        val answerOne = AnswerOptionPresentation(
                title = "Answer One",
                isChosen = false)
        val answerTwo = AnswerOptionPresentation(
                title = "Answer Two",
                isChosen = false)

        val view = CurrentQuestionView(
                title = "Question One",
                answerOptions = listOf(answerOne, answerTwo),
                commandLinePrinter = commandLinePrinter)

        view.render()

        verify(commandLinePrinter)
                .println("""
                    |Current question: Question One
                    |
                    | A. Answer One
                    | B. Answer Two
                    |
                    |    type "next" to advance to the next question
                    |    type "quit" to abort the quiz and exit
                    |
                """.trimMargin())

    }

    @Test
    fun `renders question with answer options that have been chosen`() {

        val answerOne = AnswerOptionPresentation(
                title = "Answer One",
                isChosen = false)
        val answerTwo = AnswerOptionPresentation(
                title = "Answer Two",
                isChosen = true)

        val view = CurrentQuestionView(
                title = "Question One",
                answerOptions = listOf(answerOne, answerTwo),
                commandLinePrinter = commandLinePrinter)

        view.render()

        verify(commandLinePrinter)
                .println("""
                    |Current question: Question One
                    |
                    | A. Answer One
                    |[B] Answer Two
                    |
                    |    type "next" to advance to the next question
                    |    type "quit" to abort the quiz and exit
                    |
                """.trimMargin())

    }

}