package business

class QuestionHasNoCorrectAnswer(
        questionId: String
) : RuntimeException(
        "The question with id = $questionId has no correct answer"
)
