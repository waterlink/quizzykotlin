package ui

import business.ScoringResults

class ResultsView(
        private val commandLinePrinter: CommandLinePrinter,
        private val results: ScoringResults) {

    fun render() {
        commandLinePrinter.println("""
            |Your score is: ${results.scored} out of ${results.outOf}
        """.trimMargin())

        val gotWrongQuestions = results.incorrectAnswers
                .joinToString("\n\n") {
                    """
                        |   Question: ${it.question.title}
                        |   Incorrect: ${it.incorrect.title}
                        |   Correct: ${it.correct.title}
                    """.trimMargin()
                }

        commandLinePrinter.println("""
            |You got ${results.outOf - results.scored} wrong questions:
            |
            |$gotWrongQuestions
            |
        """.trimMargin())
    }

}