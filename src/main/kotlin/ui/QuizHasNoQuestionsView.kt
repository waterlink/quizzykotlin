package ui

class QuizHasNoQuestionsView(private val message: String?) {

    fun render() {
        println("Error: $message")
        println("Please inform your quiz admin about that error")
    }

}