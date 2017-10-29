package ui

class QuizCompletedView(
        private val commandLinePrinter: CommandLinePrinter) {

    fun render() {
        commandLinePrinter.println(
                "Congratulations! You have completed the quiz!")
    }

}