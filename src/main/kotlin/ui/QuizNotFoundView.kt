package ui

class QuizNotFoundView(private val message: String?) {

    fun render() {
        println("Error: $message")
        println("Check the quiz id you’ve typed")
    }

}