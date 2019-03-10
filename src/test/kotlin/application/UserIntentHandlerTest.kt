package application

import business.TestOnlyQuizStorage
import helper.*
import org.junit.Test
import org.mockito.Mockito.*
import ui.*

class UserIntentHandlerTest {
    private val application = mock(CommandLineApplication::class.java)
    private val printer = mock(CommandLinePrinter::class.java)
    private val quiz = quiz(questions = listOf(
            question("What is this interesting question?", answers = listOf(
                    answer("I don't see any question"),
                    answer("Interesting, indeed!").correct
            )),
            question("Is this the last question?", answers = listOf(
                    answer("Yes").correct,
                    answer("No")
            ))
    ))
    private val emptyQuiz = quiz()
    private val oneQuestion = question("A single question")
    private val oneQuestionQuiz = quiz(questions = listOf(oneQuestion))
    private val finishedQuiz = quiz(questions = listOf(
            question("The question hidden in plain sight", answers = listOf(
                    answer("What is the question?").chosen,
                    answer("What is the answer?").correct
            )),
            question("What is this interesting question?", answers = listOf(
                    answer("I don't see any question"),
                    answer("Interesting, indeed!").correct.chosen
            )),
            question("Is this the last question?", answers = listOf(
                    answer("Yes").correct,
                    answer("No").chosen
            ))
    ))
    private val quizStorage = CachingQuizStorage(TestOnlyQuizStorage(
            quizes = listOf(quiz, emptyQuiz, oneQuestionQuiz, finishedQuiz)))

    private val handler = UserIntentHandler(
            application = application,
            printer = printer,
            quizStorage = quizStorage
    )

    private val startedHandler = UserIntentHandler(
            application = application,
            printer = printer,
            quizStorage = quizStorage,
            initialQuizId = quiz.id
    )

    private val emptyQuizHandler = UserIntentHandler(
            application = application,
            printer = printer,
            quizStorage = quizStorage,
            initialQuizId = emptyQuiz.id
    )

    private val oneQuestionQuizHandler = UserIntentHandler(
            application = application,
            printer = printer,
            quizStorage = quizStorage,
            initialQuizId = oneQuestionQuiz.id
    )

    private val nonExistingQuizHandler = UserIntentHandler(
            application = application,
            printer = printer,
            quizStorage = quizStorage,
            initialQuizId = "non-existing"
    )

    private val finishedQuizHandler = UserIntentHandler(
            application = application,
            printer = printer,
            quizStorage = quizStorage,
            initialQuizId = finishedQuiz.id
    )

    @Test
    fun `handle – starts quiz with specified id and shows first question`() {
        handler.handle(StartQuizUserIntent(quizId = quiz.id))

        verify(printer).println("""
            |Current question: What is this interesting question?
            |
            | A. I don't see any question
            | B. Interesting, indeed!
            |
            |    type "next" to advance to the next question
            |    type "quit" to abort the quiz and exit
            |
        """.trimMargin())

        verifyNoMoreInteractions(printer)
        verifyZeroInteractions(application)
    }

    @Test
    fun `handle – can move to next question after starting the quiz`() {
        handler.handle(StartQuizUserIntent(quizId = quiz.id))
        handler.handle(NextQuestionUserIntent())

        verify(printer).println("""
            |Current question: What is this interesting question?
            |
            | A. I don't see any question
            | B. Interesting, indeed!
            |
            |    type "next" to advance to the next question
            |    type "quit" to abort the quiz and exit
            |
        """.trimMargin())

        verify(printer).println("""
            |Current question: Is this the last question?
            |
            | A. Yes
            | B. No
            |
            |    type "next" to advance to the next question
            |    type "quit" to abort the quiz and exit
            |
        """.trimMargin())

        verifyNoMoreInteractions(printer)
        verifyZeroInteractions(application)
    }

    @Test
    fun `handle – fails to start quiz because it was not found`() {
        handler.handle(StartQuizUserIntent(quizId = "non-existing id"))

        verify(printer).println("""
            |Error: Unable to find Quiz with id = non-existing id
        """.trimMargin())

        verify(printer).println("""
            |Check the quiz id you’ve typed
        """.trimMargin())

        verify(application).requestQuit()

        verifyNoMoreInteractions(printer)
        verifyNoMoreInteractions(application)
    }

    @Test
    fun `handle – fails to start quiz because it has no questions`() {
        handler.handle(StartQuizUserIntent(quizId = emptyQuiz.id))

        verify(printer).println("""
            |Error: Quiz with id = ${emptyQuiz.id} has no questions
        """.trimMargin())

        verify(printer).println("""
            |Please inform your quiz admin about that error
        """.trimMargin())

        verify(application).requestQuit()

        verifyNoMoreInteractions(printer)
        verifyNoMoreInteractions(application)
    }

    @Test
    fun `handle – proceeds to the next question and shows it`() {
        startedHandler.handle(NextQuestionUserIntent())

        verify(printer).println("""
            |Current question: Is this the last question?
            |
            | A. Yes
            | B. No
            |
            |    type "next" to advance to the next question
            |    type "quit" to abort the quiz and exit
            |
        """.trimMargin())

        verifyNoMoreInteractions(printer)
        verifyZeroInteractions(application)
    }

