package business

data class AnswerOption(val id: String,
                        val title: String,
                        var isChosen: Boolean = false,
                        var isCorrect: Boolean = false)