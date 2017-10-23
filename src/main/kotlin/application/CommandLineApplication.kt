package application

import business.*
import ui.*

class CommandLineApplication(
        private val args: Array<String>,
        private val commandLineUser: CommandLineUser,
        private val commandLinePrinter: CommandLinePrinter,
        private val quizStorage: QuizStorage) {

    private var wantsQuit = false

    fun run() {
        val argsParser = CommandLineArgumentsParser()
        var userIntent: UserIntent = argsParser.parse(args)

        while (!wantsQuit) {

            if (userIntent is StartQuizUserIntent) {
                handleStartQuiz(userIntent)
            }

            if (userIntent is NextQuestionUserIntent) {
                handleNextQuestion(userIntent)
            }

            val userInput = commandLineUser.readCommand()

            if (userInput == "next") {
                userIntent = NextQuestionUserIntent()
            }

            if (userInput == "quit") {
                wantsQuit = true
            }

        }
    }

    private fun handleStartQuiz(userIntent: StartQuizUserIntent) {
        val service = StartQuizService(quizStorage = quizStorage)

        val result: Question

        try {
            result = service.startQuiz(userIntent.quizId)
        } catch (e: QuizNotFoundException) {
            val errorView = QuizNotFoundView(
                    message = e.message,
                    commandLinePrinter = commandLinePrinter)
            errorView.render()
            wantsQuit = true
            return
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

    private fun handleNextQuestion(userIntent: NextQuestionUserIntent) {

    }
}

