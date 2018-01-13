package ui

class CurrentQuestionView(
        private val title: String,
        private val answerOptions: List<String>,
        private val commandLinePrinter: CommandLinePrinter) {

    private val letters = listOf(
            "A", "B", "C", "D", "E", "F", "G", "H")

    private val userCommandsPartialView = UserCommandsPartialView()

    fun render() {
        val renderedAnswerOptions = answerOptions
                .mapIndexed { index, option ->
                    "${letters[index]}. $option"
                }.joinToString("\n")

        commandLinePrinter.println("""
            |Current question: $title
            |
            |$renderedAnswerOptions
            |${userCommandsPartialView.render()}
        """.trimMargin())
    }

}