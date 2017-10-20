package business

class QuizNotFoundException(id: String)
    : RuntimeException("Unable to find Quiz with id = " + id)