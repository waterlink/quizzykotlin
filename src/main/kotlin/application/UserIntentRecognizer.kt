package application

import ui.*

class UserIntentRecognizer {
    fun recognize(userInput: String): UserIntent {
        return when (userInput) {
            "next" -> NextQuestionUserIntent()

            "results" -> ShowResultsUserIntent()

            "quit" -> WantsQuitUserIntent()

            else -> {
                if (AnswerOptionLetters.letters.contains(userInput)) {
                    ChooseAnswerOptionUserIntent(userInput)
                } else {
                    UnknownUserIntent(userInput)
                }
            }
        }
    }
}
