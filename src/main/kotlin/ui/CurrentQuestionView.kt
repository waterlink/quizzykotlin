package ui

import ui.AnswerOptionLetters.letters

class CurrentQuestionView(
        private val title: String,
        private val answerOptions: List<String>,
        private val commandLinePrinter: CommandLinePrinter) {

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