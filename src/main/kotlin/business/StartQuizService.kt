package business

class StartQuizService(private val quizStorage: QuizStorage) {

    fun startQuiz(id: String): Question {
        val quiz = quizStorage.load(id) ?: throw QuizNotFoundException(id)

        quiz.start()
        return quiz.currentQuestion!!
    }

}