package ui

class CurrentQuestionView(
        private val title: String,
        private val commandLinePrinter: CommandLinePrinter) {

    fun render() {
        commandLinePrinter.println("""
            |Current question: $title
            |
            |    type "next" to advance to the next question
            |    type "quit" to abort the quiz and exit
            |
        """.trimMargin())
    }

}