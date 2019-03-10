package application

import business.*
import ui.*

open class CommandLineApplication(
        private val args: Array<String>,
        private val commandLineUser: CommandLineUser,
        private val commandLinePrinter: CommandLinePrinter,
        underlyingQuizStorage: QuizStorage) {

    private val quizStorage: QuizStorage = CachingQuizStorage(
            underlyingQuizStorage = underlyingQuizStorage)

    private var wantsQuit = false

    open fun run() {
        val argsParser = CommandLineArgumentsParser()
        var userIntent: UserIntent = argsParser.parse(args)

        while (!wantsQuit) {

            handleUserIntent(userIntent)
            if (wantsQuit) return

            val userInput = commandLineUser.readCommand()

            userIntent = determineUserIntent(userInput)

        }
    }

    open fun requestQuit() {
        wantsQuit = true
    }

    private val userIntentHandler = UserIntentHandler(
            application = this,
            printer = commandLinePrinter,
            quizStorage = quizStorage
    )

    private fun handleUserIntent(userIntent: UserIntent) {
        userIntentHandler.handle(userIntent)
    }

    private val userIntentRecognizer = UserIntentRecognizer()

    private fun determineUserIntent(userInput: String): UserIntent {
        return userIntentRecognizer.recognize(userInput)
    }
}

