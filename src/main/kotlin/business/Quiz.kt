package business

class Quiz(val id: String,
           private val questions: List<Question>) {

    var currentQuestion: Question? = null

    private var questionIndex = 0

    fun start() {
        currentQuestion = questions.firstOrNull()
    }

    fun moveToNextQuestion() {
        questionIndex += 1
        currentQuestion = questions[questionIndex]
    }

    override fun toString(): String {
        return "Quiz(id='$id', questions=$questions)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Quiz

        if (id != other.id) return false
        if (questions != other.questions) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }


}