package ui

class QuizCompletedView(
        private val commandLinePrinter: CommandLinePrinter) {

    fun render() {
        commandLinePrinter.println("""
            |Congratulations! You have completed the quiz!
            |
            |    type "results" to view your quiz results
            |    type "quit" to exit
            |
        """.trimMargin())
    }

}