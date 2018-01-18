package ui

import ui.AnswerOptionLetters.letters

class CurrentQuestionView(
        private val title: String,
        private val answerOptions: List<AnswerOptionPresentation>,
        private val commandLinePrinter: CommandLinePrinter) {

    private val userCommandsPartialView = UserCommandsPartialView()

    fun render() {
        val renderedAnswerOptions = answerOptions
                .mapIndexed { index, option ->
                    "${formatLetter(index, option)} ${option.title}"
                }.joinToString("\n")

        commandLinePrinter.println("""
            |Current question: $title
            |
            |$renderedAnswerOptions
            |${userCommandsPartialView.render()}
        """.trimMargin())
    }

    private fun formatLetter(
            index: Int,
            option: AnswerOptionPresentation): String {

        val letter = letters[index]

        return if (option.isChosen) {
            "[$letter]"
        } else {
            " $letter."
        }

    }

}