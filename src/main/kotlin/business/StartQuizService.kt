package business

class StartQuizService(private val quizStorage: QuizStorage) {

    fun startQuiz(id: String): Question {
        val quiz = quizStorage.load(id)
        quiz.start()
        return quiz.currentQuestion!!
    }

}