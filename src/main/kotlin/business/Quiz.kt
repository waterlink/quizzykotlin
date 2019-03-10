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

        if (questionIndex >= questions.size) {
            throw QuizCompletedException()
        }

        currentQuestion = questions[questionIndex]
    }

    fun copy(): Quiz {
        return Quiz(
                id = id,
                questions = questions)
    }

    fun getCurrentQuestionOrFail(): Question {
        return currentQuestion ?: throw QuizHasNoQuestionsException(id)
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

    fun questionCount(): Int {
        return questions.size
    }

    fun answeredCorrectly(): List<Question> {
        return questions.filter {
            it.chosenAnswerOption() == it.correctAnswerOption()
        }
    }

    fun answeredIncorrectly(): List<Question> {
        return questions.filter {
            it.chosenAnswerOption() != it.correctAnswerOption()
        }
    }
}