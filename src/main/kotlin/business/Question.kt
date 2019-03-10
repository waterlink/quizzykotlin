package business

data class Question(val id: String,
                    val title: String,
                    val answerOptions: List<AnswerOption>) {
    fun chooseAnswerOption(index: Int) {
        validateIndex(index)
        resetAllAnswerOptionsChosenState()
        answerOptions[index].isChosen = true
    }

    private fun resetAllAnswerOptionsChosenState() {
        answerOptions.forEach {
            it.isChosen = false
        }
    }

    private fun validateIndex(index: Int) {
        if (index < 0) {
            throw NoSuchAnswerOptionException(
                    "Answer option index can’t be < 0"
            )
        }

        if (index >= answerOptions.size) {
            throw NoSuchAnswerOptionException(
                    "Answer option index is too big"
            )
        }
    }

    fun chosenAnswerOption(): AnswerOption? {
        return answerOptions.find { it.isChosen }
    }

    fun correctAnswerOption(): AnswerOption {
        return answerOptions.find { it.isCorrect }
                ?: throw QuestionHasNoCorrectAnswer(id)
    }
}