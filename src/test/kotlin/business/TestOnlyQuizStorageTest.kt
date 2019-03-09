package business

import helper.quiz
import org.junit.Assert.assertNotSame
import org.junit.Test

class TestOnlyQuizStorageTest {

    @Test
    fun `instantiates different object on every load`() {
        // Given I have already loaded a quiz object
        val quizOne = quiz()
        val quizTwo = quiz()

        val quizes = listOf(quizOne, quizTwo)
        val quizStorage = TestOnlyQuizStorage(quizes)

        val loadedQuiz = quizStorage.load(quizOne.id)

        // When I load a quiz object with the same id again
        val loadedAgainQuiz = quizStorage.load(quizOne.id)

        // Then these quiz objects are not same
        assertNotSame(loadedQuiz, loadedAgainQuiz)
    }
}