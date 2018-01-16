package business

data class AnswerOption(val id: String,
                        val title: String,
                        val isChosen: Boolean = true)