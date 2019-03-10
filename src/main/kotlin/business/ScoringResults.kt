package business

data class ScoringResults(
        val scored: Int,
        val outOf: Int,
        val incorrectAnswers: List<IncorrectAnswer>
)

data class IncorrectAnswer(
        val question: Question,
        val incorrect: AnswerOption,
        val correct: AnswerOption
)
