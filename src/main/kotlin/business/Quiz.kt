package business

class Quiz(val id: String,
           private val questions: List<Question>) {

    var currentQuestion: Question? = null

    fun start() {
        currentQuestion = questions.first()
    }
}