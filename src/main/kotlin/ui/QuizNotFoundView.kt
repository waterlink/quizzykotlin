package ui

class QuizNotFoundView(
        private val message: String?,
        private val commandLinePrinter: CommandLinePrinter) {

    fun render() {
        commandLinePrinter.println("Error: $message")
        commandLinePrinter.println("Check the quiz id you’ve typed")
    }

}