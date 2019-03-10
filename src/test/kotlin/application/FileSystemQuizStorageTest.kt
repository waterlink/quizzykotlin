package application

import business.AnswerOption
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
    private val answerOptionOneId = UUID.randomUUID().toString()
    private val answerOptionTwoId = UUID.randomUUID().toString()

    @After
    fun tearDown() {
        val files = listOf(
                File("quizzyKotlinStorage/quizes/$quizId.json"),
                File("quizzyKotlinStorage/questions/$questionOneId.json"),
                File("quizzyKotlinStorage/questions/$questionTwoId.json"),
                File("quizzyKotlinStorage/answerOptions/$answerOptionOneId.json"),
                File("quizzyKotlinStorage/answerOptions/$answerOptionTwoId.json"))

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
                title = "How to add your own methods on built-in classes?",
                answerOptions = emptyList())
        val expectedQuestionTwo = Question(
                id = questionTwoId,
                title = "How to auto-replace with @Deprecated annotation?",
                answerOptions = emptyList())
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

    @Test
    fun `loading quiz with question that has answer options`() {

        // Given a quiz with question with answer options
        File("quizzyKotlinStorage/quizes/$quizId.json")
                .writeText("""
                    |{
                    |   "id": "$quizId",
                    |   "title": "Quiz One"
                    |}
                """.trimMargin())

        File("quizzyKotlinStorage/questions/$questionOneId.json")
                .writeText("""
                    |{
                    |   "id": "$questionOneId",
                    |   "quizId": "$quizId",
                    |   "title": "Question One"
                    |}
                """.trimMargin())

        File("quizzyKotlinStorage/answerOptions/$answerOptionOneId.json")
                .writeText("""
                    |{
                    |   "id": "$answerOptionOneId",
                    |   "questionId": "$questionOneId",
                    |   "title": "Answer One",
                    |   "isCorrect": true
                    |}
                """.trimMargin())

        File("quizzyKotlinStorage/answerOptions/$answerOptionTwoId.json")
                .writeText("""
                    |{
                    |   "id": "$answerOptionTwoId",
                    |   "questionId": "$questionOneId",
                    |   "title": "Answer Two",
                    |   "isCorrect": false
                    |}
                """.trimMargin())

        val quizStorage: QuizStorage = FileSystemQuizStorage()

        // When I load that quiz
        val quiz = quizStorage.load(quizId)

        // Then I see quiz with question with answer options
        val expectedAnswerOptionOne = AnswerOption(
                id = answerOptionOneId,
                title = "Answer One",
                isCorrect = true)

        val expectedAnswerOptionTwo = AnswerOption(
                id = answerOptionTwoId,
                title = "Answer Two",
                isCorrect = false)

        val expectedAnswerOptions = listOf(
                expectedAnswerOptionOne,
                expectedAnswerOptionTwo)
                .sortedBy { it.id }

        val expectedQuestion = Question(
                id = questionOneId,
                title = "Question One",
                answerOptions = expectedAnswerOptions)

        val expectedQuiz = Quiz(
                id = quizId,
                questions = listOf(expectedQuestion))

        assertEquals(expectedQuiz, quiz)

    }
}