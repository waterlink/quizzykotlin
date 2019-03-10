package ui

class QuestionHasNoCorrectAnswerView(
        private val message: String?,
        private val commandLinePrinter: CommandLinePrinter
) {
    fun render() {
        commandLinePrinter.println("Error: $message")
        commandLinePrinter.println("Please inform your quiz admin about that error")
    }
}
