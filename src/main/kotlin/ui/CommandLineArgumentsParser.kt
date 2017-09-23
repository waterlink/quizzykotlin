package ui

class CommandLineArgumentsParser {

    fun parse(args: Array<String>): StartQuizUserIntent {
        if (args.isEmpty()) {
            throw RuntimeException("Missing user intent argument")
        }

        val userIntentName = args[0]

        if (userIntentName == "start-quiz") {
            return parseStartQuizIntent(args)
        }

        throw RuntimeException("Unknown user intent. " +
                "Supported: start-quiz")
    }

    private fun parseStartQuizIntent(args: Array<String>)
            : StartQuizUserIntent {

        if (args.size != 2) {
            throw RuntimeException("start-quiz user intent " +
                    "should have only one argument – quiz id")
        }

        val quizId = args[1]

        return StartQuizUserIntent(quizId = quizId)
    }

}