package ui

class UnknownUserInputView(
        private val userInput: String,
        private val commandLinePrinter: CommandLinePrinter) {

    private val userCommandsPartialView = UserCommandsPartialView()

    fun render() {
        commandLinePrinter.println("""
            |Unknown user input "$userInput"
            |
            |Please use one of the following commands:
            |${userCommandsPartialView.render()}
        """.trimMargin())
    }

}