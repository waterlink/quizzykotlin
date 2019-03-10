package business

class ScoringService(private val quizStorage: QuizStorage) {
    companion object {
        private val NOT_CHOSEN = AnswerOption(
                id = "",
                title = "NOT CHOSEN"
        )
    }

    fun score(quizId: String): ScoringResults {
        val quiz = quizStorage.loadOrFail(quizId)

        val scored = quiz.answeredCorrectly().size
        val incorrectAnswers = quiz.answeredIncorrectly().map {
            IncorrectAnswer(
                    question = it,
                    incorrect = it.chosenAnswerOption() ?: NOT_CHOSEN,
                    correct = it.correctAnswerOption()
            )
        }

        return ScoringResults(
                scored = scored,
                outOf = quiz.questionCount(),
                incorrectAnswers = incorrectAnswers
        )
    }
}