    @Test
    fun `handle – next question completes quiz if quiz is empty`() {
        emptyQuizHandler.handle(NextQuestionUserIntent())

        verify(printer).println("""
            |Congratulations! You have completed the quiz!
            |
            |    type "results" to view your quiz results
            |    type "quit" to exit
            |
        """.trimMargin())

        verifyNoMoreInteractions(printer)
        verifyZeroInteractions(application)
    }

    @Test
    fun `handle – next question completes quiz if it's a last question`() {
        oneQuestionQuizHandler.handle(NextQuestionUserIntent())

        verify(printer).println("""
            |Congratulations! You have completed the quiz!
            |
            |    type "results" to view your quiz results
            |    type "quit" to exit
            |
        """.trimMargin())

        verifyNoMoreInteractions(printer)
        verifyZeroInteractions(application)
    }

    @Test
    fun `handle – next question will fail with not found if quiz is suddenly gone`() {
        nonExistingQuizHandler.handle(NextQuestionUserIntent())

        verify(printer).println("""
            |Error: Unable to find Quiz with id = non-existing
        """.trimMargin())

        verify(printer).println("""
            |Check the quiz id you’ve typed
        """.trimMargin())

        verify(application).requestQuit()

        verifyNoMoreInteractions(printer)
        verifyZeroInteractions(application)
    }

    @Test
    fun `handle – shows results`() {
        finishedQuizHandler.handle(ShowResultsUserIntent())

        verify(printer).println("""
            |Your score is: 1 out of 3
        """.trimMargin())

        verify(printer).println("""
            |You got 2 wrong questions:
            |
            |   Question: The question hidden in plain sight
            |   Incorrect: What is the question?
            |   Correct: What is the answer?
            |
            |   Question: Is this the last question?
            |   Incorrect: No
            |   Correct: Yes
            |
        """.trimMargin())

        verifyNoMoreInteractions(printer)
        verifyZeroInteractions(application)
    }

    @Test
    fun `handle – unable to show results when quiz not found`() {
        nonExistingQuizHandler.handle(ShowResultsUserIntent())

        verify(printer).println("""
            |Error: Unable to find Quiz with id = non-existing
        """.trimMargin())

        verify(printer).println("""
            |Check the quiz id you’ve typed
        """.trimMargin())

        verify(application).requestQuit()

        verifyNoMoreInteractions(printer)
        verifyNoMoreInteractions(application)
    }

    @Test
    fun `handle – unable to show results when no correct option present`() {
        oneQuestionQuizHandler.handle(ShowResultsUserIntent())

        verify(printer).println("""
            |Error: The question with id = ${oneQuestion.id} has no correct answer
        """.trimMargin())

        verify(printer).println("""
            |Please inform your quiz admin about that error
        """.trimMargin())

        verify(application).requestQuit()

        verifyNoMoreInteractions(printer)
        verifyNoMoreInteractions(application)
    }

    @Test
    fun `handle – complains about unknown user intent`() {
        handler.handle(UnknownUserIntent("some input"))

        verify(printer).println("""
            |Unknown user input "some input"
            |
            |Please use one of the following commands:
            |
            |    type "next" to advance to the next question
            |    type "quit" to abort the quiz and exit
            |
        """.trimMargin())

        verifyNoMoreInteractions(printer)
        verifyZeroInteractions(application)
    }

    @Test
    fun `handle – chooses an answer option by letter`() {
        quizStorage.loadOrFail(quiz.id).start()

        startedHandler.handle(ChooseAnswerOptionUserIntent("B"))

        verify(printer).println("""
            |Current question: What is this interesting question?
            |
            | A. I don't see any question
            |[B] Interesting, indeed!
            |
            |    type "next" to advance to the next question
            |    type "quit" to abort the quiz and exit
            |
        """.trimMargin())

        verifyNoMoreInteractions(printer)
        verifyZeroInteractions(application)
    }

    @Test
    fun `handle – choosing answer will fail with not found if quiz is suddenly gone`() {
        nonExistingQuizHandler.handle(ChooseAnswerOptionUserIntent("B"))

        verify(printer).println("""
            |Error: Unable to find Quiz with id = non-existing
        """.trimMargin())

        verify(printer).println("""
            |Check the quiz id you’ve typed
        """.trimMargin())

        verify(application).requestQuit()

        verifyNoMoreInteractions(printer)
        verifyZeroInteractions(application)
    }

    @Test
    fun `handle – choosing answer will fail with an empty quiz`() {
        emptyQuizHandler.handle(ChooseAnswerOptionUserIntent("B"))

        verify(printer).println("""
            |Error: Quiz with id = ${emptyQuiz.id} has no questions
        """.trimMargin())

        verify(printer).println("""
            |Please inform your quiz admin about that error
        """.trimMargin())

        verify(application).requestQuit()

        verifyNoMoreInteractions(printer)
        verifyZeroInteractions(application)
    }

    @Test
    fun `handle – requests app to quit when user intends to quit`() {
        handler.handle(WantsQuitUserIntent())

        verify(application).requestQuit()

        verifyNoMoreInteractions(application)
        verifyZeroInteractions(printer)
    }

}