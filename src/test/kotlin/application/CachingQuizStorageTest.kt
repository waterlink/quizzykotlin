package application

import business.Quiz
import business.QuizStorage
import business.TestOnlyQuizStorage
import org.junit.Assert
import org.junit.Test
import java.util.*

class CachingQuizStorageTest {

    // Given there is one quiz in the quiz storage
    private val quiz = Quiz(
            id = UUID.randomUUID().toString(),
            questions = emptyList())

    private val quizStorage: QuizStorage = TestOnlyQuizStorage(
            quizes = listOf(quiz))

    private val cachingQuizStorage: QuizStorage = CachingQuizStorage(
            underlyingQuizStorage = quizStorage)

    @Test
    fun `first time 'load' just loads the object`() {
        // And I haven’t loaded the quiz with that id yet

        // When I load the quiz with that id
        val loadedQuiz = cachingQuizStorage.load(quiz.id)

        // Then I see the quiz with that id
        Assert.assertEquals(quiz, loadedQuiz)
    }

    @Test
    fun `second time 'load' returns the cached object`() {
        // And I have already loaded the quiz with that id
        val loadedQuiz = cachingQuizStorage.load(quiz.id)

        // When I load the quiz with that id again
        val loadedQuizAgain = cachingQuizStorage.load(quiz.id)

        // Then I see the same object as before
        Assert.assertSame(loadedQuiz, loadedQuizAgain)
    }
}

