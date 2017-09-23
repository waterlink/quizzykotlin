package application

import business.StartQuizService
import ui.CommandLineArgumentsParser
import ui.CurrentQuestionView
import ui.StartQuizUserIntent

class CommandLineApplication(private val args: Array<String>) {
    fun run() {
        val argsParser = CommandLineArgumentsParser()
        val userIntent = argsParser.parse(args)

        if (userIntent is StartQuizUserIntent) {
            return handleStartQuiz(userIntent)
        }
    }

    private fun handleStartQuiz(userIntent: StartQuizUserIntent) {
        val quizStorage = FileSystemQuizStorage()
        val service = StartQuizService(quizStorage = quizStorage)

        val result = service.startQuiz(userIntent.quizId)

        val view = CurrentQuestionView(result.title)
        view.render()
    }
}