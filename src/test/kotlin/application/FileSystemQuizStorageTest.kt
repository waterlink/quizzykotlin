package application

import business.Question
import business.Quiz
import business.QuizStorage
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File
import java.util.*

class FileSystemQuizStorageTest {

    private val quizId = UUID.randomUUID().toString()
    private val questionOneId = UUID.randomUUID().toString()
    private val questionTwoId = UUID.randomUUID().toString()

    @After
    fun tearDown() {
        val files = listOf(
                File("quizzyKotlinStorage/quizes/$quizId.json"),
                File("quizzyKotlinStorage/questions/$questionOneId.json"),
                File("quizzyKotlinStorage/questions/$questionTwoId.json"))

        files.forEach { it.delete() }
    }

    @Test
    fun `loading an existing quiz`() {
        // Given there is a quiz with some questions
        File("quizzyKotlinStorage/quizes/$quizId.json")
                .writeText("""
        {
            "id": "$quizId",
            "title": "Advanced Kotlin Quiz"
        }
                    """)

        File("quizzyKotlinStorage/questions/$questionOneId.json")
                .writeText("""
        {
            "id": "$questionOneId",
            "quizId": "$quizId",
            "title": "How to add your own methods on built-in classes?"
        }
                    """)

        File("quizzyKotlinStorage/questions/$questionTwoId.json")
                .writeText("""
        {
            "id": "$questionTwoId",
            "quizId": "$quizId",
            "title": "How to auto-replace with @Deprecated annotation?"
        }
                    """)

        val quizStorage: QuizStorage = FileSystemQuizStorage()

        // When I try to load that quiz
        val quiz = quizStorage.load(quizId)


        // Then I see that quiz with these questions
        val expectedQuestionOne = Question(
                id = questionOneId,
                title = "How to add your own methods on built-in classes?")
        val expectedQuestionTwo = Question(
                id = questionTwoId,
                title = "How to auto-replace with @Deprecated annotation?")
        val expectedQuestions = listOf(
                expectedQuestionOne,
                expectedQuestionTwo)
                .sortedBy { it.id }
        val expectedQuiz = Quiz(
                id = quizId,
                questions = expectedQuestions)

        assertEquals(expectedQuiz, quiz)
    }

    @Test
    fun `loading non-existing quiz`() {
        // Given there is no quiz with specific id
        val nonExistentId = "non-existent"
        val quizStorage: QuizStorage = FileSystemQuizStorage()

        // When I load quiz with that specific id
        val quiz = quizStorage.load(nonExistentId)

        // Then I receive no quiz
        assertEquals(null, quiz)
    }
}