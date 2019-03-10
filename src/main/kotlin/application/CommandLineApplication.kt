package application

import business.*
import ui.*
import ui.AnswerOptionLetters.letters

open class CommandLineApplication(
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
            if (wantsQuit) return

            val userInput = commandLineUser.readCommand()

            userIntent = determineUserIntent(userInput)

        }
    }

    fun requestQuit() {
        wantsQuit = true
    }

    private fun handleUserIntent(userIntent: UserIntent) {
        when (userIntent) {
            is StartQuizUserIntent -> handleStartQuiz(userIntent)
            is NextQuestionUserIntent -> handleNextQuestion(userIntent)
            is ShowResultsUserIntent -> handleShowResults(userIntent)
            is UnknownUserIntent -> handleUnknownUserIntent(userIntent)
            is ChooseAnswerOptionUserIntent ->
                handleChooseAnswerOptionUserIntent(userIntent)
            is WantsQuitUserIntent -> requestQuit()
            else -> throw RuntimeException("unknown user intent")
        }
    }

    private val userIntentRecognizer = UserIntentRecognizer()

    private fun determineUserIntent(userInput: String): UserIntent {
        return userIntentRecognizer.recognize(userInput)
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
        val view = ResultsView(commandLinePrinter)
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
                commandLinePrinter = commandLinePrinter)
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

        wantsQuit = true
        return null
    }

    private fun moveToNextQuestion(): Question? {
        val service = NextQuestionService(quizStorage = quizStorage)

        try {
            return service.moveToNextQuestion(quizId)
        } catch (e: QuizHasNoQuestionsException) {
            renderQuizHasNoQuestions(e)
            wantsQuit = true
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

        wantsQuit = true
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
                commandLinePrinter = commandLinePrinter)
        view.render()
    }

    private fun renderQuizCompleted() {
        val view = QuizCompletedView(
                commandLinePrinter = commandLinePrinter)
        view.render()
    }

    private fun renderQuizNotFound(e: QuizNotFoundException) {
        val errorView = QuizNotFoundView(
                message = e.message,
                commandLinePrinter = commandLinePrinter)
        errorView.render()
    }

    private fun renderQuizHasNoQuestions(e: QuizHasNoQuestionsException) {
        val errorView = QuizHasNoQuestionsView(
                message = e.message,
                commandLinePrinter = commandLinePrinter)
        errorView.render()
    }
}

