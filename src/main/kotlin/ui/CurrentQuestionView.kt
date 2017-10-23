package ui

class CurrentQuestionView(
        private val title: String,
        private val commandLinePrinter: CommandLinePrinter) {

    fun render() {
        commandLinePrinter.println("Current question: $title")
    }

}