package business

class NextQuestionService(private val quizStorage: QuizStorage) {

    fun moveToNextQuestion(quizId: String): Question {
        val quiz = quizStorage.loadOrFail(quizId)

        quiz.moveToNextQuestion()

        return quiz.currentQuestion ?:
                throw QuizHasNoQuestionsException(quizId)
    }

}