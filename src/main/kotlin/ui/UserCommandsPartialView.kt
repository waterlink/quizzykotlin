package ui

class UserCommandsPartialView {
    fun render(): String {
        return """
            |
            |    type "next" to advance to the next question
            |    type "quit" to abort the quiz and exit
            |
        """.trimMargin()
    }
}