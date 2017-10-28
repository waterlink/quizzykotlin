package business

import org.junit.Assert.*
import org.junit.Test
import java.util.*

class TestOnlyQuizStorageTest {

    @Test
    fun `instantiates different object on every load`() {
        // Given I have already loaded a quiz object
        val quizOne = Quiz(
                id = UUID.randomUUID().toString(),
                questions = emptyList())

        val quizTwo = Quiz(
                id = UUID.randomUUID().toString(),
                questions = emptyList())

        val quizes = listOf(quizOne, quizTwo)
        val quizStorage = TestOnlyQuizStorage(quizes)

        val loadedQuiz = quizStorage.load(quizOne.id)

        // When I load a quiz object with the same id again
        val loadedAgainQuiz = quizStorage.load(quizOne.id)

        // Then these quiz objects are not same
        assertNotSame(loadedQuiz, loadedAgainQuiz)
    }
}