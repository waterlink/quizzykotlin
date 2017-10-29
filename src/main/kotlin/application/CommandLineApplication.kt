package application

import business.*
import ui.*

class CommandLineApplication(
        private val args: Array<String>,
        private val commandLineUser: CommandLineUser,
        private val commandLinePrinter: CommandLinePrinter,
        underlyingQuizStorage: QuizStorage) {

    private val quizStorage: QuizStorage = CachingQuizStorage(
            underlyingQuizStorage = underlyingQuizStorage)

    private var wantsQuit = false
    private var quizId = ""

    fun run() {
        val argsParser = CommandLineArgumentsParser()
        var userIntent: UserIntent = argsParser.parse(args)

        while (!wantsQuit) {

            handleUserIntent(userIntent)

            val userInput = commandLineUser.readCommand()

            userIntent = determineUserIntent(userInput)

        }
    }

    private fun handleUserIntent(userIntent: UserIntent) {
        when (userIntent) {
            is StartQuizUserIntent -> handleStartQuiz(userIntent)
            is NextQuestionUserIntent -> handleNextQuestion(userIntent)
            else -> throw RuntimeException("unknown user intent")
        }
    }

    private fun determineUserIntent(userInput: String): UserIntent {
        return when (userInput) {
            "next" -> NextQuestionUserIntent()

            "quit" -> {
                wantsQuit = true
                WantsQuitUserIntent()
            }

            else -> throw RuntimeException("unknown user input")
        }
    }

    private fun handleStartQuiz(userIntent: StartQuizUserIntent) {
        // save current quiz id
        quizId = userIntent.quizId

        // start quiz
        val service = StartQuizService(quizStorage = quizStorage)

        val result: Question

        try {
            result = service.startQuiz(userIntent.quizId)
        } catch (e: QuizNotFoundException) {

            // handle quiz not found error
            val errorView = QuizNotFoundView(
                    message = e.message,
                    commandLinePrinter = commandLinePrinter)
            errorView.render()
            wantsQuit = true

            return
        } catch (e: QuizHasNoQuestionsException) {

            // handle quiz has no questions error
            val errorView = QuizHasNoQuestionsView(
                    message = e.message,
                    commandLinePrinter = commandLinePrinter)
            errorView.render()
            wantsQuit = true

            return
        }

        // render current question view
        val view = CurrentQuestionView(
                title = result.title,
                commandLinePrinter = commandLinePrinter)
        view.render()
    }

    private fun

    private fun handleNextQuestion(userIntent: NextQuestionUserIntent) {
        val service = NextQuestionService(quizStorage = quizStorage)

        val result: Question

        try {
            result = service.moveToNextQuestion(quizId)
        } catch (e: QuizHasNoQuestionsException) {
            val errorView = QuizHasNoQuestionsView(
                    message = e.message,
                    commandLinePrinter = commandLinePrinter)
            errorView.render()
            wantsQuit = true
            return
        }

        val view = CurrentQuestionView(
                title = result.title,
                commandLinePrinter = commandLinePrinter)
        view.render()
    }
}