package business

data class Question(val id: String,
                    val title: String,
                    val answerOptions: List<AnswerOption>) {
    fun chooseAnswerOption(index: Int) {
        answerOptions.forEach {
            it.isChosen = false
        }
        answerOptions[index].isChosen = true
    }
}