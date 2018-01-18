package business

class ChooseAnswerOptionService(
        private val quizStorage: QuizStorage) {

    fun chooseAnswerOption(quizId: String, index: Int): Question {
        val quiz = quizStorage.load(quizId) ?:
                return Question("", "", emptyList())

        val currentQuestion = quiz.currentQuestion ?:
                return Question("", "", emptyList())

        currentQuestion.chooseAnswerOption(index)

        return currentQuestion
    }

}