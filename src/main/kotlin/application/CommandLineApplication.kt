package application

import business.Question
import business.QuizHasNoQuestionsException
import business.QuizNotFoundException
import business.StartQuizService
import ui.*

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

        val result: Question

        try {
            result = service.startQuiz(userIntent.quizId)
        } catch (e: QuizNotFoundException) {
            val errorView = QuizNotFoundView(e.message)
            errorView.render()
            return
        } catch (e: QuizHasNoQuestionsException) {
            val errorView = QuizHasNoQuestionsView(e.message)
            errorView.render()
            return
        }

        val view = CurrentQuestionView(result.title)
        view.render()
    }
}

