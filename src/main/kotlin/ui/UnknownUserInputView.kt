package ui

class UnknownUserInputView(
        private val commandLinePrinter: CommandLinePrinter) {

    fun render() {

        commandLinePrinter.println("""
            |Unknown user input
            |
            |    type "next" to advance to the next question
            |    type "quit" to abort the quiz and exit
            |
        """.trimMargin())
    }

}