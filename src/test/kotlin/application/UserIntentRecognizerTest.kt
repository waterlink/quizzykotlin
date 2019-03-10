package application

import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Test
import ui.ChooseAnswerOptionUserIntent
import ui.NextQuestionUserIntent
import ui.ShowResultsUserIntent
import ui.UnknownUserIntent

class UserIntentRecognizerTest {
    private val recognizer = UserIntentRecognizer()

    @Test
    fun `recognize - upon "next" returns NextQuestionIntent`() {
        val intent = recognizer.recognize("next")
        assertThat(intent, instanceOf(NextQuestionUserIntent::class.java))
    }

    @Test
    fun `recognize - upon "results" returns ShowResultsUserIntent`() {
        val intent = recognizer.recognize("results")
        assertThat(intent, instanceOf(ShowResultsUserIntent::class.java))
    }

    @Test
    fun `recognize - upon "quit" returns WantsQuitUserIntent`() {
        val intent = recognizer.recognize("results")
        assertThat(intent, instanceOf(ShowResultsUserIntent::class.java))
    }

    @Test
    fun `recognize - upon "A" returns ChooseAnswerOptionUserIntent("A")`() {
        val intent = recognizer.recognize("A")
        val expected = ChooseAnswerOptionUserIntent("A")
        assertEquals(expected, intent)
    }

    @Test
    fun `recognize - upon "B" returns ChooseAnswerOptionUserIntent("B")`() {
        val intent = recognizer.recognize("B")
        val expected = ChooseAnswerOptionUserIntent("B")
        assertEquals(expected, intent)
    }

    @Test
    fun `recognize - upon unknown returns UnknownUserIntent(user input)`() {
        val intent = recognizer.recognize("unknown")
        val expected = UnknownUserIntent("unknown")
        assertEquals(expected, intent)
    }

    @Test
    fun `recognize - upon different unknown returns UnknownUserIntent(user input)`() {
        val intent = recognizer.recognize("different unknown")
        val expected = UnknownUserIntent("different unknown")
        assertEquals(expected, intent)
    }
}