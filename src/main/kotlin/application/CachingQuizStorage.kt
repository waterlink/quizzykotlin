package application

import business.Quiz
import business.QuizStorage

class CachingQuizStorage(
        private val underlyingQuizStorage: QuizStorage)
    : QuizStorage {

    private val cache = mutableMapOf<String, Quiz>()

    override fun load(id: String): Quiz? {
        if (cache.containsKey(id)) {
            return cache[id]
        }

        val quiz = underlyingQuizStorage.load(id)
                ?: return null

        cache[id] = quiz

        return quiz
    }

}