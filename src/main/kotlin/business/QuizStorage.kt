package business

interface QuizStorage {

    fun load(id: String): Quiz

}