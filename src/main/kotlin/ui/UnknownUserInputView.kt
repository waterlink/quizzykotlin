package ui

class UnknownUserInputView(
        private val commandLinePrinter: CommandLinePrinter) {

    fun render() {

        commandLinePrinter.println("""
            |Unknown user input "hello"
            |
            |Please use one of the following commands:
            |
            |    type "next" to advance to the next question
            |    type "quit" to abort the quiz and exit
            |
        """.trimMargin())
    }

}