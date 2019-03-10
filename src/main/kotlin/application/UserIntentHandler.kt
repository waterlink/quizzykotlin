package application

import business.*
import ui.*
import ui.AnswerOptionLetters.letters

class UserIntentHandler(
        private val application: CommandLineApplication,
        private val printer: CommandLinePrinter,
        private val quizStorage: QuizStorage,
        initialQuizId: String = "") {

    private var quizId = initialQuizId

    fun handle(userIntent: UserIntent) {
        when (userIntent) {
            is StartQuizUserIntent -> handleStartQuiz(userIntent)
            is NextQuestionUserIntent -> handleNextQuestion(userIntent)
            is ShowResultsUserIntent -> handleShowResults(userIntent)
            is UnknownUserIntent -> handleUnknownUserIntent(userIntent)
            is ChooseAnswerOptionUserIntent ->
                handleChooseAnswerOptionUserIntent(userIntent)
            is WantsQuitUserIntent -> application.requestQuit()
            else -> throw RuntimeException("unknown user intent")
        }
    }

    private fun handleStartQuiz(userIntent: StartQuizUserIntent) {
        quizId = userIntent.quizId
        val result = startQuiz(userIntent) ?: return
        renderCurrentQuestion(result)
    }

    private fun handleNextQuestion(userIntent: NextQuestionUserIntent) {
        val result = moveToNextQuestion() ?: return
        renderCurrentQuestion(result)
    }

    private fun handleShowResults(userIntent: ShowResultsUserIntent) {
        val view = ResultsView(printer)
        view.render()
    }

    private fun handleChooseAnswerOptionUserIntent(
            userIntent: ChooseAnswerOptionUserIntent) {

        val firstLetterCode = letters.first().codePointAt(0)
        val letterCode = userIntent.letter.codePointAt(0)
        val index = letterCode - firstLetterCode

        val result = chooseAnswerOption(index) ?: return

        renderCurrentQuestion(result)

    }

    private fun handleUnknownUserIntent(userIntent: UnknownUserIntent) {
        val view = UnknownUserInputView(
                userInput = userIntent.userInput,
                commandLinePrinter = printer)
        view.render()
    }

    private fun startQuiz(userIntent: StartQuizUserIntent): Question? {
        val service = StartQuizService(quizStorage = quizStorage)

        try {
            return service.startQuiz(userIntent.quizId)
        } catch (e: QuizNotFoundException) {
            renderQuizNotFound(e)
        } catch (e: QuizHasNoQuestionsException) {
            renderQuizHasNoQuestions(e)
        }

        application.requestQuit()
        return null
    }

    private fun moveToNextQuestion(): Question? {
        val service = NextQuestionService(quizStorage = quizStorage)

        try {
            return service.moveToNextQuestion(quizId)
        } catch (e: QuizNotFoundException) {
            renderQuizNotFound(e)
            application.requestQuit()
        } catch (e: QuizCompletedException) {
            renderQuizCompleted()
        }

        return null
    }

    private fun chooseAnswerOption(index: Int): Question? {
        val service = ChooseAnswerOptionService(
                quizStorage = quizStorage)

        try {
            return service.chooseAnswerOption(quizId, index)
        } catch (e: QuizNotFoundException) {
            renderQuizNotFound(e)
        } catch (e: QuizHasNoQuestionsException) {
            renderQuizHasNoQuestions(e)
        }

        application.requestQuit()
        return null
    }

    private fun renderCurrentQuestion(result: Question) {
        val answerOptions = result.answerOptions.map {
            AnswerOptionPresentation(
                    title = it.title,
                    isChosen = it.isChosen
            )
        }

        val view = CurrentQuestionView(
                title = result.title,
                answerOptions = answerOptions,
                commandLinePrinter = printer)
        view.render()
    }

    private fun renderQuizCompleted() {
        val view = QuizCompletedView(
                commandLinePrinter = printer)
        view.render()
    }

    private fun renderQuizNotFound(e: QuizNotFoundException) {
        val errorView = QuizNotFoundView(
                message = e.message,
                commandLinePrinter = printer)
        errorView.render()
    }

    private fun renderQuizHasNoQuestions(e: QuizHasNoQuestionsException) {
        val errorView = QuizHasNoQuestionsView(
                message = e.message,
                commandLinePrinter = printer)
        errorView.render()
    }
}
