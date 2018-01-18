package business

class ChooseAnswerOptionService(
        private val quizStorage: QuizStorage) {

    fun chooseAnswerOption(quizId: String, index: Int): Question {
        val quiz = quizStorage.load(quizId) ?:
                throw QuizNotFoundException(quizId)

        val currentQuestion = quiz.currentQuestion ?:
                throw QuizHasNoQuestionsException(quizId)

        currentQuestion.chooseAnswerOption(index)

        return currentQuestion
    }

}