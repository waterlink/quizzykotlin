package business

class QuizHasNoQuestionsException(id: String)
    : RuntimeException("Quiz with id = $id has no questions")