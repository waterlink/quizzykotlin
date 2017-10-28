package business

class TestOnlyQuizStorage(
        private val quizes: List<Quiz>) : QuizStorage {

    override fun load(id: String): Quiz? {
        return quizes
                .find { it.id == id }
                ?.copy()
    }

}