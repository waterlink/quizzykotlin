package business

data class Question(val id: String,
                    val title: String,
                    val answerOptions: List<AnswerOption>) {
    fun chooseAnswerOption(index: Int) {
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

        answerOptions.forEach {
            it.isChosen = false
        }

        answerOptions[index].isChosen = true
    }
}