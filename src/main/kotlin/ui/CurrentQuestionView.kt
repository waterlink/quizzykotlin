package ui

class CurrentQuestionView(private val title: String) {
    fun render() {
        println("Current question: $title")
    }
}