package ui

class ResultsView(
        private val commandLinePrinter: CommandLinePrinter) {

    fun render() {
        commandLinePrinter.println("""
            |Your score is: 3 out of 3
        """.trimMargin())
    }

}