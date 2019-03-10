package business

import helper.*
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class `Oliver can see results` {

    @get:Rule
    val thrown = ExpectedException.none()

    @Test
    fun `perfect score`() {
        val quiz = quiz(questions = listOf(
                question("First question", answers = listOf(
                        answer("first answer").correct.chosen,
                        answer("second answer")
                )),
                question("Second question", answers = listOf(
                        answer("third answer"),
                        answer("fourth answer").correct.chosen
                ))
        ))
        val quizStorage = TestOnlyQuizStorage(listOf(quiz))
        val service = ScoringService(quizStorage)

        val results = service.score(quiz.id)

        assertEquals(ScoringResults(
                scored = 2,
                outOf = 2,
                incorrectAnswers = emptyList()
        ), results)
    }

    @Test
    fun `worst score`() {
        val answerOne = answer("first answer").correct
        val answerTwo = answer("second answer").chosen
        val questionOne = question("First question", answers = listOf(
                answerOne,
                answerTwo
        ))

        val answerThree = answer("third answer").chosen
        val answerFour = answer("fourth answer").correct
        val questionTwo = question("Second question", answers = listOf(
                answerThree,
                answerFour
        ))

        val quiz = quiz(questions = listOf(questionOne, questionTwo))
        val quizStorage = TestOnlyQuizStorage(listOf(quiz))
        val service = ScoringService(quizStorage)

        val results = service.score(quiz.id)

        assertEquals(ScoringResults(
                scored = 0,
                outOf = 2,
                incorrectAnswers = listOf(
                        IncorrectAnswer(
                                question = questionOne,
                                incorrect = answerTwo,
                                correct = answerOne
                        ),
                        IncorrectAnswer(
                                question = questionTwo,
                                incorrect = answerThree,
                                correct = answerFour
                        )
                )
        ), results)
    }

    @Test
    fun `quiz not found`() {
        val quizStorage = TestOnlyQuizStorage(emptyList())
        val service = ScoringService(quizStorage)

        thrown.expect(QuizNotFoundException::class.java)
        thrown.expectMessage("Unable to find Quiz with id = non-existing")

        service.score("non-existing")
    }

    @Test
    fun `did not choose the option`() {
        val correct = answer("correct").correct
        val questionOne = question("question", answers = listOf(
                correct,
                answer("other")
        ))
        val quiz = quiz(questions = listOf(
                questionOne
        ))
        val quizStorage = TestOnlyQuizStorage(listOf(quiz))
        val service = ScoringService(quizStorage)

        val results = service.score(quiz.id)

        assertEquals(ScoringResults(
                scored = 0,
                outOf = 1,
                incorrectAnswers = listOf(
                        IncorrectAnswer(
                                question = questionOne,
                                incorrect = answer(id = "", title = "NOT CHOSEN"),
                                correct = correct
                        )
                )
        ), results)
    }

    @Test
    fun `question without correct answer not possible`() {
        val question = question("question", answers = listOf(
                answer("incorrect 1"),
                answer("incorrect 2")
        ))
        val quiz = quiz(questions = listOf(question))
        val quizStorage = TestOnlyQuizStorage(listOf(quiz))
        val service = ScoringService(quizStorage)

        thrown.expect(QuestionHasNoCorrectAnswer::class.java)
        thrown.expectMessage(
                "The question with id = ${question.id} " +
                        "has no correct answer")

        service.score(quiz.id)
    }


    @Test
    fun `Quiz can tell which questions were answered correctly and not`() {
        val one = question("first question", answers = listOf(
                answer("correct option").correct.chosen,
                answer("other option")
        ))
        val two = question("second question", answers = listOf(
                answer("correct option").correct,
                answer("other option").chosen
        ))
        val three = question("third question", answers = listOf(
                answer("other option").chosen,
                answer("correct option").correct
        ))
        val four = question("fourth question", answers = listOf(
                answer("other option"),
                answer("correct option").correct.chosen
        ))

        val quiz = quiz(questions = listOf(one, two, three, four))

        assertEquals(listOf(one, four), quiz.answeredCorrectly())
        assertEquals(listOf(two, three), quiz.answeredIncorrectly())
    }
}