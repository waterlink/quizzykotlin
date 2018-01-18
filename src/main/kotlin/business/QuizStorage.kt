package business

interface QuizStorage {

    fun load(id: String): Quiz?

    fun loadOrFail(id: String): Quiz {
        return load(id) ?: throw QuizNotFoundException(id)
    }

